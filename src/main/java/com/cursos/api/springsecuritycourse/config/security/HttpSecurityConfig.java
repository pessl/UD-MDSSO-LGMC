package com.cursos.api.springsecuritycourse.config.security;

import com.cursos.api.springsecuritycourse.config.security.filter.JwtAuthenticationFilter;
import com.cursos.api.springsecuritycourse.persistence.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class HttpSecurityConfig {

    @Autowired
    private AuthenticationProvider daoAuthProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        SecurityFilterChain filterChain = http
                .csrf( csrfConfig -> csrfConfig.disable())
                .sessionManagement( sessMagConfig -> sessMagConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(daoAuthProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests( authReqConfig -> {
                    //buildRequesMatchers(authReqConfig);
                    buildRequesMatchersV2(authReqConfig);
                })
                .build();
        return filterChain;
    }

    private static void buildRequesMatchers(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {
        /*
            AUTORIZACIÓN DE ENDPOINTS DE PRODUCTS
        */
        authReqConfig.requestMatchers(HttpMethod.GET, "/products" )
                //.hasAuthority(RolePermission.READ_ALL_PRODUCTS.name());
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());

        /*authReqConfig.requestMatchers(HttpMethod.GET, "/products/{productId}" )
                .hasAuthority(RolePermission.READ_ONE_PRODUCT.name());*/

        authReqConfig.requestMatchers(RegexRequestMatcher.regexMatcher(HttpMethod.GET, "/products/[0-9]*"))
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());

        authReqConfig.requestMatchers(HttpMethod.POST, "/products" )
                //.hasAuthority(RolePermission.CREATE_ONE_PRODUCT.name());
                .hasRole(Role.ADMINISTRATOR.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/products/{productId}" )
                //.hasAuthority(RolePermission.UPDATE_ONE_PRODUCT.name());
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/products/{productId}/disabled" )
                //.hasAuthority(RolePermission.DISABLE_ONE_PRODUCT.name());
                .hasRole(Role.ADMINISTRATOR.name());

        /*
            AUTORIZACIÓN DE ENDPOINTS DE CATEGORIES
        */
        authReqConfig.requestMatchers(HttpMethod.GET, "/categories" )
                //.hasAuthority(RolePermission.READ_ALL_CATEGORIES.name());
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());

        authReqConfig.requestMatchers(HttpMethod.GET, "/categories/{categoryId}" )
                //.hasAuthority(RolePermission.READ_ONE_CATEGORY.name());
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());

        authReqConfig.requestMatchers(HttpMethod.POST, "/categories" )
                //.hasAuthority(RolePermission.CREATE_ONE_CATEGORY.name());
                .hasRole(Role.ADMINISTRATOR.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/categories/{categoryId}" )
                //.hasAuthority(RolePermission.UPDATE_ONE_CATEGORY.name());
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/categories/{categoryId}/disabled" )
                //.hasAuthority(RolePermission.DISABLE_ONE_CATEOGORY.name());
                .hasRole(Role.ADMINISTRATOR.name());

        /*
            AUTORIZACIÓN DE ENDPOINT DE PROFILE
        */
        authReqConfig.requestMatchers(HttpMethod.PUT, "/auth/profile" )
                //.hasAuthority(RolePermission.READ_MY_PROFILE.name());
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name(), Role.CUSTOMER.name());

        /*
            AUTORIZACIÓN DE ENDPOINTS PÚBLICOS
        */
        authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate-token").permitAll();

        /*
            AUTORIZACIÓN DE ENDPOINTS PRIVADOS
        */
        authReqConfig.anyRequest().authenticated();
    }

    private static void buildRequesMatchersV2(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {
        /*
            AUTORIZACIÓN DE ENDPOINTS PÚBLICOS
        */
        authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate-token").permitAll();

        /*
            AUTORIZACIÓN DE ENDPOINTS PRIVADOS
        */
        authReqConfig.anyRequest().authenticated();
    }
}
