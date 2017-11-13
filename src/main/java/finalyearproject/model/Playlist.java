package finalyearproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;


import javax.persistence.*;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
public class Playlist {
    @Id
    String id;
    URI uri;
    URL externalURL;
    Integer numOfFollowers;
    String href;
    ArrayList<String> images;
    String name;
    @ManyToOne
    User owner;
    Boolean findable;
    String snapshotId;
    ArrayList<Song> tracks;
}
