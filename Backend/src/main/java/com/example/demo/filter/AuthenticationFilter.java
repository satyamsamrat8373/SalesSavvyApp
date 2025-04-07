package com.example.demo.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.demo.entities.Role;
import com.example.demo.entities.Users;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.AuthService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = {"/api/*", "/admin/*"})
@Component
public class AuthenticationFilter implements Filter {

    private final AuthService authService;
    private final UserRepository urepo;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    private static final String ALLOWED_ORIGIN = "http://localhost:5173";
    public static final String[] UNAUTHENTICATED_PATHS = {
            "/api/users/register",
            "/api/auth/login"
    };

    public AuthenticationFilter(AuthService authService, UserRepository urepo) {
    	System.err.println("FILTER STARTED");
        this.authService = authService;
        this.urepo = urepo;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        setCORSHeaders1(httpResponse); // Set CORS headers
        try {
            executeFilterLogin(request, response, chain);
        } catch (Exception e) {
            logger.error("Unexpected Error in Authentication Filter ", e);
            sendErrorResponse(httpResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    public void executeFilterLogin(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        logger.info("Request URI: {}", requestURI);

        if (Arrays.asList(UNAUTHENTICATED_PATHS).contains(requestURI)) {
            chain.doFilter(request, response);
            return;
        }
        
        if (httpRequest.getMethod().equalsIgnoreCase("OPTIONS")) { 
        	setCORSHeaders1(httpResponse); return; 
        }
        
        String token = getAuthTokenFromCookie(httpRequest);
        logger.info("Extracted token: {}", token);

        if (token == null || !authService.validateToken(token)) {
            sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid Token");
            return;
        }

        String username = authService.extractUsername(token);
        Optional<Users> optionalUser = urepo.findByUsername(username);
        if (optionalUser.isEmpty()) {
            sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: User Not Found");
            return;
        }

        Users authenticatedUser = optionalUser.get();
        Role role = authenticatedUser.getRole();
        logger.info("Authenticated User: {}, Role: {}", authenticatedUser.getUsername(), role);

        if (requestURI.startsWith("/api/") && (role != Role.CUSTOMER && role != Role.ADMIN)) {
            sendErrorResponse(httpResponse, HttpServletResponse.SC_FORBIDDEN, "Unauthorized: Not allowed for Role");
            return;
        }
        if (requestURI.startsWith("/admin/") && role != Role.ADMIN) {
            sendErrorResponse(httpResponse, HttpServletResponse.SC_FORBIDDEN, "Unauthorized: Not allowed for Role");
            return;
        }

        httpRequest.setAttribute("authenticatedUser", authenticatedUser);
        chain.doFilter(request, response);
    }

    
    private void setCORSHeaders1(HttpServletResponse response) { 
    	response.setHeader("Access-Control-Allow-Origin", ALLOWED_ORIGIN); 
    	response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"); 
    	response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization"); 
    	response.setHeader("Access-Control-Allow-Credentials", "true"); 
    	response.setStatus(HttpServletResponse.SC_OK); 
    }
    
    
    public void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(statusCode);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + message + "\"}");
        } else {
            logger.warn("Response already committed, cannot send error message: " + message);
        }
    }

    public String getAuthTokenFromCookie(HttpServletRequest request) {
		        Cookie[] cookies = request.getCookies();
		        if (cookies != null) { 
		        	return Arrays.stream(cookies) 
		        			.filter(cookie -> "authToken".equals(cookie.getName())) 
		        			.map(Cookie::getValue) 
		        			.findFirst() 
		        			.orElse(null); 
		        	} 
		        	return null; 
		        }
}