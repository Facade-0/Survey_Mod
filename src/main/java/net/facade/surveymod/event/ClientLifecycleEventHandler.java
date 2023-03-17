package net.facade.surveymod.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.facade.surveymod.util.IEntityDataSaver;
import net.facade.surveymod.util.SurveyData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class ClientLifecycleEventHandler implements ClientLifecycleEvents.ClientStarted {
    @Override
    public void onClientStarted(MinecraftClient client) {
        PlayerEntity player = client.player;
        if (player != null) {
            SurveyData.setSurveyState((IEntityDataSaver) player, 0);
        }
    }
}
