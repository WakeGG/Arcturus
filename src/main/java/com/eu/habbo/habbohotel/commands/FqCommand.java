package com.eu.habbo.habbohotel.commands;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.rooms.RoomState;
import com.eu.habbo.messages.outgoing.generic.alerts.BubbleAlertComposer;
import com.eu.habbo.messages.outgoing.rooms.RoomSettingsUpdatedComposer;
import gnu.trove.map.hash.THashMap;

public class FqCommand extends Command {
    public  FqCommand() {
        super("cmd_alert", Emulator.getTexts().getValue("commands.keys.fq").split(";"));
    }
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        if (params.length > 0) {

            gameClient.getHabbo().getHabboInfo().getCurrentRoom().setState(RoomState.LOCKED);

            THashMap<String, String> keys = new THashMap();
            keys.put("display", "BUBBLE");
            keys.put("image", "${image.library.url}notifications/quarto_fechado.png");
            keys.put("message", "Este quarto foi fechado a todos os usuários");

            Room room = gameClient.getHabbo().getHabboInfo().getCurrentRoom();
            if (room != null) {
                room.sendComposer((new BubbleAlertComposer("", keys)).compose());
                return true;
            }
            gameClient.getHabbo().getHabboInfo().getCurrentRoom().sendComposer((new RoomSettingsUpdatedComposer(gameClient.getHabbo().getHabboInfo().getCurrentRoom())).compose());
            return true;


        }

        return false;
    }
}