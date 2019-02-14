package com.whoami.gcxhzz.entity;

public class FileModuleEntity {


    /**
     * id : 10
     * name : 动态数据.png
     * ext : .png
     * contentLength : 33406
     * contentType :
     * used : 0
     * lastWriteTime : 2018-07-13T14:28:43.9891585+08:00
     * fullName : ~/Upload/4db48f5bb9f07a5bff70cd6221efa22b_动态数据.png
     * md5Value : 4db48f5bb9f07a5bff70cd6221efa22b
     */

    private int id;
    private String name;
    private String ext;
    private int contentLength;
    private String contentType;
    private int used;
    private String lastWriteTime;
    private String fullName;
    private String md5Value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public String getLastWriteTime() {
        return lastWriteTime;
    }

    public void setLastWriteTime(String lastWriteTime) {
        this.lastWriteTime = lastWriteTime;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMd5Value() {
        return md5Value;
    }

    public void setMd5Value(String md5Value) {
        this.md5Value = md5Value;
    }
}
