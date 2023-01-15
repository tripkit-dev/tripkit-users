package tripkit.tripkitgateway.Filter;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final Environment environment;

    public AuthorizationHeaderFilter(Environment environment) {
        super(Config.class);
        this.environment = environment;
    }

    /**
     * 사용자의 헤더에 Authorization 값이 없거나 유효한 토큰이 아니라면 사용자에게 권한이 없다는 401 Unauthorized 코드를 반환
     * @param config
     * @return
     */
    @Override
    public GatewayFilter apply(AuthorizationHeaderFilter.Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer", "");
            if (!isJwtValid(jwt)) return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(err);
        return response.setComplete();
    }

    /**
     * JWT를 파싱하여 유효한 토큰인지 확인한다.
     * 여기서 사용되는 token.secret는 다음 단계에서 입력하겠지만 유저 서비스에서 사용하는 토큰과 동일
     * @param jwt
     * @return
     */
    private boolean isJwtValid(String jwt) {
        String subject = null;
        try {
            subject = Jwts.parser().setSigningKey(environment.getProperty("token.secret"))
                    .parseClaimsJws(jwt).getBody()
                    .getSubject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return !Strings.isBlank(subject);
    }

    public static class Config {}

}

