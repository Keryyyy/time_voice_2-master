package cn.sinata.xldutils.utils;

import android.text.TextUtils;

import java.util.Collection;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by LiaoXiang on 2015/11/18.
 */
public class StringUtils {
    /**
     * 是否为空字符或null
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(CharSequence str) {
        return TextUtils.isEmpty(str) || TextUtils.equals(str, "null");
    }

    public static boolean isNull(CharSequence str) {
        return str == null || TextUtils.equals(str, "null");
    }

    /**
     * 检查字符串是否为电话号码的方法,并返回true or false的判断值
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 11) {
            return false;
        }
        String expression = "(^(1[3-9])[0-9]{9}$)";
//        String expression = "^((13[0-9])|(14[4,5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199)\\d{8}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(phoneNumber);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 判断邮箱格式是否有效
     *
     * @param email 邮箱地址
     * @return true 是正确格式
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;
//        String expression = "^([a-z0-9A-Z]+[-|\\\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[\n\t]"; //要过滤掉的字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 隐藏手机号展示
     *
     * @param phone 手机号
     * @return
     */
    public static String hidePhoneNumber(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return phone;
        }
        if (phone.length() < 7) {
            return phone;
        }
        return phone.substring(0, 3) +
                "****" +
                phone.substring(phone.length() - 4, phone.length());
    }

    public static String hideCarNumber(String carNum) {
        if (TextUtils.isEmpty(carNum)) {
            return carNum;
        }
        if (carNum.length() < 5) {
            return carNum;
        }
        return carNum.substring(0, 2) +
                "***" +
                carNum.substring(carNum.length() - 3, carNum.length());
    }

    public static String hideIDCardNumber(String idCardNum) {
        if (TextUtils.isEmpty(idCardNum)) {
            return idCardNum;
        }
        if (idCardNum.length() < 10) {
            return idCardNum;
        }
        return idCardNum.substring(0, 6) +
                "********" +
                idCardNum.substring(idCardNum.length() - 4, idCardNum.length());
    }

    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

    public static String formatMoneyString(String s, double money) {
        return String.format(Locale.CHINA, s, money);
    }

    public static String formatChineseNum(int num){
        String s = String.valueOf(num);
        String rlt;
        if (s.length() == 1){
            rlt = formatSingleNum(s.charAt(0),true);
        }else if (s.length() == 2){
            char c = s.charAt(0);
            if (c=='1'){
                rlt = "十"+ formatSingleNum(s.charAt(1),false);
            }else {
                rlt = formatSingleNum(s.charAt(0),false)+"十"+ formatSingleNum(s.charAt(1),false);
            }
        }else{
            if (s.endsWith("00"))
                rlt = formatSingleNum(s.charAt(0),false)+"百";
            else if (s.endsWith("0"))
                rlt = formatSingleNum(s.charAt(0),false)+"百"+formatSingleNum(s.charAt(1),false)+"十";
            else if (s.charAt(1) == '0')
                rlt = formatSingleNum(s.charAt(0),false)+"百零"+formatSingleNum(s.charAt(2),true);
            else
                rlt = formatSingleNum(s.charAt(0),false)+"百"+formatSingleNum(s.charAt(1),false)+"十"+formatSingleNum(s.charAt(2),true);
        }
        return rlt;
    }

    private static String formatSingleNum(char c,boolean isMid){
        String rlt;
        switch (c){
            case '1':
                rlt = "一";
                break;
            case '2':
                rlt = "二";
                break;
            case '3':
                rlt = "三";
                break;
            case '4':
                rlt = "四";
                break;
            case '5':
                rlt = "五";
                break;
            case '6':
                rlt = "六";
                break;
            case '7':
                rlt = "七";
                break;
            case '8':
                rlt = "八";
                break;
            case '9':
                rlt = "九";
                break;
            default:
                rlt = isMid?"零":"";
                break;
        }
        return rlt;
    }
}
