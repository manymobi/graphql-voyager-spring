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
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

/**
 * @author Liang jianjun
 * @version 1.0.0
 * @since 1.0.0
 * date 2021/7/23 20:57
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(GraphQlVoyagerProperties.class)
@ConditionalOnProperty(name = "graphql.voyager.enabled", matchIfMissing = true)
public class GraphQlVoyagerWebMvcAutoConfiguration {


    private static final Log logger = LogFactory.getLog(GraphQlVoyagerWebMvcAutoConfiguration.class);

    @Value("${spring.graphql.path:/graphql}")
    private String graphQlPath;

    @Bean
    public RouterFunction<ServerResponse> graphQLVoyagerRouterFunction(GraphQlVoyagerProperties properties, ResourceLoader resourceLoader) {

        String path = properties.getPath();
        if (logger.isInfoEnabled()) {
            logger.info("GraphQL Voyager endpoint HTTP GET " + path);
        }

        Resource resource = resourceLoader.getResource("classpath:graphql-voyager/index.html");
        return RouterFunctions.route()
                .GET(properties.getPath(), request ->
                        {
                            if (!request.param("path").isPresent()) {
                                URI url = request.uriBuilder().queryParam("path", this.graphQlPath).build();
                                return ServerResponse.temporaryRedirect(url).build();
                            }
                            return ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(resource);
                        }
                ).build();
    }
}
