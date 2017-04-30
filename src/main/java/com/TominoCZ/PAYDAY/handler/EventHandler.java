package com.TominoCZ.PAYDAY.handler;

import java.util.List;

import com.TominoCZ.PAYDAY.block.LobbyTileEntity;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.tileentity.TileEntity;

public class EventHandler {
	LobbyTileEntity tile;

	int tick;

	@SubscribeEvent
	public void onTickEvent(TickEvent.WorldTickEvent evt) {
		if (tick >= 50) {
			for (TileEntity t : (List<TileEntity>) evt.world.loadedTileEntityList) {
				if (t instanceof LobbyTileEntity) {
					tile = (LobbyTileEntity) t;
					
					if (tile.players.size() != tile.blockMetadata)
						tile.check();
				}
			}
			
			tick = 0;
		}

		tick++;
	}
}