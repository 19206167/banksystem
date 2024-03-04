package com.nus.team4.util;

import com.alibaba.fastjson.JSON;
import com.nus.team4.constant.AuthorityConstant;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.*;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

/**
 * JWT工具类，生成jwt, 解析token
 */
@Slf4j
public class JwtUtil {

    public static String createJWT(String username) throws Exception {
        return createJWT(username, 0);
    }
    /*
     * @description: 使用Hs256算法，密匙使用笃定密钥
     * @date: 2024/2/25 17:28
     * @param: [secretKey, ttlMills, claims]
     * @return: java.lang.String
     **/
    public static String createJWT(String username, long ttlMills) throws Exception {
//        指定签名的时候使用的签名算法
        //TODO: 算法选择需要更改，这里可以作为presentation的点
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;

        if (ttlMills <= 0) {
            ttlMills = AuthorityConstant.DEFAULT_EXPIRE_MINUTE;
        }
//        JWT有效时间
        LocalDateTime dateTime = LocalDateTime.now().plusMinutes(ttlMills);
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.systemDefault());
        Date exp = Date.from(zonedDateTime.toInstant());

//        设置jwtbody
        JwtBuilder builder = Jwts.builder()

                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，
                // 这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .claim(AuthorityConstant.JWT_USER_INFO_KEY, JSON.toJSON(username))
//                  设置签名使用的算法和签名使用的密钥
                .signWith(signatureAlgorithm, getPrivateKey())
//                设置过期时间
                .setExpiration(exp);
        return builder.compact();
    }

    //    从JWT token中解析LoginUserInfo对象
    public static String parseUserInfoFromToken(String token) throws Exception {
        log.info("parse token");
//        首先判断是否为空
        if (token == null) {
            log.error("token is null");
            return null;
        }

        Jws<Claims> claimsJws = parseToken(token, getPublicKey());
        Claims body = claimsJws.getBody();

//        如果token已经过期了，返回null
        log.info(String.valueOf(Date.from(body.getExpiration().toInstant())));
        if (body.getExpiration().before(Calendar.getInstance().getTime())) {
            return null;
        }

//        返回Token中保存的用户信息, body中的数据用=匹配，必须转化为使用: 匹配，才能正确被JSON转成LoginUserInfo格式
        String corrected = body.get(AuthorityConstant.JWT_USER_INFO_KEY).toString();
//                .replace('=', ':').replaceAll("([a-zA-Z0-9]+)", "\"$1\"");
        log.info("corrected: [{}]", corrected);
        return corrected;
    }

    //    通过公钥解析JWT Token
    private static Jws<Claims> parseToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    //    根据本地存储的私钥，获取到privateKey对象
//    使用方法查询相关文档
    private static PrivateKey getPrivateKey() throws Exception{
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                Base64.getDecoder().decode(AuthorityConstant.PRIVATE_KEY)
        );
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }

    //    根据本地存储的公钥，获取到public key对象
    private static PublicKey getPublicKey() throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
                Base64.getDecoder().decode(AuthorityConstant.PUBLIC_KEY)
        );
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
}
