package techguns.items.armors;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import net.minecraft.client.resources.I18n;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import techguns.TGArmors;
import techguns.TGItems;
import techguns.*;
import techguns.api.damagesystem.DamageType;
import techguns.api.radiation.TGRadiation;
import techguns.api.render.IItemTGRenderer;
import techguns.api.tginventory.ITGSpecialSlot;
import techguns.capabilities.TGExtendedPlayer;
import techguns.client.ClientProxy;
import techguns.damagesystem.DamageSystem;
import techguns.damagesystem.TGDamageSource;
import techguns.gui.player.TGPlayerInventory;
import techguns.util.TextUtil;

public class GenericArmor extends ItemArmor implements ISpecialArmor, IItemTGRenderer {

    protected static final UUID[] RAD_RESIST_MODIFIER = {
            UUID.fromString("47E9813E-3FB7-415C-A6CD-F9A4A3C55F82"),
            UUID.fromString("AD509C77-02FC-40E8-A811-52B29842CE56"),
            UUID.fromString("CB3BF616-549C-47EF-ADC8-2ED60C331ABD"),
            UUID.fromString("052C821F-1FA9-4D67-A660-D885FAE22EED")
    };

    //For vanilla Anvil
    private ItemStack repairItem = null;

    //Values for RepairBench
    protected ItemStack repairMatMetal = null;
    protected ItemStack repairMatCloth = null;
    protected float repairMatRatioMetal = 0.5f;
    protected int repairMatCount = 0;

    String textureName;
    protected ResourceLocation armorModel = null; //default vanilla model
    protected boolean doubleTex = true;

    //item boni
    protected float SpeedBonus = 0.0f;
    protected float JumpBonus = 0.0f;
    protected float FallDMG = 0.0f;
    protected float FallFreeHeight = 0.0f;
    protected float MiningSpeedBonus = 0.0f;
    protected float WaterMiningBonus = 0.0f;
    protected int armorValue;
    protected float GunAccuracy = 0.0f;
    protected float extraHearts = 0.0f;
    protected float nightvision = 0.0f;

    protected float knockbackresistance = 0.0f;
    protected float stepassist = 0.0f;

    protected float oxygen_gear = 0.0f;
    protected float water_electrolyzer = 0.0f;
    protected float coolingsystem = 0.0f;

    protected float waterspeedbonus = 0.0f;

    protected float radresistance = 0.0f;

    protected boolean hideFaceslot = false;
    protected boolean hideBackslot = false;
    protected boolean hideGloveslot = false;

    protected TGArmorMaterial material;

    protected static DecimalFormat formatArmor = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    protected boolean use3dRenderHack = false;

    protected String modid;

    protected ResourceLocation modelLoc;

    public GenericArmor(String unlocalizedName, TGArmorMaterial material, String textureName, EntityEquipmentSlot type) {
        this(Tags.MOD_ID, unlocalizedName, material, textureName, type);
    }

    public GenericArmor(String modid, String unlocalizedName, TGArmorMaterial material, String textureName, EntityEquipmentSlot type) {
        super(material.createVanillaMaterial(), 0, type);
        this.material = material;
        this.textureName = textureName; //Armor Texture
        setCreativeTab(Techguns.tabTechgun);
        this.modid = modid;
        this.setTranslationKey(unlocalizedName);
        ResourceLocation reg = new ResourceLocation(modid, unlocalizedName);
        this.setRegistryName(reg);
        this.modelLoc = reg;
        this.armorValue = Math.round(material.getArmorValueSlot(type, DamageType.PHYSICAL));

        this.setMaxDamage(this.material.getDurability(type));

        TGArmors.armors.add(this);
    }

    public GenericArmor setArmorModel(ResourceLocation key, boolean doubleTex, ResourceLocation backupTex) {
        this.armorModel = key;
        this.doubleTex = doubleTex;
        this.modelLoc = backupTex;
        return this;
    }

    public GenericArmor setArmorModel(ResourceLocation key, boolean doubleTex) {
        this.armorModel = key;
        this.doubleTex = doubleTex;
        return this;
    }

