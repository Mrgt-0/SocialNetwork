package org.example.socialnetwork.Service;

import org.example.socialnetwork.Config.MyUserDetails;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Ищем пользователя по имени: {}", username);
        Optional<User> user=userRepository.findByUserName(username);
        if(user.isEmpty())
            logger.error("Пользователя с именем: {} не существует.", username);
        return user.map(MyUserDetails::new)
                .orElseThrow(()->new UsernameNotFoundException(username+"Пользователь с таким именем не найден."));
    }
}
