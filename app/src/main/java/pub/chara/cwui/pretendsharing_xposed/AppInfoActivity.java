package pub.chara.cwui.pretendsharing_xposed;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2017/9/15.
 */

public class AppInfoActivity extends Activity {
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_app_info);
        list = (ListView) findViewById(R.id.lstApps);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Open settings activity when clicking on an application
                String pkgName = ((TextView) view.findViewById(R.id.info)).getText().toString();
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(pkgName);
                Toast.makeText(AppInfoActivity.this, R.string.msg_cp_success, Toast.LENGTH_SHORT).show();
            }
        });
        refreshApps();
    }
    private void refreshApps() {
        // (re)load the list of apps in the background
        final PackageManager pm = getPackageManager();
        List<PackageInfo> pkgs = getPackageManager().getInstalledPackages(0);
        List<Map<String,Object>> returnBackList = new ArrayList<Map<String, Object>>();
        for(PackageInfo i:pkgs){
            if((i.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0 )
                continue; //跳过系统应用
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", String.valueOf(i.applicationInfo.loadLabel(pm)));
            map.put("info", String.valueOf(i.applicationInfo.packageName));
            map.put("img", i.applicationInfo.loadIcon(pm));
            returnBackList.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,returnBackList,R.layout.vlist,
                new String[]{"title","info","img"},
                new int[]{R.id.title,R.id.info,R.id.img});
        list.setAdapter(adapter);
    }


}
