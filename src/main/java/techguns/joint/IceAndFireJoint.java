package techguns.joint;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author srealx
 * @date 2025/5/21
 */
public class IceAndFireJoint implements ICollisionEntityFindProgress{
    private static IceAndFireJoint iceAndFireJoint;

    private IceAndFireJoint(){}

    public static IceAndFireJoint obtain(){
        if (iceAndFireJoint==null){
            iceAndFireJoint = new IceAndFireJoint();
        }
        return iceAndFireJoint;
    }


    private static final String ICEANDFIRE_PART_ENTITY_CLASS = "com.github.alexthe666.iceandfire.entity.util.EntityMultipartPart";
    private static Class<?> partEntityClass;
    private static Method parentMethod;

    // 判断实体是否为多部分子实体
    public static boolean isIceAndFireMultiPart(Entity entity) {
        return partEntityClass != null && partEntityClass.isInstance(entity);
    }

    // 获取父实体
    @Override
    public Entity progress(Entity part) {
        if (part instanceof EntityLivingBase){
            return part;
        }
        if (partEntityClass == null || parentMethod == null){
            try {
                partEntityClass = Class.forName(ICEANDFIRE_PART_ENTITY_CLASS);
                parentMethod = partEntityClass.getDeclaredMethod("getParent");
            }catch (ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        }
        parentMethod.setAccessible(true);
        if (parentMethod != null && isIceAndFireMultiPart(part)) {
            try {
                return (Entity) parentMethod.invoke(part);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return part;
    }

    @Override
    public String modName() {
        return "iceAndFire";
    }
}
