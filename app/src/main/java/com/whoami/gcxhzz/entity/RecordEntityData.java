package com.whoami.gcxhzz.entity;

import java.util.List;

public class RecordEntityData {

    /**
     * Code : 1
     * TaskName : -
     * PatrolTime : 2018/07/12
     * Rivers : 大通河
     * problem : null
     * Content : null
     * Trail : null
     * userName : haifeng
     * CreateTime : 2018/07/11 15:57
     * State : 1
     * Files : []
     */

    private int Code;
    private String TaskName;
    private String PatrolTime;
    private String Rivers;
    private String problem;
    private String Content;
    private String Trail;
    private String userName;
    private String CreateTime;
    private int State;

    private String AudioUrl;/*录音*/

    private String VideoUrl;/*录制视频*/

    private List<FileContent> Files;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) {
        TaskName = taskName;
    }

    public String getPatrolTime() {
        return PatrolTime;
    }

    public void setPatrolTime(String patrolTime) {
        PatrolTime = patrolTime;
    }

    public String getRivers() {
        return Rivers;
    }

    public void setRivers(String rivers) {
        Rivers = rivers;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getTrail() {
        return Trail;
    }

    public void setTrail(String trail) {
        Trail = trail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public List<FileContent> getFiles() {
        return Files;
    }

    public void setFiles(List<FileContent> files) {
        Files = files;
    }

    public String getAudioUrl() {
        return AudioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        AudioUrl = audioUrl;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public static class FileContent{

        /**
         * Id : 12
         * Name : QQ截图20180713150430.png
         * Ext : .png
         * ContentLength : 21844
         * ContentType : image/png
         * FullName : http://119.57.114.28:18181/Upload/QQæªå¾20180713150430.png
         */

        private int Id;
        private String Name;
        private String Ext;
        private int ContentLength;
        private String ContentType;
        private String FullName;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getExt() {
            return Ext;
        }

        public void setExt(String Ext) {
            this.Ext = Ext;
        }

        public int getContentLength() {
            return ContentLength;
        }

        public void setContentLength(int ContentLength) {
            this.ContentLength = ContentLength;
        }

        public String getContentType() {
            return ContentType;
        }

        public void setContentType(String ContentType) {
            this.ContentType = ContentType;
        }

        public String getFullName() {
            return FullName;
        }

        public void setFullName(String FullName) {
            this.FullName = FullName;
        }
    }
}
