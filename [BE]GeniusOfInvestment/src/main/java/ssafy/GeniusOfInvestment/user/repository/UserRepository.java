package ssafy.GeniusOfInvestment.user.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ssafy.GeniusOfInvestment.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findBySocialId(String socialId);
    List<User> findAllByOrderByExpDesc();

    @Query("SELECT COUNT(*) + 1 AS rank FROM User u WHERE u.exp > (SELECT u2.exp FROM User u2 WHERE u2.id = :userId)")
    Long findRankByExp(@Param("userId") Long userId);

    boolean existsByNickName(String nickname);
}
