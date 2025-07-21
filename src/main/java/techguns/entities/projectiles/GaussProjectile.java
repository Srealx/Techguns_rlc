package techguns.entities.projectiles;

import elucent.albedo.event.GatherLightsEvent;
import elucent.albedo.lighting.ILightProvider;
import elucent.albedo.lighting.Light;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import techguns.TGPackets;
import techguns.TGSounds;
import techguns.Techguns;
import techguns.api.damagesystem.DamageType;
import techguns.bean.BulletGenericDataBean;
import techguns.cache.EnchantmentDataCache;
import techguns.client.ClientProxy;
import techguns.damagesystem.DamageSystem;
import techguns.damagesystem.TGDamageSource;
import techguns.deatheffects.EntityDeathUtils;
import techguns.enchantment.GenericGunEnchantment;
import techguns.items.guns.GenericGun;
import techguns.items.guns.IProjectileFactory;
import techguns.joint.ICollisionEntityFindProgress;
import techguns.joint.IDamageSourceInitProgress;
import techguns.joint.ModsJointRegistrar;
import techguns.packets.PacketSpawnParticleOnEntity;
import techguns.util.MathUtil;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@Optional.Interface(iface="elucent.albedo.lighting.ILightProvider", modid="albedo")
public class GaussProjectile extends AdvancedBulletProjectile implements ILightProvider {

	float orDamage;
	/**
	 * 最多只能攻击8名目标
	 */
	protected int hitCount = 8;

	// 已命中的实体列表, 用于对穿透性子弹的多实体命中计算
	private Set<Entity> hitEntitySet = new HashSet<>(5);

	public GaussProjectile(World par2World, EntityLivingBase p, float damage, float speed, int TTL, float spread,
			float dmgDropStart, float dmgDropEnd, float dmgMin, float penetration, boolean blockdamage, EnumBulletFirePos leftGun,ItemStack stack, BulletGenericDataBean bulletGenericDataBean) {
		super(par2World, p, damage, speed, TTL, spread, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, leftGun,stack,bulletGenericDataBean);
		this.orDamage = damage;
		this.damageUuid = bulletGenericDataBean.getDamageUuid();
	}

	public GaussProjectile(World worldIn) {
		super(worldIn);
		this.orDamage = damage;
		if(worldIn.isRemote) {
			String fx = EnchantmentDataCache.ENCHANTMENT_FX_CACHE.get("gauss");
			if (fx != null){
				ClientProxy.get().createFXOnEntity(fx, this);
				EnchantmentDataCache.ENCHANTMENT_FX_CACHE.remove("gauss");
			}else {
				ClientProxy.get().createFXOnEntity(getParticleEffectConfig(), this);
			}
		}
	}

	protected String getParticleEffectConfig(){
		return "GaussProjectileTrail";
	}


