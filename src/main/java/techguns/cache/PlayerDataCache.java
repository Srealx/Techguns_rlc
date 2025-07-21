package techguns.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author srealx
 * @date 2025/3/20
 */
public class PlayerDataCache {

    public static final Map<UUID,ZoomCacheDataBean> PLAYER_ZOOM_DATA = new HashMap<>(8);
}
