package finalyearproject.service;


import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.FeaturedPlaylistsRequest;
import com.wrapper.spotify.methods.PlaylistRequest;
import com.wrapper.spotify.methods.UserRequest;
import com.wrapper.spotify.models.FeaturedPlaylists;
import com.wrapper.spotify.models.SimplePlaylist;
import com.wrapper.spotify.models.User;
import finalyearproject.model.Playlist;
import finalyearproject.repository.PlaylistRepository;
import finalyearproject.repository.UserRepository;
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
    UserRepository userRepository;

    @Autowired
    public RefreshLocalDb(AuthenticationService authenticationServicem, PlaylistRepository playlistRepository, UserRepository userRepository) {
        this.authenticationService = authenticationServicem;
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
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
            for (SimplePlaylist simplePlaylist : featuredPlaylistList) {
                com.wrapper.spotify.models.Playlist fullPlaylist = convertSimplePlayliststoPlaylists(api, simplePlaylist);
                if (fullPlaylist != null) {
log.info("Sucessfully pulled {}", fullPlaylist.getName());
                    Playlist newPlaylist = DownstreamMapper.mapPlaylist(fullPlaylist);
                    if (userRepository.findById(fullPlaylist.getOwner().getId()) == null) {
                        finalyearproject.model.User newUser = DownstreamMapper.mapUser(pullUser(api, fullPlaylist.getOwner().getId()));
                        userRepository.saveAndFlush(newUser);
                    }
                    playlistRepository.saveAndFlush(newPlaylist);
                    log.info("Saved a playlist called {}", newPlaylist);
                }
            }
        } catch (Exception e) {
            log.error("Failed to pull featured Playlists from Spotify Api");
            e.printStackTrace();
        }
    }


    private User pullUser(Api api, String id) {
        final UserRequest request = api.getUser(id).build();
        try {
            return request.get();
        } catch (Exception e) {
            log.error("Cannot download user {}", id);
            return null;
        }
    }

    private com.wrapper.spotify.models.Playlist convertSimplePlayliststoPlaylists(Api api, SimplePlaylist simplePlaylist) {
        final PlaylistRequest request = api.getPlaylist(simplePlaylist.getOwner().getId(), simplePlaylist.getId())
                .build();
        try {
            final com.wrapper.spotify.models.Playlist playlist = request.get();
        } catch (Exception e) {
            log.error("Failed to convert simple Playlist {} ", simplePlaylist.getName());
        }
        return null;
    }

    List<com.wrapper.spotify.models.Playlist> pullPlaylists(List<Playlist> playlistsToPull, Api api) {
        for (int i = 0; i < playlistsToPull.size(); i++) {
        }


        return null;
    }
}
