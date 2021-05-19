package bartos.lukasz.bookingservice.infrastructure.security;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 432_000_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    public static final String ISSUER = "Booking Service";
    public static final String GET_ADMINISTRATION = "Booking Service";
    public static final String ROLE = "role";
    public static final String JWT_SECRET = "secretVerySecret";
}
