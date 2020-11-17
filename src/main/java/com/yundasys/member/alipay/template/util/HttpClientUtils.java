package com.yundasys.member.alipay.template.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 *   Title: HttpClient.java
 *   Description: HTTP Post
 *   Copyright: Maple Copyright (c) 2012
 *   Company: yundaex
 * </pre>
 *
 * @author jiangwubo
 * @version 1.0
 * @date 2012-12-16
 */
@Slf4j
public class HttpClientUtils {

    public static final String CONTENT_TYPE_JSON = "application/json;charset=utf-8";

    public static final String CONTENT_TYPE_XML = "application/xml;charset=utf-8";

    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded;charset=utf-8";

    public static final String ENCODING_UTF_8 = "UTF-8";

    // 读数据超时时间：从服务器获取响应数据的超时时间
    public static final int SOCKET_TIMEOUT = 5000;

    // 建立连接超时时间
    public static final int CONNECT_TIMEOUT = 4000;

    // 从连接池中获取连接的超时时间
    public static final int CONNECT_REQUEST_TIMEOUT = 2000;

    // 连接池中最大连接数
    public static final int CONNEC_POOL_MAX_COUNT = 50;

    // 分配给同一个route(路由)最大的并发连接数
    public static final int PER_ROUTE_MAX_COUNT = 25;

