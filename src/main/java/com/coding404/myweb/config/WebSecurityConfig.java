package com.coding404.myweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    LoginIdPwValidator loginIdPwValidator;
	
	// 기본적인 URI 제어 처리
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
			// LoadBalancer 처리 
			.antMatchers("/chk").permitAll()
			// 관리자 화면 처리
            .antMatchers("/manage").hasAuthority("ROLE_ADMIN")
            // 모든 URI로 접근할 때 인증 필요
         	.anyRequest().authenticated() 
			.and()
				// 폼 로그인 방식 사용
				.formLogin() 
				// 커스텀 로그인 페이지 사용 시 설정 
//				.loginPage("/user/login") // 로그인 화면 경로 
//                .loginProcessingUrl("/loginProc") // 폼의 액션값(URI)
//                .usernameParameter("id")
//                .passwordParameter("pw")
                // 로그인 성공 시 이동할 URI
				.defaultSuccessUrl("/product/productList", true) 
				.permitAll()
			.and()
				.logout(); // 로그아웃 처리
//				.logoutRequestMatcher(new AntPathRequestMatcher("/logoutProc")); // 커스텀 로그인 페이지 사용 시
		
	}
	
	// 정적 자원들 예외 처리
	@Override
    public void configure(WebSecurity web) {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
	
	// 1. LoginIdPwValidator를 Autowired를 통해 주입
	// 2. 유저가 id와 pw를 입력한 후 form이 발송되면 LoginIdPwValidator 쪽으로 id가 넘어가 비교 진행
	@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginIdPwValidator);
    }

}
