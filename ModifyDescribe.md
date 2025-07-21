

# Modifications to Firearms and Armor Base Attributes

```
To adapt to RLCraft's monster stats and dungeon difficulty, base values of firearms and armor in `TGuns.class` and `TGArmors.class` have been modified.  
```

# Changes to Firearm Damage Calculation

```
Many monsters in RLCraft possess extremely high armor values, causing Techguns' original damage calculations to perform poorly. Therefore, the firearm damage algorithm has been rewritten, with armor penetration as a critical factor. Tungsten ore (spawning in deeper layers of the Overworld) and tungsten ingots have been added to craft armor-piercing rounds, improving mid-game penetration capabilities. Detailed armor penetration calculations are in `ArmorReductionDamageHandler.class`. Additionally, firearm quality modifiers and enchantments now grant extra armor penetration.  
```

# New Ammunition Types

```
After rebalancing firearm DPS through damage and fire rate adjustments, early-game weapons like the Hand Cannon and Revolver received lower base damage (as their original values were unrealistically close to rifles like the M4/AK47). To compensate, new variant ammunition types were added:  
- **Hand Cannon**: Granite, Sandstone, Diorite, Obsidian, and Diamond bullets (boosting damage/penetration). A Titanium Hand Cannon is planned for late-game synergy with the "Ember Barrel" enchantment (grants crit rate/damage boosts when firing the last round in a magazine).  
- **Revolver**: "Power Pistol Rounds" inspired by *Resident Evil* (+60% damage and higher penetration, crafted with more resources).  
- **Modern firearms**: Universal armor-piercing rounds (+25% penetration).  
```

# New Firearm Skills for Reskillable

```
Added 5 firearm-exclusive skills under Reskillable's Combat branch:  
- 3-tiered "Lifesteal on Kill"  
- "Explosive Kill" (chance to trigger explosions)  
- "Critical Bullet Damage" (adds crit chance).  
*Note: Currently implemented via modified Reskillable source code.*
```

# Miscellaneous Changes

```
1. **New firearm modifiers**: Weapon Tier (5 static tiers), Extra Crit Rate/Damage, Durability, etc.  
2. **Late-game AOE adjustments**:  
   - Added "Explosive Kill" skill (scales with weapon tier/enchantments).  
   - PDW/Pulse Rifle: Bullets ricochet between targets (expandable via enchantments).  
   - Gauss Rifle/Nuclear Deathray: Penetrating rounds deal line damage.  
   - TFG: Area-of-effect explosions (becomes a late-game powerhouse with RLCraft gear).  
3. **Fire mechanics**:  
   - Mid/late-game weapons (e.g., Laser Barrel guns, Nuclear Deathray, TFG) ignite targets.  
   - PDW/Pulse Rifle/Gauss Rifle: Added incendiary ammo.  
   - Flame Glove accessory triggers ignitions on bullet hits.  
4. **Armor changes**:  
   - Techguns armor now supports vanilla enchanting.  
   - Removed machine-based armor enchantment recipes.  
   - Disabled multi-damage resistance modifiers for RLCraft balance.  
5. **Ore generation**: Titanium/Uranium now spawn in the Nether (textures/names updated).  
6. **Bullet mechanics**: Shatter glass/ice.  
7. **New variant**: Golden Revolver (Dictator), obtained from Dictator Steve's loot (higher damage/penetration).  

Apologies for the lack of detailed documentation during development, as initial changes were experimental to improve Techguns' viability in RLCraft. Many minor adjustments are now undocumented.  
```

# Firearm Durability Overhaul

```
- Firearms now lose durability (displayed via a dedicated HUD stat).  
- Durability cannot drop below 5%. At 60%/40%/20%, penalties apply (reduced damage, accuracy, range, penetration).  
- Enchantments (Unbreaking, Mending) supported.  
- Future plans: Introduce jamming/backfire mechanics instead of stat penalties.  
```

# Durability Repair & Quality Tools Integration

```
- Repaired via anvil using materials:  
  - Hand Cannon: Stone  
  - Others: Mechanical Parts (new Gold/Steel/Titanium parts added; recipes rebalanced).  
- Quality Tools compatibility: Reforging firearms requires matching mechanical parts.  
```

# New Firearm Enchantments

```
- Designed for RLCraft's progression:  
  - Low-tier: Obtainable via Enchanting Table.  
  - High-tier: Requires feeding Book Wyrms (treasure enchants need Golden Book Wyrms).  
  - Ideally, rare enchants would spawn in RLCraft chests/villagers, but currently drop from Techguns mobs.  
```

# Techguns Mob Adjustments

```
1. Buffed stats: HP, armor, firearm damage.  
2. Mobs' guns now randomly spawn with enchantments (droppable via RLCraft's loot system).  
3. Updated loot tables: More ammo types/quantities + RLCraft items (Spectral Silts, Heart Crystals, trinkets).  
```

# Cross-Mod Integration

```
1. **Quality Tools**: Firearms have tool tiers reforged via mechanical parts.  
2. **ArmorSuit**: Golden/Dragonbone Armor boosts Golden/Dictator Revolvers; compatible with Reskillable's repair skills.  
3. **Magic synergy**:  
   - "Arcane Bullets" enchant converts damage to magic (scales with RLCraft's magic buffs but disables crits).  
   - Bio Gun deals magic damage by default.  
4. **Ice and Fire**: Fixed hitbox detection for dragons/hydras.  
5. **Level Up 2**:  
   - Random Crit skill: +2.5% crit rate per level.  
   - Sword Damage skill: +5% firearm base damage.  
6. **Attribute scaling**:  
   - `ATTACK_DAMAGE` boosts bullet damage.  
   - `ATTACK_SPEED` increases projectile velocity.  
7. **Trinket compatibility**: Adapted for Poison Stone/Wither Ring.  
8. **Crit events**: Trigger RLCraft items like Fury Pendant.  
```

# Recipe & Material Updates

```
- Overhauled Techguns recipes (some require modded materials via CraftTweaker).  
- Added unique items:  
  - Titanium Blade Fragment (crafted with Parasite's Infected Blade + Titanium) for Gauss Rifle barrels.  
```
