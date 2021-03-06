/**
 *@Generated by QuickVO Tools 2.0
 */
package sqltoy.showcase.system.vo;

import org.sagacity.sqltoy.config.annotation.Sharding;
import org.sagacity.sqltoy.config.annotation.SqlToyEntity;
import org.sagacity.sqltoy.config.annotation.Strategy;

import java.util.Date;
import sqltoy.showcase.system.vo.base.AbstractStaffInfoVO;

/**
 * @project sqltoy-showcase
 * @author zhongxuchen
 * @version 1.0.0 员工信息表 StaffInfoVO generated by sys_staff_info
 */
@SqlToyEntity
/*
 * db则是分库策略配置,table 则是分表策略配置，可以同时配置也可以独立配置
 * 策略name要跟spring中的bean定义name一致,fields表示要以对象的哪几个字段值作为判断依据,可以一个或多个字段
 * maxConcurrents:可选配置，表示最大并行数 maxWaitSeconds:可选配置，表示最大等待秒数
 */
@Sharding(db = @Strategy(name = "hashDataSourceSharding", fields = {
		"staffId" }), table = @Strategy(name = "hashDataSourceSharding", fields = {
				"staffId" }), maxConcurrents = 10, maxWaitSeconds = 3600)
public class StaffInfoVO extends AbstractStaffInfoVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4657027516282416619L;

	/** default constructor */
	public StaffInfoVO() {
		super();
	}

	/*---begin-constructor-area---don't-update-this-area--*/
	/** pk constructor */
	public StaffInfoVO(String staffId) {
		this.staffId = staffId;
	}

	/** minimal constructor */
	public StaffInfoVO(String staffId, String staffCode, String organId, String staffName, String operator,
			Date operateDate, String status) {
		this.staffId = staffId;
		this.staffCode = staffCode;
		this.organId = organId;
		this.staffName = staffName;
		this.operator = operator;
		this.operateDate = operateDate;
		this.status = status;
	}

	/** full constructor */
	public StaffInfoVO(String staffId, String staffCode, String organId, String staffName, String sexType,
			String mobileTel, Date birthday, Date dutyDate, Date outDutyDate, String post, String nativePlace,
			String email, String operator, Date operateDate, String status) {
		this.staffId = staffId;
		this.staffCode = staffCode;
		this.organId = organId;
		this.staffName = staffName;
		this.sexType = sexType;
		this.mobileTel = mobileTel;
		this.birthday = birthday;
		this.dutyDate = dutyDate;
		this.outDutyDate = outDutyDate;
		this.post = post;
		this.nativePlace = nativePlace;
		this.email = email;
		this.operator = operator;
		this.operateDate = operateDate;
		this.status = status;
	}

	/*---end-constructor-area---don't-update-this-area--*/

	/**
	 * 机构名称
	 */
	private String organName;

	/**
	 * 性别名称
	 */
	private String sexName;

	/**
	 * 岗位名称
	 */
	private String postName;

	/**
	 * @todo vo columns to String
	 */
	public String toString() {
		return super.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public StaffInfoVO clone() {
		try {
			// TODO Auto-generated method stub
			return (StaffInfoVO) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return the organName
	 */
	public String getOrganName() {
		return organName;
	}

	/**
	 * @param organName
	 *            the organName to set
	 */
	public void setOrganName(String organName) {
		this.organName = organName;
	}

	/**
	 * @return the sexName
	 */
	public String getSexName() {
		return sexName;
	}

	/**
	 * @param sexName
	 *            the sexName to set
	 */
	public void setSexName(String sexName) {
		this.sexName = sexName;
	}

	/**
	 * @return the postName
	 */
	public String getPostName() {
		return postName;
	}

	/**
	 * @param postName
	 *            the postName to set
	 */
	public void setPostName(String postName) {
		this.postName = postName;
	}

}
