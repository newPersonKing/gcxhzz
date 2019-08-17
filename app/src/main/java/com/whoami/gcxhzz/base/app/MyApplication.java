package com.whoami.gcxhzz.base.app;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

import com.algorithm.android.widget.item.ForwardView;
import com.baidu.mapapi.SDKInitializer;
import com.whoami.gcxhzz.activity.MainActivity;
import com.whoami.gcxhzz.entity.CustomLocationMessageEntity;
import com.whoami.gcxhzz.entity.UserInfo;
import com.whoami.gcxhzz.entity.UserInforData;
import com.whoami.gcxhzz.map.LocationService;
import com.whoami.gcxhzz.until.AppManager;
import com.whoami.gcxhzz.until.BaseUtils;
import com.whoami.gcxhzz.until.SPUtils;

import java.util.ArrayList;
import java.util.List;


public class MyApplication extends Application {

    /**
     * 登录信息
     */
    private static UserInforData loginInfo;

    private static boolean isLogin=false;

    public static String C1 = "（1）水资源总量\n" +
            "水资源总量是指地表水和地下水之和，再扣除二者之间的重复量，全县的水资源总量为11.72448亿m3,哈尔盖河区的水资源总量为2.22589亿m3，占全县的19.0%，主要是人畜饮水、农业灌溉用水和工业用水。\n" +
            "地表水情况：很据水文站资料，（哈尔盖河哈尔盖站具有5年实测资料，）对实测资料不足的水文站进行插补延长，计算出哈尔盖的多年平均径流量为128.33mm。\n" +
            "地下水情况：地下水情况：地下水主要由大气降水、地表径流的渗漏补给、山丘区的侧渗补给及田间回归水组成。哈尔盖河区的地下水资源总量为0.25889亿m3，占总量的41.7%。\n" +
            "山丘区地下水情况: 山丘区地下水位埋深一般为5—20m，少数山前坡地埋深为20—40m。哈尔盖镇的马老得、夏哥儿两座山峰形成的脊梁山滩交界处，有10余处泉水出露，流量普遍大于1m3/s。最大的拉克沟泉眼流量达10m3/s以上。\n" +
            "平原区地下水情况：平原区地下水出露不多，普遍以潜水形式储存地下。东部的哈尔盖河下游冲积、洪积平原由南向北埋深5—40m，最深不超过50m。哈尔盖平原区单蟛出水量为200—7500m3/日。\n" +
            "（2）最严格水资源管理制度\n" +
            "落实水资源管理制度，水资源框架基本形成。严守水资源开发利用、用水效率、水功能区限制纳污“三条红线”，确保刚察县生活、生产和生态用水在红线框架内。一是把水资源开发、利用、节约和保护等主要指标纳入到了经济社会发展综合评价体系中，作为生态环保工作的重要组成部分，同步高位推进生态环保、安全生产、脱贫攻坚三项重点工作。二是编制了《刚察县全面推行河长制工作方案》，成立河长制工作领导小组，建立了刚察县河长会议制度、信息制度、工作监督办法、工作考核办法和工作验收办法，进一步提升了对水资源工作的领导。\n" +
            "（3）工业、农业、生活节水情况\n" +
            "①农业节水情况：\n" +
            "2012年实施了哈尔盖镇环仓秀麻村饲草料地节水灌溉项目，其主要对农田进行了防漏衬砌，使得渠系水利用系数由0.49左右提高到0.62，提高了当地的经济效益，达到了节水的目标，为适度规模的牧区饲草料灌溉地提供了示范。\n" +
            "2012年实施了新塘曲中型灌区节水配套改造项目，改善了农业综合开发的灌排情况，改善了灌区的水利设施建设，提高了灌溉水利用系数，达到了灌区节水的目标。\n" +
            "2013年实施了新塘曲灌区续建配套及节水改造工程，新塘曲灌区控制的灌溉面积为30000亩，其为中型灌区，其使得灌区灌溉水利用系数达到了0.6，提高了农作物产量，实现了农业增产、农民增收以及灌区节水的目标。\n" +
            "2013年实施了塘曲灌区节水改造工程，从实际出发，因地制宜，以现有灌溉工程节水改造和渠道防渗衬砌为主，其灌区总控制的灌溉面积为80337.46亩，为中型灌区。工程建成后，使得灌区灌溉水利用系数达到0.61，不断提高农作物的产量，实现农业增产和农民增收；加强灌区用水管理和水费计收体制改革，促进灌区节水目标的实现。\n" +
            "②未实施工业和生活节水措施。\n" +
            "（4）河湖提供水源的高耗水项目情况\n" +
            "热水工业园区存在洗煤企业，洗煤为高耗水项目。\n" +
            "（5）河湖取排水情况\n" +
            "哈尔盖河热水段有6座取水井和泵站，其坐标为37°36'06.75\"N,100°24'32.13\"E，均为傍河取水，是热水工业园区洗煤企业所有。\n" +
            "哈尔盖河有2座拦水坝（2处灌溉取水口）：一座在塘曲，为塘曲灌区取水口，坐标是37°13'43.88\"N，100°29'24.30\"E；另一座在新塘曲，为新塘曲灌区取水口，坐标为37°24'22.79\"N，100°29'01.8\"E。\n" +
            "（6）水功能区划及水域纳污容量\n" +
            "水功能区划是实现水资源综合开发、合理利用、积极保护、科学管理的基础工作，是落实最严格水资源管理制度的重要举措，是水资源保护规划的基础和水资源保护管理的依据，也是落实国务院关于水利部职能配置中规定的水资源保护职责所开展的工作。\n" +
            "依据《水功能区管理办法》，按照《国务院关于实行最严格水资源管理制度的意见》，为推进我省水功能区限制纳污红线制度，满足今后一段时期经济社会发展和水资源管理对水功能区划工作的需求，对我省2003 年批复的《青海省水功能区划》进行修编，编制了《青海省水功能区划（2015-2020 年）》。\n" +
            "根据《青海省水功能区划（2015-2020 年）》哈尔盖河流域一级水功能区划为2个，分别为哈尔盖河刚察保留区和哈尔盖河刚察开发利用区。哈尔盖河刚察保留区其范围是从源头到十五道班，长度为59.3km，哈尔盖河刚察开发利用区河段范围是从十五道班到入湖口，其长度为50.7km。流域的二级水功能区划为哈尔盖刚察农业用水区，其河段范围是从十五道班到入湖口，长度为50.7km。\n" +
            "（8）入河湖排污口情况\n" +
            "热水工业园区距离哈尔盖河1公里左右，园区无污水处理厂，工业污水和生活污水未经处理，直接排放到哈尔盖河。\n" +
            "经实地勘察，热水工业园区有4个排污口。\n";

