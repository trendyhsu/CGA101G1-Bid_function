package com.bidrecord.model;

import java.util.List;

public interface BidRecordDAO_interface {
	// BidRecordDAO_interface 定義介面
	public void insert(BidRecordVO bidRecordVO);
	public void update(BidRecordVO bidRecordVO);
	public void delete(Integer bidRecordNo);
	public BidRecordVO findByPrimaryKey(Integer bidRecordNo);
	public List<BidRecordVO> getAll();
	// 使用 BidProductNo 取得所有 BidProductNo 的出價紀錄
	public List<BidRecordVO> findByBidProductNo(Integer bidProductNo);
	// 使用 BidProductNo 取得該商品最高出價
	public BidRecordVO findByBidProductNoHighestPrice(Integer bidProductNo);
	// 使用 memNo 取得該會員的所有出價紀錄
	public List<BidRecordVO> findByMemNo(Integer memNo);

}
