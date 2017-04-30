package com.TominoCZ.PAYDAY.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.TominoCZ.PAYDAY.PAYDAY;
import com.TominoCZ.PAYDAY.block.LobbyTileEntity;
import com.TominoCZ.PAYDAY.packet.LobbyPlayerActionPacket;
import com.TominoCZ.PAYDAY.packet.LobbyPlayerOpenedGuiPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiLobby extends GuiScreen {
	int posX, posY, posZ;

	World world;

	EntityPlayer player;

	GuiButton join, disconnect;

	public GuiLobby(World w, EntityPlayer p, int x, int y, int z) {
		posX = x;
		posY = y;
		posZ = z;

		world = w;
		player = p;

		PAYDAY.INSTANCE.sendToServer(new LobbyPlayerOpenedGuiPacket(x, y, z));
	}

	@Override
	protected void keyTyped(char c, int i) {
		if (i == 1 || i == this.mc.gameSettings.keyBindInventory.getKeyCode())
			this.mc.thePlayer.closeScreen();
	}

	public void initGui() {
		int i = this.width / 2 - 124;
		int j = this.height / 2 - 83 + 5;

		join = new GuiButton(0, i + 200 - 38, j, 80, 20, "JOIN");
		disconnect = new GuiButton(1, i + 200 - 38, j + 22, 80, 20, "LEAVE");

		this.buttonList.add(join);
		this.buttonList.add(disconnect);
	}

	@Override
	public void drawScreen(int x, int y, float partialTicks) {
		this.drawDefaultBackground();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);

		this.mc.getTextureManager().bindTexture(new ResourceLocation(PAYDAY.MODID + ":textures/gui/lobby.png"));

		int i = this.width / 2 - 124;
		int j = this.height / 2 - 83;

		this.drawTexturedModalRect(i, j, 0, 0, 256, 256);

		// DRAW PLAYERS
		TileEntity te = world.getTileEntity(posX, posY, posZ);

		LobbyTileEntity tile = null;

		boolean containsPlayer = false;

		if (te instanceof LobbyTileEntity) {
			tile = (LobbyTileEntity) te;

			if (tile != null) {
				List<EntityPlayer> plrs = tile.getPlayers();

				for (int k = 0; k < plrs.size(); k++)
					this.drawString(fontRendererObj,
							(plrs.get(k).getUniqueID().equals(player.getUniqueID()) ? "\u00A76" : "\u00A7a")
									+ plrs.get(k).getDisplayName(),
							((this.width / 2 - 128) + 25), (int) ((this.height / 2 - 87) + 25 + k * 12.35), 'a');
			}

			containsPlayer = tile.players.contains(player.getUniqueID());
		}

		boolean playerInLobby = false;

		if (tile != null) {
			playerInLobby = player.getEntityData().getInteger("PAYDAYLobbyX") == tile.xCoord
					&& player.getEntityData().getInteger("PAYDAYLobbyY") == tile.yCoord
					&& player.getEntityData().getInteger("PAYDAYLobbyZ") == tile.zCoord;
		}
		join.enabled = !player.getEntityData().getBoolean("inPAYDAYLobby") && !containsPlayer && !playerInLobby;
		disconnect.enabled = !join.enabled && playerInLobby;

		if (tile == null) {
			player.getEntityData().setBoolean("inPAYDAYLobby", false);
			player.getEntityData().setInteger("PAYDAYLobbyY", -1);

			this.mc.thePlayer.closeScreen();
		}

		super.drawScreen(x, y, partialTicks);
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void actionPerformed(GuiButton b) {
		TileEntity te = world.getTileEntity(posX, posY, posZ);
		LobbyTileEntity tile = null;

		if (te instanceof LobbyTileEntity) {
			tile = (LobbyTileEntity) te;

			switch (b.id) {
			case 0:
				tile.addPlayer(player);

				PAYDAY.INSTANCE.sendToServer(new LobbyPlayerActionPacket(posX, posY, posZ, false));

				player.getEntityData().setBoolean("inPAYDAYLobby", true);
				player.getEntityData().setInteger("PAYDAYLobbyX", posX);
				player.getEntityData().setInteger("PAYDAYLobbyY", posY);
				player.getEntityData().setInteger("PAYDAYLobbyZ", posZ);
				break;
			case 1:
				tile.removePlayer(player);

				PAYDAY.INSTANCE.sendToServer(new LobbyPlayerActionPacket(posX, posY, posZ, true));

				player.getEntityData().setBoolean("inPAYDAYLobby", false);
				player.getEntityData().setInteger("PAYDAYLobbyY", -1);
				break;
			}
		}
	}
}
