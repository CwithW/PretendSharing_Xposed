package pub.chara.cwui.pretendsharing_xposed.sub;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by user on 2017/9/8.
 */

//建设中
public class Weibo {
    public static void invoke(Intent realIntent, Activity thisActivity){
        Intent launchBackIntent = realIntent; //直接使用它
        launchBackIntent.setAction("com.sina.weibo.sdk.action.ACTION_SDK_REQ_ACTIVITY");
        final String launchBackPackage = realIntent.getStringExtra("");
        //launchBackIntent.setPackage()


    }
}
