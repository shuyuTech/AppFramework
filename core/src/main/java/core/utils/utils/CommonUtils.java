package core.utils.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.jess.arms.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import core.base.BaseApplication;

/**
 * 通用工具类
 *
 * @author Ht
 */
public class CommonUtils {
    private static final String TAG = "CommonUtils";


    //电话<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private static final String GSON_FORMAT = "yyyy-MM-dd HH:mm:ss";

    //电话>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //信息<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public static SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static SimpleDateFormat formatDay = new SimpleDateFormat("d", Locale.getDefault());

    //信息>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public static SimpleDateFormat formatMonthDay = new SimpleDateFormat("M-d", Locale.getDefault());
    public static SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static ProgressDialog progressDialog = null;
    private static Gson mGson;


    //启动新Activity方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    private CommonUtils() {
    }

    /**
     * 打电话
     *
     * @param context
     * @param phone
     */
    public static void call(Activity context, String phone) {
        if (StringUtils.isNotEmpty(phone, true)) {
            Uri uri = Uri.parse("tel:" + phone.trim());
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            toActivity(context, intent);
            return;
        }
        showShortToast(context, "请先选择号码哦~");
    }

    /**
     * 发送信息，多号码
     *
     * @param context
     * @param phoneList
     */
    public static void toMessageChat(Activity context, List<String> phoneList) {
        if (context == null || phoneList == null || phoneList.size() <= 0) {
            Log.e(TAG, "sendMessage context == null || phoneList == null || phoneList.size() <= 0 " +
                    ">> showShortToast(context, 请先选择号码哦~); return; ");
            showShortToast(context, "请先选择号码哦~");
            return;
        }

        String phones = "";
        for (int i = 0; i < phoneList.size(); i++) {
            phones += phoneList.get(i) + ";";
        }
        toMessageChat(context, phones);
    }

    /**
     * 发送信息，单个号码
     *
     * @param context
     * @param phone
     */
    public static void toMessageChat(Activity context, String phone) {
        if (context == null || StringUtils.isNotEmpty(phone, true) == false) {
            Log.e(TAG, "sendMessage  context == null || StringUtils.isNotEmpty(phone, true) == false) >> return;");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("address", phone);
        intent.setType("vnd.android-dir/mms-sms");
        toActivity(context, intent);

    }
    //启动新Activity方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //显示与关闭进度弹窗方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 分享信息
     *
     * @param context
     * @param toShare
     */
    public static void shareInfo(Activity context, String toShare) {
        if (context == null || StringUtils.isNotEmpty(toShare, true) == false) {
            Log.e(TAG, "shareInfo  context == null || StringUtils.isNotEmpty(toShare, true) == false >> return;");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "选择分享方式");
        intent.putExtra(Intent.EXTRA_TEXT, toShare.trim());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        toActivity(context, intent, -1);
    }

    /**
     * 发送邮件
     *
     * @param context
     * @param emailAddress
     */
    public static void sendEmail(Activity context, String emailAddress) {
        if (context == null || StringUtils.isNotEmpty(emailAddress, true) == false) {
            Log.e(TAG, "sendEmail  context == null || StringUtils.isNotEmpty(emailAddress, true) == false >> return;");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + emailAddress));//缺少"mailto:"前缀导致找不到应用崩溃
        intent.putExtra(Intent.EXTRA_TEXT, "内容");  //最近在MIUI7上无内容导致无法跳到编辑邮箱界面
        toActivity(context, intent, -1);
    }

    /**
     * 打开网站
     *
     * @param context
     * @param webSite
     */
    public static void openWebSite(Activity context, String webSite) {
        if (context == null || StringUtils.isNotEmpty(webSite, true) == false) {
            Log.e(TAG, "openWebSite  context == null || StringUtils.isNotEmpty(webSite, true) == false >> return;");
            return;
        }

        if (!webSite.startsWith("http://") && !webSite.startsWith("https://")) {
            webSite = "http://" + webSite;
        }
        final Uri uri = Uri.parse(webSite);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        toActivity(context, intent, -1);
    }
    /**
     * @param dialog
     * @param dialog
     */

    /**
     * 复制文字
     *
     * @param context
     * @param value
     */
    public static void copyText(Context context, String value) {
        if (context == null || StringUtils.isNotEmpty(value, true) == false) {
            Log.e(TAG, "copyText  context == null || StringUtils.isNotEmpty(value, true) == false >> return;");
            return;
        }

        ClipData cD = ClipData.newPlainText("simple text", value);
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(cD);
        showShortToast(context, "已复制\n" + value);
    }

    /**
     * 打开新的Activity，向左滑入效果
     *
     * @param intent
     */
    public static void toActivity(final Activity context, final Intent intent) {
        toActivity(context, intent, true);
    }
    //显示与关闭进度弹窗方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //show short toast 方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 打开新的Activity
     *
     * @param intent
     * @param showAnimation
     */
    public static void toActivity(final Activity context, final Intent intent, final boolean showAnimation) {
        toActivity(context, intent, -1, showAnimation);
    }

    /**
     * 打开新的Activity，向左滑入效果
     *
     * @param intent
     * @param requestCode
     */
    public static void toActivity(final Activity context, final Intent intent, final int requestCode) {
        toActivity(context, intent, requestCode, true);
    }

    /**
     * 打开新的Activity
     *
     * @param intent
     * @param requestCode
     * @param showAnimation
     */
    public static void toActivity(final Activity context, final Intent intent, final int requestCode, final boolean showAnimation) {
        if (context == null || intent == null) {
            return;
        }
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (requestCode < 0) {
                    context.startActivity(intent);
                } else {
                    context.startActivityForResult(intent, requestCode);
                }
                if (showAnimation) {
                    context.overridePendingTransition(R.anim.right_push_in, R.anim.hold);
                } else {
                    context.overridePendingTransition(R.anim.null_anim, R.anim.null_anim);
                }
            }
        });
    }
    //show short toast 方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     * 展示加载进度条,无标题
     *
     * @param stringResId
     */
    public static void showProgressDialog(Activity context, int stringResId) {
        try {
            showProgressDialog(context, null, context.getResources().getString(stringResId));
        } catch (Exception e) {
            Log.e(TAG, "showProgressDialog  showProgressDialog(Context context, null, context.getResources().getString(stringResId));");
        }
    }

    /**
     * 展示加载进度条
     *
     * @param context       上下文对象
     * @param dialogTitle   Title 标题
     * @param dialogMessage Message 信息
     */
    public static void showProgressDialog(final Activity context, final String dialogTitle, final String dialogMessage) {
        if (context == null) {
            return;
        }
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(context);
                }
                if (progressDialog.isShowing() == true) {
                    progressDialog.dismiss();
                }
                if (dialogTitle != null && !"".equals(dialogTitle.trim())) {
                    progressDialog.setTitle(dialogTitle);
                }
                if (dialogMessage != null && !"".equals(dialogMessage.trim())) {
                    progressDialog.setMessage(dialogMessage);
                }
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        });
    }

    /**
     * 隐藏加载进度
     */
    public static void dismissProgressDialog(Activity context) {
        if (context == null || progressDialog == null || progressDialog.isShowing() == false) {
            return;
        }
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
    }

    /**
     * 快捷显示short toast方法，需要long toast就用 Toast.makeText(string, Toast.LENGTH_LONG).show(); ---不常用所以这个类里不写
     *
     * @param context
     * @param stringResId
     */
    public static void showShortToast(final Context context, int stringResId) {
        try {
            showShortToast(context, context.getResources().getString(stringResId));
        } catch (Exception e) {
            Log.e(TAG, "showShortToast  context.getResources().getString(resId) >>  catch (Exception e) {" + e.getMessage());
        }
    }

    /**
     * 快捷显示short toast方法，需要long toast就用 Toast.makeText(string, Toast.LENGTH_LONG).show(); ---不常用所以这个类里不写
     *
     * @param string
     */
    public static void showShortToast(final Context context, final String string) {
        showShortToast(context, string, false);
    }

    /**
     * 快捷显示short toast方法，需要long toast就用 Toast.makeText(string, Toast.LENGTH_LONG).show(); ---不常用所以这个类里不写
     *
     * @param string
     * @param isForceDismissProgressDialog
     */
    public static void showShortToast(final Context context, final String string, final boolean isForceDismissProgressDialog) {
        if (context == null) {
            return;
        }
        Toast.makeText(context, "" + string, Toast.LENGTH_SHORT).show();
    }

    public static void startPhotoZoom(Activity context, int requestCode, String path, int width, int height) {
        startPhotoZoom(context, requestCode, Uri.fromFile(new File(path)), width, height);
    }

    /**
     * 照片裁剪
     *
     * @param context
     * @param requestCode
     * @param fileUri
     * @param width
     * @param height
     */
    public static void startPhotoZoom(Activity context, int requestCode, Uri fileUri, int width, int height) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(fileUri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("return-data", true);
        Log.i(TAG, "startPhotoZoom" + fileUri + " uri");
        toActivity(context, intent, requestCode);
    }

    /**
     * 保存照片到SD卡上面
     *
     * @param path
     * @param photoName
     * @param formSuffix
     * @param photoBitmap
     */
    public static String savePhotoToSDCard(String path, String photoName, String formSuffix, Bitmap photoBitmap) {
        if (photoBitmap == null || StringUtils.isNotEmpty(path, true) == false
                || StringUtils.isNotEmpty(StringUtils.getTrimedString(photoName)
                + StringUtils.getTrimedString(formSuffix), true) == false) {
            Log.e(TAG, "savePhotoToSDCard photoBitmap == null || StringUtils.isNotEmpty(path, true) == false" +
                    "|| StringUtils.isNotEmpty(photoName, true) == false) >> return null");
            return null;
        }

        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File photoFile = new File(path, photoName + "." + formSuffix); // 在指定路径下创建文件
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                        fileOutputStream)) {
                    fileOutputStream.flush();
                    Log.i(TAG, "savePhotoToSDCard<<<<<<<<<<<<<<\n" + photoFile.getAbsolutePath() + "\n>>>>>>>>> succeed!");
                }
            } catch (FileNotFoundException e) {
                Log.e(TAG, "savePhotoToSDCard catch (FileNotFoundException e) {\n " + e.getMessage());
                photoFile.delete();
                //				e.printStackTrace();
            } catch (IOException e) {
                Log.e(TAG, "savePhotoToSDCard catch (IOException e) {\n " + e.getMessage());
                photoFile.delete();
                //				e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "savePhotoToSDCard } catch (IOException e) {\n " + e.getMessage());
                    //					e.printStackTrace();
                }
            }

            return photoFile.getAbsolutePath();
        }

        return null;
    }

    /**
     * 检测网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取顶层 Activity
     *
     * @param context
     * @return
     */
    public static String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        return runningTaskInfos == null ? "" : runningTaskInfos.get(0).topActivity.getClassName();
    }

    /**
     * 检查是否有位置权限
     *
     * @param context
     * @return
     */
    public static boolean isHasLocationPermission(Context context) {
        return isHasPermission(context, "android.permission.ACCESS_COARSE_LOCATION") || isHasPermission(context, "android.permission.ACCESS_FINE_LOCATION");
    }

    /**
     * 检查是否有权限
     *
     * @param context
     * @param name
     * @return
     */
    public static boolean isHasPermission(Context context, String name) {
        try {
            return PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission(name, context.getPackageName());
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }

    public static void openMobilePhone(String phoneNumber, Context mContext) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        mContext.startActivity(intent);
    }

    /**
     * 验证json合法性
     *
     * @param jsonContent
     * @return
     */
    public static boolean isJsonFormat(String jsonContent) {
        try {
            new JsonParser().parse(jsonContent);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }

    /**
     * 判断下载服务是否在运行
     *
     * @param context
     * @return
     */
    public static boolean isServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.huan.studyol.service.UpdateService1".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return 年月日
     */
    public static String formatDate(Date date) {
        return formatDate.format(date);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return 年月日 时分秒
     */
    public static String formatDateTime(Date date) {
        return formatDateTime.format(date);
    }

    /**
     * 将时间戳解析成日期
     *
     * @param timeInMillis
     * @return 年月日
     */
    public static String parseDate(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        Date date = calendar.getTime();
        return formatDate(date);
    }

    /**
     * 将时间戳解析成日期
     *
     * @param timeInMillis
     * @return 年月日 时分秒
     */
    public static String parseDateTime(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        Date date = calendar.getTime();
        return formatDateTime(date);
    }

    /**
     * 解析日期
     *
     * @param date
     * @return
     */
    public static Date parseDate(String date) {
        Date mDate = null;
        try {
            mDate = formatDate.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mDate;
    }

    /**
     * 解析日期
     *
     * @param datetime
     * @return
     */
    public static Date parseDateTime(String datetime) {
        Date mDate = null;
        try {
            mDate = formatDateTime.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mDate;
    }

    /**
     * 对指定字符串进行md5加密
     *
     * @param s
     * @return 加密后的数据
     */
    public static String EncryptMD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断email格式是否正确
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 根据系统语言判断是否为中国
     *
     * @return
     */
    public static boolean isZh() {
        Locale locale = BaseApplication.getContext().getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.startsWith("zh")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 获取gson对象
     *
     * @return
     */
    public static Gson getGson() {
        if (mGson == null) {
            mGson = new GsonBuilder().setDateFormat(GSON_FORMAT).create(); // 创建gson对象，并设置日期格式
        }

        return mGson;
    }

    /**
     * 调用震动器
     *
     * @param context      调用该方法的Context
     * @param milliseconds 震动的时长，单位是毫秒
     */
    public static void vibrate(final Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    /**
     * 调用震动器
     *
     * @param context  调用该方法的Context
     * @param pattern  自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     * @param isRepeat 是否反复震动，如果是true，反复震动，如果是false，只震动一次
     */
    public static void vibrate(final Context context, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }

    /**
     * 播放音乐
     *
     * @param context
     */
    public static void playMusic(Context context) {
        MediaPlayer mp = MediaPlayer.create(context, R.raw.beep);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
    }

    /**
     * @param rank
     * @return
     */
    public static String getLevelName(int rank) {
        String levelName;
        if (rank < 50) {
            levelName = "(1)初学乍练";
        } else if (rank < 110) {
            levelName = "(2)略知一二";
        } else if (rank < 180) {
            levelName = "(3)略有小成";
        } else if (rank < 260) {
            levelName = "(4)出类拔萃";
        } else if (rank < 350) {
            levelName = "(5)技冠群雄";
        } else if (rank < 490) {
            levelName = "(6)一代宗师";
        } else if (rank < 640) {
            levelName = "(7)登峰造极";
        } else if (rank < 900) {
            levelName = "(8)学霸";
        } else {
            levelName = "深不可测";

        }
        return levelName;
    }

    /**
     * 展示加载进度条,无标题
     *
     * @param dialogMessage
     */
    public void showProgressDialog(Activity context, String dialogMessage) {
        showProgressDialog(context, null, dialogMessage);
    }

    /**
     * 获取联系人电话
     *
     * @param cursor
     * @return
     */
    private String getContactPhone(Context context, Cursor cursor) {

        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        String phoneResult = "";
        //System.out.print(phoneNum);
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人的电话号码的cursor;
            Cursor phones = context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null, null);
            //int phoneCount = phones.getCount();
            //allPhoneNum = new ArrayList<String>(phoneCount);
            if (phones.moveToFirst()) {
                // 遍历所有的电话号码
                for (; !phones.isAfterLast(); phones.moveToNext()) {
                    int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int phone_type = phones.getInt(typeindex);
                    String phoneNumber = phones.getString(index);
                    switch (phone_type) {
                        case 2:
                            phoneResult = phoneNumber;
                            break;
                    }
                    //allPhoneNum.add(phoneNumber);
                }
                if (!phones.isClosed()) {
                    phones.close();
                }
            }
        }
        return phoneResult;
    }

}
