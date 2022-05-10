package com.smart.smartcontact.config;

import com.smart.smartcontact.Repository.UserRepository;
import com.smart.smartcontact.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserServiceDetailsImp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.getUserByUserEmail(username);

        if(user==null){
            throw new UsernameNotFoundException("user is not found");
        }
        CustomUserDetails customUserDetails=new CustomUserDetails();
        customUserDetails.setUser(user);
        return customUserDetails;
    }
    
}
