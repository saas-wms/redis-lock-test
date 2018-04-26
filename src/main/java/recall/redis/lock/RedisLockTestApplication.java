package recall.redis.lock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Main
 * @author recall
 * @date 2018/4/27
 * @comment
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableSwagger2
public class RedisLockTestApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RedisLockTestApplication.class);
        app.run(args);
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("recall.redis.lock"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("RedisLockTest")
                .description("RedisLockTest")
                .version("1.0")
                .build();
    }

}
