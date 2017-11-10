package finalyearproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@Entity
public class Playlist {
    @Id
    String id;
    URI uri;
    Collection<URL> externalURLs;
    Integer numOfFollowers;
    String href;
    Collection<String> images;
    String name;
    @ManyToOne
    User owner;
    Boolean findable;
    String snapshotId;
    ArrayList<Song> tracks;
}
