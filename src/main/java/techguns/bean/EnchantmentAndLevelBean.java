package techguns.bean;

import net.minecraft.enchantment.Enchantment;

/**
 * @author srealx
 * @date 2025/3/8
 */
public class EnchantmentAndLevelBean<T extends Enchantment>{
    private T enchantment;
    private Integer level;

    public EnchantmentAndLevelBean(T enchantment,Integer level){
        this.enchantment = enchantment;
        this.level = level;
    }

    public T getEnchantment() {
        return enchantment;
    }

    public void setEnchantment(T enchantment) {
        this.enchantment = enchantment;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
