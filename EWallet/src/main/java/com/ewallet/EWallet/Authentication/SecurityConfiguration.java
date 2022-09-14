package com.ewallet.EWallet.Authentication;
import com.ewallet.EWallet.Service.WalletUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // Authentication  : Who can Access ?
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());   
    }
    /* Easy to implement & work with in-memory authentication (auth.inMemoryAuthentication().withUser("user1").password("9005").roles("ADM")   )
        but
          here we are  Getting records from Mysql using JPA to get username , password & associated roles assigned to them  */
    
    // Authorization : What to Access ?
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/transaction/getBalance/").hasRole("CUST")
                .antMatchers("/transaction/addBalance/").hasRole("ADM")
                .antMatchers("/**").permitAll()
                .and().formLogin();
        
       /* .and().csrf().disable();  - put APIs is not able to authorize users  , so add this at end to disable ( Cross-Site Request Forgery ) to add
           balance to wallet of a user  */
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new WalletUserDetailsService();
    }
    
    // Storing Password in encrypted form : Example : ADM ----->  $2a$10$/jHmFWEDD4mT2R2.nt84Zugwcp.vR8R0IuzeGEOvZu00YxFQqypIW 
    @Bean                                                       
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(getPasswordEncoder());
        return authenticationProvider;
    }

}
