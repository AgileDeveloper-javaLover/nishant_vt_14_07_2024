package com.test.auth.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.auth.enums.LogicEnum;
import com.test.auth.enums.PermissionsEnum;
import com.test.auth.jwtservices.JwtTokenUtil;
import com.test.auth.jwtservices.JwtUserDetailsService;
import com.test.auth.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class ValidateImpl {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository usersRepository;

    @Around("@annotation(com.test.auth.aop.Validate)")
    public Object authCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = (HttpServletRequest) args[0];
        final String requestTokenHeader = request.getHeader("Authorization");

        if (requestTokenHeader == null) {
            throw new SecurityException("JWT token must be present!!");
        }

        String jwtToken = requestTokenHeader.substring(7);
        String userEmail = "";
        List<String> usersPermission=null;
        try {
            Jws<Claims> claimsJws = jwtTokenUtil.getClaims(jwtToken);
            Claims bodyClaims = claimsJws != null ? claimsJws.getBody() : null;
            userEmail = bodyClaims.get("preferred_username", String.class);
            usersPermission = bodyClaims.get("permissions", List.class);
        } catch (Exception e) {
            log.error("error message in Validate {}", e.getMessage());
            throw new SecurityException("Token is invalid!!");
        }

        /*************** Uncomment below code if we also want to authenticate the user *************/
        // Users usersEntity = usersRepository.findByEmail(userEmail).orElse(null);
        // if (usersEntity == null) {
        //     throw new SecurityException("User not found with this token!!");
        // }

        /************* checking roles and permission ***************/
        Method method = signature.getMethod();

        Validate myAnnotation = method.getAnnotation(Validate.class);
        PermissionsEnum[] permissions=myAnnotation.permissions();
        LogicEnum logicType=myAnnotation.type();
        // log.info("logic type=={}",logicType.toString());
        // log.info("permissions==");
        
        if(permissions!=null){

            if(usersPermission==null)throw new SecurityException("User not allowed!!");

            for(PermissionsEnum permissionsEnum:permissions){
                // log.info(permissionsEnum.toString());
                if(logicType.equals(LogicEnum.Any) && usersPermission.contains(permissionsEnum.toString()))break;

                if(logicType.equals(LogicEnum.All) && !usersPermission.contains(permissionsEnum.toString()))throw new SecurityException("User not allowed!!");
            }
        }

        return joinPoint.proceed();

    }
}
