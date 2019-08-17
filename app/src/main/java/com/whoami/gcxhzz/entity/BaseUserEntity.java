package com.whoami.gcxhzz.entity;

import java.util.List;

public class BaseUserEntity extends BaseEntity{

    private List<TaskUser> content;


    public List<TaskUser> getContent() {
        return content;
    }

    public void setContent(List<TaskUser> content) {
        this.content = content;
    }

    public  class TaskUser{

        private String realName;

        private Long userId;

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }
}
