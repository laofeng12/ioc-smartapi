package org.openjava.boot.conf;

import lombok.Getter;
import lombok.Setter;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Jiahai
 */
@Component
@ConfigurationProperties(prefix = "jasypt.encryptor")
@Getter
@Setter
public class JasyptConfig {
    /**
     * 密钥
     */
    private String password;

    @Bean
    public BasicTextEncryptor initBasicTextEncryptor() {
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(password);
        return basicTextEncryptor;
    }
}
