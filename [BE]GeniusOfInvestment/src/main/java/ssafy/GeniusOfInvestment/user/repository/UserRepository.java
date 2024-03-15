package ssafy.GeniusOfInvestment.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ssafy.GeniusOfInvestment.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findBySocialId(String socialId);


}
