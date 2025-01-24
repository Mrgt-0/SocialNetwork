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
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        logger.info("Поиск пользователя {} в репозитории", userName);
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + userName));

        logger.info("Пользователь {} найден", user.getUserName());
        return new MyUserDetails(convertToDTO(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return user.getRole().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        if (user == null)
            return null;

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setBirthdate(user.getBirthdate());
        userDTO.setProfilePicture(user.getProfilePicture());
        userDTO.setRole(user.getRole());
        return userDTO;
    }
}
