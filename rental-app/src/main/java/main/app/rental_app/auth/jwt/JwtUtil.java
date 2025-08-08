package main.app.rental_app.auth.jwt;

import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.app.rental_app.auth.repository.RefreshTokenRepository;
import main.app.rental_app.user.repository.UserRepository;
import main.app.rental_app.user.model.User;
import main.app.rental_app.exc.UnauthorizedException;
import main.app.rental_app.exc.ForbiddenException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean isAuthenticatedNotSet() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    public void authenticateUser(String jwt, String username, HttpServletRequest request)  {
        try {
            log.debug("Authenticating user: {}", username);
            
            // Check if user has any active refresh tokens
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                var activeTokens = refreshTokenRepository.findAllByUserAndIsRevokedFalse(user);
                if (activeTokens.isEmpty()) {
                    log.debug("User {} has no active refresh tokens, denying access", username);
                    return;
                }
            }
            
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            log.debug("Loaded user details for: {}, authorities: {}", username, userDetails.getAuthorities());
            
            if (jwtService.isTokenValid(jwt, userDetails)) {
                log.debug("Token is valid for user: {}", username);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.debug("Authentication set successfully for user: {}", username);
            } else {
                log.debug("Token is not valid for user: {}", username);
            }
        } catch (Exception e) {
            log.error("Error authenticating user with JWT: {}", e.getMessage());
        }
    }

    public void checkAuthentication() throws UnauthorizedException {
        if (isAuthenticatedNotSet()) {
            log.warn("Unauthorized access attempt - user not authenticated");
            throw new UnauthorizedException("Authentication required to access this resource");
        }
    }
    
    public void checkAuthorization(String requiredRole) throws ForbiddenException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Forbidden access attempt - user not authenticated");
            throw new ForbiddenException("Access denied: Authentication required");
        }
        
        boolean hasRequiredRole = authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals(requiredRole));
            
        if (!hasRequiredRole) {
            log.warn("Forbidden access attempt - user {} lacks required role: {}", 
                authentication.getName(), requiredRole);
            throw new ForbiddenException("Access denied: Insufficient privileges");
        }
        
        log.debug("User {} authorized with role: {}", authentication.getName(), requiredRole);
    }
        
}
