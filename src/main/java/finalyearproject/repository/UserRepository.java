package finalyearproject.repository;

import finalyearproject.model.RefinedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<RefinedUser, String> {
    RefinedUser findById(String id);
}
