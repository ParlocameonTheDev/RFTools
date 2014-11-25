package com.mcjty.rftools.blocks.teleporter;

import com.mcjty.container.EmptyContainer;
import com.mcjty.container.GenericContainerBlock;
import com.mcjty.rftools.RFTools;
import com.mcjty.rftools.blocks.ModBlocks;
import com.mcjty.rftools.render.ModRenderers;
import com.mcjty.varia.Coordinate;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class MatterTransmitterBlock extends GenericContainerBlock {

    private IIcon iconTop;

    public MatterTransmitterBlock(Material material) {
        super(material, MatterTransmitterTileEntity.class);
        setBlockName("matterTransmitterBlock");
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        super.breakBlock(world, x, y, z, block, meta);
        Block b = world.getBlock(x, y+1, z);
        if (ModBlocks.teleportBeamBlock.equals(b)) {
            world.setBlockToAir(x, y+1, z);
        }
    }

    @Override
    public int getGuiID() {
        return RFTools.GUI_MATTER_TRANSMITTER;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiContainer createClientGui(EntityPlayer entityPlayer, TileEntity tileEntity) {
        MatterTransmitterTileEntity matterTransmitterTileEntity = (MatterTransmitterTileEntity) tileEntity;
        EmptyContainer matterTransmitterContainer = new EmptyContainer(entityPlayer);
        return new GuiMatterTransmitter(matterTransmitterTileEntity, matterTransmitterContainer);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        // We don't want what GenericContainerBlock does.
        restoreBlockFromNBT(world, x, y, z, itemStack);

        // Restore the transmitter beam if needed.
        if (!world.isRemote) {
            MatterTransmitterTileEntity matterTransmitterTileEntity = (MatterTransmitterTileEntity) world.getTileEntity(x, y, z);
            if (matterTransmitterTileEntity.getTeleportDestination() != null && matterTransmitterTileEntity.getTeleportDestination().isValid()) {
                DialingDeviceTileEntity.makeBeam(new Coordinate(x, y, z), world, 1, 4, 2);
            }
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        iconTop = iconRegister.registerIcon(RFTools.MODID + ":" + "machineTransmitter");
        iconSide = iconRegister.registerIcon(RFTools.MODID + ":" + "machineSide");
    }

    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
        if (side == ForgeDirection.UP.ordinal()) {
            return iconTop;
        } else {
            return iconSide;
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (side == ForgeDirection.UP.ordinal()) {
            return iconTop;
        } else {
            return iconSide;
        }
    }

}
