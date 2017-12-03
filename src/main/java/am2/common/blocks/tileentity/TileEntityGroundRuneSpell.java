package am2.common.blocks.tileentity;

import am2.api.spell.SpellData;
import am2.common.entity.EntityDummyCaster;
import am2.common.extensions.EntityExtension;
import am2.common.utils.DummyEntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityGroundRuneSpell extends TileEntity implements ITickable{
	private SpellData spell = null;
	private EntityPlayer caster = null;
	private String placedByName = null;

	private int numTriggers = 1;
	private boolean isPermanent = false;

	public TileEntityGroundRuneSpell(){
		
	}

	public void setSpellStack(SpellData spell){
		this.spell = spell.copy();
	}
	
	public SpellData getSpell() {
		return spell;
	}

	public void setNumTriggers(int triggers){
		this.numTriggers = triggers;
	}

	public int getNumTriggers(){
		return this.numTriggers;
	}

	public void setPermanent(boolean permanent){
		this.isPermanent = permanent;
	}

	public boolean getPermanent(){
		return this.isPermanent;
	}

	private void prepForActivate(){
		if (placedByName != null)
			caster = worldObj.getPlayerEntityByName(placedByName);
		if (caster == null){
			caster = DummyEntityPlayer.fromEntityLiving(new EntityDummyCaster(worldObj));
			EntityExtension.For(caster).setMagicLevelWithMana(99);
		}
	}
	
	public boolean canApply(EntityLivingBase entity) {
		if (spell == null) return false;
		prepForActivate();
		if (entity.getName().equals(placedByName)) return false;
		return true;
	}

	public boolean applySpellEffect(EntityLivingBase target){
		if (spell == null) return false;
		if (!canApply(target)) return false;
		prepForActivate();
		spell.execute(worldObj, caster, target, target.posX, target.posY, target.posZ, null);
		return true;
	}

	public void setPlacedBy(EntityLivingBase caster){
		if (caster instanceof EntityPlayer) this.placedByName = ((EntityPlayer)caster).getName();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		if (placedByName != null)
			compound.setString("placedByName", placedByName);
		if (spell != null)
			compound.setTag("spellStack", spell.writeToNBT(new NBTTagCompound()));
		compound.setInteger("numTrigger", numTriggers);
		compound.setBoolean("permanent", isPermanent);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound){
		if (compound.hasKey("placedByName"))
			placedByName = compound.getString("placedByName");
		if (compound.hasKey("spellStack"))
			spell = SpellData.readFromNBT(compound.getCompoundTag("spellStack"));
		numTriggers = compound.getInteger("numTrigger");
		isPermanent = compound.getBoolean("permanent");
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 0, writeToNBT(new NBTTagCompound()));
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void update() {
		worldObj.markAndNotifyBlock(pos, worldObj.getChunkFromBlockCoords(pos), worldObj.getBlockState(pos), worldObj.getBlockState(pos), 2);
	}
}
