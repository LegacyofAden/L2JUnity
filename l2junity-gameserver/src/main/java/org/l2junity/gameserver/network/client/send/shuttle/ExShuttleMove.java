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
package org.l2junity.gameserver.network.client.send.shuttle;

import org.l2junity.gameserver.model.actor.instance.L2ShuttleInstance;
import org.l2junity.gameserver.network.client.OutgoingPackets;
import org.l2junity.gameserver.network.client.send.IClientOutgoingPacket;
import org.l2junity.network.PacketWriter;

/**
 * @author UnAfraid
 */
public class ExShuttleMove implements IClientOutgoingPacket
{
	private final L2ShuttleInstance _shuttle;
	private final int _x, _y, _z;
	
	public ExShuttleMove(L2ShuttleInstance shuttle, double x, double y, double z)
	{
		_shuttle = shuttle;
		_x = (int) x;
		_y = (int) y;
		_z = (int) z;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_SUTTLE_MOVE.writeId(packet);
		
		packet.writeD(_shuttle.getObjectId());
		packet.writeD((int) _shuttle.getStat().getMoveSpeed());
		packet.writeD((int) _shuttle.getStat().getRotationSpeed());
		packet.writeD(_x);
		packet.writeD(_y);
		packet.writeD(_z);
		return true;
	}
}
