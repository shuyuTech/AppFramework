package core.utils.utils;

import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/8/16
 *     desc  : 字符串相关工具类
 * </pre>
 */
public class StringUtils {
    public static final String EMPTY = "无";
    public static final String UNKNOWN = "未知";
    public static final String UNLIMITED = "不限";
    public static final String I = "我";
    public static final String YOU = "你";
    public static final String HE = "他";
    public static final String SHE = "她";
    public static final String IT = "它";
    public static final String MALE = "男";
    public static final String FEMALE = "女";
    public static final String TODO = "未完成";
    public static final String DONE = "已完成";
    public static final String FAIL = "失败";
    public static final String SUCCESS = "成功";
    public static final String SUNDAY = "日";
    public static final String MONDAY = "一";
    public static final String TUESDAY = "二";
    public static final String WEDNESDAY = "三";
    public static final String THURSDAY = "四";
    public static final String FRIDAY = "五";
    public static final String SATURDAY = "六";
    public static final String YUAN = "元";
    public static final String HTTP = "http";
    public static final String URL_PREFIX = "http://";
    public static final String URL_PREFIXs = "https://";
    public static final String URL_STAFFIX = URL_PREFIX;
    public static final String URL_STAFFIXs = URL_PREFIXs;
    public static final String FILE_PATH_PREFIX = "file://";
    public static final int PRICE_FORMAT_DEFAULT = 0;
    public static final int PRICE_FORMAT_PREFIX = 1;
    public static final int PRICE_FORMAT_SUFFIX = 2;
    public static final int PRICE_FORMAT_PREFIX_WITH_BLANK = 3;
    public static final int PRICE_FORMAT_SUFFIX_WITH_BLANK = 4;
    public static final String[] PRICE_FORMATS = {
            "", "￥", "元", "￥ ", " 元"
    };
    private static final String TAG = "StringUtils";

    //获取string,为null时返回"" <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private final static Pattern EMAILER = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static ThreadLocal<SimpleDateFormat> DATE_FORMATER = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    private static final String _BR = "<br/>";
    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    //获取string,为null时返回"" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //获取去掉前后空格后的string<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private static String currentString = "";

