package techguns.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import techguns.TGBlocks;
import techguns.TGConfig;
import techguns.blocks.EnumOreType;

import java.util.Random;

public class NetherOreGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if (world.provider.getDimension() == -1) {
			if (TGConfig.doOreGenUranium) {
				this.addOreSpawn(TGBlocks.TG_ORE.getDefaultState().withProperty(TGBlocks.TG_ORE.ORE_TYPE, EnumOreType.ORE_URANIUM), world, random, chunkX * 16, chunkZ * 16, 16, 16, 3 + random.nextInt(2), 5, 70, 120);
			}
			if (TGConfig.doOreGenTitanium) {
				this.addOreSpawn(TGBlocks.TG_ORE.getDefaultState().withProperty(TGBlocks.TG_ORE.ORE_TYPE, EnumOreType.ORE_TITANIUM), world, random, chunkX * 16, chunkZ * 16, 16, 16, 4 + random.nextInt(2), 6, 15, 90);
			}
		}
	}

	public void addOreSpawn(IBlockState blockstate, World world, Random random, int blockXPos, int blockZPos, int maxX, int maxZ, int maxVeinSize, int chancesToSpawn, int minY, int maxY)
	{
		int diffBtwnMinMaxY = maxY - minY;
		for (int x = 0; x < chancesToSpawn; x++)
		{
			int posX = blockXPos + random.nextInt(maxX);
			int posY = minY + random.nextInt(diffBtwnMinMaxY);
			int posZ = blockZPos + random.nextInt(maxZ);
			new WorldGenMinable(blockstate, maxVeinSize, BlockMatcher.forBlock(Blocks.NETHERRACK)).generate(world, random, new BlockPos(posX, posY, posZ));
		}
	}
}
