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
package org.l2junity.gameserver.network.client.recv;

import org.l2junity.gameserver.data.xml.impl.RecipeData;
import org.l2junity.gameserver.enums.PrivateStoreType;
import org.l2junity.gameserver.model.World;
import org.l2junity.gameserver.model.actor.instance.PlayerInstance;
import org.l2junity.gameserver.model.holders.RecipeHolder;
import org.l2junity.gameserver.network.client.L2GameClient;
import org.l2junity.gameserver.network.client.send.RecipeShopItemInfo;
import org.l2junity.gameserver.network.client.send.string.SystemMessageId;
import org.l2junity.network.PacketReader;

/**
 * This class ... cdd
 * @version $Revision: 1.1.2.1.2.2 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestRecipeShopMakeInfo implements IClientIncomingPacket
{
	private int _playerObjectId;
	private int _recipeId;
	
	@Override
	public boolean read(L2GameClient client, PacketReader packet)
	{
		_playerObjectId = packet.readD();
		_recipeId = packet.readD();
		return true;
	}
	
	@Override
	public void run(L2GameClient client)
	{
		final PlayerInstance player = client.getActiveChar();
		if (player == null)
		{
			return;
		}
		
		final PlayerInstance manufacturer = World.getInstance().getPlayer(_playerObjectId);
		if ((manufacturer == null) || (manufacturer.getPrivateStoreType() != PrivateStoreType.MANUFACTURE))
		{
			return;
		}
		
		final RecipeHolder recipe = RecipeData.getInstance().getRecipe(_recipeId);
		if (recipe == null)
		{
			player.sendPacket(SystemMessageId.THE_RECIPE_IS_INCORRECT);
			return;
		}
		
		final Long manufactureRecipeCost = manufacturer.getManufactureItems().get(_recipeId);
		if (manufactureRecipeCost == null)
		{
			return;
		}
		
		client.sendPacket(new RecipeShopItemInfo(manufacturer, _recipeId, manufactureRecipeCost, recipe.getMaxOffering()));
	}
}
