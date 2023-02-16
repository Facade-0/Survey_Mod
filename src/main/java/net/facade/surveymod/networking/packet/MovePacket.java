package net.facade.surveymod.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

public class MovePacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        int[] inputs = buf.readIntArray();
        Vec3d movementVector = new Vec3d(inputs[0], inputs[1], inputs[2]);
        PlayerEntity player = client.player;
        player.applyMovementInput(movementVector, inputs[3]);

//        try {
//            Thread.sleep(100);
//        } catch (Exception e) {
//            // lol
//        }
    }

}
