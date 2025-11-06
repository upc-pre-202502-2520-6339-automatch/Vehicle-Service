package com.vehicle.config;
/*
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.jwt.*;
import com.github.benmanes.caffeine.cache.Cache;

public class JwtRevocationValidator implements OAuth2TokenValidator<Jwt> {
    private final StringRedisTemplate redis;
    private final Cache<String, Boolean> cache;

    public JwtRevocationValidator(StringRedisTemplate redis, Cache<String, Boolean> cache) {
        this.redis = redis;
        this.cache = cache;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        String jti = token.getId(); // 'jti'
        if (jti == null || jti.isBlank()) {
            return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token","missing jti",""));
        }
        Boolean cached = cache.getIfPresent(jti);
        if (cached != null && cached) {
            return OAuth2TokenValidatorResult.failure(new OAuth2Error("token_revoked","Token has been revoked",""));
        }
        String key = "revoked:" + jti;
        String val = redis.opsForValue().get(key);
        if (val != null) {
            cache.put(jti, true);
            return OAuth2TokenValidatorResult.failure(new OAuth2Error("token_revoked","Token has been revoked",""));
        }
        return OAuth2TokenValidatorResult.success();
    }
}8*/