package net.facade.surveymod.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.facade.surveymod.SurveyMod;
import net.facade.surveymod.networking.packet.MovePacket;
import net.minecraft.util.Identifier;

public class ModMessages {
    public static final Identifier EXAMPLE_ID_C2S = new Identifier(SurveyMod.MOD_ID, "example_c");
    public static final Identifier EXAMPLE_ID_S2C = new Identifier(SurveyMod.MOD_ID, "example_s");

    public static final Identifier MOVE_S2C = new Identifier(SurveyMod.MOD_ID, "move_s2c");

    public static void registerC2SPackets(){
        //ServerPlayNetworking.registerGlobalReceiver(EXAMPLE_ID_C2S, ExampleC2SPacket::receive);
    }
    public static void registerS2CPackets() {
        //ClientPlayNetworking.registerGlobalReceiver(EXAMPLE_ID_S2C, ExampleS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(MOVE_S2C, MovePacket::receive);
    }

}
