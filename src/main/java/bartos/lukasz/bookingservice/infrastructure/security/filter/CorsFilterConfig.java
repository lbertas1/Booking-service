package bartos.lukasz.bookingservice.infrastructure.security.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsFilterConfig {

    @Value("${spring.profiles.active}")
    private String applicationProfile;
    
    // ustawić corsa na gwiazdkę Access-Control-Allow-Origin, żeby zezwalał na wartość gwiazdkę. 
    // spróbować z tą domeną aws tutaj, co poniżej. 

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);

//        if (applicationProfile.equals("dev")) {
            corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
//        } else if (applicationProfile.equals("prod")) {
//            corsConfiguration.setAllowedOrigins(Collections.singletonList("http://18.198.24.230/"));
//            corsConfiguration.setAllowedOrigins(Collections.singletonList("http://18.198.24.230"));
//            corsConfiguration.setAllowedOrigins(Collections.singletonList("http://booking-frontend-lbertas1.s3-website.eu-central-1.amazonaws.com/"));
//            corsConfiguration.setAllowedOrigins(Collections.singletonList("http://booking-frontend-lbertas1.s3-website.eu-central-1.amazonaws.com"));
//            corsConfiguration.setAllowedOrigins(Collections.singletonList("**"));
//
//
//        }
//        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://booking-frontend-lbertas1.s3-website.eu-central-1.amazonaws.com"));

//	corsConfiguration.setAllowedOrigins(Collections.singletonList("amazonaws.com"));

        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
                "Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
                "Access-Control-Request-Method", "Access-Control-Request-Headers", "Access-Control-Allow-Methods"));

//        corsConfiguration.setAllowedHeaders(Collections.singletonList("**"));

        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));

        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
