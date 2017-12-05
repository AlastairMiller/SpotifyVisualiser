package finalyearproject.service;


import com.wrapper.spotify.Api;
import finalyearproject.model.Playlist;
import finalyearproject.repository.PlaylistRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshLocalDb {

    PlaylistRepository playlistRepository;
    AuthenticationService authenticationService;

    @Autowired
    public RefreshLocalDb(AuthenticationService authenticationServicem, PlaylistRepository playlistRepository) {
        this.authenticationService = authenticationServicem;
        this.playlistRepository = playlistRepository;
    }

    final static Logger logger = Logger.getLogger(RefreshLocalDb.class);


    @Scheduled(fixedDelay = 10000)
    public void main() {
        Api api = authenticationService.clientCredentialflow();
        logger.info("Database refresh started");
        List<Playlist> playlistsToPull = playlistRepository.findByName(null);
        if (playlistsToPull.size() > 0) {
            pullPlaylists(playlistsToPull);
        } else {
            logger.info("No updates needed to local DB");
        }
    }

    List<com.wrapper.spotify.models.Playlist> pullPlaylists(List<Playlist> playlistsToPull) {
        for (int i = 0; i < playlistsToPull.size(); i++) {

        }


        return null;
    }
}
