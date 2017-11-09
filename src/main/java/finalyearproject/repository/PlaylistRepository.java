package finalyearproject.repository;

import finalyearproject.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist,String> {
}