    // 设置请求和传输超时时间
    private static RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).setSocketTimeout(SOCKET_TIMEOUT)
            .setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT).build();

    private static PoolingHttpClientConnectionManager connecManager = new PoolingHttpClientConnectionManager();

    private static CloseableHttpClient client;

    static {
        SSLContext sslcontext = null;
        try {
            // 采用绕过验证的方式处理https请求
            sslcontext = createIgnoreVerifySSL();
        } catch (Exception e) {
            log.error("初始化绕过SSL异常:", e);
        }

        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", new SSLConnectionSocketFactory(sslcontext)).build();
        connecManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // 连接池中最大连接数
        connecManager.setMaxTotal(CONNEC_POOL_MAX_COUNT);
        /**
         * 分配给同一个route(路由)最大的并发连接数。 route：运行环境机器 到 目标机器的一条线路。 举例来说，我们使用HttpClient的实现来分别请求 www.baidu.com 的资源和 www.bing.com 的资源那么他就会产生两个route。
         */
        connecManager.setDefaultMaxPerRoute(PER_ROUTE_MAX_COUNT);
        // 创建自定义的httpclient对象
        client = HttpClients.custom().setConnectionManager(connecManager).setDefaultRequestConfig(config).build();
    }

    /**
     * Post方式请求
     * 
     * @param url 请求地址
     * @param data 参数
     * @param contentType 数据类型 1 CONTENT_TYPE_FORM 2 CONTENT_TYPE_XML 3 CONTENT_TYPE_JSON
     * @return xml数据格式
     * @throws Exception
     */
    public static String post(String url, String data, String contentType) throws Exception {
        log.debug("post---url: " + url);
        log.debug("post---data: " + data);
        StringBuffer buffer = new StringBuffer();
        URL getUrl = new URL(url);
        HttpURLConnection connection = null;
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        try {
            connection = (HttpURLConnection) getUrl.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Referer", "Keep-Alive");
            connection.setUseCaches(false);
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(SOCKET_TIMEOUT);
            connection.connect();
            out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.write(data);
            out.flush();
            // int rcode = connection.getResponseCode();
            // if (rcode != 200) {
            //
            // }
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e2) {
                    out = null;
                    log.error("关闭流异常：", e2);
                } 
            }
            if (connection != null) {
                try {
                    connection.disconnect();
                 } catch (Exception e2) {
                     connection = null;
                     log.error("关闭流异常：", e2);
                 } 
             }
             if (reader != null) {
                 try {
                     reader.close();
                 } catch (Exception e2) {
                     reader = null;
                     log.error("关闭流异常：", e2);
                 } 
             }
        }
        log.debug("http post --- result:　" + buffer.toString());
        return buffer.toString();

    }

    public static String sendPost(String url, String param, String contentType) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuffer sb = new StringBuffer();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(SOCKET_TIMEOUT);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e2) {
                    out = null;
                    log.error("关闭流异常：", e2);
                } 
            }
            if (in != null) {
                 try {
                     in.close();
                 } catch (Exception e2) {
                     in = null;
                     log.error("关闭流异常：", e2);
                 } 
            }
        }
        return sb.toString();
    }

    public static String sendPostNew(String url, String parameter) {
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", CONTENT_TYPE_JSON);

            StringEntity se = new StringEntity(parameter, Charset.forName("utf-8"));
            se.setContentEncoding("UTF-8");
            se.setContentType(CONTENT_TYPE_JSON);
            httpPost.setEntity(se);

            response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            log.debug("请求返回数据  data ===================  " + result);
            return result;
        } catch (Exception e) {
            log.error("http请求出问题 ===================" + e);
            return "fail";
        }finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e2) {
                    response = null;
                    log.error("关闭流异常：", e2);
                } 
            }
        }

    }
    // /**
    // * 有请求超时限制的post请求
    // *
    // */
    // public static String sendPostNew(String url, String param) {
    // PrintWriter out = null;
    // BufferedReader in = null;
    // StringBuffer sb = new StringBuffer();
    // try {
    // URL realUrl = new URL(url);
    // // 打开和URL之间的连接
    // URLConnection conn = realUrl.openConnection();
    // // 设置通用的请求属性
    // conn.setRequestProperty("accept", "*/*");
    // conn.setRequestProperty("connection", "Keep-Alive");
    // conn.setRequestProperty("Content-Type", CONTENT_TYPE_JSON);
    // conn.setRequestProperty("user-agent",
    // "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
    // // 发送POST请求必须设置如下两行
    // conn.setDoOutput(true);
    // conn.setDoInput(true);
    // conn.setUseCaches(false);
    // conn.setConnectTimeout(2000);
    // conn.setReadTimeout(2000);
    // // 获取URLConnection对象对应的输出流
    // out = new PrintWriter(conn.getOutputStream());
    // // 发送请求参数
    // out.print(param);
    // // flush输出流的缓冲
    // out.flush();
    // // 定义BufferedReader输入流来读取URL的响应
    // in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    // String line;
    // while ((line = in.readLine()) != null) {
    // sb.append(line);
    // }
    // } catch (Exception e) {
    // log.error("模板推送失败,http请求出问题 ===================" + e);
    // log.debug("模板推送返回数据 data =================== fail" );
    // return "fail";
    // }
    // //使用finally块来关闭输出流、输入流
    // finally{
    // try{
    // if(out!=null){
    // out.close();
    // }
    // if(in!=null){
    // in.close();
    // }
    // }
    // catch(IOException ex){
    // log.error(ex.getMessage(), ex);
    // }
    // }
    // log.debug("模板推送成功 ===================" );
    // log.debug("模板推送返回数据 data =================== "+ sb.toString() );
    // return sb.toString();
    // }

    /***
     *
     * @param url
     * @param data
     * @return
     * @throws Exception
     */
    public static String post(String url, String data) throws Exception {
        return post(url, data, CONTENT_TYPE_FORM);
    }

    /**
     * Get方式提交
     */
    public static String get(String url, String data) throws Exception {
        log.debug("data=" + data);
        log.debug("url=" + url);
        StringBuffer buffer = new StringBuffer();
        URL getUrl = new URL(url);
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        OutputStreamWriter out = null;
        try {
            connection = (HttpURLConnection) getUrl.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", CONTENT_TYPE_FORM);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setUseCaches(false);
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(SOCKET_TIMEOUT);
            connection.connect();
            out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.write(data);
            out.flush();
            // int rcode = connection.getResponseCode();
            // if (rcode != 200) {
            //
            // }
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e2) {
                    out = null;
                    log.error("关闭流异常：", e2);
                } 
            }
            if (connection != null) {
                try {
                    connection.disconnect();
                 } catch (Exception e2) {
                     connection = null;
                     log.error("关闭流异常：", e2);
                 } 
             }
             if (reader != null) {
                 try {
                     reader.close();
                 } catch (Exception e2) {
                     reader = null;
                     log.error("关闭流异常：", e2);
                 } 
             }
        }
        return buffer.toString();
    }

    /**
     * get方式请求，数据不通过流写出去
     */
    public static String sendGet(String url, String data) throws Exception {
        log.debug("data=" + data);
        log.debug("url=" + url);
        StringBuffer buffer = new StringBuffer();
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL getUrl = new URL(url + "?" + data);
            connection = (HttpURLConnection) getUrl.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setUseCaches(false);
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(SOCKET_TIMEOUT);
            connection.connect();
            int rcode = connection.getResponseCode();
            if (rcode != 200) {

            }
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                 } catch (Exception e2) {
                     connection = null;
                     log.error("关闭流异常：", e2);
                 } 
             }
             if (reader != null) {
                 try {
                     reader.close();
                 } catch (Exception e2) {
                     reader = null;
                     log.error("关闭流异常：", e2);
                 } 
             }
        }
        return buffer.toString();
    }

    /**
     * 绕过验证
     * 
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString)
                    throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString)
                    throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[] { trustManager }, null);
        return sc;
    }

    /**
     * post请求-支持https请求
     * 
     * @Title: sendPost
     * @description:
     * @author add by quaiguo
     * @date 2018年11月1日下午3:23:12
     * @param url 地址
     * @param paramJson 请求参数json格式
     * @param enCoding 编码 可传空，默认UTF-8
     * @param headers 请求头参数 可传空
     * @return
     * @throws Exception String
     */
    public static String sendPost(String url, String paramJson, String enCoding, List<Map<String, String>> headers) throws Exception {
        String body = "";
        CloseableHttpResponse response = null;
        try {
            // 创建post方式请求对象
            HttpPost httpPost = new HttpPost(url);

            // 设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();
            httpPost.setConfig(requestConfig);
            // //表单方式
            // List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            // if(paramMap!=null){
            // for (Entry<String, String> entry : paramMap.entrySet()) {
            // nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            // }
            // }
            // //设置参数到请求对象中
            // httpPost.setEntity(new UrlEncodedFormEntity(nvps, YDStringUtils.isBlank(enCoding) ? ENCODING_UTF_8 : enCoding));
            // json方式
            StringEntity stringEntity = new StringEntity(paramJson, StringUtils.isBlank(enCoding) ? ENCODING_UTF_8 : enCoding);// 解决中文乱码问题
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            // 设置header信息
            if (headers != null && !headers.isEmpty()) {
                for (Map<String, String> header : headers) {
                    if (header != null && !header.isEmpty()) {
                        for (Map.Entry<String, String> entry : header.entrySet()) {
                            httpPost.setHeader(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }
            httpPost.setHeader("Content-type", CONTENT_TYPE_JSON);
            httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            // 执行请求操作，并拿到结果
            response = client.execute(httpPost);
            // 获取结果实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // 按指定编码转换结果实体为String类型
                body = EntityUtils.toString(entity, StringUtils.isBlank(enCoding) ? ENCODING_UTF_8 : enCoding);
            }
            EntityUtils.consume(entity);

        } catch (Exception e) {
            throw e;
        } finally {
            if (response != null) {
                try {
                    // 释放链接
                    response.close();
                } catch (Exception e) {
                    response = null;
                    log.error("sendPost释放CloseableHttpResponse资源异常：", e);
                }
            }
        }
        return body;
    }

    /**
     * 发送get请求获取图像缓冲区
     * 
     * @Title: sendGetQryBufferedImage
     * @description:
     * @author add by quaiguo
     * @date 2019年2月13日下午4:26:54
     * @param url
     * @return
     * @throws Exception BufferedImage
     */
    public static BufferedImage sendGetQryBufferedImage(String url) throws Exception {
        log.debug("get---url:" + url);
        BufferedImage bufferedImage = null;
        CloseableHttpResponse closeableHttpResponse = null;
        InputStream inputStream = null;

        HttpGet httpGet = new HttpGet(url); // 1、创建请求
        try {
            closeableHttpResponse = client.execute(httpGet); // 2、执行
            // 获取响应状态
            int status = closeableHttpResponse.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                HttpEntity httpEntity = closeableHttpResponse.getEntity(); // 3、获取实体
                if (httpEntity != null) {
                    inputStream = httpEntity.getContent();
                    bufferedImage = ImageIO.read(inputStream);
                }
            } else {
                log.error("发送get请求获取图像缓冲区响应失败，状态码：" + status);
            }
        } catch (Exception e) {
            log.error("发送get请求获取图像缓冲区异常：", e);
            throw e;
        } finally {
            if (closeableHttpResponse != null) {
                try {
                    closeableHttpResponse.close();
                } catch (Exception e2) {
                    closeableHttpResponse = null;
                    log.error("关闭流异常：", e2);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e2) {
                    inputStream = null;
                    log.error("关闭流异常：", e2);
                }
            }
        }
        return bufferedImage;
    }

}
