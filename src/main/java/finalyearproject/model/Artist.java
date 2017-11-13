package finalyearproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Builder
public class Artist {
    @Id
    String id;
    URL externalURL;
    List<String> genres;
    String href;
    String name;
    String type;
    URI uri;
}
