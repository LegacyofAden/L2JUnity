/*
 * Copyright (C) 2004-2015 L2J Unity
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
package org.l2junity.gameserver.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.l2junity.gameserver.model.actor.instance.PlayerInstance;
import org.l2junity.gameserver.model.holders.ArmorsetSkillHolder;
import org.l2junity.gameserver.model.itemcontainer.Inventory;
import org.l2junity.gameserver.model.itemcontainer.PcInventory;
import org.l2junity.gameserver.model.items.instance.ItemInstance;
import org.l2junity.gameserver.model.stats.BaseStats;

/**
 * @author UnAfraid
 */
public final class ArmorSet
{
	private final int _id;
	private final int _minimumPieces;
	private final boolean _isVisual;
	
	private final Set<Integer> _requiredItems = new LinkedHashSet<>();
	private final Set<Integer> _optionalItems = new LinkedHashSet<>();
	
	private final List<ArmorsetSkillHolder> _skills = new ArrayList<>();
	private final Map<BaseStats, Double> _stats = new LinkedHashMap<>();
	
	private static final int[] ARMORSET_SLOTS = new int[]
	{
		Inventory.PAPERDOLL_CHEST,
		Inventory.PAPERDOLL_LEGS,
		Inventory.PAPERDOLL_HEAD,
		Inventory.PAPERDOLL_GLOVES,
		Inventory.PAPERDOLL_FEET
	};
	
	/**
	 * @param id
	 * @param minimumPieces
	 * @param isVisual
	 */
	public ArmorSet(int id, int minimumPieces, boolean isVisual)
	{
		_id = id;
		_minimumPieces = minimumPieces;
		_isVisual = isVisual;
	}
	
	public int getId()
	{
		return _id;
	}
	
	/**
	 * @return the minimum amount of pieces equipped to form a set
	 */
	public int getMinimumPieces()
	{
		return _minimumPieces;
	}
	
	/**
	 * @return {@code true} if the set is visual only, {@code} otherwise
	 */
	public boolean isVisual()
	{
		return _isVisual;
	}
	
	/**
	 * Adds an item to the set
	 * @param item
	 * @return {@code true} if item was successfully added, {@code false} in case it already exists
	 */
	public boolean addRequiredItem(Integer item)
	{
		return _requiredItems.add(item);
	}
	
	/**
	 * @return the set of items that can form a set
	 */
	public Set<Integer> getRequiredItems()
	{
		return _requiredItems;
	}
	
	/**
	 * Adds an shield to the set
	 * @param item
	 * @return {@code true} if shield was successfully added, {@code false} in case it already exists
	 */
	public boolean addOptionalItem(Integer item)
	{
		return _optionalItems.add(item);
	}
	
	/**
	 * @return the set of shields
	 */
	public Set<Integer> getOptionalItems()
	{
		return _optionalItems;
	}
	
	/**
	 * Adds an skill to the set
	 * @param holder
	 */
	public void addSkill(ArmorsetSkillHolder holder)
	{
		_skills.add(holder);
	}
	
	/**
	 * The list of skills that are activated when set reaches it's minimal equipped items condition
	 * @return
	 */
	public List<ArmorsetSkillHolder> getSkills()
	{
		return _skills;
	}
	
	/**
	 * Adds stats bonus to the set activated when set reaches it's minimal equipped items condition
	 * @param stat
	 * @param value
	 */
	public void addStatsBonus(BaseStats stat, double value)
	{
		_stats.putIfAbsent(stat, value);
	}
	
	/**
	 * @param stat
	 * @return the stats bonus value or 0 if doesn't exists
	 */
	public double getStatsBonus(BaseStats stat)
	{
		return _stats.getOrDefault(stat, 0d);
	}
	
	/**
	 * @param shield_id
	 * @return {@code true} if player has the shield of this set equipped, {@code false} in case set doesn't have a shield or player doesn't
	 */
	public boolean containOptionalItem(int shield_id)
	{
		return _optionalItems.contains(shield_id);
	}
	
	/**
	 * @param player
	 * @return true if all parts of set are enchanted to +6 or more
	 */
	public int getLowestSetEnchant(PlayerInstance player)
	{
		// Player don't have full set
		
		if (getPiecesCount(player, ItemInstance::getId) < getMinimumPieces())
		{
			return 0;
		}
		
		final PcInventory inv = player.getInventory();
		int enchantLevel = Byte.MAX_VALUE;
		int slotsProcessed = 0;
		for (int armorSlot : ARMORSET_SLOTS)
		{
			final ItemInstance itemPart = inv.getPaperdollItem(armorSlot);
			if ((itemPart != null) && _requiredItems.contains(itemPart.getId()))
			{
				if (enchantLevel > itemPart.getEnchantLevel())
				{
					enchantLevel = itemPart.getEnchantLevel();
				}
				slotsProcessed++;
			}
		}
		
		for (ArmorsetSkillHolder holder : _skills)
		{
			if ((holder.getMinEnchant() > 0) && (holder.getMinimumPieces() > slotsProcessed))
			{
				enchantLevel = 0;
				break;
			}
		}
		
		if (enchantLevel == Byte.MAX_VALUE)
		{
			enchantLevel = 0;
		}
		return enchantLevel;
	}
	
	public boolean hasOptionalEquipped(PlayerInstance player, Function<ItemInstance, Integer> idProvider)
	{
		return player.getInventory().getPaperdollItems().stream().anyMatch(item -> _optionalItems.contains(idProvider.apply(item)));
	}
	
	/**
	 * @param player
	 * @param idProvider
	 * @return the amount of set visual items that player has equipped
	 */
	public long getPiecesCount(PlayerInstance player, Function<ItemInstance, Integer> idProvider)
	{
		return player.getInventory().getPaperdollItems(item -> _requiredItems.contains(idProvider.apply(item))).size();
	}
}
