package com.TominoCZ.PAYDAY.handler;

import java.util.List;

import com.TominoCZ.PAYDAY.PlayerData;
import com.TominoCZ.PAYDAY.block.LobbyTileEntity;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;

public class MEventHandler {
	LobbyTileEntity tile;
	
	int tick;

	public MEventHandler() {
		new Thread()
		{
			public void run(){
				while(true){
					tick++;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	@SubscribeEvent
	public void onTickEvent(TickEvent.PlayerTickEvent evt) {
		if (tick >= 15) {
			for (TileEntity t : (List<TileEntity>) evt.player.worldObj.loadedTileEntityList) {
				if (t instanceof LobbyTileEntity) {
					tile = (LobbyTileEntity) t;
					
					if (tile.players.size() != tile.blockMetadata)
						tile.check();
				}
			}

			tick = 0;
		}
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing e) {
		if (e.entity instanceof EntityPlayer)
			PlayerData.register((EntityPlayer) e.entity);
	}

	//@SubscribeEvent
	//public void onEntityJoinWorld(EntityJoinWorldEvent e) {
		//if (e.entity instanceof EntityPlayer)
			//PlayerData.get((EntityPlayer) e.entity).requestSyncAll();
	//}

	@SubscribeEvent
	public void onPlayerCloned(PlayerEvent.Clone e) {
		NBTTagCompound nbt = new NBTTagCompound();
		
		PlayerData.get(e.original).saveReviveRelevantNBTData(nbt, e.wasDeath);
		PlayerData.get(e.entityPlayer).loadNBTData(nbt);
	}

	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent evt) {
		/*
		if (evt.block instanceof LobbyBlock) {
			// RESETS DATA AND CANCELS EVENT
			LobbyTileEntity t = (LobbyTileEntity) evt.world.getTileEntity(evt.x, evt.y, evt.z);

			if (t != null) {
				PlayerData data;
				
				for (EntityPlayer p : UUIDUtil.getPlayersFromProfileUUIDs(evt.world, t.players)) {
					data = PlayerData.get(p);
					
					data.setReady(false);
					data.setInLobby(false);
					
					if (evt.world.isRemote)
						data.sendToServer();
					
					t.removePlayer(p);
					
					//PAYDAY.INSTANCE.sendToServer(new LobbyPlayerActionPacket(evt.x, evt.y, evt.z, true));
				}
			}
			//evt.setCanceled(true);
		}*/
	}
}