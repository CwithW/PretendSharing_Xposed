package pub.chara.cwui.pretendsharing_xposed;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by user on 2017/9/6.
 */

//用来测试的xposed,release编译会drop掉这个
//将这个类名加到assets/xposed_main里，可以在系统日志里看到每一个程序startActivity和startActivityForResult的Intent参数和setResult，便于调试
//查看log方法：
// adb shell
//su                    #我用的华为rom必须su才能看到全部日志
//logcat -c             #清除logcat缓存，非必需，有助于观看
//logcat -s testtag:*

    //正式编译之前记得从xposed_main里删掉引用

public class testxposed implements IXposedHookLoadPackage {
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if(loadPackageParam.appInfo != null && (loadPackageParam.appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0 ) {// 如果是系统应用
            //XposedBridge.log("Not hooking package for it's system app:"+ loadPackageParam.packageName );
            return; //不操作
        }

        //startActivity 0b
        //params: Intent Bundle
        XposedHelpers.findAndHookMethod("android.app.ContextImpl",loadPackageParam.classLoader,"startActivity", Intent.class,Bundle.class, new XC_MethodHook(){
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.e(Constants.TAG,"startActivity from Context 0");
                Context thisActivity = (Context)param.thisObject; //这里的是Context
                Intent thisTargetIntent = (Intent)param.args[0];
                Log.e(Constants.TAG,"this package:"+thisActivity.getApplicationInfo().packageName);
                //Log.e(Constants.TAG,"this ClassName:"+thisActivity.getComponentName().getClassName());
                Log.e(Constants.TAG,"target Intent Action:"+thisTargetIntent.getAction());
                ComponentName tempi = thisTargetIntent.getComponent();
                if(tempi!=null) {
                    Log.e(Constants.TAG, "target Intent Package:" + thisTargetIntent.getComponent().getPackageName());
                    Log.e(Constants.TAG, "target Intent ClassName:" + thisTargetIntent.getComponent().getClassName());
                }else {
                    Log.e(Constants.TAG, "target Intent Package:" + "null");
                    Log.e(Constants.TAG, "target Intent ClassName:" + "null");
                }
                if(thisTargetIntent.getDataString()!=null)
                    Log.e(Constants.TAG,"target Intent Uri:"+thisTargetIntent.getDataString());
                else
                    Log.e(Constants.TAG,"target Intent Uri:"+"null");
                Log.e(Constants.TAG,"target Intent bundle things:"+utils.toString(utils.goThroughBundleAsString(thisTargetIntent.getExtras())));
                Log.e(Constants.TAG,"startActivity 0 END");
                Log.e(Constants.TAG,"------");
            }
        });

