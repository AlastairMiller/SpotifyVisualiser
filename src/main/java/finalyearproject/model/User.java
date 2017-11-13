package finalyearproject.model;

import lombok.Data;

import javax.persistence.*;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

@Data
@Entity
public class User {
    @Id
    String id;
    URI uri;
    String displayName;
    URL externalUrl;
    Integer numOfFollowers;
    String href;
    ArrayList<String> images;
}
