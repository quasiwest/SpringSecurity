package com.example.oauth2.jwt;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<SavedToken, String> {

    // accessToken으로 RefreshToken을 찾아온다.
    Optional<SavedToken> findByAccessToken(String accessToken);
}
