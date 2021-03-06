/**
 * 
 */
package org.sagacity.sqltoy.executor;

import java.io.Serializable;
import java.lang.reflect.Type;

import javax.sql.DataSource;

import org.sagacity.sqltoy.callback.ReflectPropertyHandler;
import org.sagacity.sqltoy.callback.RowCallbackHandler;
import org.sagacity.sqltoy.config.SqlConfigParseUtils;
import org.sagacity.sqltoy.config.model.SqlToyConfig;
import org.sagacity.sqltoy.utils.ParamFilterUtils;

/**
 * @project sqltoy-orm
 * @description 构造统一的查询条件模型
 * @author renfei.chen <a href="mailto:zhongxuchen@gmail.com">联系作者</a>
 * @version id:QueryExecutor.java,Revision:v1.0,Date:2012-9-3
 */
public class QueryExecutor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6149173009738072148L;

	/**
	 * 实体对象
	 */
	private Serializable entity;

	/**
	 * sql中参数名称
	 */
	private String[] paramsName;

	/**
	 * sql中参数名称对应的值
	 */
	private Object[] paramsValue;

	/**
	 * 原生数值
	 */
	private Object[] shardingParamsValue;

	/**
	 * sql语句或sqlId
	 */
	private String sql;

	/**
	 * jdbc 查询时默认加载到内存中的记录数量 -1表示不设置，采用数据库默认的值
	 */
	private int fetchSize = -1;

	/**
	 * jdbc查询最大返回记录数量
	 */
	private int maxRows = -1;

	/**
	 * 结果集反调处理(已经极少极少使用,可以废弃)
	 */
	@Deprecated
	private RowCallbackHandler rowCallbackHandler;

	/**
	 * 查询属性值反射处理
	 */
	private ReflectPropertyHandler reflectPropertyHandler;

	/**
	 * 查询结果类型
	 */
	private Type resultType;

	/**
	 * 结果为map时标题是否变成驼峰模式
	 */
	private boolean humpMapLabel = true;

	/**
	 * 特定数据库连接资源
	 */
	private DataSource dataSource;

	/**
	 * 是否已经提取过value值
	 */
	private boolean extracted = false;

	public QueryExecutor(String sql) {
		this.sql = sql;
	}

	/**
	 * update 2018-4-10 很对开发者将entity传入Class类别然后抱怨sqltoy有bug,
	 * 
	 * @param sql
	 * @param entity
	 * @throws Exception
	 */
	public QueryExecutor(String sql, Serializable entity) throws Exception {
		this.sql = sql;
		this.entity = entity;
		if (entity != null) {
			this.resultType = entity.getClass();
			if (this.resultType.equals("".getClass().getClass()))
				throw new Exception("查询参数是要求传递对象的实例,不是传递对象的class类别!你的参数=" + ((Class) entity).getName());
		}
	}

	public QueryExecutor(String sql, String[] paramsName, Object[] paramsValue) {
		this.sql = sql;
		this.paramsName = paramsName;
		this.paramsValue = paramsValue;
		this.shardingParamsValue = paramsValue;
	}

	public QueryExecutor dataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		return this;
	}

	public QueryExecutor resultType(Type resultType) {
		this.resultType = resultType;
		return this;
	}

	public QueryExecutor fetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
		return this;
	}

	public QueryExecutor maxRows(int maxRows) {
		this.maxRows = maxRows;
		return this;
	}

	public QueryExecutor humpMapLabel(boolean humpMapLabel) {
		this.humpMapLabel = humpMapLabel;
		return this;
	}

	@Deprecated
	public QueryExecutor rowCallbackHandler(RowCallbackHandler rowCallbackHandler) {
		this.rowCallbackHandler = rowCallbackHandler;
		return this;
	}

	public QueryExecutor reflectPropertyHandler(ReflectPropertyHandler reflectPropertyHandler) {
		this.reflectPropertyHandler = reflectPropertyHandler;
		return this;
	}

	/**
	 * @return the rowCallbackHandler
	 */
	public RowCallbackHandler getRowCallbackHandler() {
		return rowCallbackHandler;
	}

	/**
	 * @return the entity
	 */
	public Serializable getEntity() {
		return entity;
	}

	/**
	 * @return the paramsName
	 */
	public String[] getParamsName() {
		return paramsName;
	}

	/**
	 * @return the paramsName
	 */
	public String[] getParamsName(SqlToyConfig sqlToyConfig) {
		if (this.entity == null) {
			if (paramsName == null || paramsName.length == 0)
				return sqlToyConfig.getParamsName();
			else
				return paramsName;
		} else
			return sqlToyConfig.getParamsName();
	}

	/**
	 * @param sqlToyConfig
	 * @return
	 */
	public String[] getTableShardingParamsName(SqlToyConfig sqlToyConfig) {
		if (this.entity == null) {
			if (paramsName == null || paramsName.length == 0)
				return sqlToyConfig.getParamsName();
			else
				return paramsName;
		} else
			return sqlToyConfig.getTableShardingParams();
	}

	/**
	 * 
	 * @param sqlToyConfig
	 * @return
	 */
	public String[] getDataSourceShardingParamsName(SqlToyConfig sqlToyConfig) {
		if (this.entity == null) {
			if (paramsName == null || paramsName.length == 0)
				return sqlToyConfig.getParamsName();
			else
				return paramsName;
		} else
			return sqlToyConfig.getDataSourceShardingParams();
	}

	/**
	 * @return the paramsValue
	 */
	public Object[] getParamsValue() {
		return paramsValue;
	}

	/**
	 * @return the fetchSize
	 */
	public int getFetchSize() {
		return fetchSize;
	}

	/**
	 * @todo 获取sql中参数对应的值
	 * @param sqlToyConfig
	 * @return
	 * @throws Exception
	 */
	public Object[] getParamsValue(SqlToyConfig sqlToyConfig) throws Exception {
		Object[] realValues = null;
		// 是否萃取过
		if (!extracted) {
			if (this.entity != null) {
				paramsValue = SqlConfigParseUtils.reflectBeanParams(sqlToyConfig.getParamsName(), this.entity,
						reflectPropertyHandler);
			}
			extracted = true;
		}
		if (paramsValue != null)
			realValues = paramsValue.clone();
		// 过滤加工参数值
		if (realValues != null) {
			realValues = ParamFilterUtils.filterValue(getParamsName(sqlToyConfig), realValues,
					sqlToyConfig.getFilters());
		} else {
			// update 2017-4-11,默认参数值跟参数数组长度保持一致,并置为null
			String[] names = getParamsName(sqlToyConfig);
			if (names != null && names.length > 0) {
				realValues = new Object[names.length];
			}
		}
		return realValues;
	}

	/**
	 * @todo 获取分表时传递给分表策略的参数值
	 * @param sqlToyConfig
	 * @return
	 * @throws Exception
	 */
	public Object[] getTableShardingParamsValue(SqlToyConfig sqlToyConfig) throws Exception {
		if (this.entity != null) {
			return SqlConfigParseUtils.reflectBeanParams(sqlToyConfig.getTableShardingParams(), this.entity,
					reflectPropertyHandler);
		}
		return shardingParamsValue;
	}

	/**
	 * @todo 获取分库时传递给分库策略的参数值(策略会根据值通过逻辑返回具体的库)
	 * @param sqlToyConfig
	 * @return
	 * @throws Exception
	 */
	public Object[] getDataSourceShardingParamsValue(SqlToyConfig sqlToyConfig) throws Exception {
		if (this.entity != null) {
			return SqlConfigParseUtils.reflectBeanParams(sqlToyConfig.getDataSourceShardingParams(), this.entity,
					reflectPropertyHandler);
		}
		return shardingParamsValue;
	}

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @return the sqlOrNamed
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * @return the resultType
	 */
	public Type getResultType() {
		return resultType;
	}

	public String getResultTypeName() {
		if (null != resultType)
			return resultType.getTypeName();
		else
			return null;
	}

	/**
	 * @return the humpMapLabel
	 */
	public boolean isHumpMapLabel() {
		return humpMapLabel;
	}

	/**
	 * @return the reflectPropertyHandler
	 */
	public ReflectPropertyHandler getReflectPropertyHandler() {
		return reflectPropertyHandler;
	}

	/**
	 * @return the maxRows
	 */
	public int getMaxRows() {
		return maxRows;
	}
}