    public static boolean isTechgunArmor(ItemStack i) {
        if (i == null) {
            return false;
        } else {
            return i.getItem() instanceof GenericArmor;
        }
    }

    public GenericArmor setSpeedBoni(float speed, float jump) {
        this.SpeedBonus = speed;
        this.JumpBonus = jump;
        return this;
    }

    public GenericArmor setFallProtection(float multiplier, float freeheight) {
        this.FallDMG = multiplier;
        this.FallFreeHeight = freeheight;
        return this;
    }

    public GenericArmor setMiningBoni(float bonus) {
        this.MiningSpeedBonus = bonus;
        return this;
    }

    public GenericArmor setMiningBoniWater(float bonus) {
        this.WaterMiningBonus = bonus;
        return this;
    }

    public GenericArmor setGunBonus(float acc) {
        this.GunAccuracy = acc;
        return this;
    }

    public GenericArmor setDurability(int dur) {
        this.setMaxDamage(dur);
        return this;
    }

    public GenericArmor setArmorDisplayValue(int value) {
        this.armorValue = Math.max(0, value);
        return this;
    }

    public GenericArmor setKnockbackResistance(float resistpercent) {
        this.knockbackresistance = resistpercent;
        return this;
    }

    public GenericArmor setRADResistance(float radresistance) {
        this.radresistance = radresistance;
        return this;
    }

    public GenericArmor setOxygenGear(float value) {
        this.oxygen_gear = value;
        return this;
    }

    public GenericArmor setUseRenderHack() {
        this.use3dRenderHack = true;
        return this;
    }

    public float getBonus(TGArmorBonus type) {
        return switch (type) {
            case SPEED -> this.SpeedBonus;
            case JUMP -> this.JumpBonus;
            case FALLDMG -> this.FallDMG;
            case FREEHEIGHT -> this.FallFreeHeight;
            case BREAKSPEED -> this.MiningSpeedBonus;
            case BREAKSPEED_WATER -> this.WaterMiningBonus;
            case GUN_ACCURACY -> this.GunAccuracy;
            case EXTRA_HEART -> this.extraHearts;
            case NIGHTVISION -> this.nightvision;
            case KNOCKBACK_RESISTANCE -> this.knockbackresistance;
            case STEPASSIST -> this.stepassist;
            case OXYGEN_GEAR -> this.oxygen_gear;
            case WATER_ELECTROLYZER -> this.water_electrolyzer;
            case COOLING_SYSTEM -> this.coolingsystem;
            case SPEED_WATER -> this.waterspeedbonus;
            default -> 0.0f;
        };
    }

    public static float getArmorBonusForPlayer(EntityPlayer ply, TGArmorBonus type, boolean consumePower) {
        float bonus = 0.0f;
        TGExtendedPlayer props = TGExtendedPlayer.get(ply);
        if (props == null) {
            return 0.0f;
        }
        for (int i = 0; i < 4; i++) {
            ItemStack armor = ply.inventory.armorInventory.get(i);
            if (GenericArmor.isTechgunArmor(armor)) {


                if (!(armor.getItem() instanceof PoweredArmor pwrarmor)) {
                    if (armor.getItemDamage() < armor.getMaxDamage() - 1) {
                        float bonusVal = ((GenericArmor) armor.getItem()).getBonus(type);
                        bonus += bonusVal;
                    }
                } else {
                    //Powered Armor
                    boolean power = PoweredArmor.hasPower(armor);
                    float bonusVal;

                    if (armor.getItemDamage() < armor.getMaxDamage() - 1) {
                        if (power) {
                            bonusVal = pwrarmor.getBonus(type);
                            bonus += bonusVal;
                            if (consumePower && bonusVal > 0.0f) {
                                PoweredArmor.consumePower(armor, pwrarmor.applyPowerConsumptionOnAction(type, ply));
                            }
                        } else {
                            bonusVal = pwrarmor.getBonusUnpowered(type);
                            bonus += bonusVal;
                        }
                    }

                }
            }
        }

        bonus += getBonusForSlot(props.tg_inventory.inventory.get(TGPlayerInventory.SLOT_FACE), type, consumePower, ply);
        bonus += getBonusForSlot(props.tg_inventory.inventory.get(TGPlayerInventory.SLOT_BACK), type, consumePower, ply);
        bonus += getBonusForSlot(props.tg_inventory.inventory.get(TGPlayerInventory.SLOT_HAND), type, consumePower, ply);

        return bonus;
    }

