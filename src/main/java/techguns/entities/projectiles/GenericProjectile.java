package techguns.entities.projectiles;

import com.google.common.collect.Sets;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techguns.TGEntities;
import techguns.TGPackets;
import techguns.TGSounds;
import techguns.api.damagesystem.DamageType;
import techguns.api.npc.INPCTechgunsShooter;
import techguns.api.npc.factions.ITGNpcTeam;
import techguns.damagesystem.DamageSystem;
import techguns.damagesystem.TGDamageSource;
import techguns.deatheffects.EntityDeathUtils.DeathType;
import techguns.factions.TGNpcFactions;
import techguns.items.guns.GenericGun;
import techguns.items.guns.IProjectileFactory;
import techguns.packets.PacketGunImpactFX;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class GenericProjectile extends Entity implements IProjectile, IEntityAdditionalSpawnData {

	public static final Predicate<Entity> BULLET_TARGETS = EntitySelectors.CAN_AI_TARGET.and(EntitySelectors.IS_ALIVE).and(Entity::canBeCollidedWith);

	float damage;
	public float speed = 1.0f; // speed in blocks per tick

	protected int ticksToLive = 100;
	protected int lifetime = 100; // TTL but not reduced per tick

	// DMG drop stats
	float damageDropStart;
	float damageDropEnd;
	float damageMin;
	protected EntityLivingBase shooter;

	float penetration = 0.0f;
	static boolean ricochet = true;
	float ricochetAngle = 0.03f; //square of angle in radians
	float ricochetChance = 0.2f;
	boolean silenced = false;
	protected boolean blockdamage = false;

	boolean posInitialized = false;
	boolean hadEnteredWater = false;
	double startX;
	double startY;
	double startZ;
	double squareForAngle;
	
	float radius=0.0f;
	double gravity=0.0f;
	
	public GenericProjectile(World worldIn) {
		super(worldIn);
		this.isImmuneToFire = true;
		this.setNoGravity(true);
		this.height=0.5f;
		this.setSize(0.25F, 0.25F);
	}
	
	public GenericProjectile(World par2World, EntityLivingBase p, float damage, float speed, int TTL, float spread,
			float dmgDropStart, float dmgDropEnd, float dmgMin, float penetration, boolean blockdamage, EnumBulletFirePos firePos) {
		this(par2World);
		this.shooter = p;
		this.initProjectile(p.posX, p.posY + p.getEyeHeight(), p.posZ, p.rotationYawHead, p.rotationPitch, damage, speed,
				TTL, spread, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, firePos);
	}

	public GenericProjectile(World worldIn, double posX, double posY, double posZ, float yaw, float pitch, float damage,
			float speed, int TTL, float spread, float dmgDropStart, float dmgDropEnd, float dmgMin, float penetration,
			boolean blockdamage, EnumBulletFirePos firePos) {
		this(worldIn);
		this.shooter = null;
		this.initProjectile(posX, posY, posZ, yaw, pitch, damage, speed, TTL, spread, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, firePos);
	}

	private void initProjectile(double posX, double posY, double posZ, float yaw, float pitch, float damage,
								float speed, int TTL, float spread, float dmgDropStart, float dmgDropEnd, float dmgMin, float penetration,
								boolean blockdamage, EnumBulletFirePos firePos) {

		float offsetSide=0.16F;
		float Xzoom = offsetSide*0.2f;//-0.35f, 0.1f, 0.05f); //xyz
		float Zzoom = offsetSide*0.02f;
		float offsetHeight=0f;
		if(this.shooter!=null && shooter instanceof INPCTechgunsShooter) {
			INPCTechgunsShooter tgshooter = (INPCTechgunsShooter) this.shooter;
			offsetSide += tgshooter.getBulletOffsetSide();
			offsetHeight += tgshooter.getBulletOffsetHeight();
		}

		this.setLocationAndAngles(posX, posY, posZ, yaw + (float) (spread - (2 * Math.random() * spread)) * 40.0f,
				pitch + (float) (spread - (2 * Math.random() * spread)) * 40.0f);

		if (firePos==EnumBulletFirePos.RIGHT) {
			this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * offsetSide;
			//this.posY -= 0.10000000149011612D;
			this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * offsetSide;
		} else if (firePos==EnumBulletFirePos.LEFT) {
			this.posX += MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * offsetSide;
			//this.posY -= 0.10000000149011612D;
			this.posZ += MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * offsetSide;
		} else if (firePos==EnumBulletFirePos.ZOOMED) {
			this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * Xzoom;
			//this.posY -= 0.10000000149011612D;
			this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * Zzoom;
			//offsetHeight -= Yzoom;
		}

		this.posY += (-0.10000000149011612D+offsetHeight);

		this.setPosition(this.posX, this.posY, this.posZ);
		// this.yOffset = 0.0F;
		float f = 0.4F;
		this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI)
				* MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI) * f;
		this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI)
				* MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI) * f;
		this.motionY = -MathHelper.sin((this.rotationPitch) / 180.0F * (float) Math.PI) * f;
		this.shoot(this.motionX, this.motionY, this.motionZ, 1.5f, spread);

		Vec3d motion = new Vec3d(this.motionX, this.motionY, this.motionZ);
		motion.normalize();
		this.motionX = motion.x;
		this.motionY = motion.y;
		this.motionZ = motion.z;

		this.motionX *= speed;
		this.motionY *= speed;
		this.motionZ *= speed;
		this.damage = damage;
		this.speed = speed;
		this.ticksToLive = TTL;
		this.lifetime = TTL;
		this.penetration = penetration;

		this.damageMin = dmgMin;
		this.damageDropStart = dmgDropStart;
		this.damageDropEnd = dmgDropEnd;
		this.blockdamage = blockdamage;
	}
	
	
	/**
	 * Similar to setArrowHeading, it's point the throwable entity to a x, y, z
	 * direction.
	 */
	@Override
	public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
		float f = MathHelper.sqrt(x * x + y * y + z * z);
		x = x / (double) f;
		y = y / (double) f;
		z = z / (double) f;
		x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		x = x * (double) velocity;
		y = y * (double) velocity;
		z = z * (double) velocity;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		float f1 = MathHelper.sqrt(x * x + z * z);
		this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
		this.rotationPitch = (float) (MathHelper.atan2(y, f1) * (180D / Math.PI));
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
	}

	@Override
	public void onUpdate() {
		if (!this.posInitialized && !this.world.isRemote) {
			this.initStartPos();
		}
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;

		super.onUpdate();

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
			this.rotationPitch = (float) (MathHelper.atan2(this.motionY, f) * (180D / Math.PI));
			this.prevRotationYaw = this.rotationYaw;
			this.prevRotationPitch = this.rotationPitch;
		}

		--this.ticksToLive;
		Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
		vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
		vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

		if (raytraceresult != null) {
			vec3d = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
		}

		RayTraceResult raytraceresultEntity = this.findEntityOnPath(vec3d1, vec3d);
		if(raytraceresultEntity!=null) {
			raytraceresult=raytraceresultEntity;
		}
		
		if (raytraceresult != null && raytraceresult.entityHit instanceof EntityPlayer entityplayer) {

            if (this.shooter instanceof EntityPlayer && !((EntityPlayer) this.shooter).canAttackPlayer(entityplayer)) {
				raytraceresult = null;
			}
		}

		if (raytraceresult != null) {
			this.onHit(raytraceresult);
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		float f4 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
		
		this.rotationPitch = (float) (MathHelper.atan2(this.motionY, f4) * (180D / Math.PI));
		if (this.rotationPitch - this.prevRotationPitch < -180.0F) {
			this.prevRotationPitch -= 360.0F;
		}

		if (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		if (this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		if (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}
		

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		float f1 = 0.99F;

		f1 = this.inWaterUpdateBehaviour(f1);

		this.motionX *= f1;
		this.motionY *= f1;
		this.motionZ *= f1;

		if (this.gravity != 0) {
			this.motionY -= this.gravity;
		}

		this.setPosition(this.posX, this.posY, this.posZ);
		this.doBlockCollisions();
		
		if (this.ticksToLive<=0){
			this.setDead();
		}
	}

	protected float inWaterUpdateBehaviour(float f1) {
		if (this.isInWater()) {
			for (int i = 0; i < 4; ++i) {
				this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D,
						this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY,
						this.motionZ);
			}

			f1 = 0.85F;
			if(!this.hadEnteredWater) {
				world.playSound(this.posX, this.posY, this.posZ, TGSounds.BULLET_IMPACT_WATER, SoundCategory.AMBIENT, 1.0f, 1.0f, true);
				this.hadEnteredWater = true;
			}
		}

		if (this.isWet()) {
			this.extinguish();
		}
		return f1;
	}
	
    protected void initStartPos(){
    	this.startX=this.posX;
    	this.startY=this.posY;
    	this.startZ=this.posZ;
    	this.posInitialized=true;
    }
    
	@Nullable
	protected RayTraceResult findEntityOnPath(Vec3d start, Vec3d end) {
		Entity entity = null;
		List<Entity> list = this.world.getEntitiesInAABBexcluding(this,
				this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D),
				BULLET_TARGETS::test);
		double d0 = 0.0D;

		Vec3d hitPos = null;

		for (Entity entity1 : list) {
			if (entity1 != this.shooter) {
				AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
				RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);

				if (raytraceresult != null) {
					double d1 = start.squareDistanceTo(raytraceresult.hitVec);

					if (d1 < d0 || d0 == 0.0D) {
						entity = entity1;
						d0 = d1;
						hitPos = raytraceresult.hitVec;
					}
				}
			}
		}
		
		if(entity!=null) {
			return new RayTraceResult(entity, hitPos);
		} else {
			return null;
		}
	}

	@Override
	public float getEyeHeight() {
		return 0.25f;
	}

	/**
	 * Called when the arrow hits a block or an entity
	 */
	protected void onHit(RayTraceResult raytraceResultIn) {
		
		if (raytraceResultIn.entityHit != null && !this.world.isRemote) {

			TGDamageSource src = getProjectileDamageSource();

			if (raytraceResultIn.entityHit instanceof EntityLivingBase ent) {

                // Check for Headshot

				float dmg = DamageSystem.getDamageFactor(this.shooter, ent) * this.getDamage();

				if (dmg > 0.0f) {
					
					if (src.knockbackMultiplier==0.0f){
	                	TGDamageSource knockback = TGDamageSource.getKnockbackDummyDmgSrc(this, this.shooter);
		        		ent.attackEntityFrom(knockback, 0.01f);
        			}
					
					ent.attackEntityFrom(src, dmg);
					if (src.wasSuccessful()) {
						
						if (ent instanceof EntityLiving) {
							this.setAIRevengeTarget(((EntityLiving) ent));
						}
	
						this.onHitEffect(ent,raytraceResultIn);
					}
				}

			} else {
				raytraceResultIn.entityHit.attackEntityFrom(src, this.getDamage());

			}

			this.setDead();
		}

		if (raytraceResultIn.typeOfHit == Type.BLOCK) {


			this.hitBlock(raytraceResultIn);
			squareForAngle = this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ;
			if(ricochet && Math.random() < ricochetChance) {
				this.setPosition(raytraceResultIn.hitVec.x, raytraceResultIn.hitVec.y, raytraceResultIn.hitVec.z);
				switch (raytraceResultIn.sideHit) {
					case DOWN: //y-1  
						if((this.motionY * this.motionY / squareForAngle) < ricochetAngle) {
							this.motionY -= 2 * (this.motionY * 1f) * 1f;
						}	
						else
							this.setDead();
						break;
					case UP: //y+1
						if((this.motionY * this.motionY / squareForAngle) < ricochetAngle) {
							this.motionY -= 2 * (this.motionY * (-1f)) * (-1f);
						}
						else
							this.setDead();
						break;
					case NORTH:	//z-1
						if((this.motionZ * this.motionZ / squareForAngle) < ricochetAngle) {
							this.motionZ -= 2 * (this.motionZ * (-1f)) * (-1f);
						}
						else
							this.setDead();
						break;
					case SOUTH: //z+1
						if((this.motionZ * this.motionZ / squareForAngle) < ricochetAngle) {
							this.motionZ -= 2 * (this.motionZ * 1f) * 1f;
						}
						else
							this.setDead();
						break;
					case WEST: //x+1
						if((this.motionX * this.motionX / squareForAngle) < ricochetAngle) {
							this.motionX -= 2 * (this.motionX * 1f) * 1f;
						}
						else
							this.setDead();
						break;
					case EAST: //x-1
						if((this.motionX * this.motionX / squareForAngle) < ricochetAngle) {
							this.motionX -= 2 * (this.motionX * -(1f)) * (-1f);
						}
						else 
							this.setDead();
						break;
					default:
						this.setDead();
				}
				
			}
			else
				this.setDead();

		}


	}

	protected void setAIRevengeTarget(EntityLiving ent) {
		boolean shootBack = false;
		if (this.shooter != null) {
			if (shooter instanceof ITGNpcTeam && ent instanceof ITGNpcTeam) {
				shootBack = TGNpcFactions.isHostile(((ITGNpcTeam) shooter).getTGFaction(),
						((ITGNpcTeam) ent).getTGFaction());
			} else {

				if (!(shooter instanceof EntityPlayer)) {
					shootBack = true;
				}
			}
		}

		if (shootBack) {
			ent.setRevengeTarget(shooter);
		}
	}

	public void shiftForward(float factor) {
		this.posX += this.motionX * factor;
		this.posY += this.motionY * factor;
		this.posZ += this.motionZ * factor;
	}

	
	/**
	 * Override in subclass for entity hit effect (potion effects etc)
	 * @param ent
	 */
	protected void onHitEffect(EntityLivingBase ent, RayTraceResult rayTraceResultIn) {
	}

	/**
	 * Override for block hit effect
	 * @param raytraceResultIn
	 */
	protected void hitBlock(RayTraceResult raytraceResultIn) {
		BlockPos targetPos = raytraceResultIn.getBlockPos();
		IBlockState target = this.world.getBlockState(targetPos);
		Material mat = target.getMaterial();
		SoundType sound = target.getBlock().getSoundType(target, world, targetPos, this);
		this.doImpactEffects(mat, raytraceResultIn, sound);
	}

    protected void doImpactEffects(Material mat, RayTraceResult rayTraceResult, SoundType sound) {
    	double x = rayTraceResult.hitVec.x;
    	double y = rayTraceResult.hitVec.y;
    	double z = rayTraceResult.hitVec.z;

    	float pitch = 0.0f;
    	float yaw = 0.0f;
    	if (rayTraceResult.typeOfHit == Type.BLOCK) {
    		if (rayTraceResult.sideHit == EnumFacing.UP) {
    			pitch = -90.0f;
    		} else if (rayTraceResult.sideHit == EnumFacing.DOWN) {
    			pitch = 90.0f;
    		} else {
    			yaw = rayTraceResult.sideHit.getHorizontalAngle();
    		}
    	}else {
    		pitch = -this.rotationPitch;
    		yaw = -this.rotationYaw;
    	}
    	
    	int type = -1;
		if (rayTraceResult.typeOfHit == Type.BLOCK) {
			Block block = world.getBlockState(rayTraceResult.getBlockPos()).getBlock();
			if (Sets.newHashSet(Blocks.BRICK_BLOCK, Blocks.STONEBRICK, Blocks.MONSTER_EGG, Blocks.END_BRICKS, Blocks.NETHER_BRICK).contains(block)) {
				type = 6;
			}
		}
    	else if(sound == SoundType.STONE) {
			type = 0;
		} else if(sound == SoundType.WOOD || sound == SoundType.LADDER) {
			type = 1;
		} else if(sound == SoundType.GLASS) {
			type = 2;
		} else if(sound == SoundType.METAL || sound == SoundType.ANVIL) {
			type = 3;
		} else if(sound == SoundType.GROUND || sound == SoundType.SAND) {
			type = 4;
		}
    	this.sendImpactFX(x, y, z, pitch, yaw, type);
	}

    protected void sendImpactFX(double x, double y, double z, float pitch, float yaw, int type) {
    	sendImpactFX(x, y, z, pitch, yaw, type, false);
    }
    
    protected void sendImpactFX(double x, double y, double z, float pitch, float yaw, int type, boolean incendiary) {
    	if(!this.world.isRemote) {
    		TGPackets.wrapper.sendToAllAround(new PacketGunImpactFX((short) type,x,y,z,pitch,yaw, incendiary), new TargetPoint(this.world.provider.getDimension(), x,y,z, TGEntities.bulletTrackRange));
    	}
    }
    
	/**
     * Updates the entity motion clientside, called by packets from the server
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void setVelocity(double x, double y, double z)
    {
    }
	
	/**
	 * Override to change damage type etc
	 * 
	 * @return
	 */
	protected TGDamageSource getProjectileDamageSource() {
		TGDamageSource src = TGDamageSource.causeBulletDamage(this, this.shooter, DeathType.GORE);
		src.armorPenetration = this.penetration;
		src.setNoKnockback();
		return src;
	}

	/**
	 * Return the travelled distance
	 * 
	 * @return
	 */
	protected double getDistanceTravelled() {
		Vec3d start = new Vec3d(this.startX, this.startY, this.startZ);
		Vec3d end = new Vec3d(this.posX, this.posY, this.posZ);
		return start.distanceTo(end);
	}

    public void setSilenced(){
    	this.silenced = true;
	}
	
	/**
	 * Get Damage for distance
	 * 
	 * @return
	 */
    protected float getDamage() {

		if (this.damageDropEnd==0f) { //not having damage drop
			return this.damage;
		}
		
		double distance = this.getDistanceTravelled();

		if (distance <= this.damageDropStart) {
			return this.damage;
		} else if (distance > this.damageDropEnd) {
			return this.damageMin;
		} else {
			float factor = 1.0f - (float) ((distance - this.damageDropStart) / (this.damageDropEnd - this.damageDropStart));
			return (this.damageMin + (this.damage - this.damageMin) * factor);
		}
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tags) {
		
		this.damage = tags.getFloat("damage");
		this.speed = tags.getFloat("speed");

		this.ticksToLive = tags.getShort("ticksToLive");
		this.lifetime = tags.getShort("lifetime");

		this.damageDropStart = tags.getShort("damageDropStart");
		this.damageDropEnd = tags.getShort("damageDropEnd");
		this.damageMin = tags.getFloat("damageMin");
		
		this.gravity = tags.getFloat("gravity");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tags) {
		
		
		tags.setFloat("damage", this.damage);
		tags.setFloat("speed", this.speed);

		tags.setShort("ticksToLive", (short) this.ticksToLive);
		tags.setShort("lifetime", (short) this.lifetime);

		tags.setShort("damageDropStart", (short) this.damageDropStart);
		tags.setShort("damageDropEnd", (short) this.damageDropEnd);
		tags.setFloat("damageMin", this.damageMin);
		
		if(this.gravity!=0d) {
			tags.setFloat("gravity", (float)this.gravity);
		}
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeFloat(this.speed);
		buffer.writeDouble(this.motionX);
		buffer.writeDouble(this.motionY);
		buffer.writeDouble(this.motionZ);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		this.speed=additionalData.readFloat();
		this.motionX=additionalData.readDouble();
		this.motionY=additionalData.readDouble();
		this.motionZ=additionalData.readDouble();
	}
	
	public static void burnBlocks(World world, RayTraceResult mop, double chanceToIgnite) {
		if(world.isRemote) return;
		
		if (Math.random() <= chanceToIgnite) {
			BlockPos hit = mop.getBlockPos();

			switch (mop.sideHit) {
				case DOWN:
					if (world.isAirBlock(hit.down())) world.setBlockState(hit.down(), Blocks.FIRE.getDefaultState());
					break;
				case UP:
					if (world.isAirBlock(hit.up())) {
						if (world.getBlockState(hit) == Blocks.FARMLAND.getDefaultState()) world.setBlockState(hit, Blocks.DIRT.getDefaultState());
						world.setBlockState(hit.up(), Blocks.FIRE.getDefaultState());
					}
					break;
				default:
					BlockPos p = hit.offset(mop.sideHit);
					if(world.isAirBlock(p)) {
						world.setBlockState(p, Blocks.FIRE.getDefaultState());
					}
			}
			//TODO: burn blocks ?
    		//Burn special blocks
    	/*	Block b = this.worldObj.getBlock(x, y, z);
    		if (b ==TGBlocks.camoNetRoof || b == TGBlocks.camoNetPane) {
    			// || b == Blocks.wheat || b == Blocks.pumpkin_stem || b == Blocks.melon_stem || b == Blocks.carrots || b == Blocks.potatoes) {
    			this.worldObj.spawnParticle("lava", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
    			this.worldObj.setBlock(x, y, z, Blocks.FIRE);
    		//}else if (b == Blocks.farmland) {
    		//	this.worldObj.setBlock(x, y, z, Blocks.dirt);
    		//	this.worldObj.setBlock(x, y+1, z, Blocks.fire);
    		}else if (this.worldObj.getBlock(x, y-1, z) == Blocks.FARMLAND) {
    			this.worldObj.setBlock(x, y-1, z, Blocks.DIRT);
    			this.worldObj.setBlock(x, y, z, Blocks.FIRE);
    		}
        }*/
		
		}
	}
	
	
	public static class Factory implements IProjectileFactory<GenericProjectile> {

		@Override
		public GenericProjectile createProjectile(GenericGun gun, World world, EntityLivingBase p, float damage, float speed, int TTL, float spread, float dmgDropStart, float dmgDropEnd,
				float dmgMin, float penetration, boolean blockdamage, EnumBulletFirePos firePos, float radius, double gravity) {
			return new GenericProjectile(world,p,damage,speed,TTL,spread,dmgDropStart,dmgDropEnd,dmgMin,penetration,blockdamage,firePos);
		}

		@Override
		public DamageType getDamageType() {
			return DamageType.PROJECTILE;
		}
		
	}
}