        /* //not needed
        //startActivity A
        //params: Intent
        XposedHelpers.findAndHookMethod(Activity.class,"startActivity", Intent.class, new XC_MethodHook(){
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.e(Constants.TAG,"startActivity A");
                Activity thisActivity = (Activity)param.thisObject;
                Intent thisTargetIntent = (Intent)param.args[0];
                Log.e(Constants.TAG,"this package:"+thisActivity.getApplicationInfo().packageName);
                Log.e(Constants.TAG,"this ClassName:"+thisActivity.getComponentName().getClassName());
                Log.e(Constants.TAG,"target Intent Action:"+thisTargetIntent.getAction());
                ComponentName tempi = thisTargetIntent.getComponent();
                if(tempi!=null) {
                    Log.e(Constants.TAG, "target Intent Package:" + thisTargetIntent.getComponent().getPackageName());
                    Log.e(Constants.TAG, "target Intent ClassName:" + thisTargetIntent.getComponent().getClassName());
                }else {
                    Log.e(Constants.TAG, "target Intent Package:" + "null");
                    Log.e(Constants.TAG, "target Intent ClassName:" + "null");
                }
                if(thisTargetIntent.getDataString()!=null)
                    Log.e(Constants.TAG,"target Intent Uri:"+thisTargetIntent.getDataString());
                else
                    Log.e(Constants.TAG,"target Intent Uri:"+"null");
                Log.e(Constants.TAG,"target Intent bundle things:"+utils.toString(utils.goThroughBundleAsString(thisTargetIntent.getExtras())));
                Log.e(Constants.TAG,"startActivity A END");
                Log.e(Constants.TAG,"------");
            }
        });*/
        //startActivity B
        //params: Intent Bundle
        XposedHelpers.findAndHookMethod(Activity.class,"startActivity", Intent.class,Bundle.class, new XC_MethodHook(){
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.e(Constants.TAG,"startActivity B");
                Activity thisActivity = (Activity)param.thisObject;
                Intent thisTargetIntent = (Intent)param.args[0];
                Log.e(Constants.TAG,"this package:"+thisActivity.getApplicationInfo().packageName);
                Log.e(Constants.TAG,"this ClassName:"+thisActivity.getComponentName().getClassName());
                Log.e(Constants.TAG,"target Intent Action:"+thisTargetIntent.getAction());
                ComponentName tempi = thisTargetIntent.getComponent();
                if(tempi!=null) {
                    Log.e(Constants.TAG, "target Intent Package:" + thisTargetIntent.getComponent().getPackageName());
                    Log.e(Constants.TAG, "target Intent ClassName:" + thisTargetIntent.getComponent().getClassName());
                }else {
                    Log.e(Constants.TAG, "target Intent Package:" + "null");
                    Log.e(Constants.TAG, "target Intent ClassName:" + "null");
                }
                if(thisTargetIntent.getDataString()!=null)
                    Log.e(Constants.TAG,"target Intent Uri:"+thisTargetIntent.getDataString());
                else
                    Log.e(Constants.TAG,"target Intent Uri:"+"null");
                Log.e(Constants.TAG,"target Intent bundle things:"+utils.toString(utils.goThroughBundleAsString(thisTargetIntent.getExtras())));
                Log.e(Constants.TAG,"startActivity B END");
                Log.e(Constants.TAG,"------");
            }
        });
        //startActivityForResult B
        //params: Intent int Bundle
        XposedHelpers.findAndHookMethod(Activity.class,"startActivityForResult", Intent.class,int.class, Bundle.class, new XC_MethodHook(){
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.e(Constants.TAG,"startActivityForResult B");
                Activity thisActivity = (Activity)param.thisObject;
                Intent thisTargetIntent = (Intent)param.args[0];
                Log.e(Constants.TAG,"this package:"+thisActivity.getApplicationInfo().packageName);
                Log.e(Constants.TAG,"this ClassName:"+thisActivity.getComponentName().getClassName());
                Log.e(Constants.TAG, "request code2:"+String.valueOf(param.args[1]));
                Log.e(Constants.TAG,"target Intent Action:"+thisTargetIntent.getAction());
                ComponentName tempi = thisTargetIntent.getComponent();
                if(tempi!=null) {
                    Log.e(Constants.TAG, "target Intent Package:" + thisTargetIntent.getComponent().getPackageName());
                    Log.e(Constants.TAG, "target Intent ClassName:" + thisTargetIntent.getComponent().getClassName());
                }else {
                    Log.e(Constants.TAG, "target Intent Package:" + "null");
                    Log.e(Constants.TAG, "target Intent ClassName:" + "null");
                }
                if(thisTargetIntent.getDataString()!=null)
                    Log.e(Constants.TAG,"target Intent Uri:"+thisTargetIntent.getDataString());
                else
                    Log.e(Constants.TAG,"target Intent Uri:"+"null");
                Log.e(Constants.TAG,"target Intent bundle things:"+utils.toString(utils.goThroughBundleAsString(thisTargetIntent.getExtras())));
                Log.e(Constants.TAG,"startActivityForResult B END");
                Log.e(Constants.TAG,"------");
            }
        });
        //setResult A
        //params: int
        XposedHelpers.findAndHookMethod(Activity.class,"setResult", int.class, new XC_MethodHook(){
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.e(Constants.TAG,"setResult A");
                Activity thisActivity = (Activity)param.thisObject;
                Log.e(Constants.TAG,"this package:"+thisActivity.getApplicationInfo().packageName);
                Log.e(Constants.TAG,"this ClassName:"+thisActivity.getApplicationInfo().className);
                Log.e(Constants.TAG,"this calling package:"+thisActivity.getCallingPackage());
                ComponentName tempi = thisActivity.getCallingActivity();
                if(tempi!=null)
                    Log.e(Constants.TAG,"this calling Activity:"+thisActivity.getCallingActivity().getClassName());
                else
                    Log.e(Constants.TAG,"this calling Activity:"+"null");
                Log.e(Constants.TAG, "response code1:"+String.valueOf(param.args[0]));
                Log.e(Constants.TAG,"setResult A END");
                Log.e(Constants.TAG,"------");
            }
        });
        //setResult B
        //params: int Intent
        XposedHelpers.findAndHookMethod(Activity.class,"setResult", int.class,Intent.class, new XC_MethodHook(){
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.e(Constants.TAG,"setResult B");
                Activity thisActivity = (Activity)param.thisObject;
                Log.e(Constants.TAG,"this package:"+thisActivity.getApplicationInfo().packageName);
                Log.e(Constants.TAG,"this ClassName:"+thisActivity.getApplicationInfo().className);
                Log.e(Constants.TAG,"this calling package:"+thisActivity.getCallingPackage());
                ComponentName tempi = thisActivity.getCallingActivity();
                if(tempi!=null)
                    Log.e(Constants.TAG,"this calling Activity:"+thisActivity.getCallingActivity().getClassName());
                else
                    Log.e(Constants.TAG,"this calling Activity:"+"null");
                Intent thisIntent = (Intent)param.args[1];
                Log.e(Constants.TAG, "response code2:"+String.valueOf(param.args[0]));
                Bundle thisIntentBundle = thisIntent.getExtras();
                if(thisIntentBundle != null)
                    Log.e(Constants.TAG,"bundle things:"+ utils.toString(utils.goThroughBundleAsString(thisIntentBundle)));

                //Log.e("testtag", "response2 Intent bundle:"+thisIntent.getExtras().keySet());
                Log.e(Constants.TAG,"setResult B END");
                Log.e(Constants.TAG,"------");
            }
        });
    }

    class myXcMethodHookDebug extends XC_MethodHook{
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            Log.e(Constants.TAG,"successfully hooked function" + param.method.getName());
            Log.e(Constants.TAG,"All params:"+ utils.toString(param.args));
        }
    }
    
}
