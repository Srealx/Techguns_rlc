package techguns.damagesystem;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import techguns.api.damagesystem.DamageType;
import techguns.deatheffects.EntityDeathUtils.DeathType;

public class TGDamageSource extends EntityDamageSource {

		protected boolean attackSuccessful=false;
	
		public Entity attacker=null;
		public DamageType damageType;
		public DeathType deathType;
		public float goreChance=0.5f;
		
		public boolean knockbackOnShieldBlock=true;
		
		/**
		 * Anti-Toughness
		 */
		public float armorPenetration=0.0f;
		public boolean ignoreHurtresistTime=false;
		public float knockbackMultiplier=1.0f;
		
		/**
		 * set to true when this damagesource was converted from another damage source, with new TGDamageSource(DamageSource src) 
		 */
		public boolean wasConverted=false;

		private float originalDamage;

		private float additionalDamageRate = 0f;

		private float penetration;

		private float extraCriticalHitRate = 0;

		private float extraCriticalHitDamageRate = 0;

		private Boolean criticalHitFlat = Boolean.FALSE;

		private Boolean playSound = Boolean.TRUE;

		private Boolean playSoundNextDerivativeBullet = Boolean.TRUE;
	/**
	 * 该字段用于处理单发多次伤害的武器: 如霰弹， NDR等， 来保证每次伤害都原子化的被计算是否暴击与暴击伤害
	 */
	private String damageUuid;

	/**
	 * sourceItem
	 */
	private ItemStack sourceItemStack;

	public String getDamageUuid() {
		return damageUuid;
	}

	public void setDamageUuid(String damageUuid) {
		this.damageUuid = damageUuid;
	}

	public Boolean getPlaySound() {
		return playSound;
	}

	public void setPlaySound(Boolean playSound) {
		this.playSound = playSound;
	}

	public Boolean getPlaySoundNextDerivativeBullet() {
		return playSoundNextDerivativeBullet;
	}

	public void setPlaySoundNextDerivativeBullet(Boolean playSoundNextDerivativeBullet) {
		this.playSoundNextDerivativeBullet = playSoundNextDerivativeBullet;
	}

	public ItemStack getSourceItemStack() {
		return sourceItemStack;
	}

	public void setSourceItemStack(ItemStack sourceItemStack) {
		this.sourceItemStack = sourceItemStack;
	}

	public float getExtraCriticalHitRate() {
		return extraCriticalHitRate;
	}

	public void setExtraCriticalHitRate(float extraCriticalHitRate) {
		this.extraCriticalHitRate = extraCriticalHitRate;
	}

	public float getExtraCriticalHitDamageRate() {
		return extraCriticalHitDamageRate;
	}

	public void setExtraCriticalHitDamageRate(float extraCriticalHitDamageRate){
		this.extraCriticalHitDamageRate = extraCriticalHitDamageRate;
	}

	public void addExtraCriticalHitDamageRate(float extraCriticalHitDamageRate) {
		this.extraCriticalHitDamageRate += extraCriticalHitDamageRate;
	}

	public Boolean getCriticalHitFlat() {
		return criticalHitFlat;
	}

	public void setCriticalHitFlat(Boolean criticalHitFlat) {
		this.criticalHitFlat = criticalHitFlat;
	}

	public float getAdditionalDamageRate() {
		return additionalDamageRate;
	}

	public void setAdditionalDamageRate(float additionalDamageRate) {
		this.additionalDamageRate = additionalDamageRate;
	}

	public void addAdditionalDamageRate(float additionalDamageRate){
		this.additionalDamageRate += additionalDamageRate;
	}

	public float getPenetration() {
		return penetration;
	}

	public void setPenetration(float penetration) {
		this.penetration = penetration;
	}

	public float getOriginalDamage() {
		return originalDamage;
	}

	public void setOriginalDamage(float originalDamage) {
		this.originalDamage = originalDamage;
	}

