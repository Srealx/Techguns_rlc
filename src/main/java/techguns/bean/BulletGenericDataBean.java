package techguns.bean;

/**
 * @author srealx
 * @date 2025/3/14
 */
public class BulletGenericDataBean {

    private String damageUuid;

    private Integer bulletRegenerationCount;

    private Float knockBackRate;

    public BulletGenericDataBean(){

    }

    public BulletGenericDataBean(String damageUuid){
        this.damageUuid = damageUuid;
    }

    public String getDamageUuid() {
        return damageUuid;
    }

    public void setDamageUuid(String damageUuid) {
        this.damageUuid = damageUuid;
    }

    public Integer getBulletRegenerationCount() {
        return bulletRegenerationCount;
    }

    public void setBulletRegenerationCount(Integer bulletRegenerationCount) {
        this.bulletRegenerationCount = bulletRegenerationCount;
    }

    public Float getKnockBackRate() {
        return knockBackRate;
    }

    public void setKnockBackRate(Float knockBackRate) {
        this.knockBackRate = knockBackRate;
    }
}
