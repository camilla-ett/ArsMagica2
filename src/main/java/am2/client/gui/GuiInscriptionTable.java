package am2.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import am2.api.ArsMagicaAPI;
import am2.api.SkillRegistry;
import am2.api.SpellRegistry;
import am2.api.math.AMVector2;
import am2.api.skill.Skill;
import am2.api.spell.AbstractSpellPart;
import am2.api.spell.SpellComponent;
import am2.api.spell.SpellModifier;
import am2.api.spell.SpellShape;
import am2.client.gui.controls.GuiButtonVariableDims;
import am2.client.texture.SpellIconManager;
import am2.common.blocks.tileentity.TileEntityInscriptionTable;
import am2.common.container.ContainerInscriptionTable;
import am2.common.extensions.SkillData;
import am2.common.spell.SpellValidator.ValidationResult;
import am2.common.spell.shape.MissingShape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GuiInscriptionTable extends GuiContainer{

	private static final ResourceLocation background = new ResourceLocation("arsmagica2", "textures/gui/inscriptionTableGui.png");

	private final EntityPlayer usingPlayer;

	private final ArrayList<String> knownShapes;
	private final ArrayList<String> knownComponents;
	private final ArrayList<String> knownModifiers;

	private AbstractSpellPart hoveredItem;
	private TextureAtlasSprite hoveredIcon;
	private boolean dragging;
	private boolean lowerHover;
	private int lowerHoverIndex;
	private int lowerHoverShapeGroup = -1;

	private int lastMouseX;
	private int lastMouseY;

	private int iconX;
	private int iconY;
	int IIconXStart_upper = 41;
	int IIconYStart_upper = 5;

	int IIconXStart_lower = 41;
	int IIconYStart_lower = 146;

	int shapeGroupWidth = 37;
	int shapeGroupPadding = 3;

	int shapeGroupY = 108;
	int shapeGroupX = 13;

	AMVector2 searchFieldPosition;
	AMVector2 nameFieldPosition;

	AMVector2 searchFieldDimensions;
	AMVector2 nameFieldDimensions;

	int IIconStep = 17;

	private ValidationResult result;

	private GuiTextField searchBar;
	private GuiTextField nameBar;
	private GuiButtonVariableDims createSpellButton;
	private GuiButtonVariableDims resetSpellButton;

	private String defaultSearchLabel = "\2477\247o" + I18n.format("am2.gui.search");
	private String defaultNameLabel = "\2477\247o" + I18n.format("am2.gui.name");

	public GuiInscriptionTable(InventoryPlayer playerInventory, TileEntityInscriptionTable table){
		super(new ContainerInscriptionTable(table, playerInventory));
		usingPlayer = playerInventory.player;
		xSize = 220;
		ySize = 252;
		dragging = false;

		knownShapes = SkillData.For(this.usingPlayer).getKnownShapes();
		knownComponents = SkillData.For(this.usingPlayer).getKnownComponents();
		knownModifiers = SkillData.For(this.usingPlayer).getKnownModifiers();
	}

	@Override
	public boolean doesGuiPauseGame(){
		return false;
	}

	@Override
	public void initGui(){
		super.initGui();
		searchFieldPosition = new AMVector2(39, 59);
		searchFieldDimensions = new AMVector2(141, 12);
		searchBar = new GuiTextField(0, Minecraft.getMinecraft().fontRenderer, searchFieldPosition.iX, searchFieldPosition.iY, searchFieldDimensions.iX, searchFieldDimensions.iY);

		nameFieldPosition = new AMVector2(39, 93);
		nameFieldDimensions = new AMVector2(141, 12);
		nameBar = new GuiTextField(1, Minecraft.getMinecraft().fontRenderer, nameFieldPosition.iX, nameFieldPosition.iY, nameFieldDimensions.iX, nameFieldDimensions.iY);


		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;

		createSpellButton = new GuiButtonVariableDims(0, l - 65, i1, I18n.format("am2.gui.makeSpell"));
		createSpellButton.setDimensions(60, 20);

		resetSpellButton = new GuiButtonVariableDims(1, l + 120, i1 + 72, I18n.format("am2.gui.resetSpell"));
		resetSpellButton.setDimensions(60, 20);
		resetSpellButton.visible = false;

		if (usingPlayer.capabilities.isCreativeMode){
			this.buttonList.add(createSpellButton);
		}

		this.buttonList.add(resetSpellButton);

		nameBar.setText(((ContainerInscriptionTable)this.inventorySlots).getSpellName());
		if (nameBar.getText().equals("")){
			nameBar.setText(defaultNameLabel);
		}

		searchBar.setText(defaultSearchLabel);

		result = ((ContainerInscriptionTable)this.inventorySlots).validateCurrentDefinition();
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton){
		if (par1GuiButton == createSpellButton && usingPlayer.capabilities.isCreativeMode){
			((ContainerInscriptionTable)this.inventorySlots).giveSpellToPlayer(usingPlayer);
		}else if (par1GuiButton == resetSpellButton){
			((ContainerInscriptionTable)this.inventorySlots).resetSpellNameAndIcon();
		}
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) throws IOException{
		super.mouseClicked(par1, par2, par3);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		par1 -= l;
		par2 -= i1;

		if (hoveredItem != null && hoveredIcon != null){
			if (spellPartIsValidAddition(hoveredItem) && !lowerHover){
				dragging = true;
			}else if (lowerHover){
				if (lowerHoverShapeGroup == -1 && ((ContainerInscriptionTable)this.inventorySlots).currentRecipeContains(hoveredItem)){
					if (hoveredItem instanceof SpellShape){
						int index = lowerHoverIndex;
						int startIndex = index;
						int count = 0;
						index++;
						while (index < ((ContainerInscriptionTable)this.inventorySlots).getCurrentRecipeSize() && !(((ContainerInscriptionTable)this.inventorySlots).getRecipeItemAt(index) instanceof SpellShape)){
							count++;
							index++;
						}
						((ContainerInscriptionTable)this.inventorySlots).removeMultipleRecipeParts(startIndex, count);
					}else{
						((ContainerInscriptionTable)this.inventorySlots).removeSingleRecipePart(lowerHoverIndex);
					}
					result = ((ContainerInscriptionTable)this.inventorySlots).validateCurrentDefinition();
				}else if (lowerHoverShapeGroup >= 0){
					if (hoveredItem instanceof SpellShape){
						int index = lowerHoverIndex;
						int startIndex = index;
						int count = 0;
						index++;
						while (index < ((ContainerInscriptionTable)this.inventorySlots).getShapeGroupSize(lowerHoverShapeGroup) &&
								!(((ContainerInscriptionTable)this.inventorySlots).getShapeGroupPartAt(lowerHoverShapeGroup, index) instanceof SpellShape)){
							count++;
							index++;
						}
						((ContainerInscriptionTable)this.inventorySlots).removeMultipleRecipePartsFromGroup(lowerHoverShapeGroup, startIndex, count);
					}else{
						((ContainerInscriptionTable)this.inventorySlots).removeSingleRecipePartFromGroup(lowerHoverShapeGroup, lowerHoverIndex);
					}
					result = ((ContainerInscriptionTable)this.inventorySlots).validateCurrentDefinition();
				}
			}
		}else{
			boolean boxClick = false;
			if (par1 >= searchFieldPosition.iX && par1 <= searchFieldPosition.iX + searchFieldDimensions.iX){
				if (par2 >= searchFieldPosition.iY && par2 <= searchFieldPosition.iY + searchFieldDimensions.iY){
					if (par3 == 1 || searchBar.getText().equals(defaultSearchLabel)){
						searchBar.setText("");
					}
					if (nameBar.getText().equals("")){
						nameBar.setText(defaultNameLabel);
					}
					boxClick = true;
				}
			}
			if (par1 >= nameFieldPosition.iX && par1 <= nameFieldPosition.iX + nameFieldDimensions.iX){
				if (par2 >= nameFieldPosition.iY && par2 <= nameFieldPosition.iY + nameFieldDimensions.iY){
					if (par3 == 1 || nameBar.getText().equals(defaultNameLabel)){
						nameBar.setText("");
						((ContainerInscriptionTable)this.inventorySlots).setSpellName(nameBar.getText());
					}
					if (searchBar.getText().equals("")){
						searchBar.setText(defaultSearchLabel);
					}
					boxClick = true;
				}
			}
			if (!boxClick){
				if (nameBar.getText().equals("")){
					nameBar.setText(defaultNameLabel);
				}
				if (searchBar.getText().equals("")){
					searchBar.setText(defaultSearchLabel);
				}
			}
			searchBar.mouseClicked(par1, par2, par3);
			nameBar.mouseClicked(par1, par2, par3);
		}
	}
	
	@Override
	protected void mouseReleased(int x, int y, int action){
		super.mouseReleased(x, y, action);

		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		x -= l;
		y -= i1;

		if (action == 0 || action == 1){
			if (dragging){
				dragging = false;
				//lower section
				if (x >= IIconXStart_lower && x <= IIconXStart_lower + 150){
					if (y >= IIconYStart_lower && y <= IIconYStart_lower + 18){
						((ContainerInscriptionTable)this.inventorySlots).addRecipePart(hoveredItem);
						result = ((ContainerInscriptionTable)this.inventorySlots).validateCurrentDefinition();
					}
				}
				//spell stage groups
				int sg = ((ContainerInscriptionTable)this.inventorySlots).getNumStageGroups();
				for (int i = 0; i < sg; ++i){
					int SGX = shapeGroupX + ((shapeGroupWidth + shapeGroupPadding) * i);
					int SGY = shapeGroupY;
					if (x >= SGX && x <= SGX + shapeGroupWidth){
						if (y >= SGY && y <= SGY + shapeGroupWidth){
							((ContainerInscriptionTable)this.inventorySlots).addRecipePartToGroup(i, hoveredItem);
							result = ((ContainerInscriptionTable)this.inventorySlots).validateCurrentDefinition();
						}
					}
				}
			}
		}
	}

//	private void drawDropZones(){
//
//		int l = (width - xSize) / 2;
//		int i1 = (height - ySize) / 2;
//
//		drawRectangle(l + IIconXStart_upper, i1 + IIconYStart_upper, 150, 60, 0xFF0000);
//		drawRectangle(l + IIconXStart_lower, i1 + IIconYStart_lower, 150, 20, 0xFF0000);
//
//		int sg = ((ContainerInscriptionTable)this.inventorySlots).getNumStageGroups();
//		for (int i = 0; i < sg; ++i){
//			int SGX = l + shapeGroupX + ((shapeGroupWidth + shapeGroupPadding) * i);
//			int SGY = i1 + shapeGroupY;
//
//			drawRectangle(SGX, SGY, shapeGroupWidth, shapeGroupWidth, 0xFF0000);
//		}
//	}
//
//	private void drawRectangle(int x, int y, int width, int height, int color){
//		AMGuiHelper.line2d(x, y, x + width, y, this.zLevel, color);
//		AMGuiHelper.line2d(x + width, y, x + width, y + height, this.zLevel, color);
//		AMGuiHelper.line2d(x, y + height, x + width, y + height, this.zLevel, color);
//		AMGuiHelper.line2d(x, y, x, y + height, this.zLevel, color);
//	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException{
		if (searchBar.textboxKeyTyped(par1, par2)){

		}else if (nameBar.textboxKeyTyped(par1, par2)){
			((ContainerInscriptionTable)this.inventorySlots).setSpellName(nameBar.getText());
		}else{
			super.keyTyped(par1, par2);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j){
		mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, 165);
		drawTexturedModalRect(l + 22, i1 + 165, 0, 165, 176, 87);


		int offsetX = l + shapeGroupX;
		for (int sg = 0; sg < TileEntityInscriptionTable.MAX_STAGE_GROUPS; ++sg){
			if (sg >= ((ContainerInscriptionTable)this.inventorySlots).getNumStageGroups())
				GL11.glColor3f(0.5f, 0.5f, 0.5f);
			drawTexturedModalRect(offsetX + (sg * (shapeGroupWidth + shapeGroupPadding)), i1 + shapeGroupY, 176, 165, 37, 37);
		}

		GL11.glColor3f(1f, 1f, 1f);

		drawTexturedModalRect(l + 101, i1 + 73, 220, 0, 18, 18);

		lastMouseX = i - l;
		lastMouseY = j - i1;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2){
		super.drawGuiContainerForegroundLayer(par1, par2);

		ArrayList<String> label = new ArrayList<String>();

		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;

		this.zLevel = 0.0F;

		GL11.glEnable(GL11.GL_ALPHA_TEST);

		drawBookIcon();
		boolean hovering = false;
		if (drawAvailableParts(label)){
			hovering = true;
			lowerHover = false;
		}
		if (drawCurrentRecipe(label, l, i1)){
			hovering = true;
			lowerHover = true;
		}
		searchBar.drawTextBox();
		nameBar.drawTextBox();

		if (result.valid){
			if (((ContainerInscriptionTable)this.inventorySlots).slotHasStack(0)){
				if (((ContainerInscriptionTable)this.inventorySlots).slotIsBook(0)){
					Minecraft.getMinecraft().fontRenderer.drawSplitString(I18n.format("am2.gui.bookOut"), 225, 5, 100, 0xFF7700);
				}else{
					resetSpellButton.visible = true;
				}

			}else{
				resetSpellButton.visible = false;
			}
			createSpellButton.enabled = true;
		}else{
			if (((ContainerInscriptionTable)this.inventorySlots).slotHasStack(0) && !((ContainerInscriptionTable)this.inventorySlots).slotIsBook(0)){
				resetSpellButton.visible = true;
			}else{
				resetSpellButton.visible = false;
			}
			Minecraft.getMinecraft().fontRenderer.drawSplitString(result.message, 225, 5, 100, 0xFF7700);
			createSpellButton.enabled = false;
		}

		if (!dragging){
			if (hovering){
				drawHoveringText(label, lastMouseX, lastMouseY, Minecraft.getMinecraft().fontRenderer);
			}else{
				hoveredItem = null;
				hoveredIcon = null;
			}
		}else{
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			drawDraggedItem();
		}

	}

	private void drawBookIcon(){
		int bookX = this.inventorySlots.getSlot(0).xDisplayPosition;
		int bookY = this.inventorySlots.getSlot(0).yDisplayPosition;

		TextureAtlasSprite icon = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(Items.WRITABLE_BOOK);

		if (AMGuiHelper.instance.getFastTicker() < 20)
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.4f);
		else
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.7f);
		AMGuiHelper.DrawIconAtXY(icon, bookX, bookY, this.zLevel, 16, 16, true);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	private boolean drawCurrentRecipe(ArrayList<String> labelText, int l, int i1){

		iconX = IIconXStart_lower;
		iconY = IIconYStart_lower;

		boolean hovering = false;
		int index = 0;

		//main recipe
		for (int i = 0; i < ((ContainerInscriptionTable)this.inventorySlots).getCurrentRecipeSize(); ++i){
			AbstractSpellPart part = ((ContainerInscriptionTable)this.inventorySlots).getRecipeItemAt(i);
			if (part == new MissingShape())
				continue;
			String name = SpellRegistry.getSkillFromPart(part).getName();

			if (drawIcon(part, false)){
				labelText.add(name);
				hovering = true;
				lowerHoverIndex = index;
				lowerHoverShapeGroup = -1;
			}
			index++;
		}

		//shape groups
		for (int i = 0; i < ((ContainerInscriptionTable)this.inventorySlots).getNumStageGroups(); ++i){
			for (int n = 0; n < ((ContainerInscriptionTable)this.inventorySlots).getShapeGroupSize(i); ++n){
				AbstractSpellPart part = ((ContainerInscriptionTable)this.inventorySlots).getShapeGroupPartAt(i, n);
				String name = SpellRegistry.getSkillFromPart(part).getName();

				int SGX = shapeGroupX + ((shapeGroupWidth + shapeGroupPadding) * i) + 1;
				int SGY = shapeGroupY;

				iconX = SGX + (n % 2) * IIconStep;
				iconY = SGY + (int)Math.floor(n / 2) * IIconStep;

				if (drawIcon(part, false)){
					labelText.add(name);
					hovering = true;
					lowerHoverIndex = n;
					lowerHoverShapeGroup = i;
				}
			}
		}
		return hovering;
	}

	private boolean drawAvailableParts(ArrayList<String> labelText){

		iconX = IIconXStart_upper;
		iconY = IIconYStart_upper;

		boolean b = drawPartIcons(labelText);

		return b;
	}

	private boolean drawPartIcons(ArrayList<String> labelText){
		boolean hovering = false;
		hovering |= drawIconSet(knownShapes, labelText);
		hovering |= drawIconSet(knownComponents, labelText);
		hovering |= drawIconSet(knownModifiers, labelText);
		return hovering;
	}
	
	private boolean drawIconSet(ArrayList<String> ids, ArrayList<String> labelText){
		boolean hovering = false;
		for (String i : ids){
			Skill part = SkillRegistry.getSkillFromName(i);

			if (part == null || GameRegistry.findRegistry(AbstractSpellPart.class).getValue(part.getRegistryName()) == null)// && SkillTreeManager.instance.isSkillDisabled(part))
				continue;

			String name = part.getName();

			String filterText = searchBar.getText().toLowerCase();
			if (filterText != "" && !filterText.equals(defaultSearchLabel.toLowerCase()) && !name.toLowerCase().contains(filterText)){
				continue;
			}

			if (iconY < 0 || iconY > 42) return hovering;
			if (drawIcon(GameRegistry.findRegistry(AbstractSpellPart.class).getValue(part.getRegistryName()))){
				hovering = true;
				labelText.add(name);
			}
		}
		return hovering;
	}

	private boolean spellPartIsValidAddition(AbstractSpellPart part){
		boolean hasShape = false;
		for (int i = 0; i < ((ContainerInscriptionTable)this.inventorySlots).getNumStageGroups(); ++i){
			for (int n = 0; n < ((ContainerInscriptionTable)this.inventorySlots).getShapeGroupSize(i); ++n){
				AbstractSpellPart groupPart = ((ContainerInscriptionTable)this.inventorySlots).getShapeGroupPartAt(i, n);
				if (groupPart instanceof SpellShape){
					hasShape = true;
					break;
				}
			}
		}
		if (!hasShape && !(part instanceof SpellShape))
			return false;
		if (part instanceof SpellShape && ((ContainerInscriptionTable)this.inventorySlots).currentRecipeContains(part))
			return false;
		if (part instanceof SpellComponent){
			int index = ((ContainerInscriptionTable)this.inventorySlots).getCurrentRecipeSize() - 1;
			while (index >= 0 && !(((ContainerInscriptionTable)this.inventorySlots).getRecipeItemAt(index) instanceof SpellShape)){
				AbstractSpellPart curPart = ((ContainerInscriptionTable)this.inventorySlots).getRecipeItemAt(index--);
				if (curPart instanceof SpellComponent && SpellRegistry.getSkillFromPart(curPart).getID() == SpellRegistry.getSkillFromPart(part).getID()){
					return false;
				}
			}
		}
		if (part instanceof SpellModifier){
			return ((ContainerInscriptionTable)this.inventorySlots).modifierCanBeAdded((SpellModifier)part);
		}
		return true;
	}

	private boolean drawIcon(AbstractSpellPart part){
		return drawIcon(part, true);
	}

	private boolean drawIcon(AbstractSpellPart part, boolean allowDarken){

		boolean hovering = false;
		TextureAtlasSprite shapeIcon = SpellIconManager.INSTANCE.getSprite(SpellRegistry.getSkillFromPart(part).getID());

		if (shapeIcon == null)
			return false;

		if (!currentSpellDefIsReadOnly()){
			if (!spellPartIsValidAddition(part) && allowDarken){
				GL11.glColor3f(0.3f, 0.3f, 0.3f);
			}else{
				GL11.glColor3f(1.0f, 1.0f, 1.0f);
			}
		}else{
			GL11.glColor3f(1.0f, 0.7f, 0.7f);
		}

		AMGuiHelper.DrawIconAtXY(shapeIcon, iconX, iconY, this.zLevel, 16, 16, false);

		if (!dragging){
			if (lastMouseX > iconX && lastMouseX < iconX + 16){
				if (lastMouseY > iconY && lastMouseY < iconY + 16){
					hoveredItem = part;
					hoveredIcon = shapeIcon;
					hovering = true;
				}
			}
		}

		iconX += IIconStep;
		if (iconX >= 175){
			iconX = IIconXStart_upper;
			iconY += 17;
		}

		return hovering;
	}

	private void drawDraggedItem(){
		AMGuiHelper.DrawIconAtXY(hoveredIcon, lastMouseX - 8, lastMouseY - 8, this.zLevel, 16, 16, false);
	}

	@Override
	protected void drawHoveringText(List<String> par1List, int par2, int par3, FontRenderer font){
		if (!par1List.isEmpty()){
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			int k = 0;
			Iterator<String> iterator = par1List.iterator();

			while (iterator.hasNext()){
				String s = (String)iterator.next();
				int l = font.getStringWidth(s);

				if (l > k){
					k = l;
				}
			}

			int i1 = par2 + 12;
			int j1 = par3 - 12;
			int k1 = 8;

			if (par1List.size() > 1){
				k1 += 2 + (par1List.size() - 1) * 10;
			}

			if (i1 + k > this.width){
				i1 -= 28 + k;
			}

			if (j1 + k1 + 6 > this.height){
				j1 = this.height - k1 - 6;
			}

			this.zLevel = 300.0F;
			int l1 = -267386864;
			this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
			this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
			this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
			this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
			this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
			int i2 = 1347420415;
			int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
			this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
			this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
			this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
			this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

			for (int k2 = 0; k2 < par1List.size(); ++k2){
				String s1 = (String)par1List.get(k2);
				font.drawStringWithShadow(s1, i1, j1, -1);

				if (k2 == 0){
					j1 += 2;
				}

				j1 += 10;
			}

			this.zLevel = 0.0F;
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			RenderHelper.enableStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}

	private boolean currentSpellDefIsReadOnly(){
		return ((ContainerInscriptionTable)this.inventorySlots).currentSpellDefIsReadOnly();
	}
}
