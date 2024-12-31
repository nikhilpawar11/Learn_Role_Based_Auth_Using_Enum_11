package com.nikhil.learn.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtHelper jwtHelper;
	
	@Autowired
	private UserDetailsService userDetailService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String requestHeader = request.getHeader("Authorization");
		
		String username = null;
        String token = null;
        
        if(requestHeader != null && requestHeader.startsWith("Bearer")) {
        	
        	//looking good
            token = requestHeader.substring(7);
            
            try {
            	username = jwtHelper.getUsernameFromToken(token);
            	
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (ExpiredJwtException e) {
                e.printStackTrace();
            } catch (MalformedJwtException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();

            }
        	
        }
        
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        	
        	// fetch user detail from user-name
        	UserDetails userDetails = userDetailService.loadUserByUsername(username);
        	Boolean validateToken = jwtHelper.validateToken(token, userDetails);
        	
        	if(validateToken) {
        		
        		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        		SecurityContextHolder.getContext().setAuthentication(authentication);
        	}
        	
        }
        
        filterChain.doFilter(request, response);
		
	}

}
