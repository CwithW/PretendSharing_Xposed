package pub.chara.cwui.pretendsharing_xposed;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

/**
 * Created by user on 2017/9/8.
 */

//信息提取
    //点击信息提取键转到这里
public class MessageExtractActivity extends Activity {
    private Intent realIntent;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_message_extract);
        realIntent = getIntent().getParcelableExtra(Constants.fromIntent);
        if(realIntent==null)
            this.finish();
        textView = findViewById(R.id.textView);
        parseRealIntent();


    }
    //菜单键
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(R.string.name_message_edit);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        Toast.makeText(this, R.string.not_feautred, Toast.LENGTH_SHORT).show();
        return true;
    }

    private void parseRealIntent(){
        String tempUriStr = realIntent.getDataString();
        if(tempUriStr != null && tempUriStr.startsWith("mqqapi://")) { //qq的参数是base64加密的
            Uri tempUri = realIntent.getData();
            Set<String> tempSet = tempUri.getQueryParameterNames();
            tempUriStr = tempUriStr + "\n" + getString(R.string.string_decoded) + "\n";
            for (String tempStr:tempSet){
                String tempStrGet = tempUri.getQueryParameter(tempStr);
                String tempBase64Decoded = "";
                try{
                    tempBase64Decoded = new String(Base64.decode(tempStrGet.replaceAll(" ", "+").getBytes(),Base64.DEFAULT));
                }catch (Exception e){
                    tempBase64Decoded = tempStrGet;
                }
                tempUriStr = tempUriStr + tempStr + ":" + tempBase64Decoded + "\n";
            }
        }
        String tempBundle = utils.toString(utils.goThroughBundleAsString(realIntent.getExtras()));
        textView.setText(String.valueOf(tempUriStr)+"\n"+tempBundle);
    }

    @Override
    public void onBackPressed() {
        FakeActivity.doPretendShare(realIntent,this);
        super.onBackPressed(); //=this.finish();
    }
}
