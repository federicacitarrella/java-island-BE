package com.bankIsland.apigateway.filter;

import com.bankIsland.apigateway.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Component
public class JwtAuthenticationFilter implements GatewayFilter{

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();

        final List<String> publicApiEndpoints = List.of("/api/auth/signin", "/api/auth/signup", "/api/auth/prova", "/swagger-ui.html");
        final List<String> userApiEndpoints = List.of("/api/accounts", "/api/accounts/*", "/api/transactions", "/api/transactions/*", "/api/account_owners");
        final List<String> employeeApiEndpoints = List.of("/api/accounts/intern/**", "/api/account_owners/intern/**");

        Predicate<ServerHttpRequest> isApiSecuredGeneral = r -> publicApiEndpoints.stream()
                .noneMatch(uri -> r.getURI().getPath().endsWith(uri));

        Predicate<ServerHttpRequest> isApiSecuredUser = r -> userApiEndpoints.stream()
                .anyMatch(uri -> r.getURI().getPath().endsWith(uri));

        Predicate<ServerHttpRequest> isApiSecuredEmployee = r -> employeeApiEndpoints.stream()
                .anyMatch(uri -> r.getURI().getPath().endsWith(uri));

        if (isApiSecuredGeneral.test(request)) {
            if (!request.getHeaders().containsKey("Authorization")) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);

                return response.setComplete();
            }

            String token = request.getHeaders().getOrEmpty("Authorization").get(0);

            if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            if (!jwtUtils.validateJwtToken(token)) {

                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.BAD_REQUEST);

                return response.setComplete();
            }

            String role = jwtUtils.getRoleFromJwtToken(token);

            if((isApiSecuredUser.test(request) && role.equals("D")) || (isApiSecuredEmployee.test(request) && role.equals("C"))){
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);

                return response.setComplete();
            }
        }
        return chain.filter(exchange);
    }
}
