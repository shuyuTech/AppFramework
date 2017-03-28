package core.utils.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import core.base.BaseApplication;
import core.utils.utils.logger.Logger;

/**
 * 当前类注释:统一工具类 包括Android平台开发中需要用到的字符串判断,编码判断转化,字符字符串字节转换,
 * 系统各种信息的获取等等(屏幕分辨率,设备号,imei,imsi,系统版本,操作系统,cpu,IP,时间处理,域名,平台等等)
 */
@SuppressLint({"SimpleDateFormat", "DefaultLocale"})
public class StrUtils {
    public static final String orange = "#EF8100";
    public static final String orange_dark = "#EA8918";
    public static final String green = "#1aa607";
    public static final String blue = "#770000ff";
    /**
     * 获取当前的网络状态  -1：没有网络  1：WIFI网络 2：wap网络3：net网络
     *
     * @param context
     * @return
     */
    public static final int CMNET = 3;
    public static final int CMWAP = 2;
    public static final int WIFI = 1;
    public static final String SAPCE_REGEX = " ";
    // bt字节参考量
    private static final float SIZE_BT = 1024L;
    // KB字节参考量
    private static final float SIZE_KB = SIZE_BT * 1024.0f;
    // MB字节参考量
    private static final float SIZE_MB = SIZE_KB * 1024.0f;
    // GB字节参考量
    private static final float SIZE_GB = SIZE_MB * 1024.0f;
    // TB字节参考量
    // private static final float SIZE_TB=SIZE_GB * 1024.0f;
    // BigDecimal四舍五入精度为2
    private static final int SACLE = 2;
    // 缓冲的大小
    private static final int BUFF_SIZE = 1024;

    // 判断是否转int类型
    public static boolean isFormatInteger(String str) {
        if (str != null && !str.equals("") && isGigital(str)) {
            return true;
        }
        return false;
    }

