package finalyearproject.repository;

import finalyearproject.model.RefinedTrack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<RefinedTrack, String> {
    RefinedTrack findById(String id);
}
