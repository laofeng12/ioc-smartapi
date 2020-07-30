package com.openjava.datalake.util.secret;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import sun.misc.BASE64Encoder;

import java.util.Date;

public class JwtTokenUtil {
	
	private static final Long   EXP	= 30*60*1000l;//1小时过期
	private static final SignatureAlgorithm JWT_ALG = SignatureAlgorithm.HS256;//加解密算法

	//生成token
	public static String generateToken(String key,String secret) {
		//base64编码，加密、解密双方须一致，且使用不同的base64类可能导致解析失败
		BASE64Encoder encoder = new BASE64Encoder();
		secret = encoder.encode(secret.getBytes());
		//生成token
		String token = Jwts.builder()
				.setIssuer(key)
                .setExpiration(new Date(System.currentTimeMillis()+EXP))
                .signWith(JWT_ALG, secret)
                .compact();
		return token;
	}

	//解密token
	public static void decodeToken(String token,String secret){
		try {
//			BASE64Encoder encoder = new BASE64Encoder();
//			secret = encoder.encode(secret.getBytes());
			//解析token，claims是token包含的有效信息
			Claims claims = Jwts.parser().setSigningKey(secret)
					.parseClaimsJws(token.replace("Bearer ", "")).getBody();
//			System.out.println(JsonUtils.objectToJson(claims));
		}catch (SignatureException e){
			e.printStackTrace();
			System.out.println("密钥错误");
		}catch (Exception e){
			e.printStackTrace();
			System.out.println("token解析异常");
		}
	}

	public static void main(String[] args) {
		String key = "zhongliang";
		String secret = "liang1217";

		String token = generateToken(key,secret);
		System.out.println(token);

		decodeToken("Bearer "+token,secret);
	}


}
