package am2.client.commands;

import am2.client.gui.GuiHudCustomization;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class ConfigureAMUICommand extends CommandBase{

	private static boolean showGUI = false;

	public static void showIfQueued(){
		if (showGUI){
			Minecraft.getMinecraft().displayGuiScreen(new GuiHudCustomization());
			showGUI = false;
		}
	}

	@Override
	public int getRequiredPermissionLevel(){
		return 0;
	}

	@Override
	public List<String> getAliases(){
		ArrayList<String> aliases = new ArrayList<String>();
		aliases.add("AMUICFG");
		return aliases;
	}

	@Override
	public String getName() {
		return "amuicfg";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/amuicfg";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		showGUI = true;
	}

}
