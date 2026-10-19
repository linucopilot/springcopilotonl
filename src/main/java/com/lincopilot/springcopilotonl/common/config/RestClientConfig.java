// package com.lincopilot.springcopilotonl.common.config;

// import java.security.KeyManagementException;
// import java.security.KeyStoreException;
// import java.security.NoSuchAlgorithmException;
// import java.security.cert.X509Certificate;
// import java.util.ArrayList;
// import java.util.List;
// import javax.net.ssl.SSLContext;
// import java.util.concurrent.TimeUnit;
// import javax.annotation.PreDestroy;

// import org.apache.http.conn.ssl.NoopHostnameVerifier;
// import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
// import org.apache.http.impl.client.CloseableHttpClient;
// import org.apache.http.impl.client.HttpClients;
// import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
// import org.apache.http.ssl.SSLContexts;
// import org.apache.http.ssl.TrustStrategy;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.client.BufferingClientHttpRequestFactory;
// import org.springframework.http.client.ClientHttpRequestFactory;
// import org.springframework.http.client.ClientHttpRequestInterceptor;
// import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
// import org.springframework.util.CollectionUtils;
// import org.apache.http.client.config.RequestConfig;
// import org.apache.http.config.SocketConfig;
// import org.apache.http.conn.ConnectionKeepAliveStrategy;
// import org.springframework.context.annotation.Primary;
// import org.springframework.scheduling.annotation.EnableScheduling;
// import org.springframework.scheduling.annotation.Scheduled;

// import lombok.Getter;
// import lombok.Setter;
// import lombok.extern.slf4j.Slf4j;

// /**
//  * HTTP 요청을 처리하기 위한 RestClient의 환경 설정을 위해 작성되었다.
//  */
// @Slf4j
// @Configuration
// @EnableScheduling
// @ConditionalOnProperty(prefix = "rest.client", name = "appSource")
// @ConfigurationProperties(prefix = "rest", ignoreUnknownFields = false)
// public class RestClientConfig {

// 	@Getter
// 	@Setter
// 	public Client client;

// 	@Getter
// 	@Setter
// 	public static class Client {
		
// 		String appSource;
// 		int connectionRequestTimeout;
// 		int readTimeout;
// 		int connectTimeout;
		
// 		//wso2 url
// 		String servicePrefix;
// 		String wso2apiPrefixInt;
// 		String wso2apiPrefixExt;
// 		String wso2apiPrefixDmz;
// 		String wso2apiCreditInt;
// 		//apikey
// 		String loan_apikey;
// 		String loan_ext_apikey;
// 		String loan_dmz_apikey;
// 		String loan_dmb_apikey;
// 		String mod_apikey;
// 		String coll_apikey;
// 		String credit_apikey;
		
// 	}
    
// 	private PoolingHttpClientConnectionManager connectionManager;

//     // ---------------------- 1. SSL Context (Trust All) ----------------------
//     @Bean
//     public SSLContext sslContext() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
//         TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        
//         SSLContext sslContext = SSLContexts.custom()
//                 .loadTrustMaterial(null, acceptingTrustStrategy)
//                 .build();
        
//         log.info("✅ SSLContext initialized with trust-all strategy");
//         return sslContext;
//     }

//     // ---------------------- 2. SSL Socket Factory ----------------------
//     @Bean
//     public SSLConnectionSocketFactory sslSocketFactory(SSLContext sslContext) {
//         SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
//                 sslContext, 
//                 NoopHostnameVerifier.INSTANCE
//         );
//         log.info("✅ SSLConnectionSocketFactory created");
//         return socketFactory;
//     }

//     // ---------------------- 3. Connection Pool ----------------------
//     @Bean
//     public PoolingHttpClientConnectionManager poolingConnectionManager(SSLConnectionSocketFactory sslSocketFactory) {
//         // Create registry with SSL socket factory
//         org.apache.http.config.Registry<org.apache.http.conn.socket.ConnectionSocketFactory> socketFactoryRegistry = 
//             org.apache.http.config.RegistryBuilder.<org.apache.http.conn.socket.ConnectionSocketFactory>create()
//                 .register("https", sslSocketFactory)
//                 .register("http", new org.apache.http.conn.socket.PlainConnectionSocketFactory())
//                 .build();
        
