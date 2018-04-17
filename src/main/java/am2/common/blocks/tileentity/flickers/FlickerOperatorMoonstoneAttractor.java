package am2.common.blocks.tileentity.flickers;

import am2.api.affinity.Affinity;
import am2.api.flickers.AbstractFlickerFunctionality;
import am2.api.flickers.IFlickerController;
import am2.api.math.AMVector3;
import am2.common.blocks.BlockArsMagicaOre;
import am2.common.defs.BlockDefs;
import am2.common.defs.ItemDefs;
import am2.common.utils.AffinityShiftUtils;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;

public class FlickerOperatorMoonstoneAttractor extends AbstractFlickerFunctionality{

	@Override
	public int getID ( ) {
		return FLICKERS.MOONSTONEATTRACTOR.getID ( );
	}
	
	public final static FlickerOperatorMoonstoneAttractor instance = new FlickerOperatorMoonstoneAttractor();

	private static final ArrayList<AMVector3> attractors = new ArrayList<AMVector3>();

	public static AMVector3 getMeteorAttractor(AMVector3 target){
		for (AMVector3 attractor : attractors.toArray(new AMVector3[attractors.size()])){
			if (attractor.distanceSqTo(target) <= 16384)
				return attractor.copy();
		}
		return null;
	}

	@Override
	public boolean RequiresPower(){
		return true;
	}

	@Override
	public int PowerPerOperation(){
		return 10;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController<?> habitat, boolean powered){
		AMVector3 vec = new AMVector3((TileEntity)habitat);
		if (powered){
			if (!attractors.contains(vec)){
				attractors.add(vec);
			}
			return true;
		}else{
			attractors.remove(vec);
		}
		return false;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController<?> habitat, boolean powered, Affinity[] flickers){
		return DoOperation(worldObj, habitat, powered);
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController<?> habitat, boolean powered){
		AMVector3 vec = new AMVector3((TileEntity)habitat);
		attractors.remove(vec);
	}

	@Override
	public int TimeBetweenOperation(boolean powered, Affinity[] flickers){
		return 100;
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController<?> habitat, boolean powered, Affinity[] flickers){
		RemoveOperator(worldObj, habitat, powered);
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				"RLR",
				"AME",
				"I T",
				'R', new ItemStack(ItemDefs.rune, 1, EnumDyeColor.ORANGE.getDyeDamage()),
				'L', new ItemStack(ItemDefs.flickerJar, 1, Affinity.LIGHTNING.getID()),
				'A', new ItemStack(ItemDefs.flickerJar, 1, Affinity.ARCANE.getID()),
				'E', new ItemStack(ItemDefs.flickerJar, 1, Affinity.EARTH.getID()),
				'M', new ItemStack(BlockDefs.ores, 1, BlockArsMagicaOre.EnumOreType.MOONSTONE.ordinal()),
				'I', AffinityShiftUtils.getEssenceForAffinity(Affinity.AIR),
				'T', AffinityShiftUtils.getEssenceForAffinity(Affinity.EARTH)
		};
	}
	
	@Override
	public ResourceLocation getTexture() {
		return new ResourceLocation("arsmagica2", "FlickerOperatorMoonstoneAttractor");
	}

	@Override
	public Affinity[] getMask() {
		return new Affinity[] {Affinity.LIGHTNING, Affinity.ARCANE, Affinity.EARTH};
	}


}
