package com.openjava.datalake.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**

 (?:pattern)
 非获取匹配，匹配pattern但不获取匹配结果，不进行存储供以后使用。这在使用或字符“(|)”来组合一个模式的各个部分是很有用。例如“industr(?:y|ies)”就是一个比“industry|industries”更简略的表达式。
 (?=pattern)
 非获取匹配，正向肯定预查，在任何匹配pattern的字符串开始处匹配查找字符串，该匹配不需要获取供以后使用。例如，“Windows(?=95|98|NT|2000)”能匹配“Windows2000”中的“Windows”，但不能匹配“Windows3.1”中的“Windows”。预查不消耗字符，也就是说，在一个匹配发生后，在最后一次匹配之后立即开始下一次匹配的搜索，而不是从包含预查的字符之后开始。
 (?!pattern)
 非获取匹配，正向否定预查，在任何不匹配pattern的字符串开始处匹配查找字符串，该匹配不需要获取供以后使用。例如“Windows(?!95|98|NT|2000)”能匹配“Windows3.1”中的“Windows”，但不能匹配“Windows2000”中的“Windows”。
 (?<=pattern)
 非获取匹配，反向肯定预查，与正向肯定预查类似，只是方向相反。例如，“(?<=95|98|NT|2000)Windows”能匹配“2000Windows”中的“Windows”，但不能匹配“3.1Windows”中的“Windows”。
 (?<!pattern)
 非获取匹配，反向否定预查，与正向否定预查类似，只是方向相反。例如“(?<!95|98|NT|2000)Windows”能匹配“3.1Windows”中的“Windows”，但不能匹配“2000Windows”中的“Windows”。这个地方不正确，有问题

 */

/**
 * @Author xjd
 * @Date 2019/7/2 19:38
 * @Version 1.0
 */
public class InsensitivesUtils {
    private static final Logger LOG = LoggerFactory.getLogger(InsensitivesUtils.class);
    public static final String INVALID_SOURCE = "invalid";
    // 身份证脱敏规则正则表达式
    public static final String PROTECT_IDCARD_REGEX_18 = "(?<=\\w{6})\\w(?=\\w{4})"; // 先反向匹配，三个字符之后的字符，再正向匹配四个字符之前的字符，最后就是得到3个字符和4个字符夹着的所有字符
    // 手机号脱敏规则正则表达式
    public static final String PROTECT_PHONE_NUM_REGEX = "(?<=\\d{3})\\d"; // 匹配前三个数字，保留，后面全部屏蔽
    // 护照脱敏规则正则表达式
    public static final String PROTECT_PASSPORT_REGEX = "(?<=\\w{2})\\d(?=\\w{3})"; // 匹配前三个数字，中间4个，后面四个，保留括号的，过滤中间的
    // 银行卡脱敏规则正则表达式
    public static final String PROTECT_BANK_CARD_REGEX_16 = "(?<=\\d{4})\\d(?=\\d{4})"; // 查找前4个数字，保留，匹配中间
    public static final String PROTECT_BANK_CARD_REGEX_18 = "(?<=\\d{4})\\d(?=\\d{6})"; // 匹配前4个数字，中间4个，后面四个，保留括号的，过滤中间的
    // 脱敏地址
    public static final String PROTECT_ADDRESS = "(?<=市)(.)"; // 保留 xx市 屏蔽后面全部
    // 脱敏所属城市
    public static final String PROTECT_CITY = "(.)"; // 全脱敏
    // 脱敏密码类
    public static final String PROTECT_PASSWORD = "(.)"; // 全脱敏
    // 脱敏邮编
    public static final String PROTECT_POSTCODE = "^.{3}";
    // 营业执照号码
    public static final String PROTECT_BUSINESS_LICENSE_15 = "(?<=.{6}).";
    public static final String PROTECT_BUSINESS_LICENSE_18 = "(?<=.{8}).";
    // 邮箱地址
    public static final String PROTECT_EMAIL = "\\w*(?=\\w{3}@\\w+\\.\\w+)";

