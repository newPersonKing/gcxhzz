package com.whoami.gcxhzz.http;

public interface HttpService {
    /*登陆*/
    String API_ACCOUNT_LOGIN="/Api/Account/Login";
    /*退出*/
    String API_ACCOUNT_LOGOUT="Api/Account/Logout";

    /*任务列表*/
    String  API_TASK_LIST="/Api/Task/List";
    /*任务详情*/
    String API_TASK_DETAIL="Api/Task/Detail";

    /*巡河日志列表 */
    String API_PATROLLOG_LIST="Api/PatrolLog/List";
    /*巡河日志详情*/
    String API_PATROLLOG_DETAIL="Api/PatrolLog/Detail";
    /*添加体制*/
    String API_PATROLLOG_ADD="Api/PatrolLog/Add";

    /*我的事件上报*/
    String API_EVENT_MANAGE_MY="Api/EventManage/My";
    /*事件上报详情*/
    String API_EVENTMANAGE_DETAIL="Api/EventManage/Detail";
    /*添加事件*/
    String API_EVENTMANAGE_ADD="Api/EventManage/Add";

    /*获取河段名称*/
    String API_RIVERBASEINFO_GET="Api/RiverBaseInfo/Get";

    /*图片上传 语音视频上传*/
    String API_FILEINPUT_UPLOAD="Api/FileInput/Upload";

    /*获取河流动态信息*/
    String API_INFORMATION_GET="Api/Information/Get";


}
