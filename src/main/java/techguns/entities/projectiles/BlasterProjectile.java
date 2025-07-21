package techguns.entities.projectiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import techguns.TGPackets;
import techguns.Techguns;
import techguns.api.damagesystem.DamageType;
import techguns.bean.BulletGenericDataBean;
import techguns.damagesystem.TGDamageSource;
import techguns.deatheffects.EntityDeathUtils.DeathType;
import techguns.items.guns.GenericGun;
import techguns.items.guns.IProjectileFactory;
import techguns.packets.PacketSpawnParticle;

public class BlasterProjectile extends GenericProjectile {

	public BlasterProjectile(World worldIn) {
		super(worldIn);
	}

	public BlasterProjectile(World worldIn, double posX, double posY, double posZ, float yaw, float pitch, float damage, float speed, int TTL, float spread, float dmgDropStart,
			float dmgDropEnd, float dmgMin, float penetration, boolean blockdamage, EnumBulletFirePos leftGun) {
		super(worldIn, posX, posY, posZ, yaw, pitch, damage, speed, TTL, spread, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, leftGun);
	}

	public BlasterProjectile(World par2World, EntityLivingBase p, float damage, float speed, int TTL, float spread, float dmgDropStart, float dmgDropEnd, float dmgMin,
			float penetration, boolean blockdamage, EnumBulletFirePos leftGun,ItemStack stack,BulletGenericDataBean bulletGenericDataBean) {
		super(par2World, p, damage, speed, TTL, spread, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, leftGun,stack);
	}

	@Override
	protected TGDamageSource getProjectileDamageSource() {
		TGDamageSource src = TGDamageSource.causeEnergyDamage(this, this.shooter, DeathType.LASER);
		src.armorPenetration = this.penetration;
		src.setNoKnockback();
		afterGetProjectileDamageSource(src);
		return src;
	}

	public static class Factory implements IProjectileFactory<BlasterProjectile> {

		@Override
		public BlasterProjectile createProjectile(GenericGun gun, World world, EntityLivingBase p, float damage, float speed, int TTL, float spread, float dmgDropStart, float dmgDropEnd,
												  float dmgMin, float penetration, boolean blockdamage, EnumBulletFirePos firePos, float radius, double gravity, int gunLevel, ItemStack stack, BulletGenericDataBean bulletGenericDataBean) {
			return new BlasterProjectile(world,p,damage,speed,TTL,spread,dmgDropStart,dmgDropEnd,dmgMin,penetration,blockdamage,firePos,stack,bulletGenericDataBean);
		}

		@Override
		public DamageType getDamageType() {
			return DamageType.ENERGY;
		}
		
	}

	@Override
	protected void onHitEffect(EntityLivingBase ent, RayTraceResult rayTraceResultIn) {
		super.onHitEffect(ent, rayTraceResultIn);
		if(!ent.isImmuneToFire()) {
			ent.setFire(4);
		}
	}

	@Override
	protected boolean hitBlock(RayTraceResult rayTraceResult) {
		/*if (!this.world.isRemote){
			Vec3d hitVec = raytraceResultIn.hitVec;
			TGPackets.network.sendToAllAround(new PacketSpawnParticle("LaserGunImpact", hitVec.x, hitVec.y, hitVec.z), TGPackets.targetPointAroundEnt(this, 35.0f));
		}*/
		Techguns.proxy.createFX("LaserGunImpact", world, rayTraceResult.hitVec.x, rayTraceResult.hitVec.y, rayTraceResult.hitVec.z, 0,0,0);
		BlockPos targetPos = rayTraceResult.getBlockPos();
		IBlockState target = this.world.getBlockState(targetPos);
		// 当命中玻璃时，摧毁玻璃
		if (target.getBlock()== Blocks.GLASS ||
				target.getBlock() == Blocks.GLASS_PANE ||
				target.getBlock() == Blocks.STAINED_GLASS||
				target.getBlock() == Blocks.STAINED_GLASS_PANE){
			world.destroyBlock(targetPos, Boolean.FALSE);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@Override
	protected float inWaterUpdateBehaviour(float f1) {
		//no slowdown/bubbles in water

		if (this.isWet()) {
			this.extinguish();
		}
		return f1;
	}

	
	
}
