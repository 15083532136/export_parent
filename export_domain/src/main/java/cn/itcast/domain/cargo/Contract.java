package cn.itcast.domain.cargo;



import cn.itcast.domain.BaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 购销合同实体类
 */

public class Contract extends BaseEntity implements Serializable {
	private String id;
	/**
	 * 收购方
	 */
	private String offeror;
	/**
	 * 合同号，订单号
	 */
	private String contractNo;
	/**
	 * 签单日期
	 */
	private Date signingDate;
	/**
	 * 签单人
	 */
	private String inputBy;
	/**
	 * 审单人
	 */
	private String checkBy;
	/**
	 * 验货员
	 */
	private String inspector;
	/**
	 * 总金额=货物的总金额+附件的总金额    冗余字段，为了进行分散计算
	 */
	private BigDecimal totalAmount;
	/**
	 * 要求
	 */
	private String crequest;
	/**
	 * 客户名称
	 */
	private String customName;
	/**
	 * 船期
	 */
	private Date shipTime;
	/**
	 * 重要程度
	 */
	private Integer importNum;
	/**
	 * 交货期限
	 */
	private Date deliveryPeriod;
	/**
	 * 旧的状态，报运
	 */
	private Integer oldState;
	/**
	 * 出货状态，报运
	 */
	private Integer outState;
	/**
	 * 贸易条款
	 */
	private String tradeTerms;
	/**
	 * 打印板式，1打印一个货物2打印两个货物
	 */
	private String printStyle;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 *  状态：0草稿 1已上报待报运	2 已报运
	 */
	private Integer state;
	/**
	 * 货物数量
	 */
	private Integer proNum;
	/**
	 * 附件数量
	 */
	private Integer extNum;

	public Contract(){}

	public String getId() {
		return id;
	}

	public Contract setId(String id) {
		this.id = id;
		return this;
	}

	public String getOfferor() {
		return offeror;
	}

	public Contract setOfferor(String offeror) {
		this.offeror = offeror;
		return this;
	}

	public String getContractNo() {
		return contractNo;
	}

	public Contract setContractNo(String contractNo) {
		this.contractNo = contractNo;
		return this;
	}

	public Date getSigningDate() {
		return signingDate;
	}

	public Contract setSigningDate(Date signingDate) {
		this.signingDate = signingDate;
		return this;
	}

	public String getInputBy() {
		return inputBy;
	}

	public Contract setInputBy(String inputBy) {
		this.inputBy = inputBy;
		return this;
	}

	public String getCheckBy() {
		return checkBy;
	}

	public Contract setCheckBy(String checkBy) {
		this.checkBy = checkBy;
		return this;
	}

	public String getInspector() {
		return inspector;
	}

	public Contract setInspector(String inspector) {
		this.inspector = inspector;
		return this;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public Contract setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
		return this;
	}

	public String getCrequest() {
		return crequest;
	}

	public Contract setCrequest(String crequest) {
		this.crequest = crequest;
		return this;
	}

	public String getCustomName() {
		return customName;
	}

	public Contract setCustomName(String customName) {
		this.customName = customName;
		return this;
	}

	public Date getShipTime() {
		return shipTime;
	}

	public Contract setShipTime(Date shipTime) {
		this.shipTime = shipTime;
		return this;
	}

	public Integer getImportNum() {
		return importNum;
	}

	public Contract setImportNum(Integer importNum) {
		this.importNum = importNum;
		return this;
	}

	public Date getDeliveryPeriod() {
		return deliveryPeriod;
	}

	public Contract setDeliveryPeriod(Date deliveryPeriod) {
		this.deliveryPeriod = deliveryPeriod;
		return this;
	}

	public Integer getOldState() {
		return oldState;
	}

	public Contract setOldState(Integer oldState) {
		this.oldState = oldState;
		return this;
	}

	public Integer getOutState() {
		return outState;
	}

	public Contract setOutState(Integer outState) {
		this.outState = outState;
		return this;
	}

	public String getTradeTerms() {
		return tradeTerms;
	}

	public Contract setTradeTerms(String tradeTerms) {
		this.tradeTerms = tradeTerms;
		return this;
	}

	public String getPrintStyle() {
		return printStyle;
	}

	public Contract setPrintStyle(String printStyle) {
		this.printStyle = printStyle;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public Contract setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public Integer getState() {
		return state;
	}

	public Contract setState(Integer state) {
		this.state = state;
		return this;
	}

	public Integer getProNum() {
		return proNum;
	}

	public Contract setProNum(Integer proNum) {
		this.proNum = proNum;
		return this;
	}

	public Integer getExtNum() {
		return extNum;
	}

	public Contract setExtNum(Integer extNum) {
		this.extNum = extNum;
		return this;
	}
}
