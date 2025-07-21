package techguns.damagesystem.damageHandler;

/**
 * @Author Srealx
 * @Date 2025/5/25
 */
public interface IChainHandler<T> {

    default boolean isStartNode(){
        return Boolean.FALSE;
    }

    T getNextNode();
}
