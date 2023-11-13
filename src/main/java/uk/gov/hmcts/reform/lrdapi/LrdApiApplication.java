package uk.gov.hmcts.reform.lrdapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import uk.gov.hmcts.reform.idam.client.IdamApi;

@EnableJpaAuditing
@EnableJpaRepositories
@EnableCaching
@SpringBootApplication(scanBasePackages = {"uk.gov.hmcts.reform.idam","uk.gov.hmcts.reform.lrdapi"})
@EnableFeignClients(basePackages = {
    "uk.gov.hmcts.reform.lrdapi" },
    basePackageClasses = { IdamApi.class }
)
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
public class LrdApiApplication {

    public static void main(final String[] args) {
        SpringApplication.run(LrdApiApplication.class, args);
    }
}
