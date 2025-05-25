package techguns.entities.npcs;

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
import techguns.items.guns.GenericGun;

import java.util.Random;

public class CyberDemon extends GenericNPCUndead {

	public static final ResourceLocation LOOT = new ResourceLocation(Techguns.MODID, "entities/cyberdemon");
	
	public CyberDemon(World world) {
		super(world);
		setTGArmorStats(10.0f, 0f);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.38D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(145);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(17);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(70.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(1D);
		this.isImmuneToFire = true;
		this.hasAimedBowAnim = false;
	}

	@Override
	protected void addRandomArmor(int difficulty) {

		// No Armor
		// Weapons
		Item weapon = null;
		
		weapon = TGuns.netherblaster;
		ItemStack stack = new ItemStack(weapon);
		this.addEnchantmentForGun(stack);
		if (weapon != null) this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
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
	public boolean hasWeaponArmPose() {
		return false;
	}

	
	
	@Override
	public float getWeaponPosX() {
		return 0.16f;
	}

	@Override
	public float getWeaponPosY() {
		return 0.72f;
	}

	@Override
	public float getWeaponPosZ() {
		return 0.1f;
	}

	@Override
	public float getGunScale() {
		return 1.5f;
	}

	
	
	@Override
	public float getBulletOffsetSide() {
		return 0.3f;
	}

	@Override
	public float getBulletOffsetHeight() {
		return -0.59f;
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
	protected int getExperiencePoints(EntityPlayer player) {
		return 185;
	}

	@Override
	protected boolean shouldBurnInDay() {
		return false;
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
			default:break;
		}

		// 快速换弹附魔的概率
		int randomAcceleratedReloading = new Random().nextInt(14)+1;
		switch (randomAcceleratedReloading){
			case 3:itemStack.addEnchantment(TGEnchantments.GUNS_ACCELERATED_RELOADING,1);break;
			case 7:itemStack.addEnchantment(TGEnchantments.GUNS_ACCELERATED_RELOADING,2);break;
			default:break;
		}
		// 额外暴击率附魔的概率
		int randomCriticalHitRate = new Random().nextInt(14)+1;
		switch (randomCriticalHitRate){
			case 3:itemStack.addEnchantment(TGEnchantments.GUNS_CRITICAL_HIT_RATE,1);break;
			default:break;
		}
	}
	
}