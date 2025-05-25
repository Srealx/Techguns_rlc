package techguns;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import techguns.enchantment.*;

/**
 * tg enchantments
 * @author srealx
 * @date 2025/3/7
 */
public class TGEnchantments {
    public static final Enchantment LOWER_GUNS_FIRE_POWER_ENCHANTMENT = new LowerGunsFirePowerEnchantment();
    public static final Enchantment COMMON_GUNS_FIRE_POWER_ENCHANTMENT = new CommonGunsFirePowerEnchantment();
    public static final Enchantment ADVANCED_GUNS_FIRE_POWER_ENCHANTMENT = new AdvancedGunsFirePowerEnchantment();
    public static final Enchantment SUPER_GUNS_FIRE_POWER_ENCHANTMENT = new SuperGunsFirePowerEnchantment();
    public static final Enchantment GUNS_CRITICAL_HIT_RATE = new GunsCriticalHitRate();
    public static final Enchantment ADVANCED_GUNS_CRITICAL_HIT_RATE = new AdvancedGunsCriticalHitRate();
    public static final Enchantment GUNS_CRITICAL_HIT_DAMAGE_INCREASE_RATE = new GunsCriticalHitDamageIncreaseRate();
    public static final Enchantment ADVANCED_GUNS_CRITICAL_HIT_DAMAGE_INCREASE_RATE = new AdvancedGunsCriticalHitDamageIncreaseRate();
    public static final Enchantment GUNS_ACCELERATED_RELOADING = new GunsAcceleratedReloading();
    public static final Enchantment SHOOT_GUN_BULLET_INCREASE = new ShootGunBulletIncrease();
    public static final Enchantment NO_THINK_SHOOT = new NoThinkShoot();
    public static final Enchantment EXTRA_GUNS_PENETRATE = new ExtraGunsPenetrateEnchantment();
    public static final Enchantment EXTRA_CATAPULT_BULLET_INCREASE = new ExtraCatapultBulletIncrease();
    public static final Enchantment GUN_SHOOT_AMMO_NOT_CONSUMING = new GunShootAmmoNotConsuming();
    public static final Enchantment ULTRA_GUNS_FIRE_POWER_ENCHANTMENT = new UltraGunsFirePowerEnchantment();
    public static final Enchantment LET_BULLET_FLY_ENCHANTMENT = new LetBulletFlyEnchantment();
    public static final Enchantment ASH_BULLET_ENCHANTMENT = new AshBulletEnchantment();
    public static final Enchantment MAGIC_BULLET_ENCHANTMENT = new MagicBulletEnchantment();
    public static final Enchantment CURSE_GUNS_FIRE_POWER_ENCHANTMENT = new CurseGunsFirePowerEnchantment();
    public static final Enchantment ADVANCED_EXTRA_GUNS_PENETRATE = new AdvancedExtraGunsPenetrateEnchantment();
    public static final Enchantment GUNS_CRITICAL_HIT_POTION_DELAYED = new GunsCriticalHitPotionDelayed();
    public static final Enchantment GUNS_CORROSION = new GunsCorrosionEnchantment();


    public void registerEnchantments(RegistryEvent.Register<Enchantment> event){
        event.getRegistry().register(LOWER_GUNS_FIRE_POWER_ENCHANTMENT);
        event.getRegistry().register(COMMON_GUNS_FIRE_POWER_ENCHANTMENT);
        event.getRegistry().register(ADVANCED_GUNS_FIRE_POWER_ENCHANTMENT);
        event.getRegistry().register(SUPER_GUNS_FIRE_POWER_ENCHANTMENT);
        event.getRegistry().register(ULTRA_GUNS_FIRE_POWER_ENCHANTMENT);
        event.getRegistry().register(GUNS_CRITICAL_HIT_RATE);
        event.getRegistry().register(GUNS_CRITICAL_HIT_DAMAGE_INCREASE_RATE);
        event.getRegistry().register(GUNS_ACCELERATED_RELOADING);
        event.getRegistry().register(SHOOT_GUN_BULLET_INCREASE);
        event.getRegistry().register(NO_THINK_SHOOT);
        event.getRegistry().register(EXTRA_GUNS_PENETRATE);
        event.getRegistry().register(EXTRA_CATAPULT_BULLET_INCREASE);
        event.getRegistry().register(GUN_SHOOT_AMMO_NOT_CONSUMING);
        event.getRegistry().register(LET_BULLET_FLY_ENCHANTMENT);
        event.getRegistry().register(ASH_BULLET_ENCHANTMENT);
        event.getRegistry().register(MAGIC_BULLET_ENCHANTMENT);
        event.getRegistry().register(CURSE_GUNS_FIRE_POWER_ENCHANTMENT);
        event.getRegistry().register(ADVANCED_GUNS_CRITICAL_HIT_RATE);
        event.getRegistry().register(ADVANCED_GUNS_CRITICAL_HIT_DAMAGE_INCREASE_RATE);
        event.getRegistry().register(ADVANCED_EXTRA_GUNS_PENETRATE);
        event.getRegistry().register(GUNS_CRITICAL_HIT_POTION_DELAYED);
        event.getRegistry().register(GUNS_CORROSION);
    }

}
