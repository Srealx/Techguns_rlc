package techguns.entities.projectiles;

import elucent.albedo.event.GatherLightsEvent;
import elucent.albedo.lighting.ILightProvider;
import elucent.albedo.lighting.Light;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import techguns.TGPackets;
import techguns.TGSounds;
import techguns.Techguns;
import techguns.api.damagesystem.DamageType;
import techguns.bean.BulletGenericDataBean;
import techguns.client.audio.TGSoundCategory;
import techguns.entities.projectiles.effect.TFGBoomExplosion;
import techguns.items.guns.GenericGun;
import techguns.items.guns.IChargedProjectileFactory;
import techguns.packets.PacketPlaySound;
import techguns.packets.PacketSpawnParticle;
import techguns.util.MathUtil;

@Optional.Interface(iface="elucent.albedo.lighting.ILightProvider", modid="albedo")
public class TFGProjectile extends GenericProjectile implements IEntityAdditionalSpawnData, ILightProvider{

	public float size = 1.0f;


	public TFGProjectile(World worldIn, double posX, double posY, double posZ, float yaw, float pitch, float damage, float speed, int TTL, float spread, float dmgDropStart,
			float dmgDropEnd, float dmgMin, float penetration, boolean blockdamage, EnumBulletFirePos leftGun, double gravity, float size) {
		super(worldIn, posX, posY, posZ, yaw, pitch, damage, speed, TTL, spread, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, leftGun);
		this.size=size;
		this.gravity=gravity;
	}

	public TFGProjectile(World par2World, EntityLivingBase p, float damage, float speed, int TTL, float spread, float dmgDropStart, float dmgDropEnd, float dmgMin,
			float penetration, boolean blockdamage, EnumBulletFirePos leftGun, double gravity, float size, ItemStack stack, BulletGenericDataBean bulletGenericDataBean) {
		super(par2World, p, damage, speed, TTL, spread, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, leftGun,stack);
		this.size=size;
		this.gravity=gravity;
		this.damageUuid = bulletGenericDataBean.getDamageUuid();
	}
	
	public TFGProjectile(World worldIn) {
		super(worldIn);
	}
	
	@Override
	protected void onHitEffect(EntityLivingBase ent, RayTraceResult rayTraceResult) {
		this.explode(rayTraceResult,Boolean.FALSE);
		if(!ent.isImmuneToFire()) {
			ent.setFire(3);
		}
	}

	@Override
	protected boolean hitBlock(RayTraceResult raytraceResultIn) {
		// 击碎玻璃
		BlockPos targetPos = raytraceResultIn.getBlockPos();
		IBlockState target = this.world.getBlockState(targetPos);
		if (target.getBlock()==Blocks.GLASS ||
				target.getBlock() == Blocks.GLASS_PANE ||
				target.getBlock() == Blocks.STAINED_GLASS||
				target.getBlock() == Blocks.STAINED_GLASS_PANE ||
				target.getBlock() == Blocks.ICE||
				target.getBlock() == Blocks.PACKED_ICE||
				target.getBlock() == Blocks.FROSTED_ICE){
			SoundType sound = target.getBlock().getSoundType(target, world, targetPos, this);
			Material mat = target.getMaterial();
			this.doImpactEffects(mat, raytraceResultIn, sound);
			world.destroyBlock(targetPos, Boolean.FALSE);
			return Boolean.FALSE;
		}
		this.explode(raytraceResultIn,Boolean.TRUE);
		return Boolean.TRUE;
	}

