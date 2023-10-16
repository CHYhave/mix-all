package org.ajhy.util.http;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;

/**
 * @author jingfeng.xjf
 * @date 2022/10/24
 */
public final class HttpClientUtil {

    private HttpClientUtil() {}

    private static CloseableHttpClient httpclient;
    private static CloseableHttpAsyncClient httpAsyncClient;

    /**
     * 连接超时时间，由bean factory设置，缺省为8秒钟
     */
    private final static int defaultConnectionTimeout = 8000 * 2;

    /**
     * 回应超时时间, 由bean factory设置，缺省为30秒钟
     */
    private final static int defaultSoTimeout = 1000 * 10;

    /**
     * 默认等待HttpConnectionManager返回连接超时（只有在达到最大连接数时起作用）：1秒
     */
    private final static int defaultHttpConnectionManagerTimeout = 3 * 1000;

    static {

        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] xcs, String str) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] xcs, String str) {
            }
        };
        SSLConnectionSocketFactory socketFactory = null;
        try {
            SSLContext ctx = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
            ctx.init(null, new TrustManager[] {trustManager}, null);
            socketFactory = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Registry<ConnectionSocketFactory> sRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", socketFactory)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        PoolingHttpClientConnectionManager poolConnManager = new PoolingHttpClientConnectionManager(sRegistry);
        // 设置最大的连接数
        poolConnManager.setMaxTotal(2000);
        // 设置每个路由的基础连接数【默认，每个路由基础上的连接不超过2个，总连接数不能超过20】
        poolConnManager.setDefaultMaxPerRoute(20);

        httpclient = HttpClients.custom()
                .setConnectionManager(poolConnManager)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setSocketTimeout(defaultSoTimeout)
                        .setConnectTimeout(defaultConnectionTimeout)
                        .setConnectionRequestTimeout(defaultHttpConnectionManagerTimeout)
                        .build()).build();
    }

    static {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        // don't check
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        // don't check
                    }
                }
        };

        PoolingNHttpClientConnectionManager pool;
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, null);
            SSLIOSessionStrategy sslioSessionStrategy = new SSLIOSessionStrategy(sslContext,
                    SSLIOSessionStrategy.ALLOW_ALL_HOSTNAME_VERIFIER);
            Registry<SchemeIOSessionStrategy> registry = RegistryBuilder.<SchemeIOSessionStrategy>create()
                    .register("http", NoopIOSessionStrategy.INSTANCE)
                    .register("https", sslioSessionStrategy)
                    .build();
            ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
            pool = new PoolingNHttpClientConnectionManager(ioReactor, registry);
            pool.setMaxTotal(2000); // 设置最多连接数
            pool.setDefaultMaxPerRoute(20); // 设置每个host最多20个连接数
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        httpAsyncClient = HttpAsyncClients
                .custom()
                .setConnectionManager(pool)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(20000)
                        .setSocketTimeout(20000)
                        .setConnectionRequestTimeout(20000)
                        .build())
                .setMaxConnTotal(20000)
                .build();

        httpAsyncClient.start();
    }

    public static CloseableHttpClient httpClient() {
        return httpclient;
    }

    public static CloseableHttpAsyncClient httpAsyncClient() {
        return httpAsyncClient;
    }


}
