package am2.common.items;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//class to identify items that are part of the Ars Magica mod, mainly used for rendering
public class ItemArsMagicaRotated extends ItemArsMagica{

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D(){
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldRotateAroundWhenRendering(){
		return true;
	}
}
