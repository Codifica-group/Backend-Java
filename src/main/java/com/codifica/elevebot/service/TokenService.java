package com.codifica.elevebot.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class TokenService {
    private static final String SECRET_KEY = "Eleve";

    public static String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuer("elevebot")
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public static DecodedJWT validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("elevebot")
                .build();
        return verifier.verify(token);
    }
}
