package techguns.entities.projectiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import techguns.api.damagesystem.DamageType;
import techguns.bean.BulletGenericDataBean;
import techguns.items.guns.GenericGun;
import techguns.items.guns.IProjectileFactory;
import techguns.items.guns.ammo.DamageModifier;
import techguns.items.guns.ammo.PenetrationModifier;

public class GenericProjectileStrong extends GenericProjectile {


	public GenericProjectileStrong(World worldIn) {
		super(worldIn);
	}

	public GenericProjectileStrong(World worldIn, double posX, double posY, double posZ, float yaw, float pitch,
									  float damage, float speed, int TTL, float spread, float dmgDropStart, float dmgDropEnd, float dmgMin,
									  float penetration, boolean blockdamage, EnumBulletFirePos firePos,int gunLevel) {
		super(worldIn, posX, posY, posZ, yaw, pitch, damage, speed, TTL, spread, dmgDropStart, dmgDropEnd, dmgMin, penetration,
				blockdamage, firePos);

	}

	public GenericProjectileStrong(World par2World, EntityLivingBase p, float damage, float speed, int TTL,
									  float spread, float dmgDropStart, float dmgDropEnd, float dmgMin, float penetration, boolean blockdamage,
									  EnumBulletFirePos firePos,int gunLevel,ItemStack stack) {
		super(par2World, p, damage, speed, TTL, spread, dmgDropStart, dmgDropEnd, dmgMin, penetration, blockdamage, firePos,stack);
	}

	public static class Factory implements IProjectileFactory<GenericProjectile> {

		public Factory() {

		}

		protected DamageModifier mod = new DamageModifier().setDmg(1.6f, 0f);

		protected PenetrationModifier pen = new PenetrationModifier().setPen(1f,0.05f);

		@Override
		public DamageModifier getDamageModifier() {
			return mod;
		}

		@Override
		public PenetrationModifier getPenetrationModifier(){
			return pen;
		}

		@Override
		public GenericProjectileStrong createProjectile(GenericGun gun, World world, EntityLivingBase p,
														float damage, float speed, int TTL, float spread, float dmgDropStart, float dmgDropEnd, float dmgMin,
														float penetration, boolean blockdamage, EnumBulletFirePos firePos, float radius, double gravity, int gunLevel, ItemStack stack, BulletGenericDataBean bulletGenericDataBean) {
			return new GenericProjectileStrong(world, p, mod.getDamage(damage), speed, TTL, spread, dmgDropStart, dmgDropEnd, mod.getDamage(dmgMin), pen.getPen(penetration), blockdamage, firePos,gunLevel,stack);
		}

		@Override
		public DamageType getDamageType() {
			return DamageType.STRONG;
		}
		
	}
	
}
