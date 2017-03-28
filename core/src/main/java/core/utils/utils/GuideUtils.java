package core.utils.utils;

import android.content.Context;

import core.utils.spreference.SharedPreferencesHelper;


/**
 * 当前类注释:查询与设置引导界面标志值工具类
 * 主要用于引导界面，APP新版本第一次打开 才会进行使用引导界面
 */
public class GuideUtils {
    private static final String KEY_GUIDE_ACTIVITY = "key_guide_activity";

    /**
     * 根据版本code值 判断是否已经引导过了
     *
     * @param context 上下文
     * @return 引导过返回true, 否则返回false
     */
    public static boolean isGuided(Context context) {
        AppInfoUtils utils = AppInfoUtils.getInstance(context);
        SharedPreferencesHelper mSharedPreferencesHelper = SharedPreferencesHelper.getInstance(context);
        return mSharedPreferencesHelper.getIntValue(KEY_GUIDE_ACTIVITY) == utils.getVersionCode();
    }

    /**
     * 设置code值，表明已经引导过
     *
     * @param context
     */
    public static void setIsGuided(Context context) {
        AppInfoUtils utils = AppInfoUtils.getInstance(context);
        SharedPreferencesHelper mSharedPreferencesHelper = SharedPreferencesHelper.getInstance(context);
        mSharedPreferencesHelper.putIntValue(KEY_GUIDE_ACTIVITY, utils.getVersionCode());
    }
}
