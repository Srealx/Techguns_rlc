package techguns.items.guns;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import techguns.api.damagesystem.DamageType;
import techguns.bean.BulletGenericDataBean;
import techguns.entities.projectiles.EnumBulletFirePos;
import techguns.entities.projectiles.GenericProjectile;
import techguns.items.guns.ammo.DamageModifier;
import techguns.items.guns.ammo.PenetrationModifier;

public interface IProjectileFactory<T extends GenericProjectile> {

	 public T createProjectile(GenericGun gun, World world, EntityLivingBase p, float damage, float speed, int TTL,
							   float spread, float dmgDropStart, float dmgDropEnd, float dmgMin, float penetration, boolean blockdamage, EnumBulletFirePos firePos, float radius, double gravity, int gunLevel, ItemStack sourceItemStack, BulletGenericDataBean bulletGenericDataBean);

	/**
	 * Used for Tooltip display
	 * @return
	 */
	 public DamageType getDamageType();

	 
	 public default DamageModifier getDamageModifier() {
		 return DamageModifier.DEFAULT_MODIFIER;
	 }

	 public  default PenetrationModifier getPenetrationModifier(){
		 return PenetrationModifier.DEFAULT_MODIFIER;
	 }
}
