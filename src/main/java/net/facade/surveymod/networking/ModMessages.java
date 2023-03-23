package net.facade.surveymod.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.facade.surveymod.SurveyMod;
import net.facade.surveymod.networking.packet.SurveySyncDataC2SPacket;
import net.facade.surveymod.networking.packet.SurveySyncDataS2CPacket;
import net.minecraft.util.Identifier;

public class ModMessages {
    public static final Identifier SURVEY_SYNC_ID_S = new Identifier(SurveyMod.MOD_ID, "survey_sync_s");
    public static final Identifier SURVEY_SYNC_ID_C = new Identifier(SurveyMod.MOD_ID, "survey_sync_c");

    public static void registerC2SPackets(){
        ServerPlayNetworking.registerGlobalReceiver(SURVEY_SYNC_ID_C, SurveySyncDataC2SPacket::receive);
    }
    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(SURVEY_SYNC_ID_S, SurveySyncDataS2CPacket::receive);
    }

}
