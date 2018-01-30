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

    //TODO 1. Catch when access token expires
    //TODO 2. Remove all local songs which do not match with latest version of playlist
    //TODO 3. Write UI
    //TODO 4. Write user comparision service/module
    //TODO 5. Write documentation
    //TODO 6. Find someplace to host

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
        Api api = authenticationService.clientCredentialFlow();
        log.info("-----------------------Database refresh started-----------------------");
        List<Playlist> playlistsToPull = playlistRepository.findByName(null);
        pullLocationPlaylists(api);
        pullFeaturedPlaylists(api);
        //TODO pull playlists from user.
        if (!playlistsToPull.isEmpty()) {
            for (Playlist aPlaylistToPull : playlistsToPull) {
                pullPlaylist(api, aPlaylistToPull);
            }
        } else {
            log.info("No updates needed to local DB");
        }
    }

    private void pullLocationPlaylists(Api api) {
        log.info("-----------------------Pulling Location Playlists-----------------------");
        List<SimplePlaylist> playlists = pullPlaylistsForAUser(api, "thesoundsofspotify", 0);
        playlists.removeIf(SimplePlaylist -> !SimplePlaylist.getName().contains("The Sound of "));
//        playlists.removeIf(SimplePlaylist -> SimplePlaylist.getName().matches("\\w*[A-Z][A-Z]\\b"));
        for (int i = 0; i < playlists.size(); i++) {
            if (playlists.get(i).getName().matches(".*[A-Z][A-Z]\\b")) {
                playlists.remove(i);
            }
        }
        saveSimplePlaylistsToDatastore(api, playlists);
    }

    private void pullFeaturedPlaylists(Api api) {
        log.info("-----------------------Pulling featured Playlists-----------------------");
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
            saveSimplePlaylistsToDatastore(api, featuredPlaylistList);
        } catch (Exception e) {
            log.error("Failed to pull featured Playlists from Spotify Api, HTTP Status code: {}", e.getMessage());
        }
    }

    private void saveSimplePlaylistsToDatastore(Api api, List<SimplePlaylist> playlistsToSave) {
        for (SimplePlaylist simplePlaylist : playlistsToSave) {
            com.wrapper.spotify.models.Playlist fullPlaylist = convertSimplePlaylistToPlaylist(api, simplePlaylist);

            if (fullPlaylist != null) {
                Playlist newPlaylist = DownstreamMapper.mapPlaylist(fullPlaylist);

                playlistCleanup(newPlaylist);

                if (userRepository.findById(fullPlaylist.getOwner().getId()) == null) {

                    finalyearproject.model.User newUser = DownstreamMapper.mapUser(pullUser(api, fullPlaylist.getOwner().getId()));
                    userRepository.saveAndFlush(newUser);

                }
                ArrayList<String> songIdArrayList = new ArrayList<String>();
                for (int i = 0; i < fullPlaylist.getTracks().getItems().size(); i++) {
                    if (songRepository.findById(fullPlaylist.getTracks().getItems().get(i).getTrack().getId()) == null) {
                        songIdArrayList.add(fullPlaylist.getTracks().getItems().get(i).getTrack().getId());

                    }
                }
                if (songIdArrayList.size() > 0) {
                    List<Track> trackArrayList = pullSongs(api, songIdArrayList);
                    if (trackArrayList != null) {
                        ArrayList<String> artistIdArrayList = new ArrayList<String>();
                        for (Track track : trackArrayList) {
                            if (track != null) {
                                for (int p = 0; p < track.getArtists().size(); p++) {
                                    if (artistRepository.findById(track.getArtists().get(p).getId()) == null) {
                                        artistIdArrayList.add(track.getArtists().get(p).getId());
                                    }
                                }
                            }
                        }

                        List<com.wrapper.spotify.models.Artist> artistList = pullArtists(api, artistIdArrayList);
                        if (artistList != null) {
                            for (com.wrapper.spotify.models.Artist artist : artistList) {
                                artistRepository.saveAndFlush(DownstreamMapper.mapArtist(artist));
                                log.info("Saved Artist: {} to the database", artist.getName());
                            }
                        }

                        for (Track track : trackArrayList) {
                            songRepository.saveAndFlush(DownstreamMapper.mapSong(track));
                            log.info("Saved Song: {} to the database", track.getName());
                        }
                    }
                }


                playlistRepository.saveAndFlush(newPlaylist);
                log.info("Saved a playlist called {}", newPlaylist.getName());
            }
        }
    }


    private User pullUser(Api api, String id) {
        final UserRequest request = api.getUser(id).build();
        try {
            return request.get();
        } catch (Exception e) {
            log.error("Cannot download user metadata with id {}, HTTP Status code: {}", id, e.getMessage());
            return null;
        }
    }

    public static Track pullSong(Api api, String id) {
        final TrackRequest request = api.getTrack(id).build();
        try {
            return request.get();
        } catch (Exception e) {
            log.error("Cannot download metadata for song with id {}, HTTP Status code: {}", id, e.getMessage());
            return null;
        }
    }

    public static List<Track> pullSongs(Api api, ArrayList<String> ids) {
        List<Track> pulledTracks = new ArrayList<Track>();
        if (ids.size() > 50) {
            final TracksRequest request = api.getTracks(ids.subList(0, 49)).build();
            ids = new ArrayList<>(ids.subList(49, ids.size()));
            try {
                pulledTracks.addAll(request.get());
            } catch (Exception e) {
                log.error("Cannot retrieve batch of songs from api, HTTP Status code: {}", e.getMessage());
                return null;
            }
            pulledTracks.addAll(pullSongs(api, ids));
            return pulledTracks;
        } else {
            final TracksRequest request = api.getTracks(ids).build();
            try {
                pulledTracks.addAll(request.get());
                return pulledTracks;
            } catch (Exception e) {
                log.error("Cannot retrieve batch of songs from api, HTTP error code: {}", e.getMessage());
                return null;
            }
        }
    }

    public static com.wrapper.spotify.models.Artist pullArtist(Api api, String id) {
        ArtistRequest request = api.getArtist(id).build();
        try {
            return request.get();
        } catch (Exception e) {
            log.error("Failed to download metadata for artist {}, HTTP error code: {} ", id, e.getMessage());
            return null;
        }

    }

    public static List<com.wrapper.spotify.models.Artist> pullArtists(Api api, List<String> ids) {
        List<com.wrapper.spotify.models.Artist> pulledArtists = new ArrayList<com.wrapper.spotify.models.Artist>();
        if (ids.size() > 50) {
            final ArtistsRequest request = api.getArtists(ids.subList(0, 49)).build();
            ids = new ArrayList<String>(ids.subList(49, ids.size()));
            try {
                pulledArtists.addAll(request.get());
            } catch (Exception e) {
                log.error("Cannot retrieve batch of Artists. HTTP error code: {}", e.getMessage());
            }
            pulledArtists.addAll(pullArtists(api, ids));
            return pulledArtists;
        } else {
            final ArtistsRequest request = api.getArtists(ids).build();
            try {
                pulledArtists.addAll(request.get());
                return pulledArtists;
            } catch (Exception e) {
                log.error("Cannot retrieve batch of Artists. HTTP error code: {}", e.getMessage());
                return null;
            }
        }
    }

    private com.wrapper.spotify.models.Playlist convertSimplePlaylistToPlaylist(Api api, SimplePlaylist simplePlaylist) {
        return pullPlaylist(api, simplePlaylist.getOwner().getId(), simplePlaylist.getId());
    }

    private List<SimplePlaylist> pullPlaylistsForAUser(Api api, String userId, int offset) {
        final UserPlaylistsRequest request = api.getPlaylistsForUser(userId).limit(50).offset(offset).build();
        try {
            List<SimplePlaylist> usersPlaylist = new ArrayList<SimplePlaylist>(request.get().getItems());
            log.info("Pulled {} playlists for user {}", offset, userId);
            if (usersPlaylist.size() == 50) {
                usersPlaylist.addAll(pullPlaylistsForAUser(api, userId, offset + 50));
            }
            return usersPlaylist;
        } catch (Exception e) {
            log.error("Failed to grab user's: {} playlist", userId);
            return null;
        }
    }


    com.wrapper.spotify.models.Playlist pullPlaylist(Api api, Playlist playlistToPull) {
        return pullPlaylist(api, playlistToPull.getOwner().getId(), playlistToPull.getId());
    }

    com.wrapper.spotify.models.Playlist pullPlaylist(Api api, String ownerId, String playlistId) {
        final PlaylistRequest request = api.getPlaylist(ownerId, playlistId).build();
        try {
            return request.get();
        } catch (Exception e) {
            log.error("Failed to download metadata for playlist {}, HTTP error code: {}", playlistId, e.getMessage());
            return null;
        }

    }

    void playlistCleanup(Playlist newPlaylist) {
        Playlist storedPlaylist = playlistRepository.findOne(newPlaylist.getId());
        for (int i = 0; i < storedPlaylist.getTracks().size(); i++) {
            for (int p = 0; p < newPlaylist.getTracks().size(); p++) {
                if (storedPlaylist.getTracks().get(i).equals(newPlaylist.getTracks().get(p))) {
                    break;
                }
            }
            log.info("Removing song: {} from playlist: {}", storedPlaylist.getTracks().get(i).getName(), storedPlaylist.getName());
            storedPlaylist.getTracks().remove(i);
        }
        playlistRepository.delete(newPlaylist.getId());
        playlistRepository.saveAndFlush(storedPlaylist);
    }

}
