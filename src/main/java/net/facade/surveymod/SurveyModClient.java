package net.facade.surveymod;

import net.fabricmc.api.ClientModInitializer;
import net.facade.surveymod.networking.ModMessages;

public class SurveyModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ModMessages.registerS2CPackets();
    }
}
