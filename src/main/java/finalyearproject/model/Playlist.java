package finalyearproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@Entity
public class Playlist {
    @Id
    String id;
    URI uri;
    URL[] externalURLs;
    Integer numOfFollowers;
    String href;
    String[] images;
    String name;
    @ManyToOne
    User owner;
    Boolean findable;
    String snapshot_id;
    ArrayList<Song> tracks;
}
