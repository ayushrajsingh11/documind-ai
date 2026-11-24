package io.nexusai.demo;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;
@Configuration
class SecurityConfig {
 @Bean PasswordEncoder encoder(){return new BCryptPasswordEncoder(12);}
 @Bean UserDetailsService users(JdbcTemplate db){return username->db.query("select username,password_hash from accounts where username=?", (rs,n)->User.withUsername(rs.getString(1)).password(rs.getString(2)).roles("USER").build(),username).stream().findFirst().orElseThrow(()->new UsernameNotFoundException("Invalid credentials"));}
 @Bean SecurityFilterChain security(HttpSecurity http)throws Exception{
  return http.csrf(c->c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
   .authorizeHttpRequests(a->a.requestMatchers("/api/csrf","/api/register","/api/login").permitAll().anyRequest().authenticated())
   .exceptionHandling(e->e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
   .formLogin(f->f.loginProcessingUrl("/api/login").successHandler((q,r,a)->r.setStatus(204)).failureHandler((q,r,e)->r.setStatus(401)))
   .logout(l->l.logoutUrl("/api/logout").logoutSuccessHandler((q,r,a)->r.setStatus(204))).build();
 }
}
