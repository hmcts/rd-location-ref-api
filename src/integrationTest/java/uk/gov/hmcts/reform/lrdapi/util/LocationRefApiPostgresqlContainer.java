package uk.gov.hmcts.reform.lrdapi.util;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class LocationRefApiPostgresqlContainer extends PostgreSQLContainer<LocationRefApiPostgresqlContainer> {
    private static final String IMAGE_VERSION = "postgres:16";

    private LocationRefApiPostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    @Container
    private static final LocationRefApiPostgresqlContainer container = new LocationRefApiPostgresqlContainer();

}
