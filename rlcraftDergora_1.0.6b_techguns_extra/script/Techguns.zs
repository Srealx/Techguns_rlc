import mods.techguns.MetalPress; 
import mods.techguns.ChemLab; 
import mods.techguns.Fabricator;
import mods.techguns.ReactionChamber;


# 给机械零件添加ore
<ore:machineryIron>.add(<techguns:itemshared:57>);
<ore:machinerySteel>.add(<techguns:itemshared:178>);
<ore:machineryObsidianSteel>.add(<techguns:itemshared:58>);
<ore:machineryCarbon>.add(<techguns:itemshared:59>);
<ore:machineryGold>.add(<techguns:itemshared:180>);
<ore:machineryTitanium>.add(<techguns:itemshared:181>);
<ore:cyberneticParts>.add(<techguns:itemshared:69>);
<ore:plateObsidianSteel>.add(<techguns:itemshared:51>);

# 爆破霰弹配方
recipes.addShaped(<techguns:scatterbeamrifle>.withTag({ammo: 8 as short, ammovariant: "default"}),
 [[<techguns:itemshared:41>,<techguns:itemshared:66>,<minecraft:glass_pane>],
  [<techguns:itemshared:41>,<techguns:itemshared:36>,<techguns:itemshared:44>],
  [null,null,<techguns:itemshared:29>]]);
recipes.addShaped(<techguns:scatterbeamrifle>.withTag({ammo: 0 as short, ammovariant: "default"}),
 [[<techguns:itemshared:41>,<techguns:itemshared:66>,<minecraft:glass_pane>],
  [<techguns:itemshared:41>,<techguns:itemshared:36>,<techguns:itemshared:44>],
  [null,null,<techguns:itemshared:30>]]);

# 砂岩子弹配方
recipes.addShapeless(<techguns:itemshared:159>,[<minecraft:sandstone:1>,<minecraft:sandstone:1>,<minecraft:sandstone:1>,<minecraft:gunpowder>]);

# 线圈配方
recipes.remove(<techguns:itemshared:68>);
recipes.addShaped(<techguns:itemshared:68>,
[[null,<techguns:itemshared:63>,<techguns:itemshared:83>],
[<techguns:itemshared:63>,<minecraft:gold_ingot>,<techguns:itemshared:63>],
[<techguns:itemshared:83>,<techguns:itemshared:63>,null]]);

# 特斯拉配方(需要电龙血)
recipes.remove(<techguns:teslagun>);
recipes.remove(<techguns:teslagun:20>);
recipes.addShaped(<techguns:teslagun>.withTag({ammo: 20 as short, ammovariant: "default"}), 
[[null,<minecraft:glass_pane>,<minecraft:redstone_block>],
[<techguns:itemshared:68>,<techguns:itemshared:36>,<techguns:itemshared:44>],
[null,<iceandfire:lightning_dragon_blood>,<techguns:itemshared:29>]]);
recipes.addShaped(<techguns:teslagun>.withTag({ammo: 0 as short, ammovariant: "default"}), 
[[null,<minecraft:glass_pane>,<minecraft:redstone_block>],
[<techguns:itemshared:68>,<techguns:itemshared:36>,<techguns:itemshared:44>],
[null,<iceandfire:lightning_dragon_blood>,<techguns:itemshared:30>]]);

# 喷火器配方
recipes.remove(<techguns:flamethrower>);
recipes.remove(<techguns:flamethrower:100>);
recipes.addShaped(<techguns:flamethrower>.withTag({ammo: 50 as short, ammovariant: "default"}),
[[null,null,null],
[<techguns:itemshared:72>,<techguns:itemshared:35>,<techguns:itemshared:43>],
[<iceandfire:fire_dragon_blood>,<techguns:itemshared:27>,null]]);
recipes.addShaped(<techguns:flamethrower>.withTag({ammo: 0 as short, ammovariant: "default"}),
[[null,null,null],
[<techguns:itemshared:72>,<techguns:itemshared:35>,<techguns:itemshared:43>],
[<iceandfire:fire_dragon_blood>,<techguns:itemshared:28>,null]]);