    /**
     * 电子邮箱脱敏
     * @param sourceString
     * @return
     */
    public static String protectEmail(String sourceString) {
        if (StringUtils.isBlank(sourceString)) {
            return sourceString;
        }
        String afterInsensitives = sourceString.replaceAll(PROTECT_EMAIL, "*");
        return afterInsensitives;
    }

    /**
     * 营业执照脱敏号码
     * @param sourceString
     * @return
     */
    public static String protectBusinessLicense(String sourceString) {
        if (StringUtils.isBlank(sourceString)) {
            return sourceString;
        }
        boolean matches15w = sourceString.matches("\\w{15}");
        boolean matches18w = sourceString.matches("\\w{18}");
        String afterInsensitives;
        if (matches15w) {
            afterInsensitives = sourceString.replaceAll(PROTECT_BUSINESS_LICENSE_15, "*");
        } else if (matches18w) {
            afterInsensitives = sourceString.replaceAll(PROTECT_BUSINESS_LICENSE_18, "*");
        } else {
            afterInsensitives = sourceString.replaceAll(PROTECT_BUSINESS_LICENSE_15, "*");
        }
        return afterInsensitives;
    }

    /**
     * 密码脱敏
     * @param sourceString
     * @return
     */
    public static String protectPassword(String sourceString) {
        if (StringUtils.isBlank(sourceString)) {
            return sourceString;
        }
        String afterInsensitives = sourceString.replaceAll(PROTECT_PASSWORD, "*");
        return afterInsensitives;
    }

    /**
     * 脱敏邮编
     * @param sourceString
     * @return
     */
    public static String protectPostcode(String sourceString) {
        if (StringUtils.isBlank(sourceString)) {
            return sourceString;
        }
        String afterInsensitives = sourceString.replaceAll(PROTECT_POSTCODE, "***");
        return afterInsensitives;
    }

    /**
     * 脱敏所属城市
     * @param sourceString
     * @return
     */
    public static String protectCity(String sourceString) {
        if (StringUtils.isBlank(sourceString)) {
            return sourceString;
        }
        String afterInsensitives = sourceString.replaceAll(PROTECT_CITY, "*");
        return afterInsensitives;
    }

    /**
     * 脱敏地址
     * @param sourceString
     * @return
     */
    public static String protectAddress(String sourceString) {
        if (StringUtils.isBlank(sourceString)) {
            return sourceString;
        }
        String afterInsensitives = sourceString.replaceAll(PROTECT_ADDRESS, "*");
        return afterInsensitives;
    }

    /**
     * 脱敏银行卡号
     * @param sourceString
     * @return
     */
    public static String protectBankCard(String sourceString) {
        if (StringUtils.isBlank(sourceString)) {
            return sourceString;
        }
        boolean matches16d = sourceString.matches("\\d{16}");
        boolean matches18d = sourceString.matches("\\d{18}");
        String afterInsensitives;
        if (matches16d) {
            afterInsensitives = sourceString.replaceAll(PROTECT_BANK_CARD_REGEX_16, "*");
        } else if (matches18d) {
            afterInsensitives = sourceString.replaceAll(PROTECT_BANK_CARD_REGEX_18, "*");
        } else {
            afterInsensitives = sourceString.replaceAll(PROTECT_BANK_CARD_REGEX_16, "*");
        }
        return afterInsensitives;
    }

    /**
     * 中序脱敏规则
     * @param sourceString
     * @param beginIndex
     * @param endIndex
     * @return
     */
    public static String protectMiddle(String sourceString, String beginIndex, String endIndex) {
        if (StringUtils.isBlank(sourceString)) {
            return sourceString;
        }
        int end;
        int len;
        try {
            end = sourceString.length() - Integer.parseInt(endIndex);
            end = end <= 0 ? 0 : end;
            len = Integer.parseInt(endIndex) - Integer.parseInt(beginIndex);
        } catch (NumberFormatException e){
            LOG.info("参数异常；屏蔽字符个数只接受数字");
            return null;
        }
        if (len < 0) {
            return null;
        }
        String regexPlafrom = "(?<=.{%s}).(?=.{%d})";
        String regex = String.format(regexPlafrom, beginIndex, end);
        String afterInsensitives = sourceString.replaceAll(regex, "*");
        return afterInsensitives;

    }

