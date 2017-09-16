package pub.chara.cwui.pretendsharing_xposed;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

/**
 * Created by user on 2017/9/11.
 */

public class MainSettingsActivity extends PreferenceActivity {
    @Override @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSharedPreferences(getPackageName() + "_preferences", MODE_WORLD_READABLE).edit().commit(); //将这个文件初始化成所有人可读的
        addPreferencesFromResource(R.xml.xml_main_settings);
        findPreference("show_help").setOnPreferenceClickListener(new Starter(HelpActivity.class));
        findPreference("all_apps").setOnPreferenceClickListener(new Starter(AppInfoActivity.class));
        findPreference("disable_this").setOnPreferenceChangeListener(new Disabler("JumpActivity"));
        findPreference("version").setSummary(utils.getAppVersionName(this));
        findPreference("version").setOnPreferenceClickListener(new EasterEgg());
        if(!isXposedEnabled()){
            Toast.makeText(this,R.string.not_enabled,Toast.LENGTH_SHORT).show();
        }
    }

    //就算写成World readable系统也会改掉
    //所以自己做一个写法
    @Override
    protected void onPause() {
        writeXposedSP(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        writeXposedSP(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        writeXposedSP(this);
        super.onBackPressed();
    }

    private static void writeXposedSP(Activity thisActivity){
        SharedPreferences a = thisActivity.getSharedPreferences(thisActivity.getPackageName() + "_preferences", MODE_WORLD_READABLE);
        SharedPreferences b = thisActivity.getSharedPreferences("xposed", MODE_WORLD_READABLE);
        SharedPreferences.Editor be = b.edit();
        be.putString("package_settings",a.getString("package_settings",""));
        be.putString("xposed_mode",a.getString("xposed_mode","0"));
        be.putBoolean("enable_debug",a.getBoolean("enable_debug",false));
        be.commit(); //防止系统销毁来不及保存怎么样
    }

    public static boolean isXposedEnabled(){ //xposed会hook这个函数
        return false;
    }

    private class Starter implements Preference.OnPreferenceClickListener{ //打开其他的Activity
        private Class mTargetClass;
        public Starter(Class targetClass){
            mTargetClass = targetClass;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            try{
                MainSettingsActivity.this.startActivity(new Intent(MainSettingsActivity.this,mTargetClass));
                return true;
            }catch(Exception e) {
                Toast.makeText(MainSettingsActivity.this,R.string.startActivity_error,Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }
    private class Disabler implements Preference.OnPreferenceChangeListener{ //禁用一个Activity
        private static final String packageName = Constants.ThisPackageName;
        private String ActivityName;
        public Disabler(String ActivityName){
            super();
            this.ActivityName = packageName+"."+ActivityName;
        }
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                ComponentName targetActivity = new ComponentName(packageName, ActivityName);
                PackageManager p = getPackageManager();
                if ((boolean) newValue) {
                    p.setComponentEnabledSetting(targetActivity, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                } else {
                    p.setComponentEnabledSetting(targetActivity, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                }
            }catch (Exception e){
                Toast.makeText(MainSettingsActivity.this,R.string.disable_error,Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
    }

    private class EasterEgg implements Preference.OnPreferenceClickListener{ //
        private int clickCount = 0;
        private int targetCount = 10;

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if(clickCount<targetCount)
                clickCount+=1;
            else {
                Uri uri = Uri.parse("https://www.bilibili.com/video/av5898201/");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
            return true;
        }
    }
}
