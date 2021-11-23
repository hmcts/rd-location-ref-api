package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClusterTest {

    @Test
    void testClusterConstructor() {
        Cluster cluster = new Cluster("ID", "Cluster Name", "Welsh Cluster Name");

        assertEquals("ID", cluster.getClusterId());
        assertEquals("Cluster Name", cluster.getClusterName());
        assertEquals("Welsh Cluster Name", cluster.getWelshClusterName());
    }

    @Test
    void testClusterSetters() {
        Cluster cluster = new Cluster();

        cluster.setClusterId("ID");
        cluster.setClusterName("Cluster Name");
        cluster.setWelshClusterName("Welsh Cluster Name");

        assertEquals("ID", cluster.getClusterId());
        assertEquals("Cluster Name", cluster.getClusterName());
        assertEquals("Welsh Cluster Name", cluster.getWelshClusterName());
    }

}
