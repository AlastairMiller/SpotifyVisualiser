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
    String id;
    URI uri;
    URL externalURL;
    Integer numOfFollowers;
    String href;
    @ElementCollection
    List<String> images;
    String name;
    @ManyToOne
    User owner;
    @ElementCollection
    List<Song> tracks;
}
