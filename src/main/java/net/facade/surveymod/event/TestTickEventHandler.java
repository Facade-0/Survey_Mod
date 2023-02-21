package net.facade.surveymod.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

public class TestTickEventHandler implements ServerTickEvents.EndTick {

    @Override
    public void onEndTick(MinecraftServer server) {
        //server.sendMessage(Text.literal("tick"));
        server.getPlayerManager().broadcast(Text.literal("Tick"), false);
    }
}