# 删除核弹配方
recipes.remove(<techguns:itemshared:118>);
recipes.remove(<techguns:itemshared:117>);
# 删除装甲升级配方
recipes.remove(<techguns:itemshared:147>);
recipes.remove(<techguns:itemshared:148>);
recipes.remove(<techguns:itemshared:149>);
recipes.remove(<techguns:itemshared:150>);
recipes.remove(<techguns:itemshared:151>);
recipes.remove(<techguns:itemshared:152>);
recipes.remove(<techguns:itemshared:153>);
recipes.remove(<techguns:itemshared:154>);
recipes.remove(<techguns:itemshared:155>);

# 碳纤维配方
ChemLab.removeRecipe(<techguns:itemshared:64>,null);
MetalPress.addRecipe(<minecraft:diamond>,<iceandfire:dragonbone>,<techguns:itemshared:64>,true);

# 给龙鳞添加oreDict
<ore:dragonScales>.add(<iceandfire:dragonscales_green>);
<ore:dragonScales>.add(<iceandfire:dragonscales_red>);
<ore:dragonScales>.add(<iceandfire:dragonscales_bronze>);
<ore:dragonScales>.add(<iceandfire:dragonscales_gray>);
<ore:dragonScales>.add(<iceandfire:dragonscales_blue>);
<ore:dragonScales>.add(<iceandfire:dragonscales_white>);
<ore:dragonScales>.add(<iceandfire:dragonscales_sapphire>);
<ore:dragonScales>.add(<iceandfire:dragonscales_silver>);
<ore:dragonScales>.add(<iceandfire:dragonscales_copper>);
<ore:dragonScales>.add(<iceandfire:dragonscales_black>);
# 电龙材料
<ore:dragonScales>.add(<iceandfire:dragonscales_amethyst>);
<ore:dragonScales>.add(<iceandfire:dragonscales_electric>);

