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

package com.poc.home.service.persistence.impl;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

import com.poc.home.exception.NoSuchHomeEntryException;
import com.poc.home.model.HomeEntry;
import com.poc.home.model.HomeEntryTable;
import com.poc.home.model.impl.HomeEntryImpl;
import com.poc.home.model.impl.HomeEntryModelImpl;
import com.poc.home.service.persistence.HomeEntryPersistence;
import com.poc.home.service.persistence.HomeEntryUtil;
import com.poc.home.service.persistence.impl.constants.DGPersistenceConstants;

import java.io.Serializable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the home entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = {HomeEntryPersistence.class, BasePersistence.class})
public class HomeEntryPersistenceImpl
	extends BasePersistenceImpl<HomeEntry> implements HomeEntryPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>HomeEntryUtil</code> to access the home entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		HomeEntryImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByUuid;
	private FinderPath _finderPathWithoutPaginationFindByUuid;
	private FinderPath _finderPathCountByUuid;

	/**
	 * Returns all the home entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching home entries
	 */
	@Override
	public List<HomeEntry> findByUuid(String uuid) {
		return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the home entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>HomeEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of home entries
	 * @param end the upper bound of the range of home entries (not inclusive)
	 * @return the range of matching home entries
	 */
	@Override
	public List<HomeEntry> findByUuid(String uuid, int start, int end) {
		return findByUuid(uuid, start, end, null);
	}

	/**
	 * Returns an ordered range of all the home entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>HomeEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of home entries
	 * @param end the upper bound of the range of home entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching home entries
	 */
	@Override
	public List<HomeEntry> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<HomeEntry> orderByComparator) {

		return findByUuid(uuid, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the home entries where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>HomeEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of home entries
	 * @param end the upper bound of the range of home entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching home entries
	 */
	@Override
	public List<HomeEntry> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<HomeEntry> orderByComparator,
		boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByUuid;
				finderArgs = new Object[] {uuid};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByUuid;
			finderArgs = new Object[] {uuid, start, end, orderByComparator};
		}

		List<HomeEntry> list = null;

		if (useFinderCache) {
			list = (List<HomeEntry>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (HomeEntry homeEntry : list) {
					if (!uuid.equals(homeEntry.getUuid())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_HOMEENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(HomeEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				list = (List<HomeEntry>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first home entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching home entry
	 * @throws NoSuchHomeEntryException if a matching home entry could not be found
	 */
	@Override
	public HomeEntry findByUuid_First(
			String uuid, OrderByComparator<HomeEntry> orderByComparator)
		throws NoSuchHomeEntryException {

		HomeEntry homeEntry = fetchByUuid_First(uuid, orderByComparator);

		if (homeEntry != null) {
			return homeEntry;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchHomeEntryException(sb.toString());
	}

	/**
	 * Returns the first home entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching home entry, or <code>null</code> if a matching home entry could not be found
	 */
	@Override
	public HomeEntry fetchByUuid_First(
		String uuid, OrderByComparator<HomeEntry> orderByComparator) {

		List<HomeEntry> list = findByUuid(uuid, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last home entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching home entry
	 * @throws NoSuchHomeEntryException if a matching home entry could not be found
	 */
	@Override
	public HomeEntry findByUuid_Last(
			String uuid, OrderByComparator<HomeEntry> orderByComparator)
		throws NoSuchHomeEntryException {

		HomeEntry homeEntry = fetchByUuid_Last(uuid, orderByComparator);

		if (homeEntry != null) {
			return homeEntry;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchHomeEntryException(sb.toString());
	}

	/**
	 * Returns the last home entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching home entry, or <code>null</code> if a matching home entry could not be found
	 */
	@Override
	public HomeEntry fetchByUuid_Last(
		String uuid, OrderByComparator<HomeEntry> orderByComparator) {

		int count = countByUuid(uuid);

		if (count == 0) {
			return null;
		}

		List<HomeEntry> list = findByUuid(
			uuid, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the home entries before and after the current home entry in the ordered set where uuid = &#63;.
	 *
	 * @param homeId the primary key of the current home entry
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next home entry
	 * @throws NoSuchHomeEntryException if a home entry with the primary key could not be found
	 */
	@Override
	public HomeEntry[] findByUuid_PrevAndNext(
			long homeId, String uuid,
			OrderByComparator<HomeEntry> orderByComparator)
		throws NoSuchHomeEntryException {

		uuid = Objects.toString(uuid, "");

		HomeEntry homeEntry = findByPrimaryKey(homeId);

		Session session = null;

		try {
			session = openSession();

			HomeEntry[] array = new HomeEntryImpl[3];

			array[0] = getByUuid_PrevAndNext(
				session, homeEntry, uuid, orderByComparator, true);

			array[1] = homeEntry;

			array[2] = getByUuid_PrevAndNext(
				session, homeEntry, uuid, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected HomeEntry getByUuid_PrevAndNext(
		Session session, HomeEntry homeEntry, String uuid,
		OrderByComparator<HomeEntry> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_HOMEENTRY_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_UUID_3);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_UUID_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(HomeEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(homeEntry)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<HomeEntry> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the home entries where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	@Override
	public void removeByUuid(String uuid) {
		for (HomeEntry homeEntry :
				findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(homeEntry);
		}
	}

	/**
	 * Returns the number of home entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching home entries
	 */
	@Override
	public int countByUuid(String uuid) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid;

		Object[] finderArgs = new Object[] {uuid};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_HOMEENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_UUID_2 =
		"homeEntry.uuid = ?";

	private static final String _FINDER_COLUMN_UUID_UUID_3 =
		"(homeEntry.uuid IS NULL OR homeEntry.uuid = '')";

	private FinderPath _finderPathFetchByUUID_G;
	private FinderPath _finderPathCountByUUID_G;

	/**
	 * Returns the home entry where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchHomeEntryException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching home entry
	 * @throws NoSuchHomeEntryException if a matching home entry could not be found
	 */
	@Override
	public HomeEntry findByUUID_G(String uuid, long groupId)
		throws NoSuchHomeEntryException {

		HomeEntry homeEntry = fetchByUUID_G(uuid, groupId);

		if (homeEntry == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("uuid=");
			sb.append(uuid);

			sb.append(", groupId=");
			sb.append(groupId);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchHomeEntryException(sb.toString());
		}

		return homeEntry;
	}

	/**
	 * Returns the home entry where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching home entry, or <code>null</code> if a matching home entry could not be found
	 */
	@Override
	public HomeEntry fetchByUUID_G(String uuid, long groupId) {
		return fetchByUUID_G(uuid, groupId, true);
	}

	/**
	 * Returns the home entry where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching home entry, or <code>null</code> if a matching home entry could not be found
	 */
	@Override
	public HomeEntry fetchByUUID_G(
		String uuid, long groupId, boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {uuid, groupId};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByUUID_G, finderArgs);
		}

		if (result instanceof HomeEntry) {
			HomeEntry homeEntry = (HomeEntry)result;

			if (!Objects.equals(uuid, homeEntry.getUuid()) ||
				(groupId != homeEntry.getGroupId())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_HOMEENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_G_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_G_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_G_GROUPID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(groupId);

				List<HomeEntry> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByUUID_G, finderArgs, list);
					}
				}
				else {
					HomeEntry homeEntry = list.get(0);

					result = homeEntry;

					cacheResult(homeEntry);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (HomeEntry)result;
		}
	}

	/**
	 * Removes the home entry where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the home entry that was removed
	 */
	@Override
	public HomeEntry removeByUUID_G(String uuid, long groupId)
		throws NoSuchHomeEntryException {

		HomeEntry homeEntry = findByUUID_G(uuid, groupId);

		return remove(homeEntry);
	}

	/**
	 * Returns the number of home entries where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching home entries
	 */
	@Override
	public int countByUUID_G(String uuid, long groupId) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUUID_G;

		Object[] finderArgs = new Object[] {uuid, groupId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_HOMEENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_G_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_G_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_G_GROUPID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(groupId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_G_UUID_2 =
		"homeEntry.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_G_UUID_3 =
		"(homeEntry.uuid IS NULL OR homeEntry.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_G_GROUPID_2 =
		"homeEntry.groupId = ?";

	private FinderPath _finderPathWithPaginationFindByUuid_C;
	private FinderPath _finderPathWithoutPaginationFindByUuid_C;
	private FinderPath _finderPathCountByUuid_C;

	/**
	 * Returns all the home entries where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching home entries
	 */
	@Override
	public List<HomeEntry> findByUuid_C(String uuid, long companyId) {
		return findByUuid_C(
			uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the home entries where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>HomeEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of home entries
	 * @param end the upper bound of the range of home entries (not inclusive)
	 * @return the range of matching home entries
	 */
	@Override
	public List<HomeEntry> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return findByUuid_C(uuid, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the home entries where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>HomeEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of home entries
	 * @param end the upper bound of the range of home entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching home entries
	 */
	@Override
	public List<HomeEntry> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<HomeEntry> orderByComparator) {

		return findByUuid_C(
			uuid, companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the home entries where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>HomeEntryModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of home entries
	 * @param end the upper bound of the range of home entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching home entries
	 */
	@Override
	public List<HomeEntry> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<HomeEntry> orderByComparator,
		boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByUuid_C;
				finderArgs = new Object[] {uuid, companyId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByUuid_C;
			finderArgs = new Object[] {
				uuid, companyId, start, end, orderByComparator
			};
		}

		List<HomeEntry> list = null;

		if (useFinderCache) {
			list = (List<HomeEntry>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (HomeEntry homeEntry : list) {
					if (!uuid.equals(homeEntry.getUuid()) ||
						(companyId != homeEntry.getCompanyId())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(4);
			}

			sb.append(_SQL_SELECT_HOMEENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(HomeEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(companyId);

				list = (List<HomeEntry>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first home entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching home entry
	 * @throws NoSuchHomeEntryException if a matching home entry could not be found
	 */
	@Override
	public HomeEntry findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<HomeEntry> orderByComparator)
		throws NoSuchHomeEntryException {

		HomeEntry homeEntry = fetchByUuid_C_First(
			uuid, companyId, orderByComparator);

		if (homeEntry != null) {
			return homeEntry;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchHomeEntryException(sb.toString());
	}

	/**
	 * Returns the first home entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching home entry, or <code>null</code> if a matching home entry could not be found
	 */
	@Override
	public HomeEntry fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<HomeEntry> orderByComparator) {

		List<HomeEntry> list = findByUuid_C(
			uuid, companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last home entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching home entry
	 * @throws NoSuchHomeEntryException if a matching home entry could not be found
	 */
	@Override
	public HomeEntry findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<HomeEntry> orderByComparator)
		throws NoSuchHomeEntryException {

		HomeEntry homeEntry = fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);

		if (homeEntry != null) {
			return homeEntry;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchHomeEntryException(sb.toString());
	}

	/**
	 * Returns the last home entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching home entry, or <code>null</code> if a matching home entry could not be found
	 */
	@Override
	public HomeEntry fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<HomeEntry> orderByComparator) {

		int count = countByUuid_C(uuid, companyId);

		if (count == 0) {
			return null;
		}

		List<HomeEntry> list = findByUuid_C(
			uuid, companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the home entries before and after the current home entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param homeId the primary key of the current home entry
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next home entry
	 * @throws NoSuchHomeEntryException if a home entry with the primary key could not be found
	 */
	@Override
	public HomeEntry[] findByUuid_C_PrevAndNext(
			long homeId, String uuid, long companyId,
			OrderByComparator<HomeEntry> orderByComparator)
		throws NoSuchHomeEntryException {

		uuid = Objects.toString(uuid, "");

		HomeEntry homeEntry = findByPrimaryKey(homeId);

		Session session = null;

		try {
			session = openSession();

			HomeEntry[] array = new HomeEntryImpl[3];

			array[0] = getByUuid_C_PrevAndNext(
				session, homeEntry, uuid, companyId, orderByComparator, true);

			array[1] = homeEntry;

			array[2] = getByUuid_C_PrevAndNext(
				session, homeEntry, uuid, companyId, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected HomeEntry getByUuid_C_PrevAndNext(
		Session session, HomeEntry homeEntry, String uuid, long companyId,
		OrderByComparator<HomeEntry> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_HOMEENTRY_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
		}

		sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(HomeEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(homeEntry)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<HomeEntry> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the home entries where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	@Override
	public void removeByUuid_C(String uuid, long companyId) {
		for (HomeEntry homeEntry :
				findByUuid_C(
					uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(homeEntry);
		}
	}

	/**
	 * Returns the number of home entries where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching home entries
	 */
	@Override
	public int countByUuid_C(String uuid, long companyId) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid_C;

		Object[] finderArgs = new Object[] {uuid, companyId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_HOMEENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(companyId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_C_UUID_2 =
		"homeEntry.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_C_UUID_3 =
		"(homeEntry.uuid IS NULL OR homeEntry.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_C_COMPANYID_2 =
		"homeEntry.companyId = ?";

	private FinderPath _finderPathWithPaginationFindByFieldFinder;
	private FinderPath _finderPathWithoutPaginationFindByFieldFinder;
	private FinderPath _finderPathCountByFieldFinder;

	/**
	 * Returns all the home entries where floor = &#63; and square = &#63; and room = &#63;.
	 *
	 * @param floor the floor
	 * @param square the square
	 * @param room the room
	 * @return the matching home entries
	 */
	@Override
	public List<HomeEntry> findByFieldFinder(
		int floor, float square, int room) {

		return findByFieldFinder(
			floor, square, room, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the home entries where floor = &#63; and square = &#63; and room = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>HomeEntryModelImpl</code>.
	 * </p>
	 *
	 * @param floor the floor
	 * @param square the square
	 * @param room the room
	 * @param start the lower bound of the range of home entries
	 * @param end the upper bound of the range of home entries (not inclusive)
	 * @return the range of matching home entries
	 */
	@Override
	public List<HomeEntry> findByFieldFinder(
		int floor, float square, int room, int start, int end) {

		return findByFieldFinder(floor, square, room, start, end, null);
	}

	/**
	 * Returns an ordered range of all the home entries where floor = &#63; and square = &#63; and room = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>HomeEntryModelImpl</code>.
	 * </p>
	 *
	 * @param floor the floor
	 * @param square the square
	 * @param room the room
	 * @param start the lower bound of the range of home entries
	 * @param end the upper bound of the range of home entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching home entries
	 */
	@Override
	public List<HomeEntry> findByFieldFinder(
		int floor, float square, int room, int start, int end,
		OrderByComparator<HomeEntry> orderByComparator) {

		return findByFieldFinder(
			floor, square, room, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the home entries where floor = &#63; and square = &#63; and room = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>HomeEntryModelImpl</code>.
	 * </p>
	 *
	 * @param floor the floor
	 * @param square the square
	 * @param room the room
	 * @param start the lower bound of the range of home entries
	 * @param end the upper bound of the range of home entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching home entries
	 */
	@Override
	public List<HomeEntry> findByFieldFinder(
		int floor, float square, int room, int start, int end,
		OrderByComparator<HomeEntry> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByFieldFinder;
				finderArgs = new Object[] {floor, square, room};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByFieldFinder;
			finderArgs = new Object[] {
				floor, square, room, start, end, orderByComparator
			};
		}

		List<HomeEntry> list = null;

		if (useFinderCache) {
			list = (List<HomeEntry>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (HomeEntry homeEntry : list) {
					if ((floor != homeEntry.getFloor()) ||
						(square != homeEntry.getSquare()) ||
						(room != homeEntry.getRoom())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					5 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(5);
			}

			sb.append(_SQL_SELECT_HOMEENTRY_WHERE);

			sb.append(_FINDER_COLUMN_FIELDFINDER_FLOOR_2);

			sb.append(_FINDER_COLUMN_FIELDFINDER_SQUARE_2);

			sb.append(_FINDER_COLUMN_FIELDFINDER_ROOM_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(HomeEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(floor);

				queryPos.add(square);

				queryPos.add(room);

				list = (List<HomeEntry>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first home entry in the ordered set where floor = &#63; and square = &#63; and room = &#63;.
	 *
	 * @param floor the floor
	 * @param square the square
	 * @param room the room
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching home entry
	 * @throws NoSuchHomeEntryException if a matching home entry could not be found
	 */
	@Override
	public HomeEntry findByFieldFinder_First(
			int floor, float square, int room,
			OrderByComparator<HomeEntry> orderByComparator)
		throws NoSuchHomeEntryException {

		HomeEntry homeEntry = fetchByFieldFinder_First(
			floor, square, room, orderByComparator);

		if (homeEntry != null) {
			return homeEntry;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("floor=");
		sb.append(floor);

		sb.append(", square=");
		sb.append(square);

		sb.append(", room=");
		sb.append(room);

		sb.append("}");

		throw new NoSuchHomeEntryException(sb.toString());
	}

	/**
	 * Returns the first home entry in the ordered set where floor = &#63; and square = &#63; and room = &#63;.
	 *
	 * @param floor the floor
	 * @param square the square
	 * @param room the room
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching home entry, or <code>null</code> if a matching home entry could not be found
	 */
	@Override
	public HomeEntry fetchByFieldFinder_First(
		int floor, float square, int room,
		OrderByComparator<HomeEntry> orderByComparator) {

		List<HomeEntry> list = findByFieldFinder(
			floor, square, room, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last home entry in the ordered set where floor = &#63; and square = &#63; and room = &#63;.
	 *
	 * @param floor the floor
	 * @param square the square
	 * @param room the room
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching home entry
	 * @throws NoSuchHomeEntryException if a matching home entry could not be found
	 */
	@Override
	public HomeEntry findByFieldFinder_Last(
			int floor, float square, int room,
			OrderByComparator<HomeEntry> orderByComparator)
		throws NoSuchHomeEntryException {

		HomeEntry homeEntry = fetchByFieldFinder_Last(
			floor, square, room, orderByComparator);

		if (homeEntry != null) {
			return homeEntry;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("floor=");
		sb.append(floor);

		sb.append(", square=");
		sb.append(square);

		sb.append(", room=");
		sb.append(room);

		sb.append("}");

		throw new NoSuchHomeEntryException(sb.toString());
	}

	/**
	 * Returns the last home entry in the ordered set where floor = &#63; and square = &#63; and room = &#63;.
	 *
	 * @param floor the floor
	 * @param square the square
	 * @param room the room
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching home entry, or <code>null</code> if a matching home entry could not be found
	 */
	@Override
	public HomeEntry fetchByFieldFinder_Last(
		int floor, float square, int room,
		OrderByComparator<HomeEntry> orderByComparator) {

		int count = countByFieldFinder(floor, square, room);

		if (count == 0) {
			return null;
		}

		List<HomeEntry> list = findByFieldFinder(
			floor, square, room, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the home entries before and after the current home entry in the ordered set where floor = &#63; and square = &#63; and room = &#63;.
	 *
	 * @param homeId the primary key of the current home entry
	 * @param floor the floor
	 * @param square the square
	 * @param room the room
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next home entry
	 * @throws NoSuchHomeEntryException if a home entry with the primary key could not be found
	 */
	@Override
	public HomeEntry[] findByFieldFinder_PrevAndNext(
			long homeId, int floor, float square, int room,
			OrderByComparator<HomeEntry> orderByComparator)
		throws NoSuchHomeEntryException {

		HomeEntry homeEntry = findByPrimaryKey(homeId);

		Session session = null;

		try {
			session = openSession();

			HomeEntry[] array = new HomeEntryImpl[3];

			array[0] = getByFieldFinder_PrevAndNext(
				session, homeEntry, floor, square, room, orderByComparator,
				true);

			array[1] = homeEntry;

			array[2] = getByFieldFinder_PrevAndNext(
				session, homeEntry, floor, square, room, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected HomeEntry getByFieldFinder_PrevAndNext(
		Session session, HomeEntry homeEntry, int floor, float square, int room,
		OrderByComparator<HomeEntry> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				6 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(5);
		}

		sb.append(_SQL_SELECT_HOMEENTRY_WHERE);

		sb.append(_FINDER_COLUMN_FIELDFINDER_FLOOR_2);

		sb.append(_FINDER_COLUMN_FIELDFINDER_SQUARE_2);

		sb.append(_FINDER_COLUMN_FIELDFINDER_ROOM_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(HomeEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(floor);

		queryPos.add(square);

		queryPos.add(room);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(homeEntry)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<HomeEntry> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the home entries where floor = &#63; and square = &#63; and room = &#63; from the database.
	 *
	 * @param floor the floor
	 * @param square the square
	 * @param room the room
	 */
	@Override
	public void removeByFieldFinder(int floor, float square, int room) {
		for (HomeEntry homeEntry :
				findByFieldFinder(
					floor, square, room, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(homeEntry);
		}
	}

	/**
	 * Returns the number of home entries where floor = &#63; and square = &#63; and room = &#63;.
	 *
	 * @param floor the floor
	 * @param square the square
	 * @param room the room
	 * @return the number of matching home entries
	 */
	@Override
	public int countByFieldFinder(int floor, float square, int room) {
		FinderPath finderPath = _finderPathCountByFieldFinder;

		Object[] finderArgs = new Object[] {floor, square, room};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs);

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_HOMEENTRY_WHERE);

			sb.append(_FINDER_COLUMN_FIELDFINDER_FLOOR_2);

			sb.append(_FINDER_COLUMN_FIELDFINDER_SQUARE_2);

			sb.append(_FINDER_COLUMN_FIELDFINDER_ROOM_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(floor);

				queryPos.add(square);

				queryPos.add(room);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_FIELDFINDER_FLOOR_2 =
		"homeEntry.floor = ? AND ";

	private static final String _FINDER_COLUMN_FIELDFINDER_SQUARE_2 =
		"homeEntry.square = ? AND ";

	private static final String _FINDER_COLUMN_FIELDFINDER_ROOM_2 =
		"homeEntry.room = ?";

	public HomeEntryPersistenceImpl() {
		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("uuid", "uuid_");

		setDBColumnNames(dbColumnNames);

		setModelClass(HomeEntry.class);

		setModelImplClass(HomeEntryImpl.class);
		setModelPKClass(long.class);

		setTable(HomeEntryTable.INSTANCE);
	}

	/**
	 * Caches the home entry in the entity cache if it is enabled.
	 *
	 * @param homeEntry the home entry
	 */
	@Override
	public void cacheResult(HomeEntry homeEntry) {
		entityCache.putResult(
			HomeEntryImpl.class, homeEntry.getPrimaryKey(), homeEntry);

		finderCache.putResult(
			_finderPathFetchByUUID_G,
			new Object[] {homeEntry.getUuid(), homeEntry.getGroupId()},
			homeEntry);
	}

	private int _valueObjectFinderCacheListThreshold;

	/**
	 * Caches the home entries in the entity cache if it is enabled.
	 *
	 * @param homeEntries the home entries
	 */
	@Override
	public void cacheResult(List<HomeEntry> homeEntries) {
		if ((_valueObjectFinderCacheListThreshold == 0) ||
			((_valueObjectFinderCacheListThreshold > 0) &&
			 (homeEntries.size() > _valueObjectFinderCacheListThreshold))) {

			return;
		}

		for (HomeEntry homeEntry : homeEntries) {
			if (entityCache.getResult(
					HomeEntryImpl.class, homeEntry.getPrimaryKey()) == null) {

				cacheResult(homeEntry);
			}
		}
	}

	/**
	 * Clears the cache for all home entries.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(HomeEntryImpl.class);

		finderCache.clearCache(HomeEntryImpl.class);
	}

	/**
	 * Clears the cache for the home entry.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(HomeEntry homeEntry) {
		entityCache.removeResult(HomeEntryImpl.class, homeEntry);
	}

	@Override
	public void clearCache(List<HomeEntry> homeEntries) {
		for (HomeEntry homeEntry : homeEntries) {
			entityCache.removeResult(HomeEntryImpl.class, homeEntry);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(HomeEntryImpl.class);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(HomeEntryImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		HomeEntryModelImpl homeEntryModelImpl) {

		Object[] args = new Object[] {
			homeEntryModelImpl.getUuid(), homeEntryModelImpl.getGroupId()
		};

		finderCache.putResult(_finderPathCountByUUID_G, args, Long.valueOf(1));
		finderCache.putResult(
			_finderPathFetchByUUID_G, args, homeEntryModelImpl);
	}

	/**
	 * Creates a new home entry with the primary key. Does not add the home entry to the database.
	 *
	 * @param homeId the primary key for the new home entry
	 * @return the new home entry
	 */
	@Override
	public HomeEntry create(long homeId) {
		HomeEntry homeEntry = new HomeEntryImpl();

		homeEntry.setNew(true);
		homeEntry.setPrimaryKey(homeId);

		String uuid = PortalUUIDUtil.generate();

		homeEntry.setUuid(uuid);

		homeEntry.setCompanyId(CompanyThreadLocal.getCompanyId());

		return homeEntry;
	}

	/**
	 * Removes the home entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param homeId the primary key of the home entry
	 * @return the home entry that was removed
	 * @throws NoSuchHomeEntryException if a home entry with the primary key could not be found
	 */
	@Override
	public HomeEntry remove(long homeId) throws NoSuchHomeEntryException {
		return remove((Serializable)homeId);
	}

	/**
	 * Removes the home entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the home entry
	 * @return the home entry that was removed
	 * @throws NoSuchHomeEntryException if a home entry with the primary key could not be found
	 */
	@Override
	public HomeEntry remove(Serializable primaryKey)
		throws NoSuchHomeEntryException {

		Session session = null;

		try {
			session = openSession();

			HomeEntry homeEntry = (HomeEntry)session.get(
				HomeEntryImpl.class, primaryKey);

			if (homeEntry == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchHomeEntryException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(homeEntry);
		}
		catch (NoSuchHomeEntryException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected HomeEntry removeImpl(HomeEntry homeEntry) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(homeEntry)) {
				homeEntry = (HomeEntry)session.get(
					HomeEntryImpl.class, homeEntry.getPrimaryKeyObj());
			}

			if (homeEntry != null) {
				session.delete(homeEntry);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (homeEntry != null) {
			clearCache(homeEntry);
		}

		return homeEntry;
	}

	@Override
	public HomeEntry updateImpl(HomeEntry homeEntry) {
		boolean isNew = homeEntry.isNew();

		if (!(homeEntry instanceof HomeEntryModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(homeEntry.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(homeEntry);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in homeEntry proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom HomeEntry implementation " +
					homeEntry.getClass());
		}

		HomeEntryModelImpl homeEntryModelImpl = (HomeEntryModelImpl)homeEntry;

		if (Validator.isNull(homeEntry.getUuid())) {
			String uuid = PortalUUIDUtil.generate();

			homeEntry.setUuid(uuid);
		}

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date date = new Date();

		if (isNew && (homeEntry.getCreateDate() == null)) {
			if (serviceContext == null) {
				homeEntry.setCreateDate(date);
			}
			else {
				homeEntry.setCreateDate(serviceContext.getCreateDate(date));
			}
		}

		if (!homeEntryModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				homeEntry.setModifiedDate(date);
			}
			else {
				homeEntry.setModifiedDate(serviceContext.getModifiedDate(date));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(homeEntry);
			}
			else {
				homeEntry = (HomeEntry)session.merge(homeEntry);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(
			HomeEntryImpl.class, homeEntryModelImpl, false, true);

		cacheUniqueFindersCache(homeEntryModelImpl);

		if (isNew) {
			homeEntry.setNew(false);
		}

		homeEntry.resetOriginalValues();

		return homeEntry;
	}

	/**
	 * Returns the home entry with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the home entry
	 * @return the home entry
	 * @throws NoSuchHomeEntryException if a home entry with the primary key could not be found
	 */
	@Override
	public HomeEntry findByPrimaryKey(Serializable primaryKey)
		throws NoSuchHomeEntryException {

		HomeEntry homeEntry = fetchByPrimaryKey(primaryKey);

		if (homeEntry == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchHomeEntryException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return homeEntry;
	}

	/**
	 * Returns the home entry with the primary key or throws a <code>NoSuchHomeEntryException</code> if it could not be found.
	 *
	 * @param homeId the primary key of the home entry
	 * @return the home entry
	 * @throws NoSuchHomeEntryException if a home entry with the primary key could not be found
	 */
	@Override
	public HomeEntry findByPrimaryKey(long homeId)
		throws NoSuchHomeEntryException {

		return findByPrimaryKey((Serializable)homeId);
	}

	/**
	 * Returns the home entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param homeId the primary key of the home entry
	 * @return the home entry, or <code>null</code> if a home entry with the primary key could not be found
	 */
	@Override
	public HomeEntry fetchByPrimaryKey(long homeId) {
		return fetchByPrimaryKey((Serializable)homeId);
	}

	/**
	 * Returns all the home entries.
	 *
	 * @return the home entries
	 */
	@Override
	public List<HomeEntry> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the home entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>HomeEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of home entries
	 * @param end the upper bound of the range of home entries (not inclusive)
	 * @return the range of home entries
	 */
	@Override
	public List<HomeEntry> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the home entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>HomeEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of home entries
	 * @param end the upper bound of the range of home entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of home entries
	 */
	@Override
	public List<HomeEntry> findAll(
		int start, int end, OrderByComparator<HomeEntry> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the home entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>HomeEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of home entries
	 * @param end the upper bound of the range of home entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of home entries
	 */
	@Override
	public List<HomeEntry> findAll(
		int start, int end, OrderByComparator<HomeEntry> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<HomeEntry> list = null;

		if (useFinderCache) {
			list = (List<HomeEntry>)finderCache.getResult(
				finderPath, finderArgs);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_HOMEENTRY);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_HOMEENTRY;

				sql = sql.concat(HomeEntryModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<HomeEntry>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the home entries from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (HomeEntry homeEntry : findAll()) {
			remove(homeEntry);
		}
	}

	/**
	 * Returns the number of home entries.
	 *
	 * @return the number of home entries
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_HOMEENTRY);

				count = (Long)query.uniqueResult();

				finderCache.putResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "homeId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_HOMEENTRY;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return HomeEntryModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the home entry persistence.
	 */
	@Activate
	public void activate() {
		_valueObjectFinderCacheListThreshold = GetterUtil.getInteger(
			PropsUtil.get(PropsKeys.VALUE_OBJECT_FINDER_CACHE_LIST_THRESHOLD));

		_finderPathWithPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathCountAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0], new String[0], false);

		_finderPathWithPaginationFindByUuid = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"uuid_"}, true);

		_finderPathWithoutPaginationFindByUuid = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid",
			new String[] {String.class.getName()}, new String[] {"uuid_"},
			true);

		_finderPathCountByUuid = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid",
			new String[] {String.class.getName()}, new String[] {"uuid_"},
			false);

		_finderPathFetchByUUID_G = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByUUID_G",
			new String[] {String.class.getName(), Long.class.getName()},
			new String[] {"uuid_", "groupId"}, true);

		_finderPathCountByUUID_G = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUUID_G",
			new String[] {String.class.getName(), Long.class.getName()},
			new String[] {"uuid_", "groupId"}, false);

		_finderPathWithPaginationFindByUuid_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid_C",
			new String[] {
				String.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"uuid_", "companyId"}, true);

		_finderPathWithoutPaginationFindByUuid_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid_C",
			new String[] {String.class.getName(), Long.class.getName()},
			new String[] {"uuid_", "companyId"}, true);

		_finderPathCountByUuid_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid_C",
			new String[] {String.class.getName(), Long.class.getName()},
			new String[] {"uuid_", "companyId"}, false);

		_finderPathWithPaginationFindByFieldFinder = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByFieldFinder",
			new String[] {
				Integer.class.getName(), Float.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"floor", "square", "room"}, true);

		_finderPathWithoutPaginationFindByFieldFinder = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByFieldFinder",
			new String[] {
				Integer.class.getName(), Float.class.getName(),
				Integer.class.getName()
			},
			new String[] {"floor", "square", "room"}, true);

		_finderPathCountByFieldFinder = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByFieldFinder",
			new String[] {
				Integer.class.getName(), Float.class.getName(),
				Integer.class.getName()
			},
			new String[] {"floor", "square", "room"}, false);

		_setHomeEntryUtilPersistence(this);
	}

	@Deactivate
	public void deactivate() {
		_setHomeEntryUtilPersistence(null);

		entityCache.removeCache(HomeEntryImpl.class.getName());
	}

	private void _setHomeEntryUtilPersistence(
		HomeEntryPersistence homeEntryPersistence) {

		try {
			Field field = HomeEntryUtil.class.getDeclaredField("_persistence");

			field.setAccessible(true);

			field.set(null, homeEntryPersistence);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	@Override
	@Reference(
		target = DGPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
	}

	@Override
	@Reference(
		target = DGPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = DGPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_HOMEENTRY =
		"SELECT homeEntry FROM HomeEntry homeEntry";

	private static final String _SQL_SELECT_HOMEENTRY_WHERE =
		"SELECT homeEntry FROM HomeEntry homeEntry WHERE ";

	private static final String _SQL_COUNT_HOMEENTRY =
		"SELECT COUNT(homeEntry) FROM HomeEntry homeEntry";

	private static final String _SQL_COUNT_HOMEENTRY_WHERE =
		"SELECT COUNT(homeEntry) FROM HomeEntry homeEntry WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "homeEntry.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No HomeEntry exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No HomeEntry exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		HomeEntryPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"uuid"});

	@Override
	protected FinderCache getFinderCache() {
		return finderCache;
	}

	@Reference
	private HomeEntryModelArgumentsResolver _homeEntryModelArgumentsResolver;

}