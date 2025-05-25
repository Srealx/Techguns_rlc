package techguns.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mojang.realmsclient.gui.ChatFormatting;

import crafttweaker.api.entity.IEntityMob;
import elucent.albedo.event.GatherLightsEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import techguns.*;
import techguns.api.guns.GunHandType;
import techguns.api.guns.GunManager;
import techguns.api.guns.IGenericGun;
import techguns.api.radiation.TGRadiation;
import techguns.api.tginventory.ITGSpecialSlot;
import techguns.api.tginventory.TGSlotType;
import techguns.bean.NoThinkShootIconBackColorBean;
import techguns.capabilities.TGExtendedPlayer;
import techguns.client.ClientProxy;
import techguns.client.ShooterValues;
import techguns.client.render.entities.npcs.RenderAttackHelicopter;
import techguns.client.render.entities.projectiles.DeathEffectEntityRenderer;
import techguns.client.render.entities.projectiles.RenderGrenade40mmProjectile;
import techguns.client.render.tileentities.RenderDoor3x3Fast;
import techguns.damagesystem.DamageSystem;
import techguns.damagesystem.TGDamageSource;
import techguns.damagesystem.event.GunsCriticalHitEvent;
import techguns.deatheffects.EntityDeathUtils;
import techguns.deatheffects.EntityDeathUtils.DeathType;
import techguns.enchantment.GenericGunEnchantment;
import techguns.enchantment.NoThinkShoot;
import techguns.entities.npcs.NPCTurret;
import techguns.entities.npcs.TGDummySpawn;
import techguns.entities.spawn.TGSpawnManager;
import techguns.gui.player.TGPlayerInventory;
import techguns.gui.widgets.SlotFabricator;
import techguns.gui.widgets.SlotTG;
import techguns.items.armors.GenericArmor;
import techguns.items.armors.TGArmorBonus;
import techguns.items.guns.GenericGrenade;
import techguns.items.guns.GenericGun;
import techguns.items.guns.GenericGunCharge;
import techguns.items.guns.GenericGunMeleeCharge;
import techguns.items.guns.MiningDrill;
import techguns.packets.*;
import techguns.radiation.ItemRadiationData;
import techguns.radiation.ItemRadiationRegistry;
import techguns.util.BlockUtils;
import techguns.util.InventoryUtil;
import techguns.util.TextUtil;

@Mod.EventBusSubscriber(modid = Techguns.MODID)
public class TGEventHandler {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onMouseEvent(MouseEvent event) {
		if (event.getButton()!=-1){
		
			EntityPlayerSP ply = Minecraft.getMinecraft().player;
			
			if (Minecraft.getMinecraft().inGameHasFocus) {
				// System.out.println("MOUSE EVENT LMB");
				if (event.getButton() == 0 && !ply.getHeldItemMainhand().isEmpty() && ply.getHeldItemMainhand().getItem() instanceof IGenericGun) {
					ClientProxy cp = ClientProxy.get();
					
					if (((IGenericGun) ply.getHeldItemMainhand().getItem()).isShootWithLeftClick()) {
						cp.keyFirePressedMainhand = event.isButtonstate();
						event.setCanceled(true);
		
						// can't mine/attack while reloading
					} else if (ShooterValues.getReloadtime(ClientProxy.get().getPlayerClient(), false) > 0) {
						long diff = ShooterValues.getReloadtime(ClientProxy.get().getPlayerClient(), false) - System.currentTimeMillis();
						if (diff > 0) {
							if (event.isButtonstate()) {
								event.setCanceled(true);
							}
						}
		
					}
				} /*else if (event.getButton() == 1 && !GunManager.canUseOffhand(ply)) {
					if(!ply.getHeldItemMainhand().isEmpty()&&ply.getHeldItemMainhand().getItem() instanceof GenericGunCharge) {
						//Charging gun is allowed
					} else if (!ply.getHeldItemMainhand().isEmpty() && ply.getHeldItemMainhand().getItem() instanceof GenericGun){
						GenericGun g = (GenericGun) ply.getHeldItemMainhand().getItem();
						
						//Cancel and call secondary action
						if (!ply.isSneaking() && event.isButtonstate()) {
							boolean use = g.gunSecondaryAction(ply, ply.getHeldItemMainhand());
							event.setCanceled(use);
						}
					
					}
					
				} */ else if (event.getButton() == 1 && !ply.isSneaking() && !ply.getHeldItemOffhand().isEmpty() && ply.getHeldItemOffhand().getItem() instanceof IGenericGun && GunManager.canUseOffhand(ply)) {
					ClientProxy cp = ClientProxy.get();
					
					if (((IGenericGun) ply.getHeldItemOffhand().getItem()).isShootWithLeftClick()) {
						cp.keyFirePressedOffhand = event.isButtonstate();
						event.setCanceled(true);
		
						// can't mine/attack while reloading
					} else if (ShooterValues.getReloadtime(ClientProxy.get().getPlayerClient(), true) > 0) {
						long diff = ShooterValues.getReloadtime(ClientProxy.get().getPlayerClient(), true) - System.currentTimeMillis();
						if (diff > 0) {
							if (event.isButtonstate()) {
								event.setCanceled(true);
							}
						}
		
					}
					
				//Lock On Weapon
				}else if (event.getButton() == 1 && ply.getHeldItemMainhand().getItem() instanceof GenericGunCharge && ((GenericGunCharge)ply.getHeldItemMainhand().getItem()).getLockOnTicks() > 0) {
					//System.out.println("Start/Stop LockOn: RMB = "+event.isButtonstate());
					ClientProxy cp = ClientProxy.get();
					//cp.keyFirePressedOffhand = event.isButtonstate();
					
					TGExtendedPlayer props = TGExtendedPlayer.get(ply);
					props.lockOnEntity = null;
					props.lockOnTicks = -1;
				}
			}
		}
	}
	
	
	protected static boolean allowOffhandUse(EntityPlayer player, EnumHand hand) {
		if (hand == EnumHand.MAIN_HAND) return true;
		if(!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof IGenericGun) {
			IGenericGun g = (IGenericGun) player.getHeldItemMainhand().getItem();
			if(g.getGunHandType()==GunHandType.TWO_HANDED) {
				return false;
			}
		}
		if(!player.getHeldItemOffhand().isEmpty() && player.getHeldItemOffhand().getItem() instanceof IGenericGun) {
			IGenericGun g = (IGenericGun) player.getHeldItemOffhand().getItem();
			if(g.getGunHandType()==GunHandType.TWO_HANDED) {
				return false;
			}
		}
		return true;
	}
	
