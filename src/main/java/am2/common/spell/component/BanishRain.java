package am2.common.spell.component;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.ArsMagica2;
import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
import am2.api.blocks.IMultiblock;
import am2.api.rituals.IRitualInteraction;
import am2.api.rituals.RitualShapeHelper;
import am2.api.spell.SpellComponent;
import am2.api.spell.SpellData;
import am2.api.spell.SpellModifiers;
import am2.client.particles.AMParticle;
import am2.client.particles.ParticleFloatUpward;
import am2.common.defs.ItemDefs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BanishRain extends SpellComponent implements IRitualInteraction{

	@Override
	public boolean applyEffectBlock(SpellData spell, World world, BlockPos pos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		boolean hasMatch = RitualShapeHelper.instance.matchesRitual(this, world, pos);
		if (hasMatch){
			RitualShapeHelper.instance.consumeReagents(this, world, pos);
			world.getWorldInfo().setRainTime(0);
			world.getWorldInfo().setRaining(true);
			return true;
		}
		if (!world.isRaining()) return false;
		world.getWorldInfo().setRainTime(24000);
		world.getWorldInfo().setRaining(false);
		return true;
	}

	@Override
	public boolean applyEffectEntity(SpellData spell, World world, EntityLivingBase caster, Entity target){
		boolean hasMatch = RitualShapeHelper.instance.matchesRitual(this, world, caster.getPosition());
		if (hasMatch){
			RitualShapeHelper.instance.consumeReagents(this, world, target.getPosition());
			world.getWorldInfo().setRainTime(0);
			world.getWorldInfo().setRaining(true);
			return true;
		}
		if (!world.isRaining()) return false;
		world.getWorldInfo().setRainTime(24000);
		world.getWorldInfo().setRaining(false);
		return true;
	}

	@Override
	public float manaCost(){
		return 750;
	}
	
	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.noneOf(SpellModifiers.class);
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		int waterMeta = 0;
		for (Affinity aff : GameRegistry.findRegistry(Affinity.class).getValues()) {
			if (aff.equals(Affinity.NONE))
				continue;				
			if (aff.equals(Affinity.WATER))
				break;
			waterMeta++;
		}
		return new ItemStack[]{new ItemStack(ItemDefs.essence, 1, waterMeta)};
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		for (int i = 0; i < 25; ++i){
			AMParticle particle = (AMParticle)ArsMagica2.proxy.particleManager.spawn(world, "water_ball", x, y, z);
			if (particle != null){
				particle.addRandomOffset(5, 4, 5);
				particle.AddParticleController(new ParticleFloatUpward(particle, 0f, 0.5f, 1, false));
				particle.setMaxAge(25 + rand.nextInt(10));
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(Affinity.WATER);
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.BLUE.getDyeDamage()),
				Items.GOLD_INGOT
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.3f;
	}

	@Override
	public IMultiblock getRitualShape(){
		return RitualShapeHelper.instance.hourglass;
	}

	@Override
	public ItemStack[] getRitualReagents(){
		return new ItemStack[]{
				new ItemStack(Items.WATER_BUCKET),
				new ItemStack(Blocks.SNOW)
		};
	}

	@Override
	public int getRitualReagentSearchRadius(){
		return 3;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getResult() {
		return null;
	}
}
