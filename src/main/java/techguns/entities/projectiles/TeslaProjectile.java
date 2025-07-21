package techguns.entities.projectiles;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
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
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import techguns.api.damagesystem.DamageType;
import techguns.bean.BulletGenericDataBean;
import techguns.damagesystem.DamageSystem;
import techguns.damagesystem.TGDamageSource;
import techguns.deatheffects.EntityDeathUtils.DeathType;
import techguns.enchantment.GenericGunEnchantment;
import techguns.items.guns.GenericGun;
import techguns.items.guns.IProjectileFactory;
import techguns.joint.ICollisionEntityFindProgress;
import techguns.joint.IDamageSourceInitProgress;
import techguns.joint.ModsJointRegistrar;
import techguns.util.MathUtil;

import javax.annotation.Nullable;

public class TeslaProjectile extends AbstractBeamProjectile implements ICatapultProjectile{

	public static final int TTL = 10;
	public static final float CHAIN_RANGE = 6.0f;
	public static final int CHAIN_TARGETS = 2;
	protected float CHAIN_DAMAGE_FACTOR = 0.4f;
	
	public long seed = 0;
	
	protected int chainTargets = CHAIN_TARGETS;
	protected Entity prevTarget = null;

	protected Float toDamage;

	protected float waterDamageMulti = 1.3f;

	protected Entity sourceEntity;
	
	public TeslaProjectile(World worldIn) {
		super(worldIn);
		this.seed = worldIn.rand.nextLong();
	}
	
//	public TeslaProjectile(World worldIn, double posX, double posY, double posZ, float yaw, float pitch, float damage,
//			float speed, int TTL, float spread, float dmgDropStart, float dmgDropEnd, float dmgMin, float penetration,
//			boolean blockdamage, boolean leftGun) {
//		super(worldIn, posX, posY, posZ, yaw, pitch, damage, speed, TTL, spread, dmgDropStart, dmgDropEnd, dmgMin, penetration,
//				blockdamage, leftGun);
//	}
	
	public TeslaProjectile(World par2World, EntityLivingBase p, float damage, float speed, int TTL, float spread,
			float dmgDropStart, float dmgDropEnd, float dmgMin, float penetration, boolean blockdamage, EnumBulletFirePos leftGun, ItemStack stack,BulletGenericDataBean bulletGenericDataBean) {
		super(par2World, p, damage, speed, TTL, spread, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, leftGun,stack,bulletGenericDataBean);
		this.chainTargets = bulletGenericDataBean.getBulletRegenerationCount();
		this.damageUuid = bulletGenericDataBean.getDamageUuid();
		trace();
	}
	
