package techguns.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author srealx
 * @date 2025/3/31
 */
public class QualityDataBean {
    private Map<String,Float> attributeModifier;

    public QualityDataBean() {
        attributeModifier = new HashMap<>();
    }

    public void addAttribute(String attributeName, Float amount){
        this.attributeModifier.put(attributeName,amount);
    }

    public Float findAttributeAmount(String attributeName){
        return this.attributeModifier.get(attributeName);
    }
}
