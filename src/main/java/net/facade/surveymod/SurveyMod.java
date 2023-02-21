package net.facade.surveymod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.facade.surveymod.event.TestEventHandler;
import net.facade.surveymod.event.TestTickEventHandler;
import net.facade.surveymod.networking.ModMessages;
import net.facade.surveymod.util.ModRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurveyMod implements ModInitializer {
	public static final String MOD_ID = "surveymod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		ModRegistries.registerCommands();
		ModMessages.registerC2SPackets();

		AttackEntityCallback.EVENT.register(new TestEventHandler());
		ServerTickEvents.END_SERVER_TICK.register(new TestTickEventHandler());
	}
}
