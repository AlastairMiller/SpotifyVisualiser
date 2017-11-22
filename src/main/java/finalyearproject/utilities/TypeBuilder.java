package finalyearproject.utilities;

import com.wrapper.spotify.models.SpotifyEntityType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeBuilder {
    String type;
    String name;
    int ordinal;

    private SpotifyEntityType getType(){
        return new SpotifyEntityType(){{
            setName(name);
            setType(type);
            setOrdinal(ordinal);
        }}
    }
}
