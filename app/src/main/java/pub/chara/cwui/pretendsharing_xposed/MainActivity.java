package pub.chara.cwui.pretendsharing_xposed;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

//桌面图标
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isXposedEnabled()){
            Toast.makeText(this,R.string.is_enabled,Toast.LENGTH_SHORT).show();
            if(!BuildConfig.DEBUG){
                try {
                    ComponentName targetActivity = new ComponentName(this.getPackageName(), this.getComponentName().getClassName());
                    PackageManager p = getPackageManager();
                        p.setComponentEnabledSetting(targetActivity, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                }catch (Exception e){
                    Toast.makeText(this,R.string.disable_error,Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            Toast.makeText(this,R.string.not_enabled,Toast.LENGTH_SHORT).show();
        }
        this.finish();
    }

    public boolean isXposedEnabled(){ //xposed会hook这个函数
        return false;
    }
}
