package org.example.youtube.util;


import io.jsonwebtoken.*;
import org.example.youtube.dto.JwtDTO;
import org.example.youtube.enums.ProfileRole;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

public class JWTUtil {
    private static final int tokenLiveTime = 1000 * 3600 * 24;
    private static final String secretKey = "ecslle23esl,dsl2,ed,esdllsldlcldlcdllefwefwfwefwfl;w;fweflfwlfwfewfewf";

    public static String encode(Integer profileId, ProfileRole role) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.issuedAt(new Date());

        SignatureAlgorithm sa = SignatureAlgorithm.HS512;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());

        jwtBuilder.signWith(secretKeySpec);

        jwtBuilder.claim("id", profileId);
        jwtBuilder.claim("role", role);

        jwtBuilder.expiration(new Date(System.currentTimeMillis() + (tokenLiveTime)));
        jwtBuilder.issuer("KunUzTest");
        return jwtBuilder.compact();
    }

    public static JwtDTO decode(String token){
        SignatureAlgorithm sa = SignatureAlgorithm.HS512;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(secretKeySpec)
                .build();

        Jws<Claims> jws = jwtParser.parseSignedClaims(token);
        Claims claims = jws.getPayload();

        Integer id = (Integer) claims.get("id");
        String role = (String) claims.get("role");
        if (role != null) {
            ProfileRole profileRole = ProfileRole.valueOf(role);
            return new JwtDTO(id, profileRole);
        }
        return new JwtDTO(id);
    }


}
