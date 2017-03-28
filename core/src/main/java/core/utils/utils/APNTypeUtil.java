package core.utils.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 判断是否有网类
 * 该类需要android.permission.ACCESS_NETWORK_STATE 权限
 * 没有该权限将导致崩溃
 */
public class APNTypeUtil {
    public final static String TAG = "APNTypeUtil";

    public static int getAPNType(Context context) {
        int netType = -1;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
//				networkInfo.getExtraInfo()
            Log.e("getExtraInfo()", "networkInfo.getExtraInfo() is " + networkInfo.getExtraInfo());
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
//	                netType = CMNET;
                netType = 3;
                //net网络
            } else {
                netType = 2;
//	                wap网络
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = ConnectivityManager.TYPE_WIFI;
        }
        Log.e(TAG, "nettype" + netType);
        return netType;
    }
}
