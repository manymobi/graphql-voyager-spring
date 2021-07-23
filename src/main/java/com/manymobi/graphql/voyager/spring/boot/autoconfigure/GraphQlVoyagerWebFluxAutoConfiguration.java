package com.manymobi.graphql.voyager.spring.boot.autoconfigure;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.net.URI;

/**
 * @author Liang jianjun
 * @version 1.0.0
 * @since 1.0.0
 * date 2021/7/23 20:57
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@EnableConfigurationProperties(GraphQlVoyagerProperties.class)
@ConditionalOnProperty(name = "graphql.voyager.enabled", matchIfMissing = true)
public class GraphQlVoyagerWebFluxAutoConfiguration {

    private static final Log logger = LogFactory.getLog(GraphQlVoyagerWebFluxAutoConfiguration.class);

    @Value("${spring.graphql.path:/graphql}")
    private String graphQlPath;

    @Bean
    public RouterFunction<ServerResponse> graphQLVoyagerEndpoint(GraphQlVoyagerProperties properties,
                                                                 ResourceLoader resourceLoader) {

        String path = properties.getPath();
        if (logger.isInfoEnabled()) {
            logger.info("GraphQL Voyager endpoint HTTP GET " + path);
        }
        Resource resource = resourceLoader.getResource("classpath:graphql-voyager/index.html");
        return RouterFunctions.route()
                .GET(path, request -> {
                            if (!request.queryParam("path").isPresent()) {
                                URI url = request.uriBuilder().queryParam("path", this.graphQlPath).build();
                                return ServerResponse.temporaryRedirect(url).build();
                            }
                            return ServerResponse.ok().contentType(MediaType.TEXT_HTML)
                                    .bodyValue(resource);
                        }
                ).build();
    }
}
