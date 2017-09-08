package pub.chara.cwui.pretendsharing_xposed;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by user on 2017/9/5.
 */

public class utils {


    /**
     *  创建一个有标题，正文，两个按钮的对话框，并显示
     * @param c Context
     * @param Title 标题字样
     * @param Text 正文字样
     * @param PositiveText 确定按钮字样
     * @param NegativeText 否定按钮字样
     * @param PositiveClick 确定按钮点击接听器
     * @param NegativeClick 否定按钮点击接听器
     */
    public static final void MsgBox(Context c, String Title, String Text, String PositiveText, String NegativeText, DialogInterface.OnClickListener PositiveClick, final DialogInterface.OnClickListener NegativeClick){
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage(Text);
        builder.setTitle(Title);
        builder.setPositiveButton(PositiveText,PositiveClick);
        builder.setNegativeButton(NegativeText,NegativeClick);
        builder.setCancelable(false); //真的会卡住
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() { //然并卵还是会卡住
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                NegativeClick.onClick(null,0); //视同取消
            }
        });
        builder.create().show();
    }

    /**
     * 创建一个通向这个包的FakeActivity的Intent
     * @return 通向FakeActivity的Intent
     */
    public static final Intent generateFakeIntent(){
        Intent intent=new Intent();
        ComponentName cn=new ComponentName("pub.chara.cwui.pretendsharing_xposed",
                "pub.chara.cwui.pretendsharing_xposed.FakeActivity");
        intent.setComponent(cn);
        return intent;
    }

    //同上，但是直接在这一步把fromIntent塞进去
    //和第二个和第三个参数，便于重放
    public static final Intent generateFakeIntent( Intent fromIntent, int param1,Bundle param2){
        Intent intent=new Intent();
        ComponentName cn=new ComponentName("pub.chara.cwui.pretendsharing_xposed",
                "pub.chara.cwui.pretendsharing_xposed.FakeActivity");
        intent.setComponent(cn);
        intent.putExtra(Constants.fromIntent,fromIntent); //将fromIntent塞进去
        intent.putExtra(Constants.fromParam1,param1);
        intent.putExtra(Constants.fromParam2,param2);
        return intent;
    }

    /**
     * 创建一个通向这个包的FakeActivity的ComponentName
     * @return 通向FakeActivity的ComponentName
     */
    public static final ComponentName generateFakeComponentName(){
        return new ComponentName("pub.chara.cwui.pretendsharing_xposed",
                "pub.chara.cwui.pretendsharing_xposed.FakeActivity");
    }

    //从旧project里直接搬过来的函数
    public final static String[] goThroughBundleAsString(Bundle bundle){
        if(bundle==null)
            return new String[]{"null"};
        int i=0; //number
        ArrayList<String> tempList = new ArrayList<String>();
        Set<String> tempSet = bundle.keySet();
        for(String temp:tempSet){
            Object mTemp = bundle.get(temp);
            if(mTemp instanceof String){
                tempList.add(temp+"("+i+"<String>):"+mTemp);
            }else{
                if(mTemp != null)
                    tempList.add(temp+"("+i+"<"+mTemp.getClass()+">):"+String.valueOf(mTemp));
                else
                    tempList.add(temp+"("+i+"<"+"null"+">):"+"null");
            }
            i+=1;
        }
        String[] returnArray = new String[tempList.size()];
        tempList.toArray(returnArray);
        return returnArray;
    }
    public static String toString(Object[] a) {
        if (a == null)
            return "null";

        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        b.append("\n");
        for (int i = 0; ; i++) {
            b.append(String.valueOf(a[i]));
            if (i == iMax)
                return b.append(']').toString();
            b.append(", \n");
        }
    }
    public static void logIntent(Intent i){
        Log.e(Constants.TAG,"---START logIntent---");
        if(i==null){
            Log.e(Constants.TAG,"The Intent to log is null");
            return;
        }
        Log.e(Constants.TAG,"Intent Action:"+i.getAction());
        Log.e(Constants.TAG,"Intent Package:"+i.getPackage());
        Log.e(Constants.TAG,"Intent that:"+i.getComponent().getClassName());
        Log.e(Constants.TAG,"Intent dataString:"+i.getDataString());
        Log.e(Constants.TAG,"Intent flags:"+i.getFlags());
        if(i.getExtras()!=null)
            Log.e(Constants.TAG,"Intent Bundle things:"+toString(goThroughBundleAsString(i.getExtras())));
        else
            Log.e(Constants.TAG,"Intent Bundle things:"+"null");
        Log.e(Constants.TAG,"---END logIntent---");
    }
}
