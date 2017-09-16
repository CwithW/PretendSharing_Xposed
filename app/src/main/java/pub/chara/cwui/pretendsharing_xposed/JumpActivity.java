package pub.chara.cwui.pretendsharing_xposed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by user on 2017/9/15.
 */
//如果被打开，跳转到MainSettingsActivity
public class JumpActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this,MainSettingsActivity.class));
        this.finish();
    }
}
