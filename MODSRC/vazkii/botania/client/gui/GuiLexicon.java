/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 6:48:05 PM (GMT)]
 */
package vazkii.botania.client.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.gui.button.GuiButtonInvisible;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.item.ItemLexicon;

public class GuiLexicon extends GuiScreen {

	public static GuiLexicon currentOpenLexicon = new GuiLexicon();
	public static ItemStack stackUsed;

	public static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_LEXICON);

	String title;
	int guiWidth = 146;
	int guiHeight = 180;
	int left, top;
	
	@Override
	public void initGui() {
		super.initGui();
		title = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getDisplayName();
		currentOpenLexicon = this;

		left = width / 2 - guiWidth / 2;
		top = height / 2 - guiHeight / 2;

		buttonList.clear();
		if(isIndex()) {
			int x = 18;
			for(int i = 0; i < 12; i++) {
				int y = 16 + i * 12;
				buttonList.add(new GuiButtonInvisible(i, left + x, top + y, 110, 10, ""));
			}
			populateIndex();
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		mc.renderEngine.bindTexture(texture);
		drawTexturedModalRect(left, top, 0, 0, guiWidth, guiHeight);
		drawCenteredString(fontRendererObj, getTitle(), left + guiWidth / 2, top - getTitleHeight(), 0x00FF00);
		String subtitle = getSubtitle();
		if(subtitle != null) {
			GL11.glScalef(0.5F, 0.5F, 1F);
			drawCenteredString(fontRendererObj, subtitle, left * 2 + guiWidth, (top - getTitleHeight() + 10) * 2, 0x00FF00);
			GL11.glScalef(2F, 2F, 1F);
		}

		drawHeader();

		super.drawScreen(par1, par2, par3);
	}

	void drawHeader() {
		boolean unicode = fontRendererObj.getUnicodeFlag();
		fontRendererObj.setUnicodeFlag(true);
		fontRendererObj.drawSplitString(String.format(StatCollector.translateToLocal("botania.gui.lexicon.header"), ItemLexicon.getEdition()), left + 18, top + 14, 110, 0);
		fontRendererObj.setUnicodeFlag(unicode);
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		int i = par1GuiButton.id - 3;
		if(i < 0)
			return;

		List<LexiconCategory> categoryList = BotaniaAPI.getAllCategories();
		LexiconCategory category = i >= categoryList.size() ? null : categoryList.get(i);

		if(category != null) {
			mc.displayGuiScreen(new GuiLexiconIndex(category));
			ClientTickHandler.notifyPageChange();
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	String getTitle() {
		return title;
	}

	String getSubtitle() {
		return null;
	}

	int getTitleHeight() {
		return getSubtitle() == null ? 12 : 16;
	}

	boolean isIndex() {
		return true;
	}

	void populateIndex() {
		List<LexiconCategory> categoryList = BotaniaAPI.getAllCategories();
		for(int i = 3; i < 12; i++) {
			int i_ = i - 3;
			GuiButtonInvisible button = (GuiButtonInvisible) buttonList.get(i);
			LexiconCategory category = i_ >= categoryList.size() ? null : categoryList.get(i_);
			if(category != null)
				button.displayString = StatCollector.translateToLocal(category.getUnlocalizedName());
			else button.displayString = "";
		}
	}

}
