package techguns.client.render.entities.projectiles;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import techguns.Techguns;
import techguns.entities.projectiles.StoneBulletProjectileAnShan;

public class RenderStoneBulletAnshanProjectile extends RenderTextureProjectile<StoneBulletProjectileAnShan> {

    public RenderStoneBulletAnshanProjectile(RenderManager renderManager)
    {	
    	super(renderManager);
    	textureLoc = new ResourceLocation(Techguns.MODID,"textures/entity/handgunbullet_anshan.png");
    	scale=1.0f;
    	baseSize=0.1f;
    }
    
}
