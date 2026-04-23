package com.chatapp.chat.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class RedisDebugController {
    
    @Value("${REDIS_URL:not-set}")
    private String redisUrl;
    
    @GetMapping("/debug/redis")
    public String debugRedis() {
        StringBuilder debug = new StringBuilder();
        
        debug.append("=== Redis Configuration Debug ===\n\n");
        
        // Check if REDIS_URL is set
        if ("not-set".equals(redisUrl)) {
            debug.append("❌ REDIS_URL environment variable is NOT SET!\n");
            debug.append("Please add REDIS_URL to your Render environment variables.\n");
            return debug.toString();
        }
        
        debug.append("✅ REDIS_URL is set\n\n");
        
        try {
            URI uri = URI.create(redisUrl);
            
            debug.append("Parsed URL Details:\n");
            debug.append("- Scheme: ").append(uri.getScheme()).append("\n");
            debug.append("- Host: ").append(uri.getHost()).append("\n");
            debug.append("- Port: ").append(uri.getPort()).append("\n");
            debug.append("- Has UserInfo: ").append(uri.getUserInfo() != null).append("\n");
            
            if (uri.getUserInfo() != null) {
                String[] parts = uri.getUserInfo().split(":", 2);
                debug.append("- Username: ").append(parts[0]).append("\n");
                debug.append("- Password: ").append(parts.length > 1 ? "***SET***" : "NOT SET").append("\n");
            }
            
            debug.append("\n");
            
            // Check scheme
            if (!"redis".equalsIgnoreCase(uri.getScheme()) && !"rediss".equalsIgnoreCase(uri.getScheme())) {
                debug.append("⚠️  WARNING: Scheme should be 'redis://' or 'rediss://', got: ").append(uri.getScheme()).append("\n");
            } else if ("redis".equalsIgnoreCase(uri.getScheme())) {
                debug.append("⚠️  WARNING: Using 'redis://' without SSL. Most cloud providers require 'rediss://'\n");
            } else {
                debug.append("✅ Using 'rediss://' with SSL\n");
            }
            
            // Check port
            if (uri.getPort() < 0) {
                debug.append("⚠️  WARNING: No port specified, will default to 6379\n");
            } else {
                debug.append("✅ Port: ").append(uri.getPort()).append("\n");
            }
            
            // Check host
            if (uri.getHost() == null || uri.getHost().isEmpty()) {
                debug.append("❌ ERROR: No host specified!\n");
            } else {
                debug.append("✅ Host: ").append(uri.getHost()).append("\n");
            }
            
            // Check password
            if (uri.getUserInfo() == null) {
                debug.append("⚠️  WARNING: No authentication info found!\n");
            } else {
                String[] parts = uri.getUserInfo().split(":", 2);
                if (parts.length < 2) {
                    debug.append("⚠️  WARNING: Password might not be set correctly\n");
                } else {
                    debug.append("✅ Password appears to be set\n");
                }
            }
            
            debug.append("\nExpected format:\n");
            debug.append("rediss://default:YOUR_PASSWORD@your-host.upstash.io:6379\n");
            debug.append("\nYour format (sanitized):\n");
            debug.append(redisUrl.replaceAll(":[^:@]+@", ":***@")).append("\n");
            
        } catch (Exception e) {
            debug.append("❌ ERROR parsing REDIS_URL: ").append(e.getMessage()).append("\n");
            debug.append("\nStack trace:\n");
            for (StackTraceElement element : e.getStackTrace()) {
                debug.append("  ").append(element.toString()).append("\n");
            }
        }
        
        return debug.toString();
    }
}