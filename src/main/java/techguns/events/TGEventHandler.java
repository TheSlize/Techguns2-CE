package techguns.events;

import com.mojang.realmsclient.gui.ChatFormatting;
import elucent.albedo.event.GatherLightsEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techguns.*;
import techguns.api.guns.GunHandType;
import techguns.api.guns.GunManager;
import techguns.api.guns.IGenericGun;
import techguns.api.npc.INpcTGDamageSystem;
import techguns.api.radiation.TGRadiation;
import techguns.api.tginventory.ITGSpecialSlot;
import techguns.api.tginventory.TGSlotType;
import techguns.capabilities.TGExtendedPlayer;
import techguns.client.ClientProxy;
import techguns.client.ShooterValues;
import techguns.client.render.entities.npcs.RenderAttackHelicopter;
import techguns.client.render.entities.projectiles.DeathEffectEntityRenderer;
import techguns.client.render.entities.projectiles.RenderGrenade40mmProjectile;
import techguns.client.render.tileentities.RenderDoor3x3Fast;
import techguns.client.render.tileentities.RenderReactionChamber;
import techguns.damagesystem.DamageSystem;
import techguns.damagesystem.TGDamageSource;
import techguns.deatheffects.EntityDeathUtils;
import techguns.deatheffects.EntityDeathUtils.DeathType;
import techguns.entities.npcs.TGDummySpawn;
import techguns.entities.spawn.TGSpawnManager;
import techguns.gui.player.TGPlayerInventory;
import techguns.gui.widgets.SlotFabricator;
import techguns.gui.widgets.SlotTG;
import techguns.items.armors.GenericArmor;
import techguns.items.armors.TGArmorBonus;
import techguns.items.guns.*;
import techguns.keybind.TGKeybinds;
import techguns.packets.PacketEntityDeathType;
import techguns.packets.PacketNotifyAmbientEffectChange;
import techguns.packets.PacketRequestTGPlayerSync;
import techguns.packets.PacketTGExtendedPlayerSync;
import techguns.radiation.ItemRadiationData;
import techguns.radiation.ItemRadiationRegistry;
import techguns.tileentities.MultiBlockMachineTileEntMaster;
import techguns.tileentities.ReactionChamberTileEntMaster;
import techguns.tileentities.ReactionChamberTileEntSlave;
import techguns.util.BlockUtils;
import techguns.util.InventoryUtil;
import techguns.util.TextUtil;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class TGEventHandler {

    public static Method Block_getSilkTouchDrop = ReflectionHelper.findMethod(Block.class, "getSilkTouchDrop", "func_180643_i", IBlockState.class);

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onMouseEvent(MouseEvent event) {
        EntityPlayerSP ply = Minecraft.getMinecraft().player;

        if (event.getButton() != -1 && ply != null && Minecraft.getMinecraft().inGameHasFocus) {

            int keyCode = event.getButton() - 100;
            boolean primary = keyCode == TGKeybinds.KEY_SHOOT.getKeyCode();

            if ((primary || keyCode == TGKeybinds.KEY_SHOOT_SECONDARY.getKeyCode())
                    && handleGunKey(ply, primary, event.isButtonstate(), keyCode)) {
                event.setCanceled(true);
            }

            if (!event.isCanceled() && keyCode == Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode() && isShootingGun(ply.getHeldItemMainhand())) {
                event.setCanceled(true);
            }

            if (event.isCanceled()) {
                KeyBinding.setKeyBindState(keyCode, false);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (isShootingGun(event.getEntityPlayer().getHeldItemMainhand())) {
            event.setCanceled(true);
        }
    }

    protected static boolean isShootingGun(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof IGenericGun gun && gun.isShootWithLeftClick();
    }

    @SideOnly(Side.CLIENT)
    public static boolean handleGunKey(EntityPlayerSP ply, boolean primary, boolean pressed, int keyCode) {
        if (ply == null) return false;

        ClientProxy cp = ClientProxy.get();
        ItemStack stack_main = ply.getHeldItemMainhand();
        ItemStack stack_off = ply.getHeldItemOffhand();

        if (!pressed) {
            if (primary) {
                cp.keyFirePressedMainhand = false;
            } else {
                cp.keyFirePressedOffhand = false;
            }
        }

        if (primary) {
            if (!stack_main.isEmpty() && stack_main.getItem() instanceof IGenericGun gun) {
                if (gun.isShootWithLeftClick()) {
                    cp.keyFirePressedMainhand = pressed;
                    return true;
                }
                return pressed && isReloading(false);
            }
            return false;
        }

        if (!ply.isSneaking() && !stack_off.isEmpty() && stack_off.getItem() instanceof IGenericGun gunOff && GunManager.canUseOffhand(ply)) {
            if (gunOff.isShootWithLeftClick()) {
                cp.keyFirePressedOffhand = pressed;
                return true;
            }
            return pressed && isReloading(true);
        }

        if (!stack_main.isEmpty() && stack_main.getItem() instanceof GenericGun gunMain) {

            if (gunMain instanceof GenericGunCharge charge) {
                if (charge.getLockOnTicks() > 0) {
                    TGExtendedPlayer props = TGExtendedPlayer.get(ply);
                    props.lockOnEntity = null;
                    props.lockOnTicks = -1;
                }

                int useKey = Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode();
                if (keyCode != useKey) {
                    KeyBinding.setKeyBindState(useKey, pressed);
                    if (pressed) {
                        KeyBinding.onTick(useKey);
                    }
                }
                return false;
            }

            if (gunMain.hasSecondaryAction()) {
                if (pressed) {
                    gunMain.gunSecondaryAction(ply, stack_main);
                }
                return keyCode == Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode();
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    private static boolean isReloading(boolean offhand) {
        long reloadtime = ShooterValues.getReloadtime(ClientProxy.get().getPlayerClient(), offhand);
        return reloadtime > 0 && reloadtime - System.currentTimeMillis() > 0;
    }

    protected static boolean allowOffhandUse(EntityPlayer player, EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND) return true;
        if (!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof IGenericGun g) {
            if (g.getGunHandType() == GunHandType.TWO_HANDED) {
                return false;
            }
        }
        if (!player.getHeldItemOffhand().isEmpty() && player.getHeldItemOffhand().getItem() instanceof IGenericGun g) {
            return g.getGunHandType() != GunHandType.TWO_HANDED;
        }
        return true;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void rightClickEvent(PlayerInteractEvent.RightClickItem event) {
        boolean cancel = !allowOffhandUse(event.getEntityPlayer(), event.getHand());
        if (cancel) {
            event.setCanceled(true);
            event.setCancellationResult(EnumActionResult.PASS);
        }
        //System.out.println("Right Click Item:"+event.getEntityPlayer()+" "+event.getHand());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void rightClickEvent(PlayerInteractEvent.RightClickBlock event) {
        boolean cancel = !allowOffhandUse(event.getEntityPlayer(), event.getHand());
        if (cancel) {
            event.setCanceled(true);
            event.setUseBlock(Result.ALLOW);
            event.setUseItem(Result.DENY);
            event.setCancellationResult(EnumActionResult.PASS);
        } else if (event.getHand() == EnumHand.MAIN_HAND) {
            EntityPlayer ply = event.getEntityPlayer();
            if (ply.isSneaking() && !ply.getHeldItemOffhand().isEmpty() && (ply.getHeldItemOffhand().getItem() instanceof GenericGun) && (!event.getItemStack().isEmpty() && !(event.getItemStack().getItem() instanceof GenericGun))) {
                event.setUseBlock(Result.ALLOW);
            }
        }

		TileEntity te = event.getWorld().getTileEntity(event.getPos());
		ReactionChamberTileEntMaster master = null;

		if (te instanceof ReactionChamberTileEntMaster) {
			master = (ReactionChamberTileEntMaster) te;
		} else if (te instanceof ReactionChamberTileEntSlave) {
			MultiBlockMachineTileEntMaster m = ((ReactionChamberTileEntSlave) te).getMaster();
			if (m instanceof ReactionChamberTileEntMaster) {
				master = (ReactionChamberTileEntMaster) m;
			}
		}

		if (master != null && master.isFormed()) {
			ItemStack heldItem = event.getEntityPlayer().getHeldItem(event.getHand());
			if (!heldItem.isEmpty() && heldItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
				if (FluidUtil.interactWithFluidHandler(event.getEntityPlayer(), event.getHand(), master.inputTank)) {
					event.setCanceled(true);
					event.setCancellationResult(EnumActionResult.SUCCESS);
				}
			}
		}
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void rightClickEvent(PlayerInteractEvent.EntityInteract event) {
        boolean cancel = !TGEventHandler.allowOffhandUse(event.getEntityPlayer(), event.getHand());
        if (cancel) {
            event.setCanceled(true);
            event.setCancellationResult(EnumActionResult.PASS);
        }
        //System.out.println("EntityInteract:"+event.getEntityPlayer()+" "+event.getHand());
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOW) //set to low so other mods don't accidentally destroy it easily
    public static void handleFovEvent(FOVUpdateEvent event) {

        float f = 1.0f;
        if (TGConfig.clientside.cl_lockSpeedFov) {
            IAttributeInstance iattributeinstance = event.getEntity().getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            f = 1 / ((float) (((iattributeinstance.getAttributeValue() / (double) event.getEntity().capabilities.getWalkSpeed() + 1.0D) / 2.0D)));

            if (ClientProxy.get().getPlayerClient().isSprinting()) {
                f *= TGConfig.clientside.cl_fixedSprintFov;
            }
        }
        event.setNewfov(event.getNewfov() * ClientProxy.get().player_zoom * f);//*speedFOV;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onRenderLivingEventPre(RenderLivingEvent.Pre event) {
        if (event.getEntity() instanceof EntityPlayer ply) {

            ItemStack stack = ply.getHeldItemMainhand();
            if (!stack.isEmpty() && stack.getItem() instanceof GenericGun && ((GenericGun) stack.getItem()).hasBowAnim()) {
                ModelBase mdl = event.getRenderer().getMainModel();
                if (mdl instanceof ModelPlayer model) {
                    if (ply.getPrimaryHand() == EnumHandSide.RIGHT) {
                        model.rightArmPose = ArmPose.BOW_AND_ARROW;
                    } else {
                        model.leftArmPose = ArmPose.BOW_AND_ARROW;
                    }
                }
            } else {

                ItemStack stack2 = ply.getHeldItemOffhand();
                if (!stack2.isEmpty() && stack2.getItem() instanceof GenericGun && ((GenericGun) stack2.getItem()).hasBowAnim()) {
                    ModelBase mdl = event.getRenderer().getMainModel();
                    if (mdl instanceof ModelPlayer model) {

                        if (ShooterValues.getIsCurrentlyUsingGun(ply, true)) {

                            if (ply.getPrimaryHand() == EnumHandSide.RIGHT) {
                                model.leftArmPose = ArmPose.BOW_AND_ARROW;
                            } else {
                                model.rightArmPose = ArmPose.BOW_AND_ARROW;
                            }
                        }
                    }
                }
            }
        }

        /*
         * ENTITY DEATH EFFECTS
         */
        ClientProxy cp = ClientProxy.get();
        DeathType dt = cp.getEntityDeathType(event.getEntity());
        switch (dt) {
            case GORE:
                event.setCanceled(true);
                break;
            case DISMEMBER:
            case BIO:
            case LASER:
                event.setCanceled(true);
                DeathEffectEntityRenderer.doRender(event.getRenderer(), event.getEntity(), event.getX(), event.getY(), event.getZ(), 0f, dt);
                break;
            case DEFAULT:
            default:
                break;
        }

    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void OnLivingAttack(LivingAttackEvent event) {
        if (event.getSource() instanceof TGDamageSource) {
            event.setCanceled(true);
            try {
                DamageSystem.attackEntityFrom(event.getEntityLiving(), event.getSource(), event.getAmount());

            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if ((event.getSource() == DamageSource.LAVA || event.getSource() == DamageSource.ON_FIRE || event.getSource() == DamageSource.IN_FIRE) && event.getEntityLiving() instanceof EntityPlayer) {
            float bonus = GenericArmor.getArmorBonusForPlayer((EntityPlayer) event.getEntityLiving(), TGArmorBonus.COOLING_SYSTEM, event.getEntityLiving().world.getTotalWorldTime() % 5 == 0);

            if (bonus >= 1.0f) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof INpcTGDamageSystem) {
            event.setCanceled(true);
            try {
                DamageSystem.livingHurt(event.getEntityLiving(), event.getSource(), event.getAmount());
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onLivingJumpEvent(LivingJumpEvent event) {
        if (event.getEntity() instanceof EntityPlayer ply) {
            float jumpbonus = GenericArmor.getArmorBonusForPlayer(ply, TGArmorBonus.JUMP, true);

            ply.motionY += jumpbonus;
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onLivingFallEvent(LivingFallEvent event) {
        if (event.getEntity() instanceof EntityPlayer ply) {
            boolean consume = event.getDistance() > 3.0f;

            float fallbonus = GenericArmor.getArmorBonusForPlayer(ply, TGArmorBonus.FALLDMG, consume);
            float reduction = (fallbonus < 1 ? 1 - fallbonus : 0.0f);
            float freeheight = GenericArmor.getArmorBonusForPlayer(ply, TGArmorBonus.FREEHEIGHT, false);
            if (freeheight < event.getDistance()) {
                event.setDistance(event.getDistance() - freeheight);
            } else {
                event.setDistance(0.0f);
            }

            event.setDistance(event.getDistance() * reduction);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBreakEventHigh(BreakSpeed event) {
        EntityPlayer ply = event.getEntityPlayer();
        ItemStack item = ply.getHeldItemMainhand();
        if (!item.isEmpty() && item.getItem() instanceof GenericGunMeleeCharge g) {
            if (g.getMiningRadius(item) > 0) {
                EnumFacing sidehit = g.getSideHitMining(ply.world, ply);

                if (sidehit != null) {
                    IBlockState state = event.getState();
                    float mainHardness = state.getBlockHardness(ply.world, event.getPos());

                    List<BlockPos> blocks = BlockUtils.getBlockPlaneAroundAxisForMining(ply.world, ply, event.getPos(), sidehit.getAxis(), g.getMiningRadius(item), false, g, item);
                    float maxHardness = 0f;
                    for (BlockPos p : blocks) {
                        IBlockState s = ply.world.getBlockState(p);
                        float h = s.getBlockHardness(ply.world, p);
                        if (h > maxHardness) {
                            maxHardness = h;
                        }
                    }

                    if (maxHardness > mainHardness) {
                        event.setNewSpeed(event.getNewSpeed() * mainHardness / maxHardness);
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onBreakEvent(BreakSpeed event) {
        EntityPlayer ply = event.getEntityPlayer();
        float bonus = 1.0f + GenericArmor.getArmorBonusForPlayer(ply, TGArmorBonus.BREAKSPEED, true);
        float waterbonus = 1.0f;
        if (ply.isInsideOfMaterial(Material.WATER) || ply.isInsideOfMaterial(Material.LAVA)) {
            waterbonus += GenericArmor.getArmorBonusForPlayer(ply, TGArmorBonus.BREAKSPEED_WATER, true);
        }

        event.setNewSpeed(event.getNewSpeed() * bonus * waterbonus);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (!entity.world.isRemote) {

            if (entity instanceof EntityPlayer) {
                TGExtendedPlayer tgplayer = TGExtendedPlayer.get((EntityPlayer) event.getEntityLiving());
                tgplayer.foodleft = 0;
                tgplayer.lastSaturation = 0;
                tgplayer.addRadiation(-TGRadiationSystem.RADLOST_ON_DEATH);
            }

            if (event.getSource() instanceof TGDamageSource tgs) {
                if (tgs.deathType != DeathType.DEFAULT) {
                    if (Math.random() < tgs.goreChance) {
                        if (EntityDeathUtils.hasSpecialDeathAnim(entity, tgs.deathType)) {
                            //System.out.println("Send packet!");
                            TGPackets.wrapper.sendToAllAround(new PacketEntityDeathType(entity, tgs.deathType), TGPackets.targetPointAroundEnt(entity, 100.0f));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!event.getEntity().world.isRemote) {
            if (event.getEntity() instanceof EntityPlayer ply) {
                TGExtendedPlayer props = TGExtendedPlayer.get(ply);
                if (props != null) {
                    TGPackets.wrapper.sendTo(new PacketTGExtendedPlayerSync(ply, props, true), (EntityPlayerMP) ply);
                    ply.getDataManager().set(TGExtendedPlayer.DATA_FACE_SLOT, props.tg_inventory.getStackInSlot(TGPlayerInventory.SLOT_FACE));
                    ply.getDataManager().set(TGExtendedPlayer.DATA_BACK_SLOT, props.tg_inventory.getStackInSlot(TGPlayerInventory.SLOT_BACK));
                    ply.getDataManager().set(TGExtendedPlayer.DATA_HAND_SLOT, props.tg_inventory.getStackInSlot(TGPlayerInventory.SLOT_HAND));
                }

            } else if (event.getEntity() instanceof TGDummySpawn) {
                TGSpawnManager.handleSpawn(event.getWorld(), event.getEntity());
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getEntityPlayer().world.isRemote) {
            TGPackets.wrapper.sendToServer(new PacketRequestTGPlayerSync(event.getEntityPlayer()));
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onStopTracking(PlayerEvent.StopTracking event) {
        if (event.getEntityPlayer().world.isRemote) {
            TGExtendedPlayer props = TGExtendedPlayer.get(event.getEntityPlayer());
            if (props != null) {
                props.setJumpkeyPressed(false);
                props.isGliding = false;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent event) {
        event.getMap().registerSprite(SlotTG.BACKSLOT_TEX);
        event.getMap().registerSprite(SlotTG.FACESLOT_TEX);
        event.getMap().registerSprite(SlotTG.HANDSLOT_TEX);
        event.getMap().registerSprite(SlotTG.FOODSLOT_TEX);
        event.getMap().registerSprite(SlotTG.HEALSLOT_TEX);
        event.getMap().registerSprite(SlotTG.AMMOSLOT_TEX);

        event.getMap().registerSprite(SlotTG.AMMOEMPTYSLOT_TEX);
        event.getMap().registerSprite(SlotTG.BOTTLESLOT_TEX);
        event.getMap().registerSprite(SlotTG.TURRETGUNSLOT_TEX);
        event.getMap().registerSprite(SlotTG.TURTETARMORSLOT_TEX);

        event.getMap().registerSprite(SlotFabricator.FABRICATOR_SLOTTEX_WIRES);
        event.getMap().registerSprite(SlotFabricator.FABRICATOR_SLOTTEX_POWDER);
        event.getMap().registerSprite(SlotFabricator.FABRICATOR_SLOTTEX_PLATE);

        event.getMap().registerSprite(SlotTG.INGOTSLOT_TEX);
        event.getMap().registerSprite(SlotTG.INGOTDARKSLOT_TEX);

        RenderDoor3x3Fast.stitchTextures(event.getMap());
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        RenderGrenade40mmProjectile.initModel();
        RenderAttackHelicopter.initModels();
        RenderDoor3x3Fast.initModels();
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        float t = 1.0f;
        EntityPlayer ply = ClientProxy.get().getPlayerClient();
        ItemStack stack = ply.getActiveItemStack();

        if (!stack.isEmpty() && ((stack.getItem() instanceof GenericGunCharge && ((GenericGunCharge) stack.getItem()).hasRightClickAction()) || stack.getItem() instanceof GenericGrenade)) {
            EnumHand hand = ply.getActiveHand();

            ItemRenderer itemrenderer = Minecraft.getMinecraft().getItemRenderer();
            try {
                if (hand == EnumHand.MAIN_HAND) {
                    if (itemrenderer.equippedProgressMainHand < t) {
                        itemrenderer.equippedProgressMainHand = t;
                        itemrenderer.prevEquippedProgressMainHand = t;
                    }
                } else {
                    if (itemrenderer.equippedProgressOffHand < t) {
                        itemrenderer.equippedProgressOffHand = t;
                        itemrenderer.prevEquippedProgressOffHand = t;
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

        }

    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        RenderReactionChamber.renderPendingBeams(event.getPartialTicks());
        ClientProxy.get().particleManager.renderParticles(Minecraft.getMinecraft().getRenderViewEntity(), event.getPartialTicks());
        GlStateManager.disableBlend();
        GlStateManager.blendFunc(SourceFactor.ONE, DestFactor.ZERO);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0, 240f);
    }

    @SubscribeEvent
    public static void onCraftEvent(ItemCraftedEvent event) {
        if (event.crafting.getItem() instanceof GenericGun) {
            boolean hasGun = false;
            boolean hasAmmo = false;
            boolean hasInvalid = false;

            ItemStack gun = ItemStack.EMPTY;

            for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
                ItemStack stack = event.craftMatrix.getStackInSlot(i);

                if (!stack.isEmpty()) {
                    if (stack.getItem() instanceof GenericGun) {
                        if (!hasGun) {
                            hasGun = true;
                            gun = stack;
                        } else {
                            hasInvalid = true;
                            break;
                        }
                    } else if (stack.getItem() instanceof ITGSpecialSlot && ((ITGSpecialSlot) stack.getItem()).getSlot(stack) == TGSlotType.AMMOSLOT) {
                        if (!hasAmmo) {
                            hasAmmo = true;
                        } else {
                            hasInvalid = true;
                            break;
                        }

                    } else {
                        hasInvalid = true;
                        break;
                    }
                }
            }
            if (!hasInvalid && hasGun && hasAmmo) {
                //Was an Ammo change recipe!
                GenericGun g = (GenericGun) gun.getItem();
                List<ItemStack> items = g.getAmmoOnUnload(gun);
                items.forEach(i -> {
                    int amount = InventoryUtil.addAmmoToPlayerInventory(event.player, i);
                    if (amount > 0 && !event.player.world.isRemote) {
                        ItemStack it = i.copy();
                        it.setCount(amount);
                        event.player.world.spawnEntity(new EntityItem(event.player.world, event.player.posX, event.player.posY, event.player.posZ, it));
                    }
                });
            }

        }


    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onPlayerDrops(PlayerDropsEvent event) {
        EntityPlayer ply = event.getEntityPlayer();
        TGExtendedPlayer props = TGExtendedPlayer.get(ply);

        if (props != null) {

            props.dropInventory(ply);

        }

    }

    @SubscribeEvent
    public static void onBlockDrops(HarvestDropsEvent event) {
        EntityPlayer ply = event.getHarvester();
        if (ply != null) {
            ItemStack stack = ply.getHeldItemMainhand();
            if (!stack.isEmpty() && stack.getItem() instanceof MiningDrill md && ply.isSneaking()) {
                IBlockState state = event.getState();
                if (state.getBlock().canSilkHarvest(ply.world, event.getPos(), state, ply)) {

                    if (md.getAmmoLeft(stack) > 0) {

                        List<ItemStack> drops = event.getDrops();
                        drops.clear();

                        try {
                            drops.add((ItemStack) Block_getSilkTouchDrop.invoke(state.getBlock(), state));
                        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                            e.printStackTrace();
                        }

                        event.setDropChance(1.0f);
                    }
                }
            }
        }

    }

    //stop XP drop on silk harvesting with mining drill
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBlockBreakEvent(BlockEvent.BreakEvent event) {
        EntityPlayer ply = event.getPlayer();
        if (ply != null) {
            ItemStack stack = ply.getHeldItemMainhand();
            if (!stack.isEmpty() && stack.getItem() instanceof MiningDrill && ply.isSneaking()) {
                event.setExpToDrop(0);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH) //run before regular drop events
    public static void MilitaryCrateDrops(HarvestDropsEvent event) {
        IBlockState state = event.getState();

        if (state.getBlock() == TGBlocks.MILITARY_CRATE && !event.isSilkTouching() && !event.getWorld().isRemote) {
            EntityPlayer ply = event.getHarvester();
            if (ply != null) {
                int fortune = event.getFortuneLevel();

                LootTable loottable = ply.world.getLootTableManager().getLootTableFromLocation(TGBlocks.MILITARY_CRATE.getLootableForState(state));
                LootContext lootcontext = new LootContext.Builder((WorldServer) ply.world).withLuck(fortune).withPlayer(ply).build();

                event.getDrops().clear();
                for (ItemStack itemstack : loottable.generateLootForPools(ply.world.rand, lootcontext)) {
                    event.getDrops().add(itemstack);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onBlockHighlight(DrawBlockHighlightEvent event) {
        EntityPlayer ply = event.getPlayer();
        ItemStack item = ply.getHeldItemMainhand();
        if (!ply.isSneaking() && !item.isEmpty() && item.getItem() instanceof GenericGunMeleeCharge g) {
            if (g.getMiningRadius(item) > 0 && g.getAmmoLeft(item) > 0) {
                RayTraceResult target = event.getTarget();
                if (target != null && target.typeOfHit == Type.BLOCK) {
                    BlockPos p = target.getBlockPos();
                    List<BlockPos> otherblocks = BlockUtils.getBlockPlaneAroundAxisForMining(ply.world, ply, p, target.sideHit.getAxis(), g.getMiningRadius(item), false, g, item);
                    otherblocks.forEach(b -> {
                        RayTraceResult result = new RayTraceResult(new Vec3d(b.getX() + 0.5d, b.getY() + 0.5d, b.getZ() + 0.5d), target.sideHit, b);
                        event.getContext().drawSelectionBox(ply, result, 0, event.getPartialTicks());
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemSwitch(LivingEquipmentChangeEvent event) {
        if (event.getSlot() == EntityEquipmentSlot.MAINHAND || event.getSlot() == EntityEquipmentSlot.OFFHAND) {

            boolean fromEffect = false;
            boolean toEffect = false;
            if (!event.getTo().isEmpty() && event.getTo().getItem() instanceof GenericGun) {
                if (((GenericGun) event.getTo().getItem()).hasAmbientEffect()) {
                    toEffect = true;
                }
            }
            if (!event.getFrom().isEmpty() && event.getFrom().getItem() instanceof GenericGun) {
                if (((GenericGun) event.getFrom().getItem()).hasAmbientEffect()) {
                    fromEffect = true;
                }
            }

            if (fromEffect || toEffect) {
                EnumHand hand = EnumHand.MAIN_HAND;
                if (event.getSlot() == EntityEquipmentSlot.OFFHAND) {
                    hand = EnumHand.OFF_HAND;
                }
                TGPackets.wrapper.sendToDimension(new PacketNotifyAmbientEffectChange(event.getEntityLiving(), hand), event.getEntityLiving().world.provider.getDimension());
            }

        }
    }

    @Optional.Method(modid = "albedo")
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onGatherLightsEvent(GatherLightsEvent event) {
        ClientProxy cp = ClientProxy.get();
        for (int i = 0; i < cp.activeLightPulses.size(); i++) {
            event.add(cp.activeLightPulses.get(i).provideLight());
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void ItemRadiationTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty()) {
            ItemRadiationData data = ItemRadiationRegistry.getRadiationDataFor(stack);
            if (data != null && data.radamount > 0) {
                event.getToolTip().add(ChatFormatting.GREEN + TextUtil.trans("techguns.radiation") + " " + TextUtil.trans("potion.potency." + (data.radamount - 1)));
            }
        }
    }

    @SubscribeEvent
    public static void onEntityConstruction(EntityConstructing event) {

        if (event.getEntity() instanceof EntityLivingBase elb) {
            elb.getAttributeMap().registerAttribute(TGRadiation.RADIATION_RESISTANCE).setBaseValue(0);
        }
    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (left.isEmpty() || !(left.getItem() instanceof GenericArmor) || right.isEmpty()) return;

        Map<Enchantment, Integer> added = EnchantmentHelper.getEnchantments(right);
        if (added.isEmpty()) return;

        boolean isBook = right.getItem() == Items.ENCHANTED_BOOK;
        if (!isBook && right.getItem() != left.getItem()) return;

        ItemStack output = left.copy();
        int repairCost = 0;

        if (!isBook && output.isItemStackDamageable()) {
            int leftDurability = left.getMaxDamage() - left.getItemDamage();
            int rightDurability = right.getMaxDamage() - right.getItemDamage();
            int repaired = rightDurability + output.getMaxDamage() * 12 / 100;
            int newDamage = Math.max(0, output.getMaxDamage() - (leftDurability + repaired));
            if (newDamage < output.getItemDamage()) {
                output.setItemDamage(newDamage);
                repairCost += 2;
            }
        }

        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(output);
        int enchantCost = 0;
        boolean anyApplicable = false;
        boolean anyRejected = false;

        for (Map.Entry<Enchantment, Integer> entry : added.entrySet()) {
            Enchantment ench = entry.getKey();
            if (ench == null) continue;

            int oldLevel = enchants.getOrDefault(ench, 0);
            int newLevel = entry.getValue();
            newLevel = (oldLevel == newLevel) ? newLevel + 1 : Math.max(newLevel, oldLevel);

            boolean applicable = ench.canApply(left);
            for (Enchantment present : enchants.keySet()) {
                if (present != null && present != ench && !ench.isCompatibleWith(present)) {
                    applicable = false;
                    enchantCost++;
                }
            }

            if (!applicable) {
                anyRejected = true;
            } else {
                anyApplicable = true;
                if (newLevel > ench.getMaxLevel()) {
                    newLevel = ench.getMaxLevel();
                }
                enchants.put(ench, newLevel);

                int rarityCost = switch (ench.getRarity()) {
                    case COMMON -> 1;
                    case UNCOMMON -> 2;
                    case RARE -> 4;
                    case VERY_RARE -> 8;
                };

                if (isBook) {
                    rarityCost = Math.max(1, rarityCost / 2);
                }

                enchantCost += rarityCost * newLevel;
            }
        }

        if (anyRejected && !anyApplicable) return;

        int renameCost = 0;
        String name = event.getName();
        if (StringUtils.isBlank(name)) {
            if (left.hasDisplayName()) {
                renameCost = 1;
                output.clearCustomName();
            }
        } else if (!name.equals(left.getDisplayName())) {
            renameCost = 1;
            output.setStackDisplayName(name);
        }

        int prior = left.getRepairCost() + right.getRepairCost();
        int totalCost = prior + repairCost + (enchantCost * 3) + renameCost;

        int nextRepairCost = Math.max(left.getRepairCost(), right.getRepairCost()) * 2 + 1;
        output.setRepairCost(nextRepairCost);
        EnchantmentHelper.setEnchantments(enchants, output);

        event.setOutput(output);
        event.setCost(totalCost);
        event.setMaterialCost(0);
    }
}
