package techguns.keybind;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techguns.TGPackets;
import techguns.*;
import techguns.api.guns.GunManager;
import techguns.capabilities.TGExtendedPlayer;
import techguns.events.TGEventHandler;
import techguns.items.guns.GenericGun;
import techguns.packets.PacketTGKeybindPress;
import techguns.util.InventoryUtil;

@SideOnly(Side.CLIENT)
public class TGKeybinds {

    private static final int MOUSE_LEFT = -100;
    private static final int MOUSE_RIGHT = -99;

    /**
     * Techguns overrides the vanilla attack/use action for guns, so its keys must not be shown as conflicting.
     */
    private static final IKeyConflictContext GUN_ACTION = new IKeyConflictContext() {
        @Override
        public boolean isActive() {
            return KeyConflictContext.IN_GAME.isActive();
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return false;
        }
    };

    public static KeyBinding KEY_SHOOT;
    public static KeyBinding KEY_SHOOT_SECONDARY;

    private static boolean shootKeyDown = false;
    private static boolean secondaryKeyDown = false;

    public static KeyBinding KEY_TOGGLE_NIGHTVISION;
    public static KeyBinding KEY_TOGGLE_SAFEMODE;
    public static KeyBinding KEY_FORCE_RELOAD;
    public static KeyBinding KEY_TOGGLE_JETPACK;

    public static KeyBinding KEY_TOGGLE_AMMO_TYPE;

    /**
     * client only
     */
    public static KeyBinding KEY_TOGGLE_STEPASSIST;

    public static void init() {
        KEY_SHOOT = new KeyBinding("techguns.key.shoot", GUN_ACTION, MOUSE_LEFT, "techguns.key.categories.techguns");
        KEY_SHOOT_SECONDARY = new KeyBinding("techguns.key.shootSecondary", GUN_ACTION, MOUSE_RIGHT, "techguns.key.categories.techguns");

        KEY_TOGGLE_NIGHTVISION = new KeyBinding("techguns.key.toggleNightvision", Keyboard.KEY_N, "techguns.key.categories.techguns");
        KEY_TOGGLE_SAFEMODE = new KeyBinding("techguns.key.toggleSafemode", Keyboard.KEY_B, "techguns.key.categories.techguns");
        KEY_TOGGLE_STEPASSIST = new KeyBinding("techguns.key.toggleStepassist", Keyboard.KEY_V, "techguns.key.categories.techguns");
        KEY_FORCE_RELOAD = new KeyBinding("techguns.key.forceReload", Keyboard.KEY_R, "techguns.key.categories.techguns");
        KEY_TOGGLE_JETPACK = new KeyBinding("techguns.key.toggleJetpack", Keyboard.KEY_J, "techguns.key.categories.techguns");
        KEY_TOGGLE_AMMO_TYPE = new KeyBinding("techguns.key.switchAmmo", Keyboard.KEY_T, "techguns.key.categories.techguns");

        ClientRegistry.registerKeyBinding(KEY_SHOOT);
        ClientRegistry.registerKeyBinding(KEY_SHOOT_SECONDARY);
        ClientRegistry.registerKeyBinding(KEY_TOGGLE_NIGHTVISION);
        ClientRegistry.registerKeyBinding(KEY_TOGGLE_SAFEMODE);
        ClientRegistry.registerKeyBinding(KEY_TOGGLE_STEPASSIST);
        ClientRegistry.registerKeyBinding(KEY_FORCE_RELOAD);
        ClientRegistry.registerKeyBinding(KEY_TOGGLE_JETPACK);
        ClientRegistry.registerKeyBinding(KEY_TOGGLE_AMMO_TYPE);
    }

