package com.practice.project02.user;

import com.practice.project02.student.Student;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public List<User> findAll(Integer page, Integer count){
        if (page < 0) {
            page = 0;
        }
        if (count > 500) {
            count = 500;
        }
        return userRepository.findAll(PageRequest.of(page, count)).getContent();
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User or passowrd incorrect.");
        }
        return new org.springframework.security.core.userdetails.User(user.get().getUsername(),
                user.get().getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList(user.get().getRoles()));
    }
}
