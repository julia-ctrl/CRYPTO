package org.julia.lab56.service;

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
