package techguns.damagesystem.damageHandler;

/**
 * @Author Srealx
 * @Date 2025/5/25
 */
public class DamageHandlerFactory {
    private static IChainedDamageSystemHandler startNode;

    static {
        startNode = RestoreDamageHandler.obtain();
    }

    public static IChainedDamageSystemHandler getHandler(){
        return startNode;
    }

}
