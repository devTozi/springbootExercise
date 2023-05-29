package com.coding404.myweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.coding404.myweb.command.UserVO;
import com.coding404.myweb.user.service.UserMapper;

@Service
public class LoginIdPwValidator implements UserDetailsService {
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder(String pw) {
        return new BCryptPasswordEncoder();
    }
    
    @Autowired
    private UserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String insertedId) throws UsernameNotFoundException {
        UserVO user = mapper.getUserInfo(insertedId);
        
        if (user.getId() == null) {
            return null;
        }
        // 직접 인코딩(db에 값이 암호화되지 않은 값이 있어서 테스트할때만 직접 처리한거임)
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        String pw = passwordEncoder.encode(user.getPw());
        String roles = user.getRoles() == null ? "none" : user.getRoles(); //"USER"

        return User.builder()
                .username(insertedId)
                .password(pw)
                .roles(roles)
                .build();
    }
}
