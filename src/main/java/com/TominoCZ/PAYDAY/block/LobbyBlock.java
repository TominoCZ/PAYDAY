package com.TominoCZ.PAYDAY.block;

import java.util.UUID;

import com.TominoCZ.PAYDAY.PAYDAY;
import com.TominoCZ.PAYDAY.PlayerData;
import com.TominoCZ.PAYDAY.util.UUIDUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class LobbyBlock extends BlockContainer {
	@SideOnly(Side.CLIENT)
	public static IIcon LobbyIcon;
	@SideOnly(Side.CLIENT)
	public static IIcon FourPlayersIcon;
	@SideOnly(Side.CLIENT)
	public static IIcon OnePlayerIcon;
	@SideOnly(Side.CLIENT)
	public static IIcon TwoPlayersIcon;
	@SideOnly(Side.CLIENT)
	public static IIcon ThreePlayersIcon;

	public LobbyBlock() {
		super(new Material(MapColor.airColor));
		// this.setBlockTextureName(this.getIcon(0, meta).getIconName());
		this.isBlockContainer = true;
		this.setBlockName("Lobby");
		this.setBlockUnbreakable();
	}

	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int i, float f0, float f1, float f2) {
		if (w.isRemote)
			p.openGui(PAYDAY.instance, 0, w, x, y, z);

		return true;
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		super.registerBlockIcons(register);
		LobbyIcon = register.registerIcon(PAYDAY.MODID + ":lobby");
		OnePlayerIcon = register.registerIcon(PAYDAY.MODID + ":lobby_1");
		TwoPlayersIcon = register.registerIcon(PAYDAY.MODID + ":lobby_2");
		ThreePlayersIcon = register.registerIcon(PAYDAY.MODID + ":lobby_3");
		FourPlayersIcon = register.registerIcon(PAYDAY.MODID + ":lobby_4");
	}

	@Override
	protected String getTextureName() {
		return super.getTextureName();
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		switch (meta) {
		case 1:
			return OnePlayerIcon;
		case 2:
			return TwoPlayersIcon;
		case 3:
			return ThreePlayersIcon;
		case 4:
			return FourPlayersIcon;
		default:
			return LobbyIcon;
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new LobbyTileEntity(worldIn);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block b, int i) {
		LobbyTileEntity tile = (LobbyTileEntity) world.getTileEntity(x, y, z);
		PlayerData data;
		EntityPlayer player;
		
		for (UUID uuid : tile.players) {
			player = UUIDUtil.getPlayerFromProfileUUID(world, uuid);
			data = PlayerData.get(player);
			data.setInLobby(false);
			data.setReady(false);
			
			data.sendToServer();
		}
		
		tile.players.clear();
		
		super.breakBlock(world, x, y, z, b, i);
	}

	@Override
	public boolean onBlockEventReceived(World w, int x, int y, int z, int i, int i0) {
		super.onBlockEventReceived(w, x, y, z, i, i0);
		TileEntity tileentity = w.getTileEntity(x, y, z);
		return tileentity == null ? false : tileentity.receiveClientEvent(i, i0);
	}
}
