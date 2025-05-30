package techguns.items.guns;

import java.lang.reflect.Field;
import java.util.*;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.stats.StatList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import scala.Int;
import techguns.*;
import techguns.api.damagesystem.DamageType;
import techguns.api.guns.GunHandType;
import techguns.api.guns.IGenericGun;
import techguns.api.render.IItemTGRenderer;
import techguns.bean.BulletGenericDataBean;
import techguns.bean.EnchantmentAndLevelBean;
import techguns.bean.ShootGunSpawnProjectileEventBean;
import techguns.cache.PlayerDataCache;
import techguns.cache.ZoomCacheDataBean;
import techguns.capabilities.TGExtendedPlayer;
import techguns.client.ClientProxy;
import techguns.client.ShooterValues;
import techguns.client.audio.TGSoundCategory;
import techguns.damagesystem.TGDamageSource;
import techguns.deatheffects.EntityDeathUtils.DeathType;
import techguns.enchantment.*;
import techguns.entities.ai.EntityAIRangedAttack;
import techguns.entities.npcs.NPCTurret;
import techguns.entities.projectiles.EnumBulletFirePos;
import techguns.entities.projectiles.GenericProjectile;
import techguns.items.GenericItem;
import techguns.items.armors.GenericArmor;
import techguns.items.armors.ICamoChangeable;
import techguns.items.armors.TGArmorBonus;
import techguns.items.guns.ammo.*;
import techguns.joint.*;
import techguns.packets.GunFiredMessage;
import techguns.packets.PacketGunsLossDurableTarget;
import techguns.packets.PacketShootAmmoNotConsumingTarget;
import techguns.packets.ReloadStartedMessage;
import techguns.plugins.crafttweaker.EnumGunStat;
import techguns.task.FovTask;
import techguns.util.*;

public class GenericGun extends GenericItem implements IGenericGun, IItemTGRenderer, ICamoChangeable,IGenericGunLevel {
	public static final float SOUND_DISTANCE=4.0f;

	// Weapon Stats
	boolean semiAuto=false; // = false;
	int minFiretime=4; // = 0;
	int clipsize=10; // = 10;
	int reloadtime=40; // = 40; //ticks
	float damage = 2.0f; // = 10;
	SoundEvent firesound = TGSounds.M4_FIRE;
	SoundEvent reloadsound = TGSounds.M4_RELOAD;
	SoundEvent firesoundStart = TGSounds.M4_FIRE;
	SoundEvent rechamberSound = null;
	int ammoCount; // ammo per reload
	// float recoil = 25.0f;
	float zoomMult = 1.0f;
	boolean canZoom = false;
	boolean toggleZoom = false;
	boolean fireCenteredZoomed=false;
	int ticksToLive = 40;
	float speed = 2.5f;

	float damageMin=1.0f;
	float damageDropStart=20f;
	float damageDropEnd=40f;
	float penetration = 0.0f;

	AmmoType ammoType = AmmoTypes.PISTOL_ROUNDS;

	public boolean shotgun = false;
	boolean burst = false;
	float spread = 0.015f;
	int bulletcount = 7;
	float accuracy = 0.0f;

	public boolean regenerationBulletGun = Boolean.FALSE;
	public int regenerationCount=0;

	public boolean snipeGun = Boolean.FALSE;
	public SoundEvent snipeGunCriticalSound;
	public String criticalShootFx;
	public String criticalShootFxName;

	float projectileForwardOffset = 0.0f;
	
	int maxLoopDelay = 0;
	int recoiltime = 5; // ticks;

	int muzzleFlashtime = 5;
	boolean silenced = false;

	boolean checkRecoil = false;
	boolean checkMuzzleFlash = false;
	
	float AI_attackRange = 15.0F; //sqrt of actual distance
	int AI_attackTime = 60;
	int AI_burstCount = 0;
	int AI_burstAttackTime = 0;
	
	int camoCount=1;
	
	int lockOnTicks = 0; //MaximumLockOnTime
	int lockOnPersistTicks = 0;
	// guns level
	int gunLevel = 1;

	float knockBackRate = 0f;

	ToolMaterial material = ToolMaterial.IRON;

	Item maintenanceItem = TGItems.MECHANICAL_PARTS_IRON.getItem();

	// when shoot loss durability
	int consumingDurabilityEveryShoot = 1;
	
	EnumCrosshairStyle crossHairStyle = EnumCrosshairStyle.GUN_DYNAMIC;
	
	public ArrayList<ResourceLocation> textures;
	
	//protected IProjectileFactory projectile;
	protected ProjectileSelector projectile_selector; 
	
	GunHandType handType = GunHandType.TWO_HANDED; 
	//Ammount of bullets the gun gets per bullet item
	//protected float shotsPerBullet;
	
	int miningAmmoConsumption = 1;
	
	float meleeDamagePwr = 6.0f;
	float meleeDamageEmpty = 2.0f;
	float digSpeed=1.0f;
	
	/**
	 * spread multiplier while shooting zoomed
	 */
	float zoombonus=1.0f;
	
	float radius=1.0f;
	double gravity=0.0f;

	boolean shootWithLeftClick=true;
	
	public float turretPosOffsetX=0.0f;
	public float turretPosOffsetY=0.0f;
	public float turretPosOffsetZ=0.0f;
	
	public static ArrayList<GenericGun> guns = new ArrayList<>();
	
	/**
	 * Lightpulse parameters
	 */
	public int light_lifetime =2;
	public float light_radius_start = 3.0f;
	public float light_radius_end = 3.0f;
	public float light_r = 1f;
	public float light_g = 0.9f;
	public float light_b = 0.2f;
	protected boolean muzzelight=true;
	
	boolean hasAimedBowAnim=true;
	
	boolean hasAmbientEffect=false;
	/**
	 * Should bind the texture on rendering?
	 */
	public boolean hasCustomTexture=true;

	protected RangeTooltipType rangeTooltipType = RangeTooltipType.DROP;
	
	private GenericGun(String name) {
		super(name,false);
		this.setMaxStackSize(1);
//		this.setNoRepair();
		//TGuns.ITEMREGISTRY.register(this);
	}

	public GenericGun(String name, ProjectileSelector projectileSelector, boolean semiAuto, int minFiretime, int clipsize, int reloadtime, float damage, SoundEvent firesound, SoundEvent reloadsound, int TTL, float accuracy){
		this(true, name,projectileSelector, semiAuto, minFiretime, clipsize, reloadtime, damage, firesound, reloadsound, TTL, accuracy);
	}
	
	public GenericGun setMuzzleLight(float r, float g, float b) {
		this.light_r=r;
		this.light_g=g;
		this.light_b=b;
		return this;
	}
	
	public GenericGun setNoMuzzleLight() {
		this.muzzelight=false;
		return this;
	}
	
	public GenericGun setMuzzleLight(int lifetime, float radius_start, float radius_end, float r, float g, float b) {
		this.light_lifetime = lifetime;
		this.light_radius_start= radius_start;
		this.light_radius_end= radius_end;
		this.setMuzzleLight(r, g, b);
		return this;
	}
	
	public GenericGun setRangeTooltipType(RangeTooltipType type) {
		this.rangeTooltipType=type;
		return this;
	}
	
	public GenericGun setHasAmbient() {
		this.hasAmbientEffect=true;
		return this;
	}
	
	public boolean hasAmbientEffect() {
		return this.hasAmbientEffect;
	}
	
	public GenericGun setNoCustomTexture() {
		this.hasCustomTexture=false;
		return this;
	}
	
	public GenericGun setNoBowAnim() {
		this.hasAimedBowAnim=false;
		return this;
	}
	
	public boolean hasBowAnim() {
		return this.hasAimedBowAnim;
	}
	
	public GenericGun(boolean addToGunList,String name, ProjectileSelector projectile_selector, boolean semiAuto, int minFiretime, int clipsize, int reloadtime, float damage, SoundEvent firesound, SoundEvent reloadsound, int TTL, float accuracy){
		this(name);
		setMaxDamage(1200);
		this.ammoType = projectile_selector.ammoType;
		this.semiAuto = semiAuto;
		this.minFiretime = minFiretime;
		this.clipsize = clipsize;
		this.reloadtime = reloadtime * 2;
		this.damage = damage;
		this.firesound = firesound;
		this.reloadsound = reloadsound;

		this.ammoCount=1;
		//this.shotsPerBullet=clipsize;
		this.ticksToLive=TTL;
		this.accuracy =accuracy;
		
		//Defaults to no drop
		this.damageDropStart=TTL;
		this.damageDropEnd=TTL;
		this.damageMin=damage;
		
		this.projectile_selector = projectile_selector;
		
		if (addToGunList) {
			guns.add(this);
		}
	}
	
	public GenericGun setGravity(double grav) {
		this.gravity=grav;
		return this;
	}
	
	public GenericGun setAmmoCount(int count) {
		this.ammoCount=count;
		//this.shotsPerBullet=(this.clipsize*1.0f)/(this.ammoCount*1.0f);
		return this;
	}

	public GenericGun setZoom(float mult, boolean toggle, float bonus, boolean fireCenteredWhileZooming){
		this.canZoom=true;
		this.zoomMult = mult;
		this.toggleZoom = toggle;
		this.zoombonus=bonus;
		this.fireCenteredZoomed=fireCenteredWhileZooming;
		return this;
	}
	
	
	public GenericGun setShotgunSpred(int count, float spread){
		return this.setShotgunSpread(count, spread, false);
	}
	
	public GenericGun setShotgunSpread(int count, float spread, boolean burst){
		this.shotgun=true;
		this.spread=spread;
		this.bulletcount=count;
		this.burst=burst;
		return this;
	}

	public GenericGun setRegenerationBulletCount(int count){
		this.regenerationBulletGun = Boolean.TRUE;
		this.regenerationCount = count;
		return this;
	}

	public GenericGun isSnipeGun(SoundEvent soundEvent,String criticalShootFx,String criticalShootFxName){
		this.snipeGun = Boolean.TRUE;
		this.snipeGunCriticalSound = soundEvent;
		this.criticalShootFx = criticalShootFx;
		this.criticalShootFxName = criticalShootFxName;
		return this;
	}

	public ToolMaterial getMaterial(){
		return this.material;
	}

	public GenericGun setMaterial(ToolMaterial material){
		this.material = material;
		return this;
	}

	public SoundEvent getSnipeGunCriticalSound(){
		return this.snipeGunCriticalSound;
	}

	public GenericGun setKnockBackRate(float knockBackRate){
		this.knockBackRate = knockBackRate;
		return this;
	}

	public Item getMaintenanceItem(){
		return this.maintenanceItem;
	}

	public GenericGun setMaintenanceItem(Item item){
		this.maintenanceItem = item;
		return this;
	}

