package de.schweinefilet.prototype.client.map;

import static de.schweinefilet.prototype.client.Client.CONTROL_HANDLER;
import static de.schweinefilet.prototype.client.Client.ENTITY_HOLDER;

import de.schweinefilet.prototype.client.Client;
import de.schweinefilet.prototype.client.util.UILog;
import de.schweinefilet.prototype.common.entity.Block;
import de.schweinefilet.prototype.common.entity.Flag;
import de.schweinefilet.prototype.common.map.MapHolder;
import java.util.HashSet;
import lombok.NoArgsConstructor;

/**
 * A class which is used for updating, getting and setting map and player related information.
 */
@NoArgsConstructor
public class ClientMapHolder extends MapHolder {

    /**
     * Checks if the map is loaded.
     *
     * @return true, if the map is loaded, else false
     */
    public boolean isLoaded() {
        return map != null;
    }

    /**
     * Resets the user's and flag's location.
     */
    public void resetMap() {
        map.getFlag().reset();
        ENTITY_HOLDER.getUser().spawn();
    }

    /**
     * Wrapper function, returns the map width.
     *
     * @return The map width
     */
    public int getMapWidth() {
        return map != null ? map.getWidth() : 0;
    }

    /**
     * Wrapper function, returns the map height.
     *
     * @return The map height
     */
    public int getMapHeight() {
        return map != null ? map.getHeight() : 0;
    }

    /**
     * Wrapper function, returns every block the map contains.
     *
     * @return All the blocks of the map
     */
    public HashSet<Block> getBlocks() {
        return map.getBlocks() != null ? map.getBlocks() : new HashSet<>();
    }

    /**
     * Wrapper function, returns the flag of the map.
     *
     * @return The flag
     */
    public Flag getFlag() {
        return map.getFlag();
    }

    @Override
    protected void onMapDoesNotExist(String name) {
        UILog.showError("Die Karte " + name + " exisitert nicht!");
        Client.stop();
    }

    @Override
    protected void onMapLoadError(String name) {
        UILog.showError("Kann Karte " + name + " nicht laden!");
        Client.stop();
    }

    @Override
    protected void onMapLoadSuccess() {
        //spawn the player
        ENTITY_HOLDER.getUser().spawn();
    }
}
