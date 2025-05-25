package techguns.joint;

import levelup2.skills.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import techguns.damagesystem.TGDamageSource;

import java.util.List;

/**
 * @author srealx
 * @date 2025/4/29
 */
public class LevelUp2Joint implements IDamageSourceInitProgress,IOriginalRestoreProgress,IModifyInformationData,IExtraAttributeProgress{
    private static LevelUp2Joint levelUp2Joint;

    private LevelUp2Joint(){}

    public static LevelUp2Joint obtain(){
        if (levelUp2Joint==null){
            levelUp2Joint = new LevelUp2Joint();
        }
        return levelUp2Joint;
    }

    private static final float addRateDamageEveryLevel = 0.03f;

    private static final float addRateCriticalEveryLevel = 0.025f;


    @Override
    public void progress(TGDamageSource damageSource) {
        if (!(damageSource.attacker instanceof EntityPlayer)){
            return;
        }
        EntityPlayer player = (EntityPlayer) damageSource.attacker;
        // 根据技能等级提升伤害与暴击率
        int damageLevel = SkillRegistry.getSkillLevel(player, "levelup:sworddamage");
        int criticalLevel = SkillRegistry.getSkillLevel(player, "levelup:swordcrit");
        if (damageLevel>0){
            // 每级技能 + 3% 基础子弹伤害
            damageSource.setOriginalDamage(damageSource.getOriginalDamage() * (1 + damageLevel * addRateDamageEveryLevel));
        }
        if (criticalLevel>0){
            // 每级技能 + 2.5% 子弹暴击率
            damageSource.setExtraCriticalHitRate(damageSource.getExtraCriticalHitRate() + criticalLevel * addRateCriticalEveryLevel);
        }
    }


    @Override
    public Float modify(ItemStack stack, World worldIn, Float data, String informationName) {
        // 查找该Itemstack的拥有者
        EntityPlayer holdPlayer = findHoldPlayer(stack,worldIn);
        if (holdPlayer == null){
            return data;
        }
        if (informationName.equals("techguns.gun.tooltip.damage")){
            int damageLevel = SkillRegistry.getSkillLevel(holdPlayer, "levelup:sworddamage");
            if (damageLevel > 0){
                return data * ( 1 + damageLevel * addRateDamageEveryLevel);
            }
        }
        if (informationName.equals("techguns.gun.tooltip.extraCriticalHitRate")){
            int criticalLevel = SkillRegistry.getSkillLevel(holdPlayer, "levelup:swordcrit");
            if (criticalLevel>0){
                return data + addRateCriticalEveryLevel * criticalLevel;
            }
        }
        return data;
    }


    @Override
    public float progress(EntityLivingBase shooter, EntityLivingBase entity, float amount) {
        if (! (shooter instanceof EntityPlayer)){
            return amount;
        }
        EntityPlayer player = (EntityPlayer) shooter;
        int damageLevel = SkillRegistry.getSkillLevel(player, "levelup:sworddamage");
        if (damageLevel>0){
            return amount / (1 + damageLevel * 0.05f);
        }
        return amount;
    }


    @Override
    public float progress(World worldIn, ItemStack itemStack, String attributeName) {
        EntityPlayer holdPlayer = findHoldPlayer(itemStack, worldIn);
        if (holdPlayer == null){
            return 0;
        }
        if (attributeName.equals("CriticalRate")){
            int criticalLevel = SkillRegistry.getSkillLevel(holdPlayer, "levelup:swordcrit");
            return criticalLevel * addRateCriticalEveryLevel;
        }
        return 0;
    }


    @Override
    public String modName() {
        return "levelUp2";
    }

    private EntityPlayer findHoldPlayer(ItemStack stack,World worldIn){
        List<EntityPlayer> playerEntities = worldIn.playerEntities;
        for (EntityPlayer playerEntity : playerEntities) {
            // 先检查主手和副手
            ItemStack heldItemMainhand = playerEntity.getHeldItemMainhand();
            ItemStack heldItemOffhand = playerEntity.getHeldItemOffhand();
            if (ItemStack.areItemsEqual(stack,heldItemMainhand) || ItemStack.areItemsEqual(stack,heldItemOffhand)){
                return playerEntity;
            }
            InventoryPlayer inventory = playerEntity.inventory;
            for (ItemStack itemStack : inventory.mainInventory) {
                if(ItemStack.areItemsEqual(stack, itemStack)){
                    return playerEntity;
                }
            }
        }
        return null;
    }

}
