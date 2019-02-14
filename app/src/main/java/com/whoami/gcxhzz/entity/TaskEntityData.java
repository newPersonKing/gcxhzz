package com.whoami.gcxhzz.entity;

public class TaskEntityData {
    /**
     * Code : 5
     * Name : 12321321321321
     * Type : 督导检查任务
     * Urgency : 紧急
     * Source : 遥感监测
     * Rivers : 大通河
     * content : <p>12321321321321</p>
     * PatrolUserName : 海峰
     * PatrolDate : 2018/06/15
     * State : 3
     * UserName : 1
     * CreateTime : 2018/06/12 09:23
     * PatroLogCode : null
     * PatroLogState : null
     */

    private int Code;
    private String Name;
    private String Type;/*任务类型*/
    private String Urgency;/*紧急状态*/
    private String Source;/*任务来源*/
    private String Rivers;/*范围*/
    private String content;
    private String PatrolUserName;
    private String PatrolDate;/*巡逻时间*/
    private int State;/*任务状态*/
    private String UserName;
    private String CreateTime;
    private int PatroLogCode;
    private int PatroLogState;
    private int RiversId;


    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public int getRiversId() {
        return RiversId;
    }

    public void setRiversId(int riversId) {
        RiversId = riversId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getUrgency() {
        return Urgency;
    }

    public void setUrgency(String urgency) {
        Urgency = urgency;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getRivers() {
        return Rivers;
    }

    public void setRivers(String rivers) {
        Rivers = rivers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPatrolUserName() {
        return PatrolUserName;
    }

    public void setPatrolUserName(String patrolUserName) {
        PatrolUserName = patrolUserName;
    }

    public String getPatrolDate() {
        return PatrolDate;
    }

    public void setPatrolDate(String patrolDate) {
        PatrolDate = patrolDate;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public int getPatroLogCode() {
        return PatroLogCode;
    }

    public void setPatroLogCode(int patroLogCode) {
        PatroLogCode = patroLogCode;
    }

    public int getPatroLogState() {
        return PatroLogState;
    }

    public void setPatroLogState(int patroLogState) {
        PatroLogState = patroLogState;
    }
}
