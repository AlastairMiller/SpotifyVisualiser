package finalyearproject.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.net.URI;
import java.net.URL;
import java.util.List;

@Data
@Builder
@Entity
public class User {
    @Id
    String id;
    URI uri;
    String displayName;
    URL externalUrl;
    Integer numOfFollowers;
    String href;
    @ElementCollection
    List<String> images;
}
