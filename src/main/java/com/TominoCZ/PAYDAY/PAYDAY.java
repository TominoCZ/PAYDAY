package com.TominoCZ.PAYDAY;

import java.io.File;

import com.TominoCZ.PAYDAY.block.LobbyBlock;
import com.TominoCZ.PAYDAY.block.LobbyTileEntity;
import com.TominoCZ.PAYDAY.handler.LobbyBeginGamePacketHandler;
import com.TominoCZ.PAYDAY.handler.LobbyPlayerOpenedGuiPacketHandler;
import com.TominoCZ.PAYDAY.handler.MConfigHandler;
import com.TominoCZ.PAYDAY.handler.MEventHandler;
import com.TominoCZ.PAYDAY.handler.MGuiHandler;
import com.TominoCZ.PAYDAY.handler.PacketSyncPlayerPropertiesClientHandler;
import com.TominoCZ.PAYDAY.handler.PacketSyncPlayerPropertiesServerHandler;
import com.TominoCZ.PAYDAY.handler.PacketSyncTileEntityClientHandler;
import com.TominoCZ.PAYDAY.handler.PacketSyncTileEntityServerHandler;
import com.TominoCZ.PAYDAY.packet.LobbyBeginGamePacket;
import com.TominoCZ.PAYDAY.packet.LobbyPlayerOpenedGuiPacket;
import com.TominoCZ.PAYDAY.packet.PacketSyncPlayerPropertiesClient;
import com.TominoCZ.PAYDAY.packet.PacketSyncPlayerPropertiesServer;
import com.TominoCZ.PAYDAY.packet.PacketSyncTileEntityClient;
import com.TominoCZ.PAYDAY.packet.PacketSyncTileEntityServer;
import com.TominoCZ.PAYDAY.util.DimensionUtil;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = PAYDAY.MODID, name = PAYDAY.NAME, version = PAYDAY.VERSION)
public class PAYDAY {
	@Instance(PAYDAY.MODID)
	public static PAYDAY instance;

	public final static String MODID = "payday";
	public final static String NAME = "PAYDAY Mod";
	public final static String VERSION = "0.0.2";
	public static File config;

	public static int dimensionID = 0;
	public static int chunkProviderID = 0;

	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(PAYDAY.MODID);

	public static Block lobbyBlock = new LobbyBlock();

	public static MEventHandler eventHandler = new MEventHandler();

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		config = new File(evt.getModConfigurationDirectory() + "/PAYDAY.cfg");

		MConfigHandler.init();
	}

	@EventHandler
	public void init(FMLInitializationEvent evt) {
		INSTANCE.registerMessage(LobbyPlayerOpenedGuiPacketHandler.class, LobbyPlayerOpenedGuiPacket.class, 0,
				Side.SERVER);

		INSTANCE.registerMessage(LobbyBeginGamePacketHandler.class, LobbyBeginGamePacket.class, 1, Side.SERVER);
		INSTANCE.registerMessage(PacketSyncPlayerPropertiesClientHandler.class, PacketSyncPlayerPropertiesClient.class,
				2, Side.CLIENT);
		INSTANCE.registerMessage(PacketSyncPlayerPropertiesServerHandler.class, PacketSyncPlayerPropertiesServer.class,
				3, Side.SERVER);

		INSTANCE.registerMessage(PacketSyncTileEntityServerHandler.class, PacketSyncTileEntityServer.class, 4,
				Side.SERVER);
		INSTANCE.registerMessage(PacketSyncTileEntityClientHandler.class, PacketSyncTileEntityClient.class, 5,
				Side.CLIENT);

		NetworkRegistry.INSTANCE.registerGuiHandler(PAYDAY.instance, new MGuiHandler());
		GameRegistry.registerBlock(lobbyBlock, "Lobby");
		GameRegistry.registerTileEntity(LobbyTileEntity.class, "lobby_tile_entity");

		FMLCommonHandler.instance().bus().register(eventHandler);
		MinecraftForge.EVENT_BUS.register(eventHandler);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		//chunkProviderID = DimensionUtil.registerProvider(PAYDAYWorldProvider.class);
		//dimensionID = DimensionUtil.registerDimension(chunkProviderID);
	}
}