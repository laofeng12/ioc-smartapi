package com.openjava.datalake.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    public static String createZIPFile(String temp_folder_zip, String zipFolderPath) {
        //#region 2、将多份文件做压缩处理
        //编写压缩后文件流
        String zipPath = temp_folder_zip + "导出数据.zip";
        FileOutputStream zipOut = null;
        ZipOutputStream zipOutStream = null;
        try {
            zipOut = new FileOutputStream(zipPath);
            //5、利用压缩流 对 进行压缩
            zipOutStream = new ZipOutputStream(new BufferedOutputStream(zipOut)); //创建个压缩流 对应文件输出流 文件名及路径

            // 设置压缩的编码，解决压缩路径中的中文乱码问题
            //zipOutStream.setEncoding("UTF-8");
            // 获得需要压缩的文件夹
            File temp_folder_word = new File(zipFolderPath);

            File[] files = temp_folder_word.listFiles();
            int length = files.length;
            System.out.println("temp_folder_word.length" + length);
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String name = file.getName();
                System.out.println(i+"FileName:"+name);
            }

            String[] fileList = temp_folder_word.list();


            byte[] byt = new byte[1024];
            int count = 0;
            for (String fileName : fileList) { //迭代要压缩文件夹 子内容

                FileInputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(temp_folder_word + File.separator + fileName);
                    //设置压缩文件打开后 显示每一项的名字
                    zipOutStream.putNextEntry(new ZipEntry(fileName)); //将压缩流和自项关联上
                    while ((count = inputStream.read(byt, 0, 1024)) != -1) {
                        zipOutStream.write(byt, 0, count);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zipOutStream != null) {
                    zipOutStream.close();
                }
                if (zipOut != null) {
                    zipOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //#endregion

        return zipPath;
    }
}
