package techguns.entities.projectiles.effect;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import techguns.damagesystem.TGDamageSource;
import techguns.deatheffects.EntityDeathUtils;
import techguns.enchantment.GenericGunEnchantment;
import techguns.entities.projectiles.GenericProjectile;
import techguns.joint.IDamageSourceInitProgress;
import techguns.joint.ModsJointRegistrar;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 自定义爆炸-TFG
 */
public class TFGBoomExplosion extends Explosion {

    private float aboomAmount;

    private World aworld;

    private double ax;

    private  double ay;

    private double az;

    private float asize;

    private Entity aexploder;

    private Entity hitEntity;

    private Entity projectile;

    private float penetration;

    private ItemStack itemStack;

    private String damageUuid;

    public TFGBoomExplosion(World world, Entity entity, double x, double y, double z, float amount, float strength,
                            Entity hitEntity,Entity projectile,float penetration,ItemStack itemStack,String damageUuid) {
        super(world, entity, x, y, z, strength, Boolean.FALSE, Boolean.FALSE);
        this.aboomAmount = amount;
        this.ax = x;
        this.ay = y;
        this.az = z;
        this.asize = strength;
        this.aexploder = entity;
        this.aworld = world;
        this.hitEntity = hitEntity;
        this.projectile = projectile;
        this.penetration = penetration;
        this.itemStack = itemStack;
        this.damageUuid = damageUuid;
    }


    @Override
    public void doExplosionA() {
        int k;
        int l;
        float f3 = this.asize * 2.0F;
        k = MathHelper.floor(this.ax - (double)f3 - 1.0D);
        l = MathHelper.floor(this.ax + (double)f3 + 1.0D);
        int i2 = MathHelper.floor(this.ay - (double)f3 - 1.0D);
        int i1 = MathHelper.floor(this.ay + (double)f3 + 1.0D);
        int j2 = MathHelper.floor(this.az - (double)f3 - 1.0D);
        int j1 = MathHelper.floor(this.az + (double)f3 + 1.0D);
        List<Entity> list = this.aworld.getEntitiesWithinAABBExcludingEntity(this.aexploder, new AxisAlignedBB((double)k, (double)i2, (double)j2, (double)l, (double)i1, (double)j1));
        list.forEach(entity->{
            if (!entity.isImmuneToExplosions() && !(entity instanceof EntityPlayer)
                && !(entity instanceof EntityItem) && (hitEntity!=null?entity!=hitEntity:Boolean.TRUE)
                && !(entity instanceof GenericProjectile)) {
                double d12 = entity.getDistance(this.ax, this.ay, this.az) / (double)f3;
                if (d12 <= 1.0D) {
                    double d5 = entity.posX - this.ax;
                    double d7 = entity.posY + (double)entity.getEyeHeight() - this.ay;
                    double d9 = entity.posZ - this.az;
                    double d13 = (double)MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                    if (d13 != 0) {
                        TGDamageSource knockbackDummyDmgSrc = TGDamageSource.causeExplosionDamage(projectile, aexploder, EntityDeathUtils.DeathType.DISMEMBER);
                        knockbackDummyDmgSrc.setPenetration(this.penetration);
                        knockbackDummyDmgSrc.setOriginalDamage(aboomAmount);
                        knockbackDummyDmgSrc.setNoKnockback();
                        knockbackDummyDmgSrc.setSourceItemStack(this.itemStack);
                        knockbackDummyDmgSrc.setDamageUuid(this.damageUuid);
                        knockbackDummyDmgSrc.setPlaySoundNextDerivativeBullet(Boolean.FALSE);
                        knockbackDummyDmgSrc.attacker = this.aexploder;
                        List<GenericGunEnchantment> enchantmentList = null;
                        if (this.itemStack!=null) {
                            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(this.itemStack);
                            enchantmentList = enchantments.keySet().stream().filter(item -> item instanceof GenericGunEnchantment)
                                    .map(item -> (GenericGunEnchantment) item).sorted(Comparator.comparingInt(GenericGunEnchantment::getSort)).collect(Collectors.toList());
                        }
                        if (enchantmentList!=null){
                            enchantmentList.forEach(item->item.afterGetProjectileDamageSource(knockbackDummyDmgSrc));
                        }
                        // 执行附魔效果
                        // other mods progress
                        List<IDamageSourceInitProgress> jointList = ModsJointRegistrar.getJointList(IDamageSourceInitProgress.class);
                        jointList.forEach(item->item.progress(knockbackDummyDmgSrc));
                        if (this.itemStack!=null){
                            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(this.itemStack);
                            // 执行附魔效果
                            enchantmentList.forEach(e->e.afterDamageSourceInit(knockbackDummyDmgSrc));
                        }
                        // 使dmg与orDmg数值保持一致
                        aboomAmount = knockbackDummyDmgSrc.getOriginalDamage();
                        entity.attackEntityFrom(knockbackDummyDmgSrc, aboomAmount);
                        if(!entity.isImmuneToFire()) {
                            entity.setFire(3);
                        }
                    }
                }
            }
        });
    }

}
