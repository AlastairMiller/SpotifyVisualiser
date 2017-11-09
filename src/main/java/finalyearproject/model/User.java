package finalyearproject.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.net.URI;
import java.net.URL;

@Data
@Entity
public class User {
    @Id
    String id;
    URI uri;
    String display_Name;
    URL[] external_Urls;
    Integer numOfFollowers;
    String href;
    String[] images;
}
