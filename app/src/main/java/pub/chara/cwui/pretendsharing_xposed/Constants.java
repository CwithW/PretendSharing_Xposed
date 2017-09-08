package pub.chara.cwui.pretendsharing_xposed;

/**
 * Created by user on 2017/9/6.
 */
//常量类
    //现在不在xposed对面调用字符串了，这个类没什么用了
public class Constants {
    public static final String ThisPackageName = "pub.chara.cwui.pretendsharing_xposed";
    public static final String TAG = "testtag"; //log
    public static final String fromIntent = "djklfjiowejvdfjwe"; //防止重复,修改了架构以后这个已经不使用了
    public static final String fromParam1 = "dfzxcasdfasdawesd"; //startActivityForResult的第二个参数：int
    public static final String fromParam2 = "xcvjlsdkfjaweiofj"; //startActivityForResult的第三个参数:Bundle

    public static final String whichForm = "dfzxouwiojsdlfajf"; //新版微信使用startActivity而非startActivityForResult
    public static final int twoParams = 1; //Intent bundle
    public static final int threeParams = 2; // Intent int Bundle
    public static final int oneParam = 3; //只有一个Intent参数的startActivity

    @Deprecated
    public static final String Title = "假装分享";
    @Deprecated
    public static final String Text = "是否假装分享?";
    @Deprecated
    public static final String Yes = "是";
    @Deprecated
    public static final String No = "确定";
}
