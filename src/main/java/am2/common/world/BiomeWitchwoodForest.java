package am2.common.world;

import am2.ArsMagica2;
import am2.common.entity.EntityDryad;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;

public class BiomeWitchwoodForest extends Biome{

	public static final Biome instance = new BiomeWitchwoodForest(new BiomeProperties("WitchwoodForest"));
	private static final WitchwoodTreeHuge hugeTree = new WitchwoodTreeHuge(true);
	private static final WitchwoodTreeSmall smallTree = new WitchwoodTreeSmall(true);
	private static int biomeId;
	private static BiomeDecorator decorator;

	public BiomeWitchwoodForest(BiomeProperties par1){
		super(par1);
		this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 5, 4, 4));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityDryad.class, 5, 4, 4));
		decorator = this.createBiomeDecorator();
		decorator.treesPerChunk = 10;
		decorator.grassPerChunk = 4;
		decorator.flowersPerChunk = 10;
		biomeId = ArsMagica2.config.getWitchwoodForestID();
	}

	@Override
	public int getWaterColorMultiplier(){
		return 0x0a2a72;
	}
	
	@Override
	public int getFoliageColorAtPos(BlockPos pos) {
		return 0xdbe6e5;
	}
	
	@Override
	public int getGrassColorAtPos(BlockPos pos) {
		return 0xdbe6e5;
	}

	@Override
	public int getSkyColorByTemp(float par1){
		return 0x6699ff;
	}

	@Override
	public void decorate(World worldIn, Random rand, BlockPos pos)
	{
		this.decorator.decorate(worldIn, rand, this, pos);
	}


	public static int getBiomeId()
	{
		return biomeId;
	}

	public static int getNextFreeBiomeId() {
		for (int i = 0; i < 256; i++) {
			if (Biome.getBiome(i) != null) {
				if (i == 255) throw new IllegalArgumentException("No more biome ids are avaliable");
				continue;
			}
			return i;
		}
		return -1;
	}
	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random rand)
	{
		return rand.nextInt(10) == 0 ? hugeTree : smallTree;
	}

}
