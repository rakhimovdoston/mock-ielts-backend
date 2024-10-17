package com.search.teacher.service.user;

import com.search.teacher.config.service.UserManager;
import com.search.teacher.dto.response.AuthenticationResponse;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.entities.UserToken;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.UserTokenRepository;
import com.search.teacher.service.JwtService;
import com.search.teacher.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserTokenService {
    private final JwtService jwtService;
    private final UserTokenRepository userTokenRepository;

    public JResponse generateToken(User user) {
        String jwtToken = jwtService.generateToken(new UserManager((user)));
        String refreshToken = jwtService.generateRefreshToken(new UserManager((user)));
        UserToken userToken = new UserToken();
        userToken.setAccessToken(jwtToken);
        userToken.setRefreshToken(refreshToken);
        userToken.setUser(user);
        userToken.setExpireDate(new Date(new Date().getTime() + jwtService.jwtExpiration));
        userToken.setRefreshExpireDate(new Date(new Date().getTime() + jwtService.refreshExpiration));
        userTokenRepository.save(userToken);
        return JResponse.success(AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build());
    }

    public User checkToken(String jwt) {
        if (jwtService.isTokenExpired(jwt))
            return null;
        String email = jwtService.extractUsername(jwt);
        UserToken userToken = userTokenRepository.findByAccessToken(jwt);
        if (userToken == null || !DateUtils.isExpirationToken(userToken.getExpireDate())) {
            return null;
        }
        User user = userToken.getUser();
        if (email.equals(user.getEmail())) return user;
        return null;
    }

    public UserToken findByRefreshToken(String refreshToken) {
        return userTokenRepository.findByRefreshToken(refreshToken);
    }
}
