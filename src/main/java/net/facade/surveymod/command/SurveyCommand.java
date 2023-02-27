package net.facade.surveymod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.facade.surveymod.event.TestClientTickEventHandler;
import net.facade.surveymod.event.TestServerTickEventHandler;
import net.facade.surveymod.networking.ModMessages;
import net.facade.surveymod.util.IEntityDataSaver;
import net.facade.surveymod.util.SurveyData;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;

public class SurveyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("survey")
                .then(CommandManager.literal("on").executes(context -> {SurveyCommand.run(context, true); return 1;}))
                .then(CommandManager.literal("off").executes(context -> {SurveyCommand.run(context, false); return 1;}))
                .then(CommandManager.literal("set")
                .then(CommandManager.argument("surveyParameters", StringArgumentType.greedyString())
                        .executes(context -> {SurveyCommand.run(context, true); return 1;}))));
    }

    private static int run(CommandContext<ServerCommandSource> context, boolean state) {
        ServerPlayerEntity player = context.getSource().getPlayer();

        String surveyParameters = context.getInput();
        player.sendMessageToClient(Text.literal(surveyParameters), false);

        SurveyData.setSurveyState((IEntityDataSaver) player, state);

        //movePlayer(player);
        return 1;
    }

/*    private static void movePlayer(ServerPlayerEntity player) {
        PacketByteBuf buf = bufWritter(new int[]{0, 0, 1000, 0});
        ServerPlayNetworking.send(player, ModMessages.MOVE_S2C, buf);

        *//*for(int i = 0; i < 1000; i++) {
            ServerPlayNetworking.send(player, ModMessages.MOVE_S2C, buf);
            try {
                Thread.sleep(20);
            } catch (Exception e) {
                // Lol
            }
        }*//*

        Thread movementThread = new Thread();

        for(int i = 0; i < 1; i++) {
            buf = bufWritter(new int[]{i, 0, 0, 0});
            ServerPlayNetworking.send(player, ModMessages.MOVE_S2C, buf);
            try {
                movementThread.sleep(50);
            } catch (Exception e) {
                // Lol
            }
        }
        player.sendMessageToClient(Text.of("DONE!"), false);

    }

    private static PacketByteBuf bufWritter(int[] inputs) {
        PacketByteBuf buf = PacketByteBufs.create();
        for(int i = 0; i < inputs.length; i++) {
            buf.writeIntArray(inputs);
        }
        return buf;
    }*/




}
