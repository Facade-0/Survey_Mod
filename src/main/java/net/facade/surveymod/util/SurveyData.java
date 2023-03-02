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

    private String state = "surveyState";                // boolean
    private boolean stateValue = false;
    private String points = "surveyPoints";              // int[]
    private int[] pointsValue = null;

    public void setSurveyState(IEntityDataSaver player, boolean value) {
        NbtCompound nbt = player.getPersistentData();
        boolean state = nbt.getBoolean(this.state);
        if(state != value) {
            state = value;
        }
        this.stateValue = state;

        nbt.putBoolean(this.state, this.stateValue);
        syncSurvey((ServerPlayerEntity) player, this.stateValue, this.pointsValue);
    }

    public boolean getSurveyState(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        boolean state = nbt.getBoolean(this.state);

        return state;
    }
    public void setSurveyPoints(IEntityDataSaver player, int[] points) {
        NbtCompound nbt = player.getPersistentData();
        int[] currentPoint = nbt.getIntArray(this.points);
        if(currentPoint != points) {
            currentPoint = points;
        }
        this.pointsValue = points;

        nbt.putIntArray(this.points, this.pointsValue);
        syncSurvey((ServerPlayerEntity) player, this.stateValue, this.pointsValue);
    }

    public int[] getSurveyPoints(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        int[] points = nbt.getIntArray(this.points);

        return points;
    }

    public static Vec3d getPlayerPos(ClientPlayerEntity player) {
        return player.getPos();
    }

    private static void syncSurvey(ServerPlayerEntity player, Boolean state, int[] points) {

        /*switch (value.getClass()) {

        }*/

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(state);
        ServerPlayNetworking.send(player, ModMessages.SURVEY_SYNC_ID, buf);
    }
}
