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
package org.l2junity.scripts.quests.Q00761_AssistingTheGoldenRamArmy;

import org.l2junity.gameserver.enums.CategoryType;
import org.l2junity.gameserver.enums.QuestSound;
import org.l2junity.gameserver.enums.QuestType;
import org.l2junity.gameserver.enums.Race;
import org.l2junity.gameserver.model.actor.Npc;
import org.l2junity.gameserver.model.actor.instance.PlayerInstance;
import org.l2junity.gameserver.model.quest.Quest;
import org.l2junity.gameserver.model.quest.QuestState;
import org.l2junity.gameserver.model.quest.State;
import org.l2junity.gameserver.network.client.send.ExShowScreenMessage;
import org.l2junity.gameserver.network.client.send.string.NpcStringId;

/**
 * Assisting the Golden Ram Army (761)
 * @author St3eT
 */
public final class Q00761_AssistingTheGoldenRamArmy extends Quest
{
	// NPCs
	private static final int PIERCE = 31553;
	private static final int[] MONSTERS =
	{
		21508, // Splinter Stakato
		21509, // Splinter Stakato Worker
		21510, // Splinter Stakato Soldier
		21511, // Splinter Stakato Drone
		21513, // Needle Stakato
		21514, // Needle Stakato Worker
		21515, // Needle Stakato Soldier
		21516, // Needle Stakato Drone
		21517, // Needle Stakato Drone
		21518, // Frenzied Stakato Soldier
	};
	// Items
	private static final int SHELL = 36668; // Intact Stakato Shell
	private static final int TALON = 36669; // Intact Stakato Talon
	private static final int STEEL_DOOR_BOX = 37391; // Steel Door Guild Reward Box (Low-grade)
	// Rewards
	//@formatter:off
	// Format: min item count, exp reward, sp reward, item count reward
	private static final int[][] REWARD =
	{
		{900, 141_403_500, 33_930, 10},
		{800, 127_263_150, 30_537, 9},
		{700, 113_122_800, 27_144, 8},
		{600, 98_982_450, 23_751, 7},
		{500, 84_842_100, 20_358, 6},
		{400, 70_701_750, 16_965, 5},
		{300, 56_546_400, 13_572, 4},
		{200, 42_421_050, 10_179, 3},
		{100, 28_280_700, 6789, 2},
		{0, 14_140_350, 3393, 1},
	};
	//@formatter:on
	// Misc
	private static final int MIN_LEVEL = 65;
	private static final int MAX_LEVEL = 70;
	
	public Q00761_AssistingTheGoldenRamArmy()
	{
		super(761);
		addStartNpc(PIERCE);
		addTalkId(PIERCE);
		addKillId(MONSTERS);
		registerQuestItems(SHELL, TALON);
		addCondNotRace(Race.ERTHEIA, "31553-10.html");
		addCondLevel(MIN_LEVEL, MAX_LEVEL, "31553-11.htm");
		addCondInCategory(CategoryType.FIGHTER_GROUP, "31553-11.htm");
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		final QuestState st = getQuestState(player, false);
		if (st == null)
		{
			return null;
		}
		
		String htmltext = null;
		switch (event)
		{
			case "31553-02.htm":
			case "31553-03.htm":
			case "31553-07.html":
			case "31553-08.html":
			{
				htmltext = event;
				break;
			}
			case "31553-04.htm":
			{
				st.startQuest();
				htmltext = event;
				break;
			}
			case "31553-09.html":
			{
				if (st.isCond(2))
				{
					final long itemCount = getQuestItemsCount(player, TALON);
					
					for (int[] data : REWARD)
					{
						if (itemCount >= data[0])
						{
							if (player.getLevel() >= MIN_LEVEL)
							{
								addExp(player, data[1]);
								addSp(player, data[2]);
							}
							giveItems(player, STEEL_DOOR_BOX, data[3]);
							st.exitQuest(QuestType.DAILY, true);
							htmltext = event;
							break;
						}
					}
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, PlayerInstance player, boolean isSimulated)
	{
		final QuestState st = getQuestState(player, true);
		String htmltext = null;
		
		switch (st.getState())
		{
			case State.CREATED:
			{
				htmltext = "31553-01.htm";
				break;
			}
			case State.STARTED:
			{
				htmltext = st.isCond(1) ? "31553-05.html" : "31553-06.html";
				break;
			}
			case State.COMPLETED:
			{
				if (!st.isNowAvailable())
				{
					htmltext = getAlreadyCompletedMsg(player, QuestType.DAILY);
				}
				else
				{
					if (!isSimulated)
					{
						st.setState(State.CREATED);
					}
					htmltext = "31553-01.htm";
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(Npc npc, PlayerInstance killer, boolean isSummon)
	{
		final QuestState st = getQuestState(killer, false);
		
		if ((st != null) && (st.isCond(1) || st.isCond(2)) && (getRandom(100) < 15))
		{
			if (getQuestItemsCount(killer, SHELL) < 50)
			{
				giveItems(killer, SHELL, 1);
				playSound(killer, QuestSound.ITEMSOUND_QUEST_ITEMGET);
				
				if (getQuestItemsCount(killer, SHELL) >= 50)
				{
					st.setCond(2, true);
					showOnScreenMsg(killer, NpcStringId.YOU_CAN_GATHER_MORE_INTACT_STAKATO_TALONS, ExShowScreenMessage.TOP_CENTER, 6000);
				}
			}
			else
			{
				giveItems(killer, TALON, 1);
				playSound(killer, QuestSound.ITEMSOUND_QUEST_ITEMGET);
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
}