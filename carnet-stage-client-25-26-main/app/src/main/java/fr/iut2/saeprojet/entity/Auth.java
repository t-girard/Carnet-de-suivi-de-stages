package fr.iut2.saeprojet.entity;

import com.google.gson.annotations.SerializedName;

public class Auth {

    @SerializedName("login")
    private String login;

    @SerializedName("password")
    private String password;

    public Auth(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
