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

    @Autowired
    PlaylistRepository playlistRepository;

    final static Logger logger = Logger.getLogger(RefreshLocalDb.class);

    Api api = Api.DEFAULT_API;

    @Scheduled(fixedDelay = 10000)
    public void main() throws InterruptedException {
        Thread refresh = new Thread(RefreshRunnable);
        refresh.start();
        refresh.join();
    }

    Runnable RefreshRunnable = new Runnable() {

        public void run() {
            logger.info("Database refresh started");
            List<Playlist> playlistsToPull = playlistRepository.findByName(null);
            if (playlistsToPull != null) {
                AuthenticationService authenticationService = new AuthenticationService();

                pullPlaylists(playlistsToPull);
            }
        }

    };

    List<com.wrapper.spotify.models.Playlist> pullPlaylists(List<Playlist> playlistsToPull) {
        for(int i=0; i < playlistsToPull.size(); i++){

        }


        return null;
    }
}
