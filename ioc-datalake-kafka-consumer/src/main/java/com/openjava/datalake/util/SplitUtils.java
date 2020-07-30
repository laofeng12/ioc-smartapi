package com.openjava.datalake.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author xjd
 * @Date 2019/12/11 10:25
 * @Version 1.0
 */
public class SplitUtils {

    public static String trimHeadZero(String s) {
        return s.replaceAll("^(0+)", "");
    }

    public static String[] splitNumberLetter(String themeCode) {
        String[] numbers = themeCode.split("\\D");
        String numberString = "";
        for (String number : numbers) {
            numberString = numberString + number;
        }
        String[] letters = themeCode.split("\\d");
        String lettersString = "";
        for (String letter : letters) {
            lettersString = lettersString + letter;
        }
        String[] split;
        if (StringUtils.isBlank(lettersString)){
            split = new String[]{numberString};
        } else {
            split = new String[]{numberString, lettersString};
        }
        return split;

    }
//    public String[] getStr(){
//        String[] str_string = s.split("\\d");//  \d 为正则表达式表示[0-9]数字
//        String latter = "";
//        for (String s : str_string) {
//
//        }
//        return str_string;
//    }
//
//    public int[] getNum(){
//        String[] num_string = s.split("\\D");  // \D 为正则表达式表示非数字
//        String a = "";
//
//        for(String m : num_string){
//            a += m;
//        }
//
//        String[] num = a.split("");  //将分离出的重新保存在新数组num中不要直接用num_string，  因为在正则表达式对字符串进行选择时若前面的几个字符不符合要求但num_string数组中仍会存有其位置 是空格
//        int[] inte = new int[num.length];
//
//        for(int i =0; i < num.length; i++){
//            inte[i] = Integer.parseInt(num[i]); //将该数组中的数字存入int数组
//        }
//
//        return inte;
//    }

    public static void main(String[] args) {
        String[] strings = splitNumberLetter("01b");
        for (String string : strings) {
            System.out.println(string);
        }
    }
}
