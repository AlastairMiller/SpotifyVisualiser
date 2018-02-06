package finalyearproject.repository;

import finalyearproject.model.RefinedTrack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<RefinedTrack, String> {
    RefinedTrack findById(String id);
}
