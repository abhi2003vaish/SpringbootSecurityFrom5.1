package com.codingshuttle.SecurityApp.SecurityApplication.filters;

import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;
import com.codingshuttle.SecurityApp.SecurityApplication.services.JwtService;
import com.codingshuttle.SecurityApp.SecurityApplication.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            //String the value of the Header with the key Authorization
            final String requestTokenHeader=request.getHeader("Authorization");
            //in company standard the Jwt token are stored in this form "Bearer ahdhgdhbjfcnjnkl.dxjnhxk.dxhbxudjx"
            //if the stored is null and not start with "Bearer " then skips the filter and goes to the next filter in the chain
            if(requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")){
                filterChain.doFilter(request,response);
                return;
            }
            //here we are just taking the token in the token variable bec we need jwt token to verify
            //String token =  requestTokenHeader.replace("Bearer ","");
            String token = requestTokenHeader.split("Bearer ")[1];
            //now verify the jwt token and we get the userId so we need to store it too
            Long userId=jwtService.getUserIdFromToken(token);

            if(userId!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                User user=userService.getUserById(userId);
                UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user,
                        null, null);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request,response);

        }catch (Exception ex){
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }






    }
}
