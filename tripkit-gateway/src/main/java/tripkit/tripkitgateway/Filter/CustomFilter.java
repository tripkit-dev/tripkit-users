package tripkit.tripkitgateway.Filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component // 빈으로 등록 // 방법 3
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
  //생성자
  public CustomFilter(){
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(CustomFilter.Config config) {
    return (exchange, chain)->{
      //pre process start
      ServerHttpRequest request = exchange.getRequest();//서버에 대한 요청
      ServerHttpResponse response = exchange.getResponse();//서버 응답
      log.info("Custom pre process filter: request uri -> {}", request.getId());
      //pre process end

      //post process start
      return chain.filter(exchange).then(Mono.fromRunnable(()->{
        log.info("Custom post process filter: response code -> {}", response.getStatusCode());
      }));
      //post process end
    };
  }

  public static class Config{
    // put the configuration properties
  }
}
