package techguns.entities.projectiles;

import elucent.albedo.event.GatherLightsEvent;
import elucent.albedo.lighting.Light;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import techguns.TGPackets;
import techguns.Techguns;
import techguns.api.damagesystem.DamageType;
import techguns.bean.BulletGenericDataBean;
import techguns.deatheffects.EntityDeathUtils;
import techguns.items.guns.GenericGun;
import techguns.items.guns.IProjectileFactory;
import techguns.items.guns.ammo.DamageModifier;
import techguns.packets.PacketSpawnParticleOnEntity;
import techguns.util.MathUtil;

@Optional.Interface(iface="elucent.albedo.lighting.ILightProvider", modid="albedo")
public class GaussProjectileIncendiary extends GaussProjectile{

	public GaussProjectileIncendiary(World par2World, EntityLivingBase p, float damage, float speed, int TTL, float spread,
                                     float dmgDropStart, float dmgDropEnd, float dmgMin, float penetration, boolean blockdamage, EnumBulletFirePos leftGun,ItemStack stack,BulletGenericDataBean bulletGenericDataBean) {
		super(par2World, p, damage, speed, TTL, spread, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, leftGun,stack,bulletGenericDataBean);
	}

	public GaussProjectileIncendiary(World worldIn) {
		super(worldIn);
	}

	@Override
	protected String getParticleEffectConfig(){
		return "GaussProjectileTrailIncendiary";
	}

	@Override
	protected EntityDeathUtils.DeathType getDeathType(){
		return EntityDeathUtils.DeathType.LASER;
	}

	
	@Override
	protected void onHitEffect(EntityLivingBase ent, RayTraceResult rayTraceResult) {
		super.onHitEffect(ent, rayTraceResult);
		double x = rayTraceResult.hitVec.x;
		double y = rayTraceResult.hitVec.y;
		double z = rayTraceResult.hitVec.z;
		Techguns.proxy.createFX("GaussRifleImpact_Block_Fire", world,x,y,z,0,0,0);
		Techguns.proxy.createLightPulse(x,y,z, 5, 10, 3.0f, 1.0f, 0.5f, 0.75f, 1f);
		if(!ent.isImmuneToFire()) {
			ent.setFire(5);
		}
	}

	
	public static class Factory implements IProjectileFactory<GaussProjectileIncendiary> {
		protected DamageModifier mod = new DamageModifier().setDmg(1.1f, 0f);

		@Override
		public GaussProjectileIncendiary createProjectile(GenericGun gun, World world, EntityLivingBase p, float damage, float speed, int TTL, float spread, float dmgDropStart, float dmgDropEnd,
														  float dmgMin, float penetration, boolean blockdamage, EnumBulletFirePos firePos, float radius, double gravity, int gunLevel, ItemStack stack, BulletGenericDataBean bulletGenericDataBean) {
			//-0.14 -0.09 0.5
			float offsetX = 0.0f;
			if (firePos == EnumBulletFirePos.RIGHT) offsetX = -0.14f;
			else if (firePos == EnumBulletFirePos.LEFT) offsetX = 0.14f;			
			float offsetY = -0.09f;
			float offsetZ = 0.5f;
			
			TGPackets.network.sendToAllAround(new PacketSpawnParticleOnEntity("GaussFireFX", p, offsetX, offsetY, offsetZ, true), TGPackets.targetPointAroundEnt(p, 25.0f));
			return new GaussProjectileIncendiary(world,p,damage,speed,TTL,spread,dmgDropStart,dmgDropEnd,dmgMin,penetration,blockdamage,firePos,stack,bulletGenericDataBean);
		}

		@Override
		public DamageType getDamageType() {
			return DamageType.FIRE;
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
