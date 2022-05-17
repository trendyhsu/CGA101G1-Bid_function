package com.bidapplylist.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bidapplylist.model.BidApplyListService;
import com.bidapplylist.model.BidApplyListVO;
import com.bidproduct.model.BidProductService;
import com.bidproduct.model.BidProductVO;
import com.gamecompany.model.GameCompanyService;
import com.gamecompany.model.GameCompanyVO;
import com.gameplatformtype.model.GamePlatformTypeService;
import com.gameplatformtype.model.GamePlatformTypeVO;
import com.gametype.model.GameTypeService;
import com.gametype.model.GameTypeVO;

import groovyjarjarantlr4.v4.parse.ANTLRParser.id_return;


@Controller
@RequestMapping("/bidapplylist")
public class BidApplyListController {

	@Autowired
	GamePlatformTypeService gamePlatformTypeSvc;
	@Autowired
	GameCompanyService gameCompanySvc;
	@Autowired
	GameTypeService gameTypeSvc;
	@Autowired
	BidApplyListService bidApplyListSvc;
	@Autowired
	BidProductService bidProductSvc;
	
	// 後台列出所有競標申請單
	@GetMapping("/getAll")
	public String getAll(Model model) {
		List<BidApplyListVO> list = bidApplyListSvc.getAll();
		model.addAttribute("bidApplyListVOs", list);
		return "backend/bid/listAllBidApplyList";
	}

//	@ModelAttribute("bidApplyListVOs")
//	protected List<BidApplyListVO> referenceApplyListVOs(Model model){
//		BidApplyListService bidApplyListService = new BidApplyListService();
//		List<BidApplyListVO> list = bidApplyListService.getAll();
//		return list;
//	}

	// 前台點進新增競標申請單
	@GetMapping("/addBidApplyList")
	public String addBidApplyList(Model model) {

		List<GameCompanyVO> gameCompanyVOs = gameCompanySvc.getAll();
		List<GamePlatformTypeVO> gamePlatformTypeVOs = gamePlatformTypeSvc.getAll();
		List<GameTypeVO> gameTypeVOs = gameTypeSvc.getAll();
		model.addAttribute("gameCompanyVOs", gameCompanyVOs);
		model.addAttribute("gameTypeVOs", gameTypeVOs);
		model.addAttribute("gamePlatformTypeVOs", gamePlatformTypeVOs);

		BidApplyListVO bidApplyListVO = new BidApplyListVO();
		model.addAttribute("bidApplyListVO", bidApplyListVO);

		return "frontend/bid/addbidapplylist";
	}
	
	// 前台新增競標申請單
	@PostMapping("/bidApplyListInsert")
	public String bidApplyListInsert(@Valid BidApplyListVO bidApplyListVO, BindingResult result, Model model) {
		
		/***************************1.接收請求參數 - 輸入格式的錯誤處理******************/
		if (result.hasErrors()) {
			
			List<GameCompanyVO> gameCompanyVOs = gameCompanySvc.getAll();
			List<GamePlatformTypeVO> gamePlatformTypeVOs = gamePlatformTypeSvc.getAll();
			List<GameTypeVO> gameTypeVOs = gameTypeSvc.getAll();
			model.addAttribute("gameCompanyVOs", gameCompanyVOs);
			model.addAttribute("gameTypeVOs", gameTypeVOs);
			model.addAttribute("gamePlatformTypeVOs", gamePlatformTypeVOs);
			
			return "frontend/bid/addbidapplylist";
		}
		
		if(bidApplyListVO.getBidApplyListNo() != null) {
			Integer orderState = new Integer(4);
			BidProductVO bidProductVO = bidProductSvc.getByBidApplyListNo(bidApplyListVO.getBidApplyListNo());
			bidProductSvc.updateOrderState(orderState, bidProductVO.getBidProductNo());
		}

		Integer memNo = 11001;
		/***************************2.開始新增資料***************************************/
		bidApplyListSvc.addBidApplyList(memNo, bidApplyListVO.getBidName(), bidApplyListVO.getBidProdDescription(), 
				bidApplyListVO.getGameCompanyNo(), bidApplyListVO.getGameTypeNo(), bidApplyListVO.getGamePlatformNo(), 
				bidApplyListVO.getInitialPrice(), bidApplyListVO.getBidPriceIncrement(), bidApplyListVO.getUpcNum(), 
				bidApplyListVO.getBidLaunchedTime(), bidApplyListVO.getBidSoldTime(), new Integer(0));
		/***************************3.新增完成,準備轉交(Send the Success view)***********/
		
		List<BidApplyListVO> bidApplyListVOs = bidApplyListSvc.getAllBidApplyListByMemNo(memNo);
		model.addAttribute("bidApplyListVOs", bidApplyListVOs);
		return "frontend/bid/mybidapplylist";
	}
	
	// 申請完後跳轉顯示頁面
	@GetMapping("/myBidApplyList")
	public String myApplyList(Model model) {
		Integer memNo = 11001;
		List<BidApplyListVO> bidApplyListVOs = bidApplyListSvc.getAllBidApplyListByMemNo(memNo);
		model.addAttribute("bidApplyListVOs", bidApplyListVOs);
		
		return "frontend/bid/mybidapplylist";
	}
	
	// 後台所有競標商品申請單按下退貨
	@PostMapping("/bidApplyListReturn")
	public String bidApplyListReturn(@RequestParam("bidApplyListNo") Integer bidApplyListNo, Model model) {
		
		// 設置狀態為已退貨2
		bidApplyListSvc.updateApplyState(bidApplyListNo, new Integer(2));
		model.addAttribute("success","編號" + bidApplyListNo + "商品已退貨");
		List<BidApplyListVO> list = bidApplyListSvc.getAll();
		model.addAttribute("bidApplyListVOs", list);
		return "backend/bid/listAllBidApplyList";
	}

}
