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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SurveyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("survey").executes(context -> {SurveyCommand.run(context, 0, 0); return 1;})
                .then(CommandManager.literal("on").executes(context -> {SurveyCommand.run(context, 1, 0); return 1;}))
                .then(CommandManager.literal("off").executes(context -> {SurveyCommand.run(context, 0, 0); return 1;}))
                .then(CommandManager.literal("scan")
                    .then(CommandManager.literal("v")
                        .then(CommandManager.argument("offset", StringArgumentType.word())
                        .then(CommandManager.argument("points", StringArgumentType.greedyString())
                            .executes(context -> {SurveyCommand.run(context, 1, 2); return 1;}))))
                    .then(CommandManager.literal("h")
                        .then(CommandManager.argument("offset", StringArgumentType.word())
                        .then(CommandManager.argument("points", StringArgumentType.greedyString())
                            .executes(context -> {SurveyCommand.run(context, 1, 2); return 1;})))))
                .then(CommandManager.literal("spiral")
                    .then(CommandManager.literal("l")
                        .then(CommandManager.argument("offset", StringArgumentType.word())
                        .then(CommandManager.argument("points", StringArgumentType.greedyString())
                            .executes(context -> {SurveyCommand.run(context, 1, 2); return 1;}))))
                    .then(CommandManager.literal("r")
                        .then(CommandManager.argument("offset", StringArgumentType.word())
                        .then(CommandManager.argument("points", StringArgumentType.greedyString())
                            .executes(context -> {SurveyCommand.run(context, 1, 2); return 1;})))))
                .then(CommandManager.argument("points", StringArgumentType.greedyString())
                    .executes(context -> {SurveyCommand.run(context, 1, 1); return 1;})));
    }

    private static int run(CommandContext<ServerCommandSource> context, int state, int parameterCount) {
        ServerPlayerEntity player = context.getSource().getPlayer();


        String CommandParams = context.getInput();
        //String CommandParams2 = context.getArgument("offset", String.class);

        SurveyData.setSurveyState((IEntityDataSaver) player, state);
        player.sendMessageToClient(Text.literal("Command Entry: " + CommandParams), false);

        if(parameterCount > 0) {
            String[] surveyParameters = parseSurveyCommand(context, parameterCount);
            if(Objects.equals(surveyParameters[0], "error")) {
                parseSurveyCommandError(player, surveyParameters);
                return 1;
            }
            player.sendMessageToClient(Text.literal("Command Parameters: " + Arrays.toString(surveyParameters)), false);
        }

        return 1;
    }

    private static String[] parseSurveyCommand(CommandContext<ServerCommandSource> context, int parameterCount) {
        ArrayList<String> parameterArray = new ArrayList<>();

        String parameterPoints = context.getArgument("points", String.class);
        String parameterOffset;
        if(parameterCount == 2) {
            parameterOffset = context.getArgument("offset", String.class);
            Pattern patternValidOffset = Pattern.compile("^\\d+$");
            Matcher matcherValidOffset = patternValidOffset.matcher(parameterOffset);
            if(matcherValidOffset.find()) { parameterArray.add(parameterOffset); }
            else { return new String[] {"error", "Invalid Offset", parameterOffset}; }
        }

        Pattern patternValidCharacters = Pattern.compile("^[,0-9 -;]+$", Pattern.CASE_INSENSITIVE);
        Matcher matcherValidCharacters = patternValidCharacters.matcher(parameterPoints);
        if(!matcherValidCharacters.find()) {return new String[] {"error", "Invalid Character"};}

        String stringOfAllPoints = parameterPoints.replaceAll("([ ;])+", ";");
        String[] stringArrayOfAllPoints = stringOfAllPoints.split(";");

        for(int i = 0; i < stringArrayOfAllPoints.length; i++) {
            Pattern patternTwoNumbers = Pattern.compile("^\\d+,\\d+$", Pattern.CASE_INSENSITIVE);
            Matcher matcherTwoNumbers = patternTwoNumbers.matcher(stringArrayOfAllPoints[i]);
            if(i == 0 && matcherTwoNumbers.find()) {
                return new String[]{"error", "Invalid Initial Coordinates", stringArrayOfAllPoints[i]};
            }
            if(matcherTwoNumbers.find()) {
                String[] pointXZ = stringArrayOfAllPoints[i].split(",");
                String previousY = stringArrayOfAllPoints[i-1].split(",")[1];
                stringArrayOfAllPoints[i] = pointXZ[0] + "," + previousY + "," + pointXZ[1];
                parameterArray.add(stringArrayOfAllPoints[i]);
            } else {
                Pattern patternThreeNumbers = Pattern.compile("^\\d+,\\d+,\\d+$", Pattern.CASE_INSENSITIVE);
                Matcher matcherThreeNumbers = patternThreeNumbers.matcher(stringArrayOfAllPoints[i]);
                if(!matcherThreeNumbers.find()) {return new String[] {"error", "Invalid Coordinates", stringArrayOfAllPoints[i]};}
                parameterArray.add(stringArrayOfAllPoints[i]);
            }
        }
        String[] surveyParameters = new String[parameterArray.size()];
        for(int i = 0; i < parameterArray.size(); i++) {
            surveyParameters[i] = parameterArray.get(i);
        }
        return surveyParameters;
    }

    private static void parseSurveyCommandError(ServerPlayerEntity player, String[] error) {
        String errorType = error[1];
        String errorData;
        SurveyData.setSurveyState((IEntityDataSaver) player, 0);

        switch (errorType) {
            case("Invalid Character"):
                player.sendMessageToClient(Text.literal("Survey Command Error: Invalid character(s) in command.\nUse numbers, commas, and spaces (or semicolons) only."), false);
                break;
            case("Invalid Initial Coordinates"):
                errorData = error[2];
                player.sendMessageToClient(Text.literal("Survey Command Error: Invalid initial coordinates in command:\n" + errorData + "\nYou must specify a \"Y\" coordinate."), false);
                break;
            case("Invalid Coordinates"):
                errorData = error[2];
                player.sendMessageToClient(Text.literal("Survey Command Error: Invalid coordinates in command:\n" + errorData), false);
                break;
            case("Invalid Offset"):
                errorData = error[2];
                player.sendMessageToClient(Text.literal("Survey Command Error: Invalid offset in command:\n" + errorData + "\nThe offset must be a number."), false);
                break;
            default:
                player.sendMessageToClient(Text.literal("Unknown Survey Command Error"), false);
        }
    }


}