	@Override
	public void onUpdate() {
		if (!this.posInitialized && !this.world.isRemote) {
			this.initStartPos();
		}
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;

		if (!this.world.isRemote) {
			this.setFlag(6, this.isGlowing());
		}
		this.onEntityUpdate();

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
			this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f) * (180D / Math.PI));
			this.prevRotationYaw = this.rotationYaw;
			this.prevRotationPitch = this.rotationPitch;
		}

		--this.ticksToLive;
		Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);

		if (raytraceresult != null) {
			vec3d = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
		}
		List<Entity> hitEntityList = new ArrayList<>();
		RayTraceResult rayTraceResultEntity = this.findEntityOnPathGauss(vec3d1, vec3d,hitEntityList);
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
			for (Entity entity : hitEntityList) {
				this.onHit(raytraceresult,entity,this.getDamage());
			}
		}


		/*
		 * if (this.getIsCritical()) { for (int k = 0; k < 4; ++k) {
		 * this.world.spawnParticle(EnumParticleTypes.CRIT, this.posX +
		 * this.motionX * (double) k / 4.0D, this.posY + this.motionY * (double)
		 * k / 4.0D, this.posZ + this.motionZ * (double) k / 4.0D,
		 * -this.motionX, -this.motionY + 0.2D, -this.motionZ); } }
		 */

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		float f4 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

		/*for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f4) * (180D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
			;
		}*/

		this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f4) * (180D / Math.PI));
		if (this.rotationPitch - this.prevRotationPitch < -180.0F) {
			this.prevRotationPitch -= 360.0F;
		}


		/*while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}*/
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
		//float f2 = 0.05F;

		f1 = this.inWaterUpdateBehaviour(f1);

		this.motionX *= (double) f1;
		this.motionY *= (double) f1;
		this.motionZ *= (double) f1;

		/*if (!this.hasNoGravity()) {
			this.motionY -= 0.05000000074505806D;
		}*/
		if (this.gravity != 0) {
			this.motionY -= this.gravity;
		}

		this.setPosition(this.posX, this.posY, this.posZ);
		this.doBlockCollisions();

		if (this.ticksToLive<=0){
			this.setDead();
		}
	}


	protected void onHit(RayTraceResult raytraceResultIn,Entity ent,float damage) {

		if (raytraceResultIn.entityHit != null && !this.world.isRemote) {
			TGDamageSource src = this.getProjectileDamageSource();

			if (raytraceResultIn.entityHit instanceof EntityLivingBase) {
				float dmg = DamageSystem.getDamageFactor(this.shooter, (EntityLivingBase) ent) * damage;
				src.setOriginalDamage(dmg);
				src.setSourceItemStack(this.sourceItemStack);
				src.setPenetration(this.penetration);
				src.setDamageUuid(this.damageUuid);
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
						knockback.setSourceItemStack(this.sourceItemStack);
						knockback.deathType = getDeathType();
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
			// 生成粒子效果
			generateParticleEffects(raytraceResultIn,3.2f);
			// 记录攻击的实体与数量, 并判定是否达到极限
			this.hitEntitySet.add(ent);
			this.hitCount--;
			if (this.hitCount==0){
				this.setDead();
			}
			// 递减伤害量
			if (this.damage >= this.orDamage * 0.5f){
				this.damage = this.damage - (this.orDamage * 0.15f);
			}
		}

		if (raytraceResultIn.typeOfHit == raytraceResultIn.typeOfHit.BLOCK) {
			this.onHitBlock(raytraceResultIn);
		}
	}

	protected EntityDeathUtils.DeathType getDeathType(){
		return EntityDeathUtils.DeathType.GORE;
	}


	@Override
	protected TGDamageSource getProjectileDamageSource() {
		TGDamageSource src = TGDamageSource.causeBulletDamage(this, this.shooter, EntityDeathUtils.DeathType.GORE);
		src.armorPenetration = this.penetration;
		src.setKnockback(1.2f);
		afterGetProjectileDamageSource(src);
		return src;
	}

	@Override
	protected void doImpactEffects(Material mat, RayTraceResult rayTraceResult, SoundType sound) {
    	double x = rayTraceResult.hitVec.x;
    	double y = rayTraceResult.hitVec.y;
    	double z = rayTraceResult.hitVec.z;
    	boolean distdelay=true;
    	
    	float pitch = 0.0f;
    	float yaw = 0.0f;
    	if (rayTraceResult.typeOfHit == Type.BLOCK) {
    		if (rayTraceResult.sideHit == EnumFacing.UP) {
    			pitch = -90.0f;
    		}else if (rayTraceResult.sideHit == EnumFacing.DOWN) {
    			pitch = 90.0f;
    		}else {
    			yaw = rayTraceResult.sideHit.getHorizontalAngle();
    		}
    	}else {
    		pitch = -this.rotationPitch;
    		yaw = -this.rotationYaw;
    	}
    	
    	if(sound==SoundType.STONE) {
			this.world.playSound(x, y, z, TGSounds.BULLET_IMPACT_STONE, SoundCategory.AMBIENT, 1.0f, 1.0f, distdelay);
			Techguns.proxy.createFX("Impact_BulletRock_Blue", world, x, y, z, 0.0D, 0.0D, 0.0D, pitch, yaw);
			
		} else if(sound==SoundType.WOOD || sound==SoundType.LADDER) {
			this.world.playSound(x, y, z, TGSounds.BULLET_IMPACT_WOOD, SoundCategory.AMBIENT, 1.0f, 1.0f, distdelay);
			Techguns.proxy.createFX("Impact_BulletWood_Blue", world, x, y, z, 0.0D, 0.0D, 0.0D, pitch, yaw);
			
		} else if(sound==SoundType.GLASS) {
			this.world.playSound(x, y, z, TGSounds.BULLET_IMPACT_GLASS, SoundCategory.AMBIENT, 1.0f, 1.0f, distdelay);
			Techguns.proxy.createFX("Impact_BulletGlass_Blue", world, x, y, z, 0.0D, 0.0D, 0.0D, pitch, yaw);
			
		} else if(sound==SoundType.METAL || sound==SoundType.ANVIL) {
			this.world.playSound(x, y, z, TGSounds.BULLET_IMPACT_METAL, SoundCategory.AMBIENT, 1.0f, 1.0f, distdelay);
			Techguns.proxy.createFX("Impact_BulletMetal_Blue", world, x, y, z, 0.0D, 0.0D, 0.0D, pitch, yaw);
			
		} else if(sound ==SoundType.GROUND || sound == SoundType.SAND) {
			this.world.playSound(x, y, z, TGSounds.BULLET_IMPACT_DIRT, SoundCategory.AMBIENT, 1.0f, 1.0f, distdelay);
	    	Techguns.proxy.createFX("Impact_BulletDirt_Blue", world, x, y, z, 0.0D, 0.0D, 0.0D, pitch, yaw);
	    	
		} else {
			this.world.playSound(x, y, z, TGSounds.BULLET_IMPACT_DIRT, SoundCategory.AMBIENT, 1.0f, 1.0f, distdelay);
	    	Techguns.proxy.createFX("Impact_BulletDefault_Blue", world, x, y, z, 0.0D, 0.0D, 0.0D, pitch, yaw);
			//this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0.0D, 0.0D, 0.0D);
		}		
	}
	
	@Override
	protected void onHitEffect(EntityLivingBase ent, RayTraceResult rayTraceResult) {
		super.onHitEffect(ent, rayTraceResult);
		double x = rayTraceResult.hitVec.x;
		double y = rayTraceResult.hitVec.y;
		double z = rayTraceResult.hitVec.z;
		/*if (!this.world.isRemote) {
			TGPackets.network.sendToAllAround(new PacketSpawnParticle("GaussRifleImpact_Block", x,y,z), TGPackets.targetPointAroundEnt(this, 50.0f));
		}else {*/
//			Techguns.proxy.createFX("GaussRifleImpact_Block", world,x,y,z,0,0,0);
			Techguns.proxy.createLightPulse(x,y,z, 5, 10, 3.0f, 1.0f, 0.5f, 0.75f, 1f);
		//}
		
	}

	@Override
	protected boolean hitBlock(RayTraceResult raytraceResultIn) {
		boolean deadFlag = super.hitBlock(raytraceResultIn);
		if (deadFlag){
			generateParticleEffects(raytraceResultIn,4.5f);
			this.setDead();
		}
		return deadFlag;
	}


	@Nullable
	protected RayTraceResult findEntityOnPathGauss(Vec3d start, Vec3d end,List<Entity> hitList) {
		List<Entity> list = this.world.getEntitiesInAABBexcluding(this,
				this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D),
				BULLET_TARGETS);
		double d0 = 0.0D;
		Entity entity = null;
		Vec3d hitPos = null;

		for (int i = 0; i < list.size(); ++i) {
			Entity entity1 = list.get(i);
			if (entity1 != this.shooter && Boolean.FALSE.equals(hitEntitySet.contains(entity1))) {
				AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);

				RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);

				if (raytraceresult != null) {
					double d1 = start.squareDistanceTo(raytraceresult.hitVec);
					if (d1 < d0 || d0 == 0.0D) {
						List<ICollisionEntityFindProgress> jointList = ModsJointRegistrar.getJointList(ICollisionEntityFindProgress.class);
						for (ICollisionEntityFindProgress iCollisionEntityFindProgress : jointList) {
							entity1 =  iCollisionEntityFindProgress.progress(entity1);
						}
						if (hitEntitySet.contains(entity1) || hitList.contains(entity1)){
							continue;
						}
						entity = entity1;
						hitList.add(entity1);
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
	
	public static class Factory implements IProjectileFactory<GaussProjectile> {

		@Override
		public GaussProjectile createProjectile(GenericGun gun, World world, EntityLivingBase p, float damage, float speed, int TTL, float spread, float dmgDropStart, float dmgDropEnd,
												float dmgMin, float penetration, boolean blockdamage, EnumBulletFirePos firePos, float radius, double gravity, int gunLevel, ItemStack stack, BulletGenericDataBean bulletGenericDataBean) {
			//-0.14 -0.09 0.5
			float offsetX = 0.0f;
			if (firePos == EnumBulletFirePos.RIGHT) offsetX = -0.14f;
			else if (firePos == EnumBulletFirePos.LEFT) offsetX = 0.14f;			
			float offsetY = -0.09f;
			float offsetZ = 0.5f;
			
			TGPackets.network.sendToAllAround(new PacketSpawnParticleOnEntity("GaussFireFX", p, offsetX, offsetY, offsetZ, true), TGPackets.targetPointAroundEnt(p, 25.0f));
			return new GaussProjectile(world,p,damage,speed,TTL,spread,dmgDropStart,dmgDropEnd,dmgMin,penetration,blockdamage,firePos,stack,bulletGenericDataBean);
		}

		@Override
		public DamageType getDamageType() {
			return DamageType.PIERCE;
		}

	}
	
	@Optional.Method(modid="albedo")
	@Override
	public Light provideLight() {
		return Light.builder()
				.pos(MathUtil.getInterpolatedEntityPos(this))
				.color(0.5f,0.75f, 1f)
				.radius(2.5f)
				.build();
	}
	
	@Optional.Method(modid="albedo")
	@Override
	public void gatherLights(GatherLightsEvent evt, Entity ent) {
	}
}
