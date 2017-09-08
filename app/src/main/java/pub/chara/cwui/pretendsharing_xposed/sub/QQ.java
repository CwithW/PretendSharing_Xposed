package pub.chara.cwui.pretendsharing_xposed.sub;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

/**
 * Created by user on 2017/9/5.
 */

public class QQ {
    //复用了原假装分享的一些代码
    private static final String string_share = "share";

    /**
     *
     * @param realIntent 真的Intent
     * @param thatActivity FakeActivity.this
     */
    public static void invoke(Intent realIntent, Activity thatActivity){
        //相关qq sdk内代码：搜索mqqapi
        onButtonClick(realIntent,thatActivity);
    }

    private static void onButtonClick(Intent realIntent,Activity thatActivity) { //函数名称是因为原假装分享的原设计是点击按钮才分享
        final Intent iGet = realIntent;
        final Uri uGet = iGet.getData();//获取Uri
        if(null!=uGet)
        {
            if(!(string_share.equals(uGet.getHost()))) {
                Toast.makeText(thatActivity, "不是一个QQ分享请求", Toast.LENGTH_SHORT).show();
            }
            Intent launchBackIntent = new Intent();
            String pkgName = iGet.getStringExtra("pkg_name"); //返回包名
            launchBackIntent.setData(Uri.parse("tencent222222://tauth.qq.com/?#action=shareToQQ&result=complete&response={\"ret\":0}")); //返回结果
            launchBackIntent.setFlags(272629760); //?
            launchBackIntent.putExtra("fling_action_key",2); //?
            launchBackIntent.putExtra("preAct","LiteActivity"); //?
            launchBackIntent.putExtra("leftViewText","分享成功"); //?
            launchBackIntent.putExtra("fling_code_key",32253408); //?
            launchBackIntent.putExtra("preAct_time",1493381615748L); //?
            try{
                launchBackIntent.setComponent(new ComponentName(pkgName,"com.tencent.tauth.AuthActivity")); //根据腾讯sdk标准是要这么定义
                thatActivity.startActivity(launchBackIntent);
            }catch (Exception e){
                //Toast.makeText(thatActivity,pkgName,Toast.LENGTH_SHORT).show();
                irregularCallback(realIntent,thatActivity);
                return;
            }
            return;
        }else {
            Toast.makeText(thatActivity, "不是一个有效的动作", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private static void irregularCallback(Intent realIntent,Activity thatActivity){
        final Intent iGet = realIntent;
        Intent launchBackIntent = new Intent();
        String pkgName = iGet.getStringExtra("pkg_name");
        launchBackIntent.setData(Uri.parse("tencent222222://tauth.qq.com/?#action=shareToQQ&result=complete&response={\"ret\":0}"));
        launchBackIntent.setFlags(272629760);
        launchBackIntent.putExtra("fling_action_key",2);
        launchBackIntent.putExtra("preAct","LiteActivity");
        launchBackIntent.putExtra("leftViewText","分享成功");
        launchBackIntent.putExtra("fling_code_key",32253408);
        launchBackIntent.putExtra("preAct_time",1493381615748L);
        try{
            thatActivity.startActivity(launchBackIntent);
        }catch (Exception e){
            return;
        }
    }
}
