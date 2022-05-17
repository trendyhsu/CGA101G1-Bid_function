package com.bidpic.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "bidpic")
public class BidPicVO implements Serializable {
	@Transient
	private static final long serialVersionUID = 1L;
	// 建立承接 bidpic 表格的 Value Object
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bidProdPicNo;
	private Integer bidProductNo;
	private byte[] bidProdPicContent;

	public BidPicVO() {

	}

	public Integer getBidProdPicNo() {
		return bidProdPicNo;
	}

	public void setBidProdPicNo(Integer bidProdPicNo) {
		this.bidProdPicNo = bidProdPicNo;
	}

	public Integer getBidProductNo() {
		return bidProductNo;
	}

	public void setBidProductNo(Integer bidProductNo) {
		this.bidProductNo = bidProductNo;
	}

	public byte[] getBidProdPicContent() {
		return bidProdPicContent;
	}

	public void setBidProdPicContent(byte[] bidProdPicContent) {
		this.bidProdPicContent = bidProdPicContent;
	}

}
