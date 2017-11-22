package finalyearproject.utilities;

import com.wrapper.spotify.models.Image;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageBuilder {
    Integer height;
    String url;
    Integer width;

    private Image getImage() {
        return new Image() {{
            setHeight(height);
            setWidth(width);
            setUrl(url);
        }};
    }

    public static Image getNewImage(Integer height, Integer width, String url) {
        return ImageBuilder.builder()
                .height(height)
                .width(width)
                .url(url)
                .build()
                .getImage();
    }
}
