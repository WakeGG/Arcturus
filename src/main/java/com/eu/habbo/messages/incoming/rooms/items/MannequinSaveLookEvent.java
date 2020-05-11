package com.eu.habbo.messages.incoming.rooms.items;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.achievements.AchievementManager;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.users.HabboItem;
import com.eu.habbo.messages.incoming.MessageHandler;

public class MannequinSaveLookEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();


        HabboItem item = room.getHabboItem(this.packet.readInt());
        if (item == null)
            return;

        if (room.getOwnerId() == this.client.getHabbo().getHabboInfo().getId() || room.hasRights(this.client.getHabbo()) || this.client.getHabbo().hasPermission("acc_placefurni")) {


            String[] data = item.getExtradata().split(":");
            //TODO: Only clothing not whole body part.

            StringBuilder look = new StringBuilder();

            for (String s : this.client.getHabbo().getHabboInfo().getLook().split("\\.")) {
                if (!s.contains("hr") && !s.contains("hd") && !s.contains("he") && !s.contains("ea") && !s.contains("ha") && !s.contains("fa")) {
                    look.append(s).append(".");
                }
            }

            if (look.length() > 0) {
                look = new StringBuilder(look.substring(0, look.length() - 1));
            }

            if (data.length == 3) {
                item.setExtradata(this.client.getHabbo().getHabboInfo().getGender().name().toLowerCase() + ":" + look + ":" + data[2]);
            } else {
                item.setExtradata(this.client.getHabbo().getHabboInfo().getGender().name().toLowerCase() + ":" + look + ":" + this.client.getHabbo().getHabboInfo().getUsername() + "'s visual.");
            }

            item.needsUpdate(true);
            Emulator.getThreading().run(item);
            room.updateItem(item);
            AchievementManager.progressAchievement(this.client.getHabbo(), Emulator.getGameEnvironment().getAchievementManager().getAchievement("SelfMannequin"));
        }
    }
}