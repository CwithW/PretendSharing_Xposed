package pub.chara.cwui.pretendsharing_xposed;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import pub.chara.cwui.pretendsharing_xposed.sub.Detector;
import pub.chara.cwui.pretendsharing_xposed.sub.QQ;
import pub.chara.cwui.pretendsharing_xposed.sub.Wechat;
import pub.chara.cwui.pretendsharing_xposed.sub.Weibo;

/**
 * Created by user on 2017/9/5.
 */

//假的Activity，会把通往原分享app的Intent劫持过来
public class FakeActivity extends Activity {
    private Intent fakeIntent;
    private Intent realIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setResult(RESULT_OK, null); //反正这个是没毛病
        fakeIntent = getIntent();
        realIntent = fakeIntent.getParcelableExtra(Constants.fromIntent);
        if (realIntent == null) //如果无法获取到包装Intent，则这个不是假装分享请求
            this.finish(); //再见
        showMsgBox(); //显示弹窗
    }

    //final函数会被自动优化，
    //这里把函数提出来只是方便观看
    private final void doPretendShare(){ //确定：执行假装分享
        doPretendShare(realIntent,this);
    }
    public final static void doPretendShare(Intent realIntent,Activity thisActivity){ //消息提取界面外部调用
        String which;
        if((which = Detector.whichShare(realIntent)) != null){ //虽然这里不可能为空了，还是做一下非空检测
            switch (which){
                case "mqqapi":
                case "mqq":
                    QQ.invoke(realIntent,thisActivity);
                    break;
                case "weixin":
                    Wechat.invoke(realIntent,thisActivity);
                    break;
                /*case "weibo":
                    Weibo.invoke(realIntent,thisActivity);
                    break;*/
            }
        }
    }

    private final void doRealShare(){ //取消：真的分享
        switch (realIntent.getIntExtra(Constants.whichForm,Constants.oneParam)) { //新版微信
            case Constants.threeParams: //Intent int Bundle
                startActivityForResult(realIntent,fakeIntent.getIntExtra(Constants.fromParam1,1),fakeIntent.getBundleExtra(Constants.fromParam2));
                break;
            case Constants.twoParams: //Intent int
                startActivity(realIntent,fakeIntent.getBundleExtra(Constants.fromParam1));
                break;
            case Constants.oneParam: //只有一个Intent
                startActivity(realIntent);
                break;
        }

    }

    private final void showMsgBox(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_Dialog); //设置主题
        alertDialog.setCancelable(true); //屠龙宝刀点击取消
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                FakeActivity.this.finish();
            }
        });
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) { //如果api>=17
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    FakeActivity.this.finish();
                }
            });
        }
        alertDialog.setTitle(R.string.title);
        alertDialog.setMessage(R.string.text);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() { //左
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doPretendShare();
                FakeActivity.this.finish();
            }
        });
        alertDialog.setNegativeButton(R.string.no,new DialogInterface.OnClickListener() { //右
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doRealShare();
                FakeActivity.this.finish();
            }
        });
        alertDialog.setNeutralButton(R.string.button_message_extract, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent temp = new Intent(FakeActivity.this,MessageExtractActivity.class);
                temp.putExtra(Constants.fromIntent,realIntent);
                startActivity(temp);
                //FakeActivity.this.finish();
            }
        });
        alertDialog.create().show();
    }
}
