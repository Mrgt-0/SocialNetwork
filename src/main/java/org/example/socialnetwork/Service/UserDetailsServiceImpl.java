package org.example.socialnetwork.Service;

import org.example.socialnetwork.Config.MyUserDetails;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = userService.findByUserName(username);

        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return new MyUserDetails(user);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return user.getRole().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
    }
}
