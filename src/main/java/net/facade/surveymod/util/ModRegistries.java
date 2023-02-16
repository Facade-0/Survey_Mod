package net.facade.surveymod.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.facade.surveymod.command.SurveyCommand;

public class ModRegistries {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(SurveyCommand::register);
    }
}
