package techguns.entities.projectiles;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import techguns.TGPackets;
import techguns.TGSounds;
import techguns.Techguns;
import techguns.api.damagesystem.DamageType;
import techguns.bean.BulletGenericDataBean;
import techguns.bean.PitchAndYawBean;
import techguns.damagesystem.DamageSystem;
import techguns.damagesystem.TGDamageSource;
import techguns.deatheffects.EntityDeathUtils;
import techguns.enchantment.GenericGunEnchantment;
import techguns.items.guns.GenericGun;
import techguns.items.guns.IProjectileFactory;
import techguns.joint.ICollisionEntityFindProgress;
import techguns.joint.IDamageSourceInitProgress;
import techguns.joint.ModsJointRegistrar;
import techguns.packets.PacketSpawnParticle;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class AdvancedBulletProjectile extends GenericProjectile {

	public static final float CHAIN_RANGE = 16.0f;
	protected Set<Entity> originEntitySet = new HashSet<>();
	protected int chainTargets;
	public static final int TTL = 10;
	protected  float CHAIN_DAMAGE_FACTOR = 0.6f;

	/**
	 * 当子弹为弹射子弹时, 生成该子弹的damage参数已经被距离算法计算过，所以此处不能再用damage去算距离算法，给此值表明直接数值不参与其他动态计算
	 */
//	protected Float toDamage;
	/**
	 * 子弹为弹射子弹时, 该对象标记来源mob, 以防伤害判定到来源身上
	 */
	protected Entity sourceEntity;

	/**
	 * 射击时的子弹构造
	 */
	public AdvancedBulletProjectile(World world, EntityLivingBase p, float damage, float speed, int TTL, float spread,
			float dmgDropStart, float dmgDropEnd, float dmgMin, float penetration, boolean blockdamage, EnumBulletFirePos leftGun,ItemStack stack,BulletGenericDataBean bulletGenericDataBean) {
		super(world, p, damage, speed, TTL, spread, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, leftGun,stack);
		this.chainTargets = bulletGenericDataBean.getBulletRegenerationCount();
		this.damageUuid = bulletGenericDataBean.getDamageUuid();

	}
	
	public AdvancedBulletProjectile(World worldIn) {
		super(worldIn);
	}

	/**
	 * 弹射到entity上时构建该对象的构造方法
	 */
	public AdvancedBulletProjectile(World world, EntityLivingBase shooter, Entity source, Set<Entity> originTargetSet, PitchAndYawBean pitchAndYawBean,int chainTargets, float damage, float speed, int TTL, float dmgDropStart,
						   float dmgDropEnd, float dmgMin, float penetration, boolean blockdamage,ItemStack stack,String damageUuid) {
		super(world, shooter,  source.posX, source.posY + source.getEyeHeight(), source.posZ, pitchAndYawBean.getYaw(),pitchAndYawBean.getPitch(), damage, speed, TTL, 0, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, EnumBulletFirePos.CENTER,stack);
		this.chainTargets = chainTargets;
		this.originEntitySet = originTargetSet;
		this.sourceEntity = source;
		this.damageUuid = damageUuid;
		// 子弹第一次衰减默认值，后续衰减减少
		this.CHAIN_DAMAGE_FACTOR = 0.8f;
	}
	/**
	 * 弹射到entity上时构建该对象的构造方法
	 */
	public AdvancedBulletProjectile(World world, EntityLivingBase shooter, Entity source, PitchAndYawBean pitchAndYawBean,int chainTargets, float damage, float speed, int TTL, float dmgDropStart,
									float dmgDropEnd, float dmgMin, float penetration, boolean blockdamage,ItemStack stack,String damageUuid) {
		super(world, shooter,source.posX, source.posY + source.getEyeHeight(), source.posZ,pitchAndYawBean.getYaw(),pitchAndYawBean.getPitch(), damage, speed, TTL, 0, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, EnumBulletFirePos.CENTER,stack);
		this.chainTargets = chainTargets;
		this.sourceEntity = source;
		this.damageUuid = damageUuid;
		// 子弹第一次衰减默认值，后续衰减减少
		this.CHAIN_DAMAGE_FACTOR = 0.8f;
	}

	/**
	 * 弹射到block上时构建该对象的构造方法
	 */
	public AdvancedBulletProjectile(World world, EntityLivingBase shooter, double x, double y, double z, EntityLivingBase target, PitchAndYawBean pitchAndYawBean,int chainTargets, float damage, float speed, int TTL, float dmgDropStart,
									float dmgDropEnd, float dmgMin, float penetration, boolean blockdamage,ItemStack stack) {
		super(world, shooter,  x, y , z, pitchAndYawBean.getYaw(),pitchAndYawBean.getPitch(), damage, speed, TTL, 0, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, EnumBulletFirePos.CENTER,stack);
		this.chainTargets = chainTargets;
		this.sourceEntity = shooter;
	}



	@Override
	protected void onHit(RayTraceResult raytraceResultIn) {

		if (raytraceResultIn.entityHit != null && !this.world.isRemote) {
			EntityLivingBase ent = (EntityLivingBase) raytraceResultIn.entityHit;
			TGDamageSource src = getProjectileDamageSource();
			float dmg = DamageSystem.getDamageFactor(this.shooter, ent) * this.getDamage();
			src.setOriginalDamage(dmg);
			src.setPenetration(this.penetration);
			src.setSourceItemStack(this.sourceItemStack);
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
			if (raytraceResultIn.entityHit instanceof EntityLivingBase) {
				if (dmg > 0.0f) {
					if (src.knockbackMultiplier==0.0f){
						TGDamageSource knockback = TGDamageSource.getKnockbackDummyDmgSrc(this, this.shooter);
						knockback.deathType = getThisDeathType();
						knockback.setSourceItemStack(this.sourceItemStack);
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
				float v = this.getDamage();
				raytraceResultIn.entityHit.attackEntityFrom(src,v);
			}
			generateParticleEffects(raytraceResultIn,1.4f);
			if (this.chainTargets > 0){
				Entity lastTarget = raytraceResultIn.entityHit;
				if (lastTarget != null && !this.world.isRemote){
					originEntitySet.add(lastTarget);
					EntityLivingBase nextTarget = findNextTarget(lastTarget);
					if (nextTarget != null) {
						// 生成一个往下一目标位置发射的新子弹
						world.spawnEntity(createCatapultNew(lastTarget,nextTarget));
					}else {
						// 生成一个弹跳角度的新子弹
						world.spawnEntity(createCatapultNew(lastTarget));
					}
				}
//				else if (raytraceResultIn.typeOfHit == Type.BLOCK){
//					EntityLivingBase nextTarget = findNextTargetByBlock(raytraceResultIn.getBlockPos());
//					world.spawnEntity(createCatapultNew(lastTarget,nextTarget,dmg));
//				}
			}

			this.setDead();
		}

		if (raytraceResultIn.typeOfHit == Type.BLOCK) {
			this.onHitBlock(raytraceResultIn);
			generateParticleEffects(raytraceResultIn,2.0f);
		}
	}

	protected EntityDeathUtils.DeathType getThisDeathType(){
		return EntityDeathUtils.DeathType.DISMEMBER;
	}


	/**
	 * 生成子弹碰撞的粒子效果
	 * @param raytraceResultIn
	 * @param scale 粒子大小倍率
	 */
	protected void generateParticleEffects(RayTraceResult raytraceResultIn,float scale){
		double x = raytraceResultIn.hitVec.x;
		double y = raytraceResultIn.hitVec.y;
		double z = raytraceResultIn.hitVec.z;

		if (!this.world.isRemote) {
			TGPackets.network.sendToAllAround(new PacketSpawnParticle("GaussRifleImpact_Block", x,y,z,scale), TGPackets.targetPointAroundEnt(this, 50.0f));
		}else {
			Techguns.proxy.createLightPulse(x,y,z, 5, 10, 3.0f, 1.0f, 0.5f, 0.75f, 1f);
		}
	}

	protected AdvancedBulletProjectile createCatapultNew(Entity lastTarget,EntityLivingBase nextTarget){
		return new AdvancedBulletProjectile(world, this.shooter, lastTarget, this.originEntitySet,getYawAndPitch(lastTarget,nextTarget), this.chainTargets-1, this.damage*CHAIN_DAMAGE_FACTOR, this.speed, TTL, this.damageDropStart,  this.damageDropEnd, this.damageMin*CHAIN_DAMAGE_FACTOR, this.penetration, this.blockdamage,sourceItemStack,this.damageUuid);
	}

	protected AdvancedBulletProjectile createCatapultNew(Entity lastTarget){
		float pitchNext = this.pitch + 90;
		return new AdvancedBulletProjectile(world, this.shooter, lastTarget, new PitchAndYawBean(this.yaw,pitchNext), this.chainTargets-1, this.damage*CHAIN_DAMAGE_FACTOR, this.speed, TTL, this.damageDropStart,  this.damageDropEnd, this.damageMin*CHAIN_DAMAGE_FACTOR, this.penetration, this.blockdamage,sourceItemStack,this.damageUuid);
	}


	private EntityLivingBase findNextTargetByBlock(BlockPos blockPos){
		List<Entity> list = this.world.getEntitiesInAABBexcluding(null, new AxisAlignedBB(blockPos.getX()
				- CHAIN_RANGE, blockPos.getY()
				- CHAIN_RANGE, blockPos.getZ()
				- CHAIN_RANGE, blockPos.getX()
				+ CHAIN_RANGE, blockPos.getY()
				+ CHAIN_RANGE, blockPos.getZ()
				+ CHAIN_RANGE), BULLET_TARGETS);
		for (int i1 = 0; i1 < list.size(); ++i1)
		{
			Entity e = list.get(i1);
			if (e instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase)e;

				double distance = entity.getDistance(blockPos.getX(), blockPos.getY()+0.5, blockPos.getX());

				if (distance < CHAIN_RANGE && entity.isEntityAlive()) {
					if (!entity.equals(shooter) && !originEntitySet.contains(entity)) {
						Vec3d vec3d1 = new Vec3d(blockPos.getX(), blockPos.getY()+0.5, blockPos.getZ());
						Vec3d vec3d2 = new Vec3d(entity.posX, entity.posY+entity.getEyeHeight()*0.5f, entity.posZ);
						RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d2, false, true, false);
						if (raytraceresult == null){
							return entity;
						}else if (raytraceresult.typeOfHit == raytraceresult.typeOfHit.BLOCK){
							IBlockState target = this.world.getBlockState(blockPos);
							Material mat = target.getMaterial();
							SoundType sound = target.getBlock().getSoundType(target, world, blockPos, this);
							this.doImpactEffects(mat, raytraceresult, sound);
							// 当命中玻璃时，摧毁玻璃
							if (target.getBlock()== Blocks.GLASS ||
									target.getBlock() == Blocks.GLASS_PANE ||
									target.getBlock() == Blocks.STAINED_GLASS||
									target.getBlock() == Blocks.STAINED_GLASS_PANE){
								world.destroyBlock(blockPos, Boolean.FALSE);
							}
						}
					}
				}
			}
		}
		return null;

	}

	protected EntityLivingBase findNextTarget(Entity lastTarget) {
		List<Entity> list = this.world.getEntitiesInAABBexcluding(lastTarget, new AxisAlignedBB(lastTarget.posX
				- CHAIN_RANGE, lastTarget.posY
				- CHAIN_RANGE, lastTarget.posZ
				- CHAIN_RANGE, lastTarget.posX
				+ CHAIN_RANGE, lastTarget.posY
				+ CHAIN_RANGE, lastTarget.posZ
				+ CHAIN_RANGE), BULLET_TARGETS);

		for (int i1 = 0; i1 < list.size(); ++i1)
		{
			Entity e = list.get(i1);
			if (e instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase)e;

				double distance = entity.getDistance(lastTarget.posX, lastTarget.posY+lastTarget.getEyeHeight()*0.5f, lastTarget.posZ);

				if (distance < CHAIN_RANGE && entity.isEntityAlive() && !originEntitySet.contains(entity) && !entity.equals(shooter) && Boolean.FALSE.equals(entity instanceof EntityPlayer)) {
					Vec3d vec3d1 = new Vec3d(lastTarget.posX, lastTarget.posY+lastTarget.getEyeHeight()*0.5f, lastTarget.posZ);
					Vec3d vec3d2 = new Vec3d(entity.posX, entity.posY+entity.getEyeHeight()*0.5f, entity.posZ);
					RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d2, false, true, false);
					if (raytraceresult == null){
						return entity;
					}else if (raytraceresult.typeOfHit == raytraceresult.typeOfHit.BLOCK){
						BlockPos targetPos = raytraceresult.getBlockPos();
						IBlockState target = this.world.getBlockState(targetPos);
						Material mat = target.getMaterial();
						SoundType sound = target.getBlock().getSoundType(target, world, targetPos, this);
						this.doImpactEffects(mat, raytraceresult, sound);
						// 当命中玻璃时，摧毁玻璃
						if (target.getBlock()== Blocks.GLASS ||
								target.getBlock() == Blocks.GLASS_PANE ||
								target.getBlock() == Blocks.STAINED_GLASS||
								target.getBlock() == Blocks.STAINED_GLASS_PANE){
							world.destroyBlock(targetPos, Boolean.FALSE);
						}
					}
				}
			}
		}
		return null;
	}


	@Nullable
	@Override
	protected RayTraceResult findEntityOnPath(Vec3d start, Vec3d end) {
		Entity entity = null;
		List<Entity> list = this.world.getEntitiesInAABBexcluding(this,
				this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D),
				BULLET_TARGETS);
		double d0 = 0.0D;

		Vec3d hitPos = null;

		for (int i = 0; i < list.size(); ++i) {
			Entity entity1 = list.get(i);
			if (entity1 != this.shooter &&
					(this.sourceEntity != null ? Boolean.FALSE.equals(entity1.equals(sourceEntity)) : Boolean.TRUE)) {
				AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
				RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);

				if (raytraceresult != null) {
					double d1 = start.squareDistanceTo(raytraceresult.hitVec);

					if (d1 < d0 || d0 == 0.0D) {
						List<ICollisionEntityFindProgress> jointList = ModsJointRegistrar.getJointList(ICollisionEntityFindProgress.class);
						for (ICollisionEntityFindProgress iCollisionEntityFindProgress : jointList) {
							entity1 =  iCollisionEntityFindProgress.progress(entity1);
						}
						if (this.sourceEntity != null &&  entity1 == this.sourceEntity){
							continue;
						}
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
	

	public static class Factory implements IProjectileFactory<AdvancedBulletProjectile> {

		@Override
		public AdvancedBulletProjectile createProjectile(GenericGun gun, World world, EntityLivingBase p, float damage, float speed, int TTL, float spread, float dmgDropStart, float dmgDropEnd,
														 float dmgMin, float penetration, boolean blockdamage, EnumBulletFirePos firePos, float radius, double gravity, int gunLevel, ItemStack stack, BulletGenericDataBean bulletGenericDataBean) {
			return new AdvancedBulletProjectile(world,p,damage,speed,TTL,spread,dmgDropStart,dmgDropEnd,dmgMin,penetration,blockdamage,firePos,stack,bulletGenericDataBean);
		}

		@Override
		public DamageType getDamageType() {
			return DamageType.PROJECTILE;
		}
		
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



	// 计算 A 到 B 的 yaw 和 pitch
	protected PitchAndYawBean getYawAndPitch(Entity fromEntity, Entity toEntity) {
		// 获取 A 和 B 的位置坐标
		Vec3d fromPos = fromEntity.getPositionVector(); // A 的位置
		Vec3d toPos = toEntity.getPositionVector();     // B 的位置

		// 计算方向向量 (从 A 到 B)
		Vec3d direction = toPos.subtract(fromPos);

		// 计算 pitch（俯仰角，垂直方向的角度）
		double deltaX = direction.x;
		double deltaY = direction.y;
		double deltaZ = direction.z;
		double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

		float pitch = -(float) Math.toDegrees(Math.atan2(deltaY, horizontalDistance)); // atan2 用于计算俯仰角

		// 计算 yaw（偏航角，水平方向的角度）
		float yaw = (float) Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0f; // atan2 用于计算水平方向角度，减去90是为了调整朝向

		return new PitchAndYawBean(pitch,yaw);

	}

	
}
