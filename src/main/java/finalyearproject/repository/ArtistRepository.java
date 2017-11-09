package finalyearproject.repository;

import finalyearproject.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, String>{
}