    private StringUtils() {
        throw new UnsupportedOperationException("u can't fuck me...");
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    //获取去掉前后空格后的string>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //获取去掉所有空格后的string <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    //获取去掉所有空格后的string >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //获取string的长度<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(String s) {
        int len = length(s);
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 获取刚传入处理后的string
     *
     * @return
     * @must 上个影响currentString的方法 和 这个方法都应该在同一线程中，否则返回值可能不对
     */
    public static String getCurrentString() {
        return currentString == null ? "" : currentString;
    }

    //获取string的长度>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //判断字符是否非空 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 获取string,为null则返回""
     *
     * @param tv
     * @return
     */
    public static String getString(TextView tv) {
        if (tv == null || tv.getText() == null) {
            return "";
        }
        return getString(tv.getText().toString());
    }

    /**
     * 获取string,为null则返回""
     *
     * @param object
     * @return
     */
    public static String getString(Object object) {
        return object == null ? "" : getString(String.valueOf(object));
    }

    /**
     * 获取string,为null则返回""
     *
     * @param cs
     * @return
     */
    public static String getString(CharSequence cs) {
        return cs == null ? "" : getString(cs.toString());
    }

    /**
     * 获取string,为null则返回""
     *
     * @param s
     * @return
     */
    public static String getString(String s) {
        return s == null ? "" : s;
    }

    //判断字符是否非空 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //判断字符类型 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 获取去掉前后空格后的string,为null则返回""
     *
     * @param tv
     * @return
     */
    public static String getTrimedString(TextView tv) {
        return getTrimedString(getString(tv));
    }

    /**
     * 获取去掉前后空格后的string,为null则返回""
     *
     * @param object
     * @return
     */
    public static String getTrimedString(Object object) {
        return getTrimedString(getString(object));
    }

    /**
     * 获取去掉前后空格后的string,为null则返回""
     *
     * @param cs
     * @return
     */
    public static String getTrimedString(CharSequence cs) {
        return getTrimedString(getString(cs));
    }

    /**
     * 获取去掉前后空格后的string,为null则返回""
     *
     * @param s
     * @return
     */
    public static String getTrimedString(String s) {
        return getString(s).trim();
    }

    /**
     * 获取去掉所有空格后的string,为null则返回""
     *
     * @param tv
     * @return
     */
    public static String getNoBlankString(TextView tv) {
        return getNoBlankString(getString(tv));
    }

    /**
     * 获取去掉所有空格后的string,为null则返回""
     *
     * @param object
     * @return
     */
    public static String getNoBlankString(Object object) {
        return getNoBlankString(getString(object));
    }

    /**
     * 获取去掉所有空格后的string,为null则返回""
     *
     * @param cs
     * @return
     */
    public static String getNoBlankString(CharSequence cs) {
        return getNoBlankString(getString(cs));
    }

    /**
     * 获取去掉所有空格后的string,为null则返回""
     *
     * @param s
     * @return
     */
    public static String getNoBlankString(String s) {
        return getString(s).replaceAll(" ", "");
    }

    /**
     * 获取string的长度,为null则返回0
     *
     * @param tv
     * @param trim
     * @return
     */
    public static int getLength(TextView tv, boolean trim) {
        return getLength(getString(tv), trim);
    }

    /**
     * 获取string的长度,为null则返回0
     *
     * @param object
     * @param trim
     * @return
     */
    public static int getLength(Object object, boolean trim) {
        return getLength(getString(object), trim);
    }

    /**
     * 获取string的长度,为null则返回0
     *
     * @param cs
     * @param trim
     * @return
     */
    public static int getLength(CharSequence cs, boolean trim) {
        return getLength(getString(cs), trim);
    }

    /**
     * 获取string的长度,为null则返回0
     *
     * @param s
     * @param trim
     * @return
     */
    public static int getLength(String s, boolean trim) {
        s = trim ? getTrimedString(s) : s;
        return getString(s).length();
    }

    /**
     * 判断字符是否非空
     *
     * @param tv
     * @param trim
     * @return
     */
    public static boolean isNotEmpty(TextView tv, boolean trim) {
        return isNotEmpty(getString(tv), trim);
    }

    /**
     * 判断字符是否非空
     *
     * @param object
     * @param trim
     * @return
     */
    public static boolean isNotEmpty(Object object, boolean trim) {
        return isNotEmpty(getString(object), trim);
    }

    //判断字符类型 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //提取特殊字符<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 判断字符是否非空
     *
     * @param cs
     * @param trim
     * @return
     */
    public static boolean isNotEmpty(CharSequence cs, boolean trim) {
        return isNotEmpty(getString(cs), trim);
    }

    /**
     * 判断字符是否非空
     *
     * @param s
     * @param trim
     * @return
     */
    public static boolean isNotEmpty(String s, boolean trim) {
        //		L.i(TAG, "getTrimedString   s = " + s);
        if (s == null) {
            return false;
        }
        if (trim) {
            s = s.trim();
        }
        if (s.length() <= 0) {
            return false;
        }

        currentString = s;

        return true;
    }

    //判断手机格式是否正确
    public static boolean isPhone(String phone) {
        if (isNotEmpty(phone, true) == false) {
            return false;
        }

        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-2,5-9])|(17[0-9]))\\d{8}$");

        currentString = phone;

        return p.matcher(phone).matches();
    }

    //判断email格式是否正确
    public static boolean isEmail(String email) {
        if (isNotEmpty(email, true) == false) {
            return false;
        }

        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);

        currentString = email;

        return p.matcher(email).matches();
    }

    //提取特殊字符>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //校正（自动补全等）字符串<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    //判断是否全是数字
    public static boolean isNumer(String number) {
        if (isNotEmpty(number, true) == false) {
            return false;
        }

        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(number);
        if (!isNum.matches()) {
            return false;
        }

        currentString = number;

        return true;
    }

    /**
     * 判断字符类型是否是号码或字母
     *
     * @param inputed
     * @return
     */
    public static boolean isNumberOrAlpha(String inputed) {
        if (inputed == null) {
            Log.e(TAG, "isNumberOrAlpha  inputed == null >> return false;");
            return false;
        }
        Pattern pNumber = Pattern.compile("[0-9]*");
        Matcher mNumber;
        Pattern pAlpha = Pattern.compile("[a-zA-Z]");
        Matcher mAlpha;
        for (int i = 0; i < inputed.length(); i++) {
            mNumber = pNumber.matcher(inputed.substring(i, i + 1));
            mAlpha = pAlpha.matcher(inputed.substring(i, i + 1));
            if (!mNumber.matches() && !mAlpha.matches()) {
                return false;
            }
        }

        currentString = inputed;
        return true;
    }

    /**
     * 判断字符类型是否是身份证号
     *
     * @param idCard
     * @return
     */
    public static boolean isIDCard(String idCard) {
        if (isNumberOrAlpha(idCard) == false) {
            return false;
        }
        idCard = getString(idCard);
        if (idCard.length() == 15) {
            Log.w(TAG, "isIDCard idCard.length() == 15 old IDCard");
            currentString = idCard;
            return true;
        }
        if (idCard.length() == 18) {
            currentString = idCard;
            return true;
        }

        return false;
    }

    /**
     * 判断字符类型是否是网址
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        if (isNotEmpty(url, true) == false) {
            return false;
        } else if (!url.startsWith(URL_PREFIX) && !url.startsWith(URL_PREFIXs)) {
            return false;
        }

        currentString = url;
        return true;
    }

    /**
     * 判断文件路径是否存在
     *
     * @param path
     * @return
     */
    public static boolean isFilePathExist(String path) {
        return StringUtils.isFilePath(path) && new File(path).exists();
    }

    /**
     * 判断字符类型是否是路径
     *
     * @param path
     * @return
     */
    public static boolean isFilePath(String path) {
        if (isNotEmpty(path, true) == false) {
            return false;
        }

        if (!path.contains(".") || path.endsWith(".")) {
            return false;
        }

        currentString = path;

        return true;
    }

    /**
     * 去掉string内所有非数字类型字符
     *
     * @param tv
     * @return
     */
    public static String getNumber(TextView tv) {
        return getNumber(getString(tv));
    }

    /**
     * 去掉string内所有非数字类型字符
     *
     * @param object
     * @return
     */
    public static String getNumber(Object object) {
        return getNumber(getString(object));
    }

    /**
     * 去掉string内所有非数字类型字符
     *
     * @param cs
     * @return
     */
    public static String getNumber(CharSequence cs) {
        return getNumber(getString(cs));
    }

    /**
     * 去掉string内所有非数字类型字符
     *
     * @param s
     * @return
     */
    public static String getNumber(String s) {
        if (isNotEmpty(s, true) == false) {
            return "";
        }

        String numberString = "";
        String single;
        for (int i = 0; i < s.length(); i++) {
            single = s.substring(i, i + 1);
            if (isNumer(single)) {
                numberString += single;
            }
        }

        return numberString;
    }

    /**
     * 获取网址，自动补全
     *
     * @param tv
     * @return
     */
    public static String getCorrectUrl(TextView tv) {
        return getCorrectUrl(getString(tv));
    }

    /**
     * 获取网址，自动补全
     *
     * @param url
     * @return
     */
    public static String getCorrectUrl(String url) {
        Log.i(TAG, "getCorrectUrl : \n" + url);
        if (isNotEmpty(url, true) == false) {
            return "";
        }

        if (!url.endsWith("/") && !url.endsWith(".html")) {
            url = url + "/";
        }

        if (isUrl(url) == false) {
            return URL_PREFIX + url;
        }
        return url;
    }

    /**
     * 获取去掉所有 空格 、"-" 、"+86" 后的phone
     *
     * @param tv
     * @return
     */
    public static String getCorrectPhone(TextView tv) {
        return getCorrectPhone(getString(tv));
    }

    /**
     * 获取去掉所有 空格 、"-" 、"+86" 后的phone
     *
     * @param phone
     * @return
     */
    public static String getCorrectPhone(String phone) {
        if (isNotEmpty(phone, true) == false) {
            return "";
        }

        phone = getNoBlankString(phone);
        phone = phone.replaceAll("-", "");
        if (phone.startsWith("+86")) {
            phone = phone.substring(3);
        }
        return phone;
    }

    /**
     * 获取邮箱，自动补全
     *
     * @param tv
     * @return
     */
    public static String getCorrectEmail(TextView tv) {
        return getCorrectEmail(getString(tv));
    }

    /**
     * 获取邮箱，自动补全
     *
     * @param email
     * @return
     */
    public static String getCorrectEmail(String email) {
        if (isNotEmpty(email, true) == false) {
            return "";
        }

        email = getNoBlankString(email);
        if (isEmail(email) == false && !email.endsWith(".com")) {
            email += ".com";
        }

        return email;
    }

    /**
     * 获取价格，保留两位小数
     *
     * @param price
     * @return
     */
    public static String getPrice(String price) {
        return getPrice(price, PRICE_FORMAT_DEFAULT);
    }

    /**
     * 获取价格，保留两位小数
     *
     * @param price
     * @param formatType 添加单位（元）
     * @return
     */
    public static String getPrice(String price, int formatType) {
        if (isNotEmpty(price, true) == false) {
            return getPrice(0, formatType);
        }

        //单独写到getCorrectPrice? <<<<<<<<<<<<<<<<<<<<<<
        String correctPrice = "";
        String s;
        for (int i = 0; i < price.length(); i++) {
            s = price.substring(i, i + 1);
            if (".".equals(s) || isNumer(s)) {
                correctPrice += s;
            }
        }
        //单独写到getCorrectPrice? >>>>>>>>>>>>>>>>>>>>>>

        Log.i(TAG, "getPrice  <<<<<<<<<<<<<<<<<< correctPrice =  " + correctPrice);
        if (correctPrice.contains(".")) {
//			if (correctPrice.startsWith(".")) {
//				correctPrice = 0 + correctPrice;
//			}
            if (correctPrice.endsWith(".")) {
                correctPrice = correctPrice.replaceAll(".", "");
            }
        }

        Log.i(TAG, "getPrice correctPrice =  " + correctPrice + " >>>>>>>>>>>>>>>>");
        return isNotEmpty(correctPrice, true) ? getPrice(new BigDecimal(0 + correctPrice), formatType) : getPrice(0, formatType);
    }


    //校正（自动补全等）字符串>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     * 获取价格，保留两位小数
     *
     * @param price
     * @return
     */
    public static String getPrice(BigDecimal price) {
        return getPrice(price, PRICE_FORMAT_DEFAULT);
    }

    /**
     * 获取价格，保留两位小数
     *
     * @param price
     * @return
     */
    public static String getPrice(double price) {
        return getPrice(price, PRICE_FORMAT_DEFAULT);
    }

    /**
     * 获取价格，保留两位小数
     *
     * @param price
     * @param formatType 添加单位（元）
     * @return
     */
    public static String getPrice(BigDecimal price, int formatType) {
        return getPrice(price == null ? 0 : price.doubleValue(), formatType);
    }

    /**
     * 获取价格，保留两位小数
     *
     * @param price
     * @param formatType 添加单位（元）
     * @return
     */
    public static String getPrice(double price, int formatType) {
        String s = new DecimalFormat("#########0.00").format(price);
        switch (formatType) {
            case PRICE_FORMAT_PREFIX:
                return PRICE_FORMATS[PRICE_FORMAT_PREFIX] + s;
            case PRICE_FORMAT_SUFFIX:
                return s + PRICE_FORMATS[PRICE_FORMAT_SUFFIX];
            case PRICE_FORMAT_PREFIX_WITH_BLANK:
                return PRICE_FORMATS[PRICE_FORMAT_PREFIX_WITH_BLANK] + s;
            case PRICE_FORMAT_SUFFIX_WITH_BLANK:
                return s + PRICE_FORMATS[PRICE_FORMAT_SUFFIX_WITH_BLANK];
            default:
                return s;
        }
    }

    /**
     * 字符串截取
     *
     * @param str
     * @param length
     * @return
     * @throws Exception
     */
    public static String subString(String str, int length) throws Exception {

        byte[] bytes = str.getBytes("Unicode");
        int n = 0; // 表示当前的字节数
        int i = 2; // 要截取的字节数，从第3个字节开始
        for (; i < bytes.length && n < length; i++) {
            // 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
            if (i % 2 == 1) {
                n++; // 在UCS2第二个字节时n加1
            } else {
                // 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
                if (bytes[i] != 0) {
                    n++;
                }
            }
        }
        // 如果i为奇数时，处理成偶数
        if (i % 2 == 1) {
            // 该UCS2字符是汉字时，去掉这个截一半的汉字
            if (bytes[i - 1] != 0)
                i = i - 1;
                // 该UCS2字符是字母或数字，则保留该字符
            else
                i = i + 1;
        }
        return new String(bytes, 0, i, "Unicode");
    }

    /**
     * 计算微博内容的长度 1个汉字 == 两个英文字母所占的长度 标点符号区分英文和中文
     *
     * @param c 所要统计的字符序列
     * @return 返回字符序列计算的长度
     */
    public static long calculateWeiboLength(CharSequence c) {

        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int temp = (int) c.charAt(i);
            if (temp > 0 && temp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    /**
     * 分割字符串
     *
     * @param str       String 原始字符串
     * @param splitsign String 分隔符
     * @return String[] 分割后的字符串数组
     */
    public static String[] split(String str, String splitsign) {
        int index;
        if (str == null || splitsign == null)
            return null;
        ArrayList<String> al = new ArrayList<String>();
        while ((index = str.indexOf(splitsign)) != -1) {
            al.add(str.substring(0, index));
            str = str.substring(index + splitsign.length());
        }
        al.add(str);
        return al.toArray(new String[0]);
    }

    /**
     * 替换字符串
     *
     * @param from   String 原始字符串
     * @param to     String 目标字符串
     * @param source String 母字符串
     * @return String 替换后的字符串
     */
    public static String replace(String from, String to, String source) {
        if (source == null || from == null || to == null)
            return null;
        StringBuffer bf = new StringBuffer("");
        int index = -1;
        while ((index = source.indexOf(from)) != -1) {
            bf.append(source.substring(0, index) + to);
            source = source.substring(index + from.length());
            index = source.indexOf(from);
        }
        bf.append(source);
        return bf.toString();
    }

    /**
     * 替换字符串，能能够在HTML页面上直接显示(替换双引号和小于号)
     *
     * @param str String 原始字符串
     * @return String 替换后的字符串
     */
    public static String htmlencode(String str) {
        if (str == null) {
            return null;
        }

        return replace("\"", "&quot;", replace("<", "&lt;", str));
    }

    /**
     * 替换字符串，将被编码的转换成原始码（替换成双引号和小于号）
     *
     * @param str String
     * @return String
     */
    public static String htmldecode(String str) {
        if (str == null) {
            return null;
        }

        return replace("&quot;", "\"", replace("&lt;", "<", str));
    }

    /**
     * 在页面上直接显示文本内容，替换小于号，空格，回车，TAB
     *
     * @param str String 原始字符串
     * @return String 替换后的字符串
     */
    public static String htmlshow(String str) {
        if (str == null) {
            return null;
        }

        str = replace("<", "&lt;", str);
        str = replace(" ", "&nbsp;", str);
        str = replace("\r\n", _BR, str);
        str = replace("\n", _BR, str);
        str = replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;", str);
        return str;
    }

    /**
     * 返回指定字节长度的字符串
     *
     * @param str    String 字符串
     * @param length int 指定长度
     * @return String 返回的字符串
     */
    public static String toLength(String str, int length) {
        if (str == null) {
            return null;
        }
        if (length <= 0) {
            return "";
        }
        try {
            if (str.getBytes("GBK").length <= length) {
                return str;
            }
        } catch (Exception ex) {
        }
        StringBuffer buff = new StringBuffer();

        int index = 0;
        char c;
        length -= 3;
        while (length > 0) {
            c = str.charAt(index);
            if (c < 128) {
                length--;
            } else {
                length--;
                length--;
            }
            buff.append(c);
            index++;
        }
        buff.append("...");
        return buff.toString();
    }

    /**
     * 获取url的后缀名
     *
     * @param urlString
     * @return
     */
    public static String getUrlFileName(String urlString) {
        String fileName = urlString.substring(urlString.lastIndexOf("/"));
        fileName = fileName.substring(1, fileName.length());
        if (fileName.equalsIgnoreCase("")) {
            Calendar c = Calendar.getInstance();
            fileName = c.get(Calendar.YEAR) + "" + c.get(Calendar.MONTH) + ""
                    + c.get(Calendar.DAY_OF_MONTH) + ""
                    + c.get(Calendar.MINUTE);

        }
        return fileName;
    }

    public static String replaceSomeString(String str) {
        String dest = "";
        try {
            if (str != null) {
                str = str.replaceAll("\r", "");
                str = str.replaceAll("&gt;", ">");
                str = str.replaceAll("&ldquo;", "“");
                str = str.replaceAll("&rdquo;", "”");
                str = str.replaceAll("&#39;", "'");
                str = str.replaceAll("&nbsp;", "");
                str = str.replaceAll("<br\\s*/>", "\n");
                str = str.replaceAll("&quot;", "\"");
                str = str.replaceAll("&lt;", "<");
                str = str.replaceAll("&lsquo;", "《");
                str = str.replaceAll("&rsquo;", "》");
                str = str.replaceAll("&middot;", "·");
                str = str.replace("&mdash;", "—");
                str = str.replace("&hellip;", "…");
                str = str.replace("&amp;", "×");
                str = str.replaceAll("\\s*", "");
                str = str.trim();
                str = str.replaceAll("<p>", "\n      ");
                str = str.replaceAll("</p>", "");
                str = str.replaceAll("<div.*?>", "\n      ");
                str = str.replaceAll("</div>", "");
                dest = str;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return dest;
    }

    /**
     * 清除文本里面的HTML标签
     *
     * @param htmlStr
     * @return
     */
    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
        Log.v("htmlStr", htmlStr);
        try {
            Pattern p_script = Pattern.compile(regEx_script,
                    Pattern.CASE_INSENSITIVE);
            Matcher m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            Pattern p_style = Pattern.compile(regEx_style,
                    Pattern.CASE_INSENSITIVE);
            Matcher m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签

            Pattern p_html = Pattern.compile(regEx_html,
                    Pattern.CASE_INSENSITIVE);
            Matcher m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
        } catch (Exception e) {
            // TODO: handle exception
        }

        return htmlStr; // 返回文本字符串
    }

    public static String delSpace(String str) {
        if (str != null) {
            str = str.replaceAll("\r", "");
            str = str.replaceAll("\n", "");
            str = str.replace(" ", "");
        }
        return str;
    }

    /**
     * 检查字符串是否存在值，如果为true,
     *
     * @param str 待检验的字符串
     * @return 当 str 不为 null 或 "" 就返回 true
     */
    public static boolean isNotNull(String str) {
        return (str != null && !"".equalsIgnoreCase(str.trim()));
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return DATE_FORMATER.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String friendly_time(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }

    public static String trimmy(String str) {
        String dest = "";
        if (str != null) {
            str = str.replaceAll("-", "");
            str = str.replaceAll("\\+", "");
            dest = str;
        }
        return dest;
    }

    public static String replaceBlank(String str) {

        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\r");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date(0);
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }


    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断是不是合法手机 handset 手机号码
     */
    public static boolean isHandset(String handset) {
        try {
            if (!handset.substring(0, 1).equals("1")) {
                return false;
            }
            if (handset == null || handset.length() != 11) {
                return false;
            }
            String check = "^[0123456789]+$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(handset);
            boolean isMatched = matcher.matches();
            if (isMatched) {
                return true;
            } else {
                return false;
            }
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * 判断输入的字符串是否为纯汉字
     *
     * @param str 传入的字符窜
     * @return 如果是纯汉字返回true, 否则返回false
     */
    public static boolean isChinese(String str) {
        Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为浮点数，包括double和float
     *
     * @param str 传入的字符串
     * @return 是浮点数返回true, 否则返回false
     */
    public static boolean isDouble(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 是否为空白,包括null和""
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断是否是指定长度的字符串
     *
     * @param text   字符串
     * @param lenght 自定的长度
     * @return
     */
    public static boolean isLenghtStrLentht(String text, int lenght) {
        if (text.length() <= lenght)
            return true;
        else
            return false;
    }

    /**
     * 是否是短信的长度
     *
     * @param text
     * @return
     */
    public static boolean isSMSStrLentht(String text) {
        if (text.length() <= 70)
            return true;
        else
            return false;
    }

    /**
     * 判断手机号码是否正确
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        phoneNumber = trimmy(phoneNumber);
        NumberUtils mobile = new NumberUtils(phoneNumber);
        return mobile.isLawful();
    }

    // 判断是否为url
    public static boolean checkEmail(String email) {

        Pattern pattern = Pattern
                .compile("^\\w+([-.]\\w+)*@\\w+([-]\\w+)*\\.(\\w+([-]\\w+)*\\.)*[a-z]{2,3}$");
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    // 判断微博分享是否为是否为120个
    public static boolean isShareStrLentht(String text, int lenght) {
        if (text.length() <= 120)
            return true;
        else
            return false;
    }

    public static String getFileNameFromUrl(String url) {

        // 名字不能只用这个
        // 通过 ‘？’ 和 ‘/’ 判断文件名
        String extName = "";
        String filename;
        int index = url.lastIndexOf('?');
        if (index > 1) {
            extName = url.substring(url.lastIndexOf('.') + 1, index);
        } else {
            extName = url.substring(url.lastIndexOf('.') + 1);
        }
        filename = hashKeyForDisk(url) + "." + extName;
        return filename;
        /*
		 * int index = url.lastIndexOf('?'); String filename; if (index > 1) {
		 * filename = url.substring(url.lastIndexOf('/') + 1, index); } else {
		 * filename = url.substring(url.lastIndexOf('/') + 1); }
		 *
		 * if (filename == null || "".equals(filename.trim())) {// 如果获取不到文件名称
		 * filename = UUID.randomUUID() + ".apk";// 默认取一个文件名 } return filename;
		 */
    }

    /**
     * 一个散列方法,改变一个字符串(如URL)到一个散列适合使用作为一个磁盘文件名。
     */
    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 获取去除了url参数的简单url
     *
     * @param url
     * @return
     */
    public static String getSimpleUrl(String url) {
        String simpleUrl = url.replaceAll("\\?.*", "");
        return simpleUrl;
    }

}