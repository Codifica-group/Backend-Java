package com.codifica.elevebot;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.codifica.elevebot.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ElevebotApplicationTests {

	@Test
	void shouldValidateValidToken() {
		String username = "fernanda";
		Algorithm algorithm = Algorithm.HMAC256("Eleve");

		String token = JWT.create()
				.withSubject(username)
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60))
				.sign(algorithm);

		DecodedJWT jwt = TokenService.validateToken(token);

		assertEquals(username, jwt.getSubject());
		assertTrue(jwt.getExpiresAt().after(new Date()));
	}

	@Test
	void shouldThrowExceptionForInvalidToken() {
		String invalidToken = "Não é um token válido";

		assertThrows(Exception.class, () -> {
			TokenService.validateToken(invalidToken);
		});
	}

}
