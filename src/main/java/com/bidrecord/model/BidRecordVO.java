package com.bidrecord.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.member.model.MemService;
import com.member.model.MemVO;

@Entity
@Table(name = "bidrecord")
public class BidRecordVO implements Serializable {
	@Transient
	private static final long serialVersionUID = 1L;
	// 建立承接 bidrecord 表格的 Value Object
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer bidRecordNo;
	Integer bidProductNo;
	Integer memNo;
	Integer bidPrice;
	Timestamp bidTime;

	public BidRecordVO() {

	}

	public Integer getBidRecordNo() {
		return bidRecordNo;
	}

	public void setBidRecordNo(Integer bidRecordNo) {
		this.bidRecordNo = bidRecordNo;
	}

	public Integer getBidProductNo() {
		return bidProductNo;
	}

	public void setBidProductNo(Integer bidProductNo) {
		this.bidProductNo = bidProductNo;
	}

	public Integer getMemNo() {
		return memNo;
	}

	public void setMemNo(Integer memNo) {
		this.memNo = memNo;
	}

	public Integer getBidPrice() {
		return bidPrice;
	}

	public void setBidPrice(Integer bidPrice) {
		this.bidPrice = bidPrice;
	}

	public Timestamp getBidTime() {
		return bidTime;
	}

	public void setBidTime(Timestamp bidTime) {
		this.bidTime = bidTime;
	}

	public com.member.model.MemVO getMemVO() {
		com.member.model.MemService memSvc = new MemService();
		MemVO memVO = memSvc.getMemVObyMemNo(memNo);
		return memVO;
	}

}
