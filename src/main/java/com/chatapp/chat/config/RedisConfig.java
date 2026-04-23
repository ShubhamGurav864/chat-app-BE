package com.chatapp.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.URI;
import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${REDIS_URL}")
    private String redisUrl;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        try {
            URI uri = URI.create(redisUrl);

            RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
            redisConfig.setHostName(uri.getHost());
            redisConfig.setPort(uri.getPort() > 0 ? uri.getPort() : 6379);

            // Extract password from URL
            if (uri.getUserInfo() != null) {
                String[] userInfo = uri.getUserInfo().split(":", 2);
                if (userInfo.length > 1) {
                    // Format: username:password
                    redisConfig.setPassword(RedisPassword.of(userInfo[1]));
                } else if (userInfo.length == 1 && !userInfo[0].isEmpty()) {
                    // Format: just password
                    redisConfig.setPassword(RedisPassword.of(userInfo[0]));
                }
            }

            // Build client configuration with SSL support
            LettuceClientConfiguration.LettuceClientConfigurationBuilder clientConfigBuilder = 
                    LettuceClientConfiguration.builder()
                            .commandTimeout(Duration.ofSeconds(60));

            // Enable SSL only if using rediss:// scheme
            if ("rediss".equalsIgnoreCase(uri.getScheme())) {
                clientConfigBuilder.useSsl();
            }

            LettuceClientConfiguration clientConfig = clientConfigBuilder.build();

            return new LettuceConnectionFactory(redisConfig, clientConfig);
            
        } catch (Exception e) {
            // Mask password in error message for security
            String safeUrl = redisUrl != null ? redisUrl.replaceAll(":[^:@]+@", ":***@") : "null";
            throw new RuntimeException("Failed to configure Redis connection. URL format: " + safeUrl, e);
        }
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }
}