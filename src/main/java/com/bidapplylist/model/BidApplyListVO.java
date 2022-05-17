package com.bidapplylist.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import com.gamecompany.model.GameCompanyService;
import com.gamecompany.model.GameCompanyVO;
import com.gameplatformtype.model.GamePlatformTypeService;
import com.gameplatformtype.model.GamePlatformTypeVO;
import com.gametype.model.GameTypeService;
import com.gametype.model.GameTypeVO;
import com.member.model.MemService;
import com.member.model.MemVO;

@Entity
@Table(name = "bidapplylist")
public class BidApplyListVO implements Serializable {
	@Transient
	private static final long serialVersionUID = 1L;
	// 建立承接 bidapplylist 表格的 Value Object
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer bidApplyListNo;
	Integer memNo;
	String bidName;
	String bidProdDescription;
	Integer gameCompanyNo;
	Integer gameTypeNo;
	Integer gamePlatformNo;
	Integer initialPrice;
	Integer bidPriceIncrement;
	String upcNum;
	Timestamp bidLaunchedTime;
	Timestamp bidSoldTime;
	Integer applyState;

	public BidApplyListVO() {

	}

	public Integer getBidApplyListNo() {
		return bidApplyListNo;
	}

	public void setBidApplyListNo(Integer bidApplyListNo) {
		this.bidApplyListNo = bidApplyListNo;
	}

	public Integer getMemNo() {
		return memNo;
	}

	public void setMemNo(Integer memNo) {
		this.memNo = memNo;
	}

	@NotEmpty(message = "商品名稱: 請勿空白")
	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_:)]{1,30}$", message = "商品名稱: 只能包含中文、英文大小寫、數字和底線及冒號 , 且長度須在1到30之間")
	public String getBidName() {
		return bidName;
	}

	public void setBidName(String bidName) {
		this.bidName = bidName;
	}

	@NotEmpty(message = "商品敘述: 請勿空白")
	public String getBidProdDescription() {
		return bidProdDescription;
	}

	public void setBidProdDescription(String bidProdDescription) {
		this.bidProdDescription = bidProdDescription;
	}

	@PositiveOrZero(message = "遊戲公司請填數字")
	public Integer getGameCompanyNo() {
		return gameCompanyNo;
	}

	public void setGameCompanyNo(Integer gameCompanyNo) {
		this.gameCompanyNo = gameCompanyNo;
	}

	@PositiveOrZero(message = "遊戲類型請填數字")
	public Integer getGameTypeNo() {
		return gameTypeNo;
	}

	public void setGameTypeNo(Integer gameTypeNo) {
		this.gameTypeNo = gameTypeNo;
	}

	@PositiveOrZero(message = "遊戲平台請填數字")
	public Integer getGamePlatformNo() {
		return gamePlatformNo;
	}

	public void setGamePlatformNo(Integer gamePlatformNo) {
		this.gamePlatformNo = gamePlatformNo;
	}

	@NotNull(message = "起標價: 請勿空白")
	@Min(value = 0, message = "起標價: 不能小於{value}")
	@Max(value = 100000, message = "起標價: 不能大於{value}")
	public Integer getInitialPrice() {
		return initialPrice;
	}

	public void setInitialPrice(Integer initialPrice) {
		this.initialPrice = initialPrice;
	}

	@NotNull(message = "增額價: 請勿空白")
	@Min(value = 0, message = "增額價: 不能小於{value}")
	@Max(value = 100000, message = "增額價: 不能大於{value}")
	public Integer getBidPriceIncrement() {
		return bidPriceIncrement;
	}

	public void setBidPriceIncrement(Integer bidPriceIncrement) {
		this.bidPriceIncrement = bidPriceIncrement;
	}

	@NotEmpty(message = "UPC編號: 請勿空白")
	public String getUpcNum() {
		return upcNum;
	}

	public void setUpcNum(String upcNum) {
		this.upcNum = upcNum;
	}

	@NotNull(message = "起標時間: 請勿空白")
	@Future(message = "日期必須是在今日(不含)之後")
	public Timestamp getBidLaunchedTime() {
		return bidLaunchedTime;
	}

	public void setBidLaunchedTime(Timestamp bidLaunchedTime) {
		this.bidLaunchedTime = bidLaunchedTime;
	}

	@NotNull(message = "截標時間: 請勿空白")
	@Future(message = "日期必須是在今日(不含)之後")
	public Timestamp getBidSoldTime() {
		return bidSoldTime;
	}

	public void setBidSoldTime(Timestamp bidSoldTime) {
		this.bidSoldTime = bidSoldTime;
	}

	public Integer getApplyState() {
		return applyState;
	}

	public void setApplyState(Integer applyState) {
		this.applyState = applyState;
	}

	public com.gameplatformtype.model.GamePlatformTypeVO getGamePlatformTypeVO() {
		com.gameplatformtype.model.GamePlatformTypeService gamePlatformTypeSvc = new GamePlatformTypeService();
		GamePlatformTypeVO gamePlatformTypeVO = gamePlatformTypeSvc.getOneGamePlatformType(gamePlatformNo);
		return gamePlatformTypeVO;
	}

	public com.gametype.model.GameTypeVO getGameTypeVO() {
		com.gametype.model.GameTypeService gameTypeSvc = new GameTypeService();
		GameTypeVO gameTypeVO = gameTypeSvc.getOneGameType(gameTypeNo);
		return gameTypeVO;
	}

	public com.gamecompany.model.GameCompanyVO getGameCompanyVO() {
		com.gamecompany.model.GameCompanyService gameCompanySvc = new GameCompanyService();
		GameCompanyVO gameCompanyVO = gameCompanySvc.getOneGameCompanyVO(gameCompanyNo);
		return gameCompanyVO;
	}

	public com.member.model.MemVO getMemVO() {
		com.member.model.MemService memSvc = new MemService();
		MemVO memVO = memSvc.getMemVObyMemNo(memNo);
		return memVO;
	}
}
