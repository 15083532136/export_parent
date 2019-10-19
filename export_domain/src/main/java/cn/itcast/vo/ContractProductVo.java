package cn.itcast.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/18 8:21
 * @description：出货表
 * @modified By：
 * @version: 1.0$
 */
public class ContractProductVo implements Serializable {

    private String customName;		//客户名称
    private String contractNo;		//合同号，订单号
    private String productNo;		//货号
    private Integer cnumber;		//数量
    private String factoryName;		//厂家名称，冗余字段
    private Date deliveryPeriod;	//交货期限
    private Date shipTime;			//船期
    private String tradeTerms;		//贸易条款

    public String getCustomName() {
        return customName;
    }

    public ContractProductVo setCustomName(String customName) {
        this.customName = customName;
        return this;
    }

    public String getContractNo() {
        return contractNo;
    }

    public ContractProductVo setContractNo(String contractNo) {
        this.contractNo = contractNo;
        return this;
    }

    public String getProductNo() {
        return productNo;
    }

    public ContractProductVo setProductNo(String productNo) {
        this.productNo = productNo;
        return this;
    }

    public Integer getCnumber() {
        return cnumber;
    }

    public ContractProductVo setCnumber(Integer cnumber) {
        this.cnumber = cnumber;
        return this;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public ContractProductVo setFactoryName(String factoryName) {
        this.factoryName = factoryName;
        return this;
    }

    public Date getDeliveryPeriod() {
        return deliveryPeriod;
    }

    public ContractProductVo setDeliveryPeriod(Date deliveryPeriod) {
        this.deliveryPeriod = deliveryPeriod;
        return this;
    }

    public Date getShipTime() {
        return shipTime;
    }

    public ContractProductVo setShipTime(Date shipTime) {
        this.shipTime = shipTime;
        return this;
    }

    public String getTradeTerms() {
        return tradeTerms;
    }

    public ContractProductVo setTradeTerms(String tradeTerms) {
        this.tradeTerms = tradeTerms;
        return this;
    }
}
