package pub.chara.cwui.pretendsharing_xposed;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import pub.chara.cwui.pretendsharing_xposed.sub.QQ;
import pub.chara.cwui.pretendsharing_xposed.sub.Wechat;

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
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
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
        alertDialog.create().show();
    }

    //final函数会被自动优化，
    //这里把函数提出来只是方便观看
    private final void doPretendShare(){ //确定：执行假装分享
        String realScheme = realIntent.getScheme(); // head://这个头的head
        if (realScheme != null) {
            switch (realScheme) {
                case "mqqapi": //qq
                case "mqq": //旧qq
                    QQ.invoke(realIntent, this);
                    break;
                case "weixin": //旧微信
                    Wechat.invoke(realIntent, this);
                    break;
            }
        }
        if(realIntent.getComponent() != null
                && "com.tencent.mm".equals(realIntent.getComponent().getPackageName())
                && "com.tencent.mm.plugin.base.stub.WXEntryActivity".equals(realIntent.getComponent().getClassName())
                ){ //新微信
            Wechat.invoke(realIntent,this);
        }
    }
    private final void doRealShare(){ //取消：真的分享
        startActivityForResult(realIntent,fakeIntent.getIntExtra(Constants.fromParam1,1),fakeIntent.getBundleExtra(Constants.fromParam2));
    }
}
