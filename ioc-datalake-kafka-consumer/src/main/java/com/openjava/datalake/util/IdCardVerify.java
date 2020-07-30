package com.openjava.datalake.util;

import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;

import java.util.Calendar;

public class IdCardVerify {
    
    public static boolean isIDCard(String idcardNumber) throws APIException {
        if (idcardNumber.length() == 15 || idcardNumber.length() == 18) {
            if (!cardCodeVerifySimple(idcardNumber)) {
                throw new APIException(APIConstants.CODE_VERIFYCODE_ERR, "15位或18位身份证号码格式不正确");
//                return false;
            } else if (idcardNumber.length() == 18 && !cardCodeVerify(idcardNumber)) {
                throw new APIException(APIConstants.CODE_VERIFYCODE_ERR, "18位身份证号码不符合国家规范");
//              return false;
            }
        } else {
            throw new APIException(APIConstants.CODE_VERIFYCODE_ERR, "身份证号码长度必须等于15或18位");
//            return false;
        }        
        return true;
    }

    public static boolean isIDCardNoException(String idcardNumber){
        try {
            return isIDCard(idcardNumber);
        } catch (APIException e) {
            System.out.println("errCode:"+e.getCode()+"; errMsg:"+e.getMessage());
            return false;
        }
    }

    public static boolean cardCodeVerifySimple(String cardcode) {
        // TODO bugDone :440100 00 0000 004 通过验证 1900年00月00日的人
        //第一代身份证正则表达式(15位)
        String isIDCard1 = "^[1-9]\\d{7}((0[1-9])|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
        //第二代身份证正则表达式(18位)
        String isIDCard2 ="^[1-9]\\d{5}[1-9]\\d{3}((0[1-9])|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-Z])$";

        //验证身份证
        if (cardcode.matches(isIDCard1) || cardcode.matches(isIDCard2)) {
            return true;
        }
        return false;
    }
    
    public static boolean cardCodeVerify(String cardcode) {
        int[] weight={7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2}; // 十七位数字本体码权重
        char[] validate={ '1','0','X','9','8','7','6','5','4','3','2'}; // mod 11,对应校验码字符值
        
        int sum = 0;
        int mode = 0;
        for(int i = 0; i < cardcode.length()-1; i++) {
            sum += Integer.parseInt(String.valueOf(cardcode.charAt(i)))*weight[i];
        }
        mode = sum % 11;
        String sumChar = String.valueOf(validate[mode]);
        String codeChar = String.valueOf(cardcode.charAt(17));
        
        if(sumChar.equalsIgnoreCase(codeChar)) {
            return true;
        }else {
            return false;
        }            
    }
    
    public static int getAgeByIdCard(String idcardNumber) {
    	   int iAge = 0;
           Calendar cal = Calendar.getInstance();
           String year = idcardNumber.substring(6, 10);
           int iCurrYear = cal.get(Calendar.YEAR);
           iAge = iCurrYear - Integer.valueOf(year);
           return iAge;
    }

    public static void main(String[] args) {
        try {
            boolean idCard = IdCardVerify.isIDCard("440100000000004");
            System.out.println("验证结果：" + idCard);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

    /*
    public static boolean cardCodeVerify(String cardcode) {
        int i = 0;
        String r = "error";
        String lastnumber = "";

        i += Integer.parseInt(cardcode.substring(0, 1)) * 7;
        i += Integer.parseInt(cardcode.substring(1, 2)) * 9;
        i += Integer.parseInt(cardcode.substring(2, 3)) * 10;
        i += Integer.parseInt(cardcode.substring(3, 4)) * 5;
        i += Integer.parseInt(cardcode.substring(4, 5)) * 8;
        i += Integer.parseInt(cardcode.substring(5, 6)) * 4;
        i += Integer.parseInt(cardcode.substring(6, 7)) * 2;
        i += Integer.parseInt(cardcode.substring(7, 8)) * 1;
        i += Integer.parseInt(cardcode.substring(8, 9)) * 6;
        i += Integer.parseInt(cardcode.substring(9, 10)) * 3;
        i += Integer.parseInt(cardcode.substring(10,11)) * 7;
        i += Integer.parseInt(cardcode.substring(11,12)) * 9;
        i += Integer.parseInt(cardcode.substring(12,13)) * 10;
        i += Integer.parseInt(cardcode.substring(13,14)) * 5;
        i += Integer.parseInt(cardcode.substring(14,15)) * 8;
        i += Integer.parseInt(cardcode.substring(15,16)) * 4;
        i += Integer.parseInt(cardcode.substring(16,17)) * 2;
        i = i % 11;
        lastnumber =cardcode.substring(17,18);
        if (i == 0) {
            r = "1";
        }
        if (i == 1) {
            r = "0";
        }
        if (i == 2) {
            r = "x";
        }
        if (i == 3) {
            r = "9";
        }
        if (i == 4) {
            r = "8";
        }
        if (i == 5) {
            r = "7";
        }
        if (i == 6) {
            r = "6";
        }
        if (i == 7) {
            r = "5";
        }
        if (i == 8) {
            r = "4";
        }
        if (i == 9) {
            r = "3";
        }
        if (i == 10) {
            r = "2";
        }
        if (r.equals(lastnumber.toLowerCase())) {
            return true;
        }
        return false;
    }
*/

}
