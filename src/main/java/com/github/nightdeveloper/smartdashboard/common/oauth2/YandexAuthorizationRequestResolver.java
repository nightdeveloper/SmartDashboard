package com.github.nightdeveloper.smartdashboard.common.oauth2;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

public class YandexAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;

    public YandexAuthorizationRequestResolver(
            ClientRegistrationRepository repo, String authorizationRequestBaseUri) {
        defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(repo, authorizationRequestBaseUri);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest req = defaultResolver.resolve(request);
        if(req != null) {
            req = customizeAuthorizationRequest(req);
        }
        return req;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest req = defaultResolver.resolve(request, clientRegistrationId);
        if(req != null) {
            req = customizeAuthorizationRequest(req);
        }
        return req;
    }

    private OAuth2AuthorizationRequest customizeAuthorizationRequest(
            OAuth2AuthorizationRequest req) {

        Set<String> newScopes = new HashSet<>(req.getScopes());
        newScopes.remove("openid");

        return OAuth2AuthorizationRequest
                .from(req)
                .scopes(newScopes)
//                .additionalParameters(new HashMap<String, Object>() {{
//                }})
                .build();
    }
}
