package uk.gov.hmcts.reform.lrdapi.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClusterTest {

    @Test
    public void testClusterConstructor() {
        Cluster cluster = new Cluster("ID", "Cluster Name", "Welsh Cluster Name");

        assertThat(cluster.getClusterId()).isEqualTo("ID");
        assertThat(cluster.getClusterName()).isEqualTo("Cluster Name");
        assertThat(cluster.getWelshClusterName()).isEqualTo("Welsh Cluster Name");
    }

    @Test
    public void testClusterSetters() {
        Cluster cluster = new Cluster();

        cluster.setClusterId("ID");
        cluster.setClusterName("Cluster Name");
        cluster.setWelshClusterName("Welsh Cluster Name");

        assertThat(cluster.getClusterId()).isEqualTo("ID");
        assertThat(cluster.getClusterName()).isEqualTo("Cluster Name");
        assertThat(cluster.getWelshClusterName()).isEqualTo("Welsh Cluster Name");
    }

}
