package techguns.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import techguns.bean.BulletGenericDataBean;
import techguns.bean.ShootGunSpawnProjectileEventBean;
import techguns.capabilities.TGExtendedPlayer;
import techguns.damagesystem.TGDamageSource;
import techguns.items.guns.GenericGun;

/**
 * common guns enchantments
 * @author srealx
 * @date 2025/3/10
 */
public class GenericGunEnchantment extends Enchantment implements IEnchantmentSort{

    protected GenericGunEnchantment(Rarity p_i46731_1_, EnumEnchantmentType p_i46731_2_, EntityEquipmentSlot[] p_i46731_3_) {
        super(p_i46731_1_, p_i46731_2_, p_i46731_3_);
    }

    @Override
    protected boolean canApplyTogether(Enchantment enchantment) {
        if (enchantment == this
                || enchantment.getClass().isAssignableFrom(this.getClass())
                || this.getClass().isAssignableFrom(enchantment.getClass())){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public boolean canApplyAnvil(ItemStack stack){
        return stack.getItem() instanceof GenericGun;
    }

    public Integer beforeReloadAmmo(ItemStack itemStack,Integer reloadTime){
        return reloadTime;
    }

    public void afterGunKill(TGDamageSource damageSource){

    }

    public void afterGetProjectileDamageSource(TGDamageSource damageSource){

    }

    public void afterDamageSourceInit(TGDamageSource damageSource){

    }

    public void beforeShotGunSpawnProjectile(ItemStack itemStack,ShootGunSpawnProjectileEventBean shootGunSpawnProjectileEventBean){

    }

    public void beforeProjectilesCreate(BulletGenericDataBean bulletGenericDataBean,ItemStack itemStack){

    }

    public void afterAmmoUsed(GenericGun genericGun, ItemStack stack, int ammo, EnumHand hand, EntityPlayer player){

    }

    public void afterAmmoUsed(GenericGun genericGun, ItemStack stack, int ammo){

    }

    public void afterShootGunPredicate(World world,TGExtendedPlayer extendedPlayer, ItemStack itemStack, EntityPlayer player){

    }

    public SoundEvent beforeShootGunPlaySound(GenericGun genericGun,SoundEvent playSound){
        return playSound;
    }

    public float beforeProjectileCreate(ItemStack stack,float speed){
        return speed;
    }

    public int beforeDurabilityLoss(ItemStack stack, int consumingDurabilityEveryShoot){
        return consumingDurabilityEveryShoot;
    }

    @Override
    public int getSort() {
        return 1;
    }
}
