package techguns.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.Collections;

public class ModEnchantedBooks {
    // 创建包含自定义附魔的附魔书实例
    public static ItemStack createLowerFirepowerBook(int level) {
        ItemStack lowerGunsFirePowerEnchantmentBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new LowerGunsFirePowerEnchantment(), level),
                lowerGunsFirePowerEnchantmentBook
        );
        return lowerGunsFirePowerEnchantmentBook;
    }

    public static ItemStack createCommonFirepowerBook(int level) {
        ItemStack commonGunsFirePowerEnchantmentBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
            Collections.singletonMap(new CommonGunsFirePowerEnchantment(), level),
                commonGunsFirePowerEnchantmentBook
        );
        return commonGunsFirePowerEnchantmentBook;
    }

    public static ItemStack createAdvancedFirepowerBook(int level) {
        ItemStack advancedGunsFirePowerEnchantmentBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new AdvancedGunsFirePowerEnchantment(), level),
                advancedGunsFirePowerEnchantmentBook
        );
        return advancedGunsFirePowerEnchantmentBook;
    }

    public static ItemStack createSuperFirepowerBook(int level) {
        ItemStack superGunsFirePowerEnchantmentBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new SuperGunsFirePowerEnchantment(), level),
                superGunsFirePowerEnchantmentBook
        );
        return superGunsFirePowerEnchantmentBook;
    }

    public static ItemStack createUltraFirepowerBook(int level) {
        ItemStack ultraGunsFirePowerEnchantmentBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new UltraGunsFirePowerEnchantment(), level),
                ultraGunsFirePowerEnchantmentBook
        );
        return ultraGunsFirePowerEnchantmentBook;
    }

    public static ItemStack createCurseFirepowerBook(int level) {
        ItemStack curseGunsFirePowerEnchantmentBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new CurseGunsFirePowerEnchantment(), level),
                curseGunsFirePowerEnchantmentBook
        );
        return curseGunsFirePowerEnchantmentBook;
    }

    public static ItemStack createGunsCriticalHitRateBook(int level) {
        ItemStack gunsCriticalHitRateBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new GunsCriticalHitRate(), level),
                gunsCriticalHitRateBook
        );
        return gunsCriticalHitRateBook;
    }

    public static ItemStack createAdvancedGunsCriticalHitRateBook(int level) {
        ItemStack gunsAdvancedCriticalHitRateBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new AdvancedGunsCriticalHitRate(), level),
                gunsAdvancedCriticalHitRateBook
        );
        return gunsAdvancedCriticalHitRateBook;
    }

    public static ItemStack createGunsCriticalHitDamageIncreaseRateBook(int level) {
        ItemStack gunsCriticalHitDamageIncreaseRateBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new GunsCriticalHitDamageIncreaseRate(), level),
                gunsCriticalHitDamageIncreaseRateBook
        );
        return gunsCriticalHitDamageIncreaseRateBook;
    }

    public static ItemStack createAdvancedGunsCriticalHitDamageIncreaseRateBook(int level) {
        ItemStack gunsAdvancedCriticalHitDamageIncreaseRateBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new AdvancedGunsCriticalHitDamageIncreaseRate(), level),
                gunsAdvancedCriticalHitDamageIncreaseRateBook
        );
        return gunsAdvancedCriticalHitDamageIncreaseRateBook;
    }

    public static ItemStack createGunsAcceleratedReloading(int level) {
        ItemStack gunsAcceleratedReloading = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new GunsAcceleratedReloading(), level),
                gunsAcceleratedReloading
        );
        return gunsAcceleratedReloading;
    }

    public static ItemStack createShootGunBulletIncrease(int level) {
        ItemStack shootGunBulletIncrease = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new ShootGunBulletIncrease(), level),
                shootGunBulletIncrease
        );
        return shootGunBulletIncrease;
    }

    public static ItemStack createNoThinkShoot(int level) {
        ItemStack noThinkShoot = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new NoThinkShoot(), level),
                noThinkShoot
        );
        return noThinkShoot;
    }

    public static ItemStack createExtraGunsPenetrate(int level) {
        ItemStack extraGunsPenetrate = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new ExtraGunsPenetrateEnchantment(), level),
                extraGunsPenetrate
        );
        return extraGunsPenetrate;
    }

    public static ItemStack createAdvancedExtraGunsPenetrate(int level) {
        ItemStack advExtraGunsPenetrate = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new AdvancedExtraGunsPenetrateEnchantment(), level),
                advExtraGunsPenetrate
        );
        return advExtraGunsPenetrate;
    }

    public static ItemStack createExtraCatapultBulletIncrease(int level) {
        ItemStack extraCatapultBulletIncrease = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new ExtraCatapultBulletIncrease(), level),
                extraCatapultBulletIncrease
        );
        return extraCatapultBulletIncrease;
    }

    public static ItemStack createGunShootAmmoNotConsuming(int level) {
        ItemStack gunShootAmmoNotConsuming = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new GunShootAmmoNotConsuming(), level),
                gunShootAmmoNotConsuming
        );
        return gunShootAmmoNotConsuming;
    }

    public static ItemStack createLetBulletFly(int level) {
        ItemStack letBulletFly = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new LetBulletFlyEnchantment(), level),
                letBulletFly
        );
        return letBulletFly;
    }

    public static ItemStack createAshBullet(int level) {
        ItemStack ashBullet = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new AshBulletEnchantment(), level),
                ashBullet
        );
        return ashBullet;
    }


    public static ItemStack createMagicBullet(int level) {
        ItemStack magicBullet = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new MagicBulletEnchantment(), level),
                magicBullet
        );
        return magicBullet;
    }

    public static ItemStack createGunsCriticalHitPotionDelayed(int level) {
        ItemStack gunsCriticalHitPotionDelayed = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new GunsCriticalHitPotionDelayed(), level),
                gunsCriticalHitPotionDelayed
        );
        return gunsCriticalHitPotionDelayed;
    }

    public static ItemStack createGunsCorrosion(int level) {
        ItemStack gunsCorrosion = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(
                Collections.singletonMap(new GunsCorrosionEnchantment(), level),
                gunsCorrosion
        );
        return gunsCorrosion;
    }


    // 生成不同等级的附魔书（I到V级）
    public static final ItemStack LOWER_FIREPOWER_I = createLowerFirepowerBook(1);
    public static final ItemStack LOWER_FIREPOWER_II = createLowerFirepowerBook(2);
    public static final ItemStack LOWER_FIREPOWER_III = createLowerFirepowerBook(3);
    public static final ItemStack LOWER_FIREPOWER_IV = createLowerFirepowerBook(4);
    public static final ItemStack LOWER_FIREPOWER_V = createLowerFirepowerBook(5);

    public static final ItemStack COMMON_FIREPOWER_I = createCommonFirepowerBook(1);
    public static final ItemStack COMMON_FIREPOWER_II = createCommonFirepowerBook(2);
    public static final ItemStack COMMON_FIREPOWER_III = createCommonFirepowerBook(3);
    public static final ItemStack COMMON_FIREPOWER_IV = createCommonFirepowerBook(4);
    public static final ItemStack COMMON_FIREPOWER_V = createCommonFirepowerBook(5);

    public static final ItemStack ADVANCED_FIREPOWER_I = createAdvancedFirepowerBook(1);
    public static final ItemStack ADVANCED_FIREPOWER_II = createAdvancedFirepowerBook(2);
    public static final ItemStack ADVANCED_FIREPOWER_III = createAdvancedFirepowerBook(3);
    public static final ItemStack ADVANCED_FIREPOWER_IV = createAdvancedFirepowerBook(4);
    public static final ItemStack ADVANCED_FIREPOWER_V = createAdvancedFirepowerBook(5);

    public static final ItemStack SUPER_FIREPOWER_I = createSuperFirepowerBook(1);
    public static final ItemStack SUPER_FIREPOWER_II = createSuperFirepowerBook(2);
    public static final ItemStack SUPER_FIREPOWER_III = createSuperFirepowerBook(3);
    public static final ItemStack SUPER_FIREPOWER_IV = createSuperFirepowerBook(4);
    public static final ItemStack SUPER_FIREPOWER_V = createSuperFirepowerBook(5);

    public static final ItemStack ULTRA_FIREPOWER_I = createUltraFirepowerBook(1);
    public static final ItemStack ULTRA_FIREPOWER_II = createUltraFirepowerBook(2);
    public static final ItemStack ULTRA_FIREPOWER_III = createUltraFirepowerBook(3);
    public static final ItemStack ULTRA_FIREPOWER_IV = createUltraFirepowerBook(4);
    public static final ItemStack ULTRA_FIREPOWER_V = createUltraFirepowerBook(5);

    public static final ItemStack CURSE_FIREPOWER_I = createCurseFirepowerBook(1);
    public static final ItemStack CURSE_FIREPOWER_II = createCurseFirepowerBook(2);
    public static final ItemStack CURSE_FIREPOWER_III = createCurseFirepowerBook(3);
    public static final ItemStack CURSE_FIREPOWER_IV = createCurseFirepowerBook(4);
    public static final ItemStack CURSE_FIREPOWER_V = createCurseFirepowerBook(5);

    public static final ItemStack CRITICAL_HIT_RATE_I = createGunsCriticalHitRateBook(1);
    public static final ItemStack CRITICAL_HIT_RATE_II = createGunsCriticalHitRateBook(2);
    public static final ItemStack CRITICAL_HIT_RATE_III = createGunsCriticalHitRateBook(3);
    public static final ItemStack CRITICAL_HIT_RATE_IV = createGunsCriticalHitRateBook(4);

    public static final ItemStack CRITICAL_HIT_DAMAGE_INCREASE_RATE_I = createGunsCriticalHitDamageIncreaseRateBook(1);
    public static final ItemStack CRITICAL_HIT_DAMAGE_INCREASE_RATE_II = createGunsCriticalHitDamageIncreaseRateBook(2);
    public static final ItemStack CRITICAL_HIT_DAMAGE_INCREASE_RATE_III = createGunsCriticalHitDamageIncreaseRateBook(3);

    public static final ItemStack GUNS_ACCELERATED_RELOADING_I = createGunsAcceleratedReloading(1);
    public static final ItemStack GUNS_ACCELERATED_RELOADING_II = createGunsAcceleratedReloading(2);
    public static final ItemStack GUNS_ACCELERATED_RELOADING_III = createGunsAcceleratedReloading(3);
    public static final ItemStack GUNS_ACCELERATED_RELOADING_IV = createGunsAcceleratedReloading(4);
    public static final ItemStack GUNS_ACCELERATED_RELOADING_V  = createGunsAcceleratedReloading(5);

    public static final ItemStack SHOOT_GUN_BULLET_INCREASE_I  = createShootGunBulletIncrease(1);
    public static final ItemStack SHOOT_GUN_BULLET_INCREASE_II  = createShootGunBulletIncrease(2);
    public static final ItemStack SHOOT_GUN_BULLET_INCREASE_III  = createShootGunBulletIncrease(3);

    public static final ItemStack NO_THINK_SHOOT  = createNoThinkShoot(1);

    public static final ItemStack EXTRA_GUNS_PENETRATE_I  = createExtraGunsPenetrate(1);
    public static final ItemStack EXTRA_GUNS_PENETRATE_II  = createExtraGunsPenetrate(2);
    public static final ItemStack EXTRA_GUNS_PENETRATE_III  = createExtraGunsPenetrate(3);
    public static final ItemStack EXTRA_GUNS_PENETRATE_VI  = createExtraGunsPenetrate(4);
    public static final ItemStack EXTRA_GUNS_PENETRATE_V  = createExtraGunsPenetrate(5);

    public static final ItemStack EXTRA_CATAPULT_BULLET_INCREASE_I  = createExtraCatapultBulletIncrease(1);
    public static final ItemStack EXTRA_CATAPULT_BULLET_INCREASE_II  = createExtraCatapultBulletIncrease(2);

    public static final ItemStack GUN_SHOOT_AMMO_NOT_CONSUMING_I  = createGunShootAmmoNotConsuming(1);
    public static final ItemStack GUN_SHOOT_AMMO_NOT_CONSUMING_II  = createGunShootAmmoNotConsuming(2);
    public static final ItemStack GUN_SHOOT_AMMO_NOT_CONSUMING_III  = createGunShootAmmoNotConsuming(3);
    public static final ItemStack GUN_SHOOT_AMMO_NOT_CONSUMING_IV  = createGunShootAmmoNotConsuming(4);
    public static final ItemStack GUN_SHOOT_AMMO_NOT_CONSUMING_V  = createGunShootAmmoNotConsuming(5);

    public static final ItemStack LET_BULLET_FLY_I  = createLetBulletFly(1);

    public static final ItemStack ASH_BULLET_I  = createAshBullet(1);

    public static final ItemStack MAGIC_BULLET_I  = createMagicBullet(1);

    public static final ItemStack ADVANCED_CRITICAL_HIT_RATE_I = createAdvancedGunsCriticalHitRateBook(1);
    public static final ItemStack ADVANCED_CRITICAL_HIT_RATE_II = createAdvancedGunsCriticalHitRateBook(2);
    public static final ItemStack ADVANCED_CRITICAL_HIT_RATE_III = createAdvancedGunsCriticalHitRateBook(3);
    public static final ItemStack ADVANCED_CRITICAL_HIT_RATE_IV = createAdvancedGunsCriticalHitRateBook(4);

    public static final ItemStack ADVANCED_CRITICAL_HIT_DAMAGE_INCREASE_RATE_I = createAdvancedGunsCriticalHitDamageIncreaseRateBook(1);
    public static final ItemStack ADVANCED_CRITICAL_HIT_DAMAGE_INCREASE_RATE_II = createAdvancedGunsCriticalHitDamageIncreaseRateBook(2);
    public static final ItemStack ADVANCED_CRITICAL_HIT_DAMAGE_INCREASE_RATE_III = createAdvancedGunsCriticalHitDamageIncreaseRateBook(3);

    public static final ItemStack ADVANCED_EXTRA_GUNS_PENETRATE_I  = createAdvancedExtraGunsPenetrate(1);
    public static final ItemStack ADVANCED_EXTRA_GUNS_PENETRATE_II  = createAdvancedExtraGunsPenetrate(2);
    public static final ItemStack ADVANCED_EXTRA_GUNS_PENETRATE_III  = createAdvancedExtraGunsPenetrate(3);
    public static final ItemStack ADVANCED_EXTRA_GUNS_PENETRATE_VI  = createAdvancedExtraGunsPenetrate(4);
    public static final ItemStack ADVANCED_EXTRA_GUNS_PENETRATE_V  = createAdvancedExtraGunsPenetrate(5);

    public static final ItemStack GUNS_CRITICAL_HIT_POTION_DELAYED_I  = createGunsCriticalHitPotionDelayed(1);

    public static final ItemStack GUNS_CORROSION_I  = createGunsCorrosion(1);
    public static final ItemStack GUNS_CORROSION_II  = createGunsCorrosion(2);
    public static final ItemStack GUNS_CORROSION_III  = createGunsCorrosion(3);
}