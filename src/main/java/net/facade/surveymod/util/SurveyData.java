package net.facade.surveymod.util;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.facade.surveymod.networking.ModMessages;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.text.DecimalFormat;
import java.util.Arrays;

public class SurveyData {

    public static final  String SURVEYSTATE = "surveyState";                // int
    public static final  String SURVEYPOINTS = "surveyPoints";              // int[]
    public static final  String SURVEYDESTINAITON = "surveyDestination";    // int[]
    public static final  float  SURVEYTOLERANCE = 0.25f;                    // float
    public static final  String SURVEYTYPE = "surveyType";                  // int
    public static final  String SURVEYOFFSET = "surveyOffset";              // int

    public static void setSurveyState(IEntityDataSaver player, int value) {
        NbtCompound nbt = player.getPersistentData();
        int state = nbt.getInt(SURVEYSTATE);
        if (state != value) {
            state = value;
        }
        int[] stateArray = {state};

        nbt.putInt(SURVEYSTATE, state);
        syncSurvey(player, SURVEYSTATE, stateArray);
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
        syncSurvey(player, SURVEYPOINTS, currentPoints);
    }

    public static int[] getSurveyPoints(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        return nbt.getIntArray(SURVEYPOINTS);
    }

    public static void setSurveyDestination(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        int[] currentDestination = new int[3];
        int[] currentPoints = getSurveyPoints(player);

        if (currentPoints.length == 0) {
            currentDestination = new int[0];
            nbt.putIntArray(SURVEYDESTINAITON, currentDestination);
            setSurveyState(player, 0);
        } else if (currentPoints.length == 3) {
            currentDestination = currentPoints;
            currentPoints = new int[0];
            nbt.putIntArray(SURVEYPOINTS, currentPoints);
            nbt.putIntArray(SURVEYDESTINAITON, currentDestination);
            syncSurvey(player, SURVEYPOINTS, currentPoints);
        } else {
            System.arraycopy(currentPoints, 0, currentDestination, 0, 3);
            int[] newPoints = new int[currentPoints.length - 3];
            System.arraycopy(currentPoints, 3, newPoints, 0, newPoints.length);
            nbt.putIntArray(SURVEYPOINTS, newPoints);
            nbt.putIntArray(SURVEYDESTINAITON, currentDestination);
            syncSurvey(player, SURVEYPOINTS, newPoints);
        }
        syncSurvey(player, SURVEYDESTINAITON, currentDestination);
    }

    public static int[] getSurveyDestination(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        return nbt.getIntArray(SURVEYDESTINAITON);
    }

    public static void setSurveyOffset(IEntityDataSaver player, int offset) {
        NbtCompound nbt = player.getPersistentData();
        int currentOffset = nbt.getInt(SURVEYOFFSET);
        if (currentOffset != offset) {
            currentOffset = offset;
        }
        int[] offsetArray = {offset};

        nbt.putInt(SURVEYOFFSET, currentOffset);
        syncSurvey(player, SURVEYOFFSET, offsetArray);
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
        syncSurvey(player, SURVEYTYPE, typeArray);
    }

    public static int getSurveyType(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        return nbt.getInt(SURVEYTYPE);
    }

    public static Vec3d getPlayerPos(IEntityDataSaver player) {
        try {
            return ((ClientPlayerEntity) player).getPos();
        } catch (Exception e) {
            return ((ServerPlayerEntity) player).getPos();
        }
    }

    public static Vec3d getSurveyDirection(IEntityDataSaver player, int[] destination) {
        // destination - position -> unit
        Vec3d currentPoint = getPlayerPos(player);
        Vec3d destinationPoint = new Vec3d(destination[0], destination[1], destination[2]);
        //return currentPoint.subtract(destinationPoint).normalize();
        return destinationPoint.subtract(currentPoint).normalize();
    }

    public static double getSurveyPositionPointDifference(IEntityDataSaver player) {
        int[] destinationInts = getSurveyDestination(player);
        Vec3d destination = new Vec3d(destinationInts[0], destinationInts[1], destinationInts[2]);
        Vec3d position = SurveyData.getPlayerPos(player);
        Vec3d difference = position.subtract(destination);
        return Math.sqrt((difference.x * difference.x) + (difference.y * difference.y) + (difference.z * difference.z));
    }

    private static void syncSurvey(IEntityDataSaver player, String dataID, int[] dataValues) {

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(dataID);
        buf.writeIntArray(dataValues);

        try {
            ServerPlayNetworking.send((ServerPlayerEntity) player, ModMessages.SURVEY_SYNC_ID_S, buf);
        } catch (Exception e) {
            ClientPlayNetworking.send(ModMessages.SURVEY_SYNC_ID_C, buf);
        }
    }

    public static String[] getSurveyInfo(IEntityDataSaver player) {
        int state = getSurveyState(player);
        int[] points = getSurveyPoints(player);
        int[] destination = getSurveyDestination(player);
        int type = getSurveyType(player);
        int offset = getSurveyOffset(player);
        float tolerance = SURVEYTOLERANCE;

        DecimalFormat df_obj = new DecimalFormat("#.##");
        Vec3d positionVector = getPlayerPos(player);
        String[] position = {df_obj.format(positionVector.x), df_obj.format(positionVector.y), df_obj.format(positionVector.z)};

        String[] surveyInfo = {"Survey State: ", String.valueOf(state), "Survey Points: ", Arrays.toString(points),
                               "Survey Destination: ", Arrays.toString(destination), "Current Position: ", Arrays.toString(position),
                               "Survey Type: ", String.valueOf(type), "Survey Offset: ", String.valueOf(offset),
                               "Survey Tolerance: ", String.valueOf(tolerance)};
        return surveyInfo;
    }
}
