package tropicsnowjpr.ringexchange;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.resources.Identifier;

import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tropicsnowjpr.ringexchange.command.RingExchangeCommands;

public class RingExchangeMod implements DedicatedServerModInitializer {
	public static final String MOD_ID = "ringexchange";
	public static final Logger LOGGER = LoggerFactory.getLogger("RingExchangeMod");

	public static MinecraftServer server;

	@Override
	public void onInitializeServer() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				RingExchangeCommands.register(dispatcher));

		LOGGER.info("RingExchange initialized");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
