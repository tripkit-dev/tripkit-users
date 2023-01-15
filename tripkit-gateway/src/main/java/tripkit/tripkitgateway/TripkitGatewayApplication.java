package tripkit.tripkitgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@SpringBootApplication
public class TripkitGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(TripkitGatewayApplication.class, args);
  }

  /**
   * 게이트웨이를 통한 요청도 csrf를 비활성화 시키기 위해서 SecurityWebFilterChain 빈을 등록
   * @param security
   * @return
   */
  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity security) {
    return security.csrf().disable().build();
  }


}
