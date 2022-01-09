/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.poc.home.service.impl;

import com.liferay.portal.aop.AopService;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.poc.home.model.HomeEntry;
import com.poc.home.service.base.HomeEntryLocalServiceBaseImpl;

import com.poc.home.service.persistence.HomeEntryPersistence;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Date;
import java.util.List;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = "model.class.name=com.poc.home.model.HomeEntry",
	service = AopService.class
)
public class HomeEntryLocalServiceImpl extends HomeEntryLocalServiceBaseImpl {

	@Indexable(type = IndexableType.REINDEX)
	@SystemEvent(type = SystemEventConstants.TYPE_DEFAULT)
	public HomeEntry addHome(String address, int floor,
							 float square, int room,
							 String color, String owner,
							 ServiceContext serviceContext) throws PortalException {
		HomeEntry homeEntry = createHomeEntry(counterLocalService.increment(HomeEntry.class.getName()));

		Date current = new Date();

		updateModifiedAudit(homeEntry, current, serviceContext);

//		Set Audit
		homeEntry.setGroupId(serviceContext.getScopeGroupId());
		homeEntry.setCompanyId(serviceContext.getCompanyId());
		homeEntry.setCreateDate(serviceContext.getCreateDate());
		homeEntry.setModifiedDate(serviceContext.getModifiedDate());

		homeEntry.setAddress(address);
		homeEntry.setFloor(floor);
		homeEntry.setSquare(square);
		homeEntry.setRoom(room);
		homeEntry.setColor(color);
		homeEntry.setOwner(owner);

		return addHomeEntry(homeEntry);
	}

	@Indexable(type = IndexableType.REINDEX)
	@SystemEvent(type = SystemEventConstants.TYPE_DEFAULT)
	public HomeEntry updateHomeEntryByHomeId(long homeId, String address, int floor,
							 float square, int room,
							 String color, String owner,
							 ServiceContext serviceContext) throws PortalException {
		HomeEntry homeEntry = getHomeEntry(homeId);

		Date current = new Date();

		updateModifiedAudit(homeEntry, current, serviceContext);

		homeEntry.setAddress(address);
		homeEntry.setFloor(floor);
		homeEntry.setSquare(square);
		homeEntry.setRoom(room);
		homeEntry.setColor(color);
		homeEntry.setOwner(owner);

		return updateHomeEntry(homeEntry);
	}

	@Indexable(type = IndexableType.DELETE)
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public HomeEntry deleteHomeByHomeId(long homeId, ServiceContext serviceContext) throws PortalException {
		return deleteHomeEntry(homeId);
	}

	@SystemEvent(type = SystemEventConstants.TYPE_DEFAULT)
	public HomeEntry getHomeByHomeId(long homeId) throws PortalException{
		return getHomeEntry(homeId);
	}

	@SystemEvent(type = SystemEventConstants.TYPE_DEFAULT)
	public List<HomeEntry> getHomes() {
		return getHomeEntries(0, getHomeEntriesCount());
	}

	private void updateModifiedAudit(HomeEntry homeEntry, Date current, ServiceContext serviceContext) {
		User user = userLocalService.fetchUser(serviceContext.getUserId());

		if (user != null) {
			homeEntry.setUserName(user.getFullName());
			homeEntry.setUserUuid(user.getUserUuid());
		}

		homeEntry.setModifiedDate(serviceContext.getModifiedDate());
		homeEntry.setUserId(serviceContext.getUserId());
	}
}