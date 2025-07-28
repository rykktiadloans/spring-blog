package org.rl.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

@Configuration
public class RateLimiterConfig {

    @Bean
    public KeyResolver ipKeyResolver(){
        return exchange -> {
            InetSocketAddress address = exchange.getRequest().getRemoteAddress();
            if(address == null) {
                return Mono.just("unknown");
            }
            return Mono.just(address.getAddress().getHostName());
        };
    }
}
