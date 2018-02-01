package com.hundsun.boss.util;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;


import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 压缩测试
 * @author whl
 *
 */
public class ZipCompressUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZipCompressUtil.class);
    static final int BUFFER = 8192;

    /**
     * 执行压缩操作
     * 
     * @param srcPath 被压缩的文件/文件夹
     * @param zipPathName 被压缩文件存放目录
     */
    public static void fileToZip(String srcPath, String zipPathName) {
        File file = new File(srcPath);
        File zipFile = new File(zipPathName);
        if (!file.exists()) {
            throw new RuntimeException(srcPath + "不存在！");
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
            ZipOutputStream out = new ZipOutputStream(cos);
            String basedir = "";
            compressByType(file, out, basedir);
            out.close();
        } catch (Exception e) {
            logger.error("执行压缩操作时发生异常:" + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断是目录还是文件，根据类型（文件/文件夹）执行不同的压缩方法
     * 
     * @param file
     * @param out
     * @param basedir
     */
    private static void compressByType(File file, ZipOutputStream out, String basedir) {
        /* 判断是目录还是文件 */
        if (file.isDirectory()) {
            logger.info("压缩：" + basedir + file.getName());
            compressDirectory(file, out, basedir);
        } else {
            logger.info("压缩：" + basedir + file.getName());
            compressFile(file, out, basedir);
        }
    }

    /**
     * 压缩一个目录
     * 
     * @param dir
     * @param out
     * @param basedir
     */
    private static void compressDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists()) {
            return;
        }

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            //递归遍历该文件夹
            compressByType(files[i], out, basedir + dir.getName() + "/");
        }
    }

    /**
     * 压缩一个文件
     * 
     * @param file
     * @param out
     * @param basedir
     */
    private static void compressFile(File file, ZipOutputStream out, String basedir) {
        if (!file.exists()) {
            return;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
