package com.muzi.filemovement;

import java.io.File;

/**
 * 作者: lipeng
 * 时间: 2019/7/9
 * 邮箱: lipeng@moyi365.com
 * 功能:
 */
public interface Callback {

    int getInterval();

    void onStart();

    void onScan(File file);

    void onProgress(int progress);

    void onFinish();

}
