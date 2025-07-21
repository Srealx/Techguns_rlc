package techguns.entities.npcs;

import java.util.Random;

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
import techguns.items.armors.GenericArmorMultiCamo;
import techguns.items.guns.GenericGun;

public class StormTrooper extends GenericNPCGearSpecificStats {

	public static final ResourceLocation LOOT = new ResourceLocation(Techguns.MODID, "entities/stormtrooper");
	
	public StormTrooper(World world) {
		super(world);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(175);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(22);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(85.0D);
	}

	@Override
	protected void addRandomArmor(int difficulty) {

		// Armors
		
		int camo = 1;
		
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, GenericArmorMultiCamo.getNewWithCamo(
			TGArmors.t3_miner_Helmet,camo));
		this.setItemStackToSlot(EntityEquipmentSlot.CHEST, GenericArmorMultiCamo.getNewWithCamo(
				TGArmors.t3_miner_Chestplate,camo));
		this.setItemStackToSlot(EntityEquipmentSlot.LEGS, GenericArmorMultiCamo.getNewWithCamo(
				TGArmors.t3_miner_Leggings,camo));
		this.setItemStackToSlot(EntityEquipmentSlot.FEET, GenericArmorMultiCamo.getNewWithCamo(
				TGArmors.t3_miner_Boots,camo));
		// Weapons
		
		Random r = new Random();
		Item weapon = null;
		switch (r.nextInt(4)) {
		case 0:
			weapon = TGuns.blasterrifle;
			break;
		case 1:
			weapon = TGuns.guidedmissilelauncher;
			break;
		case 2:
			weapon = TGuns.scatterbeamrifle;
			break;
		default:
			weapon = TGuns.lasergun;
			break;
		}
		ItemStack stack = new ItemStack(weapon);
		this.addEnchantmentForGun(stack);
		if (weapon != null) this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
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
		return SoundEvents.ENTITY_ZOMBIE_STEP;
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
		return 750;
	}
}