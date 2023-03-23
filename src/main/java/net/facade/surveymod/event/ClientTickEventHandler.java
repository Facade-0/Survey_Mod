package net.facade.surveymod.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.facade.surveymod.util.IEntityDataSaver;
import net.facade.surveymod.util.SurveyData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;

public class ClientTickEventHandler implements ClientTickEvents.EndTick {
    @Override
    public void onEndTick(MinecraftClient client) {
        PlayerEntity player = client.player;
        if (player == null) { return; }

        int surveyState = SurveyData.getSurveyState((IEntityDataSaver) player);
        if (surveyState == 0) { return; }

        int[] destination = SurveyData.getSurveyDestination((IEntityDataSaver) player);
        if (destination.length == 0) { return; }

        double positionPointDifference = SurveyData.getSurveyPositionPointDifference((IEntityDataSaver) player);
        if (positionPointDifference <= SurveyData.SURVEYTOLERANCE) {
            player.sendMessage(Text.literal("Done!"), false);
            SurveyData.setSurveyDestination((IEntityDataSaver) player);
        }
        player.sendMessage(Text.literal(String.valueOf(positionPointDifference)), false);
        Vec3d direction = SurveyData.getSurveyDirection((IEntityDataSaver) player, destination);
        player.applyMovementInput(new Vec3d(direction.x, direction.y, direction.z), 10);
    }
}
