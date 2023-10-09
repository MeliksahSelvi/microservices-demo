package com.melik.kafka.streams.service.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Author mselvi
 * @Created 06.10.2023
 */

@Service
public class KafkaStreamsUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return KafkaStreamsUser.builder()
                .username(username)
                .build();
    }
}
