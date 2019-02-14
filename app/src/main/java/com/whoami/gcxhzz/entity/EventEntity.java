package com.whoami.gcxhzz.entity;

import java.util.List;

public class EventEntity extends BaseEntity{


    /**
     * content : {"pageIndex":1,"pageSize":5,"totalCount":10,"totalPage":2,"data":[{"eventId":4,"eventTitle":"asfa","source":10,"riverCode":"102","riverName":"布哈河","urgency":1,"content":null,"reportTime":"2018-06-13T15:01:28.207","state":0},{"eventId":5,"eventTitle":"1112321321312","source":10,"riverCode":"101","riverName":"大通河","urgency":1,"content":null,"reportTime":"2018-06-20T10:23:45.053","state":0},{"eventId":6,"eventTitle":"12321321","source":10,"riverCode":"101","riverName":"大通河","urgency":1,"content":null,"reportTime":"2018-06-29T08:40:59.36","state":0},{"eventId":7,"eventTitle":"12321321","source":10,"riverCode":"101","riverName":"大通河","urgency":1,"content":null,"reportTime":"2018-06-29T08:40:59.36","state":0},{"eventId":8,"eventTitle":"asdf","source":30,"riverCode":"101","riverName":"大通河","urgency":1,"content":null,"reportTime":"2018-07-02T10:53:36.217","state":0}]}
     * succeeded : true
     * errors : null
     */

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
         * pageSize : 5
         * totalCount : 10
         * totalPage : 2
         * data : [{"eventId":4,"eventTitle":"asfa","source":10,"riverCode":"102","riverName":"布哈河","urgency":1,"content":null,"reportTime":"2018-06-13T15:01:28.207","state":0},{"eventId":5,"eventTitle":"1112321321312","source":10,"riverCode":"101","riverName":"大通河","urgency":1,"content":null,"reportTime":"2018-06-20T10:23:45.053","state":0},{"eventId":6,"eventTitle":"12321321","source":10,"riverCode":"101","riverName":"大通河","urgency":1,"content":null,"reportTime":"2018-06-29T08:40:59.36","state":0},{"eventId":7,"eventTitle":"12321321","source":10,"riverCode":"101","riverName":"大通河","urgency":1,"content":null,"reportTime":"2018-06-29T08:40:59.36","state":0},{"eventId":8,"eventTitle":"asdf","source":30,"riverCode":"101","riverName":"大通河","urgency":1,"content":null,"reportTime":"2018-07-02T10:53:36.217","state":0}]
         */

        private int pageIndex;
        private int pageSize;
        private int totalCount;
        private int totalPage;
        private List<EventEntityData> data;

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

        public List<EventEntityData> getData() {
            return data;
        }

        public void setData(List<EventEntityData> data) {
            this.data = data;
        }
    }
}
