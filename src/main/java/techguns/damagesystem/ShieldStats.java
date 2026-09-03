package techguns.damagesystem;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import techguns.TGSounds;
import techguns.api.damagesystem.DamageType;

import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * Defensive stats of a shield against projectiles
 */
public class ShieldStats {

    public static final HashMap<Item, ShieldStats> SHIELD_STATS = new HashMap<>();
    public static ShieldStats VANILLA_SHIELD = new ShieldStats(ShieldMaterial.WOOD, 0d, new double[]{
            1.0d,    //PHYSICAL
            0.35d,    //PROJECTILE
            0.35d,    //FIRE
            0.35d,    //EXPLOSION
            0.35d,    //ENERGY
            0.35d,    //POISON
            0.0d,    //UNRESISTABLE
            0.35d,    //ICE
            0.35d,    //LIGHTNING
            0.0d,    //RADIATION
            0.0d    //DARK
    });
    /**
     * used for modded shields by default
     */
    public static ShieldStats DEFAULT_STATS = new ShieldStats(ShieldMaterial.METAL, 0d, new double[]{
            1.0d,    //PHYSICAL
            0.5d,    //PROJECTILE
            0.5d,    //FIRE
            0.5d,    //EXPLOSION
            0.5d,    //ENERGY
            0.5d,    //POISON
            0.0d,    //UNRESISTABLE
            0.5d,    //ICE
            0.5d,    //LIGHTNING
            0.25d,    //RADIATION
            0.25d    //DARK
    });
    public static ShieldStats RIOT_SHIELD_STATS = new ShieldStats(ShieldMaterial.GLASS, 0d, new double[]{
            1.0d,    //PHYSICAL
            0.65d,    //PROJECTILE
            0.60d,    //FIRE
            0.60d,    //EXPLOSION
            0.60d,    //ENERGY
            0.60d,    //POISON
            0.0d,    //UNRESISTABLE
            0.60d,    //ICE
            0.60d,    //LIGHTNING
            0.40d,    //RADIATION
            0.40d    //DARK
    });
    public static ShieldStats BALLISTIC_SHIELD_STATS = new ShieldStats(ShieldMaterial.METAL, 1.5d, new double[]{
            1.0d,    //PHYSICAL
            0.8d,    //PROJECTILE
            0.70d,    //FIRE
            0.70d,    //EXPLOSION
            0.70d,    //ENERGY
            0.70d,    //POISON
            0.0d,    //UNRESISTABLE
            0.70d,    //ICE
            0.70d,    //LIGHTNING
            0.60d,    //RADIATION
            0.60d    //DARK
    });
    public static ShieldStats ADVANCED_SHIELD_STATS = new ShieldStats(ShieldMaterial.METAL, 3d, new double[]{
            1.0d,    //PHYSICAL
            0.9d,    //PROJECTILE
            0.80d,    //FIRE
            0.80d,    //EXPLOSION
            0.80d,    //ENERGY
            0.80d,    //POISON
            0.0d,    //UNRESISTABLE
            0.80d,    //ICE
            0.80d,    //LIGHTNING
            0.75d,    //RADIATION
            0.75d    //DARK
    });
    //damage threshold, damage less than this will have no effect, subtract from reduced damage
    protected double dt;
    //reduction rate for each damage type, 0.0= take full damage, 1.0= take no damage
    protected double[] rates;
    //used for sound effects
    protected ShieldMaterial mat;

    public ShieldStats(ShieldMaterial mat, double dt, double[] rates) {
        super();
        this.mat = mat;
        this.dt = dt;
        this.rates = rates;
    }

    public static void playBlockSound(EntityLivingBase ent) {
        World w = ent.getEntityWorld();
        if (!w.isRemote) {
            ItemStack active = ent.getActiveItemStack();
            if (!active.isEmpty()) {
                ShieldStats s = getStats(active, ent);
                if (s != null) {
                    SoundEvent snd = getSound(s.mat);
                    if (snd != null) {
                        w.playSound(null, ent.posX, ent.posY, ent.posZ, snd, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    }
                }
            }
        }
    }

    protected static SoundEvent getSound(ShieldMaterial mat) {
        return switch (mat) {
            case GLASS -> TGSounds.BULLET_IMPACT_GLASS;
            case GROUND -> TGSounds.BULLET_IMPACT_DIRT;
            case METAL -> TGSounds.BULLET_IMPACT_METAL;
            case STONE -> TGSounds.BULLET_IMPACT_STONE;
            case WOOD -> TGSounds.BULLET_IMPACT_WOOD;
            default -> null;
        };
    }

    @Nullable
    public static ShieldStats getStats(ItemStack stack, EntityLivingBase entity) {
        ShieldStats s = null;
        if (!stack.isEmpty() && stack.getItem().isShield(stack, entity)) {
            s = ShieldStats.SHIELD_STATS.get(stack.getItem());
    		
            if (s == null) {
                s = ShieldStats.DEFAULT_STATS;
            }
        }
        return s;
    }

    /**
     * for tooltip
     *
     * @param dt
     * @return
     */
    public double getReductionAgainstType(DamageType dt) {
        return this.rates[dt.ordinal()] * 24;
    }

    public float getAmount(float amount, TGDamageSource src) {

        double d = amount * (1d - rates[src.damageType.ordinal()]);

        double effective_dt = Math.max(dt - (src.armorPenetration * 2d), 0d);

        return (float) Math.max(d - effective_dt, 0d);
    }

    public enum ShieldMaterial {
        WOOD,
        STONE,
        METAL,
        GLASS,
        GROUND,
        NONE
    }
}
