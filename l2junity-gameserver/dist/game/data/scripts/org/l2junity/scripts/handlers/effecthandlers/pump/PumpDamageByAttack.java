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
package org.l2junity.scripts.handlers.effecthandlers.pump;

import org.l2junity.gameserver.enums.DamageByAttackType;
import org.l2junity.gameserver.handler.EffectHandler;
import org.l2junity.gameserver.model.StatsSet;
import org.l2junity.gameserver.model.actor.Creature;
import org.l2junity.gameserver.model.effects.AbstractEffect;
import org.l2junity.gameserver.model.skills.Skill;
import org.l2junity.gameserver.model.stats.DoubleStat;
import org.l2junity.gameserver.scripting.annotations.SkillScript;

/**
 * An effect that changes damage taken from an attack. <br>
 * The retail implementation seems to be altering whatever damage is taken after the attack has been done and not when attack is being done. <br>
 * Exceptions for this effect appears to be DOT effects and terrain damage, they are unaffected by this stat.<br>
 * As for example in retail this effect does reduce reflected damage taken (because it is received damage), as well as it does not decrease reflected damage done,<br>
 * because reflected damage is being calculated with the original attack damage and not this altered one.<br>
 * Multiple values of this effect add-up to each other rather than multiplying with each other. Be careful, there were cases in retail where damage is deacreased to 0.
 * @author Nik
 */
public class PumpDamageByAttack extends AbstractEffect
{
	private final double _value;
	private final DamageByAttackType _type;
	
	public PumpDamageByAttack(StatsSet params)
	{
		_value = params.getDouble("amount");
		_type = params.getEnum("type", DamageByAttackType.class, DamageByAttackType.NONE);
	}
	
	@Override
	public void pump(Creature target, Skill skill)
	{
		switch (_type)
		{
			case PK:
			{
				target.getStat().mergeAdd(DoubleStat.PVP_DAMAGE_TAKEN, _value);
				break;
			}
			case ENEMY_ALL:
			{
				target.getStat().mergeAdd(DoubleStat.PVE_DAMAGE_TAKEN, _value);
				break;
			}
		}
	}
	
	@SkillScript
	public static void main()
	{
		EffectHandler.getInstance().registerHandler("p_damage_by_attack", PumpDamageByAttack::new);
	}
}