# 碳纤维材料
recipes.remove(<techguns:itemshared:59>);
MetalPress.removeRecipe(<techguns:itemshared:59>);
# 碳纤维零件
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_green>,1,<iceandfire:sea_serpent_scales_blue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_red>,1,<iceandfire:sea_serpent_scales_blue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_bronze>,1,<iceandfire:sea_serpent_scales_blue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_gray>,1,<iceandfire:sea_serpent_scales_blue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_blue>,1,<iceandfire:sea_serpent_scales_blue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_white>,1,<iceandfire:sea_serpent_scales_blue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_sapphire>,1,<iceandfire:sea_serpent_scales_blue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_silver>,1,<iceandfire:sea_serpent_scales_blue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_copper>,1,<iceandfire:sea_serpent_scales_blue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_black>,1,<iceandfire:sea_serpent_scales_blue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);

Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_green>,1,<iceandfire:sea_serpent_scales_bronze>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_red>,1,<iceandfire:sea_serpent_scales_bronze>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_bronze>,1,<iceandfire:sea_serpent_scales_bronze>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_gray>,1,<iceandfire:sea_serpent_scales_bronze>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_blue>,1,<iceandfire:sea_serpent_scales_bronze>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_white>,1,<iceandfire:sea_serpent_scales_bronze>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_sapphire>,1,<iceandfire:sea_serpent_scales_bronze>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_silver>,1,<iceandfire:sea_serpent_scales_bronze>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_copper>,1,<iceandfire:sea_serpent_scales_bronze>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_black>,1,<iceandfire:sea_serpent_scales_bronze>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);

Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_green>,1,<iceandfire:sea_serpent_scales_deepblue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_red>,1,<iceandfire:sea_serpent_scales_deepblue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_bronze>,1,<iceandfire:sea_serpent_scales_deepblue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_gray>,1,<iceandfire:sea_serpent_scales_deepblue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_blue>,1,<iceandfire:sea_serpent_scales_deepblue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_white>,1,<iceandfire:sea_serpent_scales_deepblue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_sapphire>,1,<iceandfire:sea_serpent_scales_deepblue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_silver>,1,<iceandfire:sea_serpent_scales_deepblue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_copper>,1,<iceandfire:sea_serpent_scales_deepblue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_black>,1,<iceandfire:sea_serpent_scales_deepblue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);

Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_green>,1,<iceandfire:sea_serpent_scales_green>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_red>,1,<iceandfire:sea_serpent_scales_green>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_bronze>,1,<iceandfire:sea_serpent_scales_green>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_gray>,1,<iceandfire:sea_serpent_scales_green>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_blue>,1,<iceandfire:sea_serpent_scales_green>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_white>,1,<iceandfire:sea_serpent_scales_green>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_sapphire>,1,<iceandfire:sea_serpent_scales_green>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_silver>,1,<iceandfire:sea_serpent_scales_green>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_copper>,1,<iceandfire:sea_serpent_scales_green>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_black>,1,<iceandfire:sea_serpent_scales_green>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);

Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_green>,1,<iceandfire:sea_serpent_scales_purple>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_red>,1,<iceandfire:sea_serpent_scales_purple>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_bronze>,1,<iceandfire:sea_serpent_scales_purple>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_gray>,1,<iceandfire:sea_serpent_scales_purple>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_blue>,1,<iceandfire:sea_serpent_scales_purple>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_white>,1,<iceandfire:sea_serpent_scales_purple>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_sapphire>,1,<iceandfire:sea_serpent_scales_purple>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_silver>,1,<iceandfire:sea_serpent_scales_purple>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_copper>,1,<iceandfire:sea_serpent_scales_purple>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_black>,1,<iceandfire:sea_serpent_scales_purple>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);

Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_green>,1,<iceandfire:sea_serpent_scales_red>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_red>,1,<iceandfire:sea_serpent_scales_red>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_bronze>,1,<iceandfire:sea_serpent_scales_red>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_gray>,1,<iceandfire:sea_serpent_scales_red>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_blue>,1,<iceandfire:sea_serpent_scales_red>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_white>,1,<iceandfire:sea_serpent_scales_red>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_sapphire>,1,<iceandfire:sea_serpent_scales_red>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_silver>,1,<iceandfire:sea_serpent_scales_red>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_copper>,1,<iceandfire:sea_serpent_scales_red>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_black>,1,<iceandfire:sea_serpent_scales_red>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);

Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_green>,1,<iceandfire:sea_serpent_scales_teal>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_red>,1,<iceandfire:sea_serpent_scales_teal>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_bronze>,1,<iceandfire:sea_serpent_scales_teal>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_gray>,1,<iceandfire:sea_serpent_scales_teal>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_blue>,1,<iceandfire:sea_serpent_scales_teal>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_white>,1,<iceandfire:sea_serpent_scales_teal>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_sapphire>,1,<iceandfire:sea_serpent_scales_teal>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_silver>,1,<iceandfire:sea_serpent_scales_teal>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_copper>,1,<iceandfire:sea_serpent_scales_teal>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_black>,1,<iceandfire:sea_serpent_scales_teal>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
# 电龙材料
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_amethyst>,1,<iceandfire:sea_serpent_scales_blue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_electric>,1,<iceandfire:sea_serpent_scales_blue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_amethyst>,1,<iceandfire:sea_serpent_scales_bronze>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_electric>,1,<iceandfire:sea_serpent_scales_bronze>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_amethyst>,1,<iceandfire:sea_serpent_scales_deepblue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_electric>,1,<iceandfire:sea_serpent_scales_deepblue>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_amethyst>,1,<iceandfire:sea_serpent_scales_green>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_electric>,1,<iceandfire:sea_serpent_scales_green>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_amethyst>,1,<iceandfire:sea_serpent_scales_purple>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_electric>,1,<iceandfire:sea_serpent_scales_purple>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_amethyst>,1,<iceandfire:sea_serpent_scales_red>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_electric>,1,<iceandfire:sea_serpent_scales_red>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_amethyst>,1,<iceandfire:sea_serpent_scales_teal>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);
Fabricator.addRecipe(<techguns:itemshared:64>,1,<iceandfire:dragonscales_electric>,1,<iceandfire:sea_serpent_scales_teal>,1,<iceandfire:witherbone>,1,<techguns:itemshared:59>);

# 钛制零件
recipes.remove(<techguns:itemshared:181>);
Fabricator.addRecipe(<techguns:itemshared:85>,1,<srparasites:ada_reeker_drop>,1,<srparasites:ada_longarms_drop>,1,<minecraft:quartz_block:*>,1,<techguns:itemshared:181>);

