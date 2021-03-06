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
package org.l2junity.gameserver.model.holders;

import java.util.List;

import org.l2junity.commons.util.Rnd;
import org.l2junity.gameserver.model.StatsSet;

/**
 * A DTO for items; contains item ID, count and chance.<br>
 * Complemented by {@link QuestItemHolder}.
 * @author xban1x
 */
public class ItemChanceHolder extends ItemHolder
{
	private static final long serialVersionUID = -3373566282523166353L;
	private final double _chance;
	
	public ItemChanceHolder(StatsSet params)
	{
		this(params.getInt("id"), params.getDouble("chance"), params.getLong("count"));
	}
	
	public ItemChanceHolder(int id, double chance)
	{
		this(id, chance, 1);
	}
	
	public ItemChanceHolder(int id, double chance, long count)
	{
		super(id, count);
		_chance = chance;
	}
	
	/**
	 * Gets the chance.
	 * @return the drop chance of the item contained in this object
	 */
	public double getChance()
	{
		return _chance;
	}
	
	/**
	 * Calculates a cumulative chance of all given holders. If all holders' chance sum up to 100% or above, there is 100% guarantee a holder will be selected.
	 * @param holders list of holders to calculate chance from.
	 * @return {@code ItemChanceHolder} of the successful random roll or {@code null} if there was no lucky holder selected.
	 */
	public static ItemChanceHolder getRandomHolder(List<ItemChanceHolder> holders)
	{
		double itemRandom = 100 * Rnd.nextDouble();
		
		for (ItemChanceHolder holder : holders)
		{
			// Any mathmatical expression including NaN will result in either NaN or 0 of converted to something other than double.
			// We would usually want to skip calculating any holders that include NaN as a chance, because that ruins the overall process.
			if (!Double.isNaN(holder.getChance()))
			{
				// Calculate chance
				if (holder.getChance() > itemRandom)
				{
					return holder;
				}
				
				itemRandom -= holder.getChance();
			}
		}
		
		return null;
	}
	
	@Override
	public String toString()
	{
		return "[" + getClass().getSimpleName() + "] ID: " + getId() + ", count: " + getCount() + ", chance: " + _chance;
	}
}
