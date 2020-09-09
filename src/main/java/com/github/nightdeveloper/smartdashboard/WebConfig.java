package com.github.nightdeveloper.smartdashboard;

import com.github.nightdeveloper.smartdashboard.common.Constants;
import com.github.nightdeveloper.smartdashboard.common.oauth2.YandexAuthorizationRequestResolver;
import com.github.nightdeveloper.smartdashboard.common.oauth2.YandexOAuth2UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
@EnableScheduling
public class WebConfig extends WebSecurityConfigurerAdapter {

    ClientRegistrationRepository clientRegistrationRepository;

    YandexOAuth2UserService yandexOAuth2UserService;

    public WebConfig(ClientRegistrationRepository clientRegistrationRepository, YandexOAuth2UserService yandexOAuth2UserService) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.yandexOAuth2UserService = yandexOAuth2UserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .anonymous()

                .and()

                .authorizeRequests().antMatchers("/", Constants.REST_TELEMETRY).permitAll()

                .and()

                .authorizeRequests().anyRequest().authenticated()

                .and()

                .oauth2Login()
                .authorizationEndpoint().authorizationRequestResolver(
                new YandexAuthorizationRequestResolver(
                        clientRegistrationRepository,
                        "/oauth2/authorization"))

                .and()
                .userInfoEndpoint()
                .userService(yandexOAuth2UserService);
    }
}
