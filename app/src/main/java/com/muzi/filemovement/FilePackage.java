package com.muzi.filemovement;

/**
 * 作者: lipeng
 * 时间: 2019/7/9
 * 邮箱: lipeng@moyi365.com
 * 功能:
 */
public class FilePackage {

    private String oldPath;

    private String newPath;

    private long length;

    public FilePackage() {
    }

    public String getOldPath() {
        return oldPath;
    }

    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

}
