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

package com.poc.home.model.impl;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringUtil;

import com.poc.home.model.HomeEntry;
import com.poc.home.model.HomeEntryModel;
import com.poc.home.model.HomeEntrySoap;

import java.io.Serializable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;

import java.sql.Blob;
import java.sql.Types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The base model implementation for the HomeEntry service. Represents a row in the &quot;DG_HomeEntry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>HomeEntryModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link HomeEntryImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see HomeEntryImpl
 * @generated
 */
@JSON(strict = true)
public class HomeEntryModelImpl
	extends BaseModelImpl<HomeEntry> implements HomeEntryModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a home entry model instance should use the <code>HomeEntry</code> interface instead.
	 */
	public static final String TABLE_NAME = "DG_HomeEntry";

	public static final Object[][] TABLE_COLUMNS = {
		{"uuid_", Types.VARCHAR}, {"homeId", Types.BIGINT},
		{"groupId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"userId", Types.BIGINT}, {"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP}, {"modifiedDate", Types.TIMESTAMP},
		{"address", Types.VARCHAR}, {"floor", Types.INTEGER},
		{"square", Types.FLOAT}, {"room", Types.INTEGER},
		{"color", Types.VARCHAR}, {"owner", Types.VARCHAR}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("homeId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("address", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("floor", Types.INTEGER);
		TABLE_COLUMNS_MAP.put("square", Types.FLOAT);
		TABLE_COLUMNS_MAP.put("room", Types.INTEGER);
		TABLE_COLUMNS_MAP.put("color", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("owner", Types.VARCHAR);
	}

	public static final String TABLE_SQL_CREATE =
		"create table DG_HomeEntry (uuid_ VARCHAR(75) null,homeId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,address VARCHAR(75) null,floor INTEGER,square DOUBLE,room INTEGER,color VARCHAR(75) null,owner VARCHAR(75) null)";

	public static final String TABLE_SQL_DROP = "drop table DG_HomeEntry";

	public static final String ORDER_BY_JPQL =
		" ORDER BY homeEntry.createDate DESC, homeEntry.modifiedDate DESC";

	public static final String ORDER_BY_SQL =
		" ORDER BY DG_HomeEntry.createDate DESC, DG_HomeEntry.modifiedDate DESC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long COMPANYID_COLUMN_BITMASK = 1L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long FLOOR_COLUMN_BITMASK = 2L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long GROUPID_COLUMN_BITMASK = 4L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long ROOM_COLUMN_BITMASK = 8L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long SQUARE_COLUMN_BITMASK = 16L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long UUID_COLUMN_BITMASK = 32L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *		#getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long CREATEDATE_COLUMN_BITMASK = 64L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *		#getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long MODIFIEDDATE_COLUMN_BITMASK = 128L;

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static void setEntityCacheEnabled(boolean entityCacheEnabled) {
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static void setFinderCacheEnabled(boolean finderCacheEnabled) {
	}

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static HomeEntry toModel(HomeEntrySoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		HomeEntry model = new HomeEntryImpl();

		model.setUuid(soapModel.getUuid());
		model.setHomeId(soapModel.getHomeId());
		model.setGroupId(soapModel.getGroupId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setUserId(soapModel.getUserId());
		model.setUserName(soapModel.getUserName());
		model.setCreateDate(soapModel.getCreateDate());
		model.setModifiedDate(soapModel.getModifiedDate());
		model.setAddress(soapModel.getAddress());
		model.setFloor(soapModel.getFloor());
		model.setSquare(soapModel.getSquare());
		model.setRoom(soapModel.getRoom());
		model.setColor(soapModel.getColor());
		model.setOwner(soapModel.getOwner());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static List<HomeEntry> toModels(HomeEntrySoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<HomeEntry> models = new ArrayList<HomeEntry>(soapModels.length);

		for (HomeEntrySoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public HomeEntryModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _homeId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setHomeId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _homeId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return HomeEntry.class;
	}

	@Override
	public String getModelClassName() {
		return HomeEntry.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<HomeEntry, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		for (Map.Entry<String, Function<HomeEntry, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<HomeEntry, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName, attributeGetterFunction.apply((HomeEntry)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<HomeEntry, Object>> attributeSetterBiConsumers =
			getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<HomeEntry, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(HomeEntry)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<HomeEntry, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<HomeEntry, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static Function<InvocationHandler, HomeEntry>
		_getProxyProviderFunction() {

		Class<?> proxyClass = ProxyUtil.getProxyClass(
			HomeEntry.class.getClassLoader(), HomeEntry.class,
			ModelWrapper.class);

		try {
			Constructor<HomeEntry> constructor =
				(Constructor<HomeEntry>)proxyClass.getConstructor(
					InvocationHandler.class);

			return invocationHandler -> {
				try {
					return constructor.newInstance(invocationHandler);
				}
				catch (ReflectiveOperationException
							reflectiveOperationException) {

					throw new InternalError(reflectiveOperationException);
				}
			};
		}
		catch (NoSuchMethodException noSuchMethodException) {
			throw new InternalError(noSuchMethodException);
		}
	}

	private static final Map<String, Function<HomeEntry, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<HomeEntry, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<HomeEntry, Object>> attributeGetterFunctions =
			new LinkedHashMap<String, Function<HomeEntry, Object>>();
		Map<String, BiConsumer<HomeEntry, ?>> attributeSetterBiConsumers =
			new LinkedHashMap<String, BiConsumer<HomeEntry, ?>>();

		attributeGetterFunctions.put("uuid", HomeEntry::getUuid);
		attributeSetterBiConsumers.put(
			"uuid", (BiConsumer<HomeEntry, String>)HomeEntry::setUuid);
		attributeGetterFunctions.put("homeId", HomeEntry::getHomeId);
		attributeSetterBiConsumers.put(
			"homeId", (BiConsumer<HomeEntry, Long>)HomeEntry::setHomeId);
		attributeGetterFunctions.put("groupId", HomeEntry::getGroupId);
		attributeSetterBiConsumers.put(
			"groupId", (BiConsumer<HomeEntry, Long>)HomeEntry::setGroupId);
		attributeGetterFunctions.put("companyId", HomeEntry::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId", (BiConsumer<HomeEntry, Long>)HomeEntry::setCompanyId);
		attributeGetterFunctions.put("userId", HomeEntry::getUserId);
		attributeSetterBiConsumers.put(
			"userId", (BiConsumer<HomeEntry, Long>)HomeEntry::setUserId);
		attributeGetterFunctions.put("userName", HomeEntry::getUserName);
		attributeSetterBiConsumers.put(
			"userName", (BiConsumer<HomeEntry, String>)HomeEntry::setUserName);
		attributeGetterFunctions.put("createDate", HomeEntry::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<HomeEntry, Date>)HomeEntry::setCreateDate);
		attributeGetterFunctions.put(
			"modifiedDate", HomeEntry::getModifiedDate);
		attributeSetterBiConsumers.put(
			"modifiedDate",
			(BiConsumer<HomeEntry, Date>)HomeEntry::setModifiedDate);
		attributeGetterFunctions.put("address", HomeEntry::getAddress);
		attributeSetterBiConsumers.put(
			"address", (BiConsumer<HomeEntry, String>)HomeEntry::setAddress);
		attributeGetterFunctions.put("floor", HomeEntry::getFloor);
		attributeSetterBiConsumers.put(
			"floor", (BiConsumer<HomeEntry, Integer>)HomeEntry::setFloor);
		attributeGetterFunctions.put("square", HomeEntry::getSquare);
		attributeSetterBiConsumers.put(
			"square", (BiConsumer<HomeEntry, Float>)HomeEntry::setSquare);
		attributeGetterFunctions.put("room", HomeEntry::getRoom);
		attributeSetterBiConsumers.put(
			"room", (BiConsumer<HomeEntry, Integer>)HomeEntry::setRoom);
		attributeGetterFunctions.put("color", HomeEntry::getColor);
		attributeSetterBiConsumers.put(
			"color", (BiConsumer<HomeEntry, String>)HomeEntry::setColor);
		attributeGetterFunctions.put("owner", HomeEntry::getOwner);
		attributeSetterBiConsumers.put(
			"owner", (BiConsumer<HomeEntry, String>)HomeEntry::setOwner);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@JSON
	@Override
	public String getUuid() {
		if (_uuid == null) {
			return "";
		}
		else {
			return _uuid;
		}
	}

	@Override
	public void setUuid(String uuid) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_uuid = uuid;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalUuid() {
		return getColumnOriginalValue("uuid_");
	}

	@JSON
	@Override
	public long getHomeId() {
		return _homeId;
	}

	@Override
	public void setHomeId(long homeId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_homeId = homeId;
	}

	@JSON
	@Override
	public long getGroupId() {
		return _groupId;
	}

	@Override
	public void setGroupId(long groupId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_groupId = groupId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalGroupId() {
		return GetterUtil.getLong(this.<Long>getColumnOriginalValue("groupId"));
	}

	@JSON
	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_companyId = companyId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalCompanyId() {
		return GetterUtil.getLong(
			this.<Long>getColumnOriginalValue("companyId"));
	}

	@JSON
	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_userId = userId;
	}

	@Override
	public String getUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getUserId());

			return user.getUuid();
		}
		catch (PortalException portalException) {
			return "";
		}
	}

	@Override
	public void setUserUuid(String userUuid) {
	}

	@JSON
	@Override
	public String getUserName() {
		if (_userName == null) {
			return "";
		}
		else {
			return _userName;
		}
	}

	@Override
	public void setUserName(String userName) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_userName = userName;
	}

	@JSON
	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_createDate = createDate;
	}

	@JSON
	@Override
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public boolean hasSetModifiedDate() {
		return _setModifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_setModifiedDate = true;

		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_modifiedDate = modifiedDate;
	}

	@JSON
	@Override
	public String getAddress() {
		if (_address == null) {
			return "";
		}
		else {
			return _address;
		}
	}

	@Override
	public void setAddress(String address) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_address = address;
	}

	@JSON
	@Override
	public int getFloor() {
		return _floor;
	}

	@Override
	public void setFloor(int floor) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_floor = floor;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public int getOriginalFloor() {
		return GetterUtil.getInteger(
			this.<Integer>getColumnOriginalValue("floor"));
	}

	@JSON
	@Override
	public float getSquare() {
		return _square;
	}

	@Override
	public void setSquare(float square) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_square = square;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public float getOriginalSquare() {
		return GetterUtil.getFloat(
			this.<Float>getColumnOriginalValue("square"));
	}

	@JSON
	@Override
	public int getRoom() {
		return _room;
	}

	@Override
	public void setRoom(int room) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_room = room;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public int getOriginalRoom() {
		return GetterUtil.getInteger(
			this.<Integer>getColumnOriginalValue("room"));
	}

	@JSON
	@Override
	public String getColor() {
		if (_color == null) {
			return "";
		}
		else {
			return _color;
		}
	}

	@Override
	public void setColor(String color) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_color = color;
	}

	@JSON
	@Override
	public String getOwner() {
		if (_owner == null) {
			return "";
		}
		else {
			return _owner;
		}
	}

	@Override
	public void setOwner(String owner) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_owner = owner;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return new StagedModelType(
			PortalUtil.getClassNameId(HomeEntry.class.getName()));
	}

	public long getColumnBitmask() {
		if (_columnBitmask > 0) {
			return _columnBitmask;
		}

		if ((_columnOriginalValues == null) ||
			(_columnOriginalValues == Collections.EMPTY_MAP)) {

			return 0;
		}

		for (Map.Entry<String, Object> entry :
				_columnOriginalValues.entrySet()) {

			if (!Objects.equals(
					entry.getValue(), getColumnValue(entry.getKey()))) {

				_columnBitmask |= _columnBitmasks.get(entry.getKey());
			}
		}

		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(
			getCompanyId(), HomeEntry.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public HomeEntry toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, HomeEntry>
				escapedModelProxyProviderFunction =
					EscapedModelProxyProviderFunctionHolder.
						_escapedModelProxyProviderFunction;

			_escapedModel = escapedModelProxyProviderFunction.apply(
				new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		HomeEntryImpl homeEntryImpl = new HomeEntryImpl();

		homeEntryImpl.setUuid(getUuid());
		homeEntryImpl.setHomeId(getHomeId());
		homeEntryImpl.setGroupId(getGroupId());
		homeEntryImpl.setCompanyId(getCompanyId());
		homeEntryImpl.setUserId(getUserId());
		homeEntryImpl.setUserName(getUserName());
		homeEntryImpl.setCreateDate(getCreateDate());
		homeEntryImpl.setModifiedDate(getModifiedDate());
		homeEntryImpl.setAddress(getAddress());
		homeEntryImpl.setFloor(getFloor());
		homeEntryImpl.setSquare(getSquare());
		homeEntryImpl.setRoom(getRoom());
		homeEntryImpl.setColor(getColor());
		homeEntryImpl.setOwner(getOwner());

		homeEntryImpl.resetOriginalValues();

		return homeEntryImpl;
	}

	@Override
	public HomeEntry cloneWithOriginalValues() {
		HomeEntryImpl homeEntryImpl = new HomeEntryImpl();

		homeEntryImpl.setUuid(this.<String>getColumnOriginalValue("uuid_"));
		homeEntryImpl.setHomeId(this.<Long>getColumnOriginalValue("homeId"));
		homeEntryImpl.setGroupId(this.<Long>getColumnOriginalValue("groupId"));
		homeEntryImpl.setCompanyId(
			this.<Long>getColumnOriginalValue("companyId"));
		homeEntryImpl.setUserId(this.<Long>getColumnOriginalValue("userId"));
		homeEntryImpl.setUserName(
			this.<String>getColumnOriginalValue("userName"));
		homeEntryImpl.setCreateDate(
			this.<Date>getColumnOriginalValue("createDate"));
		homeEntryImpl.setModifiedDate(
			this.<Date>getColumnOriginalValue("modifiedDate"));
		homeEntryImpl.setAddress(
			this.<String>getColumnOriginalValue("address"));
		homeEntryImpl.setFloor(this.<Integer>getColumnOriginalValue("floor"));
		homeEntryImpl.setSquare(this.<Float>getColumnOriginalValue("square"));
		homeEntryImpl.setRoom(this.<Integer>getColumnOriginalValue("room"));
		homeEntryImpl.setColor(this.<String>getColumnOriginalValue("color"));
		homeEntryImpl.setOwner(this.<String>getColumnOriginalValue("owner"));

		return homeEntryImpl;
	}

	@Override
	public int compareTo(HomeEntry homeEntry) {
		int value = 0;

		value = DateUtil.compareTo(getCreateDate(), homeEntry.getCreateDate());

		value = value * -1;

		if (value != 0) {
			return value;
		}

		value = DateUtil.compareTo(
			getModifiedDate(), homeEntry.getModifiedDate());

		value = value * -1;

		if (value != 0) {
			return value;
		}

		return 0;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof HomeEntry)) {
			return false;
		}

		HomeEntry homeEntry = (HomeEntry)object;

		long primaryKey = homeEntry.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public boolean isEntityCacheEnabled() {
		return true;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public boolean isFinderCacheEnabled() {
		return true;
	}

	@Override
	public void resetOriginalValues() {
		_columnOriginalValues = Collections.emptyMap();

		_setModifiedDate = false;

		_columnBitmask = 0;
	}

	@Override
	public CacheModel<HomeEntry> toCacheModel() {
		HomeEntryCacheModel homeEntryCacheModel = new HomeEntryCacheModel();

		homeEntryCacheModel.uuid = getUuid();

		String uuid = homeEntryCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			homeEntryCacheModel.uuid = null;
		}

		homeEntryCacheModel.homeId = getHomeId();

		homeEntryCacheModel.groupId = getGroupId();

		homeEntryCacheModel.companyId = getCompanyId();

		homeEntryCacheModel.userId = getUserId();

		homeEntryCacheModel.userName = getUserName();

		String userName = homeEntryCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			homeEntryCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			homeEntryCacheModel.createDate = createDate.getTime();
		}
		else {
			homeEntryCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			homeEntryCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			homeEntryCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		homeEntryCacheModel.address = getAddress();

		String address = homeEntryCacheModel.address;

		if ((address != null) && (address.length() == 0)) {
			homeEntryCacheModel.address = null;
		}

		homeEntryCacheModel.floor = getFloor();

		homeEntryCacheModel.square = getSquare();

		homeEntryCacheModel.room = getRoom();

		homeEntryCacheModel.color = getColor();

		String color = homeEntryCacheModel.color;

		if ((color != null) && (color.length() == 0)) {
			homeEntryCacheModel.color = null;
		}

		homeEntryCacheModel.owner = getOwner();

		String owner = homeEntryCacheModel.owner;

		if ((owner != null) && (owner.length() == 0)) {
			homeEntryCacheModel.owner = null;
		}

		return homeEntryCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<HomeEntry, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			(5 * attributeGetterFunctions.size()) + 2);

		sb.append("{");

		for (Map.Entry<String, Function<HomeEntry, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<HomeEntry, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("\"");
			sb.append(attributeName);
			sb.append("\": ");

			Object value = attributeGetterFunction.apply((HomeEntry)this);

			if (value == null) {
				sb.append("null");
			}
			else if (value instanceof Blob || value instanceof Date ||
					 value instanceof Map || value instanceof String) {

				sb.append(
					"\"" + StringUtil.replace(value.toString(), "\"", "'") +
						"\"");
			}
			else {
				sb.append(value);
			}

			sb.append(", ");
		}

		if (sb.index() > 1) {
			sb.setIndex(sb.index() - 1);
		}

		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		Map<String, Function<HomeEntry, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			(5 * attributeGetterFunctions.size()) + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<HomeEntry, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<HomeEntry, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((HomeEntry)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function<InvocationHandler, HomeEntry>
			_escapedModelProxyProviderFunction = _getProxyProviderFunction();

	}

	private String _uuid;
	private long _homeId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private String _address;
	private int _floor;
	private float _square;
	private int _room;
	private String _color;
	private String _owner;

	public <T> T getColumnValue(String columnName) {
		columnName = _attributeNames.getOrDefault(columnName, columnName);

		Function<HomeEntry, Object> function = _attributeGetterFunctions.get(
			columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((HomeEntry)this);
	}

	public <T> T getColumnOriginalValue(String columnName) {
		if (_columnOriginalValues == null) {
			return null;
		}

		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		return (T)_columnOriginalValues.get(columnName);
	}

	private void _setColumnOriginalValues() {
		_columnOriginalValues = new HashMap<String, Object>();

		_columnOriginalValues.put("uuid_", _uuid);
		_columnOriginalValues.put("homeId", _homeId);
		_columnOriginalValues.put("groupId", _groupId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("userId", _userId);
		_columnOriginalValues.put("userName", _userName);
		_columnOriginalValues.put("createDate", _createDate);
		_columnOriginalValues.put("modifiedDate", _modifiedDate);
		_columnOriginalValues.put("address", _address);
		_columnOriginalValues.put("floor", _floor);
		_columnOriginalValues.put("square", _square);
		_columnOriginalValues.put("room", _room);
		_columnOriginalValues.put("color", _color);
		_columnOriginalValues.put("owner", _owner);
	}

	private static final Map<String, String> _attributeNames;

	static {
		Map<String, String> attributeNames = new HashMap<>();

		attributeNames.put("uuid_", "uuid");

		_attributeNames = Collections.unmodifiableMap(attributeNames);
	}

	private transient Map<String, Object> _columnOriginalValues;

	public static long getColumnBitmask(String columnName) {
		return _columnBitmasks.get(columnName);
	}

	private static final Map<String, Long> _columnBitmasks;

	static {
		Map<String, Long> columnBitmasks = new HashMap<>();

		columnBitmasks.put("uuid_", 1L);

		columnBitmasks.put("homeId", 2L);

		columnBitmasks.put("groupId", 4L);

		columnBitmasks.put("companyId", 8L);

		columnBitmasks.put("userId", 16L);

		columnBitmasks.put("userName", 32L);

		columnBitmasks.put("createDate", 64L);

		columnBitmasks.put("modifiedDate", 128L);

		columnBitmasks.put("address", 256L);

		columnBitmasks.put("floor", 512L);

		columnBitmasks.put("square", 1024L);

		columnBitmasks.put("room", 2048L);

		columnBitmasks.put("color", 4096L);

		columnBitmasks.put("owner", 8192L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private HomeEntry _escapedModel;

}