	protected static ArrayList<String> unresistableTypes = new ArrayList<String>();
		static {
			unresistableTypes.add("inWall");
			unresistableTypes.add("drown");
			unresistableTypes.add("starve");
			unresistableTypes.add("fall");
			unresistableTypes.add("outOfWorld");
		}

		public void setAttackSuccessful() {
			this.attackSuccessful=true;
		}
		
		public boolean wasSuccessful() {
			return this.attackSuccessful;
		}
		
		public static TGDamageSource causeBulletDamage(Entity projectile, Entity shooter, DeathType deathType){
			TGDamageSource src = new TGDamageSource("tg_bullet",projectile, shooter, DamageType.PROJECTILE, deathType);
			src.knockbackOnShieldBlock=false;
			src.ignoreHurtresistTime=true;
			return src;
		}
		
		public static TGDamageSource causeExplosionDamage(Entity projectile, Entity shooter, DeathType deathType){
			return new TGDamageSource("tg_explosion",projectile, shooter, DamageType.EXPLOSION, deathType);
		}
		
		public static TGDamageSource causePoisonDamage(Entity projectile, Entity shooter, DeathType deathType){
			TGDamageSource src = new TGDamageSource("tg_poison",projectile, shooter, DamageType.POISON, deathType);
			src.ignoreHurtresistTime=true;
			src.knockbackOnShieldBlock=false;
			return src;
		}
		
		
		public static TGDamageSource causeFireDamage(Entity projectile, Entity shooter, DeathType deathType){
			TGDamageSource src = new TGDamageSource("tg_fire",projectile, shooter, DamageType.FIRE, deathType);
			src.ignoreHurtresistTime=true;
			src.knockbackOnShieldBlock=false;
			return src;
		}
		
		public static TGDamageSource getKnockbackDummyDmgSrc(Entity projectile, Entity shooter){
			return new TGDamageSource("tg_knockback", projectile, shooter, DamageType.PHYSICAL, DeathType.DEFAULT);
		}
		
		public static TGDamageSource causeEnergyDamage(Entity projectile, Entity shooter, DeathType deathType){
			TGDamageSource src = new TGDamageSource("tg_energy",projectile, shooter, DamageType.ENERGY, deathType);
			src.ignoreHurtresistTime=true;
			src.knockbackOnShieldBlock=false;
			return src;
		}

		public static TGDamageSource causeRadiationDamage(Entity projectile, Entity shooter, DeathType deathType){
			TGDamageSource src = new TGDamageSource("tg_rad",projectile, shooter, DamageType.RADIATION, deathType);
			src.ignoreHurtresistTime=true;
			src.knockbackOnShieldBlock=false;
			return src;
		}
		
		public static TGDamageSource causeLethalRadPoisoningDamage(Entity projectile, Entity shooter, DeathType deathType){
			TGDamageSource src = new TGDamageSource("tg_rad_poisoning",projectile, shooter, DamageType.UNRESISTABLE, deathType);
			src.ignoreHurtresistTime=true;
			src.knockbackOnShieldBlock=false;
			src.goreChance=1.0f;
			return src;
		}
		
		public static TGDamageSource causeLightningDamage(Entity projectile, Entity shooter, DeathType deathType){
			TGDamageSource src = new TGDamageSource("tg_lightning",projectile, shooter, DamageType.LIGHTNING, deathType);
			src.ignoreHurtresistTime=true;
			src.knockbackOnShieldBlock=false;
			return src;
		}
		
		public static TGDamageSource causeDarkDamage(Entity projectile, Entity shooter, DeathType deathType){
			TGDamageSource src = new TGDamageSource("tg_dark",projectile, shooter, DamageType.DARK, deathType);
			src.ignoreHurtresistTime=true;
			src.knockbackOnShieldBlock=false;
			return src;
		}
		
