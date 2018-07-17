package finalyearproject.utilities;

import com.wrapper.spotify.models.Track;
import finalyearproject.model.RefinedArtist;
import finalyearproject.model.RefinedPlaylist;
import finalyearproject.model.RefinedTrack;
import finalyearproject.model.RefinedUser;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownstreamMapper {

    public static RefinedTrack mapSong(Track fullTrack) {
        List<RefinedArtist> minimisedRefinedArtists = new ArrayList<RefinedArtist>();
        for (int i = 0; i < fullTrack.getArtists().size(); i++) {
            minimisedRefinedArtists.add(mapSimpleArtist(fullTrack.getArtists().get(i)));
        }
        try {
            return RefinedTrack.builder()
                    .id(fullTrack.getId())
                    .artists(minimisedRefinedArtists)
                    .availableMarkets(fullTrack.getAvailableMarkets())
                    .discNum(fullTrack.getDiscNumber())
                    .durationMs(fullTrack.getDuration())
                    .explicit(fullTrack.isExplicit())
                    .externalURL((new URL(fullTrack.getExternalUrls().get("spotify"))))
                    .href(fullTrack.getHref())
                    .name(fullTrack.getName())
                    .previewURL(new URL(fullTrack.getPreviewUrl()))
                    .trackNumber(fullTrack.getTrackNumber())
                    .popularity(fullTrack.getPopularity())
                    .uri(URI.create(fullTrack.getUri()))
                    .build();
        } catch (MalformedURLException e) {
            return RefinedTrack.builder()
                    .id(fullTrack.getId())
                    .artists(minimisedRefinedArtists)
                    .availableMarkets(fullTrack.getAvailableMarkets())
                    .discNum(fullTrack.getDiscNumber())
                    .durationMs(fullTrack.getDuration())
                    .explicit(fullTrack.isExplicit())
                    .href(fullTrack.getHref())
                    .name(fullTrack.getName())
                    .trackNumber(fullTrack.getTrackNumber())
                    .popularity(fullTrack.getPopularity())
                    .uri(URI.create(fullTrack.getUri()))
                    .build();
        }
    }

    public static RefinedArtist mapSimpleArtist(com.wrapper.spotify.models.SimpleArtist fullArtist) {
        try {
            return RefinedArtist.builder()
                    .id(fullArtist.getId())
                    .externalURL((new URL(fullArtist.getExternalUrls().get("spotify"))))
                    .href(fullArtist.getHref())
                    .name(fullArtist.getName())
                    .type(fullArtist.getType().getType())
                    .uri(URI.create(fullArtist.getUri()))
                    .build();
        } catch (MalformedURLException e) {
            return RefinedArtist.builder()
                    .id(fullArtist.getId())
                    .href(fullArtist.getHref())
                    .name(fullArtist.getName())
                    .type(fullArtist.getType().getType())
                    .uri(URI.create(fullArtist.getUri()))
                    .build();
        }
    }

    public static RefinedArtist mapArtist(com.wrapper.spotify.models.Artist fullArtist) {
        try {
            return RefinedArtist.builder()
                    .id(fullArtist.getId())
                    .externalURL((new URL(fullArtist.getExternalUrls().get("spotify"))))
                    .genres(fullArtist.getGenres())
                    .href(fullArtist.getHref())
                    .name(fullArtist.getName())
                    .type(fullArtist.getType().getType())
                    .uri(URI.create(fullArtist.getUri()))
                    .followers(fullArtist.getFollowers().getTotal())
                    .popularity(fullArtist.getPopularity())
                    .build();
        } catch (MalformedURLException e) {
            return RefinedArtist.builder()
                    .id(fullArtist.getId())
                    .genres(fullArtist.getGenres())
                    .href(fullArtist.getHref())
                    .name(fullArtist.getName())
                    .type(fullArtist.getType().getType())
                    .uri(URI.create(fullArtist.getUri()))
                    .followers(fullArtist.getFollowers().getTotal())
                    .popularity(fullArtist.getPopularity())
                    .build();
        }
    }

    public static RefinedUser mapUser(com.wrapper.spotify.models.User fullUser) {
        List<String> imageUrls = new ArrayList<String>();
        if( fullUser.getImages()!=null) {
            for (int i = 0; i < fullUser.getImages().size(); i++) {
                imageUrls.add(fullUser.getImages().get(i).getUrl());
            }
        }
        return RefinedUser.builder()
                .id(fullUser.getId())
                .uri(URI.create(fullUser.getUri()))
                .displayName(fullUser.getDisplayName())
                .numOfFollowers(fullUser.getFollowers().getTotal())
                .href(fullUser.getHref())
                .images(imageUrls)
                .build();
    }

    public static RefinedPlaylist mapPlaylist(com.wrapper.spotify.models.Playlist fullPlaylist){
        List<String> imageUrls = new ArrayList<>();
        if(fullPlaylist.getImages()!=null) {
            for (int i = 0; i < fullPlaylist.getImages().size(); i++) {
                imageUrls.add(fullPlaylist.getImages().get(i).getUrl());
            }
        }
        List<RefinedTrack> formattedRefinedTracks = new ArrayList<RefinedTrack>();
        for (int n = 0; n < fullPlaylist.getTracks().getItems().size(); n++) {
            formattedRefinedTracks.add(mapSong(fullPlaylist.getTracks().getItems().get(n).getTrack()));
        }
        try {
            return RefinedPlaylist.builder()
                    .id(fullPlaylist.getId())
                    .uri(URI.create(fullPlaylist.getUri()))
                    .externalURL(new URL(fullPlaylist.getExternalUrls().get("spotify")))
                    .numOfFollowers(fullPlaylist.getFollowers().getTotal())
                    .href(fullPlaylist.getHref())
                    .images(imageUrls)
                    .name(fullPlaylist.getName())
                    .owner(mapUser(fullPlaylist.getOwner()))
                    .tracks(formattedRefinedTracks)
                    .build();
        } catch (MalformedURLException e) {
            return RefinedPlaylist.builder()
                    .id(fullPlaylist.getId())
                    .uri(URI.create(fullPlaylist.getUri()))
                    .numOfFollowers(fullPlaylist.getFollowers().getTotal())
                    .href(fullPlaylist.getHref())
                    .images(imageUrls)
                    .name(fullPlaylist.getName())
                    .owner(mapUser(fullPlaylist.getOwner()))
                    .tracks(formattedRefinedTracks)
                    .build();
        }

    }


}
