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
package org.l2junity.commons.ipsuppliers.impl;

import java.io.IOException;

import org.l2junity.commons.ipsuppliers.IRealIPSupplier;

/**
 * @author UnAfraid
 */
public class IFConfigIPSupplier implements IRealIPSupplier
{
	@Override
	public String getIP() throws IOException
	{
		return defaultIPObtainMethod("https://v4.ifconfig.co/ip");
	}
	
	@Override
	public int getPriority()
	{
		return 1;
	}
}
