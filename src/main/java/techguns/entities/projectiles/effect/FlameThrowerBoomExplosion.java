package techguns.entities.projectiles.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import techguns.entities.projectiles.GenericProjectile;

import java.util.List;

/**
 * 自定义爆炸-火焰喷射器
 */
public class FlameThrowerBoomExplosion extends Explosion {


    private World aworld;

    private double ax;

    private  double ay;

    private double az;

    private float asize;

    private Entity aexploder;

    private Entity hitEntity;

    public FlameThrowerBoomExplosion(World world, Entity entity, double x, double y, double z,  float strength,
                                     Entity hitEntity) {
        super(world, entity, x, y, z, strength, Boolean.FALSE, Boolean.FALSE);
        this.ax = x;
        this.ay = y;
        this.az = z;
        this.asize = strength;
        this.aexploder = entity;
        this.aworld = world;
        this.hitEntity = hitEntity;
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
                        if(!entity.isImmuneToFire()) {
                            entity.setFire(4);
                        }
                    }
                }
            }
        });
    }

}
