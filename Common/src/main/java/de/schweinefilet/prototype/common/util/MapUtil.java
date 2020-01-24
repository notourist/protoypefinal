package de.schweinefilet.prototype.common.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.schweinefilet.prototype.common.map.Map;
import java.io.File;
import java.io.IOException;

/**
 * Contains utility methods for map loading, writing and editing.
 */
public class MapUtil {

    private MapUtil() {
    }

    /**
     * Checks if a map with the given name exists.
     *
     * @param name The map name
     * @return Returns true if the map exists, else false
     */
    public static boolean exists(String name) {
        return MapUtil.class.getResourceAsStream("/maps/" + name + ".json") != null;
    }

    /**
     * Loads the map with the given name.
     *
     * @param name The map name
     * @return Returns the map, null when an Exception occures
     * @throws IOException -
     */
    public static Map loadMap(String name) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(mapper.getDeserializationConfig().getDefaultVisibilityChecker()
            .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
            .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        return mapper.readValue(MapUtil.class.getResourceAsStream("/maps/" + name + ".json"),
            Map.class);
    }

    //map making
    private static void writeMap(Map map, String path) {
        ObjectMapper mapper = getObjectMapper();

        try {
            mapper.writeValue(new File(path, map.getName() + ".json"), map);
            LogUtil.info("Wrote map " + map.getName());
        } catch (IOException e) {
            LogUtil.error("Can't write map " + map);
            e.printStackTrace();
        }
    }

    /**
     * Creates a {@link ObjectMapper} that ignores getters, setters and constructors.
     * @return -
     */
    private static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(mapper.getDeserializationConfig().getDefaultVisibilityChecker()
            .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
            .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        return mapper;
    }

    //map saver
    public static void main(String[] args) {
        //Test1
        /*HashSet<Spawn> spawns = new HashSet<>();
        spawns.add(new Spawn(new Point(300, 450), Team.BLUE));
        spawns.add(new Spawn(new Point(900, 450), Team.RED));

        Map map = new Map("Test1",
                spawns,
                null,
                Color.DARK_GRAY,
                (byte) 10,
                900,
                900,
                new Flag(new Point(600, 450)));
        HashSet<Spawn> spawns = new HashSet<>();
        spawns.add(new Spawn(new Point(250, 250), Team.RED));
        spawns.add(new Spawn(new Point(250, 750), Team.BLUE));
        HashSet<Block> blocks = new HashSet<>();
        Map map = new Map("Test2",
            spawns,
            blocks,
            (byte) 10,
            1000,
            500,
            new Flag(new Point(500, 250)),
            (byte) 3);
        MapUtil.writeMap(map, args[0]);*/
    }
}
