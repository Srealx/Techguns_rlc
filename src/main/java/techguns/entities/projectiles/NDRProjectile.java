package techguns.entities.projectiles;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import techguns.TGPackets;
import techguns.TGRadiationSystem;
import techguns.Techguns;
import techguns.api.damagesystem.DamageType;
import techguns.bean.BulletGenericDataBean;
import techguns.client.ClientProxy;
import techguns.damagesystem.DamageSystem;
import techguns.damagesystem.TGDamageSource;
import techguns.deatheffects.EntityDeathUtils.DeathType;
import techguns.enchantment.GenericGunEnchantment;
import techguns.items.guns.GenericGun;
import techguns.items.guns.IProjectileFactory;
import techguns.joint.ICollisionEntityFindProgress;
import techguns.joint.IDamageSourceInitProgress;
import techguns.joint.ModsJointRegistrar;
import techguns.packets.PacketSpawnParticle;
import techguns.util.MathUtil;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NDRProjectile extends AbstractBeamProjectile {

	public static final int BEAM_LIFETIME = 10;
	
	public int shooterID;

	protected Boolean firstDamage = Boolean.TRUE;
	
	public NDRProjectile(World worldIn) {
		super(worldIn);
		if (worldIn.isRemote) {
			Techguns.proxy.createFXOnEntity("BeamGunMuzzleFX", this);
		}
	}
	
	public NDRProjectile(World par2World, EntityLivingBase p, float damage, float speed, int TTL, float spread,
			float dmgDropStart, float dmgDropEnd, float dmgMin, float penetration, boolean blockdamage, EnumBulletFirePos leftGun, ItemStack stack,BulletGenericDataBean bulletGenericDataBean) {
		super(par2World, p, damage, speed, TTL, spread, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, leftGun,stack,bulletGenericDataBean);
		this.damageUuid = bulletGenericDataBean.getDamageUuid();
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		super.writeSpawnData(buffer);
		if (this.shooter != null)
			buffer.writeInt(this.shooter.getEntityId());
		else buffer.writeInt(0);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		super.readSpawnData(additionalData);
		this.shooterID=additionalData.readInt();
	}
	
	@Override
	public void onUpdate() {
		//Update position to shooter
		double targetX = 0;
		double targetY = 0;
		double targetZ = 0;
		double dx, dy, dz;			
		float f = 100.0F;
		Vec3d vec3;
	    Vec3d motion;
		if (this.shooter == null) {
			Entity e = this.world.getEntityByID(shooterID);
			if (e instanceof EntityLivingBase) this.shooter = (EntityLivingBase)e;
		}
		if (shooter != null) {
			dx = (double) (-MathHelper.sin(shooter.rotationYawHead / 180.0F
					* (float) Math.PI)
					* MathHelper.cos(shooter.rotationPitch / 180.0F
							* (float) Math.PI) * f);
			dz = (double) (MathHelper.cos(shooter.rotationYawHead / 180.0F
					* (float) Math.PI)
					* MathHelper.cos(shooter.rotationPitch / 180.0F
							* (float) Math.PI) * f);
			dy = (double) (-MathHelper.sin((shooter.rotationPitch) / 180.0F
					* (float) Math.PI) * f);
	
			this.posX = shooter.posX;				
		    this.posY = shooter.posY;
		    this.posZ = shooter.posZ;
		    		    
			motion = new Vec3d(dx, dy, dz).normalize();
			this.motionX = motion.x * speed;
			this.motionY = motion.y * speed;
			this.motionZ = motion.z * speed;
			
			//System.out.println(String.format("motion: %.3f, %.3f, %.3f", this.motionX, this.motionY, this.motionZ));
			

		    if (Techguns.proxy.isClientPlayerAndIn1stPerson(shooter)){
		    	 Vec3d offset = this.getFPOffset();
		    	 offset = offset.rotatePitch((float) (shooter.rotationPitch*MathUtil.D2R)).rotateYaw((float) ((-90.0-shooter.rotationYawHead)*MathUtil.D2R));
		         //MathUtil.rotateAroundZ(offset, shooter.rotationPitch*MathUtil.D2R);
		         //MathUtil.rotateAroundY(offset, ((-90.0-shooter.rotationYawHead)*MathUtil.D2R));		         
		         this.posX+=offset.x;
		         this.posY+=offset.y+shooter.getEyeHeight();
		         this.posZ+=offset.z;		         
//TODO
//		    } else if (shooter instanceof NPCTurret) {
//		    	Vec3 offset = Vec3.createVectorHelper(0, shooter.getEyeHeight() -0.10000000149011612D, -0.1);
//		         MathUtil.rotateAroundZ(offset, shooter.rotationPitch*MathUtil.D2R);
//		         MathUtil.rotateAroundY(offset, ((-90.0-shooter.rotationYawHead)*MathUtil.D2R));
//		         
//		         this.posX+=offset.xCoord;
//		         this.posY+=offset.yCoord;
//		         this.posZ+=offset.zCoord;	    	
		    }else {
		    	this.posX-=(double)(MathHelper.cos(shooter.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		        this.posY += shooter.getEyeHeight() -0.10000000149011612D + this.get3PYOffset();
		        this.posZ -= (double)(MathHelper.sin(shooter.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		    }
		    
		    vec3 = new Vec3d(this.posX, this.posY, this.posZ);
		    
		    this.rotationPitch = shooter.rotationPitch;
		    this.rotationYaw = shooter.rotationYawHead;
//		}else {
//			
//			dx = (double) (-MathHelper.sin(this.rotationYaw / 180.0F
//					* (float) Math.PI)
//					* MathHelper.cos(this.rotationPitch / 180.0F
//							* (float) Math.PI) * f);
//			dz = (double) (MathHelper.cos(this.rotationYaw / 180.0F
//					* (float) Math.PI)
//					* MathHelper.cos(this.rotationPitch / 180.0F
//							* (float) Math.PI) * f);
//			dy = (double) (-MathHelper.sin((this.rotationPitch) / 180.0F
//					* (float) Math.PI) * f);
//			vec3 = new Vec3d(this.posX, this.posY, this.posZ);
		}
		//---------
		trace();
		--this.ticksToLive;
		if (this.ticksToLive<=0){
			this.setDead();
		}
	}

	protected void trace() {
		Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);

		if (raytraceresult != null) {
			vec3d = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
		}

		/*Entity entity = this.findEntityOnPath(vec3d1, vec3d);

		if (entity != null) {
			raytraceresult = new RayTraceResult(entity);
		}*/
		List<Entity> hitEntityList = Lists.newArrayList();
		RayTraceResult rayTraceResultEntity = this.findEntityOnPath(vec3d1, vec3d,hitEntityList);
		if(rayTraceResultEntity!=null) {
			raytraceresult=rayTraceResultEntity;
		}


		if (raytraceresult != null && raytraceresult.entityHit instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) raytraceresult.entityHit;

			if (this.shooter instanceof EntityPlayer && !((EntityPlayer) this.shooter).canAttackPlayer(entityplayer)) {
				raytraceresult = null;
			}
		}

		if (raytraceresult != null) {
			// 第一次攻击焦点目标
			this.onHit(raytraceresult,raytraceresult.entityHit,this.getDamage());
			hitEntityList.remove(raytraceresult.entityHit);
			// 颠倒顺序，正确顺序攻击前方怪
			Collections.reverse(hitEntityList);
			float reduceDamage = this.damage * 0.8f;
			int reduceCount = 1;
			for (Entity entity : hitEntityList) {
				if (reduceCount<=3){
					reduceDamage -= damage * 0.05;
					reduceCount++;
				}
				this.onHit(raytraceresult,entity,reduceDamage);
			}
			this.isDead = false;
			Vec3d hitVec = raytraceresult.hitVec;
			distance = vec3d1.distanceTo(hitVec);

//			if (!this.world.isRemote){
//				this.createImpactEffect(hitVec);
//			}
		}
		laserPitch = this.rotationPitch;
		laserYaw = this.rotationYaw;
		if (distance <= 0) {
			distance = this.speed;
		}
	}


	/**
	 * Called when the arrow hits a block or an entity
	 */
	protected void onHit(RayTraceResult raytraceResultIn,Entity ent,float damage) {

		if (raytraceResultIn.entityHit != null && !this.world.isRemote) {
			TGDamageSource src = getProjectileDamageSource();

			if (raytraceResultIn.entityHit instanceof EntityLivingBase) {
				float dmg = DamageSystem.getDamageFactor(this.shooter, (EntityLivingBase) ent) * damage;
				src.setOriginalDamage(dmg);
				src.setSourceItemStack(this.sourceItemStack);
				src.setPenetration(this.penetration);
				src.setDamageUuid(this.damageUuid);
				src.setPlaySoundNextDerivativeBullet(Boolean.FALSE);
				src.setPlaySound(Boolean.FALSE);
				src.attacker = this.shooter;
				// other mods progress
				List<IDamageSourceInitProgress> jointList = ModsJointRegistrar.getJointList(IDamageSourceInitProgress.class);
				jointList.forEach(item->item.progress(src));
				if (this.sourceItemStack!=null){
					Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(this.sourceItemStack);
					// 过滤附魔并对附魔进行排序
					List<GenericGunEnchantment> enchantmentList = enchantments.keySet().stream().filter(item -> item instanceof GenericGunEnchantment)
							.map(item -> (GenericGunEnchantment) item).sorted(Comparator.comparingInt(GenericGunEnchantment::getSort)).collect(Collectors.toList());
					// 执行附魔效果
					enchantmentList.forEach(k->k.afterDamageSourceInit(src));
				}
				// 使dmg与orDmg数值保持一致
				dmg = src.getOriginalDamage();
				if (dmg > 0.0f) {
					if (src.knockbackMultiplier==0.0f){
						TGDamageSource knockback = TGDamageSource.getKnockbackDummyDmgSrc(this, this.shooter);
						knockback.knockbackMultiplier = 0.1f;
						knockback.setSourceItemStack(this.sourceItemStack);
						ent.attackEntityFrom(knockback, 0.01f);
					}
					ent.attackEntityFrom(src, dmg);
					if (src.wasSuccessful()) {

						if (ent instanceof EntityLiving) {
							this.setAIRevengeTarget(((EntityLiving) ent));
						}

						this.onHitEffect((EntityLivingBase) ent,raytraceResultIn);
					}
				}

			} else {
				float v = damage;
				raytraceResultIn.entityHit.attackEntityFrom(src,v);
			}
			this.setDead();
		}

		if (raytraceResultIn.typeOfHit == raytraceResultIn.typeOfHit.BLOCK) {
			this.onHitBlock(raytraceResultIn);
		}
	}


	@Nullable
	protected RayTraceResult findEntityOnPath(Vec3d start, Vec3d end, List<Entity> hitEntityList) {
		Entity entity = null;
		List<Entity> list = this.world.getEntitiesInAABBexcluding(this,
				this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D),
				BULLET_TARGETS);
		double d0 = 0.0D;

		Vec3d hitPos = null;

		for (int i = 0; i < list.size(); ++i) {
			Entity entity1 = list.get(i);
			if (entity1 != this.shooter ) {
				AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
				RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);

				if (raytraceresult != null) {
					double d1 = start.squareDistanceTo(raytraceresult.hitVec);

					if (d1 < d0 || d0 == 0.0D) {
						List<ICollisionEntityFindProgress> jointList = ModsJointRegistrar.getJointList(ICollisionEntityFindProgress.class);
						for (ICollisionEntityFindProgress iCollisionEntityFindProgress : jointList) {
							entity1 =  iCollisionEntityFindProgress.progress(entity1);
						}
						if (hitEntityList.contains(entity1)){
							continue;
						}
						entity = entity1;
						hitEntityList.add(entity1);
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
	protected void doImpactEffects(Material mat, RayTraceResult rayTraceResult, SoundType sound) {
		Vec3d hitVec = rayTraceResult.hitVec;
		//TGPackets.network.sendToAllAround(new PacketSpawnParticle("BeamGunImpactFX", hitVec.x, hitVec.y, hitVec.z), TGPackets.targetPointAroundEnt(this, 35.0f));
		Techguns.proxy.createFX("BeamGunImpactFX", this.world, hitVec.x, hitVec.y, hitVec.z, 0f, 0f, 0f);

	}

//	@Override
//	protected void createImpactEffect(Vec3d hitVec) {
//		TGPackets.network.sendToAllAround(new PacketSpawnParticle("BeamGunImpactFX", hitVec.x, hitVec.y, hitVec.z), TGPackets.targetPointAroundEnt(this, 35.0f));
//	}
	
	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass==1;
	}

	
	public Vec3d getFPOffset(){
		//return new Vec3d(0,0,0);
		return new Vec3d(0, -0.08, 0.12);
	}

	public float get3PYOffset(){
		return 0.0f;
	}
	
	public float getBeam3PYOffset() {
		return 0.0f;
	}

	
	@Override
	protected void onHitEffect(EntityLivingBase ent, RayTraceResult rayTraceResultIn) {
		super.onHitEffect(ent, rayTraceResultIn);
		if(TGRadiationSystem.isEnabled()) {
			ent.addPotionEffect(new PotionEffect(TGRadiationSystem.radiation_effect,40,4,false,true));
		}
		if(!ent.isImmuneToFire()) {
			ent.setFire(3);
		}
	}

	@Override
	protected TGDamageSource getProjectileDamageSource() {
		TGDamageSource src = TGDamageSource.causeRadiationDamage(this, this.shooter, DeathType.LASER);
		src.armorPenetration = this.penetration;
		src.setNoKnockback();
		src.setKnockback(0.05f);
		afterGetProjectileDamageSource(src);
		return src;
	}



	public static class Factory implements IProjectileFactory<NDRProjectile> {
		@Override
		public NDRProjectile createProjectile(GenericGun gun, World world, EntityLivingBase p, float damage, float speed,
											  int TTL, float spread, float dmgDropStart, float dmgDropEnd, float dmgMin, float penetration,
											  boolean blockdamage, EnumBulletFirePos firePos, float radius, double gravity, int gunLevel, ItemStack stack, BulletGenericDataBean bulletGenericDataBean) {
			return new NDRProjectile(world, p, damage, speed, BEAM_LIFETIME, spread, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, firePos,stack,bulletGenericDataBean);
		}

		@Override
		public DamageType getDamageType() {
			return DamageType.RADIATION;
		}
	}
}
