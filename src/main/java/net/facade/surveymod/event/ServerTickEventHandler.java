package net.facade.surveymod.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class ServerTickEventHandler implements ServerTickEvents.EndTick {

    @Override
    public void onEndTick(MinecraftServer server) {
        //server.sendMessage(Text.literal("tick"));
        //server.getPlayerManager().broadcast(Text.literal("Tick"), false);
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
        for (ServerPlayerEntity player:players) {
            //player.sendMessageToClient(Text.literal("Tick"), false);
            //player.sendMessageToClient(Text.literal(String.valueOf(SurveyData.getSurveyState((IEntityDataSaver) player))), false);
        }
    }
}
