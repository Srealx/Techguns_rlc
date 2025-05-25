package techguns.entities.npcs;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import techguns.TGEnchantments;
import techguns.TGuns;
import techguns.Techguns;
import techguns.damagesystem.TGDamageSource;
import techguns.items.guns.GenericGun;

public class SuperMutantBasic extends GenericNPC {
	
	public static final ResourceLocation LOOT = new ResourceLocation(Techguns.MODID, "entities/supermutantbasic");

	public SuperMutantBasic(World world) {
		super(world);
		this.setSize(getMutantWidth(), 2F*this.getModelScale());
		setTGArmorStats(5.0f, 0f);
	}
	
	public int gettype() {
		return 0;
	};
	
	protected float getMutantWidth() {
		return 1.0f;
	}
	
	public double getModelHeightOffset(){
		return 0.55d;
	}
	
	public float getModelScale() {
		return 1.35f;
	}

	@Override
	public float getWeaponPosY() {
		return 0f;
	}
	
	@Override
	public float getWeaponPosX() {
		return 0.13f;
	}

	@Override
	public float getWeaponPosZ() {
		return -0.18f;
	}

	
	@Override
	public float getTotalArmorAgainstType(TGDamageSource dmgsrc) {
		switch(dmgsrc.damageType){
			case EXPLOSION:
			case LIGHTNING:
			case ENERGY:
			case FIRE:
			case ICE:
				return 10.0f;
			case PHYSICAL:
			case PROJECTILE:
				return 7.0f;
			case POISON:
			case RADIATION:
				return 15.0f;
			case UNRESISTABLE:
		default:
			return 0.0f;
		}
	}

	@Override
	public int getTotalArmorValue() {
		return 22;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(25);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(75.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(1D);
	}

	

	@Override
	protected void addRandomArmor(int difficulty) {

			// Weapons
		Random r = new Random();
		Item weapon = null;
		switch (r.nextInt(4)) {
			case 0:
				weapon = TGuns.flamethrower;
				break;
			case 1:
			case 2:
				weapon = TGuns.minigun;
				break;
			default:
				weapon = TGuns.lasergun;
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
		return 1.7f;
	}

	@Override
	public SoundEvent getAmbientSound() {
		return techguns.TGSounds.CYBERDEMON_IDLE;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return techguns.TGSounds.CYBERDEMON_HURT;
	}

	@Override
	public SoundEvent getDeathSound() {
		return techguns.TGSounds.CYBERDEMON_DEATH;
	}

	public SoundEvent getStepSound() {
		return techguns.TGSounds.CYBERDEMON_STEP;
	}
	
	@Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }
	
	
	@Override
	protected ResourceLocation getLootTable() {
		return LOOT;
	}




	@Override
	public void addEnchantmentForGun(ItemStack itemStack){
		if (itemStack.isEmpty()  || ! (itemStack.getItem() instanceof GenericGun)){
			return;
		}
		// 火力附魔的概率
		int randomFirePower = new Random().nextInt(50)+1;
		switch (randomFirePower){
			case 8:
			case 12: itemStack.addEnchantment(TGEnchantments.COMMON_GUNS_FIRE_POWER_ENCHANTMENT,2);break;
			case 16:
			case 20: itemStack.addEnchantment(TGEnchantments.COMMON_GUNS_FIRE_POWER_ENCHANTMENT,3);break;
			case 23:
			case 28: itemStack.addEnchantment(TGEnchantments.COMMON_GUNS_FIRE_POWER_ENCHANTMENT,4);break;
			case 40:
			case 42:itemStack.addEnchantment(TGEnchantments.ADVANCED_GUNS_FIRE_POWER_ENCHANTMENT,1);break;
			case 50:
			case 46:itemStack.addEnchantment(TGEnchantments.ADVANCED_GUNS_FIRE_POWER_ENCHANTMENT,2);break;
			case 3:
			case 25:itemStack.addEnchantment(TGEnchantments.ADVANCED_GUNS_FIRE_POWER_ENCHANTMENT,3);break;
			default:break;
		}

		// 快速换弹附魔的概率
		int randomAcceleratedReloading = new Random().nextInt(12)+1;
		switch (randomAcceleratedReloading){
			case 3:itemStack.addEnchantment(TGEnchantments.GUNS_ACCELERATED_RELOADING,2);break;
			case 7:itemStack.addEnchantment(TGEnchantments.GUNS_ACCELERATED_RELOADING,3);break;
			case 11:itemStack.addEnchantment(TGEnchantments.GUNS_ACCELERATED_RELOADING,4);break;
			default:break;
		}

		// 穿甲弹的概率
		int randomPen = new Random().nextInt(14)+1;
		switch (randomAcceleratedReloading){
			case 3:itemStack.addEnchantment(TGEnchantments.EXTRA_GUNS_PENETRATE,1);break;
			case 11:itemStack.addEnchantment(TGEnchantments.EXTRA_GUNS_PENETRATE,2);break;
			default:break;
		}

		// 额外暴击率附魔的概率
		int randomCriticalHitRate = new Random().nextInt(12)+1;
		switch (randomCriticalHitRate){
			case 3:itemStack.addEnchantment(TGEnchantments.GUNS_CRITICAL_HIT_RATE,1);break;
			case 12:itemStack.addEnchantment(TGEnchantments.GUNS_CRITICAL_HIT_RATE,2);break;
			default:break;
		}
	}

	@Override
	protected int getExperiencePoints(EntityPlayer player) {
		return 800;
	}
}
