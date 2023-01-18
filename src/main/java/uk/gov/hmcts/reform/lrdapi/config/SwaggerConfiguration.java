package uk.gov.hmcts.reform.lrdapi.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
//import org.springframework.web.bind.annotation.RestController;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiKey;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

//import java.util.List;
//import java.util.Optional;
//
import static com.google.common.collect.Lists.newArrayList;

@Configuration
//@EnableSwagger2
public class SwaggerConfiguration {

    /*@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .useDefaultResponseMessages(false)
            .genericModelSubstitutes(Optional.class)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
            .paths(PathSelectors.any())
            .build()
            .securitySchemes(apiKeyList());
    }

    private List<ApiKey> apiKeyList() {
        return
            newArrayList(
                new ApiKey("Authorization", "Authorization","header"),
                new ApiKey("ServiceAuthorization", "ServiceAuthorization", "header")
            );
    }*/

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
            .info(
                new Info().title("Api Documentation")
                    .description("API to upload and download Documents, retrieve metadata associated with the Documents.")
                    .version("2-beta")
            )
            .security(Collections.singletonList(new SecurityRequirement()
                                                    .addList("Authorization","Authorization")
                                                    .addList("ServiceAuthorization","ServiceAuthorization")
            ))
            .externalDocs(new ExternalDocumentation());
//            .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
    }

}
