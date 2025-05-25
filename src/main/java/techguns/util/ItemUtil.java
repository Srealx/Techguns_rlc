package techguns.util;

import java.util.*;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import techguns.bean.EnchantmentAndLevelBean;
import techguns.bean.QualityDataBean;
import techguns.constant.QualityConstant;
import techguns.items.guns.GenericGun;

/**
 * utility class with static item related functions
 *
 */
public class ItemUtil {
	
	public static void addShiftExpandedTooltip(List<String> tooltip){
		tooltip.add(TextUtil.trans("techguns.gun.tooltip.shift1")+" "+ChatFormatting.GREEN+TextUtil.trans("techguns.gun.tooltip.shift2")+" "+ChatFormatting.GRAY+TextUtil.trans("techguns.gun.tooltip.shift3"));
	}	
	
	public static void addToolClassesTooltip(HashMap<String,Integer> toolclasses, List tooltip){
		Set<String> ss = toolclasses.keySet();
		if (ss.size()>0) {
			tooltip.add(TextUtil.trans("techguns.tooltip.toolclasses"));
			for (String s: toolclasses.keySet()){
				tooltip.add("  "+s+" : "+toolclasses.get(s));
			}
		}
	}	
	
	public static boolean isItemEqual(ItemStack item1, ItemStack item2){
		if(item1.isEmpty() && item2.isEmpty()){
			return true;
		} else if (item1.isEmpty() || item2.isEmpty()){
			return false;
		} else {
			return OreDictionary.itemMatches(item1, item2, true);
		}
	}
	
	/**
	 * like isItemEqual for FluidStacks
	 * @param item1
	 * @param item2
	 * @return true if both stacks are null or both are !=null AND Fluids are equal
	 */
	public static boolean isFluidEqual(FluidStack f1, FluidStack f2){
		if(f1==null && f2==null){
			return true;
		} else if (f1==null || f2==null){
			return false;
		} else {
		//return ( (item1==null && item2==null) || ( (item1!=null && item2!=null) && (item1.getItem() == item2.getItem()) && (item1.getItemDamage()==item2.getItemDamage()) ) );
			return f1.equals(f2);
		}
	}
	
	/**
	 * Returns if an item exists in OreDictionary with key
	 * @param key
	 * @return
	 */
	public static boolean existsInOredict(String key){
		return OreDictionary.getOres(key).size()>0;
	}

	/**
	 * find enchantment in item stack
	 * @param <T>
	 * @param stack
	 * @param tClass
	 * @return
	 */
	public static <T extends Enchantment> EnchantmentAndLevelBean<T> findEnchantment(ItemStack stack, Class<T> tClass){
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
		for (Map.Entry<Enchantment, Integer> enchantmentIntegerEntry : enchantments.entrySet()) {
			Enchantment key = enchantmentIntegerEntry.getKey();
			if (tClass.isAssignableFrom(key.getClass())){
				return new EnchantmentAndLevelBean<>((T)key,enchantmentIntegerEntry.getValue());
			}
		}
		return null;
	}

	public static QualityDataBean findItemStackQualityData(ItemStack itemStack){
		NBTTagCompound tagCompound = itemStack.getTagCompound();
		if (tagCompound==null){
			return null;
		}
		if (tagCompound.hasKey(QualityConstant.QUALITY_TAG_NAME, Constants.NBT.TAG_COMPOUND)){
			NBTTagCompound qualityCompoundTag = tagCompound.getCompoundTag(QualityConstant.QUALITY_TAG_NAME);
			if (qualityCompoundTag.hasKey(QualityConstant.QUALITY_ATTRIBUTES_TAG_NAME,Constants.NBT.TAG_LIST)){
				QualityDataBean qualityDataBean = new QualityDataBean();
				NBTTagList modifiersList = qualityCompoundTag.getTagList(QualityConstant.QUALITY_ATTRIBUTES_TAG_NAME,Constants.NBT.TAG_COMPOUND);
				for (int i = 0; i < modifiersList.tagCount(); i++) {
					NBTTagCompound modifierTag = modifiersList.getCompoundTagAt(i);
					qualityDataBean.addAttribute(modifierTag.getString("AttributeName"),modifierTag.getFloat("Amount"));
				}
				return qualityDataBean;
			}
		}
		return null;
	}
}
