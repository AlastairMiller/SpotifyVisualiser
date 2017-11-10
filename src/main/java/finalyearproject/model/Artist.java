package finalyearproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.net.URI;
import java.net.URL;
import java.util.Collection;

@Data
@AllArgsConstructor
@Entity
public class Artist {
    @Id
    String id;
    URL externalURLs;
    Collection<String> genres;
    String href;
    String name;
    String type;
    URI uri;
}
