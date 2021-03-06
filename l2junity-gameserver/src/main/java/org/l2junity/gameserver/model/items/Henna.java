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
package org.l2junity.gameserver.model.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.l2junity.gameserver.model.StatsSet;
import org.l2junity.gameserver.model.base.ClassId;
import org.l2junity.gameserver.model.stats.BaseStats;
import org.l2junity.gameserver.model.stats.DoubleStat;

/**
 * Class for the Henna object.
 * @author Zoey76
 */
public class Henna
{
	private final int _dyeId;
	private final String _dyeName;
	private final int _dyeItemId;
	private final Map<BaseStats, Integer> _baseStats = new HashMap<>();
	private final int _wear_fee;
	private final int _wear_count;
	private final int _cancel_fee;
	private final int _cancel_count;
	private final List<ClassId> _wear_class;
	
	public Henna(StatsSet set)
	{
		_dyeId = set.getInt("dyeId");
		_dyeName = set.getString("dyeName");
		_dyeItemId = set.getInt("dyeItemId");
		_baseStats.put(BaseStats.STR, set.getInt("str", 0));
		_baseStats.put(BaseStats.CON, set.getInt("con", 0));
		_baseStats.put(BaseStats.DEX, set.getInt("dex", 0));
		_baseStats.put(BaseStats.INT, set.getInt("int", 0));
		_baseStats.put(BaseStats.MEN, set.getInt("men", 0));
		_baseStats.put(BaseStats.WIT, set.getInt("wit", 0));
		_baseStats.put(BaseStats.LUC, set.getInt("luc", 0));
		_baseStats.put(BaseStats.CHA, set.getInt("cha", 0));
		_wear_fee = set.getInt("wear_fee");
		_wear_count = set.getInt("wear_count");
		_cancel_fee = set.getInt("cancel_fee");
		_cancel_count = set.getInt("cancel_count");
		_wear_class = new ArrayList<>();
	}
	
	/**
	 * @return the dye Id.
	 */
	public int getDyeId()
	{
		return _dyeId;
	}
	
	/**
	 * @return the dye server-side name.
	 */
	public String getDyeName()
	{
		return _dyeName;
	}
	
	/**
	 * @return the item Id, required for this dye.
	 */
	public int getDyeItemId()
	{
		return _dyeItemId;
	}
	
	public int getBaseStats(DoubleStat stat)
	{
		return _baseStats.getOrDefault(stat, 0);
	}
	
	public Map<BaseStats, Integer> getBaseStats()
	{
		return _baseStats;
	}
	
	/**
	 * @return the wear fee, cost for adding this dye to the player.
	 */
	public int getWearFee()
	{
		return _wear_fee;
	}
	
	/**
	 * @return the wear count, the required count to add this dye to the player.
	 */
	public int getWearCount()
	{
		return _wear_count;
	}
	
	/**
	 * @return the cancel fee, cost for removing this dye from the player.
	 */
	public int getCancelFee()
	{
		return _cancel_fee;
	}
	
	/**
	 * @return the cancel count, the retrieved amount of dye items after removing the dye.
	 */
	public int getCancelCount()
	{
		return _cancel_count;
	}
	
	/**
	 * @return the list with the allowed classes to wear this dye.
	 */
	public List<ClassId> getAllowedWearClass()
	{
		return _wear_class;
	}
	
	/**
	 * @param c the class trying to wear this dye.
	 * @return {@code true} if the player is allowed to wear this dye, {@code false} otherwise.
	 */
	public boolean isAllowedClass(ClassId c)
	{
		return _wear_class.contains(c);
	}
	
	/**
	 * @param wearClassIds the list of classes that can wear this dye.
	 */
	public void setWearClassIds(List<ClassId> wearClassIds)
	{
		_wear_class.addAll(wearClassIds);
	}
}