package finalyearproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.net.URI;
import java.net.URL;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class RefinedPlaylist {
    @Id
    private String id;
    private URI uri;
    private URL externalURL;
    private Integer numOfFollowers;
    private String href;
    @ElementCollection
    private List<String> images;
    private String name;
    @ManyToOne
    private RefinedUser owner;
    @ElementCollection()
    private List<RefinedTrack> tracks;
}