    public LocationService locationService;
    public Vibrator mVibrator;
    public List<CustomLocationMessageEntity> customLocationMessageEntities =new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        BaseUtils.init(this);

        /***
         * 初始化定位sdk，建议在Application中创建 初始化 locationService
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
    }


    public void saveLoginInfo(UserInforData loginInfo, String loginName, String RealName,String password) {
        //SPUtils.getInstance().set(SPUtils.SIGN_IN, true);
        SPUtils.getInstance().remove(SPUtils.SIGN_IN_LOGIN_INFO);
        SPUtils.getInstance().remove(SPUtils.SIGN_IN_NAME);
        SPUtils.getInstance().remove(SPUtils.SIGN_IN_REAL_NAME);

        SPUtils.getInstance().set(SPUtils.SIGN_IN_LOGIN_INFO, BaseUtils.getGson().toJson(loginInfo));
        SPUtils.getInstance().set(SPUtils.SIGN_IN_NAME, loginName);
        SPUtils.getInstance().set(SPUtils.SIGN_IN_REAL_NAME, RealName);
        SPUtils.getInstance().set(SPUtils.SIGN_IN_PASSWORD,password);

        this.isLogin = true;
        this.loginInfo = loginInfo;
    }

    public boolean isLogin(){
        return isLogin;
    }

    public UserInforData getUserInfo(){
        if (loginInfo!=null) return loginInfo;
        return null;
    }

    /**
     * 退出登陆
     */
    public void exitUserLogin(Context mContext) {
        clearLoginInfo();
        AppManager.getAppManager().finishAllActivity();
        mContext.startActivity(new Intent(mContext, MainActivity.class));
    }

    /**
     * 清除登陆信息
     */
    public void clearLoginInfo() {
        SPUtils.getInstance().remove(SPUtils.SIGN_IN_LOGIN_INFO);
        SPUtils.getInstance().remove(SPUtils.SIGN_IN_NAME);
        SPUtils.getInstance().remove(SPUtils.SIGN_IN_REAL_NAME);

        this.isLogin = false;
        this.loginInfo = null;
    }

}
