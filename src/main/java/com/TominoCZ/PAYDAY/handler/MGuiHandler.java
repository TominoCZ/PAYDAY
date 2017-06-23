package com.TominoCZ.PAYDAY.handler;

import com.TominoCZ.PAYDAY.gui.GuiLobby;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class MGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		GuiScreen g = null;

		if (ID == 0)
			g = new GuiLobby(world, player, x, y, z);

		return g;
	}
}