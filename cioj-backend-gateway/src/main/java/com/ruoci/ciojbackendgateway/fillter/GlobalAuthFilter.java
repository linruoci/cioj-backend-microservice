package com.ruoci.ciojbackendgateway.fillter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @Author: ruoci
 * 鉴权网关。
 **/
@Component
public class GlobalAuthFilter implements GlobalFilter, Ordered {


    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest httpRequest = exchange.getRequest();
        String path = httpRequest.getURI().getPath();

        if (antPathMatcher.match("/**/inner/**", path)){

            ServerHttpResponse httpResponse = exchange.getResponse();
            httpResponse.setStatusCode(HttpStatus.FORBIDDEN);

            DataBufferFactory dataBufferFactory = httpResponse.bufferFactory();
            DataBuffer wrap = dataBufferFactory.wrap(new String("无权限").getBytes(StandardCharsets.UTF_8));
            return httpResponse.writeWith(Mono.just(wrap));
        }
//        todo: 统一权限校验
        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
