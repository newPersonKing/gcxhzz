package com.whoami.gcxhzz.entity;


/**
 * 上传阿里云oss---文件实体类
 */
public class FileEntity {
    private long id;
    private String fileName;//文件名称
    private String originalName;//文件原始名称
    private String size;//文件大小
    private String fileType;//文件的后缀名
    private String url;//文件的阿里云url
    private String tableName;//关联资源的所在表的表名
    private long relateId;//关联资源ID
    private String partName;//关联资源中文件列表分块字段
    private long createTime;//上传时间

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public long getRelateId() {
        return relateId;
    }

    public void setRelateId(long relateId) {
        this.relateId = relateId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "FileEntity{" +
                "id=" + id +
                ", originalName='" + originalName + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
