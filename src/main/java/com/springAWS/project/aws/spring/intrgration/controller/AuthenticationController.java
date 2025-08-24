package com.springAWS.project.aws.spring.intrgration.controller;

import com.springAWS.project.aws.spring.intrgration.security.JWTUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final JWTUtil jwtUtil;

    public AuthenticationController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public String login(@RequestParam String userName, @RequestParam String password) {
        // 🔹 Replace this with real authentication (UserDetailsService, DB, etc.)
        if ("user".equals(userName) && "password".equals(password)) {
            return jwtUtil.generateToken(userName);
        }
        throw new RuntimeException("Invalid credentials");
    }
}
