package finalyearproject.repository;

import finalyearproject.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SongRepository extends JpaRepository<Song, String> {
    Song findById(String id);

    @Query("SELECT id FROM Song ORDER BY RAND")
    String findRandom();
}
