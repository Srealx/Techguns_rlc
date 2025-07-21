package techguns;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import techguns.api.guns.GunHandType;
import techguns.entities.projectiles.*;
import techguns.init.ITGInitializer;
import techguns.items.guns.Chainsaw;
import techguns.items.guns.ChargedProjectileSelector;
import techguns.items.guns.EnumCrosshairStyle;
import techguns.items.guns.GenericGrenade;
import techguns.items.guns.GenericGun;
import techguns.items.guns.GenericGunCharge;
import techguns.items.guns.GuidedMissileLauncher;
import techguns.items.guns.IProjectileFactory;
import techguns.items.guns.MiningDrill;
import techguns.items.guns.PowerHammer;
import techguns.items.guns.ProjectileSelector;
import techguns.items.guns.RangeTooltipType;
import techguns.items.guns.Shishkebap;
import techguns.items.guns.SonicShotgun;
import techguns.items.guns.ammo.AmmoTypes;
import techguns.tools.ItemJsonCreator;

public class TGuns implements ITGInitializer {

	private static final float RANGE_MELEE=3.0f;
	private static final float RANGE_CLOSE=10.0f;
	private static final float RANGE_SHORT=20.0f;
	private static final float RANGE_MEDIUM=33.5f;
	private static final float RANGE_FAR=75.0f;
	private static final float AS50_RANGE=85.0f;
	private static final float RANGE_VARY_FAR=95.0f;

	// 最孬武器的系数
	private static final float PENETRATION_LOW=0.05f;
	// 普通机枪
	private static final float PENETRATION_MED=0.1f;
	// ak等强力机枪的系数
	private static final float PENETRATION_HIGH=0.13f;
	// 强穿武器的穿透系数
	private static final float PENETRATION_SUPER=0.3f;
	// as 50穿透
	private static final float PENETRATION_AS50=0.25f;
	// 榴弹
	private static final float PENETRATION_GRENADE_LAUNCHER=0.3f;
	// 导弹的穿透系数
	private static final float PENETRATION_SMALL_ROCKET=0.35f;
	private static final float PENETRATION_ROCKET=0.4f;
	// 超高穿透系数
	private static final float PENETRATION_VERY_SUPER=0.4f;
	// 镭射武器穿透系数
	private static final float PENETRATION_LASER=0.37f;
	// 爆能穿透系数
	private static final float PENETRATION_BAONENG=0.4f;

	// 先进弹夹穿甲系数
	private static final float PENETRATION_ADVANCED=0.45f;
    // 4级武器穿透系数
	private static final float PENETRATION_MAX=0.4f;
	// 5级武器穿透系数
	private static final float PENETRATION_ULTRA=0.47f;
    // 高斯穿透系数
	private static final float PENETRATION_GAUSSRIFLE=0.55f;


	private static final int MAX_RANGE_PISTOL=40;
	private static final int MAX_RANGE_RIFLE=60;
	private static final int MAX_RANGE_RIFLE_LONG=75;
	private static final int MAX_RANGE_SNIPER=120;
	
	public static GenericGun handcannon;
	public static GenericGun m4;
	public static GenericGun thompson;
	public static GenericGun pistol;
	public static GenericGun lmg;
	public static GenericGun boltaction;
	public static GenericGun biogun;
	public static GenericGun rocketlauncher;
	public static GenericGun sawedoff;
	public static GenericGun flamethrower;
	public static GenericGun ak47;
	public static GenericGun minigun;
	public static GenericGrenade stielgranate;
	public static GenericGrenade fraggrenade;
	public static GenericGun combatshotgun;
	public static GenericGun revolver;
	public static GenericGun grimreaper;
	public static GenericGun pdw;
	public static GenericGun as50;
	public static GenericGun teslagun;
	public static GenericGun m4_infiltrator;
	public static GenericGun goldenrevolver;
	public static GenericGun pulserifle;
	public static GenericGun lasergun;
	public static GenericGun blasterrifle;
	public static GenericGun alienblaster;
	public static GenericGun netherblaster;
	public static GenericGun powerhammer;
	public static GenericGun grenadelauncher;
	public static GenericGun aug;
	public static GenericGun sonicshotgun;
	public static GenericGun chainsaw;
	public static GenericGun scatterbeamrifle;
	public static GenericGun nucleardeathray;
	public static GenericGun mac10;
	public static GenericGun mibgun;
	public static GenericGun vector;
	public static GenericGun scar;
	public static GenericGun gaussrifle;
	public static GenericGun guidedmissilelauncher;
	public static GenericGun miningdrill;
	public static GenericGun tfg;
	public static GenericGun shishkebap;
	public static GenericGun laserpistol;
	public static GenericGun goldenrevolverBoss;

	public static ProjectileSelector<StoneBulletProjectile> STONE_BULLET_PROJECTILES;
	public static ProjectileSelector<GenericProjectile> SHOTGUN_PROJECTILES;
	public static ProjectileSelector<GenericProjectile> PISTOL_PROJECTILES;
	public static ProjectileSelector<GenericProjectile> ASSAULTRIFLE_MAG_PROJECTILES;
	public static ProjectileSelector<GenericProjectile> SMG_MAG_PROJECTILES;
	public static ProjectileSelector<GenericProjectile> PISTOL_MAG_PROJECTILES;
	public static ProjectileSelector<GenericProjectile> LMG_MAG_PROJECTILES;
	public static ProjectileSelector<GenericProjectile> RIFLE_PROJECTILES;
	public static ProjectileSelector<GenericProjectile> SNIPER_MAG_PROJECTILES;
	public static ProjectileSelector<AdvancedBulletProjectile> ADVANCED_MAG_PROJECTILES;
	public static ProjectileSelector<GenericProjectile> MINIGUN_MAG_PROJECTILES;
	public static ProjectileSelector<BlasterProjectile> BLASTER_ENERGYCELL_PROJECTILES;
	
	public static ProjectileSelector<TeslaProjectile> TESLAGUN_PROJECTILES;
	public static ProjectileSelector<LaserProjectile> LASERGUN_PROJECTILES;
	public static ProjectileSelector<LaserProjectile> LASERPISTOL_PROJECTILES;
	public static ProjectileSelector<FlamethrowerProjectile> FLAMETHROWER_PROJECTILES;
	public static ProjectileSelector<AlienBlasterProjectile> ALIENBLASTER_PROJECTILES;
	public static ProjectileSelector<DeatomizerProjectile> DEATOMIZER_PROJECTILES;
	public static ProjectileSelector<CyberdemonBlasterProjectile> NETHERBLASTER_PROJECTILES;
	public static ProjectileSelector<GaussProjectile> GAUSS_PROJECTILES;
	public static ProjectileSelector<NDRProjectile> NDR_PROJECTILES;
	public static ProjectileSelector<Grenade40mmProjectile> GRENADE40MM_PROJECTILES;
	public static ProjectileSelector<RocketProjectile> ROCKET_PROJECTILES;
	public static ProjectileSelector<SonicShotgunProjectile> SONIC_SHOTGUN_PROJECTILES;
	public static ChargedProjectileSelector<PowerHammerProjectile> POWERHAMMER_PROJECTILES;
	public static ChargedProjectileSelector<BioGunProjectile> BIOGUN_PROJECTILES;
	public static ChargedProjectileSelector<ChainsawProjectile> CHAINSAW_PROJECTILES;
	public static ChargedProjectileSelector<GuidedMissileProjectile> GUIDED_MISSILE_PROJECTILES;
	public static ChargedProjectileSelector<TFGProjectile> TFG_PROJECTILES;
	
