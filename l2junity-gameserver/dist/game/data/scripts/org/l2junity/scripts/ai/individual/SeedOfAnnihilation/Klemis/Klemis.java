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
package org.l2junity.scripts.ai.individual.SeedOfAnnihilation.Klemis;

import org.l2junity.gameserver.model.Location;
import org.l2junity.gameserver.model.actor.Npc;
import org.l2junity.gameserver.model.actor.instance.PlayerInstance;
import org.l2junity.gameserver.scripting.annotations.GameScript;

import org.l2junity.scripts.ai.AbstractNpcAI;

/**
 * Klemis AI.
 * @author St3eT
 */
public final class Klemis extends AbstractNpcAI
{
	// NPC
	private static final int KLEMIS = 32734; // Klemis
	// Location
	private static final Location LOCATION = new Location(-180218, 185923, -10576);
	// Misc
	private static final int MIN_LV = 85;
	
	private Klemis()
	{
		addStartNpc(KLEMIS);
		addTalkId(KLEMIS);
		addFirstTalkId(KLEMIS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("portInside"))
		{
			if (player.getLevel() >= MIN_LV)
			{
				player.teleToLocation(LOCATION);
			}
			else
			{
				return "32734-01.html";
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@GameScript
	public static void main()
	{
		new Klemis();
	}
}