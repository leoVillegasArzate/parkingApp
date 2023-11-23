package com.parking.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class SessionInterceptor  implements HandlerInterceptor {
	
	  @Override
	    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	            throws Exception {
	        // Aquí realizas la lógica de validación de la sesión
	        if (request.getSession().getAttribute("user") == null) {
	            response.sendRedirect(request.getContextPath() + "/login1");
	            return false; // La solicitud no se manejará más
	        }
	        return true; // Continuar con el manejo normal de la solicitud
	    }

}
