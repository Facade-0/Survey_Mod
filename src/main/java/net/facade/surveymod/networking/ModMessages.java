package net.facade.surveymod.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.facade.surveymod.SurveyMod;
import net.facade.surveymod.networking.packet.SurveySyncDataS2CPacket;
import net.minecraft.util.Identifier;

public class ModMessages {
    public static final Identifier SURVEY_SYNC_ID = new Identifier(SurveyMod.MOD_ID, "survey_sync");

    public static void registerC2SPackets(){
        //ServerPlayNetworking.registerGlobalReceiver(...);
    }
    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(SURVEY_SYNC_ID, SurveySyncDataS2CPacket::receive);
    }

}
