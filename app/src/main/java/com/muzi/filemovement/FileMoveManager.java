package com.muzi.filemovement;

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
public class FileMoveManager {

    private static final FileMoveManager ourInstance = new FileMoveManager();

    public static FileMoveManager getInstance() {
        return ourInstance;
    }

    private long totalLength;

    private long moveLength;

    private List<FilePackage> filePackageList = new ArrayList<>();

    private FileMoveManager() {
    }


    public void move(final String oldDesc, final String newDesc) {
        if (oldDesc == null) {
            return;
        }
        final File file = new File(oldDesc);
        if (!file.exists()) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                scanPath(file, oldDesc, newDesc);
                realMove();
            }
        }).start();
    }

    private void scanPath(File file, String oldDesc, String newDesc) {
        if (file.isFile()) {
            addList(file, oldDesc, newDesc);
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                scanPath(f, oldDesc, newDesc);
            }
        }
    }

    private void addList(File file, String oldDesc, String newDesc) {
        String oldPath = file.getAbsolutePath();
        String newPath = newDesc + oldPath.substring(oldDesc.length());
        long length = file.length();
        FilePackage filePackage = new FilePackage();
        filePackage.setNewPath(newPath);
        filePackage.setOldPath(oldPath);
        filePackage.setLength(length);

        totalLength = totalLength + length;
        filePackageList.add(filePackage);
    }

    private void realMove() {
        for (FilePackage aPackage : filePackageList) {
            String oldPath = aPackage.getOldPath();
            String newPath = aPackage.getNewPath();
            createParentDir(newPath);
            try {
                copy(oldPath, newPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createParentDir(String filePath) {
        File file = new File(filePath);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
    }

    public void copy(String oldPath, String newPath) throws IOException {
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

    private void remove(String path) {
        if (path == null) {
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.deleteOnExit();
        } else {
            for (File f : file.listFiles()) {
                remove(f);
            }
        }
    }

    private void remove(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.deleteOnExit();
        } else {
            for (File f : file.listFiles()) {
                remove(f);
            }
        }
    }

}
