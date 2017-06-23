package com.TominoCZ.PAYDAY.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.lwjgl.opengl.GL11;

import com.TominoCZ.PAYDAY.PAYDAY;
import com.TominoCZ.PAYDAY.PlayerData;
import com.TominoCZ.PAYDAY.block.LobbyTileEntity;
import com.TominoCZ.PAYDAY.packet.LobbyBeginGamePacket;
import com.TominoCZ.PAYDAY.packet.LobbyPlayerOpenedGuiPacket;
import com.TominoCZ.PAYDAY.util.UUIDUtil;

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

	GuiButton join, disconnect, ready, start;

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
		ready = new GuiButton(2, i + 200 - 38, j + 50, 80, 20, "READY");
		start = new GuiButton(3, i + 200 - 38, j + 72, 80, 20, "START");

		this.buttonList.add(join);
		this.buttonList.add(disconnect);
		this.buttonList.add(ready);
		this.buttonList.add(start);
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
		boolean canStart = false;
		boolean allReady = false;
		boolean isGameLive = false;
		boolean canJoin = false;
		boolean isHost = false;

		PlayerData data = PlayerData.get(player);

		if (te instanceof LobbyTileEntity) {
			tile = (LobbyTileEntity) te;

			if (tile != null) {
				// List<EntityPlayer> plrs =
				// UUIDUtil.getPlayersFromProfileUUIDs(world, tile.players);
				tile.check();
				
				List<UUID> plrs = new ArrayList<UUID>(tile.players);

				UUID thisPlr = player.getGameProfile().getId();

				StringBuilder sb = new StringBuilder();

				PlayerData tmpData;
				
				for (int k = 0; k < plrs.size(); k++) {
					UUID plr = plrs.get(k);

					EntityPlayer p = UUIDUtil.getPlayerFromProfileUUID(world, plr);

					if (p != null) {
						tmpData = PlayerData.get(p);

						sb.append(tile.isHost(p) ? "\u00A73" : (tmpData.isReady() ? "\u00A7a" : "\u00A7c"));
						sb.append("|| ");
						sb.append(plr.equals(thisPlr) ? "\u00A76" : "\u00A7a");
						sb.append(p.getDisplayName());

						this.drawString(fontRendererObj, sb.toString(), ((this.width / 2 - 128) + 25),
								(int) ((this.height / 2 - 87) + 25 + k * 12.35), 'a');
						sb = new StringBuilder();
					}
				}

				containsPlayer = plrs.contains(player.getGameProfile().getId());

				canStart = plrs.size() > 0 && data.isInLobby() && tile.allReady();

				allReady = tile.allReady();
				isGameLive = tile.isGameLive();
				canJoin = plrs.size() < 4;

				isHost = tile.isHost(player);
			}
		}

		join.enabled = !data.isInLobby() && !containsPlayer && canJoin;

		disconnect.enabled = !join.enabled && containsPlayer;

		if (tile == null) {
			data.setInLobby(false);

			this.mc.thePlayer.closeScreen();
		}

		ready.enabled = !isGameLive && containsPlayer && !isHost;
		ready.displayString = data.isReady() ? "NOT READY" : "READY";

		start.enabled = canStart && !isGameLive && isHost;

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

			PlayerData data = PlayerData.get(player);

			switch (b.id) {
			case 0:
				tile.addPlayer(player);
				break;
			case 1:
				tile.removePlayer(player);
				break;
			case 2:
				data.setReady(!data.isReady());

				data.sendToServer();
				break;
			case 3:
				if (tile.allReady() && !tile.isGameLive()) {
					PAYDAY.INSTANCE.sendToServer(new LobbyBeginGamePacket(posX, posY, posZ));
					tile.setGameLive(true);
					break;
				}
			}
		}
	}
}
