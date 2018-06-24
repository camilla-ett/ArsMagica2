package am2.common.blocks.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Lists;

import am2.ArsMagica2;
import am2.api.CraftingAltarMaterials;
import am2.api.SpellRegistry;
import am2.api.blocks.IMultiblock;
import am2.api.blocks.IMultiblockController;
import am2.api.blocks.IMultiblockGroup;
import am2.api.blocks.Multiblock;
import am2.api.blocks.MultiblockGroup;
import am2.api.blocks.TypedMultiblockGroup;
import am2.api.extensions.ISpellCaster;
import am2.api.power.IPowerNode;
import am2.api.spell.AbstractSpellPart;
import am2.client.particles.AMParticle;
import am2.client.particles.ParticleFadeOut;
import am2.client.particles.ParticleMoveOnHeading;
import am2.common.blocks.BlockLectern;
import am2.common.defs.BlockDefs;
import am2.common.defs.ItemDefs;
import am2.common.packet.AMDataReader;
import am2.common.packet.AMDataWriter;
import am2.common.packet.AMNetHandler;
import am2.common.packet.AMPacketIDs;
import am2.common.power.PowerNodeRegistry;
import am2.common.power.PowerTypes;
import am2.common.spell.SpellCaster;
import am2.common.spell.component.Summon;
import am2.common.spell.shape.Binding;
import am2.common.utils.KeyValuePair;
import am2.common.utils.NBTUtils;
import am2.common.utils.SpellUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockLever.EnumOrientation;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStairs.EnumHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityCraftingAltar extends TileEntityAMPower implements IMultiblockController, ITileEntityAMBase {

	private IMultiblock primary = new Multiblock("craftingAltar_alt");
	private IMultiblock secondary = new Multiblock("craftingAltar");
	
	private TypedMultiblockGroup out;
	private TypedMultiblockGroup out_alt;
	private TypedMultiblockGroup catalysts;
	private TypedMultiblockGroup catalysts_alt;
	private HashMap<IBlockState, Integer> capsPower = new HashMap<>();
	private HashMap<IBlockState, Integer> structurePower = new HashMap<>();
	
	private static final int BLOCKID = 0;
	private static final int STAIR_NORTH = 1;
	private static final int STAIR_SOUTH = 2;
	private static final int STAIR_EAST = 3;
	private static final int STAIR_WEST = 4;
	private static final int STAIR_NORTH_INVERTED = 5;
	private static final int STAIR_SOUTH_INVERTED = 6;
	private static final int STAIR_EAST_INVERTED = 7;
	private static final int STAIR_WEST_INVERTED = 8;
	

	private boolean isCrafting;
	private final ArrayList<ItemStack> allAddedItems;
	private final ArrayList<ItemStack> currentAddedItems;

	private final ArrayList<AbstractSpellPart> spellDef;
	private final NBTTagCompound savedData = new NBTTagCompound();
	private final ArrayList<KeyValuePair<ArrayList<AbstractSpellPart>, NBTTagCompound>> shapeGroups;
//	private boolean allShapeGroupsAdded = false;

	private int currentKey = -1;
	private int checkCounter;
	private boolean structureValid;
	private BlockPos podiumLocation;
	private BlockPos switchLocation;
	private int maxEffects;
	private boolean dirty = false;

	private ItemStack addedPhylactery = null;
	private ItemStack addedBindingCatalyst = null;

	private ItemStack[] spellGuide;
	private int[] outputCombo;
	private int[][] shapeGroupGuide;

	private int currentConsumedPower = 0;
	private int ticksExisted = 0;
	private ArrayList<PowerTypes> currentMainPowerTypes = Lists.newArrayList();
	
	private static final byte CRAFTING_CHANGED = 1;
	private static final byte COMPONENT_ADDED = 2;
	private static final byte FULL_UPDATE = 3;

//	private static final int augmatl_mutex = 2;
//	private static final int lectern_mutex = 4;

	private String currentSpellName = "";
	private IBlockState mimicState;

	public TileEntityCraftingAltar(){
		super(500);
		setupMultiblock();
		allAddedItems = new ArrayList<ItemStack>();
		currentAddedItems = new ArrayList<ItemStack>();
		isCrafting = false;
		structureValid = false;
		checkCounter = 0;
		setNoPowerRequests();
		maxEffects = 2;

		spellDef = new ArrayList<>();
		shapeGroups = new ArrayList<>();

		for (int i = 0; i < 5; ++i){
			shapeGroups.add(new KeyValuePair<ArrayList<AbstractSpellPart>, NBTTagCompound>(new ArrayList<>(), new NBTTagCompound()));
		}
	}
	
	private HashMap<Integer, IBlockState> createStateMap(IBlockState block, IBlockState stairs) {
		HashMap<Integer, IBlockState> map = new HashMap<>();
		map.put(BLOCKID, block);
		map.put(STAIR_NORTH, stairs.withProperty(BlockStairs.FACING, EnumFacing.NORTH));
		map.put(STAIR_SOUTH, stairs.withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
		map.put(STAIR_EAST, stairs.withProperty(BlockStairs.FACING, EnumFacing.EAST));
		map.put(STAIR_WEST, stairs.withProperty(BlockStairs.FACING, EnumFacing.WEST));
		map.put(STAIR_NORTH_INVERTED, stairs.withProperty(BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, EnumHalf.TOP));
		map.put(STAIR_SOUTH_INVERTED, stairs.withProperty(BlockStairs.FACING, EnumFacing.SOUTH).withProperty(BlockStairs.HALF, EnumHalf.TOP));
		map.put(STAIR_EAST_INVERTED, stairs.withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, EnumHalf.TOP));
		map.put(STAIR_WEST_INVERTED, stairs.withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, EnumHalf.TOP));
		return map;
	}
	
	private void setupMultiblock(){
		ArrayList<HashMap<Integer, IBlockState>> structureMaterials = new ArrayList<>();
		for (Entry<KeyValuePair<IBlockState, IBlockState>, Integer> entry : CraftingAltarMaterials.getMainMap().entrySet()) {
			structureMaterials.add(createStateMap(entry.getKey().key, entry.getKey().value));
			structurePower.put(entry.getKey().key, entry.getValue().intValue());
		}
		
		ArrayList<HashMap<Integer, IBlockState>> capsMaterials = new ArrayList<>();
		for (Entry<IBlockState, Integer> entry : CraftingAltarMaterials.getCapsMap().entrySet()) {
			HashMap<Integer, IBlockState> capMat = new HashMap<>();
			capMat.put(0, entry.getKey());
			capsMaterials.add(capMat);
			capsPower.put(entry.getKey(), entry.getValue().intValue());
		}
		
		catalysts = new TypedMultiblockGroup("catalysts", capsMaterials, false);
		out = new TypedMultiblockGroup("out", structureMaterials, false);
		MultiblockGroup altar = new MultiblockGroup("altar", Lists.newArrayList(BlockDefs.craftingAltar.getDefaultState()), true);
		
		catalysts.addBlock(new BlockPos(-1, 0, -2), 0);
		catalysts.addBlock(new BlockPos(1, 0, -2), 0);
		catalysts.addBlock(new BlockPos(-1, 0, 2), 0);
		catalysts.addBlock(new BlockPos(1, 0, 2), 0);
		catalysts.addBlock(new BlockPos(0, -4, 0), 0);

		out.addBlock(new BlockPos(-1, 0, -1), STAIR_EAST);
		out.addBlock(new BlockPos(-1, 0, 0), STAIR_EAST);
		out.addBlock(new BlockPos(-1, 0, 1), STAIR_EAST);
		out.addBlock(new BlockPos(1, 0, -1), STAIR_WEST);
		out.addBlock(new BlockPos(1, 0, 0), STAIR_WEST);
		out.addBlock(new BlockPos(1, 0, 1), STAIR_WEST);
		out.addBlock(new BlockPos(0, 0, -2), STAIR_SOUTH);
		out.addBlock(new BlockPos(0, 0, 2), STAIR_NORTH);
		out.addBlock(new BlockPos(-1, -1, -1), STAIR_NORTH_INVERTED);
		out.addBlock(new BlockPos(-1, -1, 1), STAIR_SOUTH_INVERTED);
		out.addBlock(new BlockPos(1, -1, -1), STAIR_NORTH_INVERTED);
		out.addBlock(new BlockPos(1, -1, 1), STAIR_SOUTH_INVERTED);
		
		out.addBlock(new BlockPos(0, 0, -1), 0);
		out.addBlock(new BlockPos(0, 0, 1), 0);
		out.addBlock(new BlockPos(1, -1, -2), 0);
		out.addBlock(new BlockPos(1, -1, 2), 0);
		out.addBlock(new BlockPos(-1, -1, -2), 0);
		out.addBlock(new BlockPos(-1, -1, 2), 0);
		out.addBlock(new BlockPos(1, -2, -2), 0);
		out.addBlock(new BlockPos(1, -2, 2), 0);
		out.addBlock(new BlockPos(-1, -2, -2), 0);
		out.addBlock(new BlockPos(-1, -2, 2), 0);
		out.addBlock(new BlockPos(1, -3, -2), 0);
		out.addBlock(new BlockPos(1, -3, 2), 0);
		out.addBlock(new BlockPos(-1, -3, -2), 0);
		out.addBlock(new BlockPos(-1, -3, 2), 0);
		out.addBlock(new BlockPos(-2, -4, -2), 0);		
		out.addBlock(new BlockPos(-2, -4, -1), 0);		
		out.addBlock(new BlockPos(-2, -4, 0), 0);		
		out.addBlock(new BlockPos(-2, -4, 1), 0);		
		out.addBlock(new BlockPos(-2, -4, 2), 0);		
		out.addBlock(new BlockPos(-1, -4, -2), 0);		
		out.addBlock(new BlockPos(-1, -4, -1), 0);		
		out.addBlock(new BlockPos(-1, -4, 0), 0);		
		out.addBlock(new BlockPos(-1, -4, 1), 0);		
		out.addBlock(new BlockPos(-1, -4, 2), 0);		
		out.addBlock(new BlockPos(0, -4, -2), 0);		
		out.addBlock(new BlockPos(0, -4, -1), 0);		
		out.addBlock(new BlockPos(0, -4, 1), 0);		
		out.addBlock(new BlockPos(0, -4, 2), 0);		
		out.addBlock(new BlockPos(1, -4, -2), 0);		
		out.addBlock(new BlockPos(1, -4, -1), 0);		
		out.addBlock(new BlockPos(1, -4, 0), 0);		
		out.addBlock(new BlockPos(1, -4, 1), 0);
		out.addBlock(new BlockPos(1, -4, 2), 0);		
		out.addBlock(new BlockPos(2, -4, -2), 0);		
		out.addBlock(new BlockPos(2, -4, -1), 0);		
		out.addBlock(new BlockPos(2, -4, 0), 0);		
		out.addBlock(new BlockPos(2, -4, 1), 0);		
		out.addBlock(new BlockPos(2, -4, 2), 0);		
		
		MultiblockGroup wall = new MultiblockGroup("wall", Lists.newArrayList(BlockDefs.magicWall.getDefaultState()), true);
		wall.addBlock(new BlockPos(0, -1, -2));
		wall.addBlock(new BlockPos(0, -2, -2));
		wall.addBlock(new BlockPos(0, -3, -2));
		wall.addBlock(new BlockPos(0, -1, 2));
		wall.addBlock(new BlockPos(0, -2, 2));
		wall.addBlock(new BlockPos(0, -3, 2));
		
		MultiblockGroup lever1 = new MultiblockGroup("lever1", Lists.newArrayList(
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.EAST),
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.EAST).withProperty(BlockLever.POWERED, true)
				), false);
		MultiblockGroup lever2 = new MultiblockGroup("lever2", Lists.newArrayList(
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.EAST),
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.EAST).withProperty(BlockLever.POWERED, true)
				), false);
		MultiblockGroup lever3 = new MultiblockGroup("lever3", Lists.newArrayList(
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.WEST),
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.WEST).withProperty(BlockLever.POWERED, true)
				), false);
		MultiblockGroup lever4 = new MultiblockGroup("lever4", Lists.newArrayList(
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.WEST),
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.WEST).withProperty(BlockLever.POWERED, true)
				), false);
		
		MultiblockGroup podium1 = new MultiblockGroup("podium1", Lists.newArrayList(
				BlockDefs.lectern.getDefaultState().withProperty(BlockLectern.FACING, EnumFacing.EAST)
				), false);
		MultiblockGroup podium2 = new MultiblockGroup("podium2", Lists.newArrayList(
				BlockDefs.lectern.getDefaultState().withProperty(BlockLectern.FACING, EnumFacing.EAST)
				), false);
		MultiblockGroup podium3 = new MultiblockGroup("podium3", Lists.newArrayList(
				BlockDefs.lectern.getDefaultState().withProperty(BlockLectern.FACING, EnumFacing.WEST)
				), false);
		MultiblockGroup podium4 = new MultiblockGroup("podium4", Lists.newArrayList(
				BlockDefs.lectern.getDefaultState().withProperty(BlockLectern.FACING, EnumFacing.WEST)
				), false);

		
		lever1.addBlock(new BlockPos(2, -2, 2));
		lever2.addBlock(new BlockPos(2, -2, -2));
		lever3.addBlock(new BlockPos(-2, -2, 2));
		lever4.addBlock(new BlockPos(-2, -2, -2));
		podium1.addBlock(new BlockPos(2, -3, 2));
		podium2.addBlock(new BlockPos(2, -3, -2));
		podium3.addBlock(new BlockPos(-2, -3, 2));
		podium4.addBlock(new BlockPos(-2, -3, -2));
		
		primary.addGroup(wall);
		primary.addGroup(lever1, lever2, lever3,  lever4);
		primary.addGroup(out);
		primary.addGroup(catalysts);
		primary.addGroup(podium1, podium2, podium3, podium4);
		primary.addGroup(altar);
		
		catalysts_alt = new TypedMultiblockGroup("catalysts_alt", capsMaterials, false);
		
		out_alt = new TypedMultiblockGroup("out_alt", structureMaterials, false);
		
		MultiblockGroup wall_alt = new MultiblockGroup("wall_alt", Lists.newArrayList(BlockDefs.magicWall.getDefaultState()), true);
		wall_alt.addBlock(new BlockPos(-2, -1, 0));
		wall_alt.addBlock(new BlockPos(-2, -2, 0));
		wall_alt.addBlock(new BlockPos(-2, -3, 0));
		wall_alt.addBlock(new BlockPos(2, -1, 0));
		wall_alt.addBlock(new BlockPos(2, -2, 0));
		wall_alt.addBlock(new BlockPos(2, -3, 0));

		
		catalysts_alt.addBlock(new BlockPos(-2, 0, -1), 0);
		catalysts_alt.addBlock(new BlockPos(-2, 0, 1), 0);
		catalysts_alt.addBlock(new BlockPos(2, 0, -1), 0);
		catalysts_alt.addBlock(new BlockPos(2, 0, 1), 0);
		catalysts_alt.addBlock(new BlockPos(0, -4, 0), 0);

		out_alt.addBlock(new BlockPos(-1, 0, -1), STAIR_SOUTH);
		out_alt.addBlock(new BlockPos(0, 0, -1), STAIR_SOUTH);
		out_alt.addBlock(new BlockPos(1, 0, -1), STAIR_SOUTH);
		out_alt.addBlock(new BlockPos(-1, 0, 1), STAIR_NORTH);
		out_alt.addBlock(new BlockPos(0, 0, 1), STAIR_NORTH);
		out_alt.addBlock(new BlockPos(1, 0, 1), STAIR_NORTH);
		out_alt.addBlock(new BlockPos(-2, 0, 0), STAIR_EAST);
		out_alt.addBlock(new BlockPos(2, 0, 0), STAIR_WEST);
		out_alt.addBlock(new BlockPos(-1, -1, -1), STAIR_WEST_INVERTED);
		out_alt.addBlock(new BlockPos(-1, -1, 1), STAIR_WEST_INVERTED);
		out_alt.addBlock(new BlockPos(1, -1, -1), STAIR_EAST_INVERTED);
		out_alt.addBlock(new BlockPos(1, -1, 1), STAIR_EAST_INVERTED);
		
		out_alt.addBlock(new BlockPos(-1, 0, 0), 0);
		out_alt.addBlock(new BlockPos(1, 0, 0), 0);
		out_alt.addBlock(new BlockPos(-2, -1, 1), 0);
		out_alt.addBlock(new BlockPos(2, -1, 1), 0);
		out_alt.addBlock(new BlockPos(-2, -1, -1), 0);
		out_alt.addBlock(new BlockPos(2, -1, -1), 0);
		out_alt.addBlock(new BlockPos(-2, -2, 1), 0);
		out_alt.addBlock(new BlockPos(2, -2, 1), 0);
		out_alt.addBlock(new BlockPos(-2, -2, -1), 0);
		out_alt.addBlock(new BlockPos(2, -2, -1), 0);
		out_alt.addBlock(new BlockPos(-2, -3, 1), 0);
		out_alt.addBlock(new BlockPos(2, -3, 1), 0);
		out_alt.addBlock(new BlockPos(-2, -3, -1), 0);
		out_alt.addBlock(new BlockPos(2, -3, -1), 0);
		out_alt.addBlock(new BlockPos(-2, -4, -2), 0);		
		out_alt.addBlock(new BlockPos(-2, -4, -1), 0);		
		out_alt.addBlock(new BlockPos(-2, -4, 0), 0);		
		out_alt.addBlock(new BlockPos(-2, -4, 1), 0);		
		out_alt.addBlock(new BlockPos(-2, -4, 2), 0);		
		out_alt.addBlock(new BlockPos(-1, -4, -2), 0);		
		out_alt.addBlock(new BlockPos(-1, -4, -1), 0);		
		out_alt.addBlock(new BlockPos(-1, -4, 0), 0);		
		out_alt.addBlock(new BlockPos(-1, -4, 1), 0);		
		out_alt.addBlock(new BlockPos(-1, -4, 2), 0);		
		out_alt.addBlock(new BlockPos(0, -4, -2), 0);		
		out_alt.addBlock(new BlockPos(0, -4, -1), 0);		
		out_alt.addBlock(new BlockPos(0, -4, 1), 0);		
		out_alt.addBlock(new BlockPos(0, -4, 2), 0);		
		out_alt.addBlock(new BlockPos(1, -4, -2), 0);		
		out_alt.addBlock(new BlockPos(1, -4, -1), 0);		
		out_alt.addBlock(new BlockPos(1, -4, 0), 0);		
		out_alt.addBlock(new BlockPos(1, -4, 1), 0);
		out_alt.addBlock(new BlockPos(1, -4, 2), 0);		
		out_alt.addBlock(new BlockPos(2, -4, -2), 0);		
		out_alt.addBlock(new BlockPos(2, -4, -1), 0);		
		out_alt.addBlock(new BlockPos(2, -4, 0), 0);		
		out_alt.addBlock(new BlockPos(2, -4, 1), 0);		
		out_alt.addBlock(new BlockPos(2, -4, 2), 0);
		
		MultiblockGroup lever1_alt = new MultiblockGroup("lever1_alt", Lists.newArrayList(
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.SOUTH),
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.SOUTH).withProperty(BlockLever.POWERED, true)
				), false);
		MultiblockGroup lever2_alt = new MultiblockGroup("lever2_alt", Lists.newArrayList(
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.NORTH),
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.NORTH).withProperty(BlockLever.POWERED, true)
				), false);
		MultiblockGroup lever3_alt = new MultiblockGroup("lever3_alt", Lists.newArrayList(
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.SOUTH),
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.SOUTH).withProperty(BlockLever.POWERED, true)
				), false);
		MultiblockGroup lever4_alt = new MultiblockGroup("lever4_alt", Lists.newArrayList(
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.NORTH),
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.NORTH).withProperty(BlockLever.POWERED, true)
				), false);
		
		MultiblockGroup podium1_alt = new MultiblockGroup("podium1_alt", Lists.newArrayList(
				BlockDefs.lectern.getDefaultState().withProperty(BlockLectern.FACING, EnumFacing.SOUTH)
				), false);
		MultiblockGroup podium2_alt = new MultiblockGroup("podium2_alt", Lists.newArrayList(
				BlockDefs.lectern.getDefaultState().withProperty(BlockLectern.FACING, EnumFacing.NORTH)
				), false);
		MultiblockGroup podium3_alt = new MultiblockGroup("podium3_alt", Lists.newArrayList(
				BlockDefs.lectern.getDefaultState().withProperty(BlockLectern.FACING, EnumFacing.SOUTH)
				), false);
		MultiblockGroup podium4_alt = new MultiblockGroup("podium4_alt", Lists.newArrayList(
				BlockDefs.lectern.getDefaultState().withProperty(BlockLectern.FACING, EnumFacing.NORTH)
				), false);
		lever1_alt.addBlock(new BlockPos(2, -2, 2));
		lever2_alt.addBlock(new BlockPos(2, -2, -2));
		lever3_alt.addBlock(new BlockPos(-2, -2, 2));
		lever4_alt.addBlock(new BlockPos(-2, -2, -2));
		podium1_alt.addBlock(new BlockPos(2, -3, 2));
		podium2_alt.addBlock(new BlockPos(2, -3, -2));
		podium3_alt.addBlock(new BlockPos(-2, -3, 2));
		podium4_alt.addBlock(new BlockPos(-2, -3, -2));		
		secondary.addGroup(wall_alt);
		secondary.addGroup(lever1_alt, lever2_alt, lever3_alt, lever4_alt);
		secondary.addGroup(out_alt);
		secondary.addGroup(catalysts_alt);
		secondary.addGroup(podium1_alt, podium2_alt, podium3_alt, podium4_alt);
		secondary.addGroup(altar);
		
		MultiblockGroup center = new MultiblockGroup("center", Lists.newArrayList(BlockDefs.craftingAltar.getDefaultState()), true);
		center.addBlock(new BlockPos(0, 0, 0));
		primary.addGroup(center);
		secondary.addGroup(center);
	}
	
	@Override
	public IMultiblock getMultiblockStructure() {
		return secondary;
	}

	public ItemStack getNextPlannedItem(){
		if (spellGuide != null){
			if (this.allAddedItems.size() < spellGuide.length){
				return spellGuide[this.allAddedItems.size()].copy();
			}else{
				return new ItemStack(ItemDefs.spellParchment);
			}
		}
		return null;
	}

	private int getNumPartsInSpell(){
		int parts = 0;
		if (outputCombo != null)
			parts = outputCombo.length;

		if (shapeGroupGuide != null){
			for (int i = 0; i < shapeGroupGuide.length; ++i){
				if (shapeGroupGuide[i] != null)
					parts += shapeGroupGuide[i].length;
			}
		}
		return parts;
	}

	private boolean spellGuideIsWithinStructurePower(){
		return getNumPartsInSpell() <= maxEffects;
	}

	private boolean currentDefinitionIsWithinStructurePower(){
		int count = this.spellDef.size();
		for (KeyValuePair<ArrayList<AbstractSpellPart>, NBTTagCompound> part : shapeGroups)
			count += part.key.size();

		return count <= this.maxEffects;
	}
	
	@Override
	public boolean isStructureValid() {
		return this.structureValid;
	}

	public boolean isCrafting(){
		return this.isCrafting;
	}

	@Override
	public void update(){
		super.update();
		ticksExisted++;
		checkStructure();
		checkForStartCondition();
		updateLecternInformation();
		if (isCrafting){
			checkForEndCondition();
			updatePowerRequestData();
			if (!world.isRemote && !currentDefinitionIsWithinStructurePower() && this.ticksExisted > 100){
				world.newExplosion(null, pos.getX() + 0.5, pos.getY() - 1.5, pos.getZ() + 0.5, 5, false, true);
				setCrafting(false);
				return;
			}
			if (world.isRemote && checkCounter == 1){
			ArsMagica2.proxy.particleManager.RibbonFromPointToPoint(world, pos.getX() + 0.5, pos.getY() - 2, pos.getZ() + 0.5, pos.getX() + 0.5, pos.getY() - 3, pos.getZ() + 0.5);
			}
			List<EntityItem> components = lookForValidItems();
			ItemStack stack = getNextPlannedItem();
			for (EntityItem item : components){
				if (item.isDead) continue;
				ItemStack entityItemStack = item.getEntityItem();
				if (stack != null && compareItemStacks(stack, entityItemStack)){
					if (!world.isRemote){
						updateCurrentRecipe(item);
						item.setDead();
					}else{
						//TODO world.playSound(pos.getX(), pos.getY(), pos.getZ(), "arsmagica2:misc.craftingaltar.component_added", 1.0f, 0.4f + world.rand.nextFloat() * 0.6f, false);
						for (int i = 0; i < 5 * ArsMagica2.config.getGFXLevel(); ++i){
							AMParticle particle = (AMParticle)ArsMagica2.proxy.particleManager.spawn(world, "radiant", item.posX, item.posY, item.posZ);
							if (particle != null){
								particle.setMaxAge(40);
								particle.AddParticleController(new ParticleMoveOnHeading(particle, world.rand.nextFloat() * 360, world.rand.nextFloat() * 360, 0.01f, 1, false));
								particle.AddParticleController(new ParticleFadeOut(particle, 1, false).setFadeSpeed(0.05f).setKillParticleOnFinish(true));
								particle.setParticleScale(0.02f);
								particle.setRGBColorF(world.rand.nextFloat(), world.rand.nextFloat(), world.rand.nextFloat());
							}
						}
					}
				}
			}
		}
		this.markDirty();
	}

	private void updateLecternInformation(){
		if (podiumLocation == null) return;
		TileEntityLectern lectern = (TileEntityLectern)world.getTileEntity(pos.add(podiumLocation));
		if (lectern != null){
			if (lectern.hasStack()){
				ItemStack lecternStack = lectern.getStack();
				if (lecternStack.hasTagCompound()){
					spellGuide = NBTUtils.getItemStackArray(lecternStack.getTagCompound(), "spell_combo");
					outputCombo = lecternStack.getTagCompound().getIntArray("output_combo");
					currentSpellName = lecternStack.getDisplayName();

					int numShapeGroups = lecternStack.getTagCompound().getInteger("numShapeGroups");
					shapeGroupGuide = new int[numShapeGroups][];

					for (int i = 0; i < numShapeGroups; ++i){
						shapeGroupGuide[i] = lecternStack.getTagCompound().getIntArray("shapeGroupCombo_" + i);
					}
				}

				if (isCrafting){
					if (spellGuide != null){
						lectern.setNeedsBook(false);
						lectern.setTooltipStack(getNextPlannedItem());
					}else{
						lectern.setNeedsBook(true);
					}
				}else{
					lectern.setTooltipStack(null);
				}
				if (spellGuideIsWithinStructurePower()){
					lectern.setOverpowered(false);
				}else{
					lectern.setOverpowered(true);
				}
			}else{
				if (isCrafting){
					lectern.setNeedsBook(true);
				}
				lectern.setTooltipStack(null);
			}
		}
	}

	public BlockPos getSwitchLocation(){
		return this.switchLocation;
	}

	public boolean switchIsOn(){
		if (switchLocation == null) return false;
		IBlockState block = world.getBlockState(pos.add(switchLocation));
		boolean b = false;
		if (block.getBlock() == Blocks.LEVER){
			for (int i = 0; i < 6; ++i){
				b |= block.getValue(BlockLever.POWERED);
				if (b) break;
			}
		}
		return b;
	}

	public void flipSwitch(){
		if (switchLocation == null) return;
		IBlockState block = world.getBlockState(pos.add(switchLocation));
		if (block.getBlock() == Blocks.LEVER){
			world.setBlockState(pos.add(switchLocation), block.withProperty(BlockLever.POWERED, false));
		}
	}

	private void updatePowerRequestData(){
		ItemStack stack = getNextPlannedItem();
		if (stack != null && stack.getItem().equals(ItemDefs.etherium)){
			if (switchIsOn()){
				int flags = stack.getItemDamage();
				setPowerRequests();
				pickPowerType(stack);
				for (PowerTypes type : this.currentMainPowerTypes) {
					if (PowerNodeRegistry.For(this.world).checkPower(this, type, Math.max(0, Math.min(100, stack.stackSize - currentConsumedPower)))) {
						currentConsumedPower += PowerNodeRegistry.For(world).consumePower(this, type, Math.min(100, stack.stackSize - currentConsumedPower));
					}
				}
				if (currentConsumedPower >= stack.stackSize){
					System.out.println(currentConsumedPower + " vs " + stack.stackSize);
					//PowerNodeRegistry.For(this.world).setPower(this, this.currentMainPowerTypes, 0);
					if (!world.isRemote)
						addItemToRecipe(new ItemStack(ItemDefs.etherium, stack.stackSize, flags));
					setNoPowerRequests();
					flipSwitch();
				}
			}else{
				setNoPowerRequests();
			}
		}else{
			setNoPowerRequests();
		}
	}

	@Override
	protected void setNoPowerRequests(){
		currentConsumedPower = 0;
		currentMainPowerTypes.clear();

		super.setNoPowerRequests();
	}

	private void pickPowerType(ItemStack stack){
		if (!this.currentMainPowerTypes.isEmpty())
			return;
		List<PowerTypes> valids = PowerTypes.getTypes(stack.getItemDamage());
		this.currentMainPowerTypes.addAll(valids);
	}

	private void updateCurrentRecipe(EntityItem item){
		ItemStack stack = item.getEntityItem();
		addItemToRecipe(stack);
	}

	private void addItemToRecipe(ItemStack stack){
		allAddedItems.add(stack);
		currentAddedItems.add(stack);

		if (!world.isRemote){
			AMDataWriter writer = new AMDataWriter();
			writer.add(pos.getX());
			writer.add(pos.getY());
			writer.add(pos.getZ());
			writer.add(COMPONENT_ADDED);
			writer.add(stack);
			AMNetHandler.INSTANCE.sendPacketToAllClientsNear(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 32, AMPacketIDs.CRAFTING_ALTAR_DATA, writer.generate());
		}

		if (matchCurrentRecipe()){
			currentAddedItems.clear();
			return;
		}
	}

	private boolean matchCurrentRecipe(){
		AbstractSpellPart part = SpellRegistry.getPartByRecipe(currentAddedItems);
		if (part == null) return false;

		KeyValuePair<ArrayList<AbstractSpellPart>, NBTTagCompound> currentShapeGroupList = getShapeGroupToAddTo();

		if (part instanceof Summon)
			handleSummonShape();
		if (part instanceof Binding)
			handleBindingShape();


		//if this is null, then we have already completed all of the shape groups that the book identifies
		//we're now creating the body of the spell
		if (currentShapeGroupList == null){
			part.encodeBasicData(savedData, currentAddedItems.toArray());
			spellDef.add(part);
		}else{
			part.encodeBasicData(currentShapeGroupList.value, currentAddedItems.toArray());
			currentShapeGroupList.key.add(part);
		}
		return true;
	}

	private KeyValuePair<ArrayList<AbstractSpellPart>, NBTTagCompound> getShapeGroupToAddTo(){
		for (int i = 0; i < shapeGroupGuide.length; ++i){
			int guideLength = shapeGroupGuide[i].length;
			int addedLength = shapeGroups.get(i).key.size();
			if (addedLength < guideLength)
				return shapeGroups.get(i);
		}

		return null;
	}

	private void handleSummonShape(){
		if (currentAddedItems.size() > 2)
			addedPhylactery = currentAddedItems.get(currentAddedItems.size() - 2);
	}

	private void handleBindingShape(){
		if (currentAddedItems.size() == 7)
			addedBindingCatalyst = currentAddedItems.get(currentAddedItems.size() - 1);
	}

	private List<EntityItem> lookForValidItems(){
		if (!isCrafting) return new ArrayList<EntityItem>();
		double radius = world.isRemote ? 2.1 : 2;
		List<EntityItem> items = this.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX() - radius, pos.getY() - 3, pos.getZ() - radius, pos.getX() + radius, pos.getY(), pos.getZ() + radius));
		return items;
	}

	private void checkStructure(){
		maxEffects = 0;
		if (checkCounter++ > 50)
			checkCounter = 0;
		if (primary.matches(world, pos)) {
			for (IMultiblockGroup matching : primary.getMatchingGroups(world, pos)) {
				for (IBlockState state : matching.getStates()) {
					if (state.getBlock().equals(Blocks.LEVER))
						this.switchLocation = matching.getPositions().get(0);
					else if (state.getBlock().equals(BlockDefs.lectern))
						this.podiumLocation = matching.getPositions().get(0);
				}
				if (matching == catalysts || matching == catalysts_alt) {
					Integer toAdd = capsPower.get(world.getBlockState(pos.down(4)));
					maxEffects += toAdd != null ? toAdd : 0;
				}else if (matching == out || matching == out_alt) {
					mimicState = world.getBlockState(pos.down(4).east());
					Integer toAdd = structurePower.get(mimicState);
					maxEffects += toAdd != null ? toAdd : 0;
				}
			}
		} else if (secondary.matches(world, pos)) {
			for (IMultiblockGroup matching : secondary.getMatchingGroups(world, pos)) {
				for (IBlockState state : matching.getStates()) {
					if (state.getBlock().equals(Blocks.LEVER))
						this.switchLocation = matching.getPositions().get(0);
					else if (state.getBlock().equals(BlockDefs.lectern))
						this.podiumLocation = matching.getPositions().get(0);
				}
				if (matching == catalysts || matching == catalysts_alt) {
					Integer toAdd = capsPower.get(world.getBlockState(pos.down(4)));
					maxEffects += toAdd != null ? toAdd : 0;
				}else if (matching == out || matching == out_alt) {
					mimicState = world.getBlockState(pos.down(4).east());
					Integer toAdd = structurePower.get(mimicState);
					maxEffects += toAdd != null ? toAdd : 0;
				}
			}
		}
		setStructureValid(primary.matches(world, pos) || secondary.matches(world, pos));
	}

	private void checkForStartCondition(){
		if (this.world.isRemote || !structureValid || this.isCrafting) return;

		List<Entity> items = this.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX() - 2, pos.getY() - 3, pos.getZ() - 2, pos.getX() + 2, pos.getY(), pos.getZ() + 2));
		if (items.size() == 1){
			EntityItem item = (EntityItem)items.get(0);
			if (item != null && !item.isDead && item.getEntityItem().getItem() == ItemDefs.blankRune){
				item.setDead();
				setCrafting(true);
			}
		}
	}
	
	public IBlockState getMimicState() {
		return mimicState;
	}
	
	private void checkForEndCondition(){
		if (!structureValid || !this.isCrafting || world == null) return;

		double radius = world.isRemote ? 2.2 : 2;

		List<Entity> items = this.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX() - radius, pos.getY() - 3, pos.getZ() - radius, pos.getX() + radius, pos.getY(), pos.getZ() + radius));
		if (items.size() == 1){
			EntityItem item = (EntityItem)items.get(0);
			if (item != null && !item.isDead && item.getEntityItem() != null && item.getEntityItem().getItem() == ItemDefs.spellParchment){
				if (!world.isRemote){
					item.setDead();
					setCrafting(false);
					EntityItem craftedItem = new EntityItem(world);
					craftedItem.setPosition(pos.getX() + 0.5, pos.getY() - 1.5, pos.getZ() + 0.5);
					ItemStack craftStack = new ItemStack(ItemDefs.spell);// SpellUtils.createSpellStack(shapeGroups, spellDef, savedData);
					ISpellCaster caster = craftStack.getCapability(SpellCaster.INSTANCE, null);
					if (caster != null) {
						caster.setSpellCommon(SpellUtils.transformParts(spellDef));
						caster.setCommonStoredData(savedData);
						List<List<List<AbstractSpellPart>>> shapeGroups = Lists.newArrayList();
						for (int i = 0; i < this.shapeGroups.size(); i++) {
							KeyValuePair<ArrayList<AbstractSpellPart>, NBTTagCompound> entry = this.shapeGroups.get(i);
							shapeGroups.add(SpellUtils.transformParts(entry.key));
							caster.setStoredData(i, entry.value);
						}
						caster.setShapeGroups(shapeGroups);
					}
					if (!craftStack.hasTagCompound())
						craftStack.setTagCompound(new NBTTagCompound());
					AddSpecialMetadata(craftStack);
					
					craftStack.getTagCompound().setString("suggestedName", currentSpellName != null ? currentSpellName : "");
					if (getNextPlannedItem() == null || getNextPlannedItem().getItem() != ItemDefs.spellParchment)
						craftStack.setTagCompound(null);
					craftedItem.setEntityItemStack(craftStack);
					world.spawnEntity(craftedItem);
					
					allAddedItems.clear();
					currentAddedItems.clear();
				}else{
					//world.playSound(pos.getX(), pos.getY(), pos.getZ(), "arsmagica2:misc.craftingaltar.create_spell", 1.0f, 1.0f, true);
				}
			}
		}
	}

	private void AddSpecialMetadata(ItemStack craftStack){}

	private void setCrafting(boolean crafting){
		this.isCrafting = crafting;
		if (!world.isRemote){
			AMDataWriter writer = new AMDataWriter();
			writer.add(pos.getX());
			writer.add(pos.getY());
			writer.add(pos.getZ());
			writer.add(CRAFTING_CHANGED);
			writer.add(crafting);
			AMNetHandler.INSTANCE.sendPacketToAllClientsNear(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 32, AMPacketIDs.CRAFTING_ALTAR_DATA, writer.generate());
		}
		if (crafting){
			allAddedItems.clear();
			currentAddedItems.clear();

			spellDef.clear();
			shapeGroups.clear();
			
			for (int i = 0; i < 5; ++i){
				shapeGroups.add(new KeyValuePair<ArrayList<AbstractSpellPart>, NBTTagCompound>(new ArrayList<>(), new NBTTagCompound()));
			}
			
			//find otherworld auras
			IPowerNode<?>[] nodes = PowerNodeRegistry.For(world).getAllNearbyNodes(world, pos, PowerTypes.DARK);
			for (IPowerNode<?> node : nodes){
				if (node instanceof TileEntityOtherworldAura){
					((TileEntityOtherworldAura)node).setActive(true, this);
					break;
				}
			}
		}
	}

	private void setStructureValid(boolean valid){
		if (this.structureValid == valid) return;
		this.structureValid = valid;
		this.markDirty();
	}

	public void deactivate(){
		if (!world.isRemote){
			this.setCrafting(false);
			for (ItemStack stack : allAddedItems){
				if (stack.getItem() == ItemDefs.etherium)
					continue;
				EntityItem eItem = new EntityItem(world);
				eItem.setPosition(pos.getX(), pos.getY() - 1, pos.getZ());
				eItem.setEntityItemStack(stack);
				world.spawnEntity(eItem);
			}
			allAddedItems.clear();
		}
	}

	@Override
	public boolean canProvidePower(PowerTypes type){
		return false;
	}

	private boolean compareItemStacks(ItemStack target, ItemStack input) {
		boolean tagCheck = target.getTagCompound() == null ? true : (input.getTagCompound() == null ? false : NBTUtils.contains(target.getTagCompound(), input.getTagCompound()));
		return OreDictionary.itemMatches(target, input, false) && tagCheck;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound){
		super.writeToNBT(nbttagcompound);

		NBTTagCompound altarCompound = new NBTTagCompound();
		altarCompound.setBoolean("isCrafting", this.isCrafting);
		altarCompound.setInteger("currentKey", this.currentKey);
		altarCompound.setString("currentSpellName", currentSpellName);
		altarCompound.setBoolean("StructureValid", structureValid);
		if (mimicState != null)
			altarCompound.setInteger("MimicState", Block.getStateId(mimicState));

		NBTTagList allAddedItemsList = new NBTTagList();
		for (ItemStack stack : allAddedItems){
			NBTTagCompound addedItem = new NBTTagCompound();
			stack.writeToNBT(addedItem);
			allAddedItemsList.appendTag(addedItem);
		}

		altarCompound.setTag("allAddedItems", allAddedItemsList);

		NBTTagList currentAddedItemsList = new NBTTagList();
		for (ItemStack stack : currentAddedItems){
			NBTTagCompound addedItem = new NBTTagCompound();
			stack.writeToNBT(addedItem);
			currentAddedItemsList.appendTag(addedItem);
		}

		altarCompound.setTag("currentAddedItems", currentAddedItemsList);

		if (addedPhylactery != null){
			NBTTagCompound phylactery = new NBTTagCompound();
			addedPhylactery.writeToNBT(phylactery);
			altarCompound.setTag("phylactery", phylactery);
		}

		if (addedBindingCatalyst != null){
			NBTTagCompound catalyst = new NBTTagCompound();
			addedBindingCatalyst.writeToNBT(catalyst);
			altarCompound.setTag("catalyst", catalyst);
		}
		
		//TODO CRAFTING Altar...
		
		NBTTagList shapeGroupData = new NBTTagList();
		for (KeyValuePair<ArrayList<AbstractSpellPart>, NBTTagCompound> list : shapeGroups){
			shapeGroupData.appendTag(ISpellPartListToNBT(list));
		}
		altarCompound.setTag("shapeGroups", shapeGroupData);

		NBTTagCompound spellDefSave = ISpellPartListToNBT(new KeyValuePair<>(spellDef, savedData));
		altarCompound.setTag("spellDef", spellDefSave);

		nbttagcompound.setTag("altarData", altarCompound);
		return nbttagcompound;
	}

	private NBTTagCompound ISpellPartListToNBT(KeyValuePair<ArrayList<AbstractSpellPart>, NBTTagCompound> spellDef2){
		return SpellUtils.encode(spellDef2);
	}

	private KeyValuePair<ArrayList<AbstractSpellPart>, NBTTagCompound> NBTToISpellPartList(NBTTagCompound compound){
		return SpellUtils.decode(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound){
		super.readFromNBT(nbttagcompound);

		if (!nbttagcompound.hasKey("altarData"))
			return;

		NBTTagCompound altarCompound = nbttagcompound.getCompoundTag("altarData");
		mimicState = Block.getStateById(altarCompound.getInteger("MimicState"));
		NBTTagList allAddedItems = altarCompound.getTagList("allAddedItems", Constants.NBT.TAG_COMPOUND);
		NBTTagList currentAddedItems = altarCompound.getTagList("currentAddedItems", Constants.NBT.TAG_COMPOUND);
		structureValid = altarCompound.getBoolean("StructureValid");
		this.isCrafting = altarCompound.getBoolean("isCrafting");
		this.currentKey = altarCompound.getInteger("currentKey");
		this.currentSpellName = altarCompound.getString("currentSpellName");

		if (altarCompound.hasKey("phylactery")){
			NBTTagCompound phylactery = altarCompound.getCompoundTag("phylactery");
			if (phylactery != null)
				this.addedPhylactery = ItemStack.loadItemStackFromNBT(phylactery);
		}

		if (altarCompound.hasKey("catalyst")){
			NBTTagCompound catalyst = altarCompound.getCompoundTag("catalyst");
			if (catalyst != null)
				this.addedBindingCatalyst = ItemStack.loadItemStackFromNBT(catalyst);
		}

		this.allAddedItems.clear();
		for (int i = 0; i < allAddedItems.tagCount(); ++i){
			NBTTagCompound addedItem = (NBTTagCompound)allAddedItems.getCompoundTagAt(i);
			if (addedItem == null)
				continue;
			ItemStack stack = ItemStack.loadItemStackFromNBT(addedItem);
			if (stack == null)
				continue;
			this.allAddedItems.add(stack);
		}

		this.currentAddedItems.clear();
		for (int i = 0; i < currentAddedItems.tagCount(); ++i){
			NBTTagCompound addedItem = (NBTTagCompound)currentAddedItems.getCompoundTagAt(i);
			if (addedItem == null)
				continue;
			ItemStack stack = ItemStack.loadItemStackFromNBT(addedItem);
			if (stack == null)
				continue;
			this.currentAddedItems.add(stack);
		}

		this.spellDef.clear();
		for (KeyValuePair<ArrayList<AbstractSpellPart>, NBTTagCompound> groups : shapeGroups)
			groups.key.clear();

		NBTTagCompound currentSpellDef = altarCompound.getCompoundTag("spellDef");
		this.spellDef.addAll(NBTToISpellPartList(currentSpellDef).key);
		this.savedData.merge(NBTToISpellPartList(currentSpellDef).value);

		NBTTagList currentShapeGroups = altarCompound.getTagList("shapeGroups", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < currentShapeGroups.tagCount(); ++i){
			NBTTagCompound compound = (NBTTagCompound)currentShapeGroups.getCompoundTagAt(i);
			try {
				shapeGroups.get(i).key.addAll(NBTToISpellPartList(compound).key);
				shapeGroups.get(i).value.merge(NBTToISpellPartList(compound).value);
			} catch (IndexOutOfBoundsException | NullPointerException e) {
				shapeGroups.add(i, new KeyValuePair<ArrayList<AbstractSpellPart>, NBTTagCompound>(new ArrayList<>(), new NBTTagCompound()));
				shapeGroups.get(i).key.addAll(NBTToISpellPartList(compound).key);
				shapeGroups.get(i).value.merge(NBTToISpellPartList(compound).value);
			}
		}
	}

	@Override
	public int getChargeRate(){
		return 250;
	}

	@Override
	public boolean canRelayPower(PowerTypes type){
		return false;
	}


	public void HandleUpdatePacket(byte[] remainingBytes){
		AMDataReader rdr = new AMDataReader(remainingBytes, false);
		byte subID = rdr.getByte();
		switch (subID){
		case FULL_UPDATE:
			this.isCrafting = rdr.getBoolean();
			this.currentKey = rdr.getInt();

			this.allAddedItems.clear();
			this.currentAddedItems.clear();

			int itemCount = rdr.getInt();
			for (int i = 0; i < itemCount; ++i)
				this.allAddedItems.add(rdr.getItemStack());
			break;
		case CRAFTING_CHANGED:
			this.setCrafting(rdr.getBoolean());
			break;
		case COMPONENT_ADDED:
			this.allAddedItems.add(rdr.getItemStack());
			break;
		}
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(pos, this.getBlockMetadata(), getUpdateTag());
		return packet;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
		this.readFromNBT(pkt.getNbtCompound());
		this.markDirty();
        //world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		//this.world.markAndNotifyBlock(pos, this.world.getChunkFromBlockCoords(pos), this.world.getBlockState(pos), this.world.getBlockState(pos), 3);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void markDirty() {
		this.markForUpdate();
		super.markDirty();
	}

	@Override
	public void markForUpdate() {
		this.dirty = true;
	}

	@Override
	public boolean needsUpdate() {
		return this.dirty;
	}

	@Override
	public void clean() {
		this.dirty = false;
	}
}
