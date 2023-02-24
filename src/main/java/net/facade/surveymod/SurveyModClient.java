package net.facade.surveymod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.facade.surveymod.event.TestClientTickEventHandler;
import net.facade.surveymod.networking.ModMessages;

public class SurveyModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ModMessages.registerS2CPackets();
        ClientTickEvents.END_CLIENT_TICK.register(new TestClientTickEventHandler());
    }
}
