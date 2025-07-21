package techguns.joint;

import com.mojang.realmsclient.gui.ChatFormatting;
import lykrast.defiledlands.common.init.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import techguns.damagesystem.TGDamageSource;
import techguns.items.guns.GenericGun;
import techguns.util.TextUtil;

import java.util.List;

/**
 * @author srealx
 * @date 2025/4/1
 */
public class ArmorSuitJoint implements IDamageSourceInitProgress ,IAddInformation{
    private static ArmorSuitJoint armorSuitJoint;

    private ArmorSuitJoint(){}

    public static ArmorSuitJoint obtain(){
        if (armorSuitJoint==null){
            armorSuitJoint = new ArmorSuitJoint();
        }
        return armorSuitJoint;
    }

    @Override
    public void progress(TGDamageSource damageSource) {
        if (damageSource.getSourceItemStack()==null || !(damageSource.getSourceItemStack().getItem() instanceof GenericGun)){
            return;
        }
        if (damageSource.attacker instanceof EntityPlayer){
            EntityPlayer player =  (EntityPlayer) damageSource.attacker;
            // 金套
            boolean allGold =  checkPlayWearAllMaterialArmor(player, ItemArmor.ArmorMaterial.GOLD);
            if (Boolean.TRUE.equals(allGold) && ((GenericGun) damageSource.getSourceItemStack().getItem()).getMaterial() == Item.ToolMaterial.GOLD){
                damageSource.setOriginalDamage(damageSource.getOriginalDamage() * 1.5f);
            }
            // 金属龙套
            boolean allScalesGolden =  checkPlayWearAllMaterialArmor(player, ModItems.materialScalesGolden);
            if (Boolean.TRUE.equals(allScalesGolden) && ((GenericGun) damageSource.getSourceItemStack().getItem()).getMaterial() == Item.ToolMaterial.GOLD){
                damageSource.setOriginalDamage(damageSource.getOriginalDamage() * 2f);
            }
        }
    }

    @Override
    public String modName() {
        return "ArmorSuit";
    }

    @Override
    public void add(ItemStack stack, World worldIn, List<String> list, ITooltipFlag flagIn) {
        if (stack==null||worldIn==null||list==null){
            return;
        }
        Item.ToolMaterial material = ((GenericGun) stack.getItem()).getMaterial();
        if (stack.getItem() instanceof GenericGun && material == Item.ToolMaterial.GOLD){
            List<EntityPlayer> playerEntities = worldIn.playerEntities;
            boolean inHand = Boolean.FALSE;
            EntityPlayer holdPlayer = null;
            for (EntityPlayer playerEntity : playerEntities) {
                // 先检查主手和副手
                ItemStack heldItemMainhand = playerEntity.getHeldItemMainhand();
                ItemStack heldItemOffhand = playerEntity.getHeldItemOffhand();
                if (ItemStack.areItemsEqual(stack,heldItemMainhand) || ItemStack.areItemsEqual(stack,heldItemOffhand)){
                    inHand = Boolean.TRUE;
                    holdPlayer = playerEntity;
                }
                InventoryPlayer inventory = playerEntity.inventory;
                for (ItemStack itemStack : inventory.mainInventory) {
                    if(ItemStack.areItemsEqual(stack, itemStack)){
                        holdPlayer = playerEntity;
                        break;
                    }
                }
            }
            if (holdPlayer == null){
                return;
            }
            if (Boolean.FALSE.equals(inHand)){
                list.add(ChatFormatting.RED + "=== " + TextUtil.trans("techguns.gun.tooltip.goldMaterial") + " (0/1) ===");
                list.add(ChatFormatting.RED +"     "+TextUtil.trans("techguns.gun.tooltip.goldMaterial.effect"));
                return;
            }
            // 金书龙套
            boolean allScalesGolden = checkPlayWearAllMaterialArmor(holdPlayer, ModItems.materialScalesGolden);
            if (allScalesGolden){
                list.add(ChatFormatting.GREEN + "=== " + TextUtil.trans("techguns.gun.tooltip.goldMaterial") + " (1/1) ===");
                list.add(ChatFormatting.GREEN +"     "+TextUtil.trans("techguns.gun.tooltip.goldMaterial.effect")+"+100%");
                return;
            }
            // 金套
            boolean allGoldArmor = checkPlayWearAllMaterialArmor(holdPlayer, ItemArmor.ArmorMaterial.GOLD);
            if (allGoldArmor){
                list.add(ChatFormatting.GREEN + "=== " + TextUtil.trans("techguns.gun.tooltip.goldMaterial") + " (1/1) ===");
                list.add(ChatFormatting.GREEN +"     "+TextUtil.trans("techguns.gun.tooltip.goldMaterial.effect")+"+50%");
                return;
            }
            list.add(ChatFormatting.GREEN + "=== " + TextUtil.trans("techguns.gun.tooltip.goldMaterial") + " (0/1) ===");
            list.add(ChatFormatting.DARK_PURPLE +"     "+TextUtil.trans("techguns.gun.tooltip.goldMaterial.effect")+"+50%");
        }
    }

    private boolean checkPlayWearAllMaterialArmor(EntityPlayer entityPlayer, ItemArmor.ArmorMaterial armorMaterial){
        NonNullList<ItemStack>  armorInventory =  entityPlayer.inventory.armorInventory;
        boolean allMaterial = Boolean.TRUE;
        // 遍历护甲是否都是金材质
        for (ItemStack itemStack : armorInventory) {
            if(itemStack.isEmpty() || !(itemStack.getItem() instanceof ItemArmor)){
                allMaterial = Boolean.FALSE;
                break;
            }
            ItemArmor.ArmorMaterial itemArmorMaterial = ((ItemArmor) itemStack.getItem()).getArmorMaterial();

            if (itemArmorMaterial != armorMaterial){
                allMaterial = Boolean.FALSE;
                break;
            }
        }
        return allMaterial;
    }
}
