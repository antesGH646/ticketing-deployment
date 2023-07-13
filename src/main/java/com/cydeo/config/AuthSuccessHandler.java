package com.cydeo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * This class implementing the onAuthenticationSuccess() method
 * of the AuthenticationSuccessHandler interface which returns
 * a specific page based on a specified role
 * The onAuthenticationSuccess() method is implemented to specify the landing page
 * for every role.
 * The AuthorityUtils has a method to capture a list of
 * authorized roles to avoid duplication.
 * The Authentication has the getAuthorities() method to fetch the roles
 * HttpServletResponse has the sendRedirect() method to specify the returning pages
 *
 */
@Configuration
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("Admin")) {
            response.sendRedirect("/user/create");
        }
        if (roles.contains("Manager")) {
            response.sendRedirect("/project/create");
        }
        if (roles.contains("Employee")) {
            response.sendRedirect("/task/employee");
        }
    }
}