	public TeslaProjectile(World world, EntityLivingBase shooter, Entity source, EntityLivingBase target, int chainTargets, float damage, float speed, int TTL, float dmgDropStart,
			float dmgDropEnd, float dmgMin, float penetration, boolean blockdamage, ItemStack stack,String damageUuid, Entity sourceEntity) {
		super(world, shooter, damage, speed, TTL, 0, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, EnumBulletFirePos.CENTER,stack,new BulletGenericDataBean(damageUuid));
		maxTicks = (short) this.ticksToLive;
		this.damageUuid = damageUuid;
		this.chainTargets = chainTargets;
		this.CHAIN_DAMAGE_FACTOR = 1.0f;
		this.prevTarget = target;
		//TODO Align Projectile from Source to Target
		this.posX = source.posX;
		this.posY = source.posY+ source.getEyeHeight()*0.5f;
		this.posZ = source.posZ;
		Vec3d src = new Vec3d(posX, posY, posZ);
		Vec3d tgt = new Vec3d(target.posX, target.posY+target.getEyeHeight()*0.5f, target.posZ);
		Vec3d dir = tgt.subtract(src).normalize();
		
		this.distance = src.distanceTo(tgt);
		this.laserPitch = (float) (Math.asin(-dir.y) * MathUtil.R2D);
		this.laserYaw = (float) (Math.atan2(dir.x, dir.z) * MathUtil.R2D);
		
		//System.out.printf("pitch : %.3f,  yaw : %.3f,  distance : %.3f\n", laserPitch, laserYaw, distance);
		
		this.rotationPitch = laserPitch;
		this.rotationYaw = laserYaw;
		
		this.motionX = dir.x*this.speed;
		this.motionY = dir.y*this.speed;
		this.motionZ = dir.z*this.speed;
		this.toDamage = damage;
		//trace();
		this.sourceEntity = sourceEntity;
		
		if (distance <= 0) {
			distance = this.speed;
		}
		
		RayTraceResult raytraceresult = new RayTraceResult(target);

		if (raytraceresult != null) {
			this.onHit(raytraceresult);
			this.isDead = false;
//			if (!this.world.isRemote){
//				this.createImpactEffect(tgt);
//			}
		}
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
			if (entity1 != this.shooter
					&& (this.sourceEntity != null ? Boolean.FALSE.equals(entity1.equals(sourceEntity)) : Boolean.TRUE)) {
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

	@Override
	protected void onHit(RayTraceResult raytraceResultIn) {
		if (raytraceResultIn.entityHit != null && !this.world.isRemote) {
			TGDamageSource src = getProjectileDamageSource();

			EntityLivingBase ent = (EntityLivingBase) raytraceResultIn.entityHit;
			float dmg;
			if (toDamage!=null){
				dmg = DamageSystem.getDamageFactor(this.shooter, ent) * toDamage;
			}else {
				dmg = DamageSystem.getDamageFactor(this.shooter, ent) * this.getDamage();
			}
			// 增加: 如果目标处于水中, 那么伤害增加 30%
			Block blockBelow = world.getBlockState(new BlockPos(ent.posX, ent.posY, ent.posZ)).getBlock();
			boolean isInWater = blockBelow == Blocks.WATER;
			// 判断目标是否在雨水中
			boolean canSeeSky = world.canSeeSky(new BlockPos(ent.posX, ent.posY + 1, ent.posZ));
			boolean inTheRain = world.isRaining()&&canSeeSky;
			if (isInWater||inTheRain){
				dmg = dmg * waterDamageMulti;
			}
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
//					if (src.knockbackMultiplier==0.0f){
//						TGDamageSource knockback = TGDamageSource.getKnockbackDummyDmgSrc(this, this.shooter);
//						knockback.setSourceItemStack(this.sourceItemStack);
//						ent.attackEntityFrom(knockback, 0.01f);
//					}
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

			this.setDead();
			if (!world.isRemote) {
				Entity lastTarget = raytraceResultIn.entityHit;
				if (lastTarget != null && this.chainTargets > 0) {
					EntityLivingBase nextTarget = findNextTarget(lastTarget);
					if (nextTarget != null) {
						TeslaProjectile proj = new TeslaProjectile(world, this.shooter, lastTarget, nextTarget, chainTargets-1,
								dmg*CHAIN_DAMAGE_FACTOR, this.speed, TTL, this.damageDropStart,  this.damageDropEnd,
								this.damageMin*CHAIN_DAMAGE_FACTOR, this.penetration, this.blockdamage,sourceItemStack,this.damageUuid,ent);
						world.spawnEntity(proj);
					}
				}
			}
		}
		if (raytraceResultIn.typeOfHit == raytraceResultIn.typeOfHit.BLOCK) {
			this.onHitBlock(raytraceResultIn);
		}
	}

	@Override
	protected void onHitEffect(EntityLivingBase ent, RayTraceResult rayTraceResultIn) {
		super.onHitEffect(ent, rayTraceResultIn);
		PotionEffect slownessEffect = new PotionEffect(Potion.getPotionById(2), 12, 2, true, true);
		ent.addPotionEffect(slownessEffect);
	}
	
	private EntityLivingBase findNextTarget(Entity lastTarget) {
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
	            
	            if (distance < CHAIN_RANGE && entity.isEntityAlive() && entity != lastTarget) {
            		if (!entity.equals(shooter) && !entity.equals(prevTarget) && !(entity instanceof EntityPlayer)) {
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
        }
		return null;
	}
	
	
	
	@Override
	protected TGDamageSource getProjectileDamageSource() {
		TGDamageSource src = TGDamageSource.causeLightningDamage(this, this.shooter, DeathType.LASER);
		src.armorPenetration = this.penetration;
		src.setNoKnockback();
		src.goreChance=0.5f;
		afterGetProjectileDamageSource(src);
		return src;
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass==1;
	}

	@Override
	public int getCatapultTargetNumber() {
		return CHAIN_TARGETS + 1;
	}

	public static class Factory implements IProjectileFactory<TeslaProjectile> {
		@Override
		public TeslaProjectile createProjectile(GenericGun gun, World world, EntityLivingBase p, float damage, float speed,
				int TTL, float spread, float dmgDropStart, float dmgDropEnd, float dmgMin, float penetration,
				boolean blockdamage, EnumBulletFirePos firePos, float radius, double gravity, int gunLevel, ItemStack stack, BulletGenericDataBean bulletGenericDataBean) {
			return new TeslaProjectile(world, p, damage, speed, TeslaProjectile.TTL, spread, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, firePos,stack,bulletGenericDataBean);
		}

		@Override
		public DamageType getDamageType() {
			return DamageType.LIGHTNING;
		}
	}

}
