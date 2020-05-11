package com.eu.habbo.messages.incoming.rooms.items;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.items.interactions.InteractionMoodLight;
import com.eu.habbo.habbohotel.modtool.ScripterManager;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.rooms.RoomMoodlightData;
import com.eu.habbo.habbohotel.users.HabboItem;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.rooms.items.MoodLightDataComposer;

import java.util.Arrays;
import java.util.List;

public class MoodLightSaveSettingsEvent extends MessageHandler {
    public static List<String> MOODLIGHT_AVAILABLE_COLORS = Arrays.asList("#74F5F5,#0053F7,#E759DE,#EA4532,#F2F851,#82F349,#000000".split(","));

    @Override
    public void handle() throws Exception {
        Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();

        if ((room.getGuildId() <= 0 && room.guildRightLevel(this.client.getHabbo()) < 2) && !room.hasRights(this.client.getHabbo()))
            return;

        int id = this.packet.readInt();
        int backgroundOnly = this.packet.readInt();
        String color = this.packet.readString();
        int brightness = this.packet.readInt();
        boolean apply = this.packet.readBoolean();

        if (!MOODLIGHT_AVAILABLE_COLORS.contains(color)) {
            ScripterManager.scripterDetected(this.client, "O usuário tentou definir um moodlight para uma cor que não esteja na lista de permissões: " + color);
            return;
        }

        if (brightness > 0xFF || brightness < (0.2 * 0xFF)) {
            ScripterManager.scripterDetected(this.client, "O usuário tentou definir o brilho de um modo fora dos limites ([76, 255]): " + brightness);
            return;
        }

        for (RoomMoodlightData data : room.getMoodlightData().valueCollection()) {
            if (data.getId() == id) {
                data.setBackgroundOnly(backgroundOnly == 2);
                data.setColor(color);
                data.setIntensity(brightness);
                if (apply) data.enable();

                for (HabboItem item : room.getRoomSpecialTypes().getItemsOfType(InteractionMoodLight.class)) {
                    item.setExtradata(data.toString());
                    item.needsUpdate(true);
                    room.updateItem(item);
                    Emulator.getThreading().run(item);
                }
            } else if (apply) {
                data.disable();
            }
        }

        room.setNeedsUpdate(true);
        this.client.sendResponse(new MoodLightDataComposer(room.getMoodlightData()));
    }
}
