package finalyearproject.repository;

import finalyearproject.model.RefinedArtist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<RefinedArtist, String>{
    RefinedArtist findById(String id);
}
