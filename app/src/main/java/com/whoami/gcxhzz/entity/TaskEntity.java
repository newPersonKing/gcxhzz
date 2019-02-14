package com.whoami.gcxhzz.entity;

import java.util.List;

public class TaskEntity extends BaseEntity{

    private ContentBean content;

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * pageIndex : 1
         * pageSize : 15
         * totalCount : 4
         * totalPage : 1
         * data : [{"Code":5,"Name":"12321321321321","Type":"督导检查任务","Urgency":"紧急","Source":"遥感监测","Rivers":"大通河","content":"<p>12321321321321<\/p>","PatrolUserName":"海峰","PatrolDate":"2018/06/15","State":3,"UserName":"1","CreateTime":"2018/06/12 09:23","PatroLogCode":null,"PatroLogState":null},{"Code":8,"Name":"132132132","Type":"督导检查任务","Urgency":"紧急","Source":"社会监督","Rivers":"大通河","content":"<p>12321321321321<\/p>","PatrolUserName":"海峰","PatrolDate":"2018/06/28","State":2,"UserName":"1","CreateTime":"2018/06/28 16:47","PatroLogCode":null,"PatroLogState":null},{"Code":10,"Name":"填写日志","Type":"常规任务","Urgency":"一般","Source":"督导检查","Rivers":"大通河","content":"<p>12321321321<\/p>","PatrolUserName":"海峰","PatrolDate":"2018/07/02","State":3,"UserName":"1","CreateTime":"2018/07/02 13:45","PatroLogCode":null,"PatroLogState":null},{"Code":3,"Name":"12321321321321321321","Type":"督导检查任务","Urgency":"紧急","Source":"遥感监测","Rivers":"大通河","content":"<p>12321321<\/p>","PatrolUserName":"海峰","PatrolDate":"2018/06/14","State":2,"UserName":"1","CreateTime":"2018/07/02 13:51","PatroLogCode":null,"PatroLogState":null}]
         */

        private int pageIndex;
        private int pageSize;
        private int totalCount;
        private int totalPage;
        private List<TaskEntityData> data;

        public int getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(int pageIndex) {
            this.pageIndex = pageIndex;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public List<TaskEntityData> getData() {
            return data;
        }

        public void setData(List<TaskEntityData> data) {
            this.data = data;
        }

    }
}
