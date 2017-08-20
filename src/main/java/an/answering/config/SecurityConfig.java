package an.answering.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * 安全配置类
 * Created by HP on 2017/8/18.
 */

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  //启用方法安全设置
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String KEY = "zjp";

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); //使用BCrpt加密技术加密密码
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**","/js/**","/fonts/**","/index").permitAll()
        .antMatchers("/h2-console/**").permitAll()  //都可以访问
        .antMatchers("/admins/**").hasRole("ADMIN") //需要相应的角色才能访问
        .and()
        .formLogin()    //基于form的表单登录验证
        .loginPage("/login").failureUrl("/login-error")
        .and()
        .rememberMe().key(KEY)     //启用remember me
        .and()
        .exceptionHandling()
        .accessDeniedPage("/403");  //处理异常，拒绝访问就重定向到403页面
        http.csrf().ignoringAntMatchers("/h2-console/**");  //禁用H2控制台的CSRF防护
        http.headers().frameOptions().sameOrigin(); //允许来自同一来源的H2控制台的请求
    }
}