# 硬质零件
MetalPress.removeRecipe(<techguns:itemshared:58>);
MetalPress.addRecipe(<techguns:itemshared:51>,<iceandfire:dragonbone>,<techguns:itemshared:58>,true);

# 精英电路板
recipes.remove(<techguns:itemshared:66>);
recipes.addShapeless(<techguns:itemshared:66>,[<techguns:itemshared:65>,<ore:dragonScales>,<ore:dragonScales>]);

  # 等离子体发生器
recipes.remove(<techguns:itemshared:131>);
recipes.addShaped(<techguns:itemshared:131>,
 [[<techguns:itemshared:52>,<srparasites:living_core>,<techguns:itemshared:52>],
  [<techguns:itemshared:98>,<techguns:itemshared:92>,<techguns:itemshared:98>],
  [<techguns:itemshared:52>,<techguns:itemshared:68>,<techguns:itemshared:52>]]);
  # 高斯步枪枪管
recipes.remove(<techguns:itemshared:128>);
recipes.addShaped(<techguns:itemshared:128>,
[[<techguns:itemshared:54>,<techguns:itemshared:63>,<techguns:itemshared:54>],
  [<techguns:itemshared:40>,<srparasites:living_core>,<techguns:itemshared:40>],
  [<techguns:itemshared:54>,<techguns:itemshared:63>,<techguns:itemshared:54>]]);
# 辐射发射器
Fabricator.removeRecipe(<techguns:itemshared:73>);
Fabricator.addRecipe(<techguns:itemshared:98>,1,<techguns:itemshared:66>,2,<srparasites:living_core>,1,<techguns:itemshared:52>,2,<techguns:itemshared:73>);

# 高斯步枪
recipes.remove(<techguns:gaussrifle>);
recipes.remove(<techguns:gaussrifle:8>);
recipes.addShaped(<techguns:gaussrifle>.withTag({ammo: 0 as short, ammovariant: "default"}),
 [[null,<techguns:itemshared:54>,<techguns:itemshared:66>],
  [<techguns:itemshared:128>,<techguns:itemshared:130>,<techguns:itemshared:44>],
  [null,null,<techguns:itemshared:30>]]);
  recipes.addShaped(<techguns:gaussrifle>.withTag({ammo: 8 as short, ammovariant: "default"}),
 [[null,<techguns:itemshared:54>,<techguns:itemshared:66>],
  [<techguns:itemshared:128>,<techguns:itemshared:130>,<techguns:itemshared:44>],
  [null,<techguns:itemshared:127>,<techguns:itemshared:29>]]);

# 核死光
recipes.remove(<techguns:nucleardeathray>);
recipes.remove(<techguns:nucleardeathray:50>);
recipes.addShaped(<techguns:nucleardeathray>.withTag({ammo: 0 as short, ammovariant: "default"}),
 [[<techguns:itemshared:54>,<techguns:itemshared:52>,<techguns:itemshared:54>],
  [<techguns:itemshared:73>,<techguns:itemshared:130>,<techguns:itemshared:44>],
  [null,<techguns:itemshared:32>,null]]);
recipes.addShaped(<techguns:nucleardeathray>.withTag({ammo: 50 as short, ammovariant: "default"}),
 [[<techguns:itemshared:54>,<techguns:itemshared:52>,<techguns:itemshared:54>],
  [<techguns:itemshared:73>,<techguns:itemshared:130>,<techguns:itemshared:44>],
  [null,<techguns:itemshared:31>,null]]);

# 生物枪
recipes.remove(<techguns:biogun>);
recipes.remove(<techguns:biogun:25>);
recipes.addShaped(<techguns:biogun>.withTag({ammo: 0 as short, ammovariant: "default"}),
 [
  [null,null,null],
  [<techguns:itemshared:72>,<techguns:itemshared:35>,<techguns:itemshared:43>],
  [<iceandfire:hydra_fang>,<techguns:itemshared:26>,null]
  ]);
recipes.addShaped(<techguns:biogun>.withTag({ammo: 25 as short, ammovariant: "default"}),
 [
  [null,null,null],
  [<techguns:itemshared:72>,<techguns:itemshared:35>,<techguns:itemshared:43>],
  [<iceandfire:hydra_fang>,<techguns:itemshared:25>,null]
  ]);

