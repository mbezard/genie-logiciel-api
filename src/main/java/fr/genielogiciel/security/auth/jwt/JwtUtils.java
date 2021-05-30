package fr.genielogiciel.security.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.function.Function;

public class JwtUtils {

    public static final String headerAuthorization = "Authorization";
    public static final String headerAuthorizationPrefix = "Bearer ";
    public static final String secretKey = "LAMARCHEDESVERTUEUXESTSEMEEDOBSTACLESQUISONTLESENTREPRISESEGOISTESQUEFAITSANSFINSURGIRLOEUVREDUMALINBENISOITILLHOMMEDEBONNEVOLONTEQUIAUNOMDELACHARITESEFAITLEBERGERDESFAIBLESQUILGUIDEDANSLAVALLEEDOMBREDELAMORTETDESLARMESCARILESTLEGARDIENDESONFREREETLAPROVIDENCEDESENFANTSEGARESJABATTRAIALORSLEBRASDUNETERRIBLECOLEREDUNEVENGEANCEFURIEUSEETEFFRAYANTESURLESHORDESIMPIESQUIPOURCHASSENTETREDUISENTANEANTLESBREBISDEDIEUETTUCONNAITRASPOURQUOIMONNOMESTLETERNELQUANDSURTOISABATTRALAVENGEANCEDUTOUTPUISSANT";


    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }



    //trouvé en ligne

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

}
