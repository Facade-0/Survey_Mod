package net.facade.surveymod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.facade.surveymod.util.IEntityDataSaver;
import net.facade.surveymod.util.SurveyData;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SurveyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("survey").executes(context -> {SurveyCommand.run(context, 0); return 1;})
                .then(CommandManager.literal("on").executes(context -> {SurveyCommand.run(context, 1); return 1;}))
                .then(CommandManager.literal("off").executes(context -> {SurveyCommand.run(context, 0); return 1;}))
                .then(CommandManager.literal("scan")
                    .then(CommandManager.literal("v")
                        .then(CommandManager.argument("surveyParameters", StringArgumentType.greedyString())
                            .executes(context -> {SurveyCommand.run(context, 1); return 1;})))
                    .then(CommandManager.literal("h")
                        .then(CommandManager.argument("surveyParameters", StringArgumentType.greedyString())
                            .executes(context -> {SurveyCommand.run(context, 1); return 1;}))))
                .then(CommandManager.literal("spiral")
                    .then(CommandManager.literal("l")
                        .then(CommandManager.argument("surveyParameters", StringArgumentType.greedyString())
                            .executes(context -> {SurveyCommand.run(context, 1); return 1;})))
                    .then(CommandManager.literal("r")
                        .then(CommandManager.argument("surveyParameters", StringArgumentType.greedyString())
                            .executes(context -> {SurveyCommand.run(context, 1); return 1;}))))
                .then(CommandManager.argument("surveyParameters", StringArgumentType.greedyString())
                    .executes(context -> {SurveyCommand.run(context, 1); return 1;})));                    // NEED TO ADD OFFSET PARAM SOMEWHERE
    }

    private static int run(CommandContext<ServerCommandSource> context, int state) {
        ServerPlayerEntity player = context.getSource().getPlayer();

        String surveyParameters = context.getInput();
        player.sendMessageToClient(Text.literal(surveyParameters), false);

        SurveyData.setSurveyState((IEntityDataSaver) player, state);

        return 1;
    }

}
