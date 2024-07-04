package uk.gov.hmcts.reform.lrdapi.util;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class LocationRefApiPostgresqlContainer extends PostgreSQLContainer<LocationRefApiPostgresqlContainer> {
    private static final String IMAGE_VERSION = "postgres:11.1";

    private LocationRefApiPostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    @Container
    private static final LocationRefApiPostgresqlContainer container = new LocationRefApiPostgresqlContainer();

}