	protected void explode(RayTraceResult raytraceResultIn, boolean hitBlockFlag){
		if (!this.world.isRemote){
//			float exp_dmgMax = this.damage*0.66f * size;
//			float exp_dmgMin = this.damage*0.33f * size;
//			float exp_r1 = size*1.0f;
//			float exp_r2 = size*2.0f;

			TGPackets.network.sendToAllAround(new PacketSpawnParticle("TFGExplosion", this.posX,this.posY,this.posZ, size*0.75f), TGPackets.targetPointAroundEnt(this, 100.0f));

//			TGExplosion explosion = new TGExplosion(world, this.shooter, this, posX, posY, posZ, exp_dmgMax, exp_dmgMin, exp_r1, exp_r2, this.blockdamage?0.5:0.0);
//			explosion.blockDropChance = 0.1f;
//
//			explosion.doExplosion(false);

			TGPackets.network.sendToAllAround(new PacketPlaySound(TGSounds.TFG_EXPLOSION, this, 5.0f, 1.0f, false, false, false, TGSoundCategory.EXPLOISON), TGPackets.targetPointAroundEnt(this, 200.0f));

			if (hitBlockFlag){
				// 触发一个爆炸，伤害为基础伤害40%, 范围按size计算
				float strength = this.size>1.0f?2.4f*this.size*0.65f:1.8f;
				new TFGBoomExplosion(this.world,this.shooter,posX,posY,posZ,this.damage * 0.4f,
						strength,null,this,this.penetration,this.sourceItemStack,this.damageUuid).doExplosionA();
			}else {
				EntityLivingBase ent = (EntityLivingBase) raytraceResultIn.entityHit;
				BlockPos position = ent.getPosition();
				// 触发一个爆炸，伤害为基础伤害40%, 范围按size计算
				float strength = this.size>1.0f?2.4f*this.size*0.65f:1.8f;
				new TFGBoomExplosion(ent.world,this.shooter,position.getX(),position.getY(),position.getZ(),this.damage * 0.4f,
						strength,ent,this,this.penetration,this.sourceItemStack,this.damageUuid).doExplosionA();
			}


			//SoundUtil.playSoundAtEntityPos(world, this, TGSounds.TFG_EXPLOSION, 1.0f, 1.0f, false, TGSoundCategory.EXPLOISON);
			
			if (this.size > 3.0f) {
				//SoundUtil.playSoundAtEntityPos(world, this, TGSounds.TFG_EXPLOSION_ECHO, 1.0f, 1.0f, false, TGSoundCategory.EXPLOISON);
				TGPackets.network.sendToAllAround(new PacketPlaySound(TGSounds.TFG_EXPLOSION_ECHO, this, 10.0f, 1.0f, false, false, false, TGSoundCategory.EXPLOISON), TGPackets.targetPointAroundEnt(this, 200.0f));
				
			}
			
		}else {
			Techguns.proxy.createLightPulse(this.posX, this.posY, this.posZ, 5, 15, 5f+(size), 1f+(size*0.5f), 0.5f, 1.0f, 0.5f);
		}
		this.setDead();
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		super.writeSpawnData(buffer);
		buffer.writeFloat(this.size);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		super.readSpawnData(additionalData);
		this.size=additionalData.readFloat();
		Techguns.proxy.createFXOnEntity("TFGTrail", this, this.size*0.75f);
	}
	
	public static class Factory implements IChargedProjectileFactory<TFGProjectile> {

		@Override
		public TFGProjectile createProjectile(GenericGun gun, World world, EntityLivingBase p, float damage, float speed, int TTL, float spread, float dmgDropStart, float dmgDropEnd,
				float dmgMin, float penetration, boolean blockdamage, EnumBulletFirePos firePos, float radius, double gravity, int gunLevel, ItemStack stack, BulletGenericDataBean bulletGenericDataBean) {
			return this.createChargedProjectile(world, p, damage, speed, TTL, spread, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, firePos, radius, gravity, 0f, 1,stack,bulletGenericDataBean);
		}

		@Override
		public DamageType getDamageType() {
			return DamageType.ENERGY;
		}
		
		@Override
		public TFGProjectile createChargedProjectile(World world, EntityLivingBase p, float damage, float speed, int TTL, float spread, float dmgDropStart, float dmgDropEnd,
				float dmgMin, float penetration, boolean blockdamage, EnumBulletFirePos firePos, float radius, double gravity, float charge, int ammoConsumed, ItemStack stack, BulletGenericDataBean bulletGenericDataBean) {
			TFGProjectile proj = new TFGProjectile(world,p,damage,speed,TTL,spread,dmgDropStart,dmgDropEnd,dmgMin,penetration,blockdamage,firePos,gravity,ammoConsumed,stack,bulletGenericDataBean);
			proj.size = 1.0f+(charge*3.0f);
			if (charge >= 1.0f) {
				proj.size += 1.0f;
			}
			// 当为右键蓄力攻击时， 根据蓄力系数调整伤害
			if (proj.size>1.0f){
				float additionalSize = proj.size - 1.0f;
				float additionalCoefficient = additionalSize * 0.125f;
				proj.damage = proj.damage * (1+additionalCoefficient);
			}
			//-0.14f, -0.10f, 0.42f
//			float offsetX = 0.0f;
//			if (firePos == EnumBulletFirePos.RIGHT) offsetX = -0.14f;
//			else if (firePos == EnumBulletFirePos.LEFT) offsetX = 0.14f;			
//			float offsetY = -0.1f;
//			float offsetZ = 0.42f;
//			
//			TGPackets.network.sendToAllAround(new PacketSpawnParticleOnEntity("TFGFire", p, offsetX, offsetY, offsetZ, true), TGPackets.targetPointAroundEnt(p, 50.0f));			
			return proj;
		
		}
				
	}
	
	@Optional.Method(modid="albedo")
	@Override
	public Light provideLight() {
		return Light.builder()
				.pos(MathUtil.getInterpolatedEntityPos(this))
				.color(0.5f,1.0f, 0.5f)
				.radius(2f+(size*0.5f))
				.build();
	}
	
	@Optional.Method(modid="albedo")
	@Override
	public void gatherLights(GatherLightsEvent evt, Entity ent) {
	}
}