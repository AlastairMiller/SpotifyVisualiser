package finalyearproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.net.URI;
import java.net.URL;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class RefinedUser {
    @Id
    private String id;
    private URI uri;
    private String displayName;
    private URL externalUrl;
    private Integer numOfFollowers;
    private String href;
    @ElementCollection
    private List<String> images;
}
