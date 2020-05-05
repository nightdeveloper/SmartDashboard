package com.github.nightdeveloper.smartdashboard.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix="users")
public class UsersProperty {

    private List<UserProperty> allowed;

    public UsersProperty() {
    }

    public List<UserProperty> getAllowed() {
        return allowed;
    }

    public void setAllowed(List<UserProperty> allowed) {
        this.allowed = allowed;
    }

    @Override
    public String toString() {
        return "UsersProperty{" +
                "allowed=" + allowed +
                '}';
    }

    public boolean isUserAllowed(String login) {
        return allowed != null && allowed
                .stream().filter(userProperty -> userProperty.getLogin() != null &&
                        userProperty.getLogin().toLowerCase().equals(login.toLowerCase()) ).count() == 1;
    }
}
