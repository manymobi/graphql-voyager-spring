package com.manymobi.graphql.voyager.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Liang jianjun
 * @version 1.0.0
 * @since 1.0.0
 * date 2021/7/23 20:57
 */
@ConfigurationProperties(prefix = "graphql.voyager")
public class GraphQlVoyagerProperties {

    /**
     * Path to the GraphQL Voyager UI endpoint.
     */
    private String path = "/graphql-voyager";

    /**
     * Whether the default GraphQL Voyager UI is enabled.
     */
    private boolean enabled = true;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
