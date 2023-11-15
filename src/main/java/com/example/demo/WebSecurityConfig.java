package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.MemberDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig{
	@Bean
	public MemberDetailsService memberDetailsService() {
		return new MemberDetailsService();
	}
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider()
;
		authProvider.setUserDetailsService(memberDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
		}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	http
//	/category/add   /category/save /category/edit/* /category/delete/*	/categories
//	/item/add	/item/save /item/edit/* /item/delete/*	/items
	.authorizeHttpRequests((requests) -> requests
	.requestMatchers("/").permitAll() 
	.requestMatchers("/view/cart").permitAll()
	.requestMatchers("/view/details/{id}").permitAll()
	.requestMatchers("/viewuser{id}").permitAll() 
	.requestMatchers("/about").permitAll()
	.requestMatchers("/cart").hasRole("USER")
	.requestMatchers("/category/add","/category/save","/category/edit/*","/category/delete/*","/categories").hasRole("ADMIN")
	.requestMatchers("/view/user").permitAll() 
	.requestMatchers("/items").hasAnyRole("USER","ADMIN")
	.requestMatchers("/categories").permitAll() 
	.requestMatchers("/item/add","/item/save","/item/edit/*","/item/delete/*").hasRole("ADMIN")
	.requestMatchers("/member/user/add","member/user/save").permitAll()
	.requestMatchers("/member/add","/members","/member/delete/*","/member/save","/member/edit/*").hasRole("ADMIN")
	.requestMatchers("/bootstrap-5.2.3-dist/*/*").permitAll()
	.requestMatchers("/img/*").permitAll()
	.requestMatchers("/css").permitAll()
	.anyRequest().authenticated()
	)
	.formLogin((form) -> form.loginPage("/login").permitAll()
	.defaultSuccessUrl("/", true))
	.logout((logout) -> logout.logoutUrl("/logout").permitAll())
	.exceptionHandling().accessDeniedPage("/403");
			
	
	return http.build();
	}

}
