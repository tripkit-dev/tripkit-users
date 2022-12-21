package tripkit.tripkitgateway.Filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import tripkit.tripkitgateway.Filter.GlobalFilter.Config;

/**
 * 라우터 정보마다 일일히 필터를 적용하는 것이 아니라 전역으로 Global Filter를 설정하는 방법
 *
 * apply를 통한 처리에서 CustomFilter와 크게 다른 점은 없다.
 * 단지 application.yml의 properties값을 가져오기 위해서 Config 클래스의 필드가 생겼다.
 *
 * 만약 application.yml 파일에서 showPreLogger의 값이 true라면 요청처리 전에 필터에서 전처리가 일어난다.
 * showPostLogger의 값이 true라면 요청처리 후에 필터에서 후처리가 일어난다.
 */
@Slf4j
@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<Config> {

  public GlobalFilter(){
    super(Config.class);
  }

  /**
   * @param config
   * @return
   */
  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();
      ServerHttpResponse response = exchange.getResponse();
      log.info("Global Filter Message: {}", config.getMessage());
      // application.yml 파일에서 show"Pre"Logger의 값이 true라면 요청처리 전에 필터에서 전처리
      if (config.isShowPreLogger()) {
        log.info("Global Filter Start: request id -> {}", request.getId());
      }
      return chain.filter(exchange).then(Mono.fromRunnable(() -> {
        //show"Post"Logger의 값이 true라면 요청처리 후에 필터에서 후처리
        if (config.isShowPostLogger()) {
          log.info("Global Filter End: response code -> {}", response.getStatusCode());
        }
      }));
    };
  }

  /**
   * application.yml의 properties값을 가져오기 위해서 Config 클래스의 필드를 아래와 같이 설정해준 것임
   * application.yml의 
   * cloud:
   *     gateway:
   *       default-filters:
   *         - name: GlobalFilter
   *           args:
   *             message: Spring Cloud Gateway GlobalFilter Message
   *             showPreLogger: true
   *             showPostLogger: true
   * 부분에서 args 부분이 여기로 매핑됨
   *
   * 현재 application.yml에는
   * 이전에 생성한 CustomFilter도 여전히 filter로 등록되어 있기 때문에
   * GlobalFilter의 내용과 같이 출력되는 것을 확인 할 수 있다.
   */
  @Data
  public static class Config{
    private String message;
    private boolean showPreLogger;
    private boolean showPostLogger;
  }

}
