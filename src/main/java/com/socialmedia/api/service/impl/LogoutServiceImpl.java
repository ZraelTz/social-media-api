package com.socialmedia.api.service.impl;

import com.socialmedia.api.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        if (Objects.isNull(authentication)) {
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith("Bearer ")) {
            return;
        }

        jwtToken = authHeader.substring(7);
        if (StringUtils.isNotBlank(jwtToken)) {
            SecurityContextHolder.clearContext();
        }
    }


}
