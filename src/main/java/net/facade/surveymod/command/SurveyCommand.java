package net.facade.surveymod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.facade.surveymod.util.IEntityDataSaver;
import net.facade.surveymod.util.SurveyData;
import net.minecraft.client.network.ClientPlayerEntity;
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
        dispatcher.register(CommandManager.literal("survey").executes(context -> {SurveyCommand.run(context, 0, -1); return 1;})
                .then(CommandManager.literal("view")
                    .executes(context -> {SurveyCommand.run(context, -1, -1); return 1;}))
                .then(CommandManager.literal("scan")
                    .then(CommandManager.literal("h")
                        .then(CommandManager.argument("offset", StringArgumentType.word())
                        .then(CommandManager.argument("points", StringArgumentType.greedyString())
                            .executes(context -> {SurveyCommand.run(context, 2, 10); return 1;}))))
                    .then(CommandManager.literal("v")
                        .then(CommandManager.argument("offset", StringArgumentType.word())
                        .then(CommandManager.argument("points", StringArgumentType.greedyString())
                            .executes(context -> {SurveyCommand.run(context, 2, 11); return 1;})))))
                .then(CommandManager.literal("spiral")
                    .then(CommandManager.literal("l")
                        .then(CommandManager.argument("offset", StringArgumentType.word())
                        .then(CommandManager.argument("points", StringArgumentType.greedyString())
                            .executes(context -> {SurveyCommand.run(context, 2, 20); return 1;}))))
                    .then(CommandManager.literal("r")
                        .then(CommandManager.argument("offset", StringArgumentType.word())
                        .then(CommandManager.argument("points", StringArgumentType.greedyString())
                            .executes(context -> {SurveyCommand.run(context, 2, 21); return 1;})))))
                .then(CommandManager.argument("points", StringArgumentType.greedyString())
                    .executes(context -> {SurveyCommand.run(context, 1, 0); return 1;})));
    }

    private static int run(CommandContext<ServerCommandSource> context, int parameterCount, int surveyType) {
        ServerPlayerEntity player = context.getSource().getPlayer();

        String surveyCommandInput = context.getInput();
        player.sendMessageToClient(Text.literal("Command Entry: " + surveyCommandInput), false);

        if (parameterCount == 0) {
            int state = 1 - SurveyData.getSurveyState((IEntityDataSaver) player);
            SurveyData.setSurveyState((IEntityDataSaver) player, state);
        } else if (parameterCount == -1) {
            String surveyInfo = SurveyData.getSurveyInfo((IEntityDataSaver) player);
            player.sendMessageToClient(Text.literal(surveyInfo), false);
        } else {
            String[] surveyParametersStrings = parseSurveyCommand(context, parameterCount);
            if (Objects.equals(surveyParametersStrings[0], "error")) {
                parseSurveyCommandError(player, surveyParametersStrings);
                return 1;
            }

            int[] surveyParametersTranslated = translateSurveyParameters(surveyParametersStrings, player);
            int surveyParametersOffset = surveyParametersTranslated[0];
            int[] surveyParametersPoints = Arrays.copyOfRange(surveyParametersTranslated, 1, surveyParametersTranslated.length);
            SurveyData.setSurveyOffset((IEntityDataSaver) player, surveyParametersOffset);
            SurveyData.setSurveyPoints((IEntityDataSaver) player, surveyParametersPoints);
            SurveyData.setSurveyDestination((IEntityDataSaver) player);
            SurveyData.setSurveyType((IEntityDataSaver) player, surveyType);
        }

        return 1;
    }

    private static String[] parseSurveyCommand(CommandContext<ServerCommandSource> context, int parameterCount) {
        ArrayList<String> parameterArray = new ArrayList<>();

        String parameterPoints = context.getArgument("points", String.class);
        String parameterOffset;
        if (parameterCount == 2) {
            parameterOffset = context.getArgument("offset", String.class);
            Pattern patternValidOffset = Pattern.compile("^\\d+$");
            Matcher matcherValidOffset = patternValidOffset.matcher(parameterOffset);
            if (matcherValidOffset.find()) { parameterArray.add(parameterOffset); }
            else { return new String[] {"error", "Invalid Offset", parameterOffset}; }
        }

        Pattern patternValidCharacters = Pattern.compile("^[,0-9 -;]+$", Pattern.CASE_INSENSITIVE);
        Matcher matcherValidCharacters = patternValidCharacters.matcher(parameterPoints);
        if (!matcherValidCharacters.find()) {return new String[] {"error", "Invalid Character"};}

        String stringOfAllPoints = parameterPoints.replaceAll("([ ;])+", ";");
        String[] stringArrayOfAllPoints = stringOfAllPoints.split(";");

        for (int i = 0; i < stringArrayOfAllPoints.length; i++) {
            Pattern patternTwoNumbers = Pattern.compile("^\\d+,\\d+$", Pattern.CASE_INSENSITIVE);
            Matcher matcherTwoNumbers = patternTwoNumbers.matcher(stringArrayOfAllPoints[i]);
            if (i == 0 && matcherTwoNumbers.find()) {
                return new String[]{"error", "Invalid Initial Coordinates", stringArrayOfAllPoints[i]};
            }
            if (matcherTwoNumbers.find()) {
                String[] pointXZ = stringArrayOfAllPoints[i].split(",");
                String previousY = stringArrayOfAllPoints[i-1].split(",")[1];
                stringArrayOfAllPoints[i] = pointXZ[0] + "," + previousY + "," + pointXZ[1];
                parameterArray.add(stringArrayOfAllPoints[i]);
            } else {
                Pattern patternThreeNumbers = Pattern.compile("^-?\\d+,-?\\d+,-?\\d+$", Pattern.CASE_INSENSITIVE);
                Matcher matcherThreeNumbers = patternThreeNumbers.matcher(stringArrayOfAllPoints[i]);
                if (!matcherThreeNumbers.find()) {return new String[] {"error", "Invalid Coordinates", stringArrayOfAllPoints[i]};}
                parameterArray.add(stringArrayOfAllPoints[i]);
            }
        }
        String[] surveyParameters = new String[parameterArray.size()];
        for (int i = 0; i < parameterArray.size(); i++) {
            surveyParameters[i] = parameterArray.get(i);
        }
        return surveyParameters;
    }

    private static void parseSurveyCommandError(ServerPlayerEntity player, String[] error) {
        String errorType = error[1];
        String errorData;
        SurveyData.setSurveyState((IEntityDataSaver) player, 0);

        switch (errorType) {
            case ("Invalid Character") ->
                    player.sendMessageToClient(Text.literal("Survey Command Error: Invalid character(s) in command.\nUse numbers, commas, and spaces (or semicolons) only."), false);
            case ("Invalid Initial Coordinates") -> {
                errorData = error[2];
                player.sendMessageToClient(Text.literal("Survey Command Error: Invalid initial coordinates in command:\n" + errorData + "\nYou must specify a \"Y\" coordinate."), false);
            }
            case ("Invalid Coordinates") -> {
                errorData = error[2];
                player.sendMessageToClient(Text.literal("Survey Command Error: Invalid coordinates in command:\n" + errorData), false);
            }
            case ("Invalid Offset") -> {
                errorData = error[2];
                player.sendMessageToClient(Text.literal("Survey Command Error: Invalid offset in command:\n" + errorData + "\nThe offset must be a number."), false);
            }
            default -> player.sendMessageToClient(Text.literal("Unknown Survey Command Error"), false);
        }
    }

    private static int[] translateSurveyParameters(String[] parameterStrings, ServerPlayerEntity debug) {
        /*  Possible string arrays to translate:   ["x,y,z"]   ["x,y,z", ..., "xn,yn,zn"]   ["o", "x,y,z"]   ["o", "x,y,z", ..., "xn,yn,zn"]*/

        int numberOfParameters = (parameterStrings[0].contains(",")) ? (parameterStrings.length * 3 + 1) : (parameterStrings.length * 3 - 2);
        int[] parameterInts = new int[numberOfParameters];
        String[] pointStrings = new String[numberOfParameters / 3 - 1];

        if (parameterStrings[0].contains(",")) {
            parameterInts[0] = 0;
            pointStrings = parameterStrings;
        } else {
            parameterInts[0] = Integer.parseInt(parameterStrings[0]);
            pointStrings = Arrays.copyOfRange(parameterStrings, 1, parameterStrings.length);
        }

        for (int i = 0; i < pointStrings.length; i++) {
            String[] coordinateSplit = pointStrings[i].split(",");
            for (int j = 0; j < 3; j++) {
                parameterInts[(i + 1) * 3 - 3 + j + 1] = Integer.parseInt(coordinateSplit[j]);
            }
        }

        //debug.sendMessageToClient(Text.literal("Debug Parameters: " + Arrays.toString(parameterInts)), false);
        return parameterInts;
    }



}
