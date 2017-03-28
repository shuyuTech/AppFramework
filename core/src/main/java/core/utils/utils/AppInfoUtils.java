package core.utils.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

/**
 * 项目 BaseApp
 * 包名 com.huan.studyol.utils
 * 主机 Administrator
 * 作者 刘欢
 * 邮箱 771383629@qq.com
 * 时间 2016/02/02 15:19
 * 注释
 */
public class AppInfoUtils {
    private static AppInfoUtils mInstance;
    private Context context;

    private AppInfoUtils(Context context) {
        this.context = context;
    }

    public static AppInfoUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (AppInfoUtils.class) {
                if (mInstance == null) {
                    mInstance = new AppInfoUtils(context);
                }
            }
        }

        return mInstance;
    }

    /**
     * 取得应用的版本号.
     *
     * @return 应用程序的版本号.
     */
    public int getVersionCode() {
        //取得包管理器的对象，这样就可以拿到应用程序的管理对象
        PackageManager pm = context.getPackageManager();
        try {
            //得到应用程序的包信息对象
            PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(), 0);
            //取得应用程序的版本号
            return pinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            //此异常不会发生
            return 1;
        }
    }

    public String getDevicesId() {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getDeviceId();
    }
}
