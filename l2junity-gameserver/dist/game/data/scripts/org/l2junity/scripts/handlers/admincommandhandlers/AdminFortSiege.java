/*
 * Copyright (C) 2004-2015 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2junity.scripts.handlers.admincommandhandlers;

import java.util.Collection;
import java.util.StringTokenizer;

import org.l2junity.gameserver.handler.AdminCommandHandler;
import org.l2junity.gameserver.handler.IAdminCommandHandler;
import org.l2junity.gameserver.instancemanager.FortManager;
import org.l2junity.gameserver.model.L2Clan;
import org.l2junity.gameserver.model.WorldObject;
import org.l2junity.gameserver.model.actor.instance.PlayerInstance;
import org.l2junity.gameserver.model.entity.Fort;
import org.l2junity.gameserver.network.client.send.NpcHtmlMessage;
import org.l2junity.gameserver.network.client.send.SystemMessage;
import org.l2junity.gameserver.network.client.send.string.SystemMessageId;
import org.l2junity.gameserver.scripting.annotations.GameScript;

/**
 * This class handles all siege commands: Todo: change the class name, and neaten it up
 */
public class AdminFortSiege implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_fortsiege",
		"admin_add_fortattacker",
		"admin_list_fortsiege_clans",
		"admin_clear_fortsiege_list",
		"admin_spawn_fortdoors",
		"admin_endfortsiege",
		"admin_startfortsiege",
		"admin_setfort",
		"admin_removefort"
	};
	
	@Override
	public boolean useAdminCommand(String command, PlayerInstance activeChar)
	{
		StringTokenizer st = new StringTokenizer(command, " ");
		command = st.nextToken(); // Get actual command
		
		// Get fort
		Fort fort = null;
		int fortId = 0;
		if (st.hasMoreTokens())
		{
			fortId = Integer.parseInt(st.nextToken());
			fort = FortManager.getInstance().getFortById(fortId);
		}
		// Get fort
		if (((fort == null) || (fortId == 0)))
		{
			// No fort specified
			showFortSelectPage(activeChar);
		}
		else
		{
			WorldObject target = activeChar.getTarget();
			PlayerInstance player = null;
			if (target instanceof PlayerInstance)
			{
				player = (PlayerInstance) target;
			}
			
			if (command.equalsIgnoreCase("admin_add_fortattacker"))
			{
				if (player == null)
				{
					activeChar.sendPacket(SystemMessageId.THAT_IS_AN_INCORRECT_TARGET);
				}
				else
				{
					if (fort.getSiege().addAttacker(player, false) == 4)
					{
						final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOUR_CLAN_HAS_BEEN_REGISTERED_TO_S1_S_FORTRESS_BATTLE);
						sm.addCastleId(fort.getResidenceId());
						player.sendPacket(sm);
					}
					else
					{
						player.sendMessage("During registering error occurred!");
					}
				}
			}
			else if (command.equalsIgnoreCase("admin_clear_fortsiege_list"))
			{
				fort.getSiege().clearSiegeClan();
			}
			else if (command.equalsIgnoreCase("admin_endfortsiege"))
			{
				fort.getSiege().endSiege();
			}
			else if (command.equalsIgnoreCase("admin_list_fortsiege_clans"))
			{
				activeChar.sendMessage("Not implemented yet.");
			}
			else if (command.equalsIgnoreCase("admin_setfort"))
			{
				if ((player == null) || (player.getClan() == null))
				{
					activeChar.sendPacket(SystemMessageId.THAT_IS_AN_INCORRECT_TARGET);
				}
				else
				{
					fort.endOfSiege(player.getClan());
				}
			}
			else if (command.equalsIgnoreCase("admin_removefort"))
			{
				L2Clan clan = fort.getOwnerClan();
				if (clan != null)
				{
					fort.removeOwner(true);
				}
				else
				{
					activeChar.sendMessage("Unable to remove fort");
				}
			}
			else if (command.equalsIgnoreCase("admin_spawn_fortdoors"))
			{
				fort.resetDoors();
			}
			else if (command.equalsIgnoreCase("admin_startfortsiege"))
			{
				fort.getSiege().startSiege();
			}
			
			showFortSiegePage(activeChar, fort);
		}
		return true;
	}
	
	private void showFortSelectPage(PlayerInstance activeChar)
	{
		int i = 0;
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(0, 1);
		adminReply.setFile(activeChar.getHtmlPrefix(), "data/html/admin/forts.htm");
		
		final Collection<Fort> forts = FortManager.getInstance().getForts();
		final StringBuilder cList = new StringBuilder(forts.size() * 100);
		
		for (Fort fort : forts)
		{
			if (fort != null)
			{
				cList.append("<td fixwidth=90><a action=\"bypass -h admin_fortsiege " + fort.getResidenceId() + "\">" + fort.getName() + " id: " + fort.getResidenceId() + "</a></td>");
				i++;
			}
			
			if (i > 2)
			{
				cList.append("</tr><tr>");
				i = 0;
			}
		}
		
		adminReply.replace("%forts%", cList.toString());
		activeChar.sendPacket(adminReply);
	}
	
	private void showFortSiegePage(PlayerInstance activeChar, Fort fort)
	{
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(0, 1);
		adminReply.setFile(activeChar.getHtmlPrefix(), "data/html/admin/fort.htm");
		adminReply.replace("%fortName%", fort.getName());
		adminReply.replace("%fortId%", String.valueOf(fort.getResidenceId()));
		activeChar.sendPacket(adminReply);
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	@GameScript
	public static void main()
	{
		AdminCommandHandler.getInstance().registerHandler(new AdminFortSiege());
	}
}