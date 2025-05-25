package techguns.entities.projectiles;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import techguns.TGPackets;
import techguns.Techguns;
import techguns.api.damagesystem.DamageType;
import techguns.bean.BulletGenericDataBean;
import techguns.bean.PitchAndYawBean;
import techguns.client.ClientProxy;
import techguns.deatheffects.EntityDeathUtils;
import techguns.items.guns.GenericGun;
import techguns.items.guns.IProjectileFactory;
import techguns.items.guns.ammo.DamageModifier;
import techguns.packets.PacketSpawnParticle;

import java.util.Set;

public class AdvancedBulletProjectileIncendiary extends AdvancedBulletProjectile{

	protected boolean showFireTrail=true;

	public AdvancedBulletProjectileIncendiary(World worldIn) {
		super(worldIn);
	}


	public AdvancedBulletProjectileIncendiary(World world, EntityLivingBase p, float damage, float speed, int TTL, float spread,
                                              float dmgDropStart, float dmgDropEnd, float dmgMin, float penetration, boolean blockdamage, EnumBulletFirePos leftGun,ItemStack stack,BulletGenericDataBean bulletGenericDataBean) {
		super(world, p, damage, speed, TTL, spread, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, leftGun,stack,bulletGenericDataBean);
		this.chainTargets = bulletGenericDataBean.getBulletRegenerationCount();
	}


	public AdvancedBulletProjectileIncendiary(World world, EntityLivingBase shooter, Entity source, Set<Entity> originTargetSet, PitchAndYawBean pitchAndYawBean, int chainTargets, float damage, float speed, int TTL, float dmgDropStart,
											  float dmgDropEnd, float dmgMin, float penetration, boolean blockdamage, ItemStack stack,String damageUuid) {
		super(world, shooter, source, originTargetSet, pitchAndYawBean ,chainTargets, damage, speed, TTL, dmgDropStart,  dmgDropEnd, dmgMin, penetration, blockdamage,stack,damageUuid);
	}

	public AdvancedBulletProjectileIncendiary(World world, EntityLivingBase shooter, Entity source, PitchAndYawBean pitchAndYawBean,int chainTargets, float damage, float speed, int TTL, float dmgDropStart,
									float dmgDropEnd, float dmgMin, float penetration, boolean blockdamage,ItemStack stack,String damageUuid ) {
		super(world, shooter, source,  pitchAndYawBean ,chainTargets, damage, speed, TTL, dmgDropStart,  dmgDropEnd, dmgMin, penetration, blockdamage,stack,damageUuid);
	}


	@Override
	protected AdvancedBulletProjectileIncendiary createCatapultNew(Entity lastTarget, EntityLivingBase nextTarget){
		return new AdvancedBulletProjectileIncendiary(world, this.shooter, lastTarget, this.originEntitySet,getYawAndPitch(lastTarget,nextTarget) ,this.chainTargets-1, this.damage*CHAIN_DAMAGE_FACTOR, this.speed, TTL, this.damageDropStart,  this.damageDropEnd, this.damageMin * CHAIN_DAMAGE_FACTOR, this.penetration, this.blockdamage,sourceItemStack,this.damageUuid);
	}

	@Override
	protected AdvancedBulletProjectileIncendiary createCatapultNew(Entity lastTarget){
		return new AdvancedBulletProjectileIncendiary(world, this.shooter, lastTarget, new PitchAndYawBean(0.45f, 0f), this.chainTargets-1, this.damage*CHAIN_DAMAGE_FACTOR, this.speed, TTL, this.damageDropStart,  this.damageDropEnd, this.damageMin * CHAIN_DAMAGE_FACTOR, this.penetration, this.blockdamage,sourceItemStack,this.damageUuid);
	}


	@Override
	protected EntityDeathUtils.DeathType getThisDeathType(){
		return EntityDeathUtils.DeathType.LASER;
	}


	@Override
	protected void onHitEffect(EntityLivingBase ent, RayTraceResult rayTraceResultIn) {
		super.onHitEffect(ent, rayTraceResultIn);
		if(!ent.isImmuneToFire()) {
			ent.setFire(3);
		}
	}


	@Override
	public void writeSpawnData(ByteBuf buffer) {
		super.writeSpawnData(buffer);
		buffer.writeBoolean(showFireTrail);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		super.readSpawnData(additionalData);
		this.showFireTrail=additionalData.readBoolean();
		if (showFireTrail) {
			ClientProxy.get().createFXOnEntity("IncendiaryShotgunTrail", this);
		}
	}

	@Override
	protected void generateParticleEffects(RayTraceResult raytraceResultIn,float scale){
		double x = raytraceResultIn.hitVec.x;
		double y = raytraceResultIn.hitVec.y;
		double z = raytraceResultIn.hitVec.z;

		if (!this.world.isRemote) {
			TGPackets.network.sendToAllAround(new PacketSpawnParticle("GaussRifleImpact_Block_Fire", x,y,z,scale), TGPackets.targetPointAroundEnt(this, 50.0f));
		}else {
			Techguns.proxy.createLightPulse(x,y,z, 5, 10, 3.0f, 1.0f, 0.5f, 0.75f, 1f);
		}
	}
	

	public static class Factory implements IProjectileFactory<AdvancedBulletProjectileIncendiary> {
		protected DamageModifier mod = new DamageModifier().setDmg(1.1f, 0f);

		@Override
		public AdvancedBulletProjectileIncendiary createProjectile(GenericGun gun, World world, EntityLivingBase p, float damage, float speed, int TTL, float spread, float dmgDropStart, float dmgDropEnd,
																   float dmgMin, float penetration, boolean blockdamage, EnumBulletFirePos firePos, float radius, double gravity, int gunLevel, ItemStack stack, BulletGenericDataBean bulletGenericDataBean) {
			return new AdvancedBulletProjectileIncendiary(world,p,damage,speed,TTL,spread,dmgDropStart,dmgDropEnd,dmgMin,penetration,blockdamage,firePos,stack,bulletGenericDataBean);
		}

		@Override
		public DamageType getDamageType() {
			return DamageType.FIRE;
		}
		
	}

	
}
