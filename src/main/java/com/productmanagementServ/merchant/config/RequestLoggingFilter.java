package com.productmanagementServ.merchant.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final int MAX_BODY_LOG = 2000;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        // Skip body logging for multipart (image uploads — too large)
        boolean isMultipart = request.getContentType() != null
                && request.getContentType().startsWith("multipart/");

        ContentCachingRequestWrapper wrappedRequest =
                isMultipart ? null : new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long start = System.currentTimeMillis();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String fullUri = query != null ? uri + "?" + query : uri;

        try {
            chain.doFilter(wrappedRequest != null ? wrappedRequest : request, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - start;
            int status = wrappedResponse.getStatus();

            String reqBody = "";
            if (wrappedRequest != null) {
                byte[] reqBytes = wrappedRequest.getContentAsByteArray();
                if (reqBytes.length > 0) {
                    reqBody = truncate(new String(reqBytes, StandardCharsets.UTF_8));
                }
            }

            String resBody = "";
            byte[] resBytes = wrappedResponse.getContentAsByteArray();
            if (resBytes.length > 0) {
                resBody = truncate(new String(resBytes, StandardCharsets.UTF_8));
            }

            if (reqBody.isEmpty() && resBody.isEmpty()) {
                log.info("{} {} -> {} ({}ms)", method, fullUri, status, duration);
            } else if (reqBody.isEmpty()) {
                log.info("{} {} -> {} ({}ms)\n  << {}", method, fullUri, status, duration, resBody);
            } else if (resBody.isEmpty()) {
                log.info("{} {} -> {} ({}ms)\n  >> {}", method, fullUri, status, duration, reqBody);
            } else {
                log.info("{} {} -> {} ({}ms)\n  >> {}\n  << {}", method, fullUri, status, duration, reqBody, resBody);
            }

            // Must copy body back so the actual response is still sent to client
            wrappedResponse.copyBodyToResponse();
        }
    }

    private String truncate(String body) {
        String trimmed = body.trim();
        if (trimmed.length() > MAX_BODY_LOG) {
            return trimmed.substring(0, MAX_BODY_LOG) + "... [truncated]";
        }
        return trimmed;
    }
}