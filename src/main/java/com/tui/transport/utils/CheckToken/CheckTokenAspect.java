package com.tui.transport.utils.CheckToken;

import com.tui.transport.services.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckTokenAspect {
    private final SecurityService securityService;

    @Around("@annotation(com.tui.transport.utils.CheckToken.CheckToken)")
    public Object checkToken(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return ResponseEntity.status(401).build();
        }
        HttpServletRequest request = attrs.getRequest();
        String token = request.getHeader("Authorization");
        try {
            securityService.validateToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
        return joinPoint.proceed();
    }
}

