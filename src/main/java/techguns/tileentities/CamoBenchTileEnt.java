package techguns.tileentities;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.jetbrains.annotations.NotNull;
import techguns.*;
import techguns.capabilities.TGExtendedPlayer;
import techguns.gui.player.TGPlayerInventory;
import techguns.items.armors.ICamoChangeable;
import techguns.tileentities.operation.CamoBenchRecipes;
import techguns.tileentities.operation.CamoBenchRecipes.CamoBenchRecipe;
import techguns.tileentities.operation.ItemStackHandlerPlus;

import static techguns.gui.ButtonConstants.BUTTON_ID_SECURITY;

public class CamoBenchTileEnt extends BasicOwnedTileEnt {

    public CamoBenchTileEnt() {
        super(1, false);

        this.inventory = new ItemStackHandlerPlus(1) {
            @Override
            protected boolean allowItemInSlot(int slot, ItemStack stack) {
                return isCamoChangeable(stack);
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return allowItemInSlot(slot, stack);
            }
        };
    }

    public static boolean isCamoChangeable(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.getItem() instanceof ICamoChangeable) return true;
        if (stack.getItem() instanceof ItemBlock) return CamoBenchRecipes.getRecipeFor(((ItemBlock) stack.getItem()).getBlock()) != null;
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(Tags.MOD_ID + ".container.camobench");
    }

    /**
     * Returns current ItemStack if it has changeable camo or null
     *
     * @return
     */
    public ItemStack getItem() {
        ItemStack stack = this.inventory.getStackInSlot(0);
        return isCamoChangeable(stack) ? stack : ItemStack.EMPTY;
    }


    @Override
    public void buttonClicked(int id, EntityPlayer ply, String data) {
        if (this.isUseableByPlayer(ply)) {

            if (id <= BUTTON_ID_SECURITY) {
                super.buttonClicked(id, ply, data);
            } else {

                int odd_even = BUTTON_ID_SECURITY % 2;

                if (id < BUTTON_ID_SECURITY + 3) {
                    ItemStack item = this.getItem();
                    if (item != null && item.getItem() instanceof ICamoChangeable camoitem) {
                        camoitem.switchCamo(item, id == BUTTON_ID_SECURITY + 2);
                        this.needUpdate();
                    } else if (item.getItem() instanceof ItemBlock) {
                        Block b = ((ItemBlock) item.getItem()).getBlock();
                        CamoBenchRecipe r = CamoBenchRecipes.getRecipeFor(b);
                        if (r != null) {
                            r.switchCamo(item, id == BUTTON_ID_SECURITY + 2);
                            this.needUpdate();
                        }
                    }
                } else if (id < BUTTON_ID_SECURITY + 11) {

                    int slotid = 3 - (((int) (Math.ceil((id - (BUTTON_ID_SECURITY + 2)) * 0.5))) - 1);

                    ItemStack item = ply.inventory.armorInventory.get(slotid);//this.content[slotid];
                    if (!item.isEmpty() && item.getItem() instanceof ICamoChangeable camoitem) {
                        boolean back = id % 2 == odd_even;

                        camoitem.switchCamo(item, back);
                        ((EntityPlayerMP) ply).connection.sendPacket(new SPacketSetSlot(ply.openContainer.windowId, 37 + (3 - slotid), item));
                    }

                } else if (id < BUTTON_ID_SECURITY + 13) {
                    boolean back = id % 2 == odd_even;
                    TGExtendedPlayer props = TGExtendedPlayer.get(ply);
                    if (props != null) {
                        ItemStack item = props.tg_inventory.inventory.get(TGPlayerInventory.SLOT_BACK);
                        if (!item.isEmpty() && item.getItem() instanceof ICamoChangeable camoitem) {
                            camoitem.switchCamo(item, back);
                        }
                    }
                } else if (id < BUTTON_ID_SECURITY + 15) {
                    boolean back = id % 2 == odd_even;
                    TGExtendedPlayer props = TGExtendedPlayer.get(ply);
                    if (props != null) {
                        ItemStack item = props.tg_inventory.inventory.get(TGPlayerInventory.SLOT_FACE);
                        if (!item.isEmpty() && item.getItem() instanceof ICamoChangeable camoitem) {
                            camoitem.switchCamo(item, back);
                        }
                    }
                }
            }
        }
    }


}
