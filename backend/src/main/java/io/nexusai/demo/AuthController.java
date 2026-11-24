package io.nexusai.demo;
import java.util.Map;
import java.security.Principal;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
@RestController @RequestMapping("/api")
class AuthController {
 private final JdbcTemplate db; private final PasswordEncoder encoder;
 AuthController(JdbcTemplate db,PasswordEncoder encoder){this.db=db;this.encoder=encoder;}
 record Credentials(String username,String password){}
 @GetMapping("/csrf") Map<String,String> csrf(CsrfToken token){return Map.of("token",token.getToken(),"header",token.getHeaderName());}
 @GetMapping("/me") Map<String,String> me(Principal p){return Map.of("username",p.getName());}
 @PostMapping("/register") ResponseEntity<Void> register(@RequestBody Credentials c){
 if(c.username()==null||!c.username().matches("[a-zA-Z0-9_.-]{3,80}")||c.password()==null||c.password().length()<12||c.password().getBytes(java.nio.charset.StandardCharsets.UTF_8).length>72)return ResponseEntity.badRequest().build();
 int inserted=db.update("insert into accounts(username,password_hash) values (?,?) on conflict do nothing",c.username(),encoder.encode(c.password()));
 return ResponseEntity.status(inserted==1?201:409).build();
 }
}
