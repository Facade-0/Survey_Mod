package net.facade.surveymod.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.facade.surveymod.networking.ModMessages;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.Vector;

public class SurveyData {

    public static final  String SURVEYSTATE = "surveyState";                // int
    public static final  String SURVEYPOINTS = "surveyPoints";              // int[]
    public static final  String SURVEYOFFSET = "surveyOffset";              // int
    public static final  String SURVEYTYPE = "surveyType";                  // int

    public static void setSurveyState(IEntityDataSaver player, int value) {
        NbtCompound nbt = player.getPersistentData();
        int state = nbt.getInt(SURVEYSTATE);
        if (state != value) {
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
        if (currentPoints != points) {
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

    public static void setSurveyOffset(IEntityDataSaver player, int offset) {
        NbtCompound nbt = player.getPersistentData();
        int currentOffset = nbt.getInt(SURVEYOFFSET);
        if (currentOffset != offset) {
            currentOffset = offset;
        }
        int[] offsetArray = {offset};

        nbt.putInt(SURVEYOFFSET, currentOffset);
        syncSurvey((ServerPlayerEntity) player, SURVEYOFFSET, offsetArray);
    }

    public static int getSurveyOffset(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        return nbt.getInt(SURVEYOFFSET);
    }

    public static void setSurveyType(IEntityDataSaver player, int type) {
        NbtCompound nbt = player.getPersistentData();
        int currentType = nbt.getInt(SURVEYTYPE);
        if (currentType != type) {
            currentType = type;
        }
        int[] typeArray = {type};

        nbt.putInt(SURVEYTYPE, currentType);
        syncSurvey((ServerPlayerEntity) player, SURVEYTYPE, typeArray);
    }

    public static int getSurveyType(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        return nbt.getInt(SURVEYTYPE);
    }

    public static Vec3d getPlayerPos(ClientPlayerEntity player) {
        return player.getPos();
    }

    public static Vec3d setPlayerDirection(ClientPlayerEntity player, int[] destination) {
        // destination - position -> unit
        Vec3d currentPoint = getPlayerPos(player);
        Vec3d destinationPoint = new Vec3d(destination[0], destination[1], destination[2]);
        return currentPoint.subtract(destinationPoint).normalize();
    }

    private static void syncSurvey(ServerPlayerEntity player, String dataID, int[] dataValues) {

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(dataID);
        buf.writeIntArray(dataValues);

        ServerPlayNetworking.send(player, ModMessages.SURVEY_SYNC_ID, buf);
    }
}
