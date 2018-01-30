package finalyearproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Playlist {
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
    private User owner;
    @ElementCollection
    private List<Song> tracks;
}
