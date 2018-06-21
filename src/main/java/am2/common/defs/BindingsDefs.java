package am2.common.defs;

import org.lwjgl.input.Keyboard;

import am2.ArsMagica2;
import am2.api.extensions.ISpellCaster;
import am2.client.gui.AuraCustomizationMenu;
import am2.common.extensions.AffinityData;
import am2.common.items.ItemSpellBook;
import am2.common.packet.AMDataWriter;
import am2.common.packet.AMNetHandler;
import am2.common.packet.AMPacketIDs;
import am2.common.spell.SpellCaster;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BindingsDefs {
	public static final KeyBinding ICE_BRIDGE = new KeyBinding("key.am2.bridge", Keyboard.KEY_I, "keybindings.am2");
	public static final KeyBinding ENDER_TP = new KeyBinding("key.am2.teleport", Keyboard.KEY_N, "keybindings.am2");
	public static final KeyBinding SHAPE_GROUP = new KeyBinding("key.am2.shape_groups", Keyboard.KEY_C, "keybindings.am2");
	public static final KeyBinding AURA_CUSTOMIZATION = new KeyBinding("key.am2.aura_customization", Keyboard.KEY_B, "keybindings.am2");
	public static final KeyBinding NIGHT_VISION = new KeyBinding("key.am2.dark_vision", Keyboard.KEY_L, "keybindings.am2");
	public static final KeyBinding SPELL_BOOK_PREV = new KeyBinding("key.am2.spellbookprev", Keyboard.KEY_Z, "keybindings.am2");
	public static final KeyBinding SPELL_BOOK_NEXT = new KeyBinding("key.am2.spellbooknext", Keyboard.KEY_X, "keybindings.am2");

	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event){
		EntityPlayer clientPlayer = FMLClientHandler.instance().getClient().player;

//		if (Minecraft.getMinecraft().currentScreen != null){
//			if (Minecraft.getMinecraft().currentScreen instanceof GuiInventory){
//				if (ManaToggleKey.isPressed()){
//					boolean curDisplayFlag = ArsMagica2.config.displayManaInInventory();
//					ArsMagica2.config.setDisplayManaInInventory(!curDisplayFlag);
//				}
//			}
//			return;
//		}
		if (NIGHT_VISION.isPressed())
			AMNetHandler.INSTANCE.sendAbilityToggle(AffinityData.NIGHT_VISION);
		else if (ICE_BRIDGE.isPressed())
			AMNetHandler.INSTANCE.sendAbilityToggle(AffinityData.ICE_BRIDGE_STATE);
		else if (SHAPE_GROUP.isPressed()){
			EntityPlayer player = Minecraft.getMinecraft().player;
			ItemStack curItem = player.inventory.getStackInSlot(player.inventory.currentItem);
			if (curItem == null || (curItem.getItem() != ItemDefs.spell && curItem.getItem() != ItemDefs.spellBook && curItem.getItem() != ItemDefs.arcaneSpellbook)){
				return;
			}
			int shapeGroup;
			if (curItem.getItem() == ItemDefs.spell && curItem.hasCapability(SpellCaster.INSTANCE, null)) {
				ISpellCaster caster = curItem.getCapability(SpellCaster.INSTANCE, null);
				shapeGroup = caster.getCurrentShapeGroup() + 1;
				if (shapeGroup >= caster.getShapeGroupCount())
					shapeGroup = 0;
			} else {
				ItemStack spellStack = ((ItemSpellBook)curItem.getItem()).GetActiveItemStack(curItem);
				if (spellStack == null || !spellStack.hasCapability(SpellCaster.INSTANCE, null)){
					return;
				}
				ISpellCaster caster = spellStack.getCapability(SpellCaster.INSTANCE, null);
				shapeGroup = caster.getCurrentShapeGroup() + 1;
				if (shapeGroup >= caster.getShapeGroupCount())
					shapeGroup = 0;
				((ItemSpellBook)curItem.getItem()).replaceActiveItemStack(curItem, spellStack);
			}

			AMNetHandler.INSTANCE.sendShapeGroupChangePacket(shapeGroup, clientPlayer.getEntityId());

		}
		else if (SPELL_BOOK_NEXT.isPressed()){
			EntityPlayer player = Minecraft.getMinecraft().player;
			ItemStack curItem = player.getHeldItem(EnumHand.MAIN_HAND);
			if (curItem != null && curItem.getItem() instanceof ItemSpellBook){
				AMNetHandler.INSTANCE.sendPacketToServer(
					AMPacketIDs.SPELLBOOK_CHANGE_ACTIVE_SLOT,
					new AMDataWriter()
						.add(ItemSpellBook.ID_NEXT_SPELL)
						.add(player.getEntityId())
						.add(player.inventory.currentItem)
						.generate());
			}
		}
		else if (SPELL_BOOK_PREV.isPressed()){
			EntityPlayer player = Minecraft.getMinecraft().player;
			ItemStack curItem = player.getHeldItem(EnumHand.MAIN_HAND);
			if (curItem != null && curItem.getItem() instanceof ItemSpellBook){
				AMNetHandler.INSTANCE.sendPacketToServer(
					AMPacketIDs.SPELLBOOK_CHANGE_ACTIVE_SLOT,
					new AMDataWriter()
						.add(ItemSpellBook.ID_PREV_SPELL)
						.add(player.getEntityId())
						.add(player.inventory.currentItem)
						.generate());
			}
		}
//		else if (this.SpellBookNextSpellKey.isPressed()){
//			EntityPlayer player = Minecraft.getMinecraft().player;
//			ItemStack curItem = player.inventory.getStackInSlot(player.inventory.currentItem);
//			if (curItem == null){
//				return;
//			}
//			if (curItem.getItem() == ItemDefs.spellBook || curItem.getItem() == ItemDefs.arcaneSpellbook){
//				//send packet to server
//				AMNetHandler.INSTANCE.sendSpellbookSlotChange(player, player.inventory.currentItem, ItemSpellBook.ID_NEXT_SPELL);
//			}
//		}else if (this.SpellBookPrevSpellKey.isPressed()){
//			EntityPlayer player = Minecraft.getMinecraft().player;
//			ItemStack curItem = player.inventory.getStackInSlot(player.inventory.currentItem);
//			if (curItem == null){
//				return;
//			}
//			if (curItem.getItem() == ItemDefs.spellBook || curItem.getItem() == ItemDefs.arcaneSpellbook){
//				//send packet to server
//				AMNetHandler.INSTANCE.sendSpellbookSlotChange(player, player.inventory.currentItem, ItemSpellBook.ID_PREV_SPELL);
//			}
//		}
		else if (AURA_CUSTOMIZATION.isPressed()){
			if (ArsMagica2.proxy.playerTracker.hasAA(clientPlayer)){
				Minecraft.getMinecraft().displayGuiScreen(new AuraCustomizationMenu());
			}
		}
	}	
}
