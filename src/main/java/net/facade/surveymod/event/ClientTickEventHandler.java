package net.facade.surveymod.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.facade.surveymod.util.IEntityDataSaver;
import net.facade.surveymod.util.SurveyData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class ClientTickEventHandler implements ClientTickEvents.EndTick {
    @Override
    public void onEndTick(MinecraftClient client) {
        PlayerEntity player = client.player;
        if (player != null) {
            int surveyState = SurveyData.getSurveyState((IEntityDataSaver) player);
            if (surveyState == 1) {
                player.sendMessage(Text.literal("Tock"), false);
            }
            //player.applyMovementInput(new Vec3d(0,10,0), 10);
        }
    }
}
