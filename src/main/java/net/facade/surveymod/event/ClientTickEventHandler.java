package net.facade.surveymod.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.facade.surveymod.util.IEntityDataSaver;
import net.facade.surveymod.util.SurveyData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.text.DecimalFormat;


public class ClientTickEventHandler implements ClientTickEvents.EndTick {
    @Override
    public void onEndTick(MinecraftClient client) {
        PlayerEntity player = client.player;
        if (player == null) { return; }

        int surveyState = SurveyData.getSurveyState((IEntityDataSaver) player);
        if (surveyState == 0) { return; }

        int[] destination = SurveyData.getSurveyDestination((IEntityDataSaver) player);
        if (destination.length == 0) { return; }

        double positionToPointDifference = SurveyData.getSurveyPositionPointDifference((IEntityDataSaver) player);
        if (positionToPointDifference <= SurveyData.SURVEYTOLERANCE) {
            SurveyData.setSurveyDestination((IEntityDataSaver) player);
        }

        DecimalFormat df_obj = new DecimalFormat("#.##");
        player.sendMessage(Text.literal("Distance To Point: " + df_obj.format(positionToPointDifference)), false);

        Vec3d direction = SurveyData.getSurveyDirection((IEntityDataSaver) player, destination);
        player.applyMovementInput(direction, 0);
    }
}
