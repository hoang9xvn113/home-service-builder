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

package com.poc.employee.service.impl;

import com.liferay.portal.aop.AopService;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.poc.employee.model.EmployeeEntry;
import com.poc.employee.service.base.EmployeeEntryLocalServiceBaseImpl;

import org.osgi.service.component.annotations.Component;

import java.util.Date;
import java.util.List;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = "model.class.name=com.poc.employee.model.EmployeeEntry",
	service = AopService.class
)
public class EmployeeEntryLocalServiceImpl
	extends EmployeeEntryLocalServiceBaseImpl {

	@Indexable(type = IndexableType.REINDEX)
	@SystemEvent(type = SystemEventConstants.TYPE_DEFAULT)
	public EmployeeEntry addEmployee(String name, Date birthDay, int gender, String address, boolean hasAccount, ServiceContext serviceContext) {
		EmployeeEntry employeeEntry = createEmployeeEntry(counterLocalService.increment(EmployeeEntry.class.getName()));

		Date current = new Date();

		updateModifiedAudit(employeeEntry, current, serviceContext);

		employeeEntry.setGroupId(serviceContext.getScopeGroupId());
		employeeEntry.setCompanyId(serviceContext.getCompanyId());
		employeeEntry.setCreateDate(serviceContext.getCreateDate());
		employeeEntry.setModifiedDate(serviceContext.getModifiedDate());

		employeeEntry.setName(name);
		employeeEntry.setBirthDay(birthDay);
		employeeEntry.setGender(gender);
		employeeEntry.setAddress(address);
		employeeEntry.setHasAccount(hasAccount);

		return addEmployeeEntry(employeeEntry);
	}

	@Indexable(type = IndexableType.REINDEX)
	@SystemEvent(type = SystemEventConstants.TYPE_DEFAULT)
	public EmployeeEntry updateEmployee(long employeeId, String name, Date birthDay, int gender, String address, boolean hasAccount, ServiceContext serviceContext) throws PortalException {

		EmployeeEntry employeeEntry = getEmployeeByEmployeeId(employeeId, serviceContext);

		employeeEntry.setModifiedDate(serviceContext.getModifiedDate());

		employeeEntry.setName(name);
		employeeEntry.setBirthDay(birthDay);
		employeeEntry.setGender(gender);
		employeeEntry.setAddress(address);
		employeeEntry.setHasAccount(hasAccount);

		return updateEmployeeEntry(employeeEntry);
	}

	@Indexable(type = IndexableType.DELETE)
	@SystemEvent(type = SystemEventConstants.TYPE_DEFAULT)
	public EmployeeEntry deleteEmployee(long employeeId) throws PortalException{

		return deleteEmployeeEntry(employeeId);
	}

	@SystemEvent(type = SystemEventConstants.TYPE_DEFAULT)
	public List<EmployeeEntry> getEmployees(ServiceContext serviceContext) {
		return getEmployeeEntriesByUuidAndCompanyId(serviceContext.getUuid(), serviceContext.getCompanyId());
	}

	@SystemEvent(type = SystemEventConstants.TYPE_DEFAULT)
	public EmployeeEntry getEmployeeByEmployeeId(long employeeId, ServiceContext serviceContext) throws PortalException{
		EmployeeEntry employeeEntry = getEmployeeEntry(employeeId);

		return employeeEntry;
	}

	private void updateModifiedAudit(EmployeeEntry employeeEntry, Date current, ServiceContext serviceContext) {
		User user = userLocalService.fetchUser(serviceContext.getUserId());

		if (user != null) {
			employeeEntry.setUserUuid(user.getUserUuid());
			employeeEntry.setUserName(user.getFullName());
		}

		employeeEntry.setModifiedDate(current);
		employeeEntry.setUserId(serviceContext.getUserId());
	}
}