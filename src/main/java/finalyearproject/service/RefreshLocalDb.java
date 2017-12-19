package finalyearproject.service;


import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.*;
import com.wrapper.spotify.models.FeaturedPlaylists;
import com.wrapper.spotify.models.SimplePlaylist;
import com.wrapper.spotify.models.Track;
import com.wrapper.spotify.models.User;
import finalyearproject.model.Playlist;
import finalyearproject.repository.ArtistRepository;
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

import java.util.ArrayList;
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
    ArtistRepository artistRepository;

    @Autowired
    public RefreshLocalDb(AuthenticationService authenticationService, PlaylistRepository playlistRepository, UserRepository userRepository, SongRepository songRepository, ArtistRepository artistRepository) {
        this.authenticationService = authenticationService;
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
        this.songRepository = songRepository;
        this.artistRepository = artistRepository;
    }


    @Scheduled(fixedDelay = 10000)
    public void main() {
        Api api = authenticationService.clientCredentialflow();
        log.info("Database refresh started");
        List<Playlist> playlistsToPull = playlistRepository.findByName(null);
        pullScheduledPlaylists(api);
        if (playlistsToPull.size() > 0) {
            for (Playlist aPlaylistToPull : playlistsToPull) {
                pullPlaylist(api, aPlaylistToPull);
            }
        } else {
            log.info("No updates needed to local DB");
        }
    }

    private void pullScheduledPlaylists(Api api) {
        log.info("Pulling featured Playlists");
        Date timestamp = new Date();
        FeaturedPlaylists featuredPlaylists;

        final FeaturedPlaylistsRequest request = api.getFeaturedPlaylists()
                .limit(20)
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

                    Playlist newPlaylist = DownstreamMapper.mapPlaylist(fullPlaylist);

                    if (userRepository.findById(fullPlaylist.getOwner().getId()) == null) {

                        finalyearproject.model.User newUser = DownstreamMapper.mapUser(pullUser(api, fullPlaylist.getOwner().getId()));
                        userRepository.saveAndFlush(newUser);

                    }
                    ArrayList<String> songIdArrayList = new ArrayList<String>();
                    for (int i = 0; i < fullPlaylist.getTracks().getTotal(); i++) {
                        if (songRepository.findById(fullPlaylist.getTracks().getItems().get(i).getTrack().getId()) == null) {
                            songIdArrayList.add(fullPlaylist.getTracks().getItems().get(i).getTrack().getId());

                        }
                    }
                    if (songIdArrayList.size() > 0) {
                        List<Track> TrackArrayList = pullSongs(api, songIdArrayList);
                        if (TrackArrayList != null) {
                            for (Track track : TrackArrayList) {
                                if (track != null) {
                                    for (int p = 0; p < track.getArtists().size(); p++) {
                                        if (artistRepository.findById(track.getArtists().get(p).getId()) == null) {
                                            com.wrapper.spotify.models.Artist newArtist = pullArtist(api, track.getArtists().get(p).getId());
                                            if (newArtist != null) {
                                                artistRepository.saveAndFlush(DownstreamMapper.mapArtist(newArtist));
                                                log.info("Saved Artist: {}", newArtist.getName());
                                            }

                                        }
                                    }
                                    songRepository.saveAndFlush(DownstreamMapper.mapSong(track));
                                    log.info("Saved Song: {} to the database", track.getName());
                                }
                            }
                        }
                    }

                    playlistRepository.saveAndFlush(newPlaylist);
                    log.info("Saved a playlist called {}", newPlaylist.getName());
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
            log.error("Cannot download user metadata with id {}", id);
            return null;
        }
    }

    public static Track pullSong(Api api, String id) {
        final TrackRequest request = api.getTrack(id).build();
        try {
            return request.get();
        } catch (Exception e) {
            log.error("Cannot download metadata for song with id {}", id);
            e.printStackTrace();
            return null;
        }
    }

    public static List<Track> pullSongs(Api api, ArrayList<String> ids) {
        final TracksRequest request = api.getTracks(ids).build();
        try {
            return request.get();
        } catch (Exception e) {
            log.error("Cannot retrieve batch of songs from api");
            return null;
        }
    }

    public static com.wrapper.spotify.models.Artist pullArtist(Api api, String id) {
        final ArtistRequest request = api.getArtist(id).build();
        try {
            return request.get();
        } catch (Exception e) {
            log.error("Failed to download metadata for artist {} ", id);
            return null;
        }

    }

    private com.wrapper.spotify.models.Playlist convertSimplePlayliststoPlaylists(Api api, SimplePlaylist simplePlaylist) {
        return pullPlaylist(api, simplePlaylist.getOwner().getId(), simplePlaylist.getId());
    }


    com.wrapper.spotify.models.Playlist pullPlaylist(Api api, Playlist playlistToPull) {
        return pullPlaylist(api, playlistToPull.getOwner().getId(), playlistToPull.getId());
    }

    com.wrapper.spotify.models.Playlist pullPlaylist(Api api, String ownerId, String playlistId) {
        final PlaylistRequest request = api.getPlaylist(ownerId, playlistId).build();
        try {
            return request.get();
        } catch (Exception e) {
            log.error("Failed to download metadata for playlist {} ", playlistId);
        }
        return null;
    }

}