	public GenericGun setConsumingDurabilityEveryShoot(int consumingDurabilityEveryShoot) {
		this.consumingDurabilityEveryShoot = consumingDurabilityEveryShoot;
		return this;
	}

	public GenericGun setForwardOffset(float offset) {
		this.projectileForwardOffset = offset;
		return this;
	}
	
	public GenericGun setBulletSpeed(float speed){
		this.speed = speed;
		return this;
	}
	
	public GenericGun setPenetration(float pen){
		this.penetration = pen;
		return this;
	}
	
	public GenericGun setMuzzleFlashTime(int time) {
		this.muzzleFlashtime = time;
		return this;
	}

	public SoundEvent getFiresound(){
		return this.firesound;
	}

	public GenericGun setFiresoundStart(SoundEvent firesoundStart) {
		this.firesoundStart = firesoundStart;
		return this;
	}
	
	public GenericGun setMaxLoopDelay(int maxLoopDelay) {
		this.maxLoopDelay = maxLoopDelay;
		return this;
	}
	
	public GenericGun setRecoiltime(int recoiltime){
		this.recoiltime = recoiltime;
		return this;
	}
	
	public GenericGun setRechamberSound(SoundEvent rechamberSound) {
		this.rechamberSound = rechamberSound;
		return this;
	}
	
	/**
	 * @param x - Offset sideways, +right -left
	 * @param y - Offset height
	 * @param z - offset forward/backward
	 * @return
	 */
	public GenericGun setTurretPosOffset(float x, float y, float z){
		this.turretPosOffsetX=x;
		this.turretPosOffsetY=y;
		this.turretPosOffsetZ=z;
		return this;
	}
	
	protected int getScaledTTL(){
		return (int) Math.ceil(this.ticksToLive/this.speed);
	}
	
	/**
	 * @param ticks Time required to lock target
	 * @param lockExtraTicks Extra time that is added when the lock is complete
	 */
	public GenericGun setLockOn(int ticks, int lockExtraTicks) {
		this.lockOnTicks = ticks;
		this.lockOnPersistTicks = lockExtraTicks;
		return this;
	}

	public GenericGun setGunLevel(int level){
		this.gunLevel = level;
		return this;
	}

	public int getLockOnTicks() {
		return lockOnTicks;
	}

	public int getLockOnPersistTicks() {
		return lockOnPersistTicks;
	}

	/**
	 * Called only clientside!, requires packets for actions other than zoom (clientside
	 * @param player
	 * @param stack
	 */
	public boolean gunSecondaryAction(EntityPlayer player, ItemStack stack) {
		if (player.world.isRemote && canZoom  && this.toggleZoom && !ShooterValues.getPlayerIsReloading(player, false)) {
			ClientProxy cp = ClientProxy.get();
			if (cp.player_zoom != 1.0f) {
				cp.player_zoom = 1.0f;
			} else {
				// 如果玩家处于奔跑状态, 取消奔跑
				if (player.isSprinting()){
					player.setSprinting(Boolean.FALSE);
				}
				cp.player_zoom = this.zoomMult;
			}
			UUID playerUuid = player.getUniqueID();
			if (cp.player_zoom!=1.0f){
				int nextFov = (int) (Minecraft.getMinecraft().gameSettings.fovSetting * cp.player_zoom);
				FovTask.fovChange((int)Minecraft.getMinecraft().gameSettings.fovSetting, nextFov);
				ZoomCacheDataBean zoomCacheDataBean = new ZoomCacheDataBean();
				zoomCacheDataBean.setOrigin((int)Minecraft.getMinecraft().gameSettings.fovSetting);
				zoomCacheDataBean.setTarget(nextFov);
				PlayerDataCache.PLAYER_ZOOM_DATA.put(playerUuid,zoomCacheDataBean);
			}
//			else {
//				nextFov = PlayerDataCache.PLAYER_ZOOM_DATA.get(playerUuid);
//			}

			return true;
    	}
		return false; //(this.getGunHandType() == GunHandType.TWO_HANDED); //Block the mouse click if two-handed gun
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		if(this.shootWithLeftClick){
			return true;
		} else {
			if(this.getCurrentAmmo(stack)>=this.miningAmmoConsumption){
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		/*if(this.getGunHandType() == GunHandType.TWO_HANDED) {
			if (this.canClickBlock(worldIn, playerIn, handIn)) {
				System.out.println("PASS!");
				return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
			} else {
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
			}
		} else {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
		}*/
		/*if (this.gunSecondaryAction(playerIn, playerIn.getHeldItem(handIn))) {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
		}*/
		this.gunSecondaryAction(playerIn, playerIn.getHeldItem(handIn));
		return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
	}

	public ItemStack[] getReloadItem(ItemStack stack) {
		return this.ammoType.getAmmo(this.getCurrentAmmoVariant(stack));
	}
	
	public int getAmmoCount() {
		return ammoCount;
	}
	
	public int getAmmoLeftCountTooltip(ItemStack item){
		int ammo = this.getCurrentAmmo(item);
		if(this.burst){
			return ammo*(this.bulletcount+1);
		} else {
			return ammo;
		}
	}

	public int getClipsizeTooltip() {
		if(this.burst){
			return clipsize*(this.bulletcount+1);
		} else {
			return clipsize;
		}
	}
	
	public int getAmmoLeftCount(int mags){
		int count=(this.clipsize/this.ammoCount) * mags; 
		if(this.burst){
			return count*(this.bulletcount+1);
		} else {
			return count;
		}
	}

	protected void spawnProjectile(final World world, final EntityLivingBase player, final ItemStack itemstack, float spread, float offset, float damagebonus, EnumBulletFirePos firePos, Entity target, String damageUuid) {
		/*GenericProjectile proj = new GenericProjectile(world, player, damage * damagebonus, speed, this.getScaledTTL(), spread, this.damageDropStart, this.damageDropEnd,
				this.damageMin * damagebonus, this.penetration, getDoBlockDamage(player), leftGun);*/
		
		IProjectileFactory<GenericProjectile> projectile = this.projectile_selector.getFactoryForType(this.getCurrentAmmoVariantKey(itemstack));
		BulletGenericDataBean bulletGenericDataBean = new BulletGenericDataBean();
		bulletGenericDataBean.setDamageUuid(damageUuid);
		bulletGenericDataBean.setBulletRegenerationCount(this.regenerationCount);
		if (this.knockBackRate != 0f){
			bulletGenericDataBean.setKnockBackRate(this.knockBackRate);
		}
		// Enchanting gain
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemstack);
		enchantments.forEach((k,v)->{
			if (k instanceof GenericGunEnchantment){
				((GenericGunEnchantment)k).beforeProjectilesCreate(bulletGenericDataBean,itemstack);
			}
		});

		float speed = this.speed;
		// 执行子弹前处理器
		List<IProjectileCreateProgress> jointList = ModsJointRegistrar.getJointList(IProjectileCreateProgress.class);
		for (IProjectileCreateProgress item : jointList) {
			speed =  item.progress(speed,itemstack,player);
		}
		// 使用附魔与外界增加子弹速度
		for (Enchantment enchantment : enchantments.keySet()) {
			if (enchantment instanceof GenericGunEnchantment){
				speed = ((GenericGunEnchantment)enchantment).beforeProjectileCreate(itemstack,speed);
			}
		}
		float damage = this.damage;
		float damageMin = this.damageMin;
		float penetration = this.penetration;
		float durable = getDamageRate(itemstack);
		if (durable > 0.4f && durable <= 0.6f){
			damage *= 0.75f;
			damageMin *= 0.75f;
			spread *= 1.25f;
			speed *= 0.85f;
			penetration -= 0.05f;
		}else if (durable > 0.6f && durable <= 0.8f){
			damage *= 0.6f;
			damageMin *= 0.6f;
			spread *= 1.5f;
			speed *= 0.75f;
			penetration -= 0.1f;
		}else if (durable > 0.8f){
			damage *= 0.45f;
			damageMin *= 0.45f;
			spread *= 1.75f;
			speed *= 0.65f;
			penetration -= 0.18f;
		}

		GenericProjectile proj = projectile.createProjectile(this, world, player, damage * damagebonus, speed, this.getScaledTTL(), spread, this.damageDropStart,
				this.damageDropEnd, damageMin * damagebonus, penetration, getDoBlockDamage(player), firePos, radius, gravity,getLevel(),itemstack,bulletGenericDataBean);

		float f=1.0f;
		if(this.muzzelight) {
			Techguns.proxy.createLightPulse(proj.posX+player.getLookVec().x*f, proj.posY+player.getLookVec().y*f, proj.posZ+player.getLookVec().z*f, this.light_lifetime, this.light_radius_start, this.light_radius_end, this.light_r, this.light_g, this.light_b);
		}
		if (silenced) {
			proj.setSilenced();
		}
		if (offset > 0.0f) {
			proj.shiftForward(offset/speed);
		}
		world.spawnEntity(proj);
	}

	public static boolean getDoBlockDamage(EntityLivingBase elb) {
		boolean blockdamage = false;
		if (elb instanceof EntityPlayer){
			TGExtendedPlayer caps = TGExtendedPlayer.get((EntityPlayer) elb);
			if(caps!=null){ blockdamage=!caps.enableSafemode; }
		}
		
		return blockdamage;
	}

	@Override
	public boolean isShootWithLeftClick() {
		return this.shootWithLeftClick;
	}

	@Override
	public boolean isSemiAuto() {
		return this.semiAuto;
	}
	
	public GenericGun setCheckRecoil(){
		this.checkRecoil=true;
		return this;
	}
	
	public GenericGun setCheckMuzzleFlash(){
		this.checkMuzzleFlash=true;
		return this;
	}

	@Override
	public boolean isZooming() {
		return ClientProxy.get().player_zoom==this.zoomMult;
	}

	@Override
	public void shootGunPrimary(ItemStack stack, World world, EntityPlayer player, boolean zooming, EnumHand hand, Entity target) {
    		int ammo = this.getCurrentAmmo(stack);
    	
    		//System.out.println("Shoot gun:"+stack+" Hand:"+hand);
    		
    		byte ATTACK_TYPE = 0;
    		TGExtendedPlayer extendedPlayer = TGExtendedPlayer.get(player);
    		if (ammo>0) {
				// 判断当前玩家是否处于奔跑状态, 如果奔跑则停止
				if(player.isSprinting()){
					player.setSprinting(Boolean.FALSE);
				}

    			//bullets left
		    	int firedelay = extendedPlayer.getFireDelay(hand);
				long fireDelayOfGun = extendedPlayer.gunCanShootResidualTime(world,stack);
				if (firedelay <=0  && fireDelayOfGun<=0) {
		    		extendedPlayer.setFireDelay(hand,this.minFiretime);
					Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
					// 附魔: 处理开火冷却
					enchantments.forEach((k,v)->{
						if(k instanceof GenericGunEnchantment){
							((GenericGunEnchantment) k).afterShootGunPredicate(world,extendedPlayer,stack,player);
						}
					});
//					if (world.isRemote){
//
//					}
					if (!player.capabilities.isCreativeMode) {
	    				this.useAmmo(stack, 1);
						if (!world.isRemote){
							// 附魔: 消费子弹后, 可用来回复子弹, 或者判断剩余子弹数量的逻辑
							enchantments.forEach((k,v)->{
								if (k instanceof GenericGunEnchantment){
									((GenericGunEnchantment) k).afterAmmoUsed(this,stack,1,hand,player);
								}
							});
						}
					}
		    		
			        if (!world.isRemote)
			        {
			        	/*
			        	 * If SERVER, create projectile
			        	 */
			        	
			        	float accuracybonus = MathUtil.clamp(1.0f-GenericArmor.getArmorBonusForPlayer(player, TGArmorBonus.GUN_ACCURACY,false), 0f,1f);
			        	
			        	//add spread penalty when shooting while blocking
			        	if(hand== EnumHand.MAIN_HAND && player.isActiveItemStackBlocking()) {
			        		if(this.handType==GunHandType.ONE_HANDED) {
			        			accuracybonus *= 4.0f;
			        		} else {
			        			accuracybonus *= 8.0f;
			        		}
			        	}
			        	
			        	EnumBulletFirePos firePos;
			        	if ((hand == EnumHand.MAIN_HAND && player.getPrimaryHand() == EnumHandSide.RIGHT) || (hand == EnumHand.OFF_HAND && player.getPrimaryHand() == EnumHandSide.LEFT)) {
			        		firePos = EnumBulletFirePos.RIGHT;
			        	}else {
			        		firePos = EnumBulletFirePos.LEFT;
			        	}
			        	
			        	if(zooming){
			        		accuracybonus*=this.zoombonus;
			        		if (fireCenteredZoomed) {
			        			firePos=EnumBulletFirePos.CENTER;
			        		}
			        	}
						// 关键射击方法
			        	this.shootGun(world, player,stack, accuracybonus,1.0f,ATTACK_TYPE, hand,firePos, target);

						// 消耗枪械耐久
						if (!player.capabilities.isCreativeMode) {
							Integer nextTotalDamage = lossDurable(stack,1.0f,0.18f);
							TGPackets.network.sendTo(new PacketGunsLossDurableTarget(nextTotalDamage,stack),(EntityPlayerMP) player);
						}
			        } else {
			        	/*
			        	 * If CLIENT, do Effects
			        	 */
						int recoiltime = this.recoiltime;
						EnchantmentAndLevelBean<NoThinkShoot> enchantment = ItemUtil.findEnchantment(stack, NoThinkShoot.class);
						if (enchantment!=null){
							recoiltime *=2;
						}
						int recoiltime_l = (int) ((((float)recoiltime/20.0f)*1000.0f));
			        	int muzzleFlashtime_l = (int) ((((float)muzzleFlashtime/20.0f)*1000.0f));

			        	
			        	if (!checkRecoil || !ShooterValues.isStillRecoiling(player,hand==EnumHand.OFF_HAND, ATTACK_TYPE) ){
			        		//System.out.println("SettingRecoilTime");
				        	ShooterValues.setRecoiltime(player, hand==EnumHand.OFF_HAND,System.currentTimeMillis() + recoiltime_l, recoiltime_l,ATTACK_TYPE);
				        	
			        	}
			        	
			        	ClientProxy cp = ClientProxy.get();
			        	if (!checkMuzzleFlash || ShooterValues.getMuzzleFlashTime(player, hand==EnumHand.OFF_HAND) <= System.currentTimeMillis()) {
			        		ShooterValues.setMuzzleFlashTime(player, hand==EnumHand.OFF_HAND, System.currentTimeMillis() + muzzleFlashtime_l, muzzleFlashtime_l);
			        		Random rand = world.rand;
			        		cp.muzzleFlashJitterX = 1.0f-(rand.nextFloat()*2.0f);
			        		cp.muzzleFlashJitterY = 1.0f-(rand.nextFloat()*2.0f);
			        		cp.muzzleFlashJitterScale = 1.0f-(rand.nextFloat()*2.0f);
			        		cp.muzzleFlashJitterAngle = 1.0f-(rand.nextFloat()*2.0f);
			        	}
			        	
				        client_weaponFired();
			        }
			        /*
			         * Do sounds on Client & Server
			         */
		        	if (maxLoopDelay > 0 && extendedPlayer.getLoopSoundDelay(hand)<=0) {

		        		SoundUtil.playSoundOnEntityGunPosition(world, player, this.firesoundStart, SOUND_DISTANCE, 1.0F, false, false, TGSoundCategory.GUN_FIRE);

		        		extendedPlayer.setLoopSoundDelay(hand,this.maxLoopDelay);
		        		
		        	}else {
						SoundEvent playSound = this.firesound;
						for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
							if (!(entry.getKey() instanceof GenericGunEnchantment)){
								continue;
							}
							playSound = ((GenericGunEnchantment) entry.getKey()).beforeShootGunPlaySound(this,playSound);
						}
		        		SoundUtil.playSoundOnEntityGunPosition(world, player, playSound, SOUND_DISTANCE, 1.0F, false, false, TGSoundCategory.GUN_FIRE);
		        		if (this.maxLoopDelay>0){
		        			extendedPlayer.setLoopSoundDelay(hand,this.maxLoopDelay);
		        		}
		        	}
		        	
		        	if (!(rechamberSound==null)) {

		        		SoundUtil.playSoundOnEntityGunPosition(world, player, rechamberSound, 1.0F, 1.0F, false, false, TGSoundCategory.RELOAD);
		        	}
			        
		    	} else {
		    		//System.out.println(Thread.currentThread().toString()+": Skip shot, can't fire yet");
		    	}
		    	
    		} else {
    			//mag empty, reload needed
    			
    			//look for ammo
				// 以下逻辑被改写: 如果拿到的ammoStack消费失败(余料为0,则重新获取其他AmmoVariant尝试消费)
				ItemStack[] ammoStack = this.ammoType.getAmmo(this.getCurrentAmmoVariant(stack));
				if (InventoryUtil.consumeAmmoPlayer(player, ammoStack)) {
					doReloadAmmo(player,world,extendedPlayer,hand,ammoStack,stack,ATTACK_TYPE);
				} else {
					for (AmmoVariant item : this.ammoType.getVariants()) {
						if (item.getAmmo().equals(ammoStack)){
							continue;
						}
						ammoStack = item.getAmmo();
						if (InventoryUtil.consumeAmmoPlayer(player, ammoStack)){
							doReloadAmmo(player,world,extendedPlayer,hand,ammoStack,stack,ATTACK_TYPE);
							// 修改物品的ammoVariant
							NBTTagCompound tagCompound = stack.getTagCompound();
							tagCompound.setString("ammovariant",item.getKey());
							break;
						}
					}
				}

    		}
	}

