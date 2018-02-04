package finalyearproject.repository;

import finalyearproject.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist,String> {
    List<Playlist> findByName(String Name);
    Playlist findById(String id);
}
