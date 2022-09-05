package com.login.auth.config;


import com.login.auth.security.AuthorizationFilter;
import com.login.auth.service.token.ITokenService;
import com.login.auth.service.user.IUserService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class Security extends WebSecurityConfigurerAdapter {
    private final IUserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ITokenService tokenService;

    @Autowired
    public Security(IUserService userService, BCryptPasswordEncoder bCryptPasswordEncoder,
                    ITokenService tokenService) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenService = tokenService;
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().

                authorizeRequests().
                antMatchers(HttpMethod.POST,"/users/signup")
                .permitAll().
                antMatchers(HttpMethod.POST,"/authentication").
                permitAll().
                antMatchers(HttpMethod.POST,"/authentication/verify").
                permitAll().
                antMatchers("/authentication/reset")
                .permitAll().
                antMatchers(HttpMethod.DELETE,"users/delete/*")
                .hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.GET,"authentication").
                hasAnyRole("ADMIN")
                .anyRequest().
                authenticated()
                .and()//.addFilter(authenticationFilter())
                .addFilterBefore(new AuthorizationFilter(authenticationManager(), userService, tokenService),
                        UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

    /*public AuthenticationFilter authenticationFilter() throws Exception {
        AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
        filter.setFilterProcessesUrl("/users/login");
        return filter;
    }*/
}