    /**
     * Mouse bound gun keys are handled in the MouseEvent, since techguns cancels that event and the vanilla
     * keybind state is never set for them. Keyboard bound ones are polled here instead.
     */
    private static void updateGunKeys() {
        Minecraft mc = Minecraft.getMinecraft();
        boolean ingame = mc.player != null && mc.inGameHasFocus;

        boolean shoot = ingame && KEY_SHOOT.getKeyCode() > 0 && KEY_SHOOT.isKeyDown();
        if (shoot != shootKeyDown) {
            shootKeyDown = shoot;
            TGEventHandler.handleGunKey(mc.player, true, shoot, KEY_SHOOT.getKeyCode());
        }

        boolean secondary = ingame && KEY_SHOOT_SECONDARY.getKeyCode() > 0 && KEY_SHOOT_SECONDARY.isKeyDown();
        if (secondary != secondaryKeyDown) {
            secondaryKeyDown = secondary;
            TGEventHandler.handleGunKey(mc.player, false, secondary, KEY_SHOOT_SECONDARY.getKeyCode());
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {

        updateGunKeys();

        if (KEY_TOGGLE_NIGHTVISION.isPressed()) {
            TGPackets.wrapper.sendToServer(new PacketTGKeybindPress(TGKeybindsID.TOGGLE_NIGHTVISION, true));
        } else if (KEY_TOGGLE_SAFEMODE.isPressed()) {
            TGPackets.wrapper.sendToServer(new PacketTGKeybindPress(TGKeybindsID.TOGGLE_SAFEMODE, true));
        } else if (KEY_TOGGLE_JETPACK.isPressed()) {
            TGPackets.wrapper.sendToServer(new PacketTGKeybindPress(TGKeybindsID.TOGGLE_JETPACK, true));
        } else if (KEY_TOGGLE_STEPASSIST.isPressed()) {
            TGPackets.wrapper.sendToServer(new PacketTGKeybindPress(TGKeybindsID.TOGGLE_STEP_ASSIST, true));
        } else if (KEY_TOGGLE_AMMO_TYPE.isPressed()) {
            EntityPlayer ply = Minecraft.getMinecraft().player;
            TGExtendedPlayer props = TGExtendedPlayer.get(Minecraft.getMinecraft().player);
            if (props != null) {

                ItemStack stack_main = ply.getHeldItemMainhand();
                ItemStack stack_off = ply.getHeldItemOffhand();
                EnumHand hand = EnumHand.MAIN_HAND;
                ItemStack gunToReload = stack_main;
                double ammoPercent = 0;
                double ammoPercent_off;
                Techguns.logger.debug("Mainhand: {}, Offhand: {}", stack_main, stack_off);
                if (stack_main.getItem() instanceof GenericGun gunMain) {
                    ammoPercent = gunMain.getPercentAmmoLeft(stack_main);
                }
                if (stack_off.getItem() instanceof GenericGun gunOff) {
                    ammoPercent_off = gunOff.getPercentAmmoLeft(stack_off);
                    if (ammoPercent_off < ammoPercent) {
                        hand = EnumHand.OFF_HAND;
                        gunToReload = stack_off;
                    }
                }
                boolean isAGun = gunToReload.getItem() instanceof GenericGun;
                Techguns.logger.debug("GunToReload: {}, GunToReloadItem: {}, is a gun: {}", gunToReload, gunToReload.getItem(), isAGun);
                if (isAGun) {
                    ((GenericGun) gunToReload.getItem()).toggleAmmoType(gunToReload, ply.world, ply, hand);
                    TGPackets.wrapper.sendToServer(new PacketTGKeybindPress(TGKeybindsID.TOGGLE_AMMO_TYPE, true));
                }
            }
        } else if (KEY_FORCE_RELOAD.isKeyDown()) {
            EntityPlayer ply = Minecraft.getMinecraft().player;

            TGExtendedPlayer props = TGExtendedPlayer.get(Minecraft.getMinecraft().player);
            if (props != null) {

                ItemStack stack_main = ply.getHeldItemMainhand();
                ItemStack stack_off = ply.getHeldItemOffhand();
                boolean canReloadMainhand = this.canReloadGun(props, ply, stack_main, EnumHand.MAIN_HAND);
                boolean canReloadOffhand = GunManager.canUseOffhand(stack_main, stack_off) && this.canReloadGun(props, ply, stack_off, EnumHand.OFF_HAND);


                if (canReloadMainhand || canReloadOffhand) {
                    EnumHand hand;
                    ItemStack gunToReload;

                    if (!canReloadOffhand) { //only mainhand can be reloaded
                        hand = EnumHand.MAIN_HAND;
                        gunToReload = stack_main;
                    } else if (!canReloadMainhand) { //only offhand can be reloaded
                        hand = EnumHand.OFF_HAND;
                        gunToReload = stack_off;
                    } else {    //both can be reloaded

                        GenericGun gunMain = (GenericGun) stack_main.getItem();
                        GenericGun gunOff = (GenericGun) stack_main.getItem();

                        double ammoPercent = gunMain.getPercentAmmoLeft(stack_main);
                        double ammoPercent_off = gunOff.getPercentAmmoLeft(stack_off);

                        if (ammoPercent_off < ammoPercent) {
                            hand = EnumHand.OFF_HAND;
                            gunToReload = stack_off;
                        } else {
                            hand = EnumHand.MAIN_HAND;
                            gunToReload = stack_main;
                        }

                    }

                    TGPackets.wrapper.sendToServer(new PacketTGKeybindPress(TGKeybindsID.FORCE_RELOAD, hand));
                    ((GenericGun) gunToReload.getItem()).tryForcedReload(gunToReload, ply.world, ply, hand);

                }
            }

        }
    }

    private boolean canReloadGun(TGExtendedPlayer props, EntityPlayer ply, ItemStack stack, EnumHand hand) {
        if (!stack.isEmpty() && stack.getItem() instanceof GenericGun gun) {
            if (props.getFireDelay(hand) <= 0 && !gun.isFullyLoaded(stack) && !(ply.getActiveItemStack() == stack)) {

                ItemStack[] ammo = gun.getReloadItem(stack);
                for (ItemStack s : ammo) {
                    if (!InventoryUtil.canConsumeAmmoPlayer(ply, s)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
