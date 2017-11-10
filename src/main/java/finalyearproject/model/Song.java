package finalyearproject.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.net.URI;
import java.net.URL;
import java.util.Collection;

@Data
@Entity
public class Song {
   @Id
   String id;
   Collection<Artist> artists;
   Collection<String> availableMarkets;
   int discNum;
   int durationMs;
   boolean explicit;
   URL externalURL;
   String href;
   boolean isPlayable;
   URL linkedFrom;
   String name;
   String previewURL;
   int trackNumber;
   int popularity;
   URI uri;

}
