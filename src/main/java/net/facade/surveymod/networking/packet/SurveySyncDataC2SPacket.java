package net.facade.surveymod.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.facade.surveymod.util.IEntityDataSaver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SurveySyncDataC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        String dataID = buf.readString();
        int[] dataValues = buf.readIntArray();
        player.sendMessageToClient(Text.literal("We're in"), false);

        switch (dataValues.length) {
            case 0:
                break;
            case 1:
                int dataValue = dataValues[0];
                ((IEntityDataSaver) player).getPersistentData().putInt(dataID, dataValue);
                break;
            default:
                ((IEntityDataSaver) player).getPersistentData().putIntArray(dataID, dataValues);
        }
    }
}