    private static float getBonusForSlot(ItemStack stack, TGArmorBonus type, boolean consumePower, EntityPlayer ply) {
        float bonus = 0;
        if (!stack.isEmpty()) {
            ITGSpecialSlot item = (ITGSpecialSlot) stack.getItem();
            if (stack.getItem() == TGItems.SHARED_ITEM || (stack.getItemDamage() < stack.getMaxDamage()) || !stack.getItem().isDamageable()) {
                bonus += item.getBonus(type, stack, consumePower, ply);
            }
        }
        return bonus;
    }

    protected boolean hasDoubleTexture() {
        return this.doubleTex;
    }

    private String trans(String text) {
        return TextUtil.trans(Tags.MOD_ID + "." + text);
    }

    @Override
    public ModelBiped getArmorModel(@NotNull EntityLivingBase entityLiving, @NotNull ItemStack itemStack, @NotNull EntityEquipmentSlot armorSlot, @NotNull ModelBiped _default) {
        if (this.armorModel != null) {

            ModelBiped model = ClientProxy.get().getArmorModel(this.armorModel);// ClientProxy.armorModels[this.modelIndex];

            if (model == null) return null;

            model.bipedHead.showModel = armorSlot == EntityEquipmentSlot.HEAD;
            model.bipedHeadwear.showModel = armorSlot == EntityEquipmentSlot.HEAD;

            if (!itemStack.isEmpty() && itemStack.getItem() == TGArmors.t2_beret) {
                model.bipedHeadwear.showModel = false;
            }

            model.bipedBody.showModel = armorSlot == EntityEquipmentSlot.CHEST || armorSlot == EntityEquipmentSlot.LEGS;
            model.bipedRightArm.showModel = armorSlot == EntityEquipmentSlot.CHEST;
            model.bipedLeftArm.showModel = armorSlot == EntityEquipmentSlot.CHEST;
            model.bipedRightLeg.showModel = armorSlot == EntityEquipmentSlot.LEGS || armorSlot == EntityEquipmentSlot.FEET;
            model.bipedLeftLeg.showModel = armorSlot == EntityEquipmentSlot.LEGS || armorSlot == EntityEquipmentSlot.FEET;
            model.isSneak = entityLiving.isSneaking();
            model.isRiding = entityLiving.isRiding();
            model.isChild = entityLiving.isChild();

            return model;
        }
        return null;
    }

    public static String formatAV(float armorValue) {
        return formatArmor.format(armorValue);
    }

    protected void addArmorvaluesInformation(List<String> list) {

        EntityEquipmentSlot slot = this.armorType;

        list.add(ChatFormatting.DARK_GRAY + TextUtil.trans("techguns.armorTooltip.damageType.AR") + " " + formatAV(this.material.getArmorValueSlot(slot, DamageType.PHYSICAL)));
        list.add(ChatFormatting.GRAY + TextUtil.trans("techguns.armorTooltip.damageType.PR") + " " + formatAV(this.material.getArmorValueSlot(slot, DamageType.PROJECTILE)));
        list.add(ChatFormatting.DARK_RED + TextUtil.trans("techguns.armorTooltip.damageType.EX") + " " + formatAV(this.material.getArmorValueSlot(slot, DamageType.EXPLOSION)));
        list.add(ChatFormatting.DARK_AQUA + TextUtil.trans("techguns.armorTooltip.damageType.E") + " " + formatAV(this.material.getArmorValueSlot(slot, DamageType.ENERGY)));
        list.add(ChatFormatting.RED + TextUtil.trans("techguns.armorTooltip.damageType.F") + " " + formatAV(this.material.getArmorValueSlot(slot, DamageType.FIRE)));
        list.add(ChatFormatting.YELLOW + TextUtil.trans("techguns.armorTooltip.damageType.L") + " " + formatAV(this.material.getArmorValueSlot(slot, DamageType.LIGHTNING)));
        list.add(ChatFormatting.DARK_GREEN + TextUtil.trans("techguns.armorTooltip.damageType.P") + " " + formatAV(this.material.getArmorValueSlot(slot, DamageType.POISON)));
        list.add(ChatFormatting.GREEN + TextUtil.trans("techguns.armorTooltip.damageType.RAD") + " " + formatAV(this.material.getArmorValueSlot(slot, DamageType.RADIATION)));
    }

