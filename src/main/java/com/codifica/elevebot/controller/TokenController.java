package com.codifica.elevebot.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.codifica.elevebot.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class TokenController {

    @PostMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String tokenHeader) {
        try {
            String token = tokenHeader.replace("Bearer ", "");

            DecodedJWT jwt = TokenService.validateToken(token);
            String username = jwt.getSubject();

            return ResponseEntity.ok("Token válido para: " + username);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token inválido ou expirado");
        }
    }
}
