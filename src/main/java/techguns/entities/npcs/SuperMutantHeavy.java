package techguns.entities.npcs;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import techguns.TGuns;
import techguns.damagesystem.TGDamageSource;

import java.util.Random;

public class SuperMutantHeavy extends SuperMutantBasic {

	public SuperMutantHeavy(World world) {
		super(world);
		setTGArmorStats(10.0f, 0);
	}
	
	public int gettype() {
		return 1;
	};

	public double getModelHeightOffset(){
		return 0.75d;
	}
	
	public float getModelScale() {
		return 1.5f;
	}
	
	@Override
	protected float getMutantWidth() {
		return 1.2f;
	}
	
	@Override
	public float getWeaponPosX() {
		return 0.17f;
	}

	@Override
	public float getWeaponPosZ() {
		return -0.3f;
	}



	@Override
	protected void addRandomArmor(int difficulty) {

		// Weapons
		Random r = new Random();
		Item weapon = null;
		switch (r.nextInt(3)) {
			case 0:
				weapon = TGuns.rocketlauncher;
				break;
			case 1:
				weapon = TGuns.minigun;
				break;
			default:
				weapon = TGuns.guidedmissilelauncher;
				break;
			/*default:
				weapon = Items.IRON_SHOVEL;
				break;*/
		}
		// 添加随机附魔
		ItemStack weaponStack = new ItemStack(weapon);
		this.addEnchantmentForGun(weaponStack);
		if (weapon != null) this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, weaponStack);
	}


	protected float getShootGunDamageRate(){
		return 2.2f;
	}

	
	@Override
	public float getTotalArmorAgainstType(TGDamageSource dmgsrc) {
		switch(dmgsrc.damageType){
		case EXPLOSION:
		case LIGHTNING:
		case ENERGY:
		case FIRE:
		case ICE:
			return 16.0f;
		case PHYSICAL:
		case PROJECTILE:
			return 12.0f;
		case POISON:
		case RADIATION:
			return 18.0f;
		case UNRESISTABLE:
	default:
		return 0.0f;
		}
	}

	@Override
	public int getTotalArmorValue() {
		return 35;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.27D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(375);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(36);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(16);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(85.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(3D);
	}

	@Override
	protected int getExperiencePoints(EntityPlayer player) {
		return 1200;
	}
}
