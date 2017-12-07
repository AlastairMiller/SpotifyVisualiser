package finalyearproject.repository;

import finalyearproject.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, String> {
    Song findById(String id);
}
