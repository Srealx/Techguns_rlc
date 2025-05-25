package techguns.joint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author srealx
 * @date 2025/4/1
 */
public class ModsJointRegistrar {
    public static List<IModsJoint> modsJointList;
    static {
        modsJointList = new ArrayList<>();
        modsJointList.add(ToolsQualityJoint.obtain());
        modsJointList.add(ArmorSuitJoint.obtain());
        modsJointList.add(PowerPotionJoint.obtain());
        modsJointList.add(PlayerAttributeJoint.obtain());
        modsJointList.add(TrinketsPoisonStoneJoint.obtain());
        modsJointList.add(LevelUp2Joint.obtain());
        modsJointList.add(MagicPotionJoint.obtain());
        modsJointList.add(MagicAttributeJoint.obtain());
        modsJointList.add(TrinketsWitherRingJoint.obtain());
        modsJointList.add(IceAndFireJoint.obtain());
    }

    public static <T extends IModsJoint> List<T> getJointList(Class<T> tClass){
        List<T> returnList = new ArrayList<>();
        modsJointList.forEach(item->{
            if (tClass.isAssignableFrom(item.getClass())){
                returnList.add((T)item);
            }
        });
        return returnList;
    }

}
