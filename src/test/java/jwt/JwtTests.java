package jwt;

import io.jsonwebtoken.*;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

/**
 * @Author: Lishenglong
 * @Date: 2022/8/8 12:22
 */
public class JwtTests {

    private static long tokenExpiration = 1000 * 60 * 60 * 24; //一天
    private static String tokenSignKey = "lishenglong";

    @Test
    public void testCreatedToken() {
        JwtBuilder jwtBuilder = Jwts.builder();
        //头、载荷、签名哈希
        //头有两部分组成 k,v
        String jwtToken = jwtBuilder
                //头
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                //载荷：自定义信息
                .claim("nickname", "lsl")
                .claim("avatar", "1.jpg")
                .claim("role", "admin")
                //载荷：默认信息
                .setSubject("finance-user")//令牌主题
                .setAudience("lsl")//接收方
                .setIssuer("lsl")//发送方
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))//过期时间点
                .setNotBefore(new Date(System.currentTimeMillis() + 1000 * 20)) //生效时间20秒之后
                .setId(UUID.randomUUID().toString()) //防止重放攻击 唯一性
                //签名哈希
                .signWith(SignatureAlgorithm.HS256,tokenSignKey)//生成签名
                //组装jwt字符串
                .compact();

        System.out.println(jwtToken);

    }

    @Test
    public void testGetUserInfo(){
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuaWNrbmFtZSI6IkhlbGVuIiwiYXZhdGFyIjoiMS5qcGciLCJyb2xlIjoiYWRtaW4iLCJzdWIiOiJmaW5hbmNlLXVzZXIiLCJhdWQiOiJsc2wiLCJpc3MiOiJsc2wiLCJleHAiOjE2NjAwMjA0OTgsIm5iZiI6MTY1OTkzNDExOCwianRpIjoiNTYxMDVhYjYtYjdiNi00Mzg2LWJkNmEtMTY5ZmZmMTU5N2VjIn0.VsuyWayDMdfb747_Km6s9riMY3PDR-LPAShzNujkXyE";
        JwtParser jwtParser = Jwts.parser();
        //使用相同的签名才能解析
        Jws<Claims> claimsJws = jwtParser.setSigningKey(tokenSignKey).parseClaimsJws(token);

        Claims claims = claimsJws.getBody();
        String nickname = (String) claims.get("nickname");
        String avatar = (String) claims.get("avatar");
        String role = (String) claims.get("role");
        System.out.println(nickname);
        System.out.println(avatar);
        System.out.println(role);

        String id = claims.getId();
        System.out.println(id);


    }
}
