package com.persona.rate.limiter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitingFilter implements Filter {

    private final Map<String, AtomicInteger> requestCount = new ConcurrentHashMap<>();

    private static final int MAX_REQUEST = 2; // límite de peticiones
    private static final long TIME_WINDOW = 60000; // 1 minuto

    private volatile long windowStart = System.currentTimeMillis();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String clientIP = getClientIP(httpRequest);
        long currentTime = System.currentTimeMillis();

        // 🔥 Ignorar preflight (CORS)
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        // 🔄 Reiniciar ventana de tiempo (thread-safe)
        if (currentTime - windowStart > TIME_WINDOW) {
            synchronized (this) {
                if (currentTime - windowStart > TIME_WINDOW) {
                    windowStart = currentTime;
                    requestCount.clear();
                }
            }
        }

        requestCount.putIfAbsent(clientIP, new AtomicInteger(0));
        int currentCount = requestCount.get(clientIP).incrementAndGet();

        // 🔍 DEBUG
        System.out.println("IP: " + clientIP + " - Requests: " + currentCount);

        // 🚫 Límite excedido
        if (currentCount >= MAX_REQUEST) {

            System.out.println("🔥 BLOQUEADO - IP: " + clientIP);

            httpResponse.setStatus(429); // ✔ sin usar constante problemática
            httpResponse.setContentType("application/json;charset=UTF-8");

            String json = "{ \"error\": \"Demasiadas solicitudes\", \"mensaje\": \"Intenta nuevamente en 1 minuto\" }";

            httpResponse.getWriter().write(json);
            httpResponse.getWriter().flush();
            httpResponse.flushBuffer(); // 🔥 asegura envío inmediato

            return;
        }

        chain.doFilter(request, response);
    }

    // 🔥 Obtener IP real (soporta proxies)
    private String getClientIP(HttpServletRequest request) {

        String xfHeader = request.getHeader("X-Forwarded-For");

        if (xfHeader == null || xfHeader.isEmpty()) {
            return request.getRemoteAddr();
        }

        return xfHeader.split(",")[0];
    }
}

/*
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
*/
/*
 * ERROR 429: enviado demasiadas solicitudes en un período de tiempo determinado.
 * 
 * AtomicInteger: Se usa para aplicaciones como contadores, Se utiliza en entornos donde se trabaja con varios hilos al mismo tiempo, deriva de number
 * 
 *  ConcurrentHashMap: La clase del marco de colecciones de Java proporciona un mapa seguro para subprocesos.
 *  
 *  FilterChain es una API de Java que permite invocar filtros en una cadena. Los filtros pueden modificar una respuesta o transformar una solicitud
 */
/*
@Component
public class RateLimitingFilter implements Filter{
	
	private final Map<String, AtomicInteger> requestCount = new ConcurrentHashMap<>();
	private final int MAX_REQUEST = 115;
	private final long TIME_WINDOW = 60000;
	private long windowStart = System.currentTimeMillis();
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,ServletException{
		String clientIP = ((HttpServletRequest) request).getRemoteAddr();
		long currentTime = System.currentTimeMillis();
		if(currentTime - windowStart > TIME_WINDOW) {
			windowStart = currentTime;
			requestCount.clear();
		}
		
		requestCount.putIfAbsent(clientIP, new AtomicInteger(0));
		int currentCount = requestCount.get(clientIP).incrementAndGet();
		
		if(currentCount > MAX_REQUEST) {
			((HttpServletResponse) response).setStatus(429);
			response.getWriter().write("DEMASIADAS SOLICITUDES, INTENTE DE NUEVO EN 1 MINUTO!!!!");
			return;
		}
		
		chain.doFilter(request, response);
	
	}	
	
}*/