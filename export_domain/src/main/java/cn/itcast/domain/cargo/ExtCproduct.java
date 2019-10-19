package cn.itcast.domain.cargo;

import cn.itcast.domain.BaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 合同下货物的附件
 * @author
 */
public class ExtCproduct extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;
	/**
	 * 产品号
	 */
	private String productNo;
	/**
	 * 图片
	 */
	private String productImage;
	/**
	 * 产品描述
	 */
	private String productDesc;
	/**
	 * 包装单位   PCS/SETS
	 */
	private String packingUnit;
	/**
	 * 数量
	 */
	private Integer cnumber;
	/**
	 * 单价
	 */
	private BigDecimal price;
	/**
	 * 总金额 　自动计算: 数量x单价
	 */
	private BigDecimal amount;
	/**
	 * 要求
	 */
	private String productRequest;
	/**
	 * 排序号
	 */
	private Integer orderNo;
	private String contractProductId;
	private String factoryId;
	/**
	 * 工厂名
	 */
	private String factoryName;//
	private String contractId;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getPackingUnit() {
		return packingUnit;
	}

	public void setPackingUnit(String packingUnit) {
		this.packingUnit = packingUnit;
	}

	public Integer getCnumber() {
		return cnumber;
	}

	public void setCnumber(Integer cnumber) {
		this.cnumber = cnumber;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getProductRequest() {
		return productRequest;
	}

	public void setProductRequest(String productRequest) {
		this.productRequest = productRequest;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getContractProductId() {
		return contractProductId;
	}

	public void setContractProductId(String contractProductId) {
		this.contractProductId = contractProductId;
	}

	public String getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
}