    /**
     * @param str 字符串
     * @return 如果字符串是数字返回ture，反正false
     */
    public static boolean isGigital(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isGigital = pattern.matcher(str);
        if (!isGigital.matches()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断字符串是不是float型
     */
    public static boolean isFloat(String str) {
        Pattern pattern = Pattern.compile("[0-9]*(\\.?)[0-9]*");
        Matcher isFloat = pattern.matcher(str);
        if (isFloat.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param url 保存文件的文字
     * @return 文件名
     */
    public static String getFileName(String url) {
        String fileName = null;
        if (url != null && url.contains("/")) {
            String[] data = url.split("/");
            fileName = data[data.length - 1];
        }
        return fileName;
    }

    /**
     * @param style 类型
     * @return 用逗号，或者分号截取字符串前两个(这个方法用于类型的字符串截取)
     */
    public static String get2InString(String style) {
        Pattern pattern = Pattern.compile("[,;]");
        String[] actors = pattern.split(style);
        StringBuffer buffer = new StringBuffer();
        if (actors.length <= 1) {
            buffer.append(actors[0]);
        } else if (actors.length == 2) {
            buffer.append(actors[0]);
            buffer.append(",");
            buffer.append(actors[1]);
        } else if (actors.length >= 3) {
            buffer.append(actors[0]);
            buffer.append(",");
            buffer.append(actors[1]);
            buffer.append(",");
            buffer.append(actors[2]);
        }
        return buffer.toString();
    }

    /**
     * @param str 字符串
     * @return 字符串转化MD5
     */
    public static String calcMd5(String str) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(str.getBytes());
            return toHexString(algorithm.digest(), "");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param file 文件
     * @return 文件转换MD5
     */
    public static String calcMd5(File file) {
        FileInputStream in = null;
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            in = new FileInputStream(file);
            FileChannel ch = in.getChannel();
            MappedByteBuffer byteBuffer;
            byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            algorithm.update(byteBuffer);
            return toHexString(algorithm.digest(), "");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 上面的辅助类
    public static String toHexString(byte[] bytes, String separator) {
        StringBuilder hexString = new StringBuilder();
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        for (byte b : bytes) {
            hexString.append(hexDigits[b >> 4 & 0xf]);
            hexString.append(hexDigits[b & 0xf]);
        }
        return hexString.toString();
    }

    // 去掉字符串中的空格、回车、换行符、制表符
    public static String replaceBlank(String str) {
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            String after = m.replaceAll("");
            return after;
        } else {
            return null;
        }
    }

    // 换域名
    public static String replaceRealmName(String newRealmName, String oldRealmName, String source) {
        if (oldRealmName == null) {
            return source;
        }
        StringBuffer bf = new StringBuffer("");
        int index = -1;
        while ((index = source.indexOf(oldRealmName)) != -1) {
            bf.append(source.substring(0, index) + newRealmName);
            source = source.substring(index + oldRealmName.length());
            index = source.indexOf(oldRealmName);
        }
        bf.append(source);
        return bf.toString();
    }

    // 详情页面综艺的时间
    public static String getDetailsDescriptionTime(String tm) {
        Long timestamp = Long.parseLong(tm) * 1000;
        String date = new SimpleDateFormat("yyyy年MM月").format(new Date(timestamp));
        return date;
    }

    // 获取新闻的时间
    public static String getDescriptionTime(String tm) {
        Long timestamp = Long.parseLong(tm) * 1000;
        String date = new SimpleDateFormat("yyyy年MM月dd日").format(new Date(timestamp));
        return date;
    }

    // 获取今天的时间
    public static String getTodayTime() {
        long todayDate = new Date().getTime();
        String date = new SimpleDateFormat("yyyy年MM月dd日").format(new Date(todayDate));
        return date;
    }

    // // 获取opudid
    // public static String getOpenUdid() {
    // OpenUDID_manager.sync(PPStvApp.getPPSInstance());
    // String openUDID = null;
    // if (OpenUDID_manager.isInitialized()) {
    // openUDID = OpenUDID_manager.getOpenUDID();
    // }
    // return openUDID;
    // }

    // 获取昨天的时间
    public static String getYesterdayTime() {
        long todayDate = new Date().getTime();
        long yesterdayDate = todayDate - 24 * 60 * 60 * 1000;
        String date = new SimpleDateFormat("yyyy年MM月dd日").format(new Date(yesterdayDate));
        return date;
    }

    // 获取旧分辨率
    public static String getOldLcd(Activity activity) {
        StringBuffer buffer = new StringBuffer();
        int density = 0;
        int widthPixels = 0;
        int heightPixels = 0;
        DisplayMetrics dm = new DisplayMetrics();
        if (dm != null) {
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            density = dm.densityDpi;
            widthPixels = dm.widthPixels;
            heightPixels = dm.heightPixels;
        }
        buffer.append(heightPixels);
        buffer.append("*");
        buffer.append(widthPixels);
        buffer.append(",");
        buffer.append(density);
        buffer.append("dpi");
        return buffer.toString();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    // 获取屏幕长宽
    public static int[] getScreenSizeArray(Activity activity) {
        int[] size = new int[2];
        int widthPixels = 0;
        int heightPixels = 0;
        DisplayMetrics dm = new DisplayMetrics();
        if (dm != null) {
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            widthPixels = dm.widthPixels;
            heightPixels = dm.heightPixels;
            size[0] = widthPixels;
            size[1] = heightPixels;
        }
        return size;
    }

    // 获取新分辨率
    public static String getNewLcd(Activity activity) {
        StringBuffer buffer = new StringBuffer();
        int widthPixels = 0;
        int heightPixels = 0;
        DisplayMetrics dm = new DisplayMetrics();
        if (dm != null) {
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            widthPixels = dm.widthPixels;
            heightPixels = dm.heightPixels;
        }
        buffer.append(heightPixels);
        buffer.append("*");
        buffer.append(widthPixels);
        return buffer.toString();
    }

    // 获取手机屏幕宽高乘积
    public static int getScreenSize(Activity activity) {
        int widthPixels = 0;
        int heightPixels = 0;
        DisplayMetrics dm = new DisplayMetrics();
        if (dm != null) {
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            widthPixels = dm.widthPixels;
            heightPixels = dm.heightPixels;
        }
        return widthPixels * heightPixels;
    }

    // 获取当前时间戳
    public static String getTimesTamp() {
        long timestamp = System.currentTimeMillis();
        return String.valueOf(timestamp);
    }

    // 客户端版本版本号
    public static String getVersionName(Activity activity) {
        String version = null;
        try {
            version = String.valueOf(activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static int getVersionCode(Activity activity) {
        int versionCode = 0;
        try {
            versionCode = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    // 屏幕密度
    public static float getdensity(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        return density;
    }

    // 把字符串转换成UTF-8的格式
    public static String stringToUTF(String str) {
        if (str != null && !str.equals("")) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // 把字符串转换成GBK的格式
    public static String stringToGBK(String str) {
        if (str != null && !str.equals("")) {
            try {
                return URLDecoder.decode(str, "GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // 把字符串编码成GBK的格式
    public static String stringUTF8ToGBK(String str) {
        if (str != null && !str.equals("")) {
            try {
                return URLEncoder.encode(str, "GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // 获取系统时间
    public static String getSystemTime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = format.format(date);
        return result;

    }

    // 把文件转变字符串
    public static String file2String(File file, String encoding) {
        InputStreamReader reader = null;
        StringWriter writer = new StringWriter();
        try {
            if (encoding == null || "".equals(encoding.trim())) {
                reader = new InputStreamReader(new FileInputStream(file));
            } else {
                reader = new InputStreamReader(new FileInputStream(file), encoding);
            }
            // 将输入流写入输出流
            char[] buffer = new char[BUFF_SIZE];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return writer.toString();
    }

    // 把inputstream转换为字符串（方法一）
    public static String getStr1FromInputstream(InputStream input) {
        String result = null;
        int i = -1;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            while ((i = input.read()) != -1) {
                baos.write(i);
            }
            result = baos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 把inputstream转换为字符串（方法二）
    public static String getStr2FromInputstream(InputStream input) {
        int i = -1;
        String result = null;
        byte[] b = new byte[1024];
        StringBuffer sb = new StringBuffer();
        try {
            while ((i = input.read(b)) != -1) {
                sb.append(new String(b, 0, i));
            }
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 获取手机型号
    public static String getLocalModel() {
        String model = android.os.Build.MODEL;
        if (model == null) {
            model = "";
        }
        return model;
    }

    // 获取手机系统版本
    public static String getLocalSystemVersion() {
        String version = android.os.Build.VERSION.RELEASE;
        if (version == null) {
            version = "";
        }
        return version;

    }

    // 获取手机厂商
    public static String getLocalManufacturer() {
        String manufacturer = android.os.Build.MANUFACTURER;
        if (manufacturer == null) {
            manufacturer = "";
        }
        return manufacturer;
    }

    // 获取ip地区
    public static String getIpCountry() {
        String ipCountry = "460";
        try {
            TelephonyManager mTelephonyManager = (TelephonyManager) BaseApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephonyManager != null) {
                String IMSI = mTelephonyManager.getSubscriberId();
                if (IMSI != null && !IMSI.equals("") && IMSI.length() >= 3) {
                    // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
                    ipCountry = IMSI.substring(0, 3);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipCountry;
    }

    // 获取ip运营商
    public static String getIpName() {
        String ipName = null;
        try {
            TelephonyManager mTelephonyManager = (TelephonyManager) BaseApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephonyManager != null) {
                String IMSI = mTelephonyManager.getSubscriberId();
                if (IMSI != null && !IMSI.equals("") && IMSI.length() >= 5) {
                    // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
                    ipName = IMSI.substring(3, 5);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipName;
    }

    // 获取ip基站
    public static String getIpBaseStation() {
        TelephonyManager telMgr = (TelephonyManager) BaseApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        int cid = 0;
        int lac = 0;
        try {
            if (telMgr != null) {
                GsmCellLocation gc = (GsmCellLocation) telMgr.getCellLocation();
                if (null == gc) {
                    return "0_0";
                }
                cid = gc.getCid();
                lac = gc.getLac();
            }
        } catch (Exception e) {
            if (telMgr != null) {
                CdmaCellLocation location = (CdmaCellLocation) telMgr.getCellLocation();
                if (null == location) {
                    return "0_0";
                }
                lac = location.getNetworkId();
                cid = location.getBaseStationId();
                cid /= 16;
            }
        }
        return lac + "_" + cid;
    }

    // 获取包名
    public static String getPackageName(Activity activity) {

        String packageName = null;
        try {
            packageName = String.valueOf(activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageName;
    }

    // 获取Android_id
    public static String getAndroidId() {

        return Settings.Secure.getString(BaseApplication.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    // 判断设备是否越狱ROOT
    public static boolean getIsJailBreak() {
        for (String str : new String[]{"/system/bin/", "/system/xbin/", "/data/local/xbin/", "/data/local/bin/", "/system/sd/xbin/"}) {
            if (new File(str + "su").exists()) {
                return true;
            }
        }
        return false;
    }

    // 获取手机硬件属性
    public static String[] getTotalHardwareMessage() {
        String result[] = new String[3];
        String str1 = "/proc/cpuinfo";
        String str2 = null;
        FileReader localFileReader = null;
        BufferedReader localBufferedReader = null;
        try {
            localFileReader = new FileReader(str1);
            localBufferedReader = new BufferedReader(localFileReader);
            while ((str2 = localBufferedReader.readLine()) != null) {
                if (str2.contains("Processor")) {
                    if (str2.contains(":")) {
                        String[] arrayOfString = str2.split(":");
                        if (arrayOfString.length == 2) {
                            result[0] = arrayOfString[1];
                            if (result[0].length() > 32 && result[0] != null) {
                                result[0] = result[0].substring(0, 32);
                            }
                        }
                    }
                }
                if (str2.contains("Features")) {
                    if (str2.contains(":")) {
                        String[] arrayOfString = str2.split(":");
                        if (arrayOfString.length == 2) {
                            result[1] = arrayOfString[1];
                            if (result[1].length() > 50 && result[1] != null) {
                                result[1] = result[1].substring(0, 50);
                            }
                        }
                    }
                }
                if (str2.contains("Hardware")) {
                    if (str2.contains(":")) {
                        String[] arrayOfString = str2.split(":");
                        if (arrayOfString.length == 2) {
                            result[2] = arrayOfString[1];
                            if (result[2].length() > 32 && result[2] != null) {
                                result[2] = result[2].substring(0, 32);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (localBufferedReader != null) {
                try {
                    localBufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (localFileReader != null) {
                try {
                    localFileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    // 获取用户的IPd
    public static int getIpAddress() {
        int ipAddress = 0;
        WifiManager wifiManager = (WifiManager) BaseApplication.getContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo == null || wifiInfo.equals("")) {
            return ipAddress;
        } else {
            ipAddress = wifiInfo.getIpAddress();
        }
        return ipAddress;
    }

    // 获取用户设备IP
    public static String getUserIp() {
        int ipAddress = getIpAddress();
        return String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
    }

    // 获取时区
    public static String getTimeArea() {
        return String.valueOf(TimeZone.getDefault().getOffset(new Date().getTime() / 1000)).toString();
    }

    // 获取useragent
    public static String getUserAgent(Context context) {
        String userAgent = null;
        WebView webView = new WebView(context);
        WebSettings settings = webView.getSettings();
        if (settings != null) {
            userAgent = settings.getUserAgentString();
        }
        return userAgent;
    }

    public static String getDetailsPromptMessage(int playPosition) {
        String message = "上次播放到：";
        playPosition /= 1000;
        int minute = playPosition / 60;
        int hour = minute / 60;
        int second = playPosition % 60;
        minute %= 60;
        if (hour == 0) {
            message = message + minute + "分" + second + "秒";
            return message;
        } else {
            message = message + hour + "小时" + minute + "分" + second + "秒";
            return message;
        }
    }

    public static String getHistoryPromptMessage(int playPosition, int tvTotalTimes) {
        if (playPosition >= (tvTotalTimes - 1000 * 3)) {
            return "本片已播放完(100%)";
        } else if (playPosition >= 0 && (playPosition <= 1000 * 60)) {
            return "观看时间小于1分钟";
        } else {
            playPosition /= 1000;
            int minute = playPosition / 60;
            int hour = minute / 60;
            int second = playPosition % 60;
            minute %= 60;
            if (hour == 0) {
                String message = minute + "分" + second + "秒";
                return "观看至" + message;
            } else {
                String message = hour + "小时" + minute + "分" + second + "秒";
                return "观看至" + message;
            }
        }
    }

    public static String getDetailsPlayDuration(int playPosition, int tvTotalTimes) {
        float pos_percent = 0;
        String pospercent = "(0%)";
        String message;
        if ((playPosition != 0) && (tvTotalTimes != 0)) {
            pos_percent = ((float) playPosition / tvTotalTimes * 100);
            pospercent = "(" + String.format("%.1f", pos_percent) + "%" + ")";
        }
        if (playPosition >= (tvTotalTimes - 1000 * 3)) {
            message = "本片已播放完(100%)";
            return message;
        } else if (playPosition >= 0 && (playPosition <= 1000 * 60)) {
            message = "观看时间小于1分钟" + pospercent;
            return message;
        } else {
            playPosition /= 1000;
            int minute = playPosition / 60;
            int hour = minute / 60;
            int second = playPosition % 60;
            minute %= 60;
            if (hour == 0) {
                message = minute + "分" + second + "秒" + pospercent;
                return message;
            } else {
                message = hour + "小时" + minute + "分" + second + "秒" + pospercent;
                return message;
            }
        }
    }

    // 计算UGC时长
    public static String getUgcDuration(int time) {
        StringBuffer buffer = new StringBuffer();
        int second = time / 1000;
        if (second > 60) {
            buffer.append(second / 60);
            buffer.append("分");
            if (second % 60 > 0) {
                buffer.append(second % 60);
                buffer.append("秒");
            }
        } else {
            buffer.append(second);
            buffer.append("秒");
        }
        return buffer.toString();
    }

    // 会员登录处理返回的用户名
    public static String convertAccountNmae(String string) {
        String result = "";
        try {
            result = new String(gbk2utf8(string), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] gbk2utf8(String string) {
        char c[] = string.toCharArray();
        byte[] fullByte = new byte[3 * c.length];
        for (int i = 0; i < c.length; i++) {
            int m = (int) c[i];
            String word = Integer.toBinaryString(m);

            StringBuffer sb = new StringBuffer();
            int len = 16 - word.length();
            for (int j = 0; j < len; j++) {
                sb.append("0");
            }
            sb.append(word);
            sb.insert(0, "1110");
            sb.insert(8, "10");
            sb.insert(16, "10");
            String s1 = sb.substring(0, 8);
            String s2 = sb.substring(8, 16);
            String s3 = sb.substring(16);
            byte b0 = Integer.valueOf(s1, 2).byteValue();
            byte b1 = Integer.valueOf(s2, 2).byteValue();
            byte b2 = Integer.valueOf(s3, 2).byteValue();
            byte[] bf = new byte[3];
            bf[0] = b0;
            fullByte[i * 3] = bf[0];
            bf[1] = b1;
            fullByte[i * 3 + 1] = bf[1];
            bf[2] = b2;
            fullByte[i * 3 + 2] = bf[2];

        }
        return fullByte;
    }

    // 通过下载地址得到fid
    public static String transPPSUrl2fid(String origUrl) {
        String toUrl = origUrl;
        String fid = "";
        if (origUrl == null) {
            return null;
        }
        if (toUrl.contains("pps://") || toUrl.contains("tvod://")) {
            int index = toUrl.lastIndexOf('?');
            if (index > 0 && index < toUrl.length() - 1) {
                fid = toUrl.substring(index + 1, toUrl.length());
                if (fid.contains("fid=") && fid.length() == 36/* fid=xxx */) {
                    index = fid.lastIndexOf('=');
                    if (index == 3) {
                        fid = fid.substring(4, 36);
                        return fid;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取有颜色的文字，这里默认为橙色
     *
     * @param str 文字内容
     * @return
     */
    public static String getHtmlColorString(String color, String str) {
        StringBuffer sb = new StringBuffer();
        sb.append("<font color='" + color + "'>");
        sb.append(str);
        sb.append("</font>");
        return sb.toString();
    }

    /**
     * 根据分数不同返回不同颜色的文字
     *
     * @return
     */
    public static String getRatingColorString(String vote) {
        StringBuffer sb = new StringBuffer();
        if (vote == null || vote.equals("") || !StrUtils.isFloat(vote)) {
            vote = "0";
        }
        int score = (int) Float.parseFloat(vote);
        switch (score) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                sb.append("<font color='" + "#f3ad07" + "'>");
                break;
            case 7:
                sb.append("<font color='" + "#ef8203" + "'>");
                break;
            case 8:
                sb.append("<font color='" + "#ff7510" + "'>");
                break;
            default:
                sb.append("<font color='" + "#fe4223" + "'>");
                break;
        }
        sb.append(vote);
        sb.append("</font>");
        return sb.toString();
    }

    public static String getNewDetailsFn(String str, String name) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(str.substring(0, 2));
        buffer.append("年");
        buffer.append(str.substring(2, 4));
        buffer.append("月");
        buffer.append(str.substring(4, 6));
        buffer.append("日");
        buffer.append("-");
        buffer.append(name);
        return buffer.toString();
    }

    // 获取BP设置重试地址
    public static String getBpSetRetryUrl(String retryName, String sourceStr) {
        String retryUrl = StrUtils.replaceRealmName(retryName, "bip.ppstream.com", sourceStr);
        return retryUrl;
    }

    // 获取列表重试地址
    public static String getRetryUrl(String retryName, String sourceStr) {
        String retryUrl = StrUtils.replaceRealmName(retryName, "list1.ppstream.com", sourceStr);
        return retryUrl;
    }

    // 获取BP播放重试地址
    public static String getBpPlayRetryUrl(String retryName, String sourceStr) {
        String retryUrl = StrUtils.replaceRealmName(retryName, "dp.ugc.pps.tv", sourceStr);
        return retryUrl;
    }

    // 根据传入的字节数，返回对应的字符串
    public static String getReadableSize(long length) {
        if (length >= 0 && length < SIZE_BT) {
            // Math.round四舍五入
            return (Math.round(length * 10) / 10.0) + "B";
        } else if (length >= SIZE_BT && length < SIZE_KB) {
            // //length/SIZE_BT+"KB";
            return (Math.round((length / SIZE_BT) * 10) / 10.0) + "KB";
        } else if (length >= SIZE_KB && length < SIZE_MB) {
            // length/SIZE_KB+"MB";
            return (Math.round((length / SIZE_KB) * 10) / 10.0) + "MB";
        } else if (length >= SIZE_MB && length < SIZE_GB) {
            // bigdecimal这个对象进行数据相互除
            BigDecimal longs = new BigDecimal(Double.valueOf(length + "").toString());
            BigDecimal sizeMB = new BigDecimal(Double.valueOf(SIZE_MB + "").toString());
            String result = longs.divide(sizeMB, SACLE, BigDecimal.ROUND_HALF_UP).toString();
            return result + "GB";
        } else {
            // bigdecimal这个对象进行数据相互除
            BigDecimal longs = new BigDecimal(Double.valueOf(length + "").toString());
            BigDecimal sizeMB = new BigDecimal(Double.valueOf(SIZE_GB + "").toString());
            String result = longs.divide(sizeMB, SACLE, BigDecimal.ROUND_HALF_UP).toString();
            return result + "TB";
        }
    }

    // 从url获取投递地址
    public static String getDeliverUrlFromUrl(String url) {
        if (url != null && !url.equals("")) {
            String[] array1 = url.split("[?]");
            if (array1.length >= 2) {
                return array1[0];
            }
        }
        return null;
    }

    // 从url获取投递参数
    public static HashMap<String, String> getDeliverMapFromUrl(String url) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (url != null && !url.equals("")) {
            String[] array1 = url.split("[?]");
            if (array1.length >= 2) {
                String content = array1[1];
                String[] array2 = content.split("[&]");
                for (int j = 0; j < array2.length; j++) {
                    String str2 = array2[j];
                    String[] array3 = str2.split("[=]");
                    String key = null;
                    String value = null;
                    for (int z = 0; z < array3.length; z++) {
                        if (z % 2 == 0) {
                            key = array3[z];
                        } else {
                            value = array3[z];
                            map.put(key, value);
                        }
                    }
                }
            }
        }
        return map;
    }

    // 获取string转int
    public static int string2Int(String str) {
        if (str != null && !str.equals("") && isGigital(str)) {
            return Integer.parseInt(str);
        }
        return 0;
    }

    // 获取cup数目
    public static int getNumCores() {
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new CpuFilter());
            return files.length;
        } catch (Exception e) {
            return 1;
        }
    }

    public static int getAPNType(Context context) {
        int netType = -1;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            Logger.i("networkInfo.getExtraInfo()", "networkInfo.getExtraInfo() is " + networkInfo.getExtraInfo());
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                netType = CMNET;
            } else {
                netType = CMWAP;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = WIFI;
        }
        return netType;
    }

    /**
     * 判断是否为null或者长度等于0
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }

    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * compare two string
     *
     * @param actual
     * @param expected
     * @return
     * @see ObjectUtils#isEquals(Object, Object)
     */
    public static boolean isEquals(String actual, String expected) {
        return ObjectUtils.isEquals(actual, expected);
    }

    /**
     * get length of CharSequence
     * <pre>
     * length(null) = 0;
     * length(\"\") = 0;
     * length(\"abc\") = 3;
     * </pre>
     *
     * @param str
     * @return if str is null or empty, return 0, else return {@link CharSequence#length()}.
     */
    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    /**
     * null Object to empty string
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     *
     * @param str
     * @return
     */
    public static String nullStrToEmpty(Object str) {
        return (str == null ? "" : (str instanceof String ? (String) str : str.toString()));
    }

    /**
     * capitalize first letter
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length()).append(Character.toUpperCase(c)).append(str.substring(1)).toString();
    }

    /**
     * encoded in utf-8
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * encoded in utf-8, if exception, return defultReturn
     *
     * @param str
     * @param defultReturn
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }

    /**
     * get innerHtml from href
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     *
     * @param href
     * @return <ul>
     * <li>if href is null, return ""</li>
     * <li>if not match regx, return source</li>
     * <li>return the last string that match regx</li>
     * </ul>
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

    /**
     * process special char in html
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     *
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return StrUtils.isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }

    /**
     * transform half width char to full width char
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
     * </pre>
     *
     * @param s
     * @return
     */
    public static String fullWidthToHalfWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char) (source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * transform full width char to half width char
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
     * </pre>
     *
     * @param s
     * @return
     */
    public static String halfWidthToFullWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char) 12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char) (source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * 一个简单的Map与String的转换方法,比较实用的一段java代码
     * 1）将Map转成形如username'chenziwen^password'1234的字符串
     * 方法名称:transMapToString
     * 传入参数:map
     * 返回值:String 形如 username'chenziwen^password'1234
     */
    public static String transMapToString(Map map) {
        Map.Entry entry;
        StringBuffer sb = new StringBuffer();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (Map.Entry) iterator.next();
            sb.append(entry.getKey().toString()).append("'").append(null == entry.getValue() ? "" :
                    entry.getValue().toString()).append(iterator.hasNext() ? "^" : "");
        }
        return sb.toString();
    }

    /**
     * 2）将形如形如username'chenziwen^password'1234的字符串转成Map
     * 方法名称:transStringToMap
     * 传入参数:mapString 形如 username'chenziwen^password'1234
     * 返回值:Map
     */
    public static Map transStringToMap(String mapString) {
        Map map = new HashMap();
        StringTokenizer items;
        for (StringTokenizer entrys = new StringTokenizer(mapString, "^"); entrys.hasMoreTokens();
             map.put(items.nextToken(), items.hasMoreTokens() ? ((items.nextToken())) : null))
            items = new StringTokenizer(entrys.nextToken(), "'");
        return map;
    }


    /* if (url.contains("group://")) {
                    String arg1 = (url.split("\\?"))[1];
                    if (!StringUtil.isEmpty(arg1)) {
                        String[] temp = arg1.split("&");
                        tagId = (temp[0].split("="))[1];

                        String[] tempTag = temp[1].split("=");
                        if(tempTag.length == 2){
                            tagName = URLDecoder.decode(tempTag[1]).trim();
                        }else{
                            tagName = mGroupName;
                        }
                        Intent intent = new Intent();
                        intent.putExtra(Constants.INTENT_TAG_ID, tagId);
                        intent.putExtra(Constants.INTENT_TAG_NAME, tagName);
                        setResult(RESULT_OK, intent);
//                        getCloudContactList(Long.parseLong(gId), mCurrentPage ,mCloudHandler.obtainMessage(MSG_CONTACT_GETLIST), tagId);
                    }*/

    public static String formatTime(long time) {
        long days = 0L;
        long hours = 0L;
        long mins = 0L;
        long totalDuration = time;
        mins = totalDuration / 60;
        if (totalDuration / 60 > 0) {
            if (mins / 60 > 0) {
                hours = mins / 60;
                if (hours / 24 > 0) {
                    days = hours / 24;
                    return days + "天," + hours + "小时, " + mins + "分钟, " + totalDuration % 60
                            + "秒";
                } else {
                    return hours + "小时, " + mins % 60 + "分钟, " + totalDuration % 60 + "秒";
                }
            } else {
                return mins + "分钟, " + totalDuration % 60 + "秒";
            }
        } else {
            return totalDuration + "秒";
        }
    }

    public static Map<String, String> analysisUrl(String url) {
        Map<String, String> result = new HashMap<String, String>();
        String key = "";
        String value = "";
        if (!StrUtils.isEmpty(url)) {
            String content = (url.split("\\?"))[1];
            String[] temp = content.split("&");
            for (int i = 0; i < temp.length; i++) {
                String[] keyANDvalue = temp[i].split("=");
                if (keyANDvalue.length == 2) {
                    key = keyANDvalue[0];
                    value = keyANDvalue[1];
                    result.put(key, value);
                } else if (keyANDvalue.length == 1) {
                    key = keyANDvalue[0];
                    value = "";
                    result.put(key, value);
                }
            }
        }
        return result;
    }

    /**
     * 将byte数组通过GZIP压缩后用base64转码成字符串
     *
     * @param byteArr
     * @return
     */
    public static String compressByteArrayByGzip(byte[] byteArr) {
        String base64String = null;
        ByteArrayOutputStream os = null;
        GZIPOutputStream gos = null;
        try {
            os = new ByteArrayOutputStream();
            gos = new GZIPOutputStream(os);
            gos.write(byteArr);
            gos.finish();
            //gos.flush();//android 4.4报错
            base64String = Base64.encodeToString(os.toByteArray(), Base64.DEFAULT);
        } catch (Exception e) {
            Log.e("compress", "IOException: ", e);
        } finally {
            if (gos != null) {
                try {
                    gos.close();
                } catch (IOException e) {
                    Log.e("compress", "gos : ", e);
                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.e("compress", "os : ", e);
                }
            }
        }
        return base64String;
    }

    /**
     * base64反解析图片流后Gzip解压
     *
     * @param zipText
     * @return
     */
    public static byte[] decompressToByteArrayByGzip(String zipText) {
        byte[] byteArr = null;
        byte[] compressed;
        try {
            compressed = Base64.decode(zipText, Base64.NO_WRAP);
        } catch (OutOfMemoryError e) {
            return byteArr;
        } catch (Exception e) {
            //参数错误
            return byteArr;
        }
        GZIPInputStream gzipInputStream = null;

        ByteArrayOutputStream baos = null;
        try {
            gzipInputStream = new GZIPInputStream(
                    new ByteArrayInputStream(compressed, 0, compressed.length));
            baos = new ByteArrayOutputStream();

            byte[] buf = new byte[1024];
            int count = 0;
            while ((count = gzipInputStream.read(buf)) != -1) {
                baos.write(buf, 0, count);
            }
            byteArr = baos.toByteArray();
        } catch (IOException e) {
            Log.e("decompress", "IOException", e);
        } finally {
            if (gzipInputStream != null) {
                try {
                    gzipInputStream.close();
                } catch (IOException e) {
                    Log.e("decompress", "gzip", e);
                }
            }

            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    Log.e("decompress", "baos", e);
                }
            }
        }
        return byteArr;
    }

    /**
     * 将文件转成base64字符串(用于发送语音文件)
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    /**
     * 将base64编码后的字符串转成文件
     *
     * @param base64Code
     * @throws Exception
     */
    public static String decoderBase64File(String base64Code) {
        String savePath = "";
        String saveDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Yun/Sounds/";
       // String saveDir = G.APPRECORD;
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        savePath = saveDir + getRandomFileName();
        byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
        try {
            FileOutputStream out = new FileOutputStream(savePath);
            out.write(buffer);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return savePath;
    }

    public static String getRandomFileName() {
        String rel = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        rel = formatter.format(curDate);
        rel = rel + new Random().nextInt(1000);
        return rel + ".amr";
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 电话号码验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }

    /**
     * �ο�PhoneNumberUtils.isUriNumber
     *
     * @param number the number for check
     * @return true if the string is a URI number
     */
    public static boolean isUriNumber(String number) {
        // Neither "@" nor "%40" will ever be found in a legal PSTN number.)
        return number != null && (number.contains("@") || number.contains("%40"));
    }

    /**
     * it's a alternative solution to return the name of a URI number
     *
     * @param number the URI number
     * @return the name of URI number
     */
    public static String getUriName(String number) {
        String name = null;

        int at = number.indexOf("@");
        if (at == -1) {
            at = number.indexOf("%40");
        }

        if (at != -1) {
            name = number.substring(0, at);
        }

        return name;
    }

    public static final void writeUTF(DataOutputStream dos, String string) throws IOException {
        if (string == null) {
            dos.writeUTF("");
        } else {
            dos.writeUTF(string);
        }
    }

    public static String md5ToHex(String in) {
        MessageDigest digest;
        StringBuffer result = new StringBuffer();
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
//            int len = a.length;
            for (int i = 0; i < a.length; i++) {
                result.append(byteToHex(a[i] & 0xFF));
            }

            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byteToHex(int b) {
        return byteToHex(new StringBuilder(), b).toString();
    }

    public static StringBuilder byteToHex(StringBuilder sb, int b) {
        b &= 0xFF;
        sb.append("0123456789ABCDEF".charAt(b >> 4));
        sb.append("0123456789ABCDEF".charAt(b & 0xF));
        return sb;
    }

    public static String compressByGzip(String str) {
        String ret = null;
        ByteArrayOutputStream os = null;
        GZIPOutputStream gos = null;
        try {
            os = new ByteArrayOutputStream(str.length());
            gos = new GZIPOutputStream(os);
            gos.write(str.getBytes());
            gos.finish();
//            gos.flush();//在android4.4上此语句报错
            ret = Base64.encodeToString(os.toByteArray(), Base64.DEFAULT);
        } catch (Exception e) {
            Log.e("compress", "IOException: ", e);
        } finally {
            if (gos != null) {
                try {
                    gos.close();
                } catch (IOException e) {
                    Log.e("compress", "gos : ", e);
                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.e("compress", "os : ", e);
                }
            }
        }

        return ret;
    }

    public static String decompressByGzip(String zipText) {

        String ret = null;

        byte[] compressed = Base64.decode(zipText, Base64.NO_WRAP);
        GZIPInputStream gzipInputStream = null;
        ByteArrayOutputStream baos = null;

        try {
            gzipInputStream = new GZIPInputStream(
                    new ByteArrayInputStream(compressed, 0, compressed.length));
            baos = new ByteArrayOutputStream();

            byte[] buf = new byte[1024];
            int count = 0;
            while ((count = gzipInputStream.read(buf)) != -1) {
                baos.write(buf, 0, count);
            }
            ret = new String(baos.toByteArray(), "UTF-8");
        } catch (IOException e) {
            Log.e("decompress", "IOException", e);
        } finally {
            if (gzipInputStream != null) {
                try {
                    gzipInputStream.close();
                } catch (IOException e) {
                    Log.e("decompress", "gzip", e);
                }
            }

            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    Log.e("decompress", "baos", e);
                }
            }
        }

        return ret;
    }

    public static Object objectWithString(String value) {
        if (StrUtils.isEmpty(value)) return null;

        try {
            String decoderValue = URLDecoder.decode(value, "UTF-8");
            ByteArrayInputStream bais = new ByteArrayInputStream(
                    decoderValue.getBytes("ISO-8859-1"));
            ObjectInputStream ios = new ObjectInputStream(bais);
            return ios.readObject();
        } catch (UnsupportedEncodingException e1) {
            // Auto-generated catch block
            e1.printStackTrace();
        } catch (StreamCorruptedException e) {
            // Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public static String stringWithObject(Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            String value = baos.toString("ISO-8859-1");
            String encodedValue = URLEncoder.encode(value, "UTF-8");
            return encodedValue;
        } catch (IOException e) {
            // Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    public static String filterUnNumber(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    //    private static final Pattern IS_DIGITS_PATTERN = Pattern.compile("[\\+]?[0-9.]+");
    public static boolean isNumeric(String str) {
        str = str.replaceAll(SAPCE_REGEX, "");
        // upto length 13 and including character + infront. ^\+[0-9]{10,13}$
        // ^[+]?[0-9]{10,13}
        return str.matches("^[\\+0-9]\\d*$");
        // return str.matches("^[\\+0-9]*[1-9][0-9]*$");
    }

    /**
     * Function that get the size of an object.
     *
     * @param object
     * @return Size in bytes of the object or -1 if the object
     * is null.
     * @throws IOException
     */
    public static final int sizeOf(Object object) throws IOException {

        if (object == null)
            return -1;

        // Special output stream use to write the content
        // of an output stream to an internal byte array.
        ByteArrayOutputStream byteArrayOutputStream =
                new ByteArrayOutputStream();

        // Output stream that can write object
        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(byteArrayOutputStream);

        // Write object and close the output stream
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        objectOutputStream.close();

        // Get the byte array
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        // can the toByteArray() method return a
        // null array ?
        return byteArray == null ? 0 : byteArray.length;

    }

    public static boolean isMobilePhoneInChina(String phoneNumber) {

        /*
         * 手机：^(1(([35][0-9])|(47)|[8][01236789]))\d{8}$
         * ^(13[0-9]|147|15[0|3|6|7|8|9]|18[0|8|9])\d{8}$
            座机：^0\d{2,3}(\-)?\d{7,8}$
         */
        return phoneNumber.matches("^((\\+86)|(86)){0,1}(13[0-9]|147|15[0-9]|18[0-9])\\d{8}$");
    }

    public static boolean isLandlinePhoneInChina(String phoneNumber) {
        return phoneNumber.matches("^0\\d{2,3}(\\-)?\\d{7,8}$");
    }

    public static String getStringDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        return format.format(date);
    }
}
