package at.ahmacademy.ahmnet.configs;

import java.util.Arrays;
import java.util.Collections;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import at.ahmacademy.ahmnet.filters.CustomAuthenticationFilter;
import at.ahmacademy.ahmnet.filters.CustomAuthorizationFilter;

@Configuration
@EnableWebSecurity()
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  DataSource dataSource;

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
    configuration.setAllowedOrigins(Collections.singletonList("*"));
    configuration.addAllowedMethod(HttpMethod.TRACE);
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "OPTIONS", "PUT"));

    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.cors().and().csrf().disable();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
    http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    http.headers().frameOptions().disable(); // needed for H2 console

    http.logout()
      .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
      .invalidateHttpSession(true)
      .deleteCookies("JSESSIONID");

    http.authorizeRequests()
      //Permit access to the H2 console
      .antMatchers("/h2-console/**").permitAll()
      .and().formLogin()
      .loginProcessingUrl("/login");
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    //Configure roles and passwords via datasource
    auth.jdbcAuthentication().dataSource(dataSource)
      .usersByUsernameQuery("select username, password, enabled from user where username=?")
      .authoritiesByUsernameQuery("select user_username, roles from user_user_role where user_username=?");
  }

  @Bean
  public static PasswordEncoder passwordEncoder() {
    // :TODO: use proper passwordEncoder and do not store passwords in plain text
    return NoOpPasswordEncoder.getInstance();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
