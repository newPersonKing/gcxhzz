package com.whoami.gcxhzz.entity;

import java.util.List;

public class RecordEntity extends BaseEntity {

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
         * totalCount : 1
         * totalPage : 1
         * data : [{"Code":1,"TaskName":"-","PatrolTime":"2018/07/12","Rivers":"大通河","problem":null,"Content":null,"Trail":null,"userName":"haifeng","CreateTime":"2018/07/11 15:57","State":1,"Files":[]}]
         */

        private int pageIndex;
        private int pageSize;
        private int totalCount;
        private int totalPage;
        private List<RecordEntityData> data;

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

        public List<RecordEntityData> getData() {
            return data;
        }

        public void setData(List<RecordEntityData> data) {
            this.data = data;
        }


    }
}
