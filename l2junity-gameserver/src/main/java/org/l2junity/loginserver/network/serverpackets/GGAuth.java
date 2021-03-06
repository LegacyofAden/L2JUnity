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
package org.l2junity.loginserver.network.serverpackets;

import org.l2junity.gameserver.config.GeneralConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fromat: d d: response
 */
public final class GGAuth extends L2LoginServerPacket
{
	static final Logger _log = LoggerFactory.getLogger(GGAuth.class);
	public static final int SKIP_GG_AUTH_REQUEST = 0x0b;
	
	private final int _response;
	
	public GGAuth(int response)
	{
		_response = response;
		if (GeneralConfig.DEBUG)
		{
			_log.warn("Reason Hex: " + (Integer.toHexString(response)));
		}
	}
	
	@Override
	protected void write()
	{
		writeC(0x0b);
		writeD(_response);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
	}
}
