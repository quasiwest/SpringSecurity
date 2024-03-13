package com.example.oauth2.user.service;

import com.example.oauth2.entity.User;
import com.example.oauth2.user.dto.request.SingUpRequestDto;
import com.example.oauth2.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findBySocialId(String socialId){
        return userRepository.findBySocialId(socialId);
    }

    @Transactional
    public Long saveSocialMember(SingUpRequestDto singUpRequestDto) {

        User user = User.from(singUpRequestDto);

        return userRepository.save(user).getId();

    }



}