    /**
     * 右后序脱敏规则
     * @param sourceString
     * @param protectNum
     * @return
     */
    public static String protectEnd(String sourceString, String protectNum) {
        if (StringUtils.isBlank(sourceString)){
            return sourceString;
        }
        int protectNumInt;
        try {
            protectNumInt = Integer.parseInt(protectNum);
        } catch (NumberFormatException e){
            LOG.info("参数异常；屏蔽字符个数只接受数值类型");
            return null;
        }
        String regexPlafrom = ".{%d}$";
        String regex = String.format(regexPlafrom, protectNumInt);
        String replacement = (new String(new char[protectNumInt])).replace("\0", "*");
        if (sourceString.length() >= protectNumInt) {
            String afterInsensitives = sourceString.replaceAll(regex, replacement);
            return afterInsensitives;
        } else {
            return replacement;
        }
    }

    /**
     * （左）前序脱敏规则
     * @param sourceString
     * @param protectNum
     * @return
     */
    public static String protectFront(String sourceString, String protectNum) {
        if (StringUtils.isBlank(sourceString)){
            return sourceString;
        }
        int protectNumInt;
        try {
            protectNumInt = Integer.parseInt(protectNum);
        } catch (NumberFormatException e){
            LOG.info("参数异常；屏蔽字符个数只接受数值类型");
            return null;
        }
        String regexPlafrom = "^.{%d}";
        String regex = String.format(regexPlafrom, protectNumInt);
        String replacement = (new String(new char[protectNumInt])).replace("\0", "*");
        if (sourceString.length() >= protectNumInt) {
            String afterInsensitives = sourceString.replaceAll(regex, replacement);
            return afterInsensitives;
        } else {
            return replacement;
        }

    }

    /**
     * 姓名脱敏
     * @param sourceString
     * @return
     */
    public static String protectRealName(String sourceString){

        if (StringUtils.isBlank(sourceString) || sourceString.length() < 2) {
            return sourceString;
        }
        String afterInsensitives;
        String regex = "";
        if (sourceString.length() == 4) {
            regex = "(?<=.{2}).(?=.{1})";
        } else if (sourceString.length() == 2){
            regex = "(?<=.{1}).";
        } else {
            regex = "(?<=.{1}).(?=.{1})";
        }
        try {
            afterInsensitives = sourceString.replaceAll(regex, "*");
        } catch (Throwable e) {
            e.printStackTrace();
            LOG.error("正则表达式有问题");
            return null;
        }
        return afterInsensitives;
    }

    /**
     * 身份证脱敏
     * @param sourceString
     * @return
     */
    public static String protectIdCard(String sourceString) {
        if (StringUtils.isBlank(sourceString) || sourceString.length() < 8) {
            return sourceString;
        }
        String afterInsensitives;
        try {
            afterInsensitives = sourceString.replaceAll(PROTECT_IDCARD_REGEX_18, "*");
        } catch (Throwable e) {
            e.printStackTrace();
            LOG.info("正则表达式有问题");
            return null;
        }
        return afterInsensitives;
    }

    /**
     * 手机号码脱敏
     * @param sourceString
     * @return
     */
    public static String protectPhoneNum (String sourceString) {
        if (StringUtils.isBlank(sourceString) || sourceString.length() < 8) {
            return sourceString;
        }
        String afterInsensitives;
        try {
            afterInsensitives = sourceString.replaceAll(PROTECT_PHONE_NUM_REGEX, "*");
        } catch (Throwable e) {
            e.printStackTrace();
            LOG.info("正则表达式有问题");
            return null;
        }
        return afterInsensitives;
    }

    /**
     * 护照脱敏
     * @param sourceString
     * @return
     */
    public static String protectPassport (String sourceString) {
        if (StringUtils.isBlank(sourceString) || sourceString.length() < 7) {
            return sourceString;
        }
        String afterInsensitives;
        try {
            afterInsensitives = sourceString.replaceAll(PROTECT_PASSPORT_REGEX, "*");
        } catch (Throwable e) {
            e.printStackTrace();
            LOG.info("正则表达式有问题");
            return null;
        }
        return afterInsensitives;
    }



}
