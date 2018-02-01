/**
 * hundsun.com
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.hundsun.boss.security.encryptsetting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件处理
 * 
 * @author shilei
 * @version $Id: FileUtil.java, v 0.1 2016年8月3日 上午9:33:18 shilei Exp $
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 私有构造方法，防止实例化
     */
    private FileUtil() {
    }

    /**
     * 创建文件，并写入文件内容
     * 
     * @param fullPath
     * @param fileName
     * @param content
     */
    public static void createFile(String fullPath, String fileName, String content) throws Exception {

        File dir = new File(fullPath);
        if (!dir.exists()) {
            dir.mkdirs();
            dir.setWritable(true);
        }

        File file = new File(dir, fileName);
        if (file.exists()) {
            return;
        }

        FileWriter fw = null;
        BufferedWriter bw = null;
        //创建新文件
        file.createNewFile();
        file.setWritable(true);
        //设置文件属性为隐藏
        if (Constants.OS_NAME.contains("Windows")) {
            String cmd = "attrib " + file.getAbsolutePath() + " +H";
            Runtime.getRuntime().exec(cmd);
        }
        fw = new FileWriter(file);
        bw = new BufferedWriter(fw);
        bw.write(content);
        bw.flush();
        bw.close();
        fw.close();
    }

    /**
     * 删除文件
     * 
     * @param file
     */
    public static void deleteFile(File file) {
        if (null == file || !file.exists()) {
            return;
        }
        file.delete();
    }

    /**
     * 从文件中读取一个字符串
     * 
     * @param file
     * @return
     */
    public static String getString(File file) {

        if (null == file || !file.exists()) {
            return Constants.STR_BLANK;
        }
        StringBuffer sb = new StringBuffer();
        FileReader fr = null;
        BufferedReader br = null;

        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String tempStr = null;
            while ((tempStr = br.readLine()) != null) {
                sb.append(tempStr);
            }
        } catch (FileNotFoundException e) {
            logger.error("File {} not found", file.getPath());
        } catch (IOException e) {
            logger.error("Read file {} exception", file.getPath());
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
            if (null != fr) {
                try {
                    fr.close();
                } catch (IOException e) {
                }
            }
        }
        String str = sb.toString();
        return str;
    }
}
