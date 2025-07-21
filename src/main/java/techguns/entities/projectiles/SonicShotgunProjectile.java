package techguns.entities.projectiles;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import techguns.TGPackets;
import techguns.api.damagesystem.DamageType;
import techguns.bean.BulletGenericDataBean;
import techguns.damagesystem.DamageSystem;
import techguns.damagesystem.TGDamageSource;
import techguns.deatheffects.EntityDeathUtils.DeathType;
import techguns.enchantment.GenericGunEnchantment;
import techguns.items.guns.GenericGun;
import techguns.items.guns.IProjectileFactory;
import techguns.joint.IDamageSourceInitProgress;
import techguns.joint.ModsJointRegistrar;
import techguns.packets.PacketSpawnParticle;

public class SonicShotgunProjectile extends GenericProjectile implements IEntityAdditionalSpawnData{
	
	public ArrayList<Entity> entitiesHit;

	public boolean mainProjectile = false;

	public SonicShotgunProjectile(World worldIn, double posX, double posY, double posZ, float yaw, float pitch,
			float damage, float speed, int TTL, float spread, float dmgDropStart, float dmgDropEnd, float dmgMin,
			float penetration, boolean blockdamage, EnumBulletFirePos firePos) {
		super(worldIn, posX, posY, posZ, yaw, pitch, damage, speed, TTL, spread, dmgDropStart, dmgDropEnd, dmgMin, penetration,
				blockdamage, firePos);
	}
	
	public SonicShotgunProjectile(World par2World, EntityLivingBase p, float damage, float speed, int TTL, float spread,
			float dmgDropStart, float dmgDropEnd, float dmgMin, float penetration, boolean blockdamage,
			EnumBulletFirePos firePos, ItemStack stack) {
		super(par2World, p, damage, speed, TTL, spread, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, firePos,stack);
	}

	public SonicShotgunProjectile(World worldIn) {
		super(worldIn);
	}

	@Override
	protected TGDamageSource getProjectileDamageSource() {
		TGDamageSource src = new TGDamageSource("tg_sonic",this, shooter, DamageType.PHYSICAL, DeathType.GORE);
		src.ignoreHurtresistTime=true;
    	src.armorPenetration = this.penetration;
    	src.setKnockback(2.0f);
    	src.goreChance=1.0f;
		afterGetProjectileDamageSource(src);
    	return src;
	}
	
	@Override
	public boolean shouldRenderInPass(int pass) {
		if ( pass==1) {
			return mainProjectile;
		}else {
			return !mainProjectile;
		}
	}
	
	@Override
	protected void doImpactEffects(Material mat, RayTraceResult rayTraceResult, SoundType sound) {
		if (mainProjectile) {
			if (!this.world.isRemote){
    			TGPackets.network.sendToAllAround(new PacketSpawnParticle("SonicShotgunImpact", this.posX,this.posY,this.posZ), TGPackets.targetPointAroundEnt(this, 50.0f));
    		}
		}
	}

	@Override
	protected void onHit(RayTraceResult mop) {
		if (mop.entityHit != null )
        {
			if (entitiesHit != null && !entitiesHit.contains(mop.entityHit)) {
	        	TGDamageSource knockback = TGDamageSource.getKnockbackDummyDmgSrc(this, this.shooter);
				knockback.setSourceItemStack(this.sourceItemStack);
				EntityLivingBase ent = (EntityLivingBase) mop.entityHit;
				float dmgFactor = DamageSystem.getDamageFactor(this.shooter, ent);
				float dmg =  this.getDamage()*dmgFactor;
				TGDamageSource src = getProjectileDamageSource();
				src.setPenetration(this.penetration);
				src.setSourceItemStack(this.sourceItemStack);
				src.setDamageUuid(this.damageUuid);
				src.attacker = this.shooter;
				src.setOriginalDamage(dmg);
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
				dmg  = src.getOriginalDamage();
	        	
	        	if (mop.entityHit instanceof EntityLivingBase){
	        		if (dmg>0){
		        		ent.attackEntityFrom(knockback, 0.01f);
		        		ent.attackEntityFrom(src, dmg);
		        		if (src.wasSuccessful()) {
							
							if (ent instanceof EntityLiving) {
								this.setAIRevengeTarget(((EntityLiving) ent));
							}
		
							this.onHitEffect(ent,mop);
						}
	        		}
	        	} else {
	        		mop.entityHit.attackEntityFrom(src,this.getDamage());            	
	        	}
	        	
	        	entitiesHit.add(mop.entityHit);
            }
        } else if (mop.typeOfHit == RayTraceResult.Type.BLOCK) {
			boolean b = this.hitBlock(mop);
			if (Boolean.TRUE.equals(b)){
				this.setDead();
			}
    	}
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeBoolean(mainProjectile);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		this.mainProjectile = additionalData.readBoolean();
	}
	
	public static class Factory implements IProjectileFactory<SonicShotgunProjectile> {

		@Override
		public SonicShotgunProjectile createProjectile(GenericGun gun, World world, EntityLivingBase p, float damage, float speed, int TTL, float spread, float dmgDropStart, float dmgDropEnd,
				float dmgMin, float penetration, boolean blockdamage, EnumBulletFirePos firePos, float radius, double gravity,int gunLevel, ItemStack stack, BulletGenericDataBean bulletGenericDataBean) {
			return new SonicShotgunProjectile(world,p,damage,speed,TTL,spread,dmgDropStart,dmgDropEnd,dmgMin,penetration,blockdamage,firePos,stack);
		}

		@Override
		public DamageType getDamageType() {
			return DamageType.PHYSICAL;
		}
		
	}
}
