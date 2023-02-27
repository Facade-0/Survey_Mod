package net.facade.surveymod.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.facade.surveymod.networking.ModMessages;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class SurveyData {
    public static void setSurveyState(IEntityDataSaver player, boolean value) {
        NbtCompound nbt = player.getPersistentData();
        boolean state = nbt.getBoolean("surveyState");
        if(state != value) {
            state = value;
        }

        nbt.putBoolean("surveyState", state);
        syncSurvey(state, (ServerPlayerEntity) player);
    }

    public static boolean getSurveyState(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        boolean state = nbt.getBoolean("surveyState");

        return state;
    }

    private static void syncSurvey(boolean state, ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(state);
        ServerPlayNetworking.send(player, ModMessages.SURVEY_SYNC_ID, buf);
    }
}
