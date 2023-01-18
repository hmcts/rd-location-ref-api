package uk.gov.hmcts.reform.lrdapi.config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

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

//    @Bean
//    public OpenAPI api() {
//        return new OpenAPI()
//            .info(
//                new Info().title("Api Documentation")
//                    .description("API to upload and download Documents, retrieve metadata associated with the Documents.")
//                    .version("2-beta")
//            )
//            .security(Collections.singletonList(new SecurityRequirement()
//                                                    .addList("Authorization","Authorization")
//                                                    .addList("ServiceAuthorization","ServiceAuthorization")
//            ))
//            .externalDocs(new ExternalDocumentation());
////            .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
//    }

    @Bean
    public GroupedOpenApi publicApi(OperationCustomizer customGlobalHeaders) {
        return GroupedOpenApi.builder()
            .group("rd-location-ref-api")
            .pathsToMatch("/**")
            .addOperationCustomizer(customGlobalHeaders)
            .build();
    }

    @Bean
    public OperationCustomizer customGlobalHeaders() {
        return (Operation customOperation, HandlerMethod handlerMethod) -> {
            Parameter serviceAuthorizationHeader = new Parameter()
                .in(ParameterIn.HEADER.toString())
                .schema(new StringSchema())
                .name("ServiceAuthorization")
                .description("Keyword `Bearer` followed by a service-to-service token for a whitelisted micro-service")
                .required(true);
            customOperation.addParametersItem(serviceAuthorizationHeader);

            return customOperation;
        };
    }
}
