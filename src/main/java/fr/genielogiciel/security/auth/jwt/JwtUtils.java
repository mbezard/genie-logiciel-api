package fr.genielogiciel.security.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtils {

    public static final String headerAuthorization = "Authorization";
    public static final String headerAuthorizationPrefix = "Bearer ";
    public static final String secretKey = "LAMARCHEDESVERTUEUXESTSEMEEDOBSTACLESQUISONTLESENTREPRISESEGOISTESQUEFAITSANSFINSURGIRLOEUVREDUMALINBENISOITILLHOMMEDEBONNEVOLONTEQUIAUNOMDELACHARITESEFAITLEBERGERDESFAIBLESQUILGUIDEDANSLAVALLEEDOMBREDELAMORTETDESLARMESCARILESTLEGARDIENDESONFREREETLAPROVIDENCEDESENFANTSEGARESJABATTRAIALORSLEBRASDUNETERRIBLECOLEREDUNEVENGEANCEFURIEUSEETEFFRAYANTESURLESHORDESIMPIESQUIPOURCHASSENTETREDUISENTANEANTLESBREBISDEDIEUETTUCONNAITRASPOURQUOIMONNOMESTLETERNELQUANDSURTOISABATTRALAVENGEANCEDUTOUTPUISSANT";



    public static String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .signWith(Keys.hmacShaKeyFor(JwtUtils.secretKey.getBytes(StandardCharsets.UTF_8)))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + (1000 * 60 * 60 * 24 * 7 * 2)))//2 weeks
                .compact();
    }

}
