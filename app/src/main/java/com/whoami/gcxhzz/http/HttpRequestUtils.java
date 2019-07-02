package com.whoami.gcxhzz.http;

import com.tamic.novate.Novate;
import com.whoami.gcxhzz.BuildConfig;
import com.whoami.gcxhzz.until.BaseUtils;
import com.whoami.gcxhzz.until.ObjectUtils;

import java.util.HashMap;
import java.util.Map;


public class HttpRequestUtils {


    public static final String HOST_URL = "http://222.128.113.192:18181";

    private static final int COTTECT_TIME_OUT = 30;
    private static final int WRITE_TIME_OUT = 60;
    private static final int READ_TIME_OUT = 60;

    private static Novate novate;
    private static Map<String, Object> parameters = new HashMap<String, Object>();
    private static Map<String, String> headers = new HashMap<>();

    private static HttpRequestUtils instance;

    /**
     * 私有构造
     */
    private HttpRequestUtils() {
    }

    /**
     * 初始化
     * @return
     */
    public static synchronized HttpRequestUtils getInstance() {
        if (ObjectUtils.isNull(instance)) {
            instance = new HttpRequestUtils();
            novate = new Novate.Builder(BaseUtils.getContext())
                    .connectTimeout(COTTECT_TIME_OUT)
                    .writeTimeout(WRITE_TIME_OUT)
                    .readTimeout(READ_TIME_OUT)
                    .baseUrl(HOST_URL)
                    .addParameters(parameters)
                    .addHeader(headers)
                    .addCookie(true)
                    .addCache(false)
//                    .addLog(false)
                    .addLog(BuildConfig.DEBUG)
                    .build();
        }
        return instance;
    }



    public Novate getNovate() {
        return novate;
    }

    public HttpRequestUtils setParameters(Map<String, Object> parameters){
        this.parameters.clear();
        this.parameters.putAll(parameters);
        return this;
    }

    public HttpRequestUtils setHeaders(Map<String, String> headers){
        this.headers.clear();
        this.headers.putAll(headers);
        return this;
    }
}
