package finalyearproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
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
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    @Id
    String id;
    @ElementCollection
    List<Artist> artists;
    @ElementCollection
    List<String> availableMarkets;
    int discNum;
    int durationMs;
    boolean explicit;
    URL externalURL;
    String href;
    String name;
    URL previewURL;
    int trackNumber;
    int popularity;
    URI uri;

}