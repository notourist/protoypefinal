package de.schweinefilet.prototype.common.map;

import de.schweinefilet.prototype.common.util.LogUtil;
import de.schweinefilet.prototype.common.util.MapUtil;
import java.io.IOException;
import java.util.Optional;
import lombok.Getter;

/**
 * Contains the map, a method to load the map and listener methods to
 * react to map loading events.
 */
public abstract class MapHolder {

    /**
     * The map the game is played on.
     */
    @Getter
    protected Map map;

    /**
     * Loads the map with the given name.
     *
     * @param name the map name
     */
    public void setMap(String name) {
        //check if the map exists in the jar
        if (MapUtil.exists(name)) {
            try {
                //load the map
                map = MapUtil.loadMap(name);
            } catch (IOException e) {
                LogUtil.error("Can't load map " + name);
                e.printStackTrace();
                onMapLoadError(name);
            }
            LogUtil.info("Loaded map " + name);
            onMapLoadSuccess();
        } else {
            //map doesn't exist
            LogUtil.error("Map \"" + name + "\" doesn't exist!");
            onMapDoesNotExist(name);
        }
    }

    /**
     * Wraps the {@link MapHolder#map} into an {@link Optional#ofNullable(Object)}.
     * @return -
     */
    public Optional<Map> getOptionalMap() {
        return Optional.ofNullable(map);
    }

    /**
     * Called when the map does not exist.
     * @param name the map name
     */
    protected abstract void onMapDoesNotExist(String name);

    /**
     * Called when the map was not loaded successfully.
     * @param name the map name
     */
    protected abstract void onMapLoadError(String name);

    /**
     * Called when the map load was successful.
     */
    protected abstract void onMapLoadSuccess();
}
