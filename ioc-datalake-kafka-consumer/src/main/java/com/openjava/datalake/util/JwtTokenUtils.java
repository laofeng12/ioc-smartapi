package com.openjava.datalake.util;

import io.jsonwebtoken.*;
import org.ljdp.component.exception.APIException;
import org.springframework.http.HttpStatus;
import sun.misc.BASE64Encoder;

import java.util.Date;

/**
 * @author Jiahai
 */
public class JwtTokenUtils {
    /**
     * 过期时间（1小时）
     */
    private static final Long EXPIRE_TIME = 30 * 60 * 1000L;

    /**
     * 加解密算法
     */
    private static final SignatureAlgorithm JWT_ALG = SignatureAlgorithm.HS256;

    /**
     * 生成token
     *
     * @param key
     * @param secret
     * @return
     */
    public static String generateToken(String key, String secret) {
        BASE64Encoder encoder = new BASE64Encoder();
        String base64Str = encoder.encode(secret.getBytes());
        // 生成token
        String token = Jwts.builder()
                .setIssuer(key)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(JWT_ALG, base64Str)
                .compact();
        return token;
    }

    //解密token
    public static String decodeTokenBase64(String token, String secret) throws APIException {
        try {
            BASE64Encoder encoder = new BASE64Encoder();
            secret = encoder.encode(secret.getBytes());
            //解析token，claims是token包含的有效信息
            JwtParser jwtParser = Jwts.parser().setSigningKey(secret);
            Claims claims = Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token.replace("Bearer ", "")).getBody();
            return claims.getIssuer();
        } catch (SignatureException e){
            e.printStackTrace();
            System.out.println("密钥错误");
        } catch (ExpiredJwtException eje) {
            eje.printStackTrace();
            Claims claims = eje.getClaims();
            String key = claims.getIssuer();
            System.out.println(key);
            throw new APIException(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), "token过期");
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("token解析异常");
        }
        return null;
    }

}
