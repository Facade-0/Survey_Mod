package net.facade.surveymod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.facade.surveymod.event.ClientLifecycleEventHandler;
import net.facade.surveymod.event.ClientTickEventHandler;
import net.facade.surveymod.networking.ModMessages;
import net.minecraft.util.Identifier;

public class SurveyModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ModMessages.registerS2CPackets();
        ClientLifecycleEvents.CLIENT_STARTED.register(new ClientLifecycleEventHandler());
        ClientTickEvents.END_CLIENT_TICK.register(new ClientTickEventHandler());
    }
}
