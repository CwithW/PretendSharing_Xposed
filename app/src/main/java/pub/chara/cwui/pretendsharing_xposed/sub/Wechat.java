package pub.chara.cwui.pretendsharing_xposed.sub;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.security.MessageDigest;

/**
 * Created by user on 2017/9/6.
 */

public class Wechat {
    //依旧偷懒
    public static void invoke(Intent realIntent, Activity thatActivity) {
        String calling = realIntent.getStringExtra("_mmessage_appPackage");
        if(calling == null||"".equals(calling)){
            calling = thatActivity.getCallingPackage(); //因为我们不是微信 无法获取app包名
            if("".equals(calling) || calling == null){ //如果两种方法都失败
                Toast.makeText(thatActivity,"抱歉，这个APP不能被伪装",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        String callBackClass = calling + ".wxapi.WXEntryActivity";
        ComponentName callBackName = new ComponentName(calling,callBackClass);
        Intent callBackIntent = new Intent();
        callBackIntent.putExtra("wx_token_key","com.tencent.mm.openapi.token"); //奇怪的来自微信验证
        String stringExtra = "辣鸡腾讯坑我钱财";
        int intExtra = 587333634;
        String stringExtra2 = "com.tencent.mm";//微信验证用
        byte[] wechatSign = wechatSign(stringExtra,intExtra,stringExtra2);//微信验证
        callBackIntent.putExtra("_mmessage_content",stringExtra); //消息内容？
        callBackIntent.putExtra("_mmessage_sdkVersion",intExtra); //微信版本.暂时回复常数 未来可能从微信获取
        callBackIntent.putExtra("_mmessage_appPackage",stringExtra2); //微信包名？
        callBackIntent.putExtra("_mmessage_checksum",wechatSign); //checksum
        callBackIntent.putExtra("_wxapi_command_type",realIntent.getIntExtra("_wxapi_command_type",0)); //命令类型。还给你
        callBackIntent.putExtra("_wxapi_baseresp_errcode",0); //错误码/是否成功。0：成功
        callBackIntent.putExtra("_wxapi_baseresp_transaction","1#sep#4#sep#4884235#sep#1504783117034"); //未知
        callBackIntent.setFlags(272629760);
        try{
            callBackIntent.setComponent(callBackName);
            thatActivity.startActivity(callBackIntent);
        }catch (Exception e){
            Toast.makeText(thatActivity,"包"+calling+"没有会调活动项。",Toast.LENGTH_SHORT).show();
            return;
        }
            //Toast.makeText(this,R.string.msg_wechat_share_success,Toast.LENGTH_SHORT).show();
        return;
    }
    //微信sdk的checksum机制
    //怎么得到的？我猜出来的
    private final static byte[] wechatSign(String str, int i, String str2) {
        StringBuffer stringBuffer = new StringBuffer();
        if (str != null) {
            stringBuffer.append(str);
        }
        stringBuffer.append(i);
        stringBuffer.append(str2);
        stringBuffer.append("mMcShCsTr");
        return m33a(stringBuffer.toString().substring(1, 9).getBytes()).getBytes();
    }
    public static final String m33a(byte[] bArr) {
        int i = 0;
        char[] cArr = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(bArr);
            byte[] digest = instance.digest();
            int length = digest.length;
            char[] cArr2 = new char[(length * 2)];
            int i2 = 0;
            while (i < length) {
                byte b = digest[i];
                int i3 = i2 + 1;
                cArr2[i2] = cArr[(b >>> 4) & 15];
                i2 = i3 + 1;
                cArr2[i3] = cArr[b & 15];
                i++;
            }
            return new String(cArr2);
        } catch (Exception e) {
            return null;
        }
    }

}
