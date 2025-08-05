package main.app.rental_app.auth.jwt;

import main.app.rental_app.exc.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    private final JwtUtil jwtUtil;
    
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = jwtUtil.getTokenFromRequest(request);

            if (token != null) {
                String username = jwtService.extractUsername(token);
                log.debug("Extracted username from token: {}", username);

                if (username != null && jwtUtil.isAuthenticatedNotSet()) {
                    log.debug("Setting authentication for user: {}", username);
                    jwtUtil.authenticateUser(token, username, request);
                } else if (username == null) {
                    log.debug("Could not extract username from token");
                } else {
                    log.debug("Authentication already set for user: {}", username);
                }
            } else {
                log.debug("No JWT token found in request");
            }
        } catch (Exception e) {
            log.error("Error processing JWT token: {}", e.getMessage());
            throw new UnauthorizedException("Invalid or expired token");
        }

        filterChain.doFilter(request, response);
    }
}
