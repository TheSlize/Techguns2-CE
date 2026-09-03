package techguns.items.armors;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techguns.TGArmors;
import techguns.*;
import techguns.api.damagesystem.DamageType;
import techguns.damagesystem.ShieldStats;
import techguns.util.ItemStackOreDict;
import techguns.util.TextUtil;

public class GenericShield extends ItemShield implements ICamoChangeable {

    protected int camocount;

    protected ItemStackOreDict repairMat = ItemStackOreDict.EMPTY;
    protected int repairMatCount = 0;

    protected static DecimalFormat formatReduction = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ENGLISH));


    public GenericShield(String modid, String name, int durability, boolean addToList, int camocount) {
        super();
        setMaxStackSize(1);
        setCreativeTab(Techguns.tabTechgun);
        setRegistryName(name);
        setTranslationKey(modid + "." + name);
        this.camocount = Math.max(camocount, 1);
        this.setMaxDamage(durability);
        if (addToList) {
            TGArmors.shields.add(this);
        }
    }

    public GenericShield(String name, int durability, int camocount) {
        this(Tags.MOD_ID, name, durability, true, camocount);
    }

    public GenericShield setRepairMat(ItemStackOreDict mats) {
        this.repairMat = mats;
        this.repairMatCount = mats.stackSize;
        return this;
    }

    public ArrayList<ItemStack> getRepairMats(ItemStack item) {
        ArrayList<ItemStack> mats = new ArrayList<>();

        if (item.getItemDamage() > 0) {
            GenericShield armor = (GenericShield) item.getItem();

            float dmgpercent = (item.getItemDamage() * 1.0f) / ((item.getMaxDamage() - 1) * 1.0f);

            int count = (int) Math.ceil(armor.repairMatCount * dmgpercent);

            if (!armor.repairMat.isEmpty() && count > 0) {
                mats.add(armor.repairMat.getItemStacks(count).get(0));
            }

        }
        return mats;
    }

    public boolean canRepairOnRepairBench(ItemStack item) {
        GenericShield shield = (GenericShield) item.getItem();
        return shield.repairMatCount > 0;
    }

    @Override
    public @NotNull String getItemStackDisplayName(@NotNull ItemStack stack) {
        //same as in Item
        return I18n.translateToLocal(this.getTranslationKey(stack) + ".name").trim();
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(new ResourceLocation("minecraft:shield"), "inventory"));
    }

    @Override
    public int getCamoCount() {
        return this.camocount;
    }

    @Override
    public boolean isShield(ItemStack stack, EntityLivingBase entity) {
        return (stack.getItemDamage() < stack.getMaxDamage());
    }

    @Override
    public String getCurrentCamoName(ItemStack item) {
        NBTTagCompound tags = item.getTagCompound();
        byte camoID = 0;
        if (tags != null && tags.hasKey("camo")) {
            camoID = tags.getByte("camo");
        }
        if (camoID > 0) {
            return TextUtil.trans(this.getTranslationKey() + ".camoname." + camoID);
        } else {
            return TextUtil.trans("techguns.item.defaultcamo");
        }
    }

    @Override
    public boolean getIsRepairable(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {
        return this.repairMat.isEqualWithOreDict(repair) || super.getIsRepairable(toRepair, repair);
    }

    @Override
    public void addInformation(@NotNull ItemStack item, World worldIn, @NotNull List<String> list, @NotNull ITooltipFlag flagIn) {
        super.addInformation(item, worldIn, list, flagIn);

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {

            list.add(TextUtil.transTG("armorTooltip.durability") + ": " + (item.getMaxDamage() - item.getItemDamage()) + "/" + (item.getMaxDamage()));
            ShieldStats s = ShieldStats.getStats(item, null);
            list.add(ChatFormatting.BLUE + TextUtil.transTG("armorTooltip.resistances") + ":");

            list.add(ChatFormatting.DARK_GRAY + TextUtil.trans("techguns.armorTooltip.damageType.AR") + " " + formatReduction.format(s.getReductionAgainstType(DamageType.PHYSICAL)));
            list.add(ChatFormatting.GRAY + TextUtil.trans("techguns.armorTooltip.damageType.PR") + " " + formatReduction.format(s.getReductionAgainstType(DamageType.PROJECTILE)));
            list.add(ChatFormatting.DARK_RED + TextUtil.trans("techguns.armorTooltip.damageType.EX") + " " + formatReduction.format(s.getReductionAgainstType(DamageType.EXPLOSION)));
            list.add(ChatFormatting.DARK_AQUA + TextUtil.trans("techguns.armorTooltip.damageType.E") + " " + formatReduction.format(s.getReductionAgainstType(DamageType.ENERGY)));
            list.add(ChatFormatting.RED + TextUtil.trans("techguns.armorTooltip.damageType.F") + " " + formatReduction.format(s.getReductionAgainstType(DamageType.FIRE)));
            list.add(ChatFormatting.AQUA + TextUtil.trans("techguns.armorTooltip.damageType.I") + " " + formatReduction.format(s.getReductionAgainstType(DamageType.ICE)));
            list.add(ChatFormatting.YELLOW + TextUtil.trans("techguns.armorTooltip.damageType.L") + " " + formatReduction.format(s.getReductionAgainstType(DamageType.LIGHTNING)));
            list.add(ChatFormatting.DARK_GREEN + TextUtil.trans("techguns.armorTooltip.damageType.P") + " " + formatReduction.format(s.getReductionAgainstType(DamageType.POISON)));
            list.add(ChatFormatting.DARK_GRAY + TextUtil.trans("techguns.armorTooltip.damageType.D") + " " + formatReduction.format(s.getReductionAgainstType(DamageType.DARK)));
            list.add(ChatFormatting.GREEN + TextUtil.trans("techguns.armorTooltip.damageType.RAD") + " " + formatReduction.format(s.getReductionAgainstType(DamageType.RADIATION)));

        } else {
            list.add(TextUtil.trans("techguns.gun.tooltip.shift1") + " " + ChatFormatting.GREEN + TextUtil.trans("techguns.gun.tooltip.shift2") + " " + ChatFormatting.GRAY + TextUtil.trans("techguns.gun.tooltip.shift3"));
        }
    }
}
