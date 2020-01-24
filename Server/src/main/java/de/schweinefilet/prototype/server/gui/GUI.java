package de.schweinefilet.prototype.server.gui;

import static de.schweinefilet.prototype.server.Server.MAP_HOLDER;

import de.schweinefilet.prototype.common.entity.Player;
import de.schweinefilet.prototype.server.map.ServerMapHolder;
import java.awt.Scrollbar;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import lombok.Getter;

/**
 * The server's graphical user interface. Can kick a player and/or changes his team.
 */
public class GUI {

    @Getter
    private JPanel panel;
    /**
     * The player list.
     */
    private JList<Object> list;

    public GUI() {
        panel = new JPanel();
        list = new JList<>(new DefaultListModel<>());
        list.addMouseListener(new PopClickListener());

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);
        Scrollbar scrollbar = new Scrollbar(Scrollbar.VERTICAL);
        list.add(scrollbar);
        panel.add(list);
        panel.add(scrollbar);
    }

    /**
     * Updates the {@link GUI#list} with {@link ServerMapHolder#players}.
     */
    public void update() {
        if (MAP_HOLDER.getPlayers() != null && list != null) {
            list.setListData(new Object[1]);
            list.setListData(MAP_HOLDER.getPlayers().toArray());
        }
    }

    /**
     * Returns the selected player based on his {@link java.util.UUID}.
     *
     * @return the selected player
     */
    Player getSelectedPlayer() {
        return MAP_HOLDER.getPlayer(((Player) list.getSelectedValue()).getUniqueId());
    }

    /**
     * Checks if the GUI user has selected a player.
     *
     * @return true, if a player is selected, else false
     */
    boolean hasSelectedPlayer() {
        return list.getSelectedIndex() != -1;
    }
}
