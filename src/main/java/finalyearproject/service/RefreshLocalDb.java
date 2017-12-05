package finalyearproject.service;


import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.FeaturedPlaylistsRequest;
import com.wrapper.spotify.models.FeaturedPlaylists;
import com.wrapper.spotify.models.SimplePlaylist;
import finalyearproject.model.Playlist;
import finalyearproject.repository.PlaylistRepository;
import finalyearproject.utilities.DownstreamMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class RefreshLocalDb {

    PlaylistRepository playlistRepository;
    AuthenticationService authenticationService;

    @Autowired
    public RefreshLocalDb(AuthenticationService authenticationServicem, PlaylistRepository playlistRepository) {
        this.authenticationService = authenticationServicem;
        this.playlistRepository = playlistRepository;
    }


    @Scheduled(fixedDelay = 10000)
    public void main() {
        Api api = authenticationService.clientCredentialflow();
        log.info("Database refresh started");
        List<Playlist> playlistsToPull = playlistRepository.findByName(null);
        pullScheduledPlaylists(api);
        if (playlistsToPull.size() > 0) {
            pullPlaylists(playlistsToPull, api);
        } else {
            log.info("No updates needed to local DB");
        }
    }

    private void pullScheduledPlaylists(Api api) {
        log.info("Pulling featured Playlists");
        Date timestamp = new Date();
        FeaturedPlaylists featuredPlaylists = new FeaturedPlaylists();

        final FeaturedPlaylistsRequest request = api.getFeaturedPlaylists()
                .limit(10)
                .offset(1)
                .country("GB")
                .timestamp(timestamp)
                .build();
        try {
            featuredPlaylists = request.get();
            List<SimplePlaylist> featuredPlaylistList = featuredPlaylists.getPlaylists().getItems();
            for (int i = 0; i <featuredPlaylistList.size(); i++){
                Playlist newPlaylist = DownstreamMapper.mapPlaylist(featuredPlaylistList.get(i));
            }
        } catch (Exception e) {
            log.error("Failed to pull featured Playlists from Spotify Api");
            e.printStackTrace();

        }
    }

    List<com.wrapper.spotify.models.Playlist> pullPlaylists(List<Playlist> playlistsToPull, Api api) {
        for (int i = 0; i < playlistsToPull.size(); i++) {
        }


        return null;
    }
}
