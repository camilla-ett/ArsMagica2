package am2.common.blocks.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import am2.ArsMagica2;
import am2.api.DamageSources;
import am2.api.blocks.IMultiblock;
import am2.api.blocks.Multiblock;
import am2.api.blocks.MultiblockGroup;
import am2.api.blocks.TypedMultiblockGroup;
import am2.client.particles.AMLineArc;
import am2.common.blocks.BlockArsMagicaBlock;
import am2.common.buffs.BuffEffectAstralDistortion;
import am2.common.buffs.BuffEffectManaRegen;
import am2.common.defs.BlockDefs;
import am2.common.defs.PotionEffectsDefs;
import am2.common.entity.EntityAirSled;
import am2.common.entity.EntityBroom;
import am2.common.entity.EntityDarkling;
import am2.common.entity.EntityFlicker;
import am2.common.entity.EntityShadowHelper;
import am2.common.entity.EntityThrownRock;
import am2.common.entity.EntityThrownSickle;
import am2.common.entity.EntityWinterGuardianArm;
import am2.common.power.PowerNodeRegistry;
import am2.common.power.PowerTypes;
import am2.common.utils.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class TileEntityBlackAurem extends TileEntityObelisk {

	private final HashMap<EntityLivingBase, AMLineArc> arcs;
	private final ArrayList<EntityLivingBase> cachedEntities;
	private int ticksSinceLastEntityScan = 0;
	
	@SuppressWarnings("unchecked")
	public TileEntityBlackAurem(){
		super(10000);

		arcs = new HashMap<>();

		cachedEntities = new ArrayList<EntityLivingBase>();

		structure = new Multiblock("blackaurem_structure");

		pillars = new MultiblockGroup("pillars", Lists.newArrayList(Blocks.NETHER_BRICK.getDefaultState()), false);

		this.caps = new HashMap<>();
		capsGroup = new TypedMultiblockGroup("caps", Lists.newArrayList(
				createMap(BlockDefs.blocks.getDefaultState().withProperty(BlockArsMagicaBlock.BLOCK_TYPE, BlockArsMagicaBlock.EnumBlockType.SUNSTONE)),
				createMap(BlockDefs.blocks.getDefaultState().withProperty(BlockArsMagicaBlock.BLOCK_TYPE, BlockArsMagicaBlock.EnumBlockType.CHIMERITE)),
				createMap(Blocks.OBSIDIAN.getDefaultState())), false);
		this.caps.put(BlockDefs.blocks.getDefaultState().withProperty(BlockArsMagicaBlock.BLOCK_TYPE, BlockArsMagicaBlock.EnumBlockType.CHIMERITE), 1.1f);
		this.caps.put(Blocks.OBSIDIAN.getDefaultState(), 1.5f);
		this.caps.put(BlockDefs.blocks.getDefaultState().withProperty(BlockArsMagicaBlock.BLOCK_TYPE, BlockArsMagicaBlock.EnumBlockType.SUNSTONE), 2f);
		
		MultiblockGroup aurem = new MultiblockGroup("aurem", Lists.newArrayList(BlockDefs.blackAurem.getDefaultState()), true);
		
		aurem.addBlock(BlockPos.ORIGIN);
		
		capsGroup.addBlock(new BlockPos(-2, 2, -2), 0);
		capsGroup.addBlock(new BlockPos(2, 2, -2), 0);
		capsGroup.addBlock(new BlockPos(-2, 2, 2), 0);
		capsGroup.addBlock(new BlockPos(2, 2, 2), 0);

		pillars.addBlock(new BlockPos (-2, 0, -2));
		pillars.addBlock(new BlockPos (-2, 1, -2));

		pillars.addBlock(new BlockPos (2, 0, -2));
		pillars.addBlock(new BlockPos (2, 1, -2));

		pillars.addBlock(new BlockPos (-2, 0, 2));
		pillars.addBlock(new BlockPos (-2, 1, 2));

		pillars.addBlock(new BlockPos (2, 0, 2));
		pillars.addBlock(new BlockPos (2, 1, 2));

		wizardChalkCircle = addWizChalkGroupToStructure(structure);
		structure.addGroup(pillars);
		structure.addGroup(capsGroup);
		structure.addGroup(wizardChalkCircle);
		structure.addGroup(aurem);
	}

	@Override
	public void update(){
		if (worldObj.isRemote){
			Iterator<EntityLivingBase> arcIterator = arcs.keySet().iterator();
			ArrayList<Entity> toRemove = new ArrayList<Entity>();
			while (arcIterator.hasNext()){
				EntityLivingBase arcEnt = arcIterator.next();
				AMLineArc arc = (AMLineArc)arcs.get(arcEnt);
				if (arcEnt == null || arcEnt.isDead || arc == null || !arc.isAlive() || arcEnt.getDistanceSq(pos) > 100 || EntityUtils.isSummon(arcEnt))
					toRemove.add(arcEnt);
			}

			for (Entity e : toRemove){
				arcs.remove(e);
			}
		}else{
			surroundingCheckTicks++;
		}

		if (worldObj.isRemote || ticksSinceLastEntityScan++ > 25){
			updateNearbyEntities();
			ticksSinceLastEntityScan = 0;
		}

		Iterator<EntityLivingBase> it = cachedEntities.iterator();
		while (it.hasNext()){
			
			EntityLivingBase ent = it.next();
			
			if (!ent.isPotionActive(PotionEffectsDefs.ASTRAL_DISTORTION))
				ent.addPotionEffect(new BuffEffectAstralDistortion(600, 0));
			
			if (ent.isDead || ent.getDistanceSq(pos) > 100){
				it.remove();
				continue;
			}

			RayTraceResult mop = this.world.rayTraceBlocks(new Vec3d(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5), new Vec3d(ent.posX, ent.posY + ent.getEyeHeight(), ent.posZ), false);

			if (EntityUtils.isSummon(ent) || mop != null){
				continue;
			}

			ent.motionY = 0;
			ent.motionX = 0;
			ent.motionZ = 0;
			double deltaX = this.pos.getX() + 0.5f - ent.posX;
			double deltaZ = this.pos.getZ() + 0.5f - ent.posZ;
			double angle = Math.atan2(deltaZ, deltaX);

			double offsetX = Math.cos(angle) * 0.1;
			double offsetZ = Math.sin(angle) * 0.1;
			double offsetY = 0.05f;

			double distanceHorizontal = deltaX * deltaX + deltaZ * deltaZ;
			double distanceVertical = this.pos.getY() - ent.posY;
			if (distanceHorizontal < 1.3 * Math.max(1, Math.abs(distanceVertical / 2))){
				if (distanceVertical < -1.5){
					if (worldObj.isRemote && worldObj.rand.nextInt(10) < 3){
						ArsMagica2.proxy.particleManager.BoltFromPointToPoint(worldObj, pos.getX() + 0.5, pos.getY() + 1.3, pos.getZ() + 0.5, ent.posX, ent.posY, ent.posZ, 4, 0x000000);
					}
				}
				if (distanceVertical < -2){
					offsetY = 0;
					if (!worldObj.isRemote){
						if (ent.attackEntityFrom(DamageSources.darkNexus, 4)){
							if (ent.getHealth() <= 0){
								ent.setDead();
								float power = ((int)Math.ceil((ent.getMaxHealth() * (ent.ticksExisted / 20)) % 5000)) * this.powerMultiplier;
								PowerNodeRegistry.For(this.world).insertPower(this, PowerTypes.DARK, power);
							}
						}
					}
				}
			}

			if (worldObj.isRemote){
				if (!arcs.containsKey(ent)){
					AMLineArc arc = (AMLineArc)ArsMagica2.proxy.particleManager.spawn(worldObj, "textures/blocks/oreblocksunstone.png", pos.getX() + 0.5, pos.getY() + 1.3, pos.getZ() + 0.5, ent);
					if (arc != null){
						arc.setExtendToTarget();
						arc.setRBGColorF(1, 1, 1);
					}
					arcs.put(ent, arc);
				}
			}
			if (!worldObj.isRemote)
				ent.moveEntity(offsetX, offsetY, offsetZ);
		}
		if (surroundingCheckTicks % 100 == 0){
			checkNearbyBlockState();
			surroundingCheckTicks = 1;
			if (!worldObj.isRemote && PowerNodeRegistry.For(this.world).checkPower(this, this.capacity * 0.1f)){
				List<EntityPlayer> nearbyPlayers = worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.pos.add(-2, 0, -2), pos.add(2, 3, 2)));
				for (EntityPlayer p : nearbyPlayers){
					if (p.isPotionActive(PotionEffectsDefs.MANA_REGEN)) continue;
					p.addPotionEffect(new BuffEffectManaRegen(600, 2));
				}
			}

			//TODO:
			/*if (rand.nextDouble() < (this.getCharge() / this.getCapacity()) * 0.01){
					int maxSev = (int)Math.ceil((this.getCharge() / this.getCapacity()) * 2) + rand.nextInt(2);
					IllEffectsManager.instance.ApplyRandomBadThing(this, IllEffectSeverity.values()[maxSev], BadThingTypes.DARKNEXUS);
				}*/
		}

		super.callSuperUpdate();
	}

	private void updateNearbyEntities(){
		List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.add(-10, 0, -10), pos.add(10, 4, 10)));
		for (EntityLivingBase entity : nearbyEntities){
			if (entity.isEntityInvulnerable(DamageSources.darkNexus) ||
					!entity.isNonBoss() ||
					entity instanceof EntityDarkling ||
					entity instanceof EntityPlayer ||
					entity instanceof EntityAirSled ||
					entity instanceof EntityWinterGuardianArm ||
					entity instanceof EntityThrownSickle ||
					entity instanceof EntityFlicker ||
					entity instanceof EntityShadowHelper||
					entity instanceof EntityThrownRock||
					entity instanceof EntityBroom
					)
				continue;
			if (!cachedEntities.contains(entity))
				cachedEntities.add(entity);
		}

		ticksSinceLastEntityScan = 0;
	}

	@Override
	public IMultiblock getMultiblockStructure() {
		return structure;
	}

	@Override
	public boolean canRequestPower(){
		return false;
	}

	@Override
	public boolean canProvidePower(PowerTypes type){
		return type == PowerTypes.DARK;
	}

	@Override
	public List<PowerTypes> getValidPowerTypes(){
		return Lists.newArrayList(PowerTypes.DARK);
	}

	@Override
	public int getSizeInventory(){
		return 0;
	}
}