    @Override
    public void addInformation(@NotNull ItemStack item, World worldIn, @NotNull List<String> list, @NotNull ITooltipFlag flagIn) {
        super.addInformation(item, worldIn, list, flagIn);

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {

            list.add(trans("armorTooltip.durability") + ": " + (item.getMaxDamage() - item.getItemDamage()) + "/" + (item.getMaxDamage()));

            if (this.toughness > 0) {
                list.add(ChatFormatting.GRAY + trans("armorTooltip.toughness") + ": " + this.toughness);
            }

            list.add(ChatFormatting.BLUE + trans("armorTooltip.resistances") + ":");
            this.addArmorvaluesInformation(list);

            if (this.getBonus(TGArmorBonus.EXTRA_HEART) > 0.0f) {
                list.add(trans("armorTooltip.healthbonus") + ": +" + (int) this.getBonus(TGArmorBonus.EXTRA_HEART) + " " + trans("armorTooltip.hearts"));
            } else if (this.getBonus(TGArmorBonus.SPEED) != 0.0f) {
                list.add(trans("armorTooltip.movespeed") + ": +" + this.getBonus(TGArmorBonus.SPEED) * 100.0f + "%");
            } else if (this.getBonus(TGArmorBonus.JUMP) > 0.0f) {
                list.add(trans("armorTooltip.jumpheight") + ": +" + this.getBonus(TGArmorBonus.JUMP));
            }
            if (this.getBonus(TGArmorBonus.FALLDMG) > 0.0f) {
                list.add(trans("armorTooltip.falldamage") + ": -" + this.getBonus(TGArmorBonus.FALLDMG) * 100.0f + "%");
            }
            if (this.getBonus(TGArmorBonus.FREEHEIGHT) > 0.0f) {
                list.add(trans("armorTooltip.fallheight") + ": -" + this.getBonus(TGArmorBonus.FREEHEIGHT));
            }
            if (this.getBonus(TGArmorBonus.BREAKSPEED) > 0.0f) {
                list.add(trans("armorTooltip.miningspeed") + ": +" + this.getBonus(TGArmorBonus.BREAKSPEED) * 100.0f + "%");
            }
            if (this.getBonus(TGArmorBonus.BREAKSPEED_WATER) > 0.0f) {
                list.add(trans("armorTooltip.underwatermining") + ": +" + this.getBonus(TGArmorBonus.BREAKSPEED_WATER) * 100.0f + "%");
            }
            if (this.getBonus(TGArmorBonus.KNOCKBACK_RESISTANCE) > 0.0f) {
                list.add(trans("armorTooltip.knockbackresistance") + ": +" + this.getBonus(TGArmorBonus.KNOCKBACK_RESISTANCE) * 100.0f + "%");
            }
            if (this.getBonus(TGArmorBonus.NIGHTVISION) > 0.0f) {
                list.add(trans("armorTooltip.nightvision"));
            }
            if (this.getBonus(TGArmorBonus.STEPASSIST) > 0.0f) {
                list.add(trans("armorTooltip.stepassist"));
            }
            if (this.getBonus(TGArmorBonus.OXYGEN_GEAR) > 0.0f) {
                list.add(trans("armorTooltip.oxygengear"));
            }
            if (this.getBonus(TGArmorBonus.COOLING_SYSTEM) > 0.0f) {
                list.add(trans("armorTooltip.coolingsystem"));
            }
        } else {
            this.addMinimalInformation(item, list);
            list.add(TextUtil.trans("techguns.gun.tooltip.shift1") + " " + ChatFormatting.GREEN + TextUtil.trans("techguns.gun.tooltip.shift2") + " " + ChatFormatting.GRAY + TextUtil.trans("techguns.gun.tooltip.shift3"));
        }

    }

