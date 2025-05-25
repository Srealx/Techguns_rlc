package techguns.items.guns.ammo;

import net.minecraft.item.ItemStack;

import java.util.Arrays;

public class AmmoVariant {

	protected String key;
	protected ItemStack[] ammo;
	protected ItemStack[] bullet;
	
	public AmmoVariant(ItemStack[] ammo, ItemStack[] bullet) {
		this(AmmoTypes.TYPE_DEFAULT,ammo,bullet);
	}
	
	public AmmoVariant(String key, ItemStack[] ammo, ItemStack[] bullet) {
		super();
		this.key = key;
		this.bullet = bullet;
		this.ammo = ammo;
	}

	public String getKey() {
		return key;
	}

	public ItemStack[] getAmmo(){
		return this.ammo;
	}

	@Override
	public String toString() {
		return "AmmoVariant{" +
				"key='" + key + '\'' +
				", ammo=" + Arrays.toString(ammo) +
				", bullet=" + Arrays.toString(bullet) +
				'}';
	}
}
