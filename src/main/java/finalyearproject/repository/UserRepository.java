package finalyearproject.repository;

import finalyearproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findById(String id);
}
