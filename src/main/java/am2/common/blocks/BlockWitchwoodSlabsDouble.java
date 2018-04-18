package am2.common.blocks;

import am2.common.registry.Registry;
import net.minecraft.util.ResourceLocation;

public class BlockWitchwoodSlabsDouble extends BlockWitchwoodSlabs {
	
	public BlockWitchwoodSlabsDouble() {}
	
	@Override
	public boolean isDouble() {
		return true;
	}

	public BlockWitchwoodSlabsDouble registerAndName(ResourceLocation rl) {
		this.setUnlocalizedName(rl.toString());
		this.setRegistryName(rl);
		Registry.GetBlocksToRegister().add(this);
		return this;
	}

}
