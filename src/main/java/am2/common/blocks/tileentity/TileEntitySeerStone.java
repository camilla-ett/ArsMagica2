package am2.common.blocks.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import am2.ArsMagica2;
import am2.api.blocks.IKeystoneLockable;
import am2.api.items.ISpellFocus;
import am2.api.items.ItemFilterFocus;
import am2.client.models.SpriteRenderInfo;
import am2.client.particles.AMParticle;
import am2.client.particles.ParticleFloatUpward;
import am2.client.particles.ParticleMoveOnHeading;
import am2.common.blocks.BlockSeerStone;
import am2.common.defs.BlockDefs;
import am2.common.power.PowerNodeRegistry;
import am2.common.power.PowerTypes;
import am2.common.utils.KeystoneUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

public class TileEntitySeerStone extends TileEntityAMPower implements IInventory, IKeystoneLockable<TileEntitySeerStone>{

	private boolean hasSight;
	private ArrayList<SpriteRenderInfo> animations;
	private ArrayList<Integer> animationWeighting;
	private SpriteRenderInfo currentAnimation;
	private int ticksToNextCheck;
	private int maxTicksToCheck = 20;
	private ItemStack[] inventory;
	int tickCounter;
	public static int keystoneSlot = 1;

	private List<PowerTypes> validTypes = Lists.newArrayList(
			PowerTypes.LIGHT
	);

	boolean swapDetectionMode = false;

	public TileEntitySeerStone(){
		super(100);
		hasSight = false;
		tickCounter = 0;
		animations = new ArrayList<SpriteRenderInfo>();
		animationWeighting = new ArrayList<Integer>();

		animations.add(new SpriteRenderInfo(50, 59, 2)); //blink
		animationWeighting.add(0);

		animations.add(new SpriteRenderInfo(20, 29, 2)); //idle
		animationWeighting.add(60);

		animations.add(new SpriteRenderInfo(30, 39, 2)); //look left
		animationWeighting.add(11);

		animations.add(new SpriteRenderInfo(40, 49, 2)); //look right
		animationWeighting.add(11);

		animations.add(new SpriteRenderInfo(0, 14, 2)); //flare 1
		animationWeighting.add(6);

		animations.add(new SpriteRenderInfo(60, 74, 2)); //flare 2
		animationWeighting.add(6);

		animations.add(new SpriteRenderInfo(80, 94, 2)); //flare 3
		animationWeighting.add(6);

		currentAnimation = animations.get(0);
		currentAnimation.isDone = true;

		inventory = new ItemStack[getSizeInventory()];
		ticksToNextCheck = maxTicksToCheck;
	}

	@Override
	public float particleOffset(int axis){
		EnumFacing meta = world.getBlockState(pos).getValue(BlockSeerStone.FACING);

		if (axis == 0){
			switch (meta){
			case UP:
				return 0.15f;
			case DOWN:
				return 0.85f;
			default:
				return 0.5f;
			}
		}else if (axis == 1){
			switch (meta){
			case EAST:
				return 0.85f;
			case WEST:
				return 0.2f;
			default:
				return 0.5f;
			}
		}else if (axis == 2){
			switch (meta){
			case NORTH:
				return 0.15f;
			case SOUTH:
				return 0.85f;
			default:
				return 0.5f;
			}
		}

		return 0.5f;
	}

	public void invertDetection(){
		this.swapDetectionMode = !this.swapDetectionMode;
	}

	public boolean isInvertingDetection(){
		return this.swapDetectionMode;
	}

	private SpriteRenderInfo GetWeightedRandomAnimation(){
		currentAnimation.reset(false);

		int randomNumber = world.rand.nextInt(100);
		int index = 0;

		SpriteRenderInfo current = animations.get(0);

		for (Integer i : animationWeighting){
			if (randomNumber < i){
				current = animations.get(index);
				break;
			}
			index++;
			randomNumber -= i;
		}

		return current == animations.get(0) ? animations.get(1) : current;
	}