	@SubscribeEvent(priority=EventPriority.HIGH, receiveCanceled=false)
	public static void rightClickEvent(PlayerInteractEvent.RightClickItem event) {
		boolean cancel = !allowOffhandUse(event.getEntityPlayer(), event.getHand());
		if (cancel) {
			event.setCanceled(cancel);
			event.setCancellationResult(EnumActionResult.PASS);
		}
		//System.out.println("Right Click Item:"+event.getEntityPlayer()+" "+event.getHand());
	}
	
	@SubscribeEvent(priority=EventPriority.HIGH, receiveCanceled=false)
	public static void rightClickEvent(PlayerInteractEvent.RightClickBlock event) {
		boolean cancel = !allowOffhandUse(event.getEntityPlayer(), event.getHand());
		if (cancel) {
			event.setCanceled(cancel);
			event.setUseBlock(Result.ALLOW);
			event.setUseItem(Result.DENY);
			event.setCancellationResult(EnumActionResult.PASS);
		} else if (event.getHand() == EnumHand.MAIN_HAND){
			
			EntityPlayer ply = event.getEntityPlayer();
			if(ply.isSneaking() && !ply.getHeldItemOffhand().isEmpty() && (ply.getHeldItemOffhand().getItem() instanceof GenericGun) && (!event.getItemStack().isEmpty() && !(event.getItemStack().getItem() instanceof GenericGun)) ) {
				event.setUseBlock(Result.ALLOW);
			}
			
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGH, receiveCanceled=false)
	public static void rightClickEvent(PlayerInteractEvent.EntityInteract event) {
		boolean cancel = !allowOffhandUse(event.getEntityPlayer(), event.getHand());
		if (cancel) {
			event.setCanceled(cancel);
			event.setCancellationResult(EnumActionResult.PASS);
		}
		//System.out.println("EntityInteract:"+event.getEntityPlayer()+" "+event.getHand());
	}
	
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.LOW) //set to low so other mods don't accidentally destroy it easily
	public static void handleFovEvent(FOVUpdateEvent event){

//		float f = 1.0f;
//		if ( TGConfig.cl_lockSpeedFov){
//			IAttributeInstance iattributeinstance = event.getEntity().getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
//        	f = 1/ ((float)(((iattributeinstance.getAttributeValue() / (double)event.getEntity().capabilities.getWalkSpeed() + 1.0D) / 2.0D)));
//
//        	if(ClientProxy.get().getPlayerClient().isSprinting()){
//        		f*=TGConfig.cl_fixedSprintFov;
//        	}
//		}
//		event.setNewfov(event.getNewfov()*ClientProxy.get().player_zoom*f);
		//*speedFOV;
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=false)
	public static void onRenderLivingEventPre(RenderLivingEvent.Pre event){
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer ply = (EntityPlayer) event.getEntity();
			
		 	ItemStack stack =ply.getHeldItemMainhand();
		 	if(!stack.isEmpty() && stack.getItem() instanceof GenericGun && ((GenericGun) stack.getItem()).hasBowAnim()){
		 		ModelBase mdl = event.getRenderer().getMainModel();
		 		if (mdl instanceof ModelPlayer) {
			 		ModelPlayer model = (ModelPlayer) mdl;
			 		if (ply.getPrimaryHand()==EnumHandSide.RIGHT) {
			 			model.rightArmPose = ArmPose.BOW_AND_ARROW;
			 		} else {
			 			model.leftArmPose = ArmPose.BOW_AND_ARROW;
			 		}
		 		}
		 	} else {
		 	
			 	ItemStack stack2 =ply.getHeldItemOffhand();
			 	if(!stack2.isEmpty() && stack2.getItem() instanceof GenericGun && ((GenericGun) stack2.getItem()).hasBowAnim()){
			 		ModelBase mdl = event.getRenderer().getMainModel();
			 		if (mdl instanceof ModelPlayer) {
				 		ModelPlayer model = (ModelPlayer) mdl;
				 		
				 		if (ShooterValues.getIsCurrentlyUsingGun(ply,true)){
					 		
					 		if (ply.getPrimaryHand()==EnumHandSide.RIGHT) {
					 			model.leftArmPose = ArmPose.BOW_AND_ARROW;
					 		} else {
					 			model.rightArmPose = ArmPose.BOW_AND_ARROW;
					 		}
				 		}
			 		}
			 	}
		 	}
		}
		