    /**
     * to override in subclasses
     *
     * @param item
     * @param player
     * @param list
     * @param b
     * @return
     */
    protected void addMinimalInformation(ItemStack item, List<String> list) {
        list.add(trans("armorTooltip.durability") + ": " + (item.getMaxDamage() - item.getItemDamage()) + "/" + (item.getMaxDamage()));
    }

    public float getArmorValue(DamageType type) {
        return this.material.getArmorValueSlot(armorType, type);
    }

    public TGArmorMaterial getTGArmorMaterial() {
        return this.material;
    }

    public void refreshMaterialDerivedStats() {
        this.armorValue = Math.round(this.material.getArmorValueSlot(this.armorType, DamageType.PHYSICAL));
        this.setMaxDamage(this.material.getDurability(this.armorType));
    }

    public float getToughness() {
        return this.material.getToughness();
    }

    @Override
    public boolean getIsRepairable(ItemStack item, @NotNull ItemStack mat) {
        GenericArmor armor = (GenericArmor) item.getItem();
        if (armor.repairItem != null) {
            return OreDictionary.itemMatches(armor.repairItem, mat, true);
        }
        return false;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, @NotNull ItemStack stack,
                            DamageSource source, int damage, int slot) {

        if (!(entity instanceof EntityPlayerMP) || !((EntityPlayer) entity).capabilities.isCreativeMode) {
            if (stack.isItemStackDamageable()) {
                //Keep Armours at 1 durability.
                int maxDmg = stack.getMaxDamage() - 1 - stack.getItemDamage();
                if (damage > maxDmg) {
                    damage = maxDmg;
                }

                if (stack.attemptDamageItem(damage, entity.getRNG(), entity instanceof EntityPlayerMP ? (EntityPlayerMP) entity : null)) {
                    entity.renderBrokenItemStack(stack);

                    if (stack.getItemDamage() > stack.getMaxDamage()) {
                        stack.setItemDamage(stack.getMaxDamage());
                    }
                }
            }
        }

    }

    public boolean isHideFaceslot() {
        return hideFaceslot;
    }

    public GenericArmor setHideFaceslot(boolean hideFaceslot) {
        this.hideFaceslot = hideFaceslot;
        return this;
    }

    public boolean isHideBackslot() {
        return hideBackslot;
    }

    public GenericArmor setHideBackslot(boolean hideBackslot) {
        this.hideBackslot = hideBackslot;
        return this;
    }

    public boolean isHideGloveslot() {
        return hideGloveslot;
    }

    public GenericArmor setHideGloveslot(boolean hideGloveslot) {
        this.hideGloveslot = hideGloveslot;
        return this;
    }


    public GenericArmor setRepairMats(ItemStack metal, ItemStack cloth, float metalpercent, int totalmats) {
        if (metal != null && !metal.isEmpty()) {
            this.repairItem = new ItemStack(metal.getItem(), 1, metal.getItemDamage());
        } else if (cloth != null && !cloth.isEmpty()) {
            this.repairItem = new ItemStack(cloth.getItem(), 1, cloth.getItemDamage());
        }

        if (metal != null && !metal.isEmpty()) {
            this.repairMatMetal = new ItemStack(metal.getItem(), 1, metal.getItemDamage());
        }
        if (cloth != null && !cloth.isEmpty()) {
            this.repairMatCloth = new ItemStack(cloth.getItem(), 1, cloth.getItemDamage());
        }
        this.repairMatCount = totalmats;
        this.repairMatRatioMetal = metalpercent;

        return this;
    }

