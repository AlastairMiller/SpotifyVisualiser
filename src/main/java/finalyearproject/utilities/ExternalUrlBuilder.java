package finalyearproject.utilities;

import com.wrapper.spotify.models.ExternalUrls;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class ExternalUrlBuilder {
    @Builder.Default
    private Map<String, String> externalUrls = new HashMap<String, String>();

    public ExternalUrls getExternalUrls() {
        return new ExternalUrls() {{
            setExternalUrls(externalUrls);
        }};
    }

    public static ExternalUrls getNewExternalUrls(Map<String, String> externalUrls) {
        return  ExternalUrlBuilder.builder()
                .externalUrls(externalUrls)
                .build()
                .getExternalUrls();
    }



}
