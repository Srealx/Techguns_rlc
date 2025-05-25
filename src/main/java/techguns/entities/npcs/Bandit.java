package techguns.entities.npcs;

import java.util.Random;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import techguns.TGArmors;
import techguns.TGEnchantments;
import techguns.TGuns;
import techguns.Techguns;
import techguns.items.guns.GenericGun;

public class Bandit extends GenericNPC {

	public static final ResourceLocation LOOT = new ResourceLocation(Techguns.MODID, "entities/Bandit");
	
	public Bandit(World world) {
		super(world);
		setTGArmorStats(5.0f, 0f);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(48);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(9);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(65.0D);
	}

	@Override
	protected void addRandomArmor(int difficulty) {

		// Armors
		
		this.setItemStackToSlot(EntityEquipmentSlot.CHEST,new ItemStack(TGArmors.t1_scout_Chestplate));
		this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(TGArmors.t1_scout_Leggings));
		this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(TGArmors.t1_scout_Boots));
		
		double chance = 0.5;
		if (Math.random() <= chance) {
			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(TGArmors.t1_scout_Helmet));
		}			 

		// Weapons
		Random r = new Random();
		Item weapon = null;
		switch (r.nextInt(6)) {
		case 0:
			weapon = TGuns.pistol;
			break;
		case 1:
			weapon = TGuns.ak47;
			break;
		case 2:
			weapon = TGuns.sawedoff;
			break;
		case 3:
			weapon = TGuns.thompson;
			break;
		case 4:
			weapon = TGuns.revolver;
			break;
			case 5:
		default:
			weapon = TGuns.boltaction;
			break;
		}
		// 添加随机附魔
		ItemStack weaponStack = new ItemStack(weapon);
		this.addEnchantmentForGun(weaponStack);
		if (weapon != null) this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, weaponStack);
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
			case 1:
			case 11:itemStack.addEnchantment(TGEnchantments.LOWER_GUNS_FIRE_POWER_ENCHANTMENT,1);break;
			case 19:
			case 26:itemStack.addEnchantment(TGEnchantments.LOWER_GUNS_FIRE_POWER_ENCHANTMENT,2);break;
			case 6:
			case 32:itemStack.addEnchantment(TGEnchantments.LOWER_GUNS_FIRE_POWER_ENCHANTMENT,3);break;
			case 22: itemStack.addEnchantment(TGEnchantments.COMMON_GUNS_FIRE_POWER_ENCHANTMENT,1);break;
			case 36: itemStack.addEnchantment(TGEnchantments.COMMON_GUNS_FIRE_POWER_ENCHANTMENT,2);break;
			default:break;
		}
		// 快速换弹附魔的概率
		int randomAcceleratedReloading = new Random().nextInt(14)+1;
		switch (randomAcceleratedReloading){
			case 5:itemStack.addEnchantment(TGEnchantments.GUNS_ACCELERATED_RELOADING,1);break;
			case 11:itemStack.addEnchantment(TGEnchantments.GUNS_ACCELERATED_RELOADING,2);break;
			default:break;
		}
		// 额外暴击率附魔的概率
		int randomCriticalHitRate = new Random().nextInt(20)+1;
		switch (randomCriticalHitRate){
			case 7:itemStack.addEnchantment(TGEnchantments.GUNS_CRITICAL_HIT_RATE,1);break;
			default:break;
		}
	}

	@Override
	protected int getExperiencePoints(EntityPlayer player) {
		return 45;
	}
}