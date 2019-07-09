package com.muzi.filemovement;

/**
 * 作者: lipeng
 * 时间: 2019/7/9
 * 邮箱: lipeng@moyi365.com
 * 功能:
 */
public interface Callback {

    void onStart();

    void onScan(float totalSize, int fileSize);

    void onProgress(int progress);

    void onFinish();

}
