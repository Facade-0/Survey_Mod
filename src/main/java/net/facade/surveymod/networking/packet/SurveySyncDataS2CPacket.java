package net.facade.surveymod.networking.packet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.facade.surveymod.util.IEntityDataSaver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class SurveySyncDataS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        String dataID = buf.readString();
        int[] dataValues = buf.readIntArray();

        switch (dataValues.length) {
            case 0:
                break;
            case 1:
                int dataValue = dataValues[0];
                ((IEntityDataSaver) client.player).getPersistentData().putInt(dataID, dataValue);
                break;
            default:
                ((IEntityDataSaver) client.player).getPersistentData().putIntArray(dataID, dataValues);
        }

    }
}