		/*
		 * ENTITY DEATH EFFECTS
		 */
		ClientProxy cp = ClientProxy.get();
		DeathType dt = cp.getEntityDeathType(event.getEntity());
		switch (dt) {
		case GORE:
			event.setCanceled(true);
			break;
		case DISMEMBER:
		case BIO:
		case LASER:
			//TODO
			event.setCanceled(true);
			DeathEffectEntityRenderer.doRender(event.getRenderer(), event.getEntity(), event.getX(), event.getY(), event.getZ(), 0f, dt);
			break;
		case DEFAULT:
		default:
			break;
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGH, receiveCanceled=false)
	public static void OnLivingAttack(LivingAttackEvent event){
		if (event.getSource() instanceof TGDamageSource) {
			event.setCanceled(true);
			try {
				DamageSystem.attackEntityFrom(event.getEntityLiving(), event.getSource(), event.getAmount());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} else if ( (event.getSource() == DamageSource.LAVA || event.getSource()==DamageSource.ON_FIRE || event.getSource()==DamageSource.IN_FIRE) && event.getEntityLiving() instanceof EntityPlayer) {
			float bonus = GenericArmor.getArmorBonusForPlayer((EntityPlayer) event.getEntityLiving(), TGArmorBonus.COOLING_SYSTEM,event.getEntityLiving().world.getTotalWorldTime()%5==0);
			
			if (bonus >=1.0f) {
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public static void onLivingHurt(LivingHurtEvent event) {
		// 修改玩家或炮塔使用枪械造成的伤害逻辑
		Entity trueSource = event.getSource().getTrueSource();
		if(trueSource instanceof EntityPlayer || trueSource instanceof NPCTurret){
			EntityLivingBase shooter = (EntityLivingBase)event.getSource().getTrueSource();
			if (event.getSource() instanceof TGDamageSource){
				int gunLevel=1;
				ItemStack sourceItemStack = ((TGDamageSource) event.getSource()).getSourceItemStack();
				if (sourceItemStack != null && sourceItemStack.getItem() instanceof GenericGun){
					gunLevel = ((GenericGun) sourceItemStack.getItem()).getLevel();
				}else if (shooter.getHeldItemMainhand().getItem() instanceof GenericGun){
					gunLevel =  ((GenericGun)shooter.getHeldItemMainhand().getItem()).getLevel();
				}
				DamageSystem.playerShootProjectileDamageHit(event,shooter,gunLevel);
				event.setCanceled(true);
			}
		}
	}


	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onBulletCriticalHit(GunsCriticalHitEvent criticalHitEvent){
		TGDamageSource damageSource = criticalHitEvent.getDamageSource();
		EntityPlayer shooter = criticalHitEvent.getEntityPlayer();
		if ( Boolean.TRUE.equals(damageSource.getPlaySound())
				&& Boolean.FALSE.equals(damageSource.isExplosion())){
			TGPackets.network.sendTo(new PacketPlayCriticalSoundTarget(),(EntityPlayerMP) shooter);
		}
	}

	@SubscribeEvent
	public static void onGunsKill(LivingDeathEvent livingDeathEvent){
		DamageSource source = livingDeathEvent.getSource();
		if (source instanceof TGDamageSource){
			ItemStack sourceItemStack = ((TGDamageSource) source).getSourceItemStack();
			if (sourceItemStack!=null){
				Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(sourceItemStack);
				enchantments.forEach((k,v)->{
					if (k instanceof GenericGunEnchantment){
						((GenericGunEnchantment) k).afterGunKill((TGDamageSource) source);
					}
				});
			}
		}
	}


	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=false)
	public static void onLivingJumpEvent(LivingJumpEvent event)
	{
	    if (event.getEntity() instanceof EntityPlayer)
	    {
	        EntityPlayer ply = (EntityPlayer) event.getEntity();
	        float jumpbonus = GenericArmor.getArmorBonusForPlayer(ply, TGArmorBonus.JUMP,true);
	        
	        /*if (ply.onGround && ply.isSneaking()){
	        	jumpbonus*=5;
	        }*/
	        
	        ply.motionY+=jumpbonus;
	    }  
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=false)
	public static void onLivingFallEvent(LivingFallEvent event)
	{
	    if (event.getEntity() instanceof EntityPlayer)
	    {	
	    	boolean consume= event.getDistance()>3.0f;
	    	
	        EntityPlayer ply = (EntityPlayer) event.getEntity();
	        float fallbonus = GenericArmor.getArmorBonusForPlayer(ply, TGArmorBonus.FALLDMG,consume);
	        float reduction = (fallbonus <1 ? 1-fallbonus : 0.0f);
	        float freeheight = GenericArmor.getArmorBonusForPlayer(ply, TGArmorBonus.FREEHEIGHT,false);
	        if(freeheight<event.getDistance()){
	        	event.setDistance(event.getDistance() - freeheight);
	        } else {
	        	event.setDistance(0.0f);
	        }
	        
	        event.setDistance(event.getDistance() * reduction);
	    }  
	}
	
	@SubscribeEvent(priority=EventPriority.HIGH, receiveCanceled=false)
	public static void onBreakEventHigh(BreakSpeed event){
		EntityPlayer ply = event.getEntityPlayer();
		ItemStack item = ply.getHeldItemMainhand();
		if(!item.isEmpty() && item.getItem() instanceof GenericGunMeleeCharge) {
			GenericGunMeleeCharge g = (GenericGunMeleeCharge) item.getItem();
			if(g.getMiningRadius(item)>0) {	
				EnumFacing sidehit = g.getSideHitMining(ply.world, ply);
				
				if (sidehit!=null) {
					IBlockState state = event.getState();
					float mainHardness = state.getBlockHardness(ply.world, event.getPos());
					
					List<BlockPos> blocks = BlockUtils.getBlockPlaneAroundAxisForMining(ply.world,ply, event.getPos(), sidehit.getAxis(), g.getMiningRadius(item), false, g, item);
					float maxHardness = 0f;
					for (BlockPos p: blocks) {
						IBlockState s = ply.world.getBlockState(p);
						float h = s.getBlockHardness(ply.world, p);
						if(h>maxHardness) {
							maxHardness=h;
						}
					}
					
					if (maxHardness>mainHardness) {
						event.setNewSpeed(event.getNewSpeed()*mainHardness/maxHardness);
					}
				}
			}
		}
	}
	
	
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=false)
	public static void onBreakEvent(BreakSpeed event){
		EntityPlayer ply = event.getEntityPlayer();
		float bonus = 1.0f+GenericArmor.getArmorBonusForPlayer(ply, TGArmorBonus.BREAKSPEED,true);
		float waterbonus=1.0f;
		if(ply.isInsideOfMaterial(Material.WATER)  || ply.isInsideOfMaterial(Material.LAVA)){
			waterbonus += GenericArmor.getArmorBonusForPlayer(ply, TGArmorBonus.BREAKSPEED_WATER,true);
		}
		
		event.setNewSpeed(event.getNewSpeed()*bonus*waterbonus);
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=false)
	public static void onLivingDeathEvent(LivingDeathEvent event){
		EntityLivingBase entity = event.getEntityLiving();
		if(!entity.world.isRemote){

			if(entity instanceof EntityPlayer){
				TGExtendedPlayer tgplayer = TGExtendedPlayer.get((EntityPlayer) event.getEntityLiving());
				tgplayer.foodleft=0;
				tgplayer.lastSaturation=0;
				tgplayer.addRadiation(-TGRadiationSystem.RADLOST_ON_DEATH);
			}
			
			if (event.getSource() instanceof TGDamageSource) {
				TGDamageSource tgs = (TGDamageSource)event.getSource();
				if (tgs.deathType != DeathType.DEFAULT) {
					if(Math.random()<tgs.goreChance) {
						if (EntityDeathUtils.hasSpecialDeathAnim(entity, tgs.deathType)) {
							//System.out.println("Send packet!");
							TGPackets.network.sendToAllAround(new PacketEntityDeathType(entity, tgs.deathType), TGPackets.targetPointAroundEnt(entity, 100.0f));
						}
					}
				}
			}
		}
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=false)
	public static void onEntityJoinWorld(EntityJoinWorldEvent event){
		if (!event.getEntity().world.isRemote){
			if(event.getEntity() instanceof EntityPlayer){
				EntityPlayer ply = (EntityPlayer) event.getEntity();
				TGExtendedPlayer props = TGExtendedPlayer.get(ply);
				if (props!=null){
					//System.out.println("SENT EXTENDED PLAYER SYNC");
					//TGPackets.network.sendToDimension(new PacketTGExtendedPlayerSync(props, false), event.entity.dimension);
					TGPackets.network.sendTo(new PacketTGExtendedPlayerSync(ply,props, true), (EntityPlayerMP) ply);
					
					ply.getDataManager().set(TGExtendedPlayer.DATA_FACE_SLOT, props.tg_inventory.getStackInSlot(TGPlayerInventory.SLOT_FACE));
					ply.getDataManager().set(TGExtendedPlayer.DATA_BACK_SLOT, props.tg_inventory.getStackInSlot(TGPlayerInventory.SLOT_BACK));
					ply.getDataManager().set(TGExtendedPlayer.DATA_HAND_SLOT, props.tg_inventory.getStackInSlot(TGPlayerInventory.SLOT_HAND));
					//ply.getDataWatcher().updateObject(TechgunsExtendedPlayerProperties.DATA_WATCHER_ID_FACESLOT, props.TG_inventory.inventory[TGPlayerInventory.SLOT_FACE]);
					//ply.getDataWatcher().updateObject(TechgunsExtendedPlayerProperties.DATA_WATCHER_ID_BACKSLOT, props.TG_inventory.inventory[TGPlayerInventory.SLOT_BACK]);
				
				}

			} else if (event.getEntity() instanceof TGDummySpawn){
				//
				TGSpawnManager.handleSpawn(event.getWorld(), event.getEntity());
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void onStartTracking(PlayerEvent.StartTracking event){
		if(event.getEntityPlayer().world.isRemote){
			TGPackets.network.sendToServer(new PacketRequestTGPlayerSync(event.getEntityPlayer()));			
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void onStopTracking(PlayerEvent.StopTracking event){
		if(event.getEntityPlayer().world.isRemote){
			TGExtendedPlayer props = TGExtendedPlayer.get(event.getEntityPlayer());
			if(props!=null){
				props.setJumpkeyPressed(false);
				props.isGliding=false;
			}
		}
	}
		
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent event) {
		event.getMap().registerSprite(SlotTG.BACKSLOT_TEX);
		event.getMap().registerSprite(SlotTG.FACESLOT_TEX);
		event.getMap().registerSprite(SlotTG.HANDSLOT_TEX);
		event.getMap().registerSprite(SlotTG.FOODSLOT_TEX);
		event.getMap().registerSprite(SlotTG.HEALSLOT_TEX);
		event.getMap().registerSprite(SlotTG.AMMOSLOT_TEX);
		
		event.getMap().registerSprite(SlotTG.AMMOEMPTYSLOT_TEX);
		event.getMap().registerSprite(SlotTG.BOTTLESLOT_TEX);
		event.getMap().registerSprite(SlotTG.TURRETGUNSLOT_TEX);
		event.getMap().registerSprite(SlotTG.TURTETARMORSLOT_TEX);

		event.getMap().registerSprite(SlotFabricator.FABRICATOR_SLOTTEX_WIRES);
		event.getMap().registerSprite(SlotFabricator.FABRICATOR_SLOTTEX_POWDER);
		event.getMap().registerSprite(SlotFabricator.FABRICATOR_SLOTTEX_PLATE);
		
		event.getMap().registerSprite(SlotTG.INGOTSLOT_TEX);
		event.getMap().registerSprite(SlotTG.INGOTDARKSLOT_TEX);
		
		RenderDoor3x3Fast.stitchTextures(event.getMap());
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onModelBake(ModelBakeEvent event) {
		RenderGrenade40mmProjectile.initModel();
		RenderAttackHelicopter.initModels();
		RenderDoor3x3Fast.initModels();
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onRenderHand(RenderHandEvent event) {
		float t = 1.0f;
		EntityPlayer ply = ClientProxy.get().getPlayerClient();
		ItemStack stack = ply.getActiveItemStack();
		
		if(!stack.isEmpty() && ((stack.getItem() instanceof GenericGunCharge && ((GenericGunCharge)stack.getItem()).hasRightClickAction()) || stack.getItem() instanceof GenericGrenade)) {
			EnumHand hand = ply.getActiveHand();

			ItemRenderer itemrenderer = Minecraft.getMinecraft().getItemRenderer();
			try {
				ClientProxy cp = ClientProxy.get();
				if(hand==EnumHand.MAIN_HAND) {
					if(cp.Field_ItemRenderer_equippedProgressMainhand.getFloat(itemrenderer)<t) {
						cp.Field_ItemRenderer_equippedProgressMainhand.setFloat(itemrenderer, t);
						cp.Field_ItemRenderer_prevEquippedProgressMainhand.setFloat(itemrenderer, t);
					}
				} else {
					if(cp.Field_ItemRenderer_equippedProgressOffhand.getFloat(itemrenderer)<t) {
						cp.Field_ItemRenderer_equippedProgressOffhand.setFloat(itemrenderer, t);
						cp.Field_ItemRenderer_prevEquippedProgressOffhand.setFloat(itemrenderer, t);
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
		} /*else {
			
			if(!ply.getHeldItemMainhand().isEmpty() && ply.getHeldItemMainhand().getItem() instanceof IGenericGunMelee) {
				ItemRenderer itemrenderer = Minecraft.getMinecraft().getItemRenderer();
				float f = ply.getCooledAttackStrength(1.0f);
				System.out.println("f:"+f);
				if (f<1f) {
					try {
						System.out.println("Set to 1");
						Field_ItemRenderer_equippedProgressMainhand.setFloat(itemrenderer, 1.0f);
						Field_ItemRenderer_prevEquippedProgressMainhand.setFloat(itemrenderer, 1.0f);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
		}*/

	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onRenderWorldLast(RenderWorldLastEvent event) {

//		GLStateSnapshot states = new GLStateSnapshot();
		//System.out.println("***********BEFORE**********");
		//states.printDebug();
		ClientProxy.get().particleManager.renderParticles(Minecraft.getMinecraft().getRenderViewEntity(), event.getPartialTicks());
//		states.restore();
		//System.out.println("<<<<<<<<<<<AFTER>>>>>>>>>>>>");
		//new GLStateSnapshot().printDebug();
		GlStateManager.disableBlend();
		GlStateManager.blendFunc(SourceFactor.ONE, DestFactor.ZERO);
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.disableLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0, 240f);
	}
	
	@SubscribeEvent
	public static void onCraftEvent(ItemCraftedEvent event) {
		if(event.crafting.getItem() instanceof GenericGun) {
			boolean hasGun=false;
			boolean hasAmmo=false;
			boolean hasInvalid=false;
			
			ItemStack gun=ItemStack.EMPTY;
			
			for(int i=0; i<event.craftMatrix.getSizeInventory();i++) {
				ItemStack stack = event.craftMatrix.getStackInSlot(i);
				
				if(!stack.isEmpty()) {
					if( stack.getItem() instanceof GenericGun) {
						if(!hasGun) {
							hasGun=true;
							gun=stack;
						} else {
							hasInvalid=true;
							break;
						}
					} else if (stack.getItem() instanceof ITGSpecialSlot && ((ITGSpecialSlot)stack.getItem()).getSlot(stack)==TGSlotType.AMMOSLOT){
						if(!hasAmmo) {
							hasAmmo=true;
						} else {
							hasInvalid=true;
							break;
						}
						
					} else {
						hasInvalid=true;
						break;
					}
				}
			}
			if(!hasInvalid && hasGun && hasAmmo) {
				//Was an Ammo change recipe!
				GenericGun g = (GenericGun) gun.getItem();
				List<ItemStack> items = g.getAmmoOnUnload(gun);
				items.forEach(i -> {
					int amount = InventoryUtil.addAmmoToPlayerInventory(event.player, i);
					if(amount>0 && !event.player.world.isRemote) {
						ItemStack it = i.copy();
						it.setCount(amount);
						event.player.world.spawnEntity(new EntityItem(event.player.world, event.player.posX, event.player.posY, event.player.posZ, it));
					}		
					});
			}
			
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=false)
	public static void onPlayerDrops(PlayerDropsEvent event){
		EntityPlayer ply = event.getEntityPlayer();
		TGExtendedPlayer props = TGExtendedPlayer.get(ply);
		
		if(props!=null){

			props.dropInventory(ply);

		}
	}
	
	public static Method Block_getSilkTouchDrop = ReflectionHelper.findMethod(Block.class, "getSilkTouchDrop", "func_180643_i", IBlockState.class);

	@SubscribeEvent
	public static void onBlockDrops(HarvestDropsEvent event) {
		EntityPlayer ply = event.getHarvester();
		if(ply!=null) {
			ItemStack stack = ply.getHeldItemMainhand();
			if(!stack.isEmpty() && stack.getItem() instanceof MiningDrill && ply.isSneaking()) {
				IBlockState state = event.getState();
				if (state.getBlock().canSilkHarvest(ply.world, event.getPos(), state, ply)){
				
					MiningDrill md = (MiningDrill) stack.getItem();
					if (md.getAmmoLeft(stack)>0) {
						
						List drops = event.getDrops();
						drops.clear();
						
						try {
							drops.add(Block_getSilkTouchDrop.invoke(state.getBlock(), state));
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
						
						event.setDropChance(1.0f);
					}
				}
			}
		}
		
	}
	
	//stop XP drop on silk harvesting with mining drill
	@SubscribeEvent(priority=EventPriority.HIGH)
	public static void onBlockBreakEvent(BlockEvent.BreakEvent event) {
		EntityPlayer ply = event.getPlayer();
		if(ply!=null) {
			ItemStack stack = ply.getHeldItemMainhand();
			if(!stack.isEmpty() && stack.getItem() instanceof MiningDrill && ply.isSneaking()) {
				event.setExpToDrop(0);
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGH) //run before regular drop events
	public static void MilitaryCrateDrops(HarvestDropsEvent event) {
		IBlockState state = event.getState();

		if(state.getBlock()==TGBlocks.MILITARY_CRATE && !event.isSilkTouching() && !event.getWorld().isRemote) {
			BlockPos pos = event.getPos();
			EntityPlayer ply = event.getHarvester();
			if (ply!=null) {
				int fortune = event.getFortuneLevel();
						
				LootTable loottable = ply.world.getLootTableManager().getLootTableFromLocation(TGBlocks.MILITARY_CRATE.getLootableForState(state));
				LootContext lootcontext = new LootContext.Builder((WorldServer) ply.world).withLuck(fortune).withPlayer(ply).build();
				
				event.getDrops().clear();
				for (ItemStack itemstack : loottable.generateLootForPools(ply.world.rand, lootcontext))
	            {
					event.getDrops().add(itemstack);
	            }
			}
		}
	}
	
	/*@SubscribeEvent
	public static void damageTest(LivingHurtEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			System.out.println("Attacking"+event.getEntityLiving()+" for "+event.getAmount() +" with "+event.getSource());
		}
	}*/
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onBlockHighlight(DrawBlockHighlightEvent event) {
		EntityPlayer ply = event.getPlayer();
		ItemStack item = ply.getHeldItemMainhand();
		if(!ply.isSneaking() && !item.isEmpty() && item.getItem() instanceof GenericGunMeleeCharge) {
			GenericGunMeleeCharge g = (GenericGunMeleeCharge) item.getItem();
			if (g.getMiningRadius(item)>0 && g.getAmmoLeft(item)>0) {
				RayTraceResult target = event.getTarget();
				if(target!=null && target.typeOfHit == Type.BLOCK) {
					BlockPos p = target.getBlockPos();
					List<BlockPos> otherblocks = BlockUtils.getBlockPlaneAroundAxisForMining(ply.world, ply, p, target.sideHit.getAxis(), g.getMiningRadius(item), false, g, item);
					otherblocks.forEach(b -> {
						RayTraceResult result = new RayTraceResult(new Vec3d(b.getX()+0.5d,  b.getY()+0.5d,  b.getZ()+0.5d),target.sideHit,b);
						event.getContext().drawSelectionBox(ply, result, 0, event.getPartialTicks());
					});
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onItemSwitch(LivingEquipmentChangeEvent event) {
		if (event.getSlot()==EntityEquipmentSlot.MAINHAND || event.getSlot()==EntityEquipmentSlot.OFFHAND) {
			
			boolean fromEffect=false;
			boolean toEffect =false;
			if (!event.getTo().isEmpty() && event.getTo().getItem() instanceof GenericGun) {
				if(((GenericGun)event.getTo().getItem()).hasAmbientEffect()){
					toEffect=true;
				}
			}
			if (!event.getFrom().isEmpty() && event.getFrom().getItem() instanceof GenericGun) {
				if(((GenericGun)event.getFrom().getItem()).hasAmbientEffect()){
					fromEffect=true;
				}
			}
			
			if(fromEffect||toEffect){
				EnumHand hand = EnumHand.MAIN_HAND;
				if (event.getSlot()==EntityEquipmentSlot.OFFHAND) {
					hand=EnumHand.OFF_HAND;
				}
				TGPackets.network.sendToDimension(new PacketNotifyAmbientEffectChange(event.getEntityLiving(), hand), event.getEntityLiving().world.provider.getDimension());
			}
			 
		}
	}
	
	@Optional.Method(modid="albedo")
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onGatherLightsEvent(GatherLightsEvent event) {
		ClientProxy cp = ClientProxy.get();
		for (int i=0;i<cp.activeLightPulses.size();i++) {
			event.add(cp.activeLightPulses.get(i).provideLight());
		}
		//ClientProxy.get().activeLightPulses.forEach(l -> event.getLightList().add(l.provideLight()));
	}
	
	
	/*@SubscribeEvent
	public static void itemPickupRadiation(EntityItemPickupEvent event) {
		ItemStack stack = event.getItem().getItem();
		if(!stack.isEmpty()) {
			ItemRadiationData data = ItemRadiationRegistry.getRadiationDataFor(stack);
			if(data!=null) {
				event.getEntityPlayer().addPotionEffect(new PotionEffect(TGRadiationSystem.radiation_effect, data.radduration, data.radamount-1, false,false));
			}
		}
	}*/
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void ItemRadiationTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if(!stack.isEmpty()) {
			ItemRadiationData data = ItemRadiationRegistry.getRadiationDataFor(stack);
			if(data!=null && data.radamount>0) {
				event.getToolTip().add(ChatFormatting.GREEN+TextUtil.trans("techguns.radiation")+" "+TextUtil.trans("potion.potency."+(data.radamount-1)));
			}
		}
	}
	
	@SubscribeEvent
	public static void onEntityConstruction(EntityConstructing event) {

		if(event.getEntity() instanceof EntityLivingBase) {
			EntityLivingBase elb = (EntityLivingBase) event.getEntity();
			elb.getAttributeMap().registerAttribute(TGRadiation.RADIATION_RESISTANCE).setBaseValue(0);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void controlGunsEnchantment(AnvilUpdateEvent event) {
		ItemStack leftItem = event.getLeft();
		ItemStack rightItem = event.getRight();
		if (leftItem.isEmpty() || rightItem.isEmpty()){
			return;
		}
		if (rightItem.getItem() == Items.ENCHANTED_BOOK && leftItem.getItem() instanceof GenericGun){
			List<Enchantment> cantList = new ArrayList<>();
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(rightItem);
			enchantments.forEach((k,v)->{
				if (k instanceof GenericGunEnchantment){
					boolean canApplyAnvil = ((GenericGunEnchantment) k).canApplyAnvil(leftItem);
					boolean canApply = k.canApply(leftItem);
					if (!canApplyAnvil || !canApply){
						cantList.add(k);
					}
				}
			});
			if (!cantList.isEmpty()){
				event.setCanceled(Boolean.TRUE);
			}
		}
	}

	@SubscribeEvent
	public static void controlGunsMaintenance(AnvilUpdateEvent event) {
		// 获取铁砧输入的物品
		ItemStack leftItem = event.getLeft();
		ItemStack rightItem = event.getRight();
		if (leftItem.isEmpty() || rightItem.isEmpty()){
			return;
		}
		if (leftItem.getItem() instanceof GenericGun && leftItem.getItemDamage()>0){
			if (rightItem.getItem() == ((GenericGun) leftItem.getItem()).getMaintenanceItem()){
				int itemDamage = leftItem.getItemDamage();
				int rightCount = rightItem.getCount();
				// this 4 is max damage repeat required
				int repeatAmount = leftItem.getMaxDamage() / 4;
				int needCountBase =  itemDamage / repeatAmount;
				int needCountRemainder = (itemDamage % repeatAmount) > 0 ? 1:0;
				int needCount = needCountBase + needCountRemainder;
				int repair =  rightCount * repeatAmount;
				event.setMaterialCost(needCount);
				event.setOutput(leftItem.copy());
				if (repair >= itemDamage){
					event.getOutput().setItemDamage(0);
				}else {
					event.getOutput().setItemDamage(itemDamage -  repair);
				}
				event.setCost(needCount);
			}
		}
	}


	@SubscribeEvent
	public static void onLivingDrops(LivingDropsEvent event) {
		// 检查是否是怪物死亡（排除玩家和非生物实体）
		if ((event.getEntity() instanceof EntityPlayer)) {
			return;
		}
		// 遍历所有掉落的物品
		for (EntityItem entityItem : event.getDrops()) {
			ItemStack stack = entityItem.getItem();
			if (stack.getItem() instanceof GenericGun){
				GenericGun gun = (GenericGun)stack.getItem();
				int maxAmmo = gun.getClipsize();
				int ammo =  new Random().nextInt(maxAmmo)+1;
				NBTTagCompound tagCompound = stack.getTagCompound();
				if(tagCompound == null){
					tagCompound = new NBTTagCompound();
					stack.setTagCompound(tagCompound);
				}
				tagCompound.setShort("ammo",(short) ammo);
				// 随机耐久值
				int ranDamage =  new Random().nextInt(10)+1;
				if (ranDamage>5){
					int maxDamage = stack.getMaxDamage();
					int damage = Math.round(maxDamage * 0.16f * (ranDamage - 5));
					stack.setItemDamage(damage);
				}
			}
		}
	}


	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onRenderItemOverlay(RenderGameOverlayEvent.Post event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
			Minecraft mc = Minecraft.getMinecraft();
			EntityPlayer player = mc.player;

			for (int i = 0; i < 9; i++) {
				ItemStack stack = player.inventory.mainInventory.get(i);
				if (!stack.isEmpty() && stack.getItem() instanceof GenericGun){
					NBTTagCompound tagCompound = stack.getTagCompound();
					if(tagCompound == null){
						continue;
					}
					if (!tagCompound.hasKey("gunFireCooling")){
						continue;
					}
					// 判断枪械是否有冷却tag
					long gunFireCooling = tagCompound.getLong("gunFireCooling");
					long currentTime = mc.world.getWorldTime();
					// 绘制冷却进度条
					int x = (event.getResolution().getScaledWidth()/ 2 - 90 + i * 20) + 2;
					int y = event.getResolution().getScaledHeight() - 19;
					if (gunFireCooling - currentTime > 0) {
						// 如果因为玩家修改了游戏时间导致cd过长, 则将cd置为新的开始时间
						if (gunFireCooling - currentTime > NoThinkShoot.COOLING_TIME * 20f){
							tagCompound.setLong("gunFireCooling",currentTime + NoThinkShoot.COOLING_TIME * 20L);
							gunFireCooling = currentTime + NoThinkShoot.COOLING_TIME * 20L;
						}
						// 计算冷却时间的百分比
						float cooldownProgress = ((float)(gunFireCooling - currentTime)) / (NoThinkShoot.COOLING_TIME * 20f);
						renderCooldownOverlay(x, y, (int) (16 * (1 - cooldownProgress)),new NoThinkShootIconBackColorBean(1.0F, 1.0F, 1.0F, 0.4F));
					}else {
						renderCooldownOverlay(x, y, 0,new NoThinkShootIconBackColorBean(0.5F, 0F, 0.5F, 0.15F));
					}
				}
			}
		}
	}

	public static void renderCooldownOverlay(int x, int y, int cooldownHeight, NoThinkShootIconBackColorBean noThinkShootIconBackColorBean) {
		// 开始渲染
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(
				GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
		);

		// 禁用纹理和深度测试（避免覆盖层被遮挡）
		GlStateManager.disableTexture2D();
		GlStateManager.disableDepth();
		// 设置半透明的白色
		GlStateManager.color(noThinkShootIconBackColorBean.getColor1(), noThinkShootIconBackColorBean.getColor2(),
				noThinkShootIconBackColorBean.getColor3(), noThinkShootIconBackColorBean.getColor4());  // RGB 为 1，透明度为 0.5F

        // 计算冷却进度的大小（从0到1之间）
		int width = 16;
		int height = 16;

        // 使用 Tessellator 直接绘制几何体（避免依赖 Gui 方法）
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder buffer = tess.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        // 定义覆盖层顶点（从底部向上收缩）
		buffer.pos(x, y + height, 0)          .color(noThinkShootIconBackColorBean.getColor1(), noThinkShootIconBackColorBean.getColor2(),
				noThinkShootIconBackColorBean.getColor3(), noThinkShootIconBackColorBean.getColor4()).endVertex(); // 左下
		buffer.pos(x + width, y + height, 0)  .color(noThinkShootIconBackColorBean.getColor1(), noThinkShootIconBackColorBean.getColor2(),
				noThinkShootIconBackColorBean.getColor3(), noThinkShootIconBackColorBean.getColor4()).endVertex(); // 右下
		buffer.pos(x + width, y + cooldownHeight, 0).color(noThinkShootIconBackColorBean.getColor1(), noThinkShootIconBackColorBean.getColor2(),
				noThinkShootIconBackColorBean.getColor3(), noThinkShootIconBackColorBean.getColor4()).endVertex(); // 右上
		buffer.pos(x, y + cooldownHeight, 0)        .color(noThinkShootIconBackColorBean.getColor1(), noThinkShootIconBackColorBean.getColor2(),
				noThinkShootIconBackColorBean.getColor3(), noThinkShootIconBackColorBean.getColor4()).endVertex(); // 左上
		tess.draw();
        // 必须恢复所有 OpenGL 状态！
		GlStateManager.enableTexture2D();  // 重新启用纹理（防止后续 UI 渲染异常）
		GlStateManager.enableDepth();      // 恢复深度测试
		GlStateManager.disableBlend();     // 关闭混合模式
		GlStateManager.color(1, 1, 1, 1);  // 重置颜色为不透明白色

		GlStateManager.popMatrix();
	}
}
