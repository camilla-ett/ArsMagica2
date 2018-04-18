package am2.common.blocks;

import am2.common.registry.Registry;
import net.minecraft.util.ResourceLocation;

public class BlockWitchwoodSlabsSimple extends BlockWitchwoodSlabs {
	
	public BlockWitchwoodSlabsSimple() {}
	
	@Override
	public boolean isDouble() {
		return false;
	}

	public BlockWitchwoodSlabsSimple registerAndName(ResourceLocation rl) {
		this.setUnlocalizedName(rl.toString());
		this.setRegistryName(rl);
		Registry.GetBlocksToRegister().add(this);
		return this;
	}

}
