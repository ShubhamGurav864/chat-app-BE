package com.chatapp.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new HandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, 
                                                 ServerHttpResponse response,
                                                 WebSocketHandler wsHandler, 
                                                 Map<String, Object> attributes) throws Exception {
                        
                        // Extract username from query parameter
                        if (request instanceof ServletServerHttpRequest) {
                            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                            String username = servletRequest.getServletRequest().getParameter("username");
                            
                            System.out.println("🔗 Handshake - Username: " + username);
                            
                            if (username != null) {
                                attributes.put("username", username);
                            }
                        }
                        
                        return true;
                    }

                    @Override
                    public void afterHandshake(ServerHttpRequest request, 
                                             ServerHttpResponse response,
                                             WebSocketHandler wsHandler, 
                                             Exception exception) {
                    }
                })
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(ServerHttpRequest request,
                                                    WebSocketHandler wsHandler,
                                                    Map<String, Object> attributes) {
                        
                        String username = (String) attributes.get("username");
                        
                        System.out.println("👤 Creating Principal for: " + username);
                        
                        return () -> username != null ? username : "anonymous";
                    }
                })
                .withSockJS();
    }
}