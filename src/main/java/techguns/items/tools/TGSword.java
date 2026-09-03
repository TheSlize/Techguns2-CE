package techguns.items.tools;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import techguns.*;
import techguns.api.damagesystem.DamageType;
import techguns.api.damagesystem.IToolTGDamageSystem;
import techguns.damagesystem.TGDamageSource;
import techguns.deatheffects.EntityDeathUtils.DeathType;
import techguns.util.ItemUtil;
import techguns.util.TextUtil;

public class TGSword extends ItemSword implements IToolTGDamageSystem {

    protected DamageType dmgType = DamageType.PHYSICAL;
    protected ToolMaterial mat;
    protected float penetration = 0.0f;

    public TGSword(ToolMaterial mat, String name) {
        super(mat);
        this.mat = mat;
        setCreativeTab(Techguns.tabTechgun);
        setRegistryName(name);
        setTranslationKey(Tags.MOD_ID + "." + name);
    }

    @Override
    public TGDamageSource getDamageSource(DamageSource original) {
        TGDamageSource src = new TGDamageSource(original.damageType, original.getImmediateSource(), original.getTrueSource(), this.dmgType, DeathType.GORE);
        src.ignoreHurtresistTime = false;
        src.goreChance = 0.25f;
        return src;
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        String dmgType = String.format(" (%s: %s§7)",
                TextUtil.trans("techguns.gun.tooltip.damageType"),
                this.dmgType.toString());
        tooltip.add(TextUtil.trans("techguns.gun.tooltip.damage") + dmgType + ": §f" + ItemUtil.getMeleeDamage(stack));
        if (this.penetration > 0.0f) {
            tooltip.add(TextUtil.trans("techguns.gun.tooltip.armorPen") + ": " + this.penetration);
        }

    }
}