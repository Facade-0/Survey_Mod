package net.facade.surveymod.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.util.InputUtil;

import java.util.function.BooleanSupplier;

public class MovePacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        int[] inputs = buf.readIntArray();
        Vec3d movementVector = new Vec3d(inputs[0], inputs[1], inputs[2]);
        PlayerEntity player = client.player;


        //player.applyMovementInput(movementVector, inputs[3]);

        //client.handleProfilerKeyPress(inputs[0]);

        player.sendMessage(Text.of("The MovePacket is not longer in use!"));//, false);

        /*Untested
        player.addVelocity();
        player.travel(movementVector);
        client.getServer().getThread();
        client.getServer().getTicks();
        client.getServer().getTickTime();
        client.getServer().getTaskCount();
        client.getServer().tickWorlds();
        */


        /*Tested
            client.tick();
            client.getServer().tick(() -> true);
            try {
                Thread.sleep(20);
            } catch (Exception e) {
                // Lol
            }
        */

        /*Thread movementThread = new Thread();

        for(int i = 0; i < 1000; i++) {
            player.applyMovementInput(movementVector, inputs[3]);
            try {
                movementThread.sleep(20);
            } catch (Exception e) {
                // Lol
            }
        }*/
    }

}