	private void doReloadAmmo(EntityPlayer player,World world,TGExtendedPlayer extendedPlayer,EnumHand hand,ItemStack[] ammoStack,ItemStack stack,byte ATTACK_TYPE){
		Arrays.stream(this.ammoType.getEmptyMag()).forEach( e -> {
			if (!e.isEmpty()){
				//player.inventory.addItemStackToInventory(new ItemStack(emptyMag.getItem(),1,emptyMag.getItemDamage()));
				int amount=InventoryUtil.addAmmoToPlayerInventory(player, TGItems.newStack(e, 1));
				if(amount >0 && !world.isRemote){
					player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, TGItems.newStack(e, amount)));
				}
			}
		});

		//stop toggle zooming when reloading
		if (world.isRemote) {
			if (canZoom  && this.toggleZoom) {
				ClientProxy cp = ClientProxy.get();
				if (cp.player_zoom != 1.0f) {
					cp.player_zoom= 1.0f;
					ZoomCacheDataBean zoomCacheDataBean = PlayerDataCache.PLAYER_ZOOM_DATA.get(player.getUniqueID());
					if (zoomCacheDataBean!=null){
						PlayerDataCache.PLAYER_ZOOM_DATA.remove(player.getUniqueID());
						FovTask.fovChange(zoomCacheDataBean.getTarget(),zoomCacheDataBean.getOrigin());
					}
				}
			}
		}

		//START RELOAD
		Integer reloadTime = this.reloadtime;
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
		for (Map.Entry<Enchantment, Integer> item : enchantments.entrySet()) {
			Enchantment enchantment = item.getKey();
			if (enchantment instanceof GenericGunEnchantment){
				reloadTime = ((GenericGunEnchantment) enchantment).beforeReloadAmmo(stack, reloadTime);
			}
		}

