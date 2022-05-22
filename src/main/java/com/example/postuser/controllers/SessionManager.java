package com.example.postuser.controllers;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;


@Component
public class SessionManager {
    private static final String LOGGED_USER_ID = "LoggedUser";

    public int getLoggedUser(HttpSession session) throws AuthenticationException {
        if (session.getAttribute(LOGGED_USER_ID) == null) {
            throw new AuthenticationException("You have to log in!");
        } else {
            return (Integer) session.getAttribute(LOGGED_USER_ID);
        }
    }

    public void loginUser(HttpSession ses, Integer id) {
        ses.setAttribute(LOGGED_USER_ID, id);
    }

    public void logoutUser(HttpSession ses) {
        ses.invalidate();
    }
}
