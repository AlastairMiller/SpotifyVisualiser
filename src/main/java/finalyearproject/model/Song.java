package finalyearproject.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Builder
public class Song {
    @Id
    String id;
    List<Artist> artists;
    List<String> availableMarkets;
    int discNum;
    int durationMs;
    boolean explicit;
    URL externalURL;
    URL href;
    String name;
    URL previewURL;
    int trackNumber;
    int popularity;
    URI uri;

}
