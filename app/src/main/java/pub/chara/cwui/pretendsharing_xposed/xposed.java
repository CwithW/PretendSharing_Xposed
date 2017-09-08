package pub.chara.cwui.pretendsharing_xposed;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.widget.Toast;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by user on 2017/9/5.
 */

public class xposed implements IXposedHookLoadPackage {
    //定义一些默认禁用的包名，如qq，微信等，hook他们自己的分享不会有好结果
    private static final String[] DisabledPackgeNames = {
            "android", //framework-res.apk好像不是系统属性
            "de.robv.android.xposed.installer", //xp安装器
            "com.tencent.mobileqq", //qq
            "com.tencent.qqlite", //qq轻聊版
            "com.tencent.tim", //tim
            "com.qzone", //qq空间
            "com.tencent.mm", //微信
            "com.sina.weibo", //微博，微博的第三方和lite都不支持分享
            "im.yixin" //易信
    };
    private static final String[] TargetIntentSchemes = { //对于这些head
            "mqqapi", //qq
            "mqq", //旧qq
            "weixin", //旧微信
    };
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if(Constants.ThisPackageName.equals(loadPackageParam.packageName)) {
            XposedHelpers.findAndHookMethod("pub.chara.cwui.pretendsharing_xposed.MainActivity", loadPackageParam.classLoader, "isXposedEnabled", XC_MethodReplacement.returnConstant(true));
            return; //不进一步操作
        }
        if(loadPackageParam.appInfo != null && (loadPackageParam.appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0 ) {// 如果是系统应用
            //XposedBridge.log("Not hooking package for it's system app:"+ loadPackageParam.packageName );
            return; //不操作，防止出现hook系统应用导致莫名其妙崩溃然后用户找上门的情况
        }//否则
        for(String packgeName: DisabledPackgeNames) {//遍历数组
            if (loadPackageParam.packageName.equals(packgeName)) { //如果是被禁用的包名
                //XposedBridge.log("Not hooking package for it's disabled app:"+ loadPackageParam.packageName );
                return; //不操作
            }
        } //否则，为非系统非禁用应用，开始作死
        //XposedBridge.log("Hooking package:" + loadPackageParam.packageName);
        //其实可以hook app本身分享sdk的方法
        //但是由于各种混淆加固和multidex，hook app本身方法的通用性会变低
        //但hook 安卓自带方法不会有此问题

        //在Api >=16时，
        //startActivityForResult的(Intent int)方法
        //跳转到startActivityForResult(Intent int Bundle:null)
        XposedHelpers.findAndHookMethod(Activity.class, "startActivityForResult", Intent.class, int.class, Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                //((Intent)param.args[0])
                //业务逻辑：
                //如果Intent满足条件，则取消原startActivityForResult的调用，
                //使用原Activity发送一个到这个包的FakeActivity的startActivity,将原Intent和param1-2作为参数带走
                //是否分享在FakeActivity处理
                /*测试区域
                if(((Intent)param.args[0]).getComponent() != null){
                    Toast.makeText((Activity)param.thisObject,((Intent)param.args[0]).getComponent().getPackageName()+";"+((Intent)param.args[0]).getComponent().getClassName(),Toast.LENGTH_SHORT).show();
                }
                测试区域*/
                if((!((Intent)param.args[0]).getBooleanExtra(Constants.fromIntent,false))) { //防止重复

                    final String a; //temp
                    final ComponentName b; //temp
                    if ((a = ((Intent)param.args[0]).getScheme()) != null) { //使用||的话会莫名其妙的空指针，改成遍历数组
                        for(String each: TargetIntentSchemes) { //遍历数组
                            if (each.equals(a)) {
                                doIt(param);
                                break;
                            }
                        }
                    }

                    else if((b = ((Intent)param.args[0]).getComponent()) != null){ //新微信
                        final String packageName = b.getPackageName();
                        final String className = b.getClassName(); //debug用的土方法
                        if ("com.tencent.mm".equals(packageName) && "com.tencent.mm.plugin.base.stub.WXEntryActivity".equals(className))
                            doIt(param);
                        //else
                        //   Toast.makeText((Activity)param.thisObject,packageName+";"+className,Toast.LENGTH_LONG).show();
                    }




                }
            }
      });
    }

    //因为不同的if结果都要调用这个代码段，
    //将这个代码段提出来能节省空间和脑力
    //多次调用的方法不应申请final
    private static void doIt(XC_MethodHook.MethodHookParam param){
        //由于某些原因，把param.args[0]直接修改成跳转到FakeActivity的话就是不会正常工作
        //所以要调用Activity自己启动一份，然后阻止原函数运行
        final Intent fromIntent = (Intent) ((Intent) param.args[0]).clone(); //由于是指向引用，必须复制一份
        ((Activity) param.thisObject).startActivity(utils.generateFakeIntent(fromIntent,(int)param.args[1],(Bundle)param.args[2]));
        param.setResult(null); //防止原函数被调用的小技巧
    }










    /* old
    private static final void doIt(XC_MethodHook.MethodHookParam param){
            //克隆对象消耗内存，仅在需要的时候执行
            final Intent fromIntent = (Intent) ((Intent) param.args[0]).clone();
            //而相对的，指向对象并不会实际消耗内存
            final Activity thisActivity = (Activity) param.thisObject; //当前的Activity，用来显示对话框
            final Context appContext = thisActivity.getApplicationContext(); //使用这个防止Activity被直接退出对话框无法显示
            final int paramArgs1 = (int) param.args[1];
            final Bundle paramArgs2 = (Bundle) param.args[2];
            utils.MsgBox(appContext, Constants.Title, Constants.Text, Constants.Yes, Constants.No, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent temp = utils.generateFakeIntent();
                    temp.putExtra(Constants.fromIntent, fromIntent);
                    appContext.startActivity(temp);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    fromIntent.putExtra(Constants.fromIntent, true);//防止重复
                    //Context不能startActivityForResult
                    thisActivity.startActivityForResult(fromIntent, paramArgs1, paramArgs2); //如果取消，执行原Intent
                }
            });
            param.setResult(null); //防止原函数被调用的小技巧
        }
        */

}