    public boolean canRepairOnRepairBench(ItemStack item) {
        GenericArmor armor = (GenericArmor) item.getItem();
        return armor.repairMatCount > 0;
    }

    public ArrayList<ItemStack> getRepairMats(ItemStack item) {
        ArrayList<ItemStack> mats = new ArrayList<>();

        if (item.getItemDamage() > 0) {
            GenericArmor armor = (GenericArmor) item.getItem();

            float dmgpercent = (item.getItemDamage() * 1.0f) / ((item.getMaxDamage() - 1) * 1.0f);

            int count = (int) Math.ceil(armor.repairMatCount * dmgpercent);

            int metalcount = (int) Math.ceil(count * armor.repairMatRatioMetal);
            int clothcount = count - metalcount;

            if (armor.repairMatMetal != null && metalcount > 0) {
                mats.add(new ItemStack(armor.repairMatMetal.getItem(), metalcount, armor.repairMatMetal.getItemDamage()));
            }
            if (armor.repairMatCloth != null && clothcount > 0) {
                mats.add(new ItemStack(armor.repairMatCloth.getItem(), clothcount, armor.repairMatCloth.getItemDamage()));
            }
        }
        return mats;
    }

    @Override
    public @NotNull String getTranslationKey(@NotNull ItemStack s) {
        return this.modid + "." + super.getTranslationKey(s);
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, @NotNull ItemStack armor, DamageSource source, double damage, int slot) {
        TGDamageSource src = TGDamageSource.getFromGenericDamageSource(source);

        return new ArmorProperties(0, 1 - DamageSystem.getDamageAfterAbsorb_TGFormula(1.0f, this.getArmorValue(src.damageType), this.material.toughness, src.armorPenetration), Integer.MAX_VALUE);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        if (armor.getItemDamage() >= armor.getMaxDamage() - 1) {
            return 0;
        } else {
            return this.armorValue;
        }
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.modelLoc, "inventory"));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasEffect(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean shouldUseRenderHack(ItemStack stack) {
        return this.use3dRenderHack;
    }

    public boolean usesRenderHack() {
        return use3dRenderHack;
    }

    public void setArmorStat(EnumArmorStat stat, float value) {
        switch (stat) {
            case COOLING_SYSTEM:
                this.coolingsystem = value;
                return;
            case EXTRA_HEARTS:
                this.extraHearts = value;
                return;
            case FALL_DAMAGE:
                this.FallDMG = value;
                return;
            case FALL_HEIGHT:
                this.FallFreeHeight = value;
                return;
            case GUN_ACCURACY:
                this.GunAccuracy = value;
                return;
            case JUMP:
                this.JumpBonus = value;
                return;
            case KNOCKBACK_RESITANCE:
                this.knockbackresistance = value;
                return;
            case MINING_SPEED:
                this.MiningSpeedBonus = value;
                return;
            case NIGHTVISION:
                this.nightvision = value;
                return;
            case OXYGEN_GEAR:
                this.oxygen_gear = value;
                return;
            case SPEED:
                this.SpeedBonus = value;
                return;
            case STEP_ASSIST:
                this.stepassist = value;
                return;
            case WATER_ELECTROLYZER:
                this.water_electrolyzer = value;
                return;
            case WATER_MINING_SPEED:
                this.WaterMiningBonus = value;
                return;
            case WATER_SPEED:
                this.waterspeedbonus = value;
                return;
            case RAD_RESISTANCE:
                this.radresistance = value;
                return;
            default:
        }
    }


    @Override
    public @NotNull Multimap<String, AttributeModifier> getAttributeModifiers(@NotNull EntityEquipmentSlot slot, @NotNull ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

        if (slot == this.armorType && radresistance > 0) {
            multimap.put(TGRadiation.RADIATION_RESISTANCE.getName(), new AttributeModifier(RAD_RESIST_MODIFIER[slot.getIndex()], "techguns.radresistance." + slot, this.radresistance, 0));
        }
        return multimap;
    }

    public ResourceLocation getModelLocation() {
        return this.modelLoc;
    }


}

