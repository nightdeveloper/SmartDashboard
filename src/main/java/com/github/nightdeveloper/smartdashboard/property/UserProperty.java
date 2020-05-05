package com.github.nightdeveloper.smartdashboard.property;

public class UserProperty {
    private String login;

    public UserProperty() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "UserProperty{" +
                "login='" + login + '\'' +
                '}';
    }
}
