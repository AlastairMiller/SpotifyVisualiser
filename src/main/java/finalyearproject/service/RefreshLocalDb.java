package finalyearproject.service;


import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.FeaturedPlaylistsRequest;
import com.wrapper.spotify.methods.PlaylistRequest;
import com.wrapper.spotify.methods.TrackRequest;
import com.wrapper.spotify.methods.UserRequest;
import com.wrapper.spotify.models.FeaturedPlaylists;
import com.wrapper.spotify.models.SimplePlaylist;
import com.wrapper.spotify.models.Track;
import com.wrapper.spotify.models.User;
import finalyearproject.model.Playlist;
import finalyearproject.model.Song;
import finalyearproject.repository.PlaylistRepository;
import finalyearproject.repository.SongRepository;
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
    SongRepository songRepository;

    @Autowired
    public RefreshLocalDb(AuthenticationService authenticationServicem, PlaylistRepository playlistRepository, UserRepository userRepository, SongRepository songRepository) {
        this.authenticationService = authenticationServicem;
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
        this.songRepository = songRepository;
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
                    for(int i=0; i<fullPlaylist.getTracks().getTotal(); i++){
                        if(songRepository.findById(fullPlaylist.getTracks().getItems().get(i).getTrack().getId()) == null){
                            Song newSong = DownstreamMapper.mapSong(pullSong(api,fullPlaylist.getTracks().getItems().get(i).getTrack().getId()));
                            songRepository.saveAndFlush(newSong);
                        }
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
            log.error("Cannot download user with id {}", id);
            return null;
        }
    }

    Track pullSong(Api api, String id) {
        final TrackRequest request = api.getTrack(id).build();
        try {
            return request.get();
        } catch (Exception e) {
            log.error("Cannot download song with id {}", id);
            return null;
        }
    }

    private com.wrapper.spotify.models.Playlist convertSimplePlayliststoPlaylists(Api api, SimplePlaylist simplePlaylist) {
        final PlaylistRequest request = api.getPlaylist(simplePlaylist.getOwner().getId(), simplePlaylist.getId())
                .build();
        try {
            final com.wrapper.spotify.models.Playlist playlist = request.get();
            return playlist;
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
