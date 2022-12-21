package tripkit.tripkitgateway.Config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

  /**
   * "/test-server-1/" 경로로 들어오는 요청의 헤더에 "test-server-1-request" 키와 "test-server-1-request-header" 값을 가지는 헤더를 추가하고 TEST-SERVER-1 이라는 이름을 가진 애플리케이션에 리다이렉트 한다.
   * "/test-server-2/" 경로로 들어오는 요청의 헤더에 "test-server-2-request" 키와 "test-server-2-request-header" 값을 가지는 헤더를 추가하고 TEST-SERVER-2 라는 이름을 가진 애플리케이션에 리다이렉트 한다.
   *
   * 만약 자바 코드로 filter를 적용하고 싶지 않다면?!
   * application.yml 파일을 수정해서 필터를 적용해도 동일하게 작동  - 방법 1
   *
   * spring:
   *   application:
   *     name: gateway-service
   *   cloud:
   *     gateway:
   *       routes:
   *         - id: test-server-1
   *           uri: lb://TEST-SERVER-1
   *           predicates:
   *             - Path=/test-server-1/**
   *           filters:
   *             - AddRequestHeader=test-server-1-request, test-server-1-request-header
   *             - AddResponseHeader=test-server-1-response, test-server-1-response-header
   *         - id: test-server-2
   *           uri: lb://TEST-SERVER-2
   *           predicates:
   *             - Path=/test-server-2/**
   *           filters:
   *             - AddRequestHeader=test-server-2-request, test-server-2-request-header
   *             - AddResponseHeader=test-server-2-response, test-server-2-response-header
   *
   * @param builder
   * @return
   */

  // 즉 리다이렉트를 위한 필터를 자바 코드로 설정  - 방법 2
  // 방법 3은 CustomFilter를 만들고 application.yml에 적용
  // CustomFilter.java 참고
  /** 
  @Bean
  public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){

    return builder.routes()
        .route(r -> r.path("/test-server-1/**")
            .filters(f -> f.addRequestHeader("test-server-1-request", "test-server-1-request-header")
                .addResponseHeader("test-server-1-response", "test-server-1-response-header"))
            .uri("lb://TEST-SERVER-1"))
        .route(r -> r.path("/test-server-2/**")
            .filters(f -> f.addRequestHeader("test-server-2-request", "test-server-2-request-header")
                .addResponseHeader("test-server-2-response", "test-server-2-response-header"))
            .uri("lb://TEST-SERVER-2"))
        .build();
  }
  
   */


}
