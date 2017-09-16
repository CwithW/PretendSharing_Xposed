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


//用来测试的类
    //如果调试打开，会invoke这个类
public class testxposed {
    public static void invoke(XC_LoadPackage.LoadPackageParam loadPackageParam){
        XposedBridge.log("PretendSharing_Xposed Debug: Hooking package :"+ loadPackageParam.packageName);
        //startActivity 0b
        //params: Intent Bundle
        XposedHelpers.findAndHookMethod("android.app.ContextImpl",loadPackageParam.classLoader,"startActivity", Intent.class,Bundle.class, new XC_MethodHook(){
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                XposedBridge.log(Constants.TAG + ":" +"startActivity from Context 0");
                Context thisActivity = (Context)param.thisObject; //这里的是Context
                Intent thisTargetIntent = (Intent)param.args[0];
                XposedBridge.log(Constants.TAG + ":" +"this package:"+thisActivity.getApplicationInfo().packageName);
                //XposedBridge.log(Constants.TAG + ":" +"this ClassName:"+thisActivity.getComponentName().getClassName());
                XposedBridge.log(Constants.TAG + ":" +"target Intent Action:"+thisTargetIntent.getAction());
                ComponentName tempi = thisTargetIntent.getComponent();
                if(tempi!=null) {
                    XposedBridge.log(Constants.TAG + ":" + "target Intent Package:" + thisTargetIntent.getComponent().getPackageName());
                    XposedBridge.log(Constants.TAG + ":" + "target Intent ClassName:" + thisTargetIntent.getComponent().getClassName());
                }else {
                    XposedBridge.log(Constants.TAG + ":" + "target Intent Package:" + "null");
                    XposedBridge.log(Constants.TAG + ":" + "target Intent ClassName:" + "null");
                }
                if(thisTargetIntent.getDataString()!=null)
                    XposedBridge.log(Constants.TAG + ":" +"target Intent Uri:"+thisTargetIntent.getDataString());
                else
                    XposedBridge.log(Constants.TAG + ":" +"target Intent Uri:"+"null");
                XposedBridge.log(Constants.TAG + ":" +"target Intent bundle things:"+utils.toString(utils.goThroughBundleAsString(thisTargetIntent.getExtras())));
                XposedBridge.log(Constants.TAG + ":" +"startActivity 0 END");
                XposedBridge.log(Constants.TAG + ":" +"------");
            }
        });