//         connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
//         connectionManager.setMaxTotal(2000);
//         connectionManager.setDefaultMaxPerRoute(500);
//         connectionManager.setValidateAfterInactivity(5000);

//         SocketConfig socketConfig = SocketConfig.custom()
//                 .setSoTimeout(client.getReadTimeout())
//                 .setTcpNoDelay(true)
//                 .build();
//         connectionManager.setDefaultSocketConfig(socketConfig);

//         log.info("✅ ConnectionManager initialized with SSL (maxTotal={}, maxPerRoute={})",
//                 connectionManager.getMaxTotal(), connectionManager.getDefaultMaxPerRoute());
//         return connectionManager;
//     }

//     // ---------------------- 4. Keep-Alive Strategy ----------------------
//     private final ConnectionKeepAliveStrategy keepAliveStrategy = (response, context) -> 30_000;

//     // ---------------------- 5. HttpClient ----------------------
//     @Bean(destroyMethod = "close")
//     public CloseableHttpClient pooledHttpClient(
//             PoolingHttpClientConnectionManager manager,
//             SSLConnectionSocketFactory sslSocketFactory) {

//         RequestConfig requestConfig = RequestConfig.custom()
//                 .setConnectTimeout(client.getConnectTimeout())
//                 .setConnectionRequestTimeout(client.getConnectionRequestTimeout())
//                 .setSocketTimeout(client.getReadTimeout())
//                 .build();

//         CloseableHttpClient httpClient = HttpClients.custom()
//                 .setConnectionManager(manager)
//                 .setConnectionManagerShared(true)
//                 .setDefaultRequestConfig(requestConfig)
//                 .setSSLSocketFactory(sslSocketFactory) // Use the SSL socket factory bean
//                 .setKeepAliveStrategy(keepAliveStrategy)
//                 .evictExpiredConnections()
//                 .evictIdleConnections(15, TimeUnit.SECONDS)
//                 .build();
        
//         log.info("✅ HttpClient initialized with custom SSL and connection pooling");
//         return httpClient;
//     }

//     // ---------------------- 6. Scheduled Cleanup ----------------------
//     @Scheduled(fixedDelay = 30_000)
//     public void evictIdleConnections() {
//         if (connectionManager != null) {
//             connectionManager.closeExpiredConnections();
//             connectionManager.closeIdleConnections(30, TimeUnit.SECONDS);
//             log.debug("🧹 Evicted expired/idle HTTP connections");
//         }
//     }

//     @PreDestroy
//     public void shutdown() {
//         if (connectionManager != null) {
//             connectionManager.shutdown();
//             log.info("🧩 HTTP Connection Manager shut down");
//         }
//     }

//     // ---------------------- 7. Request Factory ----------------------
//     private ClientHttpRequestFactory requestFactory(CloseableHttpClient httpClient) {
//         HttpComponentsClientHttpRequestFactory factory = 
//                 new HttpComponentsClientHttpRequestFactory(httpClient);
//         factory.setConnectTimeout(client.getConnectTimeout());
//         factory.setReadTimeout(client.getReadTimeout());
//         factory.setConnectionRequestTimeout(client.getConnectionRequestTimeout());
//         return new BufferingClientHttpRequestFactory(factory);
//     }

//     // ---------------------- 8. Logging Interceptor ----------------------
//     @Bean
//     public RestTemplateLogInterceptor restTemplateLogInterceptor() {
//         return new RestTemplateLogInterceptor();
//     }

//     @Autowired
//     private RestTemplateLogInterceptor restTemplateLogInterceptor;

//     // ---------------------- 9. RestClient ----------------------
//     @Bean
//     @Primary
//     public RestClient restClient(CloseableHttpClient pooledHttpClient) {
//         RestClient restClient = new RestClient(requestFactory(pooledHttpClient));
        
//         List<ClientHttpRequestInterceptor> interceptors = restClient.getInterceptors();
//         if (CollectionUtils.isEmpty(interceptors)) {
//             interceptors = new ArrayList<>();
//         }
        
//         interceptors.add(restTemplateLogInterceptor);
//         restClient.setInterceptors(interceptors);

//         log.info("✅ RestClient initialized with high-performance HTTP pool and SSL");
//         return restClient;
//     }
 
// }