		public static TGDamageSource getFromGenericDamageSource(DamageSource src){
			if(src instanceof TGDamageSource){
				return (TGDamageSource) src;
			}
			return new TGDamageSource(src);
		}
		
		public  TGDamageSource setNoKnockback(){
			this.knockbackMultiplier=0.25f;
			return this;
		}
		public TGDamageSource setKnockback(float mult){
			this.knockbackMultiplier=mult;
			return this;
		}
		
		public boolean hasKnockback(){
			return this.knockbackMultiplier>0.0f;
		}
		
	    /**
	     * Gets the death message that is displayed when the player dies
	     */
		@Override
	    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn)
	    {
			ITextComponent itextcomponent;
			ItemStack itemstack = ItemStack.EMPTY;
			if(this.attacker==null && this.damageSourceEntity==null) {	
				itextcomponent = entityLivingBaseIn.getDisplayName();
			} else {	
				itextcomponent = this.attacker == null ? this.damageSourceEntity.getDisplayName() : this.attacker.getDisplayName();
				itemstack = this.attacker instanceof EntityLivingBase ? ((EntityLivingBase)this.attacker).getHeldItemMainhand() : ItemStack.EMPTY;  
			}
	        String s = "death.attack." + this.getDamageType();
	        String s1 = s + ".item";
	        return !itemstack.isEmpty() && itemstack.hasDisplayName() && I18n.canTranslate(s1) ? new TextComponentTranslation(s1, new Object[] {entityLivingBaseIn.getDisplayName(), itextcomponent, itemstack.getTextComponent()}) : new TextComponentTranslation(s, new Object[] {entityLivingBaseIn.getDisplayName(), itextcomponent});
	    }
		
		public static TGDamageSource copyWithNewEnt(TGDamageSource other, Entity damagingEntity, Entity attacker) {
			TGDamageSource newSrc = new TGDamageSource(other.getDamageType(), damagingEntity, attacker, other.damageType, other.deathType);
			newSrc.knockbackMultiplier = other.knockbackMultiplier;
			newSrc.armorPenetration = other.armorPenetration;
			newSrc.goreChance = other.goreChance;
			newSrc.ignoreHurtresistTime = other.ignoreHurtresistTime;
			return newSrc;
		}
		
		public TGDamageSource(String name, Entity damagingEntity, Entity attacker, DamageType damageType, DeathType deathType) {
			super(name, damagingEntity);
			this.attacker = attacker;
			this.damageType = damageType;
			this.deathType = deathType;
			setBehaviourForVanilla();
		}

		public void setBehaviourForVanilla(){
			switch(damageType){
				case ENERGY:
//					this.setMagicDamage();
					this.setProjectile();
					break;
				case EXPLOSION:
					this.setExplosion();
					break;
				case FIRE:
					//not set as fire damage since this would cause immunity with fire resistance :-/
//					this.setMagicDamage();
					this.setProjectile();
					break;
				case ICE:
//					this.setMagicDamage();
					this.setProjectile();
					break;
				case LIGHTNING:
//					this.setMagicDamage();
					this.setProjectile();
					break;
				case PHYSICAL:
					break;
				case POISON:
					this.setProjectile();
					this.setMagicDamage();
					break;
				case PROJECTILE:
					this.setProjectile();
					break;
				case RADIATION:
//					this.setMagicDamage();
					this.setProjectile();
					break;
				case DARK:
//					this.setMagicDamage();
					this.setProjectile();
					break;
				case UNRESISTABLE:
					this.setDamageBypassesArmor();
					this.setDamageIsAbsolute();
					break;
				default:
					break;
			}
		}
		
		public TGDamageSource(EntityDamageSourceIndirect dmg){
			super(dmg.damageType,dmg.getImmediateSource());
			this.attacker=dmg.getTrueSource();
			this.determineTGDamageType(dmg);
			this.wasConverted=true;
			
			if(dmg.canHarmInCreative()){
				this.setDamageAllowedInCreativeMode();
			}
			if(dmg.isDamageAbsolute()){
				this.setDamageIsAbsolute();
			}
			if(dmg.isDifficultyScaled()){
				this.setDamageIsAbsolute();
			}
			if(dmg.isExplosion()){
				this.setExplosion();
			}
			if(dmg.isFireDamage()){
				this.setFireDamage();
			}
			if(dmg.isMagicDamage()){
				this.setMagicDamage();
			}
			if(dmg.isProjectile()){
				this.setProjectile();
			}
			if(dmg.isUnblockable()){
				this.setDamageBypassesArmor();
			}
		}
		
		
		public TGDamageSource(EntityDamageSource dmg){
			super(dmg.damageType,dmg.getImmediateSource());
			this.attacker= dmg.getTrueSource();
			this.determineTGDamageType(dmg);
			this.wasConverted=true;
			
			if(dmg.canHarmInCreative()){
				this.setDamageAllowedInCreativeMode();
			}
			if(dmg.isDamageAbsolute()){
				this.setDamageIsAbsolute();
			}
			if(dmg.isDifficultyScaled()){
				this.setDamageIsAbsolute();
			}
			if(dmg.isExplosion()){
				this.setExplosion();
			}
			if(dmg.isFireDamage()){
				this.setFireDamage();
			}
			if(dmg.isMagicDamage()){
				this.setMagicDamage();
			}
			if(dmg.isProjectile()){
				this.setProjectile();
			}
			if(dmg.isUnblockable()){
				this.setDamageBypassesArmor();
			}
		}
		
		public TGDamageSource(DamageSource dmg){
			super(dmg.damageType, dmg.getImmediateSource());
			this.attacker=dmg.getTrueSource();
			this.determineTGDamageType(dmg);
			this.wasConverted=true;
			
			if(dmg.canHarmInCreative()){
				this.setDamageAllowedInCreativeMode();
			}
			if(dmg.isDamageAbsolute()){
				this.setDamageIsAbsolute();
			}
			if(dmg.isDifficultyScaled()){
				this.setDifficultyScaled();
			}
			if(dmg.isExplosion()){
				this.setExplosion();
			}
			if(dmg.isFireDamage()){
				this.setFireDamage();
			}
			if(dmg.isMagicDamage()){
				this.setMagicDamage();
			}
			if(dmg.isProjectile()){
				this.setProjectile();
				this.knockbackOnShieldBlock=false;
			}
			if(dmg.isUnblockable()){
				this.setDamageBypassesArmor();
			}
		}
		
		private void determineTGDamageType(DamageSource dmg){
			if( dmg.isExplosion()){
				damageType = DamageType.EXPLOSION;
			} else if (dmg.isMagicDamage()) {
				damageType = DamageType.ENERGY;
			} else if (dmg.isFireDamage() || dmg.getDamageType().equals("dragonBreath")) {
				damageType = DamageType.FIRE;
			} else if (dmg.isProjectile()){
				damageType = DamageType.PROJECTILE;
			} else if (dmg.getDamageType().equals("wither")){
				damageType = DamageType.POISON;
			} else if (dmg.getDamageType().equals("lightningBolt")){
				damageType = DamageType.LIGHTNING;
			} else if( dmg.canHarmInCreative() || unresistableTypes.contains(dmg.damageType)){
				damageType = DamageType.UNRESISTABLE;
			} else {
				damageType = DamageType.PHYSICAL;
			}
			
			
		}
		
		@Override
		public Entity getTrueSource() {
			return this.attacker;
		}

		@Override
		public Entity getImmediateSource() {
			return this.damageSourceEntity;
		}

		public boolean knockbackOnShieldBlock() {
			return this.knockbackOnShieldBlock;
		}

		public TGDamageSource setKnockbackOnShieldBlock(boolean knockbackOnShieldBlock) {
			this.knockbackOnShieldBlock = knockbackOnShieldBlock;
			return this;
		}

}
