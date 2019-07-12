package com.muzi.filemovement;

import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

/**
 * 作者: lipeng
 * 时间: 2019/7/9
 * 邮箱: lipeng@moyi365.com
 * 功能:
 */
public class FileMoveManagerOkio {

    private static final FileMoveManagerOkio ourInstance = new FileMoveManagerOkio();

    public static FileMoveManagerOkio getInstance() {
        return ourInstance;
    }

    private double totalLength;

    private double moveLength;

    private Callback callback;

    private List<FilePackage> filePackageList = new ArrayList<>();

    private FileMoveManagerOkio() {
    }


    public void move(final String oldDesc, final String newDesc, final Callback callback) {
        this.callback = callback;
        if (oldDesc == null) {
            return;
        }
        final File file = new File(oldDesc);
        if (!file.exists()) {
            return;
        }
        if (callback != null) {
            callback.onStart();
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                scanPath(file, oldDesc, newDesc);
                realMove();
                if (callback != null) {
                    callback.onProgress(100);
                    callback.onFinish();
                }
            }
        });
        thread.start();
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
        if (callback != null) {
            callback.onScan(file);
        }
        String oldPath = file.getAbsolutePath();
        String newPath = newDesc + oldPath.substring(oldDesc.length());
        File newFile = new File(newPath);
        if (newFile.exists()) {
            return;
        }
        long length = file.length();
        FilePackage filePackage = new FilePackage();
        filePackage.setNewPath(newPath);
        filePackage.setOldPath(oldPath);
        filePackage.setLength(length);

        totalLength = totalLength + length;
        filePackageList.add(filePackage);
    }

    private void realMove() {
        handler.sendEmptyMessage(0);
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
        Source oldSource = Okio.source(new File(oldPath));
        Sink newSink = Okio.sink(new File(newPath));
        BufferedSink bufferedSink = Okio.buffer(newSink);
        BufferedSource bufferedSource = Okio.buffer(oldSource);
        byte[] b = new byte[2048];
        int len;
        while ((len = bufferedSource.read(b)) != -1) {
            moveLength = moveLength + len;
            bufferedSink.write(b, 0, len);
        }
        bufferedSink.flush();
        bufferedSink.close();
        bufferedSource.close();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (callback != null) {
                int progress = msg.arg1;
                callback.onProgress(progress);
                if (progress < 100) {
                    callProgress();
                }
            }
        }
    };

    private void callProgress() {
        Message message = new Message();
        int progress = (int) (moveLength / totalLength * 100);
        message.arg1 = progress;
        int interval = callback != null ? callback.getInterval() : 200;
        handler.sendMessageDelayed(message, interval);
    }

    private void delete(String path) {
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
                delete(f);
            }
        }
    }

    private void delete(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.deleteOnExit();
        } else {
            for (File f : file.listFiles()) {
                delete(f);
            }
        }
    }

}