        /* //not needed
        //startActivity A
        //params: Intent
        XposedHelpers.findAndHookMethod(Activity.class,"startActivity", Intent.class, new XC_MethodHook(){
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                XposedBridge.log(Constants.TAG + ":" +"startActivity A");
                Activity thisActivity = (Activity)param.thisObject;
                Intent thisTargetIntent = (Intent)param.args[0];
                XposedBridge.log(Constants.TAG + ":" +"this package:"+thisActivity.getApplicationInfo().packageName);
                XposedBridge.log(Constants.TAG + ":" +"this ClassName:"+thisActivity.getComponentName().getClassName());
                XposedBridge.log(Constants.TAG + ":" +"target Intent Action:"+thisTargetIntent.getAction());
                ComponentName tempi = thisTargetIntent.getComponent();
                if(tempi!=null) {
                    XposedBridge.log(Constants.TAG + ":" + "target Intent Package:" + thisTargetIntent.getComponent().getPackageName());
                    XposedBridge.log(Constants.TAG + ":" + "target Intent ClassName:" + thisTargetIntent.getComponent().getClassName());
                }else {
                    XposedBridge.log(Constants.TAG + ":" + "target Intent Package:" + "null");
                    XposedBridge.log(Constants.TAG + ":" + "target Intent ClassName:" + "null");
                }
                if(thisTargetIntent.getDataString()!=null)
                    XposedBridge.log(Constants.TAG + ":" +"target Intent Uri:"+thisTargetIntent.getDataString());
                else
                    XposedBridge.log(Constants.TAG + ":" +"target Intent Uri:"+"null");
                XposedBridge.log(Constants.TAG + ":" +"target Intent bundle things:"+utils.toString(utils.goThroughBundleAsString(thisTargetIntent.getExtras())));
                XposedBridge.log(Constants.TAG + ":" +"startActivity A END");
                XposedBridge.log(Constants.TAG + ":" +"------");
            }
        });*/
        //startActivity B
        //params: Intent Bundle
        XposedHelpers.findAndHookMethod(Activity.class,"startActivity", Intent.class,Bundle.class, new XC_MethodHook(){
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                XposedBridge.log(Constants.TAG + ":" +"startActivity from Activity B");
                Activity thisActivity = (Activity)param.thisObject;
                Intent thisTargetIntent = (Intent)param.args[0];
                XposedBridge.log(Constants.TAG + ":" +"this package:"+thisActivity.getApplicationInfo().packageName);
                XposedBridge.log(Constants.TAG + ":" +"this ClassName:"+thisActivity.getComponentName().getClassName());
                XposedBridge.log(Constants.TAG + ":" +"target Intent Action:"+thisTargetIntent.getAction());
                ComponentName tempi = thisTargetIntent.getComponent();
                if(tempi!=null) {
                    XposedBridge.log(Constants.TAG + ":" + "target Intent Package:" + thisTargetIntent.getComponent().getPackageName());
                    XposedBridge.log(Constants.TAG + ":" + "target Intent ClassName:" + thisTargetIntent.getComponent().getClassName());
                }else {
                    XposedBridge.log(Constants.TAG + ":" + "target Intent Package:" + "null");
                    XposedBridge.log(Constants.TAG + ":" + "target Intent ClassName:" + "null");
                }
                if(thisTargetIntent.getDataString()!=null)
                    XposedBridge.log(Constants.TAG + ":" +"target Intent Uri:"+thisTargetIntent.getDataString());
                else
                    XposedBridge.log(Constants.TAG + ":" +"target Intent Uri:"+"null");
                XposedBridge.log(Constants.TAG + ":" +"target Intent bundle things:"+utils.toString(utils.goThroughBundleAsString(thisTargetIntent.getExtras())));
                XposedBridge.log(Constants.TAG + ":" +"startActivity B END");
                XposedBridge.log(Constants.TAG + ":" +"------");
            }
        });
        //startActivityForResult B
        //params: Intent int Bundle
        XposedHelpers.findAndHookMethod(Activity.class,"startActivityForResult", Intent.class,int.class, Bundle.class, new XC_MethodHook(){
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                XposedBridge.log(Constants.TAG + ":" +"startActivityForResult from Activity B");
                Activity thisActivity = (Activity)param.thisObject;
                Intent thisTargetIntent = (Intent)param.args[0];
                XposedBridge.log(Constants.TAG + ":" +"this package:"+thisActivity.getApplicationInfo().packageName);
                XposedBridge.log(Constants.TAG + ":" +"this ClassName:"+thisActivity.getComponentName().getClassName());
                XposedBridge.log(Constants.TAG + ":" + "request code2:"+String.valueOf(param.args[1]));
                XposedBridge.log(Constants.TAG + ":" +"target Intent Action:"+thisTargetIntent.getAction());
                ComponentName tempi = thisTargetIntent.getComponent();
                if(tempi!=null) {
                    XposedBridge.log(Constants.TAG + ":" + "target Intent Package:" + thisTargetIntent.getComponent().getPackageName());
                    XposedBridge.log(Constants.TAG + ":" + "target Intent ClassName:" + thisTargetIntent.getComponent().getClassName());
                }else {
                    XposedBridge.log(Constants.TAG + ":" + "target Intent Package:" + "null");
                    XposedBridge.log(Constants.TAG + ":" + "target Intent ClassName:" + "null");
                }
                if(thisTargetIntent.getDataString()!=null)
                    XposedBridge.log(Constants.TAG + ":" +"target Intent Uri:"+thisTargetIntent.getDataString());
                else
                    XposedBridge.log(Constants.TAG + ":" +"target Intent Uri:"+"null");
                XposedBridge.log(Constants.TAG + ":" +"target Intent bundle things:"+utils.toString(utils.goThroughBundleAsString(thisTargetIntent.getExtras())));
                XposedBridge.log(Constants.TAG + ":" +"startActivityForResult B END");
                XposedBridge.log(Constants.TAG + ":" +"------");
            }
        });
        //setResult A
        //params: int
        XposedHelpers.findAndHookMethod(Activity.class,"setResult", int.class, new XC_MethodHook(){
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                XposedBridge.log(Constants.TAG + ":" +"setResult A");
                Activity thisActivity = (Activity)param.thisObject;
                XposedBridge.log(Constants.TAG + ":" +"this package:"+thisActivity.getApplicationInfo().packageName);
                XposedBridge.log(Constants.TAG + ":" +"this ClassName:"+thisActivity.getApplicationInfo().className);
                XposedBridge.log(Constants.TAG + ":" +"this calling package:"+thisActivity.getCallingPackage());
                ComponentName tempi = thisActivity.getCallingActivity();
                if(tempi!=null)
                    XposedBridge.log(Constants.TAG + ":" +"this calling Activity:"+thisActivity.getCallingActivity().getClassName());
                else
                    XposedBridge.log(Constants.TAG + ":" +"this calling Activity:"+"null");
                XposedBridge.log(Constants.TAG + ":" + "response code1:"+String.valueOf(param.args[0]));
                XposedBridge.log(Constants.TAG + ":" +"setResult A END");
                XposedBridge.log(Constants.TAG + ":" +"------");
            }
        });
        //setResult B
        //params: int Intent
        XposedHelpers.findAndHookMethod(Activity.class,"setResult", int.class,Intent.class, new XC_MethodHook(){
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                XposedBridge.log(Constants.TAG + ":" +"setResult B");
                Activity thisActivity = (Activity)param.thisObject;
                XposedBridge.log(Constants.TAG + ":" +"this package:"+thisActivity.getApplicationInfo().packageName);
                XposedBridge.log(Constants.TAG + ":" +"this ClassName:"+thisActivity.getApplicationInfo().className);
                XposedBridge.log(Constants.TAG + ":" +"this calling package:"+thisActivity.getCallingPackage());
                ComponentName tempi = thisActivity.getCallingActivity();
                if(tempi!=null)
                    XposedBridge.log(Constants.TAG + ":" +"this calling Activity:"+thisActivity.getCallingActivity().getClassName());
                else
                    XposedBridge.log(Constants.TAG + ":" +"this calling Activity:"+"null");
                Intent thisIntent = (Intent)param.args[1];
                XposedBridge.log(Constants.TAG + ":" + "response code2:"+String.valueOf(param.args[0]));
                Bundle thisIntentBundle = thisIntent.getExtras();
                if(thisIntentBundle != null)
                    XposedBridge.log(Constants.TAG + ":" +"bundle things:"+ utils.toString(utils.goThroughBundleAsString(thisIntentBundle)));

                //Log.e("testtag", "response2 Intent bundle:"+thisIntent.getExtras().keySet());
                XposedBridge.log(Constants.TAG + ":" +"setResult B END");
                XposedBridge.log(Constants.TAG + ":" +"------");
            }
        });
    }

    class myXcMethodHookDebug extends XC_MethodHook{
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            XposedBridge.log(Constants.TAG + ":" +"successfully hooked function" + param.method.getName());
            XposedBridge.log(Constants.TAG + ":" +"All params:"+ utils.toString(param.args));
        }
    }
    
}
