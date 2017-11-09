package finalyearproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.net.URI;
import java.net.URL;

@Data
@AllArgsConstructor
@Entity
public class Artist {
    @Id
    String id;
    URL externalURLs;
    String[] genres;
    String href;
    String name;
    String type;
    URI uri;
}
