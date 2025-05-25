package techguns.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import techguns.Techguns;
import techguns.cache.EnchantmentDataCache;
import techguns.capabilities.TGExtendedPlayer;
import techguns.damagesystem.TGDamageSource;
import techguns.items.guns.GenericGun;

import java.util.Random;

/**
 * Guns Accelerated Reloading
 * @author srealx
 * @date 2025/3/8
 */
public class NoThinkShoot extends GenericGunEnchantment {
    public static final Float BASE_CRITICAL_STRIKE_DAMAGE_RATE = 1f;
    /**
     * Unit: seconds
     */
    public static final Long COOLING_TIME = 15L;

    public NoThinkShoot() {
        super(Rarity.VERY_RARE,
                EnumEnchantmentType.WEAPON,
                new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND,EntityEquipmentSlot.OFFHAND});
        this.setRegistryName(new ResourceLocation(Techguns.MODID,"no_think_shoot"))
                .setName("no_think_shoot");
    }

     
    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof GenericGun && ((GenericGun) stack.getItem()).snipeGun;
    }

    @Override
    public boolean canApplyAnvil(ItemStack stack){
        return stack.getItem() instanceof GenericGun && ((GenericGun) stack.getItem()).snipeGun;
    }

    @Override
    protected boolean canApplyTogether(Enchantment enchantment) {
        return super.canApplyTogether(enchantment)
                && Boolean.FALSE.equals(enchantment instanceof GunsCriticalHitDamageIncreaseRate)
                && Boolean.FALSE.equals(enchantment instanceof GunShootAmmoNotConsuming)
                && Boolean.FALSE.equals(enchantment instanceof ExtraCatapultBulletIncrease)
                && Boolean.FALSE.equals(enchantment instanceof ShootGunBulletIncrease)
                && Boolean.FALSE.equals(enchantment instanceof MagicBulletEnchantment)
                ;
    }

      
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return canApply(stack);
    }

     
    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMinEnchantability(int level) {
        return  60;
    }

    @Override
    public int getMaxEnchantability(int level) {
        return this.getMinEnchantability(level) + 100;
    }

    @Override
    public boolean isTreasureEnchantment() {
        return Boolean.TRUE;
    }

    @Override
    public void afterDamageSourceInit(TGDamageSource damageSource){
        ItemStack sourceItemStack = damageSource.getSourceItemStack();
        if(Boolean.FALSE.equals(((GenericGun)sourceItemStack.getItem()).snipeGun)){
            return;
        }
        float baseChRate = damageSource.getExtraCriticalHitRate();
        if (baseChRate < 0){
            baseChRate = 0;
        }
        int random =  450 + new Random().nextInt(400);
        damageSource.addExtraCriticalHitDamageRate(BASE_CRITICAL_STRIKE_DAMAGE_RATE +  (random/100f) * baseChRate);
        damageSource.setCriticalHitFlat(Boolean.TRUE);
    }

    @Override
    public void afterShootGunPredicate(World world, TGExtendedPlayer extendedPlayer, ItemStack itemStack, EntityPlayer player){
        if(Boolean.FALSE.equals(((GenericGun)itemStack.getItem()).snipeGun)){
            return;
        }
        long timestamp = world.getWorldTime();
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        tagCompound.setLong("gunFireCooling",timestamp + COOLING_TIME * 20);
        // 缓存后续的fx信息
        GenericGun gun =  (GenericGun)itemStack.getItem();
        if (gun.snipeGun && gun.criticalShootFx!=null){
            EnchantmentDataCache.ENCHANTMENT_FX_CACHE.put(gun.criticalShootFx,gun.criticalShootFxName);
        }
    }

    @Override
    public SoundEvent beforeShootGunPlaySound(GenericGun genericGun,SoundEvent playSound){
        if(Boolean.FALSE.equals(genericGun.snipeGun)){
            return genericGun.getFiresound();
        }
        return genericGun.getSnipeGunCriticalSound();
    }

    @Override
    public int getSort() {
        // 由于该附魔需要获取额外的暴击率，所以必须放在最后执行
        return 99;
    }

}
