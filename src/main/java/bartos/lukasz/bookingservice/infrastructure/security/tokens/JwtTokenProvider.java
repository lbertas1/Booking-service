package bartos.lukasz.bookingservice.infrastructure.security.tokens;

import bartos.lukasz.bookingservice.infrastructure.security.dto.MyUserPrincipal;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static bartos.lukasz.bookingservice.infrastructure.security.SecurityConstant.*;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
public class JwtTokenProvider {

    public String generateJwtToken(MyUserPrincipal userPrincipal) {
        return JWT
                .create()
                .withIssuer(ISSUER)
                .withAudience(GET_ADMINISTRATION)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withSubject(userPrincipal.getUsername())
                .withClaim(ROLE, getClaimFromUser(userPrincipal))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(JWT_SECRET.getBytes()));
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        return Stream.of(getClaimFromToken(token)).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public String getSubject(String token) {
        return getJWTVerifier().verify(token).getSubject();
    }

    public boolean isTokenValid(String username, String token) {
        JWTVerifier verifier = getJWTVerifier();
        return !username.isBlank() && !isTokenExpired(verifier, token);
    }

    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    private String getClaimFromToken(String token) {
        return getJWTVerifier().verify(token).getClaim(ROLE).asString();
    }

    private String getClaimFromUser(MyUserPrincipal userPrincipal) {
        return userPrincipal.getAuthorities().stream().findFirst().orElseThrow(() -> new JWTVerificationException("Role not found")).getAuthority();
    }

    private JWTVerifier getJWTVerifier() {
        try {
            Algorithm algorithm = HMAC512(JWT_SECRET);
            return JWT.require(algorithm).withIssuer(ISSUER).build();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token cannot be verified");
        }
    }
}
