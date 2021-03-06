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
package org.l2junity.gameserver.model.stats;

import java.util.function.BiPredicate;

import org.l2junity.gameserver.model.actor.Creature;

/**
 * @author UnAfraid
 */
public class StatsHolder
{
	private final DoubleStat _stat;
	private final double _value;
	private final BiPredicate<Creature, StatsHolder> _condition;
	
	public StatsHolder(DoubleStat stat, double value, BiPredicate<Creature, StatsHolder> condition)
	{
		_stat = stat;
		_value = value;
		_condition = condition;
	}
	
	public StatsHolder(DoubleStat stat, double value)
	{
		this(stat, value, null);
	}
	
	public DoubleStat getStat()
	{
		return _stat;
	}
	
	public double getValue()
	{
		return _value;
	}
	
	public boolean verifyCondition(Creature creature)
	{
		return (_condition == null) || _condition.test(creature, this);
	}
}
