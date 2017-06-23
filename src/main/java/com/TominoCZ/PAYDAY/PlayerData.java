package com.TominoCZ.PAYDAY;

import com.TominoCZ.PAYDAY.packet.PacketSyncPlayerPropertiesClient;
import com.TominoCZ.PAYDAY.packet.PacketSyncPlayerPropertiesServer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerData implements IExtendedEntityProperties {
	public EntityPlayer thePlayer;
	public World theWorld;

	boolean isInLobby = false;
	boolean isReady = false;
	//int lobbyHash = -1;
	
	public PlayerData(EntityPlayer player) {
		this.thePlayer = player;
	}

	@Override
	public void saveNBTData(NBTTagCompound nbt) {
		nbt.setBoolean(PAYDAY.MODID + "-ReadyInLobby", this.isReady);
		nbt.setBoolean(PAYDAY.MODID + "-InLobby", this.isInLobby);
		//nbt.setInteger(PAYDAY.MODID + "-LobbyHash", this.lobbyHash);
	}

	@Override
	public void loadNBTData(NBTTagCompound nbt) {
		if (nbt.hasKey(PAYDAY.MODID + "-ReadyInLobby"))
			this.setReady(nbt.getBoolean(PAYDAY.MODID + "-ReadyInLobby"));
		if (nbt.hasKey(PAYDAY.MODID + "-InLobby"))
			this.setReady(nbt.getBoolean(PAYDAY.MODID + "-InLobby"));
		//if (nbt.hasKey(PAYDAY.MODID + "-LobbyHash"))
			//this.setLobbyHash(nbt.getInteger(PAYDAY.MODID + "-LobbyHash"));
		
		//sendToServer();
	}

	@Override
	public void init(Entity entity, World world) {
		thePlayer = (EntityPlayer) entity;
		theWorld = world;
	}

	public void setReady(boolean ready) {
		this.isReady = ready;
	}

	public void setInLobby(boolean inLobby) {
		this.isInLobby = inLobby;
	}

	//public void setLobbyHash(int hashCode) {
		//this.lobbyHash = hashCode;
	//}
	
	public boolean isReady() {
		return this.isReady;
	}

	public boolean isInLobby() {
		return this.isInLobby;
	}

	public boolean isServerSide() {
		return this.thePlayer != null && this.thePlayer.worldObj != null && !this.thePlayer.worldObj.isRemote;
	}

	public void sendToServer()
	{
		PAYDAY.INSTANCE.sendToServer(new PacketSyncPlayerPropertiesServer(this.isReady, this.isInLobby));
	}
	
	@Deprecated
	public void sendToClients()
	{
		PAYDAY.INSTANCE.sendToAll(new PacketSyncPlayerPropertiesClient(this.thePlayer.getGameProfile().getId(), this.isReady, this.isInLobby));
	}
	
	@Deprecated
	public void syncProperties() {
		if (this.isServerSide()) {
			//for (EntityPlayer p : (List<EntityPlayer>) this.theWorld.playerEntities)
				//if (p != this.thePlayer)
					//PAYDAY.INSTANCE.sendTo(
							//new PacketSyncPlayerPropertiesServer(this.thePlayer, this.isReady(), this.isInLobby()),
							//(EntityPlayerMP) p);
		}
	}

	public static void register(EntityPlayer player) {
		player.registerExtendedProperties(PAYDAY.MODID + "-EntityExtendedProperties", new PlayerData(player));
	}

	public void saveReviveRelevantNBTData(NBTTagCompound nbt, boolean wasDeath) {
		if (!wasDeath)
			this.saveNBTData(nbt);
	}

	public static PlayerData get(Entity p) {
		return (PlayerData) p.getExtendedProperties(PAYDAY.MODID + "-EntityExtendedProperties");
	}
}
