/*
 * Copyright (C) 2004-2016 L2J Unity
 * 
 * This file is part of L2J Unity.
 * 
 * L2J Unity is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Unity is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2junity.scripts.quests.Q00137_TempleChampionPart1;

import org.l2junity.gameserver.enums.QuestSound;
import org.l2junity.gameserver.model.actor.Npc;
import org.l2junity.gameserver.model.actor.instance.PlayerInstance;
import org.l2junity.gameserver.model.quest.Quest;
import org.l2junity.gameserver.model.quest.QuestState;
import org.l2junity.gameserver.model.quest.State;

import org.l2junity.scripts.quests.Q00134_TempleMissionary.Q00134_TempleMissionary;
import org.l2junity.scripts.quests.Q00135_TempleExecutor.Q00135_TempleExecutor;

/**
 * Temple Champion - 1 (137)
 * @author nonom, Gladicek
 */
public class Q00137_TempleChampionPart1 extends Quest
{
	// NPCs
	private static final int SYLVAIN = 30070;
	private static final int MOBS[] =
	{
		20083, // Granite Golem
		20144, // Hangman Tree
		20199, // Amber Basilisk
		20200, // Strain
		20201, // Ghoul
		20202, // Dead Seeker
	};
	// Items
	private static final int FRAGMENT = 10340;
	private static final int EXECUTOR = 10334;
	private static final int MISSIONARY = 10339;
	// Misc
	private static final int MIN_LEVEL = 35;
	private static final int MAX_LEVEL = 41;
	
	public Q00137_TempleChampionPart1()
	{
		super(137);
		addStartNpc(SYLVAIN);
		addTalkId(SYLVAIN);
		addKillId(MOBS);
		addCondMinLevel(MIN_LEVEL, "30070-17.html");
		addCondCompletedQuest(Q00134_TempleMissionary.class.getSimpleName(), "30070-18.html");
		addCondCompletedQuest(Q00135_TempleExecutor.class.getSimpleName(), "30070-18.html");
		registerQuestItems(FRAGMENT);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		final QuestState st = getQuestState(player, false);
		if (st == null)
		{
			return getNoQuestMsg(player);
		}
		
		String htmltext = event;
		switch (event)
		{
			case "30070-02.htm":
			{
				st.startQuest();
				break;
			}
			case "30070-05.html":
			{
				st.set("talk", "1");
				break;
			}
			case "30070-06.html":
			{
				st.set("talk", "2");
				break;
			}
			case "30070-08.html":
			{
				if (st.isCond(1))
				{
					st.unset("talk");
					st.setCond(2, true);
				}
				break;
			}
			case "30070-16.html":
			{
				if (st.isCond(3) && (hasQuestItems(player, EXECUTOR) && hasQuestItems(player, MISSIONARY)))
				{
					if (player.getLevel() >= MIN_LEVEL)
					{
						takeItems(player, EXECUTOR, -1);
						takeItems(player, MISSIONARY, -1);
						giveAdena(player, 69146, true);
						if (player.getLevel() < MAX_LEVEL)
						{
							addExp(player, 219_975);
							addSp(player, 20); // TODO: Retail value
						}
						st.exitQuest(false, true);
					}
					else
					{
						htmltext = getNoQuestLevelRewardMsg(player);
					}
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(Npc npc, PlayerInstance player, boolean isSummon)
	{
		final QuestState st = getQuestState(player, false);
		if ((st != null) && st.isCond(2) && (getQuestItemsCount(player, FRAGMENT) < 30))
		{
			giveItems(player, FRAGMENT, 1);
			if (getQuestItemsCount(player, FRAGMENT) >= 30)
			{
				st.setCond(3, true);
			}
			else
			{
				playSound(player, QuestSound.ITEMSOUND_QUEST_ITEMGET);
			}
		}
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public String onTalk(Npc npc, PlayerInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		if (st == null)
		{
			return htmltext;
		}
		
		switch (st.getState())
		{
			case State.CREATED:
			{
				htmltext = "30070-01.htm";
				break;
			}
			case State.STARTED:
			{
				switch (st.getCond())
				{
					case 1:
					{
						switch (st.getInt("talk"))
						{
							case 1:
								htmltext = "30070-05.html";
								break;
							case 2:
								htmltext = "30070-06.html";
								break;
							default:
								htmltext = "30070-03.html";
								break;
						}
						break;
					}
					case 2:
					{
						htmltext = "30070-08.html";
						break;
					}
					case 3:
					{
						if (st.getInt("talk") == 1)
						{
							htmltext = "30070-10.html";
						}
						else if (getQuestItemsCount(player, FRAGMENT) >= 30)
						{
							st.set("talk", "1");
							htmltext = "30070-09.html";
							takeItems(player, FRAGMENT, -1);
						}
						break;
					}
				}
				break;
			}
			case State.COMPLETED:
			{
				htmltext = getAlreadyCompletedMsg(player);
				break;
			}
		}
		return htmltext;
	}
}
