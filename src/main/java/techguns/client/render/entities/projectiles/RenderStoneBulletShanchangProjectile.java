package techguns.client.render.entities.projectiles;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import techguns.Techguns;
import techguns.entities.projectiles.StoneBulletProjectileShanChang;

public class RenderStoneBulletShanchangProjectile extends RenderTextureProjectile<StoneBulletProjectileShanChang> {

    public RenderStoneBulletShanchangProjectile(RenderManager renderManager)
    {	
    	super(renderManager);
    	textureLoc = new ResourceLocation(Techguns.MODID,"textures/entity/handgunbullet_shanchang.png");
    	scale=1.0f;
    	baseSize=0.1f;
    }
    
}
