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
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefinedTrack {
    @Id
    private String id;
    @ElementCollection
    private List<RefinedArtist> artists;
    @ElementCollection
    private List<String> availableMarkets;
    private int discNum;
    private int durationMs;
    private boolean explicit;
    private URL externalURL;
    private String href;
    private String name;
    private URL previewURL;
    private int trackNumber;
    private int popularity;
    private URI uri;

}