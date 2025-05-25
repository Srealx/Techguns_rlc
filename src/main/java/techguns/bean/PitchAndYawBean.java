package techguns.bean;

public class PitchAndYawBean {
    public PitchAndYawBean(Float pitch, Float yaw) {
        this.pitch = pitch;
        this.yaw = yaw;
    }

    private Float pitch;
    private Float yaw;

    public Float getPitch() {
        return pitch;
    }

    public void setPitch(Float pitch) {
        this.pitch = pitch;
    }

    public Float getYaw() {
        return yaw;
    }

    public void setYaw(Float yaw) {
        this.yaw = yaw;
    }

    public PitchAndYawBean setYawReverse(){
        this.yaw = -yaw;
        return this;
    }
}
