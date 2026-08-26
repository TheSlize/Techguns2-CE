package techguns.gui.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import techguns.api.tginventory.TGSlotType;
import techguns.capabilities.TGExtendedPlayer;
import techguns.gui.player.TGPlayerInventory;
import techguns.gui.widgets.SlotArmor;
import techguns.gui.widgets.SlotTG;
import techguns.tileentities.CamoBenchTileEnt;

public class CamoBenchContainer extends OwnedTileContainer {

    public static final int SLOT_INPUT_X = 17;
    public static final int SLOT_INPUT_Y = 18;

    public CamoBenchContainer(InventoryPlayer player, CamoBenchTileEnt ent) {
        super(player, ent);

        IItemHandler inventory = ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.SOUTH);

        this.addSlotToContainer(new SlotItemHandler(inventory, 0, SLOT_INPUT_X, SLOT_INPUT_Y));

        this.addDefaultPlayerInventorySlots(player);
        int i;
        for (i = 0; i < 4; ++i) {
            this.addSlotToContainer(new SlotArmor(player, 39 - i, 99 + (i * 18), 18, i, player.player));
        }

        TGExtendedPlayer props = TGExtendedPlayer.get(player.player);
        if (props != null) {

            this.addSlotToContainer(new SlotTG(props.tg_inventory, TGPlayerInventory.SLOT_FACE, 44, 18, TGSlotType.FACESLOT));
            this.addSlotToContainer(new SlotTG(props.tg_inventory, TGPlayerInventory.SLOT_BACK, 62, 18, TGSlotType.BACKSLOT));
            this.addSlotToContainer(new SlotTG(props.tg_inventory, TGPlayerInventory.SLOT_HAND, 80, 18, TGSlotType.HANDSLOT));

        }
        this.addSlotToContainer(new Slot(player, 40, -17, 94) {
            @Override
            public String getSlotTexture() {
                return "minecraft:items/empty_armor_slot_shield";
            }
        });

    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer player, int slotid) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotid);

        if (slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();
            if (!stack.isEmpty()) {

                //camoslot pressed
                if (slotid == 0) {
                    boolean moved = false;
                    if (stack1.getItem() instanceof ItemArmor) {
                        int type = getArmorIntFromEntityEquipmentSlot(((ItemArmor) stack1.getItem()).armorType);
                        //put armor back in player armor inventory
                        moved = this.mergeItemStack(stack1, 37 + type, 37 + type + 1, false);
                    }
                    if (!moved && !this.mergeItemStack(stack1, 1, 37, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onSlotChange(stack1, stack);
                } else {
                    if (!this.mergeItemStack(stack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onSlotChange(stack1, stack);
                }

                if (stack1.isEmpty()) {
                    slot.putStack(ItemStack.EMPTY);
                } else {
                    slot.onSlotChanged();
                }

                if (stack1.getCount() == stack.getCount()) {
                    return ItemStack.EMPTY;
                }

                //slot.onPickupFromSlot(player, stack1);
                slot.onTake(player, stack1);
            }
        }
        return stack;
    }

}
