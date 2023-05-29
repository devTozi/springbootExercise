//package com.coding404.myweb.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import com.coding404.myweb.util.interceptor.MenuHandler;
//import com.coding404.myweb.util.interceptor.UserAuthHandler;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer{
//
//	// 프리핸들러
//	@Bean
//	public UserAuthHandler userAuthHandler() {
//		return new UserAuthHandler();
//	}
//	
//	// 포스트핸들러
//	@Bean
//	public MenuHandler menuHandler() {
//		return new MenuHandler();
//	}
//	
//	// 인터셉터 추가
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		
//		registry.addInterceptor(userAuthHandler())
//				.addPathPatterns("/main")
//				.addPathPatterns("/product/*")
//				.addPathPatterns("/user/*")
//				.excludePathPatterns("/user/login")
//				.excludePathPatterns("/user/join");
//		
//		registry.addInterceptor(menuHandler())
//				.addPathPatterns("/main")
//				.addPathPatterns("/product/*")
//				.addPathPatterns("/user/*");
//				
//	}
//	
//	
//}
