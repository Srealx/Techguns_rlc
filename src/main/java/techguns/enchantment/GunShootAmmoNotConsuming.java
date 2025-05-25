package techguns.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import techguns.TGPackets;
import techguns.Techguns;
import techguns.items.guns.GenericGun;
import techguns.packets.PacketShootAmmoNotConsumingTarget;

import java.util.Random;

/**
 * ShotGun Bullet Increase
 * @author srealx
 * @date 2025/3/8
 */
public class GunShootAmmoNotConsuming extends GenericGunEnchantment {

    public static final float RATE_EVERY_LEVEL = 13.5f;

    public GunShootAmmoNotConsuming() {
        super(Rarity.UNCOMMON,
                EnumEnchantmentType.WEAPON,
                new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setRegistryName(new ResourceLocation(Techguns.MODID,"gun_shoot_ammo_not_consuming"))
                .setName("gun_shoot_ammo_not_consuming");
    }

     
    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof GenericGun;
    }

    @Override
    public boolean canApplyAnvil(ItemStack stack){
        return stack.getItem() instanceof GenericGun;
    }

      
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return canApply(stack);
    }

     
    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinEnchantability(int level) {
        return  35 + level * 5;
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
    public void afterAmmoUsed(GenericGun genericGun, ItemStack stack, int ammo, EnumHand hand, EntityPlayer player){
        if (!(stack.getItem() instanceof GenericGun)){
            return;
        }
        int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(this, stack);
        int random =  new Random().nextInt(100)+1;
        if (random > Math.round(RATE_EVERY_LEVEL * enchantmentLevel)){
            return;
        }
        int nowAmmo = genericGun.getCurrentAmmo(stack);
        NBTTagCompound tags = stack.getTagCompound();
        int targetAmmo = nowAmmo + ammo;
        tags.setShort("ammo",(short) targetAmmo);
        TGPackets.network.sendTo(new PacketShootAmmoNotConsumingTarget(targetAmmo,stack),(EntityPlayerMP) player);
    }

    @Override
    public void afterAmmoUsed(GenericGun genericGun, ItemStack stack, int ammo){
        if (!(stack.getItem() instanceof GenericGun)){
            return;
        }
        int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(this, stack);
        int random =  new Random().nextInt(100)+1;
        if (random > Math.round(RATE_EVERY_LEVEL * enchantmentLevel)){
            return;
        }
        int nowAmmo = genericGun.getCurrentAmmo(stack);
        NBTTagCompound tags = stack.getTagCompound();
        int targetAmmo = nowAmmo + ammo;
        tags.setShort("ammo",(short) targetAmmo);
    }

}