	public static void registerItems(RegistryEvent.Register<Item> event){
		IForgeRegistry<Item> reg = event.getRegistry();
	
		reg.register(handcannon);
		reg.register(sawedoff);
		reg.register(revolver);
		reg.register(goldenrevolver);
		reg.register(thompson);
		reg.register(ak47);
		reg.register(boltaction);
		reg.register(m4);
		reg.register(m4_infiltrator);
		reg.register(pistol);
		reg.register(combatshotgun);
		reg.register(mac10);
		reg.register(flamethrower);
		reg.register(rocketlauncher);
		reg.register(grimreaper);
		reg.register(grenadelauncher);
		reg.register(aug);
		reg.register(netherblaster);
		reg.register(biogun);
		reg.register(teslagun);
		reg.register(lmg);
		reg.register(minigun);
		reg.register(as50);
		reg.register(vector);
		reg.register(scar);
		reg.register(lasergun);
		reg.register(blasterrifle);
		reg.register(scatterbeamrifle);
		reg.register(sonicshotgun);
		reg.register(pdw);
		reg.register(pulserifle);
		reg.register(mibgun);
		reg.register(alienblaster);
		reg.register(powerhammer);
		reg.register(chainsaw);
		reg.register(nucleardeathray);
		reg.register(gaussrifle);
		reg.register(guidedmissilelauncher);
		reg.register(miningdrill);
		reg.register(tfg);
		reg.register(laserpistol);
		if(TGConfig.debug) { //FIXME remove debug
			reg.register(shishkebap);
		}
		reg.register(stielgranate);
		reg.register(fraggrenade);
		reg.register(goldenrevolverBoss);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {

		IProjectileFactory[] STONE_BULLET = { new StoneBulletProjectile.Factory(),
		new StoneBulletProjectileAnShan.Factory(),new StoneBulletProjectileShanChang.Factory(),new StoneBulletProjectileHuaGang.Factory(),
		new StoneBulletProjectileShaYan.Factory(),new StoneBulletProjectileHeiYaoShi.Factory(),new StoneBulletProjectileZuanShi.Factory()};

		IProjectileFactory<GenericProjectile> GENERIC_PROJECTILE = new GenericProjectile.Factory();
		IProjectileFactory<GenericProjectile> INCENDIARY_ROUNDS = new GenericProjectileIncendiary.Factory(false);
		IProjectileFactory<GenericProjectile> PENETRATE_PROJECTILE = new GenericProjectilePenetrate.Factory();
		IProjectileFactory<GenericProjectile> STRONG_PROJECTILE = new GenericProjectileStrong.Factory();

		IProjectileFactory[] PISTOL_BULLET = {GENERIC_PROJECTILE, INCENDIARY_ROUNDS, PENETRATE_PROJECTILE , STRONG_PROJECTILE};

		IProjectileFactory[] GENERIC_BULLET = {GENERIC_PROJECTILE, INCENDIARY_ROUNDS, PENETRATE_PROJECTILE};
		IProjectileFactory[] SNIPER_ROUNDS = {GENERIC_PROJECTILE, INCENDIARY_ROUNDS, new GenericProjectileExplosive.Factory(),PENETRATE_PROJECTILE};
		IProjectileFactory[] ADVANCED_BULLET = {new AdvancedBulletProjectile.Factory(),new AdvancedBulletProjectileIncendiary.Factory()};

		IProjectileFactory[] GAUSS_BULLET = {new GaussProjectile.Factory(),new GaussProjectileIncendiary.Factory()};

		SHOTGUN_PROJECTILES = new ProjectileSelector<GenericProjectile>(AmmoTypes.SHOTGUN_ROUNDS, GENERIC_BULLET);

		STONE_BULLET_PROJECTILES = new ProjectileSelector<StoneBulletProjectile>(AmmoTypes.STONE_BULLETS, STONE_BULLET);
		PISTOL_PROJECTILES = new ProjectileSelector<GenericProjectile>(AmmoTypes.PISTOL_ROUNDS, PISTOL_BULLET);
		ASSAULTRIFLE_MAG_PROJECTILES = new ProjectileSelector<GenericProjectile>(AmmoTypes.ASSAULT_RIFLE_MAGAZINE, GENERIC_BULLET);
		SMG_MAG_PROJECTILES = new ProjectileSelector<GenericProjectile>(AmmoTypes.SMG_MAGAZINE, GENERIC_BULLET);
		PISTOL_MAG_PROJECTILES = new ProjectileSelector<GenericProjectile>(AmmoTypes.PISTOL_MAGAZINE, GENERIC_BULLET);
		LMG_MAG_PROJECTILES = new ProjectileSelector<GenericProjectile>(AmmoTypes.LMG_MAGAZINE, GENERIC_BULLET);
		RIFLE_PROJECTILES = new ProjectileSelector<GenericProjectile>(AmmoTypes.RIFLE_ROUNDS, GENERIC_BULLET);
		SNIPER_MAG_PROJECTILES = new ProjectileSelector<GenericProjectile>(AmmoTypes.AS50_MAGAZINE, SNIPER_ROUNDS);
		ADVANCED_MAG_PROJECTILES = new ProjectileSelector<AdvancedBulletProjectile>(AmmoTypes.ADVANCED_MAGAZINE, ADVANCED_BULLET );
		MINIGUN_MAG_PROJECTILES = new ProjectileSelector<GenericProjectile>(AmmoTypes.MINIGUN_AMMO_DRUM, GENERIC_BULLET);
		BLASTER_ENERGYCELL_PROJECTILES = new ProjectileSelector<BlasterProjectile>(AmmoTypes.ENERGY_CELL, new BlasterProjectile.Factory());

		LASERGUN_PROJECTILES = new ProjectileSelector<LaserProjectile>(AmmoTypes.ENERGY_CELL, new LaserProjectile.Factory());
		LASERPISTOL_PROJECTILES = new ProjectileSelector<LaserProjectile>(AmmoTypes.REDSTONE_BATTERY, new LaserProjectile.Factory());
		TESLAGUN_PROJECTILES = new ProjectileSelector<TeslaProjectile>(AmmoTypes.ENERGY_CELL, new TeslaProjectile.Factory());
		FLAMETHROWER_PROJECTILES = new ProjectileSelector<FlamethrowerProjectile>(AmmoTypes.FUEL_TANK, new FlamethrowerProjectile.Factory());
		ALIENBLASTER_PROJECTILES =  new ProjectileSelector<AlienBlasterProjectile>(AmmoTypes.ENERGY_CELL, new AlienBlasterProjectile.Factory());
		DEATOMIZER_PROJECTILES = new ProjectileSelector<DeatomizerProjectile>(AmmoTypes.ENERGY_CELL, new DeatomizerProjectile.Factory());
		NETHERBLASTER_PROJECTILES = new ProjectileSelector<CyberdemonBlasterProjectile>(AmmoTypes.NETHER_CHARGE, new CyberdemonBlasterProjectile.Factory());
		GAUSS_PROJECTILES = new ProjectileSelector<GaussProjectile>(AmmoTypes.AMMO_GAUSS_RIFLE, GAUSS_BULLET);
		NDR_PROJECTILES = new ProjectileSelector<NDRProjectile>(AmmoTypes.NUCLEAR_POWER_CELL, new NDRProjectile.Factory());
		GRENADE40MM_PROJECTILES = new ProjectileSelector<Grenade40mmProjectile>(AmmoTypes.GRENADES_40MM, new Grenade40mmProjectile.Factory());
		ROCKET_PROJECTILES = new ProjectileSelector(AmmoTypes.ROCKETS, new RocketProjectile.Factory(), new RocketProjectileNuke.Factory(), new RocketProjectileHV.Factory());
		
		SONIC_SHOTGUN_PROJECTILES = new ProjectileSelector<SonicShotgunProjectile>(AmmoTypes.ENERGY_CELL, new SonicShotgunProjectile.Factory());
		
		POWERHAMMER_PROJECTILES = new ChargedProjectileSelector<PowerHammerProjectile>(AmmoTypes.COMPRESSED_AIR_TANK, new PowerHammerProjectile.Factory());
		BIOGUN_PROJECTILES = new ChargedProjectileSelector<BioGunProjectile>(AmmoTypes.BIO_TANK, new BioGunProjectile.Factory());
		CHAINSAW_PROJECTILES = new ChargedProjectileSelector<ChainsawProjectile>(AmmoTypes.FUEL_TANK, new ChainsawProjectile.Factory());
		GUIDED_MISSILE_PROJECTILES = new ChargedProjectileSelector(AmmoTypes.ROCKETS_NO_NUKES, new GuidedMissileProjectile.Factory(), new GuidedMissileProjectileHV.Factory());
		TFG_PROJECTILES = new ChargedProjectileSelector<TFGProjectile>(AmmoTypes.NUCLEAR_POWER_CELL, new TFGProjectile.Factory());
		// 手炮 dps:3
		handcannon = new GenericGun("handcannon", STONE_BULLET_PROJECTILES, true, 15,1,30, 4.5f, TGSounds.HANDGUN_FIRE, TGSounds.HANDGUN_RELOAD,25,0.035f).setBulletSpeed(1.2f).setGravity(0.015d).setDamageDrop(15, 25, 3.5f).setAIStats(12f, 60, 0, 0).setTexture("textures/guns/handgun").setRecoiltime(12).setCrossHair(EnumCrosshairStyle.QUAD_NO_CORNERS).setGunLevel(1).setMaintenanceItem(Item.getItemFromBlock(Blocks.STONE)).setConsumingDurabilityEveryShoot(10).setMaxDamage(1000);//.setMuzzleParticle(2,0.2f);
        // 左轮 dps: 7.5
		revolver = new GenericGun("revolver",PISTOL_PROJECTILES, true, 12, 6,25,4.5f, TGSounds.REVOLVER_FIRE, TGSounds.REVOLVER_RELOAD,MAX_RANGE_PISTOL,0.025f).setDamageDrop(20, 28, 3.5f).setBulletSpeed(2f).setRecoiltime(6).setAIStats(RANGE_SHORT, 144, 6, 20).setTexture("textures/guns/revolver").setHandType(GunHandType.ONE_HANDED).setGunLevel(1).setPenetration(PENETRATION_LOW).setConsumingDurabilityEveryShoot(4).setMaxDamage(1000); //.setMuzzleParticle(2,0,0.05f,-0.45f);
        // 黄金左轮 dps: 10
		goldenrevolver = new GenericGun("goldenrevolver",PISTOL_PROJECTILES, true, 12, 6,25,6f, TGSounds.REVOLVER_GOLDEN_FIRE, TGSounds.REVOLVER_RELOAD,45,0.015f).setDamageDrop(20, 28, 5f).setRecoiltime(8).setBulletSpeed(2.3f).setAIStats(RANGE_MEDIUM, 144, 6, 20).setTexture("textures/guns/goldenrevolver").setHandType(GunHandType.ONE_HANDED).setGunLevel(1).setPenetration(PENETRATION_MED).setMaterial(Item.ToolMaterial.GOLD).setMaintenanceItem(TGItems.MECHANICAL_PARTS_GOLD.getItem()).setConsumingDurabilityEveryShoot(4).setMaxDamage(1000);
        // 汤普森: dps: 22.5
		thompson = new GenericGun("thompson",SMG_MAG_PROJECTILES, false, 4, 26,30,4.5f, TGSounds.THOMPSON_FIRE, TGSounds.THOMSPON_RELOAD,MAX_RANGE_PISTOL,0.05f).setBulletSpeed(2f).setDamageDrop(15, 24, 3.5f).setAIStats(RANGE_SHORT, 40, 3, 4).setTexture("textures/guns/thompson").setMuzzleFlashTime(4).setTurretPosOffset(0, 0, 0.04f).setGunLevel(1).setPenetration(PENETRATION_LOW).setMaxDamage(900);//.setMuzzleParticle(1,0,0.05f,0);
        // 双管猎枪 dps: 22
		sawedoff = new GenericGun("sawedoff",SHOTGUN_PROJECTILES, true, 7, 2, 22, 2.5f, TGSounds.SAWEDOFF_FIRE, TGSounds.SAWEDOFF_RELOAD,10, 0.01f).setAmmoCount(2).setShotgunSpread(7,0.2f,false).setDamageDrop(1, 4, 2f).setAIStats(RANGE_CLOSE, 60, 2, 10).setTexture("textures/guns/sawedoff").setBulletSpeed(1.5f).setCrossHair(EnumCrosshairStyle.FOUR_PARTS).setGunLevel(1).setPenetration(PENETRATION_LOW).setConsumingDurabilityEveryShoot(6).setMaxDamage(1000); //.setMuzzleParticle(2,0.1f);
		// 手枪 dps: 18.4
		pistol = new GenericGun("pistol",PISTOL_MAG_PROJECTILES, true, 6, 18, 22, 5.5f, TGSounds.PISTOL_FIRE, TGSounds.PISTOL_RELOAD,MAX_RANGE_PISTOL, 0.025f).setBulletSpeed(2.0f).setTexture("textures/guns/pistol3").setDamageDrop(18, 25, 4.5f).setAIStats(RANGE_MEDIUM, 40, 3, 8).setHandType(GunHandType.ONE_HANDED)/*.setMuzzleParticle(1,0,0.05f,-0.45f)*/.setRecoiltime(3).setCrossHair(EnumCrosshairStyle.GUN_DYNAMIC).setPenetration(PENETRATION_MED).setGunLevel(2).setMaintenanceItem(TGItems.MECHANICAL_PARTS_STEEL.getItem()).setConsumingDurabilityEveryShoot(2).setMaxDamage(1080);
		// 拉栓式步枪 dps: 21.7
		boltaction = new GenericGun("boltaction", RIFLE_PROJECTILES, true, 18, 6, 35, 19.5f, TGSounds.BOLT_ACTION_FIRE, TGSounds.BOLT_ACTION_RELOAD,90,0.05f).setZoom(0.3f, true,0.125f,true).setBulletSpeed(3.8f).setRecoiltime(12).setRechamberSound(TGSounds.BOLT_ACTION_RECHAMBER).setDamageDrop(45, 75, 17.5f).setPenetration(0.18f).setAIStats(RANGE_FAR, 55, 0, 0).setTextures("textures/guns/boltactionrifle",3).setTurretPosOffset(0, 0, 0.14f).setGunLevel(2).isSnipeGun(TGSounds.AS50_FIRE,null,null).setKnockBackRate(0.5f).setMaintenanceItem(TGItems.MECHANICAL_PARTS_STEEL.getItem()).setConsumingDurabilityEveryShoot(6);//.setMuzzleParticle(1,0,0.05f,0.3f);
        // mac10 dps: 33.4
		mac10 = new GenericGun("mac10",SMG_MAG_PROJECTILES, false, 3, 24,24,5f, TGSounds.MAC10_FIRE, TGSounds.M4_RELOAD,MAX_RANGE_PISTOL,0.05f).setDamageDrop(15, 24, 3.5f).setAIStats(RANGE_SHORT, 35, 4, 3).setTexture("textures/guns/mac10texture").setRecoiltime(2).setMuzzleFlashTime(3).setBulletSpeed(1.75f).setHandType(GunHandType.ONE_POINT_FIVE_HANDED).setTurretPosOffset(0, 0, -0.07f).setGunLevel(2).setPenetration(PENETRATION_LOW).setMaintenanceItem(TGItems.MECHANICAL_PARTS_STEEL.getItem()).setMaxDamage(1000);
		// 电锯 dps: 35
		chainsaw = new Chainsaw("chainsaw", CHAINSAW_PROJECTILES, false, 4, 150, 65, 6.5f, TGSounds.CHAINSAW_LOOP, TGSounds.POWERHAMMER_RELOAD, 2, 0.0f,1f,1).setMeleeDmg(5.5f, 2.0f).setTool("axe", 3).setDigSpeed(14.0f).setTexture("textures/guns/chainsaw").setRecoiltime(5).setShootWithLeftClick(false).setFiresoundStart(TGSounds.CHAINSAW_LOOP_START).setMaxLoopDelay(10).setPenetration(0.3f).setAIStats(RANGE_MELEE, 14, 0, 0).setTurretPosOffset(0, -0.47f, -0.08f).setNoMuzzleLight().setCrossHair(EnumCrosshairStyle.VANILLA).setGunLevel(2).setMaintenanceItem(TGItems.MECHANICAL_PARTS_STEEL.getItem()); //.setTurretPosOffset(0, 0.50f, 0);
		// 生物枪 dps: 34
		biogun = new GenericGunCharge("biogun", BIOGUN_PROJECTILES, false, 5, 25,30,8.5f, TGSounds.BIOGUN_FIRE, TGSounds.BIOGUN_RELOAD, MAX_RANGE_PISTOL, 0.015f,30.0f,3).setChargeSound(TGSounds.BIOGUN_CHARGE).setChargeFX("biogunCharge",-0.12f, -0.07f, 0.27f).setBulletSpeed(0.75f).setGravity(0.01d).setPenetration(0.25f).setTexture("textures/guns/BioGun").setAIStats(15f, 30, 2, 5).setDamageDrop(8, 15, 6f).setMuzzleLight(0.2f, 0.9f, 0.5f).setForwardOffset(0.40f).setCrossHair(EnumCrosshairStyle.QUAD_NO_CORNERS).setGunLevel(2).setMaintenanceItem(TGItems.MECHANICAL_PARTS_OBSIDIAN_STEEL.getItem()).setConsumingDurabilityEveryShoot(2);
		// aug dps: 32.5
		aug = new GenericGun("aug", ASSAULTRIFLE_MAG_PROJECTILES, false, 4, 30,30,6.5f, TGSounds.AUG_FIRE, TGSounds.AUG_RELOAD, MAX_RANGE_RIFLE, 0.010f).setZoom(0.60f, true,0.5f,true).setBulletSpeed(2.8f).setDamageDrop(30, 45, 5.5f).setAIStats(45, 46, 4, 4).setTextures("textures/guns/AugTexture",5).setPenetration(PENETRATION_MED).setMuzzleFlashTime(4).setGunLevel(2).setMaintenanceItem(TGItems.MECHANICAL_PARTS_STEEL.getItem());
        // m4 dps:32.5
		m4 = new GenericGun("m4",ASSAULTRIFLE_MAG_PROJECTILES, false, 4, 30,30,6.5f, TGSounds.M4_FIRE, TGSounds.M4_RELOAD, MAX_RANGE_RIFLE, 0.015f).setBulletSpeed(2.8f).setZoom(0.75f, true,0.75f,false).setDamageDrop(25, 40, 5.5f).setAIStats(45, 46, 4, 4).setTextures("textures/guns/m4Texture", 4).setTurretPosOffset(0, 0, 0.08f)/*.setMuzzleParticle(1,0,0.025f,0)*/.setPenetration(PENETRATION_MED).setMuzzleFlashTime(4).setGunLevel(2).setMaintenanceItem(TGItems.MECHANICAL_PARTS_STEEL.getItem());
		// 战术霰弹 dps: 38
		combatshotgun = new GenericGun("combatshotgun",SHOTGUN_PROJECTILES, true, 14, 6, 28, 3.3f, TGSounds.COMBATSHOTGUN_FIRE, TGSounds.COMBATSHOTGUN_RELOAD,15, 0.01f).setAmmoCount(6).setShotgunSpread(7,0.15f,false).setRecoiltime(12).setBulletSpeed(1.6f).setRechamberSound(TGSounds.COMBATSHOTGUN_RECHAMBER).setDamageDrop(2, 5, 2.3f).setPenetration(PENETRATION_HIGH).setAIStats(RANGE_CLOSE,35,0,0).setTexture("textures/guns/combatShotgun").setCrossHair(EnumCrosshairStyle.FOUR_PARTS).setGunLevel(2).setMaintenanceItem(TGItems.MECHANICAL_PARTS_STEEL.getItem()).setConsumingDurabilityEveryShoot(5);
		// akm dps: 37.5
		ak47 = new GenericGun("ak47", ASSAULTRIFLE_MAG_PROJECTILES, false, 4, 30,30,7.5f, TGSounds.AK_FIRE, TGSounds.AK_RELOAD, MAX_RANGE_RIFLE, 0.030f).setDamageDrop(20, 30, 6.5f).setBulletSpeed(2.8f).setAIStats(RANGE_MEDIUM, 46, 4, 4).setTextures("textures/guns/ak47Texture",2).setMuzzleFlashTime(4).setTurretPosOffset(0, 0, 0.08f).setPenetration(PENETRATION_MED).setGunLevel(2).setMaintenanceItem(TGItems.MECHANICAL_PARTS_STEEL.getItem());//.setMuzzleParticle(1,0,0.05f,0.12f);
		//scar dps: 36
		scar = new GenericGun("scar", ASSAULTRIFLE_MAG_PROJECTILES, false, 4, 30,28,7.2f, TGSounds.SCAR_FIRE, TGSounds.SCAR_RELOAD, MAX_RANGE_RIFLE, 0.015f).setZoom(0.65f, true,0.5f,true).setDamageDrop(35, 60, 6.0f).setAIStats(45, 60, 5, 4).setTextures("textures/guns/scar_texture", 2).setBulletSpeed(2.8f).setMuzzleFlashTime(5).setTurretPosOffset(0, 0.02f, 0.09f).setGunLevel(2).setPenetration(0.15f).setMaintenanceItem(TGItems.MECHANICAL_PARTS_STEEL.getItem());
		// 维克托 dps:40
		vector = new GenericGun("vector",SMG_MAG_PROJECTILES, false, 3, 25,20,6f, TGSounds.VECTOR_FIRE, TGSounds.VECTOR_RELOAD,MAX_RANGE_PISTOL,0.05f).setZoom(0.75f, true,0.35f,false).setDamageDrop(17, 25, 4.5f).setBulletSpeed(2.3f).setAIStats(RANGE_SHORT, 45, 5, 3).setTextures("textures/guns/vector_texture",2).setRecoiltime(2).setMuzzleFlashTime(3).setTurretPosOffset(0, -0.1f, 0.15f).setGunLevel(2).setPenetration(PENETRATION_MED).setMaintenanceItem(TGItems.MECHANICAL_PARTS_STEEL.getItem()).setMaxDamage(1500);
		// 渗透者 dps:37.5
		m4_infiltrator = new GenericGun("m4_infiltrator", ASSAULTRIFLE_MAG_PROJECTILES, false, 4, 35,26,7.5f, TGSounds.M4_SILENCED_FIRE, TGSounds.M4_RELOAD, MAX_RANGE_RIFLE, 0.010f).setZoom(0.50f, true,0.5f,true).setDamageDrop(25, 35, 6.5f).setBulletSpeed(3f).setSilenced(true).setAIStats(50, 60, 5, 4).setTexture("textures/guns/m4_uq_texture").setPenetration(PENETRATION_HIGH).setTurretPosOffset(0, 0, 0.08f).setNoMuzzleLight().setGunLevel(2).setMaintenanceItem(TGItems.MECHANICAL_PARTS_STEEL.getItem()).setMaxDamage(1380);
		// 火箭发射器 16
		rocketlauncher = new GenericGun("rocketlauncher", ROCKET_PROJECTILES, true, 30, 1 , 30, 24.0f, TGSounds.ROCKET_FIRE, TGSounds.ROCKET_RELOAD, 200, 0.05f).setGravity(0.01D).setBulletSpeed(1.5f).setDamageDrop(3.0f,5.0f,14f).setRecoiltime(10).setAIStats(RANGE_MEDIUM,80,0,0).setTextures("textures/guns/rocketlauncher",2).setTurretPosOffset(0, 0, -0.1f).setRangeTooltipType(RangeTooltipType.RADIUS).setForwardOffset(0.35f).setCrossHair(EnumCrosshairStyle.QUAD_CORNERS_DOT).setGunLevel(2).setPenetration(PENETRATION_GRENADE_LAUNCHER).setMaintenanceItem(TGItems.MECHANICAL_PARTS_STEEL.getItem()).setConsumingDurabilityEveryShoot(15);
		// 轻机枪: dps:40
		lmg = new GenericGun("lmg",LMG_MAG_PROJECTILES,false, 3, 80,60,6f, TGSounds.LMG_FIRE, TGSounds.LMG_RELOAD, MAX_RANGE_RIFLE, 0.020f).setZoom(0.75f, true,0.75f,false).setDamageDrop(40, 60, 5f).setPenetration(PENETRATION_HIGH).setBulletSpeed(3f).setAIStats(40, 55, 6, 3).setTexture("textures/guns/mg2_texture").setMuzzleFlashTime(2).setRecoiltime(7).setGunLevel(2).setMaintenanceItem(TGItems.MECHANICAL_PARTS_STEEL.getItem()).setMaxDamage(1500);//.setMuzzleParticle(1,0,0.1f,0.02f);
        // 榴弹: dps: 28
		grenadelauncher = new GenericGun("grenadelauncher", GRENADE40MM_PROJECTILES, true, 10, 6, 50, 14f, TGSounds.GRENADE_LAUNCHER_FIRE, TGSounds.GRENADE_LAUNCHER_RELOAD, 160, 0.015f).setTexture("textures/guns/grenadelauncher").setBulletSpeed(0.5f).setAIStats(RANGE_MEDIUM, 75, 3, 20).setAmmoCount(6).setGravity(0.01d).setRangeTooltipType(RangeTooltipType.RADIUS).setCrossHair(EnumCrosshairStyle.QUAD_NO_CORNERS).setGunLevel(3).setPenetration(PENETRATION_GRENADE_LAUNCHER).setMaintenanceItem(TGItems.MECHANICAL_PARTS_STEEL.getItem()).setConsumingDurabilityEveryShoot(5);
		// 火焰喷射器 dps: 36
		flamethrower = new GenericGun("flamethrower", FLAMETHROWER_PROJECTILES, false, 5, 50, 45, 9f, TGSounds.FLAMETHROWER_FIRE, TGSounds.FLAMETHROWER_RELOAD,16,0.05f).setBulletSpeed(0.5f).setGravity(0.01d).setFiresoundStart(TGSounds.FLAMETHROWER_START).setMaxLoopDelay(10).setDamageDrop(4, 16, 8f).setAIStats(15f, 55, 4, 5).setTexture("textures/guns/flamethrower").setCheckRecoil().setRecoiltime(10).setCheckMuzzleFlash().setMuzzleFlashTime(10).setTurretPosOffset(0, 0, 0.1f).setForwardOffset(0.35f).setCrossHair(EnumCrosshairStyle.QUAD_NO_CORNERS).setGunLevel(3).setPenetration(PENETRATION_SUPER).setMaintenanceItem(TGItems.MECHANICAL_PARTS_OBSIDIAN_STEEL.getItem());
		// 镭射手枪: dps: 36
		laserpistol = new GenericGun("laserpistol", LASERPISTOL_PROJECTILES, false, 8, 18, 22, 14.5f, TGSounds.LASER_PISTOL_FIRE, TGSounds.LASER_PISTOL_RELOAD, 15, 0.025f).setBulletSpeed(30.0f).setAIStats(RANGE_MEDIUM, 70, 3, 16).setTexture("textures/guns/laser_pistol").setMuzzleLight(0.9f, 0.3f, 0.1f).setRangeTooltipType(RangeTooltipType.NO_DROP).setHandType(GunHandType.ONE_HANDED).setCrossHair(EnumCrosshairStyle.HORIZONTAL_TWO_PART).setGunLevel(3).setPenetration(PENETRATION_LASER).setMaintenanceItem(TGItems.PLATE_OBSIDIAN_STEEL.getItem()).setConsumingDurabilityEveryShoot(2); //.setTexture("textures/guns/laserGunNew");//
		// as50 dps: 40
		as50 = new GenericGun("as50", SNIPER_MAG_PROJECTILES, true, 20, 6, 35, 40f, TGSounds.AS50_FIRE, TGSounds.AS50_RELOAD, 120,0.06f).setDamageDrop(70, 100, 35.0f).setZoom(0.2f, true,0.1f,true).setBulletSpeed(5.5f).setRecoiltime(16).setPenetration(PENETRATION_AS50).setAIStats(AS50_RANGE, 58, 0, 0).setTexture("textures/guns/as50texture").setTurretPosOffset(0, 0.02f, 0.13f).setGunLevel(3).isSnipeGun(TGSounds.AS50_FIRE_CS,null,null).setKnockBackRate(0.85f).setMaintenanceItem(TGItems.MECHANICAL_PARTS_OBSIDIAN_STEEL.getItem()).setConsumingDurabilityEveryShoot(5);//.setMuzzleParticle(1,0,0.05f,0.3f);
		// 导弹发射器 dps: 23
		guidedmissilelauncher = new GuidedMissileLauncher("guidedmissilelauncher", GUIDED_MISSILE_PROJECTILES, true, 25, 1 , 28, 32.0f, TGSounds.GUIDEDMISSILE_FIRE, TGSounds.ROCKET_RELOAD, 100, 0.05f, 200, 1).setFireWhileCharging(true).setChargeFireAnims(false).setBulletSpeed(1.5f).setDamageDrop(4.0f,8.0f,18f).setRecoiltime(10).setAIStats(RANGE_MEDIUM,80,0,0).setTexture("textures/guns/guidedmissilelauncher").setLockOn(20, 80).setTurretPosOffset(0, 0.01f, -0.12f).setRangeTooltipType(RangeTooltipType.RADIUS).setCrossHair(EnumCrosshairStyle.QUAD_CORNERS_DOT).setGunLevel(3).setPenetration(PENETRATION_SMALL_ROCKET).setMaintenanceItem(TGItems.MECHANICAL_PARTS_OBSIDIAN_STEEL.getItem()).setConsumingDurabilityEveryShoot(12); //.setHandType(GunHandType.ONE_HANDED);/*.setTurretPosOffset(0f, -0.7f, -0.2f);*/
		// 镭射步枪 dps: 45
		lasergun = new GenericGun("lasergun", LASERGUN_PROJECTILES, false, 6, 35, 25, 13.5f, TGSounds.LASERGUN_FIRE, TGSounds.LASERGUN_RELOAD, 18, 0.0f).setZoom(0.75f, true,0.75f,false).setBulletSpeed(30.0f).setAIStats(RANGE_MEDIUM, 36, 2, 10).setTexture("textures/guns/lasergun").setTurretPosOffset(0, 0.01f, 0.11f).setMuzzleLight(0.9f, 0.3f, 0.1f).setRangeTooltipType(RangeTooltipType.NO_DROP).setCrossHair(EnumCrosshairStyle.HORIZONTAL_TWO_PART).setGunLevel(3).setPenetration(PENETRATION_LASER).setMaintenanceItem(TGItems.MECHANICAL_PARTS_OBSIDIAN_STEEL.getItem()).setMaxDamage(1050); //.setTexture("textures/guns/laserGunNew");//
		// 加特林 dps: 48
		minigun = new GenericGun("minigun",MINIGUN_MAG_PROJECTILES,false, 3, 150, 100, 7.2f, TGSounds.MINIGUN_FIRE, TGSounds.MINIGUN_RELOAD, MAX_RANGE_RIFLE, 0.025f).setDamageDrop(30, 50, 6.2f).setPenetration(0.18f).setBulletSpeed(3.5f).setAIStats(RANGE_MEDIUM, 80, 10, 3).setTexture("textures/guns/minigun")/*.setHandType(GunHandType.ONE_HANDED)*//*.setTurretPosOffset(0, 0.20f, 0)*/.setCheckRecoil().setMuzzleFlashTime(4).setCheckMuzzleFlash().setTurretPosOffset(0, -0.49f, -0.14f).setGunLevel(3).setRecoiltime(6).setMaintenanceItem(TGItems.MECHANICAL_PARTS_OBSIDIAN_STEEL.getItem()).setMaxDamage(1900);//.setRecoiltime(30).setFiresoundStart("techguns:guns.minigunStart").setMaxLoopDelay(10)
		// 特斯拉 dps: 38.75
		teslagun = new GenericGun("teslagun", TESLAGUN_PROJECTILES, false, 8, 20, 28, 15.5f, TGSounds.TESLA_FIRE, TGSounds.TESLA_RELOAD, 30/*TODO ?Teslagun.LIFETIME*/, 0.0f).setZoom(0.75f, true,1.0f,false).setBulletSpeed(18.0f/* TODO ??Lasergun.SPEED*/).setMuzzleFlashTime(10).setTexture("textures/guns/teslagun").setAIStats(RANGE_MEDIUM, 24, 0, 0).setMuzzleLight(0f, 0.8f, 1.0f).setCrossHair(EnumCrosshairStyle.HORIZONTAL_TWO_PART_E).setGunLevel(3).setPenetration(PENETRATION_LASER).setRegenerationBulletCount(2).setMaintenanceItem(TGItems.MECHANICAL_PARTS_CARBON.getItem()).setMaxDamage(800);
		// 爆能步枪 dps: 51.5
		blasterrifle = new GenericGun("blasterrifle", BLASTER_ENERGYCELL_PROJECTILES, false, 7, 40, 26, 18f, TGSounds.BLASTER_RIFLE_FIRE, TGSounds.LASERGUN_RELOAD, MAX_RANGE_RIFLE, 0.025f).setZoom(0.5f, true,0.75f,true).setAIStats(RANGE_MEDIUM, 85, 4, 10).setTexture("textures/guns/blasterrifle").setMuzzleLight(0.9f, 0.3f, 0.1f).setCrossHair(EnumCrosshairStyle.HORIZONTAL_TWO_PART).setGunLevel(3).setPenetration(PENETRATION_BAONENG).setMaintenanceItem(TGItems.MECHANICAL_PARTS_CARBON.getItem()).setMaxDamage(925);
		// 爆能霰弹 dps: 50
		scatterbeamrifle = new GenericGun("scatterbeamrifle", BLASTER_ENERGYCELL_PROJECTILES, false, 14, 8, 24, 5f, TGSounds.LASERGUN_FIRE, TGSounds.LASERGUN_RELOAD, 30/*TODO?Lasergun.LIFETIME*/, 0.1f).setShotgunSpread(6,0.15f,false).setBulletSpeed(1.5f).setZoom(0.75f, true,0.75f,false).setAIStats(RANGE_SHORT, 40, 0, 0).setTexture("textures/guns/lasergunnew").setMuzzleLight(0.9f, 0.3f, 0.1f).setCrossHair(EnumCrosshairStyle.FOUR_PARTS).setGunLevel(3).setPenetration(PENETRATION_BAONENG).setMaintenanceItem(TGItems.MECHANICAL_PARTS_CARBON.getItem()).setConsumingDurabilityEveryShoot(3);
        // 下界爆破 dps: 55
		netherblaster = new GenericGun("netherblaster", NETHERBLASTER_PROJECTILES, false, 10, 10, 28, 27.5f, TGSounds.NETHERBLASTER_FIRE, TGSounds.NETHERBLASTER_RELOAD, MAX_RANGE_RIFLE, 0.0f).setBulletSpeed(1.8f).setMuzzleFlashTime(10).setPenetration(PENETRATION_VERY_SUPER).setAIStats(45f, 60, 2, 15).setTexture("textures/guns/cyberdemonblaster").setDamageDrop(15, 30, 24.5f).setHandType(GunHandType.ONE_POINT_FIVE_HANDED).setTurretPosOffset(0, -0.16f, 0.12f).setMuzzleLight(0.9f, 0.8f, 0.1f).setCrossHair(EnumCrosshairStyle.FOUR_PARTS_SPIKED).setGunLevel(3).setMaintenanceItem(TGItems.CYBERNETIC_PARTS.getItem()).setMaxDamage(780);
		// 外星人手枪 dps: 45
		alienblaster = new GenericGun("alienblaster", ALIENBLASTER_PROJECTILES, false, 8, 20, 20, 18f, TGSounds.ALIENBLASTER_FIRE, TGSounds.ALIENBLASTER_RELOAD, MAX_RANGE_PISTOL, 0.0f).setBulletSpeed(1.0f).setMuzzleFlashTime(10).setAIStats(RANGE_MEDIUM, 24, 0, 0).setTexture("textures/guns/alien_blaster").setHandType(GunHandType.ONE_HANDED).setTurretPosOffset(0, -0.03f, -0.04f).setMuzzleLight(0.925f, 0.415f, 1f).setCrossHair(EnumCrosshairStyle.FOUR_PARTS_SPIKED).setGunLevel(3).setPenetration(PENETRATION_VERY_SUPER).setMaintenanceItem(TGItems.MECHANICAL_PARTS_CARBON.getItem()).setMaxDamage(1000);
		// 反原子手枪 dps: 48
		mibgun = new GenericGun("mibgun", DEATOMIZER_PROJECTILES, true, 10, 22, 20, 24f, TGSounds.MIBGUN_FIRE, TGSounds.MIBGUN_RELOAD, MAX_RANGE_PISTOL, 0.035f).setAIStats(RANGE_MEDIUM, 31, 0, 0).setTexture("textures/guns/mibgun").setDamageDrop(20, 30, 25.0f).setBulletSpeed(1.5f).setHandType(GunHandType.ONE_HANDED).setTurretPosOffset(0, -0.04f, 0f).setMuzzleLight(0.3333f, 0.9f, 1f).setCrossHair(EnumCrosshairStyle.HORIZONTAL_TWO_PART).setGunLevel(4).setPenetration(PENETRATION_VERY_SUPER).setMaintenanceItem(TGItems.MECHANICAL_PARTS_CARBON.getItem()).setMaxDamage(800);
		// pdw  pds: 63
		pdw = new GenericGun("pdw", ADVANCED_MAG_PROJECTILES, false, 3, 50, 28, 9.5f, TGSounds.PDW_FIRE, TGSounds.PDW_RELOAD,MAX_RANGE_RIFLE,0.03f).setPenetration(PENETRATION_ADVANCED)/*.setShotgunSpread(1, 0.03f,true)*/.setAIStats(RANGE_SHORT, 45, 5, 3).setTextures("textures/guns/pdw",3)./*setCheckRecoil().*/setHandType(GunHandType.ONE_POINT_FIVE_HANDED).setMuzzleFlashTime(2).setMuzzleLight(0f, 0.8f, 1.0f).setCrossHair(EnumCrosshairStyle.HORIZONTAL_TWO_PART).setGunLevel(4).setRegenerationBulletCount(1).setMaintenanceItem(TGItems.MECHANICAL_PARTS_TITANIUM.getItem()).setMaxDamage(2400);
		// 死神 dps: 70
		grimreaper = new GuidedMissileLauncher("grimreaper", GUIDED_MISSILE_PROJECTILES, false, 12, 4 , 80, 42.0f, TGSounds.GUIDEDMISSILE_FIRE, TGSounds.ROCKET_RELOAD, 200, 0.05f,100, 1).setFireWhileCharging(true).setChargeFireAnims(false).setBulletSpeed(1.8f).setRecoiltime(10).setDamageDrop(4.0f,8.0f,20f).setAmmoCount(4).setAIStats(RANGE_MEDIUM, 150, 4, 15).setTexture("textures/guns/grimreaper").setLockOn(20, 80).setTurretPosOffset(0, 0.11f, -0.16f).setRangeTooltipType(RangeTooltipType.RADIUS).setCrossHair(EnumCrosshairStyle.VANILLA).setPenetration(PENETRATION_ROCKET).setGunLevel(4).setMaintenanceItem(TGItems.MECHANICAL_PARTS_CARBON.getItem()).setConsumingDurabilityEveryShoot(9);
		// 脉冲步枪 dps: 67.5
		pulserifle = new GenericGun("pulserifle",ADVANCED_MAG_PROJECTILES, false, 8, 30, 35, 9f,  TGSounds.PULSE_RIFLE_FIRE, TGSounds.PULSE_RIFEL_RELOAD, MAX_RANGE_RIFLE_LONG,0.024f).setZoom(0.35f, true,0.5f,true).setBulletSpeed(3f).setRecoiltime(8).setPenetration(PENETRATION_ADVANCED).setShotgunSpread(2, 0.015f,true).setAIStats(RANGE_MEDIUM, 24, 0, 0).setTextures("textures/guns/pulserifle",3).setMuzzleFlashTime(4).setTurretPosOffset(0, 0, -0.09f).setMuzzleLight(0f, 0.8f, 1.0f).setCrossHair(EnumCrosshairStyle.HORIZONTAL_TWO_PART).setGunLevel(4).setRegenerationBulletCount(2).setMaintenanceItem(TGItems.MECHANICAL_PARTS_TITANIUM.getItem()).setMaxDamage(950);
        // 声波 dps: 45
		sonicshotgun = new SonicShotgun("sonicshotgun",SONIC_SHOTGUN_PROJECTILES,true, 20, 8, 35, 48.0f, TGSounds.SONIC_SHOTGUN_FIRE, TGSounds.SONIC_SHOTGUN_RELOAD,20,0.0f).setDamageDrop(5, 15, 30.0f).setPenetration(PENETRATION_MAX).setAIStats(RANGE_SHORT, 60, 0, 0).setTexture("textures/guns/sonicshotgun").setCrossHair(EnumCrosshairStyle.QUAD_NO_CORNERS).setGunLevel(4).setMaintenanceItem(TGItems.MECHANICAL_PARTS_TITANIUM.getItem()).setConsumingDurabilityEveryShoot(3);
		// 高斯 dps: 80
		gaussrifle = new GenericGun("gaussrifle", GAUSS_PROJECTILES, true, 20, 8, 40, 80.0f, TGSounds.GAUSS_RIFLE_FIRE, TGSounds.GAUSS_RIFLE_RELOAD, 150, 0.07f).setZoom(0.15f, true,0.0f,true).setBulletSpeed(7f).setAIStats(RANGE_VARY_FAR, 65, 0, 0).setRechamberSound(TGSounds.GAUSS_RIFLE_RECHAMBER).setRecoiltime(16).setTexture("textures/guns/gaussrifle").setTurretPosOffset(0, -0.02f, 0.12f).setMuzzleLight(0f, 0.8f, 1.0f).setForwardOffset(0.45f).setPenetration(PENETRATION_GAUSSRIFLE).setCrossHair(EnumCrosshairStyle.HORIZONTAL_TWO_PART_LARGE).setGunLevel(5).setMaintenanceItem(TGItems.MECHANICAL_PARTS_TITANIUM.getItem()).isSnipeGun(TGSounds.GAUSS_RIFLE_FIRE_CS,"gauss","GaussProjectileCs").setConsumingDurabilityEveryShoot(4);//
        // 核死光 dps: 75
		nucleardeathray = new GenericGun("nucleardeathray", NDR_PROJECTILES, false, 5, 50, 50, 1.875f, TGSounds.BEAMGUN_FIRE, TGSounds.LASERGUN_RELOAD, MAX_RANGE_SNIPER/* TODO? Beamgun.LIFETIME*/, 0.0f).setFiresoundStart(TGSounds.BEAMGUN_START).setMaxLoopDelay(10).setRecoiltime(10).setCheckRecoil().setBulletSpeed(60.0f).setAIStats(RANGE_MEDIUM, 60, 4, 5).setTexture("textures/guns/ndr").setPenetration(PENETRATION_ULTRA).setHandType(GunHandType.TWO_HANDED).setTurretPosOffset(0, 0.04f, -0.19f).setCrossHair(EnumCrosshairStyle.TRI).setGunLevel(5).setMaintenanceItem(TGItems.MECHANICAL_PARTS_TITANIUM.getItem()).setMaxDamage(1600);//.setCheckRecoil();
        // tfg dps: 70
		tfg = new GenericGunCharge("tfg", TFG_PROJECTILES, false, 6, 40,40,21f, TGSounds.TFG_FIRE, TGSounds.BIOGUN_RELOAD, 67, 0.015f, 60.0f ,10).setChargeSound(TGSounds.TFG_CHARGE).setChargeFX("TFGChargeStart",-0.14f, -0.10f, 0.42f).setBulletSpeed(2.0f).setRangeTooltipType(RangeTooltipType.RADIUS).setPenetration(PENETRATION_ULTRA).setTexture("textures/guns/tfg").setAIStats(RANGE_SHORT, 67, 3, 8).setMuzzleLight(0.2f, 1.0f, 0.2f).setMuzzleFlashTime(10).setRecoiltime(10).setForwardOffset(0.40f).setCrossHair(EnumCrosshairStyle.TRI_INV).setGunLevel(5).setMaintenanceItem(TGItems.MECHANICAL_PARTS_TITANIUM.getItem()).setMaxDamage(1400);

         // 动力锤
		 powerhammer = new PowerHammer("powerhammer", POWERHAMMER_PROJECTILES, false, 8, 300, 45, 6f, TGSounds.POWERHAMMER_FIRE, TGSounds.POWERHAMMER_RELOAD,3,0.0f,20f,5).setMeleeDmg(5.0f, 2.0f).setTool("pickaxe", 2).setTool("shovel", 2).setDigSpeed(12.0f).setChargeSound(TGSounds.POWERHAMMER_CHARGE).setBulletSpeed(1.0f).setTexture("textures/guns/powerHammer").setRecoiltime(12).setShootWithLeftClick(false).setAIStats(RANGE_MELEE, 30, 0, 0).setDamageDrop(3, 3, 2.5f).setNoMuzzleLight().setCrossHair(EnumCrosshairStyle.VANILLA).setGunLevel(1).setMaintenanceItem(TGItems.MECHANICAL_PARTS_IRON.getItem()).setMaxDamage(800);
         // 采矿钻机
		 miningdrill = new MiningDrill("miningdrill", CHAINSAW_PROJECTILES, false, 6, 300, 45, 8f, TGSounds.DRILLER_LOOP, TGSounds.POWERHAMMER_RELOAD, 2, 0.0f,1f,1).setMeleeDmg(7.0f, 2.0f).setTool("pickaxe", 3).setTool("shovel", 3).setDigSpeed(14.0f).setMiningRadius(1).setSwingSoundDelay(10).setTexture("textures/guns/miningdrill_obsidian").setRecoiltime(5).setShootWithLeftClick(false).setFiresoundStart(TGSounds.DRILLER_SWING).setMaxLoopDelay(10).setPenetration(PENETRATION_MED).setAIStats(RANGE_MELEE, 10, 0, 0).setTurretPosOffset(0, -0.47f, -0.08f).setNoMuzzleLight().setCrossHair(EnumCrosshairStyle.VANILLA).setGunLevel(2).setMaintenanceItem(TGItems.MECHANICAL_PARTS_OBSIDIAN_STEEL.getItem()).setMaxDamage(1000); //.setTurretPosOffset(0, 0.50f, 0);

		 shishkebap = new Shishkebap("shishkebap", CHAINSAW_PROJECTILES, false, 3, 300, 50, 6.0f, TGSounds.SHISHKEBAP_SWING, TGSounds.SHISHKEBAP_RELOAD, 2, 0.0f,1f,1).setMeleeDmg(14.0f, 2.0f).setTool("sword", 3).setDigSpeed(4.0f).setTextures("textures/guns/shishkebab_texture",3).setRecoiltime(5).setShootWithLeftClick(false).setPenetration(PENETRATION_MED).setAIStats(RANGE_MELEE, 10, 0, 0).setTurretPosOffset(0, -0.47f, -0.08f).setNoMuzzleLight().setHandType(GunHandType.ONE_HANDED).setHasAmbient().setCrossHair(EnumCrosshairStyle.VANILLA).setMaxDamage(2400); //.setTurretPosOffset(0, 0.50f, 0);

		 stielgranate = new GenericGrenade("stielgranate", 16, 72000, new GrenadeProjectile.Factory()).setDamageAndRadius(10, 3.0f, 5, 5.0f);
		 
		 fraggrenade = new GenericGrenade("fraggrenade", 16, 72000, new FragGrenadeProjectile.Factory()).setStartSound(TGSounds.GRENADE_PIN).setDamageAndRadius(12, 3.5f, 6, 7.0f);

		 // boss用的黄金左轮
		goldenrevolverBoss = new GenericGun("goldenrevolverBoss",PISTOL_PROJECTILES, true, 12, 6,25,14f, TGSounds.REVOLVER_GOLDEN_FIRE, TGSounds.REVOLVER_RELOAD,(int)(MAX_RANGE_PISTOL*1.5),0.015f).setDamageDrop(30, 45, 12f).setRecoiltime(8).setBulletSpeed(3.2f).setAIStats(RANGE_MEDIUM, 90, 6, 12).setTexture("textures/guns/goldenrevolver").setHandType(GunHandType.ONE_HANDED).setGunLevel(3).setPenetration(0.2f).setMaterial(Item.ToolMaterial.GOLD).setMaintenanceItem(TGItems.MECHANICAL_PARTS_GOLD.getItem()).setConsumingDurabilityEveryShoot(4);


		if(TGItems.WRITE_ITEM_JSON && event.getSide()==Side.CLIENT){
			GenericGun.guns.forEach(g -> ItemJsonCreator.writeJsonFilesForGun(g));
		 }
	}

	@SideOnly(Side.CLIENT)
    public static void initModels() {
	//	ModelLoader.setCustomModelResourceLocation(grenadelauncher, 0, new ModelResourceLocation(grenadelauncher.getRegistryName(), "inventory"));
	//	ModelLoader.setCustomModelResourceLocation(grenadelauncher, 1, new ModelResourceLocation(grenadelauncher.getRegistryName()+"_1", "inventory"));
		
		//ModelLoader.setCustomModelResourceLocation(gaussrifle, 0, new ModelResourceLocation(gaussrifle.getRegistryName(), "inventory"));
    }
	
	@Override
	public void init(FMLInitializationEvent event) {
		
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

	}
	
}
