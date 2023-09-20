package org.example.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException, IOException {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            response.sendRedirect("/main");
        } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
            response.sendRedirect("/main");
        } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("COMPOSITOR"))) {
            response.sendRedirect("/main");
        } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("SUPER_ADMIN"))) {
            response.sendRedirect("/main");
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}


