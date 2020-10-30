package Auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class Token {
    public static String generateToken(String id, String name){
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        return Jwts.builder()
                .setSubject(name)
                .setId(id)
                .setIssuedAt(now)
                .signWith(key)
                .compact();
    }
}
