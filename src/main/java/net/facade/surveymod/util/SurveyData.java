package net.facade.surveymod.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.facade.surveymod.networking.ModMessages;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class SurveyData {

    public static final  String SURVEYSTATE = "surveyState";                // boolean
    public static final  String SURVEYPOINTS = "surveyPoints";              // int[]

    public static void setSurveyState(IEntityDataSaver player, int value) {
        NbtCompound nbt = player.getPersistentData();
        int state = nbt.getInt(SURVEYSTATE);
        if(state != value) {
            state = value;
        }
        int[] stateArray = {state};

        nbt.putInt(SURVEYSTATE, state);
        syncSurvey((ServerPlayerEntity) player, SURVEYSTATE, stateArray);
    }

    public static int getSurveyState(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        return nbt.getInt(SURVEYSTATE);
    }

    public static void setSurveyPoints(IEntityDataSaver player, int[] points) {
        NbtCompound nbt = player.getPersistentData();
        int[] currentPoints = nbt.getIntArray(SURVEYPOINTS);
        if(currentPoints != points) {
            currentPoints = points;
        }
        currentPoints = points;

        nbt.putIntArray(SURVEYPOINTS, currentPoints);
        syncSurvey((ServerPlayerEntity) player, SURVEYPOINTS, currentPoints);
    }

    public static int[] getSurveyPoints(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        return nbt.getIntArray(SURVEYPOINTS);
    }

    public static Vec3d getPlayerPos(ClientPlayerEntity player) {
        return player.getPos();
    }

    private static void syncSurvey(ServerPlayerEntity player, String dataID, int[] dataValues) {

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(dataID);
        buf.writeIntArray(dataValues);

        ServerPlayNetworking.send(player, ModMessages.SURVEY_SYNC_ID, buf);
    }
}
