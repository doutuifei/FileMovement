package com.muzi.filemovement;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: lipeng
 * 时间: 2019/7/9
 * 邮箱: lipeng@moyi365.com
 * 功能:
 */
public class FileMoveUtils {

    private static final FileMoveUtils ourInstance = new FileMoveUtils();

    public static FileMoveUtils getInstance() {
        return ourInstance;
    }

    private FileMoveUtils() {
    }

    private long totalLength;

    private long moveLength;

    private List<FilePackage> filePackageList = new ArrayList<>();

    public void move(String oldPath, String newPath) {
        if (oldPath == null) {
            return;
        }
        File file = new File(oldPath);
        if (!file.exists()) {
            return;
        }
        readyMove(file, newPath);
    }

    private void readyMove(final File file, final String newPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                scanPath(file, newPath);
                moveList();
            }
        }).start();
    }

    /**
     * 扫描所有文件并计算大小
     *
     * @param file
     */
    private void scanPath(File file, String newPath) {
        if (file.isFile()) {
            long length = file.length();
            totalLength += length;
            addList(file, newPath);
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                scanPath(f, newPath);
            }
        }
    }

    private void addList(File file, String newPath) {
        FilePackage filePackage = new FilePackage();
        long length = file.length();
        filePackage.setLength(length);
        String absolutePath = file.getAbsolutePath();
        filePackage.setOldPath(absolutePath);
        String newAbsolutePath = newPath + File.separator + file.getName();
        filePackage.setNewPath(newAbsolutePath);
        filePackageList.add(filePackage);

        Log.e("FileMoveUtils", "length:" + length);
        Log.e("FileMoveUtils", "absolutePath:" + absolutePath);
        Log.e("FileMoveUtils", "newAbsolutePath:" + newAbsolutePath);
    }

    private void moveList() {
        if (filePackageList.isEmpty()) {
            return;
        }
        Log.e("FileMoveUtils", "totalLength:" + totalLength);
        for (FilePackage filePackage : filePackageList) {
            try {
                Log.e("FileMoveUtils", "moveLength:" + moveLength);
                realMove(filePackage.getOldPath(), filePackage.getNewPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开始移动
     *
     * @param oldPath 完整路径名
     * @param newPath 完整路径名
     */
    public void realMove(String oldPath, String newPath) throws IOException {
        FileInputStream in = new FileInputStream(oldPath);
        FileOutputStream out = new FileOutputStream(newPath);
        BufferedInputStream bin = new BufferedInputStream(in);
        BufferedOutputStream bout = new BufferedOutputStream(out);
        byte[] b = new byte[2048];
        int len;
        while ((len = bin.read(b)) != -1) {
            moveLength = moveLength + len;
            bout.write(b, 0, len);
        }
        bout.flush();
        bout.close();
        bin.close();
    }

}
