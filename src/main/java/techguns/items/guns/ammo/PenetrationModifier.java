package techguns.items.guns.ammo;

/**
 * @author srealx
 * @date 2025/2/27
 */
public class PenetrationModifier {
    public static final PenetrationModifier DEFAULT_MODIFIER = new PenetrationModifier();

    protected float penMul=1.0f;
    protected float penAdd=0f;

    public PenetrationModifier setPen(float mul, float add) {
        if (mul != 0f){
            this.penMul=mul;
        }
        this.penAdd=add;
        return this;
    }

    public float getPen(float pen) {
        return pen * this.penMul + this.penAdd;
    }

    public float getPenMul(){
        return this.penMul;
    }

    public float getPenAdd(){
        return this.penAdd;
    }


}
