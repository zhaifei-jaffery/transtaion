package com.test.test1;

import com.alibaba.fastjson.JSON;
import com.test.test1.lbtransaction.transactional.LbTransactionManager;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;


/**
 * ClassName: HttpUtils
 * Description:
 * date: 2019/7/17 18:17
 *
 * @author YangXu
 * @since JDK 1.8
 */

public class HttpUtils {

    private static PoolingHttpClientConnectionManager cm;


    static {
        if(cm==null){
            synchronized (HttpUtils.class){
                cm = new PoolingHttpClientConnectionManager();
                cm.setMaxTotal(500);
                cm.setDefaultMaxPerRoute(50);
            }
        }
    }

    private static CloseableHttpClient getHttpClient(){
      return HttpClients.custom().setConnectionManager(cm).build();
    }

    /**
     * get请求
     * @param url 请求地址
     * @return
     */
    public static JSONObject get(String url){
        JSONObject result = null;
        CloseableHttpClient httpClient = getHttpClient();
         HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-type","application/json");
        httpGet.setHeader("groupId", LbTransactionManager.getCurrentGroupId());
        httpGet.setHeader("transactionCount",String.valueOf(LbTransactionManager.getTransactionCount()));
//        httpGet.setHeader("Connection","keep-alive");
//        httpGet.setHeader("sdk-version","1");
//        httpGet.setHeader("Host","aweme-eagle-hl.snssdk.com");
        //httpGet.setHeader("X-Khronos","1563723608");
        //httpGet.setHeader("X-Gorgon","8300000000007389d53fb28e0749751d166b85d18b4988fe1036");
        //httpGet.setHeader("x-Tt-Token","00d834238eede6e96772e3d0557c07d38fe6de87889d749484df4c779a539e81856f69979e7ad47a901a59659c1a53c97b58");
        //httpGet.setHeader("Cookie","odin_tt=07757c83ea03c3783176bec1a5027bbaccacdea2b422b5103976e789e9c034e5e833493f683e63d43c796f165f032697; sid_guard=d834238eede6e96772e3d0557c07d38f%7C1562847976%7C5184000%7CMon%2C+09-Sep-2019+12%3A26%3A16+GMT; uid_tt=50a73994a153a17a6f6d2caa5fab0359; sid_tt=d834238eede6e96772e3d0557c07d38f; sessionid=d834238eede6e96772e3d0557c07d38f; install_id=77695119599; ttreq=1$e2beaeec3db1aed46060c92083250f8631ef181e");
        return execute(httpGet);
    }


    /**
     * post请求
     * @param url
     * @return
     */
    public static  JSONObject post(String url){
        HttpPost httpPost = new HttpPost(url);
//        httpPost.setHeader("user-agent", DoYinApi.userAgent);
        return execute(httpPost);
    }

    /**
     * post带参请求
     * @param url
     * @param params
     * @return
     */
    public static  JSONObject post(String url, List<BasicNameValuePair> params) {
        try {
            HttpPost httpPost = new HttpPost(url);
//            httpPost.setHeader("user-agent", DoYinApi.userAgent);
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            return execute(httpPost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * post请求，文件上传
     * @param url 请求路径
     * @param file 文件
     * @param params 参数
     * @return
     */
    /*public static  JSONObject post(String url, String fileName ,File file, List<BasicNameValuePair> params) {
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("user-agent", DoYinApi.userAgent);

            FileBody fileBody = new FileBody(file);
            HttpEntity httpEntity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addPart(fileName,fileBody)
                    .build();
            httpPost.setEntity(httpEntity);

            if(params!=null){
                httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            }
            return execute(httpPost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * Http请求
     * @param request 请求类型 （GET、POST......）
     * @return
     */
    public static JSONObject execute(HttpUriRequest request){
        JSONObject result = null;
        CloseableHttpClient httpClient = getHttpClient();
        try(CloseableHttpResponse response = httpClient.execute(request)){
            HttpEntity entity = response.getEntity();
            result = JSON.parseObject(EntityUtils.toString(entity));
            EntityUtils.consume(entity);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }



}