# TGX
ChemLab.removeRecipe(<techguns:itemshared:75>,null);
Fabricator.addRecipe(<minecraft:dye:4>,1,<minecraft:diamond>,1,<minecraft:gunpowder>,1,<techguns:itemshared:96>,1,<techguns:itemshared:75>);

# 钛质剑刃碎片
ChemLab.addRecipe(<techguns:itemshared:85>,1,<srparasites:infectious_blade_fragment>,1,<liquid:creeper_acid>*100,true,<techguns:itemshared:182>,<liquid:lava>*0,40);

# 高斯步枪弹药
MetalPress.removeRecipe(<techguns:itemshared:127>*8);
MetalPress.addRecipe(<techguns:itemshared:54>,<techguns:itemshared:182>,<techguns:itemshared:127>*8,true);

# 核动力能量单元: 耗尽
Fabricator.removeRecipe(<techguns:itemshared:32>);
Fabricator.addRecipe(<techguns:itemshared:83>,1,<techguns:itemshared:66>,1,<srparasites:vile_shell>,1,<techguns:itemshared:52>,2,<techguns:itemshared:32>);

# 制造室
recipes.remove(<techguns:multiblockmachine>);
recipes.addShaped(<techguns:multiblockmachine>,[
  [<techguns:itemshared:50>,<techguns:itemshared:58>,<techguns:itemshared:50>],
  [<techguns:itemshared:69>,<techguns:itemshared:70>,<techguns:itemshared:69>],
  [<techguns:itemshared:50>,<techguns:itemshared:66>,<techguns:itemshared:50>]
]);

# 激光焦点
ReactionChamber.removeRecipe(<minecraft:diamond>,<liquid:lava>);
Fabricator.addRecipe(<minecraft:diamond>,1,<techguns:itemshared:63>,1,<iceandfire:fire_dragon_blood>,1,<techguns:itemshared:55>,1,<techguns:itemshared:71>);

# 镭射枪管
recipes.remove(<techguns:itemshared:41>);
recipes.addShaped(<techguns:itemshared:41>,[
  [<ore:ingotGold>,<ore:ingotGold>,<ore:ingotGold>],
  [<ore:blockGlass>,<ore:blockGlass>,<techguns:itemshared:71>],
  [<ore:ingotGold>,<ore:ingotGold>,<ore:ingotGold>]
]);

# 爬行者酸液
ChemLab.removeRecipe(null,<liquid:creeper_acid>*1000);
ChemLab.addRecipe(<iceandfire:fire_dragon_heart>,1,<minecraft:skull:4>,1,<lycanitesmobs:bucketsharacid>,<liquid:water>*1000,true,<minecraft:bucket>,<liquid:creeper_acid>*2000,35);
ChemLab.addRecipe(<iceandfire:fire_dragon_heart>,1,<iceandfire:hydra_fang>,1,<lycanitesmobs:bucketsharacid>,<liquid:water>*1000,true,<minecraft:bucket>,<liquid:creeper_acid>*2000,35);
ChemLab.addRecipe(<iceandfire:ice_dragon_heart>,1,<minecraft:skull:4>,1,<lycanitesmobs:bucketsharacid>,<liquid:water>*1000,true,<minecraft:bucket>,<liquid:creeper_acid>*2000,35);
ChemLab.addRecipe(<iceandfire:ice_dragon_heart>,1,<iceandfire:hydra_fang>,1,<lycanitesmobs:bucketsharacid>,<liquid:water>*1000,true,<minecraft:bucket>,<liquid:creeper_acid>*2000,35);
ChemLab.addRecipe(<iceandfire:lightning_dragon_heart>,1,<minecraft:skull:4>,1,<lycanitesmobs:bucketsharacid>,<liquid:water>*1000,true,<minecraft:bucket>,<liquid:creeper_acid>*2000,35);
ChemLab.addRecipe(<iceandfire:lightning_dragon_heart>,1,<iceandfire:hydra_fang>,1,<lycanitesmobs:bucketsharacid>,<liquid:water>*1000,true,<minecraft:bucket>,<liquid:creeper_acid>*2000,35);