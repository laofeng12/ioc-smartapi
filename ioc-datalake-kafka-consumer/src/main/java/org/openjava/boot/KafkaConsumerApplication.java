package org.openjava.boot;

import com.openjava.framework.user.CloudUserProvider;
import com.openjava.framework.user.LmAuthorityPersistent;
import com.openjava.framework.validate.RedisSessionVaidator;
import org.ljdp.common.spring.SpringContext;
import org.ljdp.core.db.jpa.JPASessionFactoryRouter;
import org.ljdp.secure.validate.SessionValidator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@EntityScan(basePackages = {
        "org.ljdp.plugin.batch.persistent",
        "org.ljdp.support.attach.domain",
        "com.openjava.**.domain"
})
@EnableJpaRepositories(
        basePackages = {"org.ljdp.support.**.repository",
                "com.openjava.**.repository"},
        repositoryFactoryBeanClass = org.ljdp.core.spring.data.LjdpJpaRepositoryFactoryBean.class)
@MapperScan({
        "com.openjava.**.dao",
        "com.openjava.**.mapper"
})
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@SpringBootApplication(
        scanBasePackages = {
                "org.ljdp.support.**.component",
                "org.ljdp.support.**.service",
                "org.ljdp.support.**.controller",
                "com.openjava.**.service",
                "com.openjava.**.component",
                "com.openjava.**.dao",
                "com.openjava.**.util",
                "org.openjava.boot.conf",
                "com.openjava.datalake.consumer",
                "com.openjava.**.rabbitMQ",
                "org.openjava.boot.tenant",
                "com.openjava.**.common"

        })
@ServletComponentScan(basePackages = {"org.ljdp.support.web.listener2"})
@ImportResource("classpath:springconfig/transaction.xml")
@EnableCaching
@EnableScheduling
public class KafkaConsumerApplication {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    /**
     * Spring Boot 2.x 缓存配置
     *
     * @param factory
     * @return
     */
    @Bean("cacheManager")
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        // 设置缓存的默认过期时间，也是使用Duration设置
        config = config.entryTtl(Duration.ofMinutes(60))
                // 不缓存空值
                .disableCachingNullValues();

        // 设置一个初始化的缓存空间set集合
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add("default");
        cacheNames.add("quick");

        // 对每个缓存空间应用不同的配置
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        configMap.put("default", config);
        configMap.put("quick", config.entryTtl(Duration.ofSeconds(120)));

        // 使用自定义的缓存配置初始化一个cacheManager
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(configMap)
                .build();
        return cacheManager;
    }

    /**
     * ==============会话认证=====================
     *
     * @return
     */
    @Bean
    public SessionValidator sessionValidator() {
        return new RedisSessionVaidator();
//		return new EhcacheSessionValidator();
    }

    @Bean
    public LmAuthorityPersistent authorityPersistent() {
        return new LmAuthorityPersistent();
    }

    /**
     * ============LJDP相关配置=======================
     *
     * @return
     */
    @Bean("db.SessionFactoryRouter")
    public JPASessionFactoryRouter sessionFactoryRouter() {
        return new JPASessionFactoryRouter();
    }

    @Bean("web.UserProvider")
    public CloudUserProvider lmUserProvider() {
        return new CloudUserProvider();
    }

    @Bean
    public KafkaConsumerBootRunner JobBootRunner() {
        return new KafkaConsumerBootRunner();
    }

    @Bean
    public SpringContext springContext() {
        return SpringContext.getEmbedInstance();
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(5)) // 5秒超时
                .setReadTimeout(Duration.ofSeconds(5))
                .additionalMessageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8))
                .build();
        return restTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaConsumerApplication.class, args);
    }

}
