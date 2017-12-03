package am2.common.spell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import am2.api.affinity.Affinity;
import am2.api.extensions.IEntityExtension;
import am2.api.extensions.ISpellCaster;
import am2.api.spell.AbstractSpellPart;
import am2.api.spell.SpellComponent;
import am2.api.spell.SpellModifier;
import am2.api.spell.SpellShape;
import am2.common.extensions.EntityExtension;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class SpellCaster implements ISpellCaster, ICapabilityProvider, ICapabilitySerializable<NBTBase> {
	
	@CapabilityInject(ISpellCaster.class)
	public static Capability<ISpellCaster> INSTANCE = null;
	
	private ArrayList<List<List<AbstractSpellPart>>> shapeGroups = new ArrayList<>();
	private ArrayList<List<AbstractSpellPart>> spellCommon = new ArrayList<>();
	private ArrayList<Float> shapeGroupCosts = new ArrayList<>();
	private HashMap<Affinity, Float> affinityShift = new HashMap<>();
	private UUID uuid = new UUID(0, 0);
	private int currentShapeGroup = 0;

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == INSTANCE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == INSTANCE)
			return (T) this;
		return null;
	}

	@Override
	public float getManaCost(World world, EntityLivingBase caster) {
		float manaCost = this.getBaseManaCost(currentShapeGroup);
		IEntityExtension ext = EntityExtension.For(caster);
		manaCost *= (1 + (ext.getCurrentBurnout() / ext.getMaxBurnout()));
		return manaCost;
	}

	@Override
	public boolean cast(World world, EntityLivingBase caster) {
		//TODO
		return false;
	}

	@Override
	public List<List<AbstractSpellPart>> getSpellCommon() {
		return ImmutableList.copyOf(spellCommon);
	}

	@Override
	public List<List<List<AbstractSpellPart>>> getShapeGroups() {
		return ImmutableList.copyOf(shapeGroups);
	}

	@Override
	public Map<Affinity, Float> getAffinityShift() {
		return ImmutableMap.copyOf(affinityShift);
	}

	@Override
	public int getShapeGroupCount() {
		return shapeGroups.size();
	}

	@Override
	public float getBaseManaCost(int shapeGroup) {
		Float f = shapeGroupCosts.get(MathHelper.clamp_int(shapeGroup, 0, shapeGroupCosts.size() - 1));
		return f != null ? f.floatValue() : 0;
	}

	@Override
	public UUID getSpellUUID() {
		return UUID.fromString(uuid.toString());
	}

	@Override
	public void setSpellCommon(List<List<AbstractSpellPart>> data) {
		this.spellCommon.clear();
		if (data != null) {
			for (List<AbstractSpellPart> ls : data) {
				if (ls != null) {
					ArrayList<AbstractSpellPart> parts = new ArrayList<>();
					for (AbstractSpellPart asp : ls) {
						if (asp != null) {
							parts.add(asp);
						}
					}
					spellCommon.add(parts);
				}
			}
		}
	}

	@Override
	public void setShapeGroups(List<List<List<AbstractSpellPart>>> data) {
		this.shapeGroups.clear();
		if (data != null) {
			for (List<List<AbstractSpellPart>> ls : data) {
				if (ls != null) {
					ArrayList<List<AbstractSpellPart>> stages = new ArrayList<>();
					for (List<AbstractSpellPart> ls2 : ls) {
						if (ls2 != null) {
							ArrayList<AbstractSpellPart> parts = new ArrayList<>();
							for (AbstractSpellPart asp : ls2) {
								if (asp != null) {
									parts.add(asp);
								}
							}
							stages.add(parts);
						}
					}
					shapeGroups.add(stages);
				}
			}
		}
	}

	@Override
	public void setAffinityShift(Map<Affinity, Float> shift) {
		this.affinityShift.clear();
		if (shift != null) {
			for (Entry<Affinity, Float> entry : shift.entrySet()) {
				if (entry.getValue() != null && entry.getKey() != null) {
					this.affinityShift.put(entry.getKey(), entry.getValue());
				}
			}
		}
	}

	@Override
	public void setBaseManaCost(int shapeGroup, float manaCost) {
		shapeGroupCosts.ensureCapacity(shapeGroup + 1);
		shapeGroupCosts.set(shapeGroup, manaCost);
	}

	@Override
	public void setUUID(UUID uuid) {
		this.uuid = uuid == null ? new UUID(0, 0) : uuid;
	}

	@Override
	public void gatherBaseManaCosts() {
		this.shapeGroupCosts.clear();
		for (int i = 0; i < shapeGroups.size(); i++) {
			float cost = 0F;
			float multiplier = 1F;
			float stageMultiplier = 1.0F;
			for (List<AbstractSpellPart> parts : shapeGroups.get(i)) {
				for (AbstractSpellPart part : parts) {
					if (part instanceof SpellModifier) {
						multiplier *= ((SpellModifier) part).getManaCostMultiplier();
					} else if (part instanceof SpellShape) {
						stageMultiplier *= ((SpellShape) part).manaCostMultiplier();
					}
				}
			}
			for (List<AbstractSpellPart> parts : spellCommon) {
				float _cost = 0F;
				float _multiplier = stageMultiplier;
				parts.sort((t, o)-> t.compareTo(o));
				for (AbstractSpellPart part : parts) {
					if (part instanceof SpellModifier) {
						multiplier *= ((SpellModifier) part).getManaCostMultiplier();
					} else if (part instanceof SpellShape) {
						_multiplier *= ((SpellShape) part).manaCostMultiplier();
						stageMultiplier = 1.0F;
					} else if (part instanceof SpellComponent) {
						_cost += ((SpellComponent) part).manaCost();
					}
				}
				
				cost += _cost * _multiplier;
			}
			shapeGroupCosts.ensureCapacity(i + 1);
			shapeGroupCosts.add(cost * multiplier);
		}
	}

	@Override
	public void gatherAffinityShift() {
		this.affinityShift.clear();
		for (List<AbstractSpellPart> parts : spellCommon) {
			for (AbstractSpellPart part : parts) {
				if (part instanceof SpellComponent) {
					for (Affinity aff : ((SpellComponent)part).getAffinity()) {
						if (affinityShift.get(aff) != null) {
							affinityShift.put(aff, affinityShift.get(aff) + ((SpellComponent)part).getAffinityShift(aff));
						} else {
							affinityShift.put(aff, ((SpellComponent)part).getAffinityShift(aff));
						}
					}
				}
			}
		}
	}

	@Override
	public void generateUUID() {
		uuid = UUID.randomUUID();
	}

	@Override
	public int getCurrentShapeGroup() {
		return currentShapeGroup;
	}

	@Override
	public void setCurentShapeGroup(int shapeGroup) {
		this.currentShapeGroup = MathHelper.clamp_int(shapeGroup, 0, shapeGroups.size() - 1);
	}
	
	@Override
	public boolean validate() {
		for (List<AbstractSpellPart> parts : getSpellCommon()) {
			if (parts == null)
				return false;
			boolean foundShape = false;
			for (AbstractSpellPart part : parts) {
				if (part == null)
					return false;
				if (part instanceof SpellShape) {
					if (foundShape)
						return false;
					else 
						foundShape = true;
				}
			}
		}
		for (List<List<AbstractSpellPart>> groups : getShapeGroups()) {
			if (groups == null)
				return false;
			boolean flag = false;
			for (List<AbstractSpellPart> parts : groups) {
				if (parts == null)
					return false;
				boolean foundShape = false;
				for (AbstractSpellPart part : parts) {
					if (part == null)
						return false;
					if (part instanceof SpellShape) {
						if (foundShape)
							return false;
						else {
							foundShape = true;
							flag = true;
						}
					}
				}
			}
			if (!flag)
				return false;
		}
		return true;
	}
	
	@Override
	public NBTBase serializeNBT() {
		return INSTANCE.writeNBT(this, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		INSTANCE.readNBT(this, null, nbt);
	}

}
