package de.schweinefilet.prototype.server.gui;

import com.esotericsoftware.kryonet.Connection;
import de.schweinefilet.prototype.common.entity.Player;
import de.schweinefilet.prototype.common.entity.Team;
import de.schweinefilet.prototype.common.network.packet.PacketChangeTeam;
import de.schweinefilet.prototype.common.network.packet.PacketKick;
import de.schweinefilet.prototype.common.util.LogUtil;
import de.schweinefilet.prototype.server.Server;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * The pop up menu. Currently can kick and change the team of a player.
 */
class PopUpMenu extends JPopupMenu {

    /**
     * Creates a new {@link PopUpMenu} with a kick and team change option.
     */
    PopUpMenu() {
        JMenuItem kickMenu = new JMenuItem("Kick");
        kickMenu.addActionListener(e -> {
            if (Server.GUI.hasSelectedPlayer()) {
                LogUtil.info("Kicked player " + Server.GUI.getSelectedPlayer());
                Connection connection = Server
                    .NETWORK_HANDLER
                    .getAffiliatedConnection(Server.GUI.getSelectedPlayer());
                connection.sendUDP(new PacketKick());
                connection.close();
                Server.updateGUI();
            }
        });
        add(kickMenu);

        JMenuItem changeTeamItem = new JMenuItem("Change Team");
        changeTeamItem.addActionListener(e -> {
            if (Server.GUI.hasSelectedPlayer()) {
                Player player = Server.GUI.getSelectedPlayer();
                switch(player.getTeam()) {

                    case BLUE:
                        player.setTeam(Team.RED);
                        break;
                    case RED:
                        player.setTeam(Team.BLUE);
                        break;
                }
                Connection connection = Server
                    .NETWORK_HANDLER
                    .getAffiliatedConnection(Server.GUI.getSelectedPlayer());
                connection.sendUDP(new PacketChangeTeam());
                LogUtil.info("Changed the team of " + player);
                Server.updateGUI();
            }
        });
        add(changeTeamItem);
    }
}