//		extendedPlayer.setFireDelay(hand, this.reloadtime-this.minFiretime);
		extendedPlayer.setFireDelay(hand, reloadTime);

		if (ammoCount >1) {
			int i =1;
			while (i<ammoCount && InventoryUtil.consumeAmmoPlayer(player,ammoStack)){
				i++;
			}

			this.reloadAmmo(stack, i);
		} else {
			this.reloadAmmo(stack);
		}

		SoundUtil.playReloadSoundOnEntity(world,player,reloadsound, 1.0F, 1.0F, false, true, TGSoundCategory.RELOAD);


		if (world.isRemote) {

			int time = (int) (((float)reloadTime/20.0f)*1000);
			ShooterValues.setReloadtime(player,hand==EnumHand.OFF_HAND,System.currentTimeMillis()+time, time, ATTACK_TYPE);

			client_startReload();
		} else{
			//send reloadpacket
			//send pakets to clients

			int msg_reloadtime = ((int)(((float)reloadTime/20.0f)*1000.0f));
			TGPackets.network.sendToAllAround(new ReloadStartedMessage(player,hand,msg_reloadtime,ATTACK_TYPE), TGPackets.targetPointAroundEnt(player, 100.0));
			//
		}
		// 借用此时机更新枪械耐久值
		NBTTagCompound tagCompound = stack.getTagCompound();
		if (tagCompound!=null){
			stack.setItemDamage(tagCompound.getInteger("Damage"));
		}
	}
	
	/**
	 * for extra actions in subclass
	 */
	protected void client_weaponFired() {	
	}

	/**
	 * for extra actions in subclass
	 */
	protected void client_startReload() {	
	}
	
	public GenericGun setAIStats(float attackRange, int attackTime, int burstCount, int burstAttackTime) {
		this.AI_attackRange = attackRange;
		this.AI_attackTime = attackTime;
		this.AI_burstCount = burstCount;
		this.AI_burstAttackTime = burstAttackTime;
		return this;
	}
	
	public GenericGun setTexture(String path){
		return setTextures(path,1);
	}
	
	public GenericGun setTextures(String path, int variations){
		Techguns.proxy.setGunTextures(this, path, variations);
		this.camoCount=variations;
		return this;
	}
	
	public GenericGun setTexture(ResourceLocation path){
		return setTextures(path,1);
	}
	
	public GenericGun setTextures(ResourceLocation path, int variations){
		Techguns.proxy.setGunTextures(this, path, variations);
		this.camoCount=variations;
		return this;
	}
	
	@Override
	public ResourceLocation getCurrentTexture(ItemStack stack) {
		int camo = this.getCurrentCamoIndex(stack);
		if (camo < this.textures.size()) {
			return textures.get(camo);
		}
		return this.textures.get(0);
	}

	protected void shootGun(World world, EntityLivingBase player,ItemStack itemstack,float accuracybonus,float damagebonus, int attackType, EnumHand hand, EnumBulletFirePos firePos, Entity target){
		
		//boolean leftGun = (hand == EnumHand.OFF_HAND) != (player.getPrimaryHand() == EnumHandSide.LEFT);
		
		//send pakets to clients
		if (!world.isRemote){
			int recoiltime = this.recoiltime;
			EnchantmentAndLevelBean<NoThinkShoot> enchantment = ItemUtil.findEnchantment(itemstack, NoThinkShoot.class);
			if (enchantment!=null){
				recoiltime *= 2;
			}
	    	int msg_recoiltime = ((int)(((float)recoiltime/20.0f)*1000.0f));
	    	int msg_muzzleflashtime = ((int)(((float)muzzleFlashtime/20.0f)*1000.0f));
	    	TGPackets.network.sendToAllAround(new GunFiredMessage(player,msg_recoiltime,msg_muzzleflashtime,attackType,checkRecoil,hand==EnumHand.OFF_HAND), TGPackets.targetPointAroundEnt(player, 100.0f));
		}
		String damageUuid = UUID.randomUUID().toString();
		//
		spawnProjectile(world, player,itemstack, accuracy*accuracybonus, projectileForwardOffset,damagebonus, firePos, target,damageUuid);
		
        if (shotgun){
        	float offset=0;
        	if (this.burst){
        		offset = this.speed/(this.bulletcount);
        	}

			ShootGunSpawnProjectileEventBean shootGunSpawnProjectileEventBean = new ShootGunSpawnProjectileEventBean();
			shootGunSpawnProjectileEventBean.setBulletCount(this.bulletcount);
			shootGunSpawnProjectileEventBean.setOffset(offset);
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemstack);
			enchantments.forEach((k,v)->{
				if (k instanceof GenericGunEnchantment){
					((GenericGunEnchantment) k).beforeShotGunSpawnProjectile(itemstack,shootGunSpawnProjectileEventBean);
				}
			});
			for (int i=0; i<shootGunSpawnProjectileEventBean.getBulletCount(); i++) {
        		spawnProjectile(world, player,itemstack, spread*accuracybonus,projectileForwardOffset+shootGunSpawnProjectileEventBean.getOffset()*(i+1.0f),damagebonus, firePos, target,damageUuid);
        	}
        }		
	}
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		//super.onCreated(stack, world, player);
		NBTTagCompound tags = stack.getTagCompound();
		if(tags==null){
			tags=new NBTTagCompound();
			stack.setTagCompound(tags);
		
			int dmg = stack.getItemDamage();
			tags.setByte("camo", (byte) 0);
			tags.setString("ammovariant", AmmoTypes.TYPE_DEFAULT);
			tags.setShort("ammo", dmg==0 ? (short)this.clipsize : (short)(this.clipsize-dmg));
			stack.setItemDamage(0);
			this.addInitialTags(tags);
		} else {
			stack.setItemDamage(0);
		}
	}
	
	/**
	 * Add subclass tags here
	 * @param tags
	 */
	protected void addInitialTags(NBTTagCompound tags) {
		
	}
	
	public int getCurrentAmmo(ItemStack stack){
		NBTTagCompound tags = stack.getTagCompound();
		if(tags==null){
			this.onCreated(stack, null, null); //world and player are not needed
			tags = stack.getTagCompound();
		}
		return tags.getShort("ammo");
	}
	
	public int getCurrentAmmoVariant(ItemStack stack){
		String variant = this.getCurrentAmmoVariantKey(stack);
		return this.getAmmoType().getIDforVariantKey(variant);
	}
	
	public String getCurrentAmmoVariantKey(ItemStack stack){
		NBTTagCompound tags = stack.getTagCompound();
		if(tags==null){
			this.onCreated(stack, null, null); //world and player are not needed
			tags = stack.getTagCompound();
		}
		String var = tags.getString("ammovariant");
		if(var==null || var.equals("")) return AmmoTypes.TYPE_DEFAULT;
		return var;
	}
	
	/**
	 * Reduces ammo by amount, use positive values! amount=3 means ammo-=3;
	 * @param stack
	 * @param amount
	 * @return actually consumed ammo
	 */
	public int useAmmo(ItemStack stack, int amount){
		int ammo = this.getCurrentAmmo(stack);
		NBTTagCompound tags = stack.getTagCompound();
		if(ammo-amount>=0){
			tags.setShort("ammo", (short) (ammo-amount));
			return amount;
		} else {
			tags.setShort("ammo", (short) 0);
			return ammo;
		}
	}
	
	public void reloadAmmo(ItemStack stack){
		this.reloadAmmo(stack, this.clipsize);
	}
	
	public void reloadAmmo(ItemStack stack, int amount){
		int ammo = this.getCurrentAmmo(stack);
		NBTTagCompound tags = stack.getTagCompound();
		tags.setShort("ammo", (short) (ammo+amount));
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)){
			ItemStack gun = new ItemStack(this, 1,0);
			this.onCreated(gun, null, null);
			items.add(gun);
		}
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1.0-getPercentAmmoLeft(stack);
	}

	public double getPercentAmmoLeft(ItemStack stack) {
		return ((double)this.getCurrentAmmo(stack))/((double)this.clipsize);
	}
	
	@Override
	public int getAmmoLeft(ItemStack stack) {
		return this.getCurrentAmmo(stack);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		
		return slotChanged || !oldStack.isItemEqual(newStack); 
		/*if (!b && oldStack!=newStack) {
			Techguns.proxy.fixReequipAnim(oldStack, newStack);
		}*/
		//return b;
	}
	
	public GenericGun setDamageDrop(float start, float end, float minDamage){
		this.damageDropStart=start;
		this.damageDropEnd=end;
		this.damageMin=minDamage;
		return this;
	}
	
	public GenericGun setSilenced(boolean s){
		this.silenced=s;
		return this;
	}

	@Override
	public GunHandType getGunHandType() {
		return this.handType;
	}
	
	public GenericGun setHandType(GunHandType type) {
		this.handType=type;
		return this;
	}

	@Override
	public boolean isHoldZoom() {
		return !this.toggleZoom;
	}

	@Override
	public float getZoomMult() {
		return this.zoomMult;
	}


	protected String getSpread(ItemStack stack){
		float accuracy = this.accuracy;
		float durable = getDamageRate(stack);
		int durableReduce = 0;
		if (durable > 0.4f && durable <= 0.6f){
			accuracy *= 1.3f;
			durableReduce = 30;
		}else if (durable > 0.6f && durable <= 0.8f){
			accuracy *= 1.6f;
			durableReduce = 60;
		}else if (durable > 0.8f){
			accuracy *= 1.9f;
			durableReduce = 90;
		}
		String sb = "";
		if(durableReduce != 0){
			sb += ChatFormatting.RED;
		}
		sb += String.format("%.4f",accuracy) + (this.zoombonus!=1.0 ? (" Z:"+ String.format("%.4f",this.zoombonus*accuracy)) : "");
		if(durableReduce != 0){
			sb +=  ChatFormatting.DARK_RED +"(+" + durableReduce +"%)";
		}
		return sb;
	}


	protected String getTooltipTextPenetrate(ItemStack stack,World worldIn,List<IModifyInformationData> jointList,String informationName){
		PenetrationModifier mod = this.projectile_selector.getFactoryForType(this.getCurrentAmmoVariantKey(stack)).getPenetrationModifier();
		float penMul = mod.getPenMul();
		float penAdd = mod.getPenAdd();
		float totalPen = mod.getPen(this.penetration);
		float extraPen = 0f;

		EnchantmentAndLevelBean<ExtraGunsPenetrateEnchantment> enchantment = ItemUtil.findEnchantment(stack, ExtraGunsPenetrateEnchantment.class);
		if (enchantment!=null){
			extraPen += enchantment.getLevel() * enchantment.getEnchantment().getPen();
		}
		for (IModifyInformationData item : jointList) {
			extraPen = item.modify(stack, worldIn ,extraPen, informationName);
		}
		float durable = getDamageRate(stack);
		float durableReduce = 0;
		if (durable > 0.4f && durable <= 0.6f){
			durableReduce -= 0.05f;
		}else if (durable > 0.6f && durable <= 0.8f){
			durableReduce -= 0.12f;
		}else if (durable > 0.8f){
			durableReduce -= 0.2f;
		}
		totalPen += extraPen;
		totalPen += durableReduce;
		if (totalPen <0){
			totalPen = 0;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%.0f",totalPen*100)+"%");
		if (penMul>1.0f){
			sb.append(ChatFormatting.GREEN).append("(").append("+").append(String.format("%.2f",penMul-1)).append("%").append(")");
		}
		if (penAdd+extraPen>0f){
			sb.append(ChatFormatting.GREEN).append("(").append("+").append(String.format("%.0f",(penAdd+extraPen)*100)+"%").append(")");
		}else if (penAdd+extraPen<0){
			sb.append(ChatFormatting.RED).append("(").append(String.format("%.0f",(penAdd+extraPen)*100)+"%").append(")");
		}
		if (durableReduce!=0){
			sb.append(ChatFormatting.DARK_RED).append("(").append(String.format("%.0f",durableReduce*100)+"%").append(")");
		}
		return sb.toString();
	}

	protected String getTooltipTextDmg(ItemStack stack,World worldIn ,boolean expanded,List<IModifyInformationData> jointList,String informationName) {
		DamageModifier mod = this.projectile_selector.getFactoryForType(this.getCurrentAmmoVariantKey(stack)).getDamageModifier();
		// 判断当前枪械是否有附魔
		float otherMulti = 1;
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
		for (Map.Entry<Enchantment, Integer> enchantmentIntegerEntry : enchantments.entrySet()) {
			Enchantment enchantment = enchantmentIntegerEntry.getKey();
			if (enchantment instanceof GunsFirePowerEnchantment){
				GunsFirePowerEnchantment gunsFirePowerEnchantment  = (GunsFirePowerEnchantment) enchantment;
				float additionalDamageEveryLevel = gunsFirePowerEnchantment.getAdditionalDamageEveryLevel();
				otherMulti += additionalDamageEveryLevel * enchantmentIntegerEntry.getValue();
				break;
			}
		}
		for (IModifyInformationData item : jointList) {
			otherMulti = item.modify(stack,worldIn ,otherMulti, informationName);
		}
		float dmg = mod.getDamage(this.damage);
		dmg *=  otherMulti;
		float durable = getDamageRate(stack);
		int durableReduce = 0;
		if (durable > 0.4f && durable <= 0.6f){
			dmg *= 0.75f;
			durableReduce = -25;
		}else if (durable > 0.6f && durable <= 0.8f){
			dmg *= 0.6f;
			durableReduce = -40;
		}else if (durable > 0.8f){
			dmg *= 0.45f;
			durableReduce = -55;
		}
		if(dmg==this.damage) {
			return ""+ (this.damage)+(this.damageMin<this.damage?"-"+(this.damageMin):"");
		} else {
			float dmgmin = mod.getDamage(this.damageMin);
			dmgmin += dmgmin * otherMulti;
			if (durable > 0.4f && durable <= 0.6f){
				dmgmin *= 0.75f;
			}else if (durable > 0.6f && durable <= 0.8f){
				dmgmin *= 0.6f;
			}else if (durable > 0.8f){
				dmgmin *= 0.45f;
			}
			ChatFormatting prefix=ChatFormatting.GREEN;
			String sgn="+";
			if(dmg<this.damage) {
				prefix = ChatFormatting.RED;
				sgn="-";
			}
			String suffix="";

			if(expanded) {
				if(mod.getDmgMul()!=1f || otherMulti != 0f) {
					float f = mod.getDmgMul()-1f;
					if (otherMulti!=0f){
						f =  ((1 + f) * otherMulti)  - 1;
					}
					String x = String.format("%.0f", f * 100f);
					suffix += " ("+sgn+x+"%)";
				}
				if(mod.getDmgAdd()!=0f) {
					float add = mod.getDmgAdd() * otherMulti;
					suffix += " ("+(add>0?"+":"")+String.format("%.1f", add)+")";
				}
			}
			if (durableReduce!=0){
				suffix += ChatFormatting.DARK_RED + " (" + durableReduce +"%)";
			}

			String sd = String.format("%.1f", dmg);
			String sm = String.format("%.1f", dmgmin);
			
			return prefix+""+(sd)+(dmgmin<dmg?"-"+sm:"")+suffix;
		}
		
	}

	protected String getDurable(ItemStack stack){
		float loss = getDamageRate(stack);
		ChatFormatting color = ChatFormatting.GREEN;
		if (loss > 0.4f){
			color = ChatFormatting.YELLOW;
		}
		if (loss > 0.6f){
			color = ChatFormatting.RED;
		}
		if (loss > 0.8f){
			color = ChatFormatting.DARK_RED;
		}
		return color + String.format("%.0f",(1-loss)*100) + "%";
	}
	
	protected String getTooltipTextRange(ItemStack stack) {
		DamageModifier mod = this.projectile_selector.getFactoryForType(this.getCurrentAmmoVariantKey(stack)).getDamageModifier();
		
		int ttl = mod.getTTL(this.ticksToLive);
		float rangeStart = mod.getRange(this.damageDropStart);
		float rangeEnd = mod.getRange(this.damageDropEnd);
		
		String prefix = "";	
		String suffix="";
		if(rangeStart != this.damageDropStart) {

			String sgn="+";
			if (rangeStart > this.damageDropStart) {
				prefix = ChatFormatting.GREEN.toString();
			} else {
				prefix = ChatFormatting.RED.toString();
				sgn="-";
			}
			
			if(mod.getRangeMul()!=1f) {
				float f = mod.getRangeMul()-1f;
				String x = String.format("%.0f", f*100f);
				suffix += " ("+sgn+x+"%)";
			}
			if(mod.getRangeAdd()!=0f) {
				float add = mod.getRangeAdd();
				suffix += " ("+(add>0?"+":"")+String.format("%.1f", add)+")";
			}
	
		} 
			
		String sStart = String.format("%.1f", rangeStart);
		String sEnd = String.format("%.1f", rangeEnd);
		
		if(this.rangeTooltipType == RangeTooltipType.DROP) {
			return TextUtil.trans("techguns.gun.tooltip.range")+": "+prefix+sStart+","+sEnd+","+ttl+suffix;
		} else if ( this.rangeTooltipType == RangeTooltipType.NO_DROP) {
			return TextUtil.trans("techguns.gun.tooltip.range")+": "+prefix+sStart+suffix;
		} else {
			return TextUtil.trans("techguns.gun.tooltip.radius")+": "+prefix+sStart+"-"+sEnd+suffix;
		}
	
	}
	
	protected String getTooltipTextVelocity(ItemStack stack,World worldIn ,List<IModifyInformationData> jointList) {
		DamageModifier mod = this.projectile_selector.getFactoryForType(this.getCurrentAmmoVariantKey(stack)).getDamageModifier();
		float velocity = mod.getVelocity(this.speed);
		for (IModifyInformationData item : jointList) {
			velocity =  item.modify(stack, worldIn ,velocity,"techguns.gun.tooltip.velocity");
		}
		EnchantmentAndLevelBean<LetBulletFlyEnchantment> enchantment = ItemUtil.findEnchantment(stack, LetBulletFlyEnchantment.class);
		if (enchantment!=null){
			velocity *= (1+ LetBulletFlyEnchantment.projectileSpeedAddRate*enchantment.getLevel());
		}
		float durable = getDamageRate(stack);
		int durableReduce = 0;
		if (durable > 0.4f && durable <= 0.6f){
			velocity *= 0.85f;
			durableReduce = -15;
		}else if (durable > 0.6f && durable <= 0.8f){
			velocity *= 0.75f;
			durableReduce = -25;
		}else if (durable > 0.8f){
			velocity *= 0.65f;
			durableReduce = -35;
		}

		String prefix = "";	
		String suffix="";
		if(velocity != this.speed) {

			String sgn="+";
			if (velocity >= this.speed) {
				prefix = ChatFormatting.GREEN.toString();
			} else {
				prefix = ChatFormatting.RED.toString();
				sgn="";
			}
			
			if(mod.getVelocityMul()!=1f) {
				float f = mod.getVelocityMul()-1f;
				String x = String.format("%.0f", f*100f);
				suffix += " ("+sgn+x+"%)";
			}
			if(mod.getVelocityAdd()!=0f) {
				float add = mod.getVelocityAdd();
				suffix += " ("+(add>0?"+":"")+String.format("%.1f", add)+")";
			}
	
		}
		if(durableReduce!=0){
			suffix += ChatFormatting.DARK_RED+" ("+durableReduce+"%)";
		}

			
		String sVelocity = String.format("%.1f", velocity);
		
		return TextUtil.trans("techguns.gun.tooltip.velocity")+": "+prefix+sVelocity+suffix;
	
	}
		
	protected void addMiningTooltip(ItemStack stack, World world, List<String> list, ITooltipFlag flagIn, boolean longTooltip) {}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> list, ITooltipFlag flagIn) {
//		super.addInformation(stack, worldIn, list, flagIn);
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
			List<IModifyInformationData> jointList = ModsJointRegistrar.getJointList(IModifyInformationData.class);
			list.add(TextUtil.trans("techguns.gun.tooltip.gunLevel")+": "+switchGunLevelColor(this.gunLevel)+switchLevelDescribe(this.gunLevel));
			list.add(TextUtil.trans("techguns.gun.tooltip.handtype")+": "+this.getGunHandType().toString());
			
			ItemStack[] ammo = this.ammoType.getAmmo(this.getCurrentAmmoVariant(stack));
			for(int i=0;i< ammo.length;i++) {
				list.add(TextUtil.trans("techguns.gun.tooltip.ammo")+": "+(this.ammoCount>1 ? this.ammoCount+"x " : "")+ChatFormatting.WHITE+TextUtil.trans(ammo[i].getUnlocalizedName()+".name"));
			}
			this.addMiningTooltip(stack, worldIn, list, flagIn, true);
			list.add(TextUtil.trans("techguns.gun.tooltip.damageType")+": "+this.getDamageType(stack).toString());
			list.add(TextUtil.trans("techguns.gun.tooltip.damage")+(this.shotgun ? ("(x"+ (getBulletCount(stack))+")") : "" )+": "+getTooltipTextDmg(stack,worldIn,true,jointList,"techguns.gun.tooltip.damage"));
			if (this.regenerationBulletGun){
				list.add(TextUtil.trans("techguns.gun.tooltip.extraCatapultBulletIncrease")+": "+getCatapultBulletIncrease(stack));
			}
			list.add(TextUtil.trans("techguns.gun.tooltip.extraCriticalHitRate")+": " + getExtraCriticalHitRateString(stack,worldIn,jointList,"techguns.gun.tooltip.extraCriticalHitRate"));
			list.add(TextUtil.trans("techguns.gun.tooltip.extraCriticalHitDamageRate")+": " + getExtraCriticalHitDamageRateString(stack,worldIn));

			//list.add(TextUtil.trans("techguns.gun.tooltip.range")+": "+this.damageDropStart+","+this.damageDropEnd+","+this.ticksToLive);
			list.add(getTooltipTextRange(stack));
			//list.add(TextUtil.trans("techguns.gun.tooltip.velocity")+": "+this.speed);
			list.add(getTooltipTextVelocity(stack,worldIn,jointList));
			list.add(TextUtil.trans("techguns.gun.tooltip.spread")+": "+ getSpread(stack));
			list.add(TextUtil.trans("techguns.gun.tooltip.clipsize")+": "+this.clipsize);
			list.add(TextUtil.trans("techguns.gun.tooltip.reloadTime")+": "+ getReloadTime(stack));
			list.add(TextUtil.trans("techguns.gun.tooltip.armorPen")+": "+ getTooltipTextPenetrate(stack,worldIn,jointList,"techguns.gun.tooltip.armorPen"));
			if (this.canZoom) {
				list.add(TextUtil.trans("techguns.gun.tooltip.zoom")+":"+(this.toggleZoom ? "("+TextUtil.trans("techguns.gun.tooltip.zoom.toogle")+")":"("+TextUtil.trans("techguns.gun.tooltip.zoom.hold")+")")+" "+TextUtil.trans("techguns.gun.tooltip.zoom.multiplier")+":"+this.zoomMult);
			}
			list.add(TextUtil.trans("techguns.gun.tooltip.durable")+": "+ getDurable(stack));
			List<IAddInformation> addInformationList = ModsJointRegistrar.getJointList(IAddInformation.class);
			addInformationList.forEach(item->item.add(stack,worldIn,list,flagIn));
		} else {
			ItemStack[] ammo = this.ammoType.getAmmo(this.getCurrentAmmoVariant(stack));
			List<IModifyInformationData> jointList = ModsJointRegistrar.getJointList(IModifyInformationData.class);
			for(int i=0;i< ammo.length;i++) {
				list.add(TextUtil.trans("techguns.gun.tooltip.ammo")+": "+(this.ammoCount>1 ? this.ammoCount+"x " : "")+ChatFormatting.WHITE+TextUtil.trans(ammo[i].getUnlocalizedName()+".name"));
			}
			this.addMiningTooltip(stack, worldIn, list, flagIn, false);
			list.add(TextUtil.trans("techguns.gun.tooltip.damage")+(this.shotgun ? ("(x"+ (getBulletCount(stack))+")") : "" )+": "+getTooltipTextDmg(stack,worldIn,false,jointList,"techguns.gun.tooltip.damage"));
			list.add(TextUtil.trans("techguns.gun.tooltip.shift1")+" "+ChatFormatting.GREEN+TextUtil.trans("techguns.gun.tooltip.shift2")+" "+ChatFormatting.GRAY+TextUtil.trans("techguns.gun.tooltip.shift3"));
			list.add(TextUtil.trans("techguns.gun.tooltip.durable")+": "+ getDurable(stack));
			List<IAddInformation> addInformationList = ModsJointRegistrar.getJointList(IAddInformation.class);
			addInformationList.forEach(item->item.add(stack,worldIn,list,flagIn));
		}
		//} else {
		//	list.add("Sneak to view stats");
		//}
	}

	private String switchGunLevelColor(int gunLevel){
		switch (gunLevel){
			case 1: return "§f";
			case 2: return "§2";
			case 3: return "§3";
			case 4: return "§d";
			case 5: return "§6";
			default:return "§f";
		}
	}

	private String getReloadTime(ItemStack itemStack){
		EnchantmentAndLevelBean<GunsAcceleratedReloading> enchantment = ItemUtil.findEnchantment(itemStack, GunsAcceleratedReloading.class);
		if (enchantment!=null){
			float rate =  enchantment.getLevel() * GunsAcceleratedReloading.REDUCE_RELOAD_TIME_EVERY_LEVEL;
			return ChatFormatting.GREEN + "" +
					String.format("%.1f",(this.reloadtime - this.reloadtime * rate)*0.05f)
					+ "s" + "(" + "-" + String.format("%.1f",rate*100) +"%)";
		}

		return this.reloadtime * 0.05f+"s";
	}

	private String getBulletCount(ItemStack itemStack){
		EnchantmentAndLevelBean<ShootGunBulletIncrease> enchantment = ItemUtil.findEnchantment(itemStack,ShootGunBulletIncrease.class);
		if (enchantment!=null){
			return "" + ChatFormatting.GREEN + (this.bulletcount+1+enchantment.getLevel()) + ChatFormatting.WHITE;
		}
		return "" + (this.bulletcount+1);
	}

	private String getCatapultBulletIncrease(ItemStack itemStack){
		EnchantmentAndLevelBean<ExtraCatapultBulletIncrease> enchantment = ItemUtil.findEnchantment(itemStack, ExtraCatapultBulletIncrease.class);
		int extraCount = 0;
		if (enchantment!=null){
			extraCount = ExtraCatapultBulletIncrease.BULLET_INCREASE_EVERY_LEVEL * enchantment.getLevel();
		}
		if (extraCount>0){
			return ""+ (this.regenerationCount+extraCount) + ChatFormatting.GREEN+ "(+" + extraCount +")";
		}
		return ""+this.regenerationCount;
	}

	private String getExtraCriticalHitRateString(ItemStack itemStack, World worldIn ,List<IModifyInformationData> jointList,String informationName){
		float extraCriticalHitRate=0;
		EnchantmentAndLevelBean<GunsCriticalHitRate> enchantment = ItemUtil.findEnchantment(itemStack, GunsCriticalHitRate.class);
		if (enchantment!=null){
			extraCriticalHitRate = enchantment.getEnchantment().extraCriticalHitRate() * enchantment.getLevel();
		}
		for (IModifyInformationData item : jointList) {
			extraCriticalHitRate = item.modify(itemStack, worldIn ,extraCriticalHitRate,informationName);
		}
		if (extraCriticalHitRate>0){
			return ChatFormatting.GREEN + String.format("%.2f",extraCriticalHitRate*100) +"%";
		}
		if(extraCriticalHitRate<0){
			return ChatFormatting.RED + String.format("%.2f",extraCriticalHitRate*100) +"%";
		}
		return String.format("%.2f",extraCriticalHitRate*100) +"%";
	}

	private String getExtraCriticalHitDamageRateString(ItemStack itemStack,World worldIn){
		EnchantmentAndLevelBean<NoThinkShoot> noThinkShoot = ItemUtil.findEnchantment(itemStack, NoThinkShoot.class);
		if (noThinkShoot != null){
			int criticalHitRateEnchantmentLevel = 0;
			EnchantmentAndLevelBean<GunsCriticalHitRate> criticalHitRateEnchantment = ItemUtil.findEnchantment(itemStack, GunsCriticalHitRate.class);
			if (criticalHitRateEnchantment != null){
				criticalHitRateEnchantmentLevel = criticalHitRateEnchantment.getLevel();
			}
			float extraByJoint = 0;
			List<IExtraAttributeProgress> jointList = ModsJointRegistrar.getJointList(IExtraAttributeProgress.class);
			for (IExtraAttributeProgress item : jointList) {
				extraByJoint += item.progress(worldIn,itemStack,"CriticalRate");
			}
			float min = 100 +  450 * (criticalHitRateEnchantmentLevel * criticalHitRateEnchantment.getEnchantment().extraCriticalHitRate() + extraByJoint);
			float max = 100 +  850 * (criticalHitRateEnchantmentLevel * criticalHitRateEnchantment.getEnchantment().extraCriticalHitRate() + extraByJoint);
			if (min == max){
				return ChatFormatting.GOLD + String.format("%.2f",min) + "%";
			}
			return ChatFormatting.GOLD + String.format("%.2f",min) + "%" + " - " + String.format("%.2f",max) +"%";
		}

		float aFloat = itemStack.getTagCompound().getFloat(GunsCriticalHitDamageIncreaseRate.TAG_NAME);
		if (aFloat>0){
			return ChatFormatting.GREEN + String.format("%.2f",aFloat*100) +"%";
		}
		return String.format("%.2f",aFloat*100) +"%";
	}

	private String switchLevelDescribe(int gunLevel){
		return TextUtil.trans("techguns.gun.tooltip.gunLevel"+gunLevel);
	}

	public DamageType getDamageType(ItemStack stack) {
		return this.projectile_selector.getFactoryForType(this.getCurrentAmmoVariantKey(stack)).getDamageType();
	}

	@Override
	public int getCamoCount() {
		return this.camoCount;
	}

	@Override
	public String getCurrentCamoName(ItemStack item) {
		NBTTagCompound tags = item.getTagCompound();
		byte camoID=0;
		if (tags!=null && tags.hasKey("camo")){
			camoID=tags.getByte("camo");
		}
		if(camoID>0){
			return TextUtil.trans(this.getUnlocalizedName()+".camoname."+camoID);
		} else {
			return TextUtil.trans("techguns.item.defaultcamo");
		}
	}

	/**
	 * Should this weapon shoot with left click instead of mine, defaults to true
	 * @param shootWithLeftClick
	 */
	public GenericGun setShootWithLeftClick(boolean shootWithLeftClick) {
		this.shootWithLeftClick = shootWithLeftClick;
		return this;
	}
	
	public GenericGun setMiningAmmoConsumption(int ammo) {
		this.miningAmmoConsumption = ammo;
		return this;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity targetEntity) {
		
		if (this.shootWithLeftClick) {
			return true;
		} else {
			if (player.world.isRemote) {

				int time = (int) (((float) this.recoiltime / 20.0f) * 1000);
				//ClientProxy.get().setplayerRecoiltime(player, System.currentTimeMillis() + time, time, (byte) 0);
				ShooterValues.setRecoiltime(player, false, System.currentTimeMillis() + time, time, (byte) 0);
			}

			/**
			 * COPY FROM ENTITYPLAYER
			 */
			if (targetEntity.canBeAttackedWithItem() && GenericProjectile.BULLET_TARGETS.apply(targetEntity)) {
				if (!targetEntity.hitByEntity(player)) {
					float f = (float) player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
					float f1;

					if (targetEntity instanceof EntityLivingBase) {
						f1 = EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(), ((EntityLivingBase) targetEntity).getCreatureAttribute());
					} else {
						f1 = EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(), EnumCreatureAttribute.UNDEFINED);
					}

					float f2 = player.getCooledAttackStrength(0.5F);
					f = f * (0.2F + f2 * f2 * 0.8F);
					f1 = f1 * f2;
					player.resetCooldown();

					if (f > 0.0F || f1 > 0.0F) {
						boolean flag = f2 > 0.9F;
						boolean flag1 = false;
						int i = 0;
						i = i + EnchantmentHelper.getKnockbackModifier(player);

						if (player.isSprinting() && flag) {
							player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK,
									player.getSoundCategory(), 1.0F, 1.0F);
							++i;
							flag1 = true;
						}

						boolean flag2 = flag && player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater()
								&& !player.isPotionActive(MobEffects.BLINDNESS) && !player.isRiding() && targetEntity instanceof EntityLivingBase;
						flag2 = flag2 && !player.isSprinting();

						if (flag2) {
							f *= 1.5F;
						}

						f = f + f1;
						boolean flag3 = false;
						double d0 = (double) (player.distanceWalkedModified - player.prevDistanceWalkedModified);

						if (flag && !flag2 && !flag1 && player.onGround && d0 < (double) player.getAIMoveSpeed()) {
							ItemStack itemstack = player.getHeldItem(EnumHand.MAIN_HAND);

							flag3= this.hasSwordSweep() && this.getAmmoLeft(stack)>0; //if (itemstack.getItem() instanceof ItemSword) {
							//	flag3 = true;
							//}
						}

						float f4 = 0.0F;
						boolean flag4 = false;
						int j = EnchantmentHelper.getFireAspectModifier(player);

						if (targetEntity instanceof EntityLivingBase) {
							f4 = ((EntityLivingBase) targetEntity).getHealth();

							if (j > 0 && !targetEntity.isBurning()) {
								flag4 = true;
								targetEntity.setFire(1);
							}
						}

						double d1 = targetEntity.motionX;
						double d2 = targetEntity.motionY;
						double d3 = targetEntity.motionZ;
						
						//EDIT: return parameter workaround for attackEntityFrom
						TGDamageSource src = getMeleeDamageSource(player,stack);
						targetEntity.attackEntityFrom(src, f);
						boolean flag5 = src.wasSuccessful();

						if (flag5) {
							this.consumeAmmoOnMeleeHit(player, stack);
							
							if (i > 0) {
								if (targetEntity instanceof EntityLivingBase) {
									((EntityLivingBase) targetEntity).knockBack(player, (float) i * 0.5F, (double) MathHelper.sin(player.rotationYaw * 0.017453292F),
											(double) (-MathHelper.cos(player.rotationYaw * 0.017453292F)));
								} else {
									targetEntity.addVelocity((double) (-MathHelper.sin(player.rotationYaw * 0.017453292F) * (float) i * 0.5F), 0.1D,
											(double) (MathHelper.cos(player.rotationYaw * 0.017453292F) * (float) i * 0.5F));
								}

								player.motionX *= 0.6D;
								player.motionZ *= 0.6D;
								player.setSprinting(false);
							}

							if (flag3) {
								float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * f;

								for (EntityLivingBase entitylivingbase : player.world.getEntitiesWithinAABB(EntityLivingBase.class,
										targetEntity.getEntityBoundingBox().grow(1.0D, 0.25D, 1.0D))) {
									if (entitylivingbase != player && entitylivingbase != targetEntity && !player.isOnSameTeam(entitylivingbase)
											&& player.getDistanceSq(entitylivingbase) < 9.0D) {
										entitylivingbase.knockBack(player, 0.4F, (double) MathHelper.sin(player.rotationYaw * 0.017453292F),
												(double) (-MathHelper.cos(player.rotationYaw * 0.017453292F)));
										TGDamageSource dmgsrc = getMeleeDamageSource(player,stack);
										entitylivingbase.attackEntityFrom(dmgsrc, f3);
										if(dmgsrc.wasSuccessful()) {
											this.onMeleeHitTarget(stack, entitylivingbase);
										}
									}
								}

								/*player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,
										player.getSoundCategory(), 1.0F, 1.0F);
								player.spawnSweepParticles();*/
								this.doSweepAttackEffect(player);
							}

							/**
							 * Extra hit effect 
							 */
							this.onMeleeHitTarget(stack, targetEntity);
							
							if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged) {
								((EntityPlayerMP) targetEntity).connection.sendPacket(new SPacketEntityVelocity(targetEntity));
								targetEntity.velocityChanged = false;
								targetEntity.motionX = d1;
								targetEntity.motionY = d2;
								targetEntity.motionZ = d3;
							}

							if (flag2) {
								player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, player.getSoundCategory(),
										1.0F, 1.0F);
								player.onCriticalHit(targetEntity);
							}

							if (!flag2 && !flag3) {
								if (flag) {
									player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG,
											player.getSoundCategory(), 1.0F, 1.0F);
								} else {
									player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK,
											player.getSoundCategory(), 1.0F, 1.0F);
								}
							}

							if (f1 > 0.0F) {
								player.onEnchantmentCritical(targetEntity);
							}

							player.setLastAttackedEntity(targetEntity);

							if (targetEntity instanceof EntityLivingBase) {
								EnchantmentHelper.applyThornEnchantments((EntityLivingBase) targetEntity, player);
							}

							EnchantmentHelper.applyArthropodEnchantments(player, targetEntity);
							ItemStack itemstack1 = player.getHeldItemMainhand();
							Entity entity = targetEntity;

							if (targetEntity instanceof MultiPartEntityPart) {
								IEntityMultiPart ientitymultipart = ((MultiPartEntityPart) targetEntity).parent;

								if (ientitymultipart instanceof EntityLivingBase) {
									entity = (EntityLivingBase) ientitymultipart;
								}
							}

							if (!itemstack1.isEmpty() && entity instanceof EntityLivingBase) {
								ItemStack beforeHitCopy = itemstack1.copy();
								itemstack1.hitEntity((EntityLivingBase) entity, player);

								if (itemstack1.isEmpty()) {
									net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, beforeHitCopy, EnumHand.MAIN_HAND);
									player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
								}
							}

							if (targetEntity instanceof EntityLivingBase) {
								float f5 = f4 - ((EntityLivingBase) targetEntity).getHealth();
								player.addStat(StatList.DAMAGE_DEALT, Math.round(f5 * 10.0F));

								if (j > 0) {
									targetEntity.setFire(j * 4);
								}

								if (player.world instanceof WorldServer && f5 > 2.0F) {
									int k = (int) ((double) f5 * 0.5D);
									((WorldServer) player.world).spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, targetEntity.posX,
											targetEntity.posY + (double) (targetEntity.height * 0.5F), targetEntity.posZ, k, 0.1D, 0.0D, 0.1D, 0.2D);
								}
							}

							player.addExhaustion(0.1F);
						} else {
							player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, player.getSoundCategory(),
									1.0F, 1.0F);

							if (flag4) {
								targetEntity.extinguish();
							}
						}
					}
				}
			}

			return true;
		}
	}
	
	protected void onMeleeHitTarget(ItemStack stack, Entity target) {
	}
	
	protected void consumeAmmoOnMeleeHit(EntityLivingBase elb, ItemStack stack) {
		if(elb instanceof EntityPlayer) {
			EntityPlayer ply = (EntityPlayer) elb;
			if(ply.capabilities.isCreativeMode) {
				return;
			}
		}
		this.useAmmo(stack, 1);
	}
	
	protected void doSweepAttackEffect(EntityPlayer player) {
        if(!player.world.isRemote) {
			double d0 = (double)(-MathHelper.sin(player.rotationYaw * 0.017453292F));
	        double d1 = (double)MathHelper.cos(player.rotationYaw * 0.017453292F);
        	double x = player.posX+d0;
        	double y = player.posY+player.height*0.8d;
        	double z = player.posZ+d1;
        	this.spawnSweepParticle(player.world, x, y, z, d0, 0, d1);
        	this.playSweepSoundEffect(player);
        }
	}

	protected void playSweepSoundEffect(EntityPlayer player) {
		player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,
				player.getSoundCategory(), 1.0F, 1.0F);
	}
	
	protected void spawnSweepParticle(World w, double x, double y, double z, double motionX, double motionY, double motionZ) {
		
	}
	
	/**
	 * Override in Subclass to define damagesource used for player melee attacks, only relevant if "shootWithLeftClick" is not set
	 * @return
	 */
	protected TGDamageSource getMeleeDamageSource(EntityPlayer player, ItemStack stack){
		TGDamageSource src = new TGDamageSource("player", player, player, DamageType.PHYSICAL, DeathType.GORE);
		return src;
	}
	
	protected boolean hasSwordSweep() {
		return true;
	}
	
	public int getMiningAmmoConsumption() {
		return miningAmmoConsumption;
	}

	@Override
	public boolean isModelBase(ItemStack stack) {
		return this.hasCustomTexture;
	}
	
	public EntityAIRangedAttack getAIAttack(IRangedAttackMob shooter) {
		return new EntityAIRangedAttack(shooter, 1.0D, this.AI_attackTime/3, this.AI_attackTime, this.AI_attackRange, this.AI_burstCount, this.AI_burstAttackTime);
	}
	
	 public AmmoType getAmmoType() {
		return ammoType;
	}

	public float getAI_attackRange() {
		return AI_attackRange;
	}
	 
	public boolean isFullyLoaded(ItemStack stack){
		return this.clipsize == this.getCurrentAmmo(stack);
	}
	
	public boolean hasRightClickAction() {
		return this.getGunHandType()==GunHandType.TWO_HANDED && this.canZoom;
	}
	
	/**
     * Weapon is used by NPC
     */
    public void fireWeaponFromNPC(EntityLivingBase shooter, float dmgscale, float accscale) {
    	
    	SoundUtil.playSoundOnEntityGunPosition(shooter.world, shooter ,firesound, SOUND_DISTANCE, 1.0F, false, false, TGSoundCategory.GUN_FIRE);
    	
    	
    	EnumBulletFirePos firePos = EnumBulletFirePos.RIGHT;
    	
    	if (shooter instanceof NPCTurret){
    		//dmgscale=1.0f;
    		//accscale=1.0f;
    		firePos=EnumBulletFirePos.CENTER;
    	}
    	/*} else {
	    	
	    	/*EnumDifficulty difficulty = shooter.world.getDifficulty();
	    	
	    	switch(difficulty){
	    		case EASY:
	    			dmg=0.5f;
	    			acc=1.3f;
	    			break;
	    		case NORMAL:
	    			dmg = 0.6f;
	    			acc = 1.15f;
	    			break;
	    		case HARD:
	    			dmg = 0.75f;
	    			acc = 1.0f;
	    			break;
	    		case PEACEFUL:
	    			dmg = 0.0f;
	    			acc = 1.3f;
	    			break;
	    	}*/
    	//}
    	
    	if (!shooter.world.isRemote){
    		this.shootGun(shooter.world, shooter, shooter.getHeldItemMainhand(), this.zoombonus*accscale,dmgscale,0, EnumHand.MAIN_HAND, firePos, null);

			ItemStack stack = shooter.getHeldItemMainhand();
			lossDurable(stack,0.8f,0.15f);
    	}

    }
    
    /**
     * Get all ammo and magazines the gun currently holds is retrievable
     * @param stack
     * @return
     */
    public List<ItemStack> getAmmoOnUnload(ItemStack stack){
    	List<ItemStack> items = new ArrayList<>();
    	
    	int ammo = this.getCurrentAmmo(stack);
    	
    	if(this.ammoCount>1 && this.getAmmoLeft(stack)>0) {
    		for(ItemStack s : this.getAmmoType().getBullet(this.getCurrentAmmoVariant(stack))) {
    			items.add(TGItems.newStack(s,this.getAmmoLeft(stack)));
    		}
    	} else {
    		if (!this.isFullyLoaded(stack)) {
    			int amount = this.ammoType.getEmptyMag().length;
    			
    			for(int i=0;i<amount;i++) {
			    	int bulletsBack = (int) Math.floor(ammo/this.ammoType.getShotsPerBullet(clipsize, ammo));
					if (bulletsBack>0){
						items.add(TGItems.newStack(this.getAmmoType().getBullet(this.getCurrentAmmoVariant(stack))[i],bulletsBack));
					}
			    	if(!this.ammoType.getEmptyMag()[i].isEmpty()) {
			    		items.add(TGItems.newStack(this.ammoType.getEmptyMag()[i], 1));
			    	}
    			}
    		} else {
    			int amount = this.ammoType.getEmptyMag().length;	
    			for(int i=0;i<amount;i++) {
    				items.add(TGItems.newStack(this.ammoType.getAmmo(this.getCurrentAmmoVariant(stack))[i], 1));
    			}
    		}
    	}

    	return items;
    }
    
    /**
	 * try to force reload the gun, might lose some ammo
	 */
	public void tryForcedReload(ItemStack item, World world,EntityPlayer player, EnumHand hand){
		TGExtendedPlayer extendedPlayer = TGExtendedPlayer.get(player);
		
		if (extendedPlayer.getFireDelay(hand)<=0 && !this.isFullyLoaded(item)){
			
			int oldAmmo = this.getCurrentAmmo(item);
			
			//look for ammo
			if (InventoryUtil.consumeAmmoPlayer(player,this.getReloadItem(item))) {
			
				//empty gun and do reload if we can't put in ammo individual
				if (ammoCount <= 1){
					this.useAmmo(item, oldAmmo);
				}
				
				int ammos = this.getAmmoType().getEmptyMag().length;
				for (int i=0;i<ammos;i++) {
					if (!this.ammoType.getEmptyMag()[i].isEmpty()){
						
						int amount=InventoryUtil.addAmmoToPlayerInventory(player, TGItems.newStack(this.ammoType.getEmptyMag()[i], 1));
						if(amount >0 && !world.isRemote){
							player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, TGItems.newStack(this.ammoType.getEmptyMag()[i], amount)));
						}
						
						int bulletsBack = (int) Math.floor(oldAmmo/this.ammoType.getShotsPerBullet(clipsize, oldAmmo));
						if (bulletsBack>0){
							int amount2=InventoryUtil.addAmmoToPlayerInventory(player, TGItems.newStack(this.ammoType.getBullet(this.getCurrentAmmoVariant(item))[i], bulletsBack));
							if(amount2 >0 && !world.isRemote){
								player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, TGItems.newStack(this.ammoType.getBullet(this.getCurrentAmmoVariant(item))[i], amount2)));
							}
						}
						
					}
				}
				
				//stop toggle zooming when reloading
				if (world.isRemote) {
					if (canZoom  && this.toggleZoom) {
						ClientProxy cp = ClientProxy.get();
		    			if (cp.player_zoom != 1.0f) {
		    				cp.player_zoom= 1.0f;
							ZoomCacheDataBean zoomCacheDataBean = PlayerDataCache.PLAYER_ZOOM_DATA.get(player.getUniqueID());
							if (zoomCacheDataBean!=null){
								PlayerDataCache.PLAYER_ZOOM_DATA.remove(player.getUniqueID());
								FovTask.fovChange(zoomCacheDataBean.getTarget(),zoomCacheDataBean.getOrigin());
							}
		    			}
		    		}
				}
				
				extendedPlayer.setFireDelay(hand, this.reloadtime-this.minFiretime);
    			//System.out.println(Thread.currentThread().toString()+": reloadtime:"+reloadtime);
						    			
    			if (ammoCount >1) {
    				int i =1;
    				while (i<(ammoCount-oldAmmo) && InventoryUtil.consumeAmmoPlayer(player,this.ammoType.getAmmo(this.getCurrentAmmoVariant(item)))){
    					i++;
    				}
    				
    				//item.setItemDamage(ammoCount-i);
    				this.reloadAmmo(item, i);
    			} else {
    				this.reloadAmmo(item);
    			}
    			SoundUtil.playReloadSoundOnEntity(world,player,reloadsound, 1.0F, 1.0F, false, true, TGSoundCategory.RELOAD);

				if (world.isRemote) {

					int time = (int) (((float)reloadtime/20.0f)*1000);
					
					ShooterValues.setReloadtime(player, hand==EnumHand.OFF_HAND, System.currentTimeMillis()+time, time, (byte)0);
					
					client_startReload();
				} else{
					//send reloadpacket
					//send pakets to clients
					
			    	int msg_reloadtime = ((int)(((float)reloadtime/20.0f)*1000.0f));
			    	TGPackets.network.sendToAllAround(new ReloadStartedMessage(player,hand, msg_reloadtime,0), new TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 100.0f));
			    	//
				}

			} else {

				//TODO: "can't reload" sound
				/*if (!world.isRemote)
		        {
    				world.playSoundAtEntity(player, "mob.villager.idle", 1.0F, 1.0F );
		        }*/
			}

			
		}
		
	}

	public int getClipsize() {
		return clipsize;
	}
	
	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return !(this.handType == GunHandType.TWO_HANDED);
	}

	public boolean canClickBlock(World world, EntityPlayer player, EnumHand hand) {
		 //ItemStack itemstack = player.getHeldItem(hand);
	     RayTraceResult raytraceresult = this.rayTrace(world, player, false);
	    
	     if (raytraceresult==null || raytraceresult.typeOfHit!=RayTraceResult.Type.BLOCK) {
	    	 return false;
	     }
	    
	     return true;
	}
	
	public boolean setGunStat(EnumGunStat stat, float value) {
		switch(stat) {
		case DAMAGE:
			this.damage=value;
			return true;
		case DAMAGE_MIN:
			this.damageMin=value;
			return true;
		case DAMAGE_DROP_START:
			this.damageDropStart=value;
			return true;
		case DAMAGE_DROP_END:
			this.damageDropEnd=value;
			return true;
		case BULLET_SPEED:
			this.speed=value;
			return true;
		case BULLET_DISTANCE:
			this.ticksToLive= (int)value;
			return true;
		case GRAVITY:
			this.gravity=value;
			return true;
		case SPREAD:
			this.spread=value;
			return true;
		default:
			return false;
		}
	}

	public float getSpread() {
		return spread;
	}

	public GunHandType getHandType() {
		return handType;
	}

	public float getZoombonus() {
		return zoombonus;
	}

	public EnumCrosshairStyle getCrossHairStyle() {
		return this.crossHairStyle;
	}
	
	public GenericGun setCrossHair(EnumCrosshairStyle crosshair) {
		this.crossHairStyle = crosshair;
		return this;
	}

	@Override
	public int getLevel() {
		return this.gunLevel;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean hasEffect(ItemStack stack) {
		// 当物品有附魔时显示发光效果
		return !(stack.getEnchantmentTagList().hasNoTags());
	}

	@Override
	public int getItemEnchantability() {
		// 设置附魔能力值
		return 10;
	}

	@Override
	public void setDamage(ItemStack itemStack, int damage) {
		// 保持最低耐久为5%, 确保枪械无法损坏
		int dmg = damage;
		if(dmg > this.getMaxDamage(itemStack) * 0.95f){
			dmg = Math.round(this.getMaxDamage(itemStack) * 0.95f);
		}
		super.setDamage(itemStack,dmg);
		NBTTagCompound tagCompound = itemStack.getTagCompound();
		if (tagCompound == null){
			tagCompound = new NBTTagCompound();
		}
		int tagDmg = tagCompound.getInteger("Damage");
		//This is to balance the durability of external destructive tools. Some creatures or debuffs may instantly inflict durability damage, in which case the firearm durability tag needs to be updated synchronously
		if (this.getDamage(itemStack) > tagDmg){
			tagCompound.setInteger("Damage",tagDmg + damage);
		}
	}

	@Override
	public boolean getIsRepairable(ItemStack stack, ItemStack material) {
		if (stack.getItem() instanceof GenericGun){
			GenericGun gun =  (GenericGun)stack.getItem();
			ToolMaterial gunMaterial = gun.getMaterial();
			if (gunMaterial.getRepairItem() == material.getItem()){
				return true;
			}
		}
		return false;
	}

	@Override
	public GenericGun setMaxDamage(int maxDamage) {
		maxDamage *= 1.25f;
		super.setMaxDamage(maxDamage);
		return this;
	}

	public void setItemDamage(ItemStack stack,int damage){
		NBTTagCompound tagCompound = stack.getTagCompound();
		if (tagCompound==null){
			tagCompound = new NBTTagCompound();
			stack.setTagCompound(tagCompound);
		}
		tagCompound.setInteger("Damage",damage);
	}

	private float getDamageRate(ItemStack stack){
		NBTTagCompound tagCompound = stack.getTagCompound();
		Integer itemDamage =  tagCompound.getInteger("Damage");
		if (itemDamage == null){
			itemDamage = 0;
		}
		int maxDamage = stack.getMaxDamage();
		return (float)itemDamage / (float)maxDamage;
	}

	public int getItemDamage(ItemStack stack){
	        NBTTagCompound tagCompound = stack.getTagCompound();
	        if (tagCompound == null){
		        return 0;
	        }
	        return tagCompound.getInteger("Damage");
	}


	private Integer lossDurable(ItemStack stack, float baseLossRate, float reductionRateEveryEnchantLevel){
		// 消耗枪械耐久
		float lossRate = baseLossRate;
		Map<Enchantment, Integer> itemStackEnchantments = EnchantmentHelper.getEnchantments(stack);
		int level=0;
		for (Map.Entry<Enchantment, Integer> item : itemStackEnchantments.entrySet()) {
			if (item.getKey().equals(Enchantments.UNBREAKING) || Enchantments.UNBREAKING.getClass().isAssignableFrom(item.getKey().getClass())){
				level += item.getValue();
			}
		}
		if (level > 0){
			// 每级耐久相关附魔减少 15% 的耐久损耗率
			lossRate -= reductionRateEveryEnchantLevel * level;
		}
		// 控制最小值, 5%
		if(lossRate<0.05){
			lossRate = 0.05f;
		}
		float ran = (float)(new Random().nextInt(100)+1) / 100f;
		if (lossRate >= ran){
			// 减少枪械耐久度
			int consuming = this.consumingDurabilityEveryShoot;
			for (Map.Entry<Enchantment, Integer> entry : itemStackEnchantments.entrySet()) {
				Enchantment enchan = entry.getKey();
				if (enchan instanceof GenericGunEnchantment){
					consuming = ((GenericGunEnchantment) enchan).beforeDurabilityLoss(stack,consuming);
				}
			}
			int nextTotalDamage = this.getItemDamage(stack) + consuming;
			if (nextTotalDamage > (int)(stack.getMaxDamage()*0.95f)){
				nextTotalDamage = (int)(stack.getMaxDamage()*0.95f);
			}
		        this.setItemDamage(stack,nextTotalDamage);
			// stack.setItemDamage(nextTotalDamage);
			return nextTotalDamage;
		}
		return null;
	}

}
