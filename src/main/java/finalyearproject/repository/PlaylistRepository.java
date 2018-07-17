package finalyearproject.repository;

import finalyearproject.model.RefinedPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<RefinedPlaylist,String> {
    List<RefinedPlaylist> findByName(String Name);
    RefinedPlaylist findById(String id);
}
