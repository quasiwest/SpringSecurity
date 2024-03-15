package ssafy.GeniusOfInvestment.user.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ssafy.GeniusOfInvestment._common.exception.CustomBadRequestException;
import ssafy.GeniusOfInvestment._common.response.ErrorType;
import ssafy.GeniusOfInvestment.entity.User;
import ssafy.GeniusOfInvestment.user.dto.SecurityUserDto;
import ssafy.GeniusOfInvestment.user.dto.request.SignUpRequestDto;
import ssafy.GeniusOfInvestment.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{

    private final UserRepository userRepository;

    public SecurityUserDto getSecurityMember(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(IllegalStateException::new);
        return SecurityUserDto.from(user);
    }

    public Optional<User> findBySocialId(String socialId) {
        return userRepository.findBySocialId(socialId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(Long.parseLong(username));

        if(user.isEmpty()){
            throw new CustomBadRequestException(ErrorType.NOT_FOUND_USER);
        }

        return user.get();
    }

    public Long saveSocialMember(SignUpRequestDto signUpRequestDto) {
        User user = User.from(signUpRequestDto);
        return userRepository.save(user).getId();
    }

    public void deleteMember(Optional<User> user) {
        if(user.isEmpty())
            throw new CustomBadRequestException(ErrorType.NOT_FOUND_USER);
        userRepository.save(user.get());
    }
}
