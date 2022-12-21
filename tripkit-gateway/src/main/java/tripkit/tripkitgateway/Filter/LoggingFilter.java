package tripkit.tripkitgateway.Filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**Logging Filter는
 * Custom Filter와 거의 동일한 방식으로 진행되며 추가로 Filter가 적용되는 순서를 지정하는 방법에 대해서 알아본다.
 */
@Slf4j
@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
  //이게 없으면 aplication.yml의 args와 Config 매핑이 안 됨;;
  public LoggingFilter() {
    super(Config.class);
  }
  @Override
  public GatewayFilter apply(LoggingFilter.Config config) {
    //이전(CustomFilter, Global Filter)과 다르게 apply에서 반환하는 타입이 OrderedGatewayFilter로 구체화 되었다.
    return new OrderedGatewayFilter((exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();
      ServerHttpResponse response = exchange.getResponse();
      log.info("Logging filter message: {}", config.getMessage());
      //application.yml 파일에서 show"Pre"Logger의 값이 true라면 요청처리 전에 필터에서 전처리
      if (config.isShowPreLogger()) {
        log.info("Logging filter pre process: request uri -> {}", request.getURI());
      }
      return chain.filter(exchange).then(Mono.fromRunnable(() -> {
        //show"Post"Logger의 값이 true라면 요청처리 후에 필터에서 후처리
        if (config.isShowPostLogger()) {
          log.info("Logging filter post process: response code -> {}", response.getStatusCode());
        }
      }));
    }, Ordered.LOWEST_PRECEDENCE); //파라미터로 Ordered.LOWEST_PRECEDENCE를 전달받아서 적용되어 있는 Filter 중 가장 낮은 우선권을 가지도록 구현하였다
  }

  /**
   * application.yml의 properties값을 가져오기 위해서 Config 클래스의 필드를 아래와 같이 설정해준 것임
   * application.yml의

   * 부분에서 args 부분이 여기로 매핑됨
   *
   */
  @Data
  public static class Config {
    private String message;
    private boolean showPreLogger;
    private boolean showPostLogger;
  }
}