	public boolean isActive(){
		if (this.world == null)
			return false;
		return PowerNodeRegistry.For(this.world).checkPower(this, PowerTypes.LIGHT, this.hasSight ? 2 : 1) && GetSearchRadius() > 0;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		NBTTagCompound compound = new NBTTagCompound();
		this.writeToNBT(compound);
		return new SPacketUpdateTileEntity(pos, 0, compound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
		this.readFromNBT(pkt.getNbtCompound());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(){
		super.update();

		if (!world.isRemote && isActive()){
			if (hasSight)
				PowerNodeRegistry.For(world).consumePower(this, PowerTypes.LIGHT, 0.25f);
			else
				PowerNodeRegistry.For(world).consumePower(this, PowerTypes.LIGHT, 0.125f);
		}

		ticksToNextCheck--;
		if (ticksToNextCheck <= 0 && isActive()){
			ticksToNextCheck = maxTicksToCheck;

			long key = KeystoneUtilities.instance.getKeyFromRunes(getRunesInKey());

			int radius = GetSearchRadius();
			Class<? extends Entity> searchClass = GetSearchClass();
			ArrayList<Entity> nearbyMobs = new ArrayList<Entity>();
			if (searchClass != null){
				nearbyMobs = (ArrayList<Entity>)this.world.getEntitiesWithinAABB(searchClass, new AxisAlignedBB(pos.add(-radius, -radius, -radius), pos.add(1+radius, 1+radius, 1+radius)));

				if (key > 0){
					ArrayList<Entity> mobsToIgnore = new ArrayList<Entity>();
					for (Entity e : nearbyMobs){
						if (swapDetectionMode){
							if (!(e instanceof EntityPlayer)){
								mobsToIgnore.add(e);
								continue;
							}
							if (!KeystoneUtilities.instance.GetKeysInInvenory((EntityLivingBase)e).contains(key)){
								mobsToIgnore.add(e);
							}
						}else{
							if (!(e instanceof EntityPlayer)) continue;
							if (KeystoneUtilities.instance.GetKeysInInvenory((EntityLivingBase)e).contains(key)){
								mobsToIgnore.add(e);
							}
						}
					}
					for (Entity e : mobsToIgnore) nearbyMobs.remove(e);
				}
			}

			if (nearbyMobs.size() > 0){
				if (!hasSight){
					hasSight = true;
					notifyNeighborsOfPowerChange();

					if (world.isRemote){
						currentAnimation.reset(false);
						currentAnimation = animations.get(0);
						currentAnimation.reset(true);
					}
				}
			}else{
				if (hasSight){
					hasSight = false;
					notifyNeighborsOfPowerChange();

					if (world.isRemote){
						currentAnimation.reset(false);
						currentAnimation = animations.get(0);
						currentAnimation.reset(false);
					}
				}
			}
		}else{
			if (hasSight && !isActive()){
				hasSight = false;
				notifyNeighborsOfPowerChange();

				if (world.isRemote){
					currentAnimation.reset(false);
					currentAnimation = animations.get(0);
					currentAnimation.reset(false);
				}
			}
		}

		//animations
		if (world.isRemote){
			if (!currentAnimation.isDone){
				tickCounter++;
				if (tickCounter == currentAnimation.speed){
					tickCounter = 0;
					currentAnimation.incrementIndex();
				}
			}else{
				if (isActive() && hasSight){
					currentAnimation = GetWeightedRandomAnimation();
				}
			}

			if (isActive() && hasSight){

				EnumFacing meta = world.getBlockState(pos).getValue(BlockSeerStone.FACING);

				double yaw = 0;
				double y = pos.getX() + 0.5;
				double x = pos.getY() + 0.5;
				double z = pos.getZ() + 0.5;

				switch (meta){
				case UP:
					y += 0.3;
					break;
				case DOWN:
					y -= 0.3;
					break;
				case EAST:
					yaw = 270;
					z += 0.3;
					break;
				case WEST:
					yaw = 90;
					z -= 0.3;
					break;
				case NORTH:
					yaw = 180;
					x += 0.3;
					break;
				case SOUTH:
					yaw = 0;
					x -= 0.3;
					break;
				}

				AMParticle effect = (AMParticle)ArsMagica2.proxy.particleManager.spawn(world, "sparkle2", x, y, z);
				if (effect != null){
					effect.setIgnoreMaxAge(false);
					effect.setMaxAge(35);
					//effect.setRGBColorF(0.9f, 0.7f, 0.0f);

					switch (meta){
					case UP:
						effect.AddParticleController(new ParticleFloatUpward(effect, 0.1f, -0.01f, 1, false));
						break;
					case DOWN:
						effect.AddParticleController(new ParticleFloatUpward(effect, 0.1f, 0.01f, 1, false));
						break;
					default:
						effect.AddParticleController(new ParticleMoveOnHeading(effect, yaw, 0, 0.01f, 1, false));
						effect.AddParticleController(new ParticleFloatUpward(effect, 0.1f, 0, 1, false));
					}
				}
			}
		}
	}

	private void notifyNeighborsOfPowerChange(){
		this.world.notifyBlockOfStateChange(pos, BlockDefs.seerStone);
		BlockPos otherPos = pos.offset(world.getBlockState(pos).getValue(BlockSeerStone.FACING));
		this.world.notifyBlockOfStateChange(otherPos, BlockDefs.seerStone);
		this.world.markAndNotifyBlock(otherPos, world.getChunkFromBlockCoords(otherPos), world.getBlockState(otherPos), world.getBlockState(otherPos), 3);
	}

	public boolean ShouldAnimate(){
		return (isActive() && hasSight) || !currentAnimation.isDone;
	}

	public int getAnimationIndex(){
		return currentAnimation.curFrame;
	}

	private int GetSearchRadius(){
		int focusLevel = -1;
		int inventoryIndex = 0;

		if (inventory[inventoryIndex] != null && inventory[inventoryIndex].getItem() instanceof ISpellFocus){
			int tempFocusLevel = ((ISpellFocus)inventory[inventoryIndex].getItem()).getFocusLevel();
			if (tempFocusLevel > focusLevel){
				focusLevel = tempFocusLevel;
			}
		}
		int radius = (focusLevel + 1) * 5;
		return radius;
	}

	private Class<? extends Entity> GetSearchClass(){
		if (inventory[1] != null && inventory[1].getItem() instanceof ItemFilterFocus){
			return ((ItemFilterFocus)inventory[1].getItem()).getFilterClass();
		}
		return null;
	}

	public boolean HasSight(){
		return isActive() && this.hasSight;
	}

	@Override
	public int getSizeInventory(){
		return 5;
	}

	@Override
	public ItemStack[] getRunesInKey(){
		ItemStack[] runes = new ItemStack[3];
		runes[0] = inventory[2];
		runes[1] = inventory[3];
		runes[2] = inventory[4];
		return runes;
	}

	@Override
	public boolean keystoneMustBeHeld(){
		return false;
	}

	@Override
	public boolean keystoneMustBeInActionBar(){
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int slot){
		if (slot >= inventory.length)
			return null;
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int i, int j){
		if (inventory[i] != null){
			if (inventory[i].getCount() <= j){
				ItemStack itemstack = inventory[i];
				inventory[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = inventory[i].splitStack(j);
			if (inventory[i].getCount() == 0){
				inventory[i] = null;
			}
			return itemstack1;
		}else{
			return null;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int i){
		if (inventory[i] != null){
			ItemStack itemstack = inventory[i];
			inventory[i] = null;
			return itemstack;
		}else{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack){
		inventory[i] = itemstack;
		if (itemstack != null && itemstack.getCount() > getInventoryStackLimit()){
			itemstack.getCount() = getInventoryStackLimit();
		}
	}

	@Override
	public String getName(){
		return "Seer Stone";
	}

	@Override
	public int getInventoryStackLimit(){
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer){
		if (world.getTileEntity(pos) != this){
			return false;
		}
		return entityplayer.getDistanceSqToCenter(pos) <= 64D;
	}

	@Override
	public void openInventory(EntityPlayer player){
	}

	@Override
	public void closeInventory(EntityPlayer player){
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound){
		super.readFromNBT(nbttagcompound);
		this.swapDetectionMode = nbttagcompound.getBoolean("seerStoneIsInverting");
		NBTTagList nbttaglist = nbttagcompound.getTagList("SeerStoneInventory", Constants.NBT.TAG_COMPOUND);
		inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++){
			String tag = String.format("ArrayIndex", i);
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte(tag);
			if (byte0 >= 0 && byte0 < inventory.length){
				inventory[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound){
		super.writeToNBT(nbttagcompound);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inventory.length; i++){
			if (inventory[i] != null){
				String tag = String.format("ArrayIndex", i);
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte(tag, (byte)i);
				inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setBoolean("seerStoneIsInverting", this.isInvertingDetection());

		nbttagcompound.setTag("SeerStoneInventory", nbttaglist);
		return nbttagcompound;
	}

	@Override
	public boolean canProvidePower(PowerTypes type){
		return false;
	}

	@Override
	public boolean hasCustomName(){
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack){
		return false;
	}

	@Override
	public int getChargeRate(){
		return 20;
	}

	@Override
	public List<PowerTypes> getValidPowerTypes(){
		return validTypes;
	}

	@Override
	public boolean canRelayPower(PowerTypes type){
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
}
