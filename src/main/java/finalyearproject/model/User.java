package finalyearproject.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.net.URI;
import java.net.URL;
import java.util.Collection;

@Data
@Entity
public class User {
    @Id
    String id;
    URI uri;
    String displayName;
    Collection<URL> externalUrls;
    Integer numOfFollowers;
    String href;
    Collection<String> images;
}
