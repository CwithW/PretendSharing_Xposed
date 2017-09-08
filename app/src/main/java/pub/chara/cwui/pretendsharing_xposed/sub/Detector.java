package pub.chara.cwui.pretendsharing_xposed.sub;

import android.content.ComponentName;
import android.content.Intent;

import pub.chara.cwui.pretendsharing_xposed.Constants;

/**
 * Created by user on 2017/9/8.
 */

public class Detector {
    /**
     * 把检测函数提出来，
     * @param realIntent 原Intent
     * @return 如果是分享，返回分享名称，如果不是分享，返回null
     */
    private static final String[] TargetIntentSchemes = { //对于这些head
            "mqqapi", //qq
            "mqq", //旧qq
            "weixin", //旧微信
    };
    public static String whichShare(Intent realIntent){
            final String a; //temp
            final ComponentName b; //temp
            final String c; //temp
            if ((a = realIntent.getScheme()) != null) { //使用||的话会莫名其妙的空指针，改成遍历数组
                for(String each: TargetIntentSchemes) { //遍历数组
                    if (each.equals(a)) {
                        return each;
                    }
                }
            }

            else if((b = realIntent.getComponent()) != null){ //新微信
                final String packageName = b.getPackageName();
                final String className = b.getClassName(); //debug用的土方法
                if ("com.tencent.mm".equals(packageName) && "com.tencent.mm.plugin.base.stub.WXEntryActivity".equals(className)
                        && realIntent.getIntExtra("_wxapi_command_type",0) == 2 //命令1是调用微信登录，只有命令2是分享到微博
                        )
                    return "weixin";
            }

            /*else if((c = realIntent.getAction()) != null){
                if("com.sina.weibo.sdk.action.ACTION_WEIBO_ACTIVITY".equals(c))
                    return "weibo";
            }*/

        return null; //不是分享
    }
}
