package com.safecar.platform.iam.infrastructure.authorization.sfs.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Unauthorized Request Handler Entry Point
 * <p>
 *     This class is the entry point for unauthorized requests.
 *     It will handle unauthorized requests.
 * </p>
 */
@Component
public class UnauthorizedRequestHandlerEntryPoint implements AuthenticationEntryPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnauthorizedRequestHandlerEntryPoint.class);

    /**
     * Handle unauthorized requests
     * <p>
     *     This method will handle unauthorized requests.
     *     It will log the error message.
     * </p>
     * @param request {@link HttpServletRequest} Request
     * @param response {@link HttpServletResponse} Response
     * @param authenticationException {@link AuthenticationException} Authentication exception
     * @throws IOException If an error occurs
     * @throws ServletException If an error occurs
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        LOGGER.error("Unauthorized request: {} {} - {}", method, requestUri, authenticationException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
    }
}