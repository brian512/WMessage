package com.brian.common.utils;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * 通过文件头识别文件类型
 * Created by brian on 17-8-20.
 */

public class FileTypeUtil {

    private static final HashMap<String, String> mFileTypes = new HashMap<>();

    static {
        //images
        mFileTypes.put("FFD8FF", "jpg");
        mFileTypes.put("89504E", "png");
        mFileTypes.put("474946", "gif");
        mFileTypes.put("424D3E", "bmp");

        // video
        mFileTypes.put("2E524D", "rm");
        mFileTypes.put("000001", "mpg");
        mFileTypes.put("6D6F6F", "mov");
        mFileTypes.put("415649", "avi");

        mFileTypes.put("FFFB50", "mp3");
        mFileTypes.put("574156", "wav");
        mFileTypes.put("49492A", "tif");
        mFileTypes.put("414331", "dwg"); //CAD
        mFileTypes.put("384250", "psd");
        mFileTypes.put("7B5C72", "rtf"); //日记本
        mFileTypes.put("3C3F78", "xml");
        mFileTypes.put("68746D", "html");
        mFileTypes.put("44656C", "eml"); //邮件
        mFileTypes.put("D0CF11", "doc");
        mFileTypes.put("252150", "ps");
        mFileTypes.put("255044", "pdf");
        mFileTypes.put("504B03", "zip");
        mFileTypes.put("526172", "rar");
        mFileTypes.put("1F8B08", "gz");
        mFileTypes.put("4D5A90", "exe");
    }

    public static String getFileType(@NonNull File file) {
        return mFileTypes.get(getFileHeader(file.getPath()));
    }

    public static String getFileType(@NonNull String filePath) {
        return mFileTypes.get(getFileHeader(filePath));
    }

    //获取文件头信息
    public static String getFileHeader(@NonNull String filePath) {
        FileInputStream is = null;
        String value = null;
        try {
            is = new FileInputStream(filePath);
            byte[] b = new byte[3]; // 只读取可前三位
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(value);
        return value;
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }

    public static void main(String[] args) throws Exception {
        final String fileType = getFileType("/home/brian/Pictures/test.pdf");
        System.out.println(fileType);
    }

}
