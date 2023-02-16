package net.facade.surveymod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.facade.surveymod.networking.ModMessages;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class SurveyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("survey")
                .then(CommandManager.literal("set")
                .then(CommandManager.argument("surveyParameters", StringArgumentType.greedyString())
                        .executes(SurveyCommand::run))));
    }

    private static int run(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();

        movePlayer(player);
        return 1;
    }

    private static void movePlayer(ServerPlayerEntity player) {
        PacketByteBuf buf = bufWritter(new int[]{0, 0, 10, 0});

        for(int i = 0; i < 600; i++) {
            ServerPlayNetworking.send(player, ModMessages.MOVE_S2C, buf);
            try {
                Thread.sleep(20);
            } catch (Exception e) {
                // Lol
            }
        }
    }

    private static PacketByteBuf bufWritter(int[] inputs) {
        PacketByteBuf buf = PacketByteBufs.create();
        for(int i = 0; i < inputs.length; i++) {
            buf.writeIntArray(inputs);
        }
        return buf;
    }




}
