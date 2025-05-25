package techguns.entities.npcs;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import techguns.TGArmors;
import techguns.TGEnchantments;
import techguns.TGuns;
import techguns.Techguns;
import techguns.items.guns.GenericGun;

import java.util.Random;

public class Commando extends GenericNPC {

	public static final ResourceLocation LOOT = new ResourceLocation(Techguns.MODID, "entities/commando");
	
	public Commando(World world) {
		super(world);
		setTGArmorStats(8.0f, 0f);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(105);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(13);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(80.0D);
	}

	@Override
	protected void addRandomArmor(int difficulty) {

		// Armors
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(TGArmors.t2_commando_Helmet));
		this.setItemStackToSlot(EntityEquipmentSlot.CHEST,new ItemStack(TGArmors.t2_commando_Chestplate));
		this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(TGArmors.t2_commando_Leggings));
	    this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(TGArmors.t2_commando_Boots));

		Random r = new Random();
		Item weapon;
		switch (r.nextInt(4)) {
			case 0:
			case 1:
			case 2: weapon = TGuns.m4_infiltrator;break;
			case 3:
			default:weapon = TGuns.combatshotgun;break;

		}
		// 添加随机附魔
		ItemStack weaponStack = new ItemStack(weapon);
		this.addEnchantmentForGun(weaponStack);
		if (weapon != null) this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, weaponStack);
	}
	
	@Override
	public SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_VILLAGER_AMBIENT;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_VILLAGER_HURT;
	}

	@Override
	public SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_VILLAGER_DEATH;
	}

	public SoundEvent getStepSound() {
		return SoundEvents.ENTITY_ZOMBIE_VILLAGER_STEP;
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
		return 120;
	}


	@Override
	protected float getShootGunDamageRate(){
		return 1.7f;
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
			case 11:itemStack.addEnchantment(TGEnchantments.GUNS_ACCELERATED_RELOADING,3);break;
			default:break;
		}
		// 额外暴击率附魔的概率
		int randomCriticalHitRate = new Random().nextInt(16)+1;
		switch (randomCriticalHitRate){
			case 3:itemStack.addEnchantment(TGEnchantments.GUNS_CRITICAL_HIT_RATE,1);break;
			case 12:itemStack.addEnchantment(TGEnchantments.GUNS_CRITICAL_HIT_RATE,2);break;
			default:break;
		}
	}
}
