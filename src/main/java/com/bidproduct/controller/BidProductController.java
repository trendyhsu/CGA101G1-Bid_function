package com.bidproduct.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.validation.constraints.NotEmpty;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.bidapplylist.model.BidApplyListService;
import com.bidapplylist.model.BidApplyListVO;
import com.bidpic.model.BidPicService;
import com.bidpic.model.BidPicVO;
import com.bidproduct.model.BidProductService;
import com.bidproduct.model.BidProductVO;
import com.bidrecord.model.BidRecordService;
import com.bidrecord.model.BidRecordVO;
import com.gamecompany.model.GameCompanyService;
import com.gamecompany.model.GameCompanyVO;
import com.gameplatformtype.model.GamePlatformTypeService;
import com.gameplatformtype.model.GamePlatformTypeVO;
import com.gametype.model.GameTypeService;
import com.gametype.model.GameTypeVO;
import com.member.model.MemService;
import com.member.model.MemVO;
import com.product.model.ProductService;
import com.product.model.ProductVO;

@Controller
@Validated
@RequestMapping("/bid")
public class BidProductController {

	@Autowired
	BidProductService bidProductSvc;

	@Autowired
	BidPicService bidPicSvc;

	@Autowired
	BidApplyListService bidApplyListSvc;
	
	@Autowired
	BidRecordService bidRecordSvc;

	@Autowired
	GamePlatformTypeService gamePlatformTypeSvc;

	@Autowired
	GameCompanyService gameCompanySvc;

	@Autowired
	GameTypeService gameTypeSvc;

	// ????????????????????????
	@GetMapping("/listAllBid")
	public String listAllBid() {
		return "frontend/bid/listallbid";
	}

	// ??????????????????
	@GetMapping("/listOneBid")
	public String listOneBid(@RequestParam("bidProductNo") String bidProductNo) {

		return "frontend/bid/listonebid";
	}

	// ??????????????????list API
	@RequestMapping("/bidProduct")
	@ResponseBody
	public List<BidProductVO> bidProduct() {
		BidProductService bidProductSvc = new BidProductService();
		List<BidProductVO> list = bidProductSvc.getAll();
		return list;
	}

	// ????????????Game API
	@RequestMapping("/bidProductGetGame")
	@ResponseBody
	public String bidProductGetGame() {
		// ????????????????????????
		GameTypeService gameTypeSvc = new GameTypeService();
		List<GameTypeVO> gameTypeVOs = gameTypeSvc.getAll();
		JSONArray jsonObject1 = new JSONArray(gameTypeVOs);

		// ????????????????????????
		GameCompanyService gameCompanySvc = new GameCompanyService();
		List<GameCompanyVO> gameCompanyVOs = gameCompanySvc.getAll();
		JSONArray jsonObject2 = new JSONArray(gameCompanyVOs);

		// ????????????????????????
		GamePlatformTypeService gamePlatformTypeSvc = new GamePlatformTypeService();
		List<GamePlatformTypeVO> gamePlatformTypeVOs = gamePlatformTypeSvc.getAll();
		JSONArray jsonObject3 = new JSONArray(gamePlatformTypeVOs);

		// ??????JSONObject?????? ??????????????????
		JSONObject gameAll = new JSONObject();
		gameAll.put("gameTypeVOs", jsonObject1);
		gameAll.put("gameCompanyVOs", jsonObject2);
		gameAll.put("gamePlatformTypeVOs", jsonObject3);
		return gameAll.toString();
	}

	// ?????????????????? API
	@RequestMapping("/bidProductSearch")
	@ResponseBody
	public List<BidProductVO> bidProductSearch(@RequestParam("keyword") String keyword) {
		BidProductService bidProductSvc = new BidProductService();
		List<BidProductVO> list = bidProductSvc.getAllByBidName(keyword);
		if (list.size() == 0) {
			list = bidProductSvc.getAll();
		}
		return list;
	}

	// ???????????????????????? API
	@RequestMapping("/bidProductGameSearch")
	@ResponseBody
	public List<BidProductVO> bidProductGameSearch(@RequestParam("keyword") String keyword) {
		BidProductService bidProductSvc = new BidProductService();
		BidApplyListService bidApplyListSvc = new BidApplyListService();

		// ????????????????????????BidProductVO
		List<BidProductVO> list = new ArrayList<BidProductVO>();

		// ?????????????????????????????????????????????????????????
		if (keyword.startsWith("64")) {
			List<BidApplyListVO> bidApplyListVOs = bidApplyListSvc
					.getAllBidApplyListByGamePlatformNo(Integer.valueOf(keyword));
			for (BidApplyListVO bidApplyListVO : bidApplyListVOs) {
				BidProductVO bidProductVO = bidProductSvc.getByBidApplyListNo(bidApplyListVO.getBidApplyListNo());
				if (bidProductVO.getBidProductNo() != null) {
					list.add(bidProductVO);
				}

			}

		} else if (keyword.startsWith("63")) {
			List<BidApplyListVO> bidApplyListVOs = bidApplyListSvc
					.getAllBidApplyListByGameTypeNo(Integer.valueOf(keyword));
			for (BidApplyListVO bidApplyListVO : bidApplyListVOs) {
				BidProductVO bidProductVO = bidProductSvc.getByBidApplyListNo(bidApplyListVO.getBidApplyListNo());
				if (bidProductVO.getBidProductNo() != null) {
					list.add(bidProductVO);
				}
			}

		} else {
			List<BidApplyListVO> bidApplyListVOs = bidApplyListSvc
					.getAllBidApplyListByGameCompanyNo(Integer.valueOf(keyword));
			for (BidApplyListVO bidApplyListVO : bidApplyListVOs) {
				BidProductVO bidProductVO = bidProductSvc.getByBidApplyListNo(bidApplyListVO.getBidApplyListNo());
				if (bidProductVO.getBidProductNo() != null) {
					list.add(bidProductVO);
				}
			}
		}

		if (list.size() == 0) {
			list = bidProductSvc.getAll();
			// ?????????????
		}
		return list;
	}

	// ??????????????????????????????
	@RequestMapping("/bidProductGetOne")
	@ResponseBody
	public String bidProductGetOne(@RequestParam("bidProductNo") Integer bidProductNo) {

		// ?????????????????????VO
		BidProductService bidProductSvc = new BidProductService();
		BidProductVO bidProductVO = bidProductSvc.getOneBid(bidProductNo);
		JSONObject jsonObject1 = new JSONObject(bidProductVO);

		// ???????????????????????????
		BidRecordService bidRecordSvc = new BidRecordService();
		BidRecordVO bidRecordVO = bidRecordSvc.getHighestByBidProductNo(bidProductNo);

		// ??????????????????VO
		ProductService productSvc = new ProductService();
		ProductVO productVO = productSvc.GetOne(bidProductVO.getProductNo());

		// ??????????????????VO
		GamePlatformTypeVO gamePlatformTypeVO = bidProductVO.getBidApplyListVO().getGamePlatformTypeVO();
		JSONObject jsonObject4 = new JSONObject(gamePlatformTypeVO);

		// ??????????????????VO
		GameTypeVO gameTypeVO = bidProductVO.getBidApplyListVO().getGameTypeVO();
		JSONObject jsonObject5 = new JSONObject(gameTypeVO);

		// ??????????????????VO
		GameCompanyVO gameCompanyVO = bidProductVO.getBidApplyListVO().getGameCompanyVO();
		JSONObject jsonObject6 = new JSONObject(gameCompanyVO);

		// ??????JSONObject?????? ??????????????????
		JSONObject bidAll = new JSONObject();

		bidAll.put("bidProductVO", jsonObject1);

		if (bidRecordVO != null) {
			JSONObject jsonObject2 = new JSONObject(bidRecordVO);
			bidAll.put("bidRecordVO", jsonObject2);
		}
		if (productVO != null) {
			JSONObject jsonObject3 = new JSONObject(productVO);
			bidAll.put("productVO", jsonObject3);
		}
		bidAll.put("gamePlatformTypeVO", jsonObject4);
		bidAll.put("gameTypeVO", jsonObject5);
		bidAll.put("gameCompanyVO", jsonObject6);

		return bidAll.toString();
	}

	// ???????????????????????????????????? mybid
	@GetMapping("/myBid")
	public String myBid(Model model) {

		Integer memNo = 11002;

		List<BidRecordVO> bidRecordVOs = bidRecordSvc.getByMemNo(memNo);

		Set<Integer> bidProductNoSet = new TreeSet<Integer>();

		// ?????? set ????????????
		for (BidRecordVO bidRecordVO : bidRecordVOs) {
			bidProductNoSet.add(bidRecordVO.getBidProductNo());
		}

		Set<Integer> bidProductNoSet2 = new TreeSet<Integer>();
		for (BidRecordVO bidRecordVO : bidRecordVOs) {
			bidProductNoSet2.add(bidRecordVO.getBidProductNo());
		}

		BidProductService bidProductSvc = new BidProductService();

		//
		Iterator<Integer> iterator = bidProductNoSet.iterator();

		// ?????????????????? bidProductVO
		List<BidProductVO> bidProductVOs = new ArrayList<BidProductVO>();
		List<BidRecordVO> bidRecordVOByProductNos = new ArrayList<BidRecordVO>();

		while (iterator.hasNext()) {
			BidProductVO bidProductVO = bidProductSvc.getOneBid(iterator.next());
			bidProductVOs.add(bidProductVO);

		}

		Iterator<Integer> iterator2 = bidProductNoSet2.iterator();

		while (iterator2.hasNext()) {

			BidRecordVO bidRecordVO = bidRecordSvc.getHighestByBidProductNo(iterator2.next());

			if (bidRecordVO != null) {
				bidRecordVOByProductNos.add(bidRecordVO);

			}
		}
		// ???????????????VO?????????model???
		model.addAttribute("bidProductVOs", bidProductVOs);
		model.addAttribute("bidRecordVOByProductNos", bidRecordVOByProductNos);
		return "frontend/bid/mybid";
	}

	// ???????????????????????????????????? mybidorder
	@GetMapping("/myBidOrder")
	public String myBidOrder(Model model) {

		Integer memNo = 11003;

		List<BidProductVO> bidProductVOs = bidProductSvc.getAllByBuyNo(memNo);

		model.addAttribute("bidProductVOs", bidProductVOs);

		return "frontend/bid/mybidorder";
	}

	// mybidorder??????checkout??????
	@PostMapping("/bidCheckout")
	public String bidCheckout(@RequestParam("bidProductNo") Integer bidProductNo, Model model) {
		// ?????????????????????
		BidProductVO bidProductVO = bidProductSvc.getOneBid(bidProductNo);

		// ?????????????????????????????????
		List<BidPicVO> bidPicVOs = bidPicSvc.getAllBidPicByBidProductNo(bidProductNo);

		model.addAttribute("bidProductVO", bidProductVO);
		model.addAttribute("bidPicVOs", bidPicVOs);

		// ??????????????????
		Integer memNo = 11003;
		MemService memSvc = new MemService();
		MemVO memVO = memSvc.getMemVObyMemNo(memNo);
		model.addAttribute("memVO", memVO);

		return "frontend/bid/bidcheckout";

	}

	// checkout??????????????????
	@PostMapping("/bidProductCheckout")
	public String bidProductCheckout(@RequestParam("bidProductNo") Integer bidProductNo,
			@RequestParam("receiverName") String receiverName,
			@RequestParam("receiverAddressCity") String receiverAddressCity,
			@RequestParam("receiverAddressDist") String receiverAddressDist,
			@RequestParam("receiverAddressDetail") String receiverAddressDetail,
			@RequestParam("receiverPhone") String receiverPhone, @RequestParam("creditcardNum") String creditcardNum,
			Model model) {
		/*************************** 1.?????????????????? - ??????????????????????????? ******************/

		/*************************** 2.?????????????????? ***************************************/
		// ????????????
		receiverAddressDetail = receiverAddressCity + receiverAddressDist + receiverAddressDetail;
		// ??????orderState???1 ???????????????
		Integer orderState = new Integer(1);
		// ??????????????????????????????
		bidProductSvc.updateReceiverAndOrderState(orderState, receiverName, receiverAddressDetail, receiverPhone,
				bidProductNo);
		/*************************** 3.????????????,????????????(Send the Success view) ***********/
		Integer memNo = 11002;
		System.out.println(receiverAddressDetail);
		List<BidProductVO> bidProductVOs = bidProductSvc.getAllByBuyNo(memNo);

		model.addAttribute("bidProductVOs", bidProductVOs);
		model.addAttribute("success", "???????????????????????????????????????");

		return "frontend/bid/mybidorder";
	}

	// mybidorder????????????
	@PostMapping("/bidProductReceive")
	public String bidProductReceive(@RequestParam("bidProductNo") Integer bidProductNo, Model model) {

		// ?????????????????????????????? 5 ?????????
		Integer orderState = new Integer(5);
		bidProductSvc.updateOrderState(orderState, bidProductNo);

		Integer memNo = 11002;
		List<BidProductVO> bidProductVOs = bidProductSvc.getAllByBuyNo(memNo);

		model.addAttribute("bidProductVOs", bidProductVOs);
		model.addAttribute("success", "?????? " + bidProductNo + " ??????????????????");

		return "frontend/bid/mybidorder";
	}

	// ??????????????????????????????????????? mysellingbid
	@GetMapping("/mySellingBid")
	public String mySellingBid(Model model) {

		Integer memNo = 11001;

		List<BidProductVO> bidProductVOs = bidProductSvc.getAllBySellerNo(memNo);

		model.addAttribute("bidProductVOs", bidProductVOs);

		return "frontend/bid/mysellingbid";
	}

	// mysellingbid ?????? ??????
	@PostMapping("/bidProductGetBack")
	public String bidProductGetBack(@RequestParam("bidProductNo") Integer bidProductNo, Model model) {

		// ?????????????????????????????? 3 ???????????????
		Integer orderState = new Integer(3);
		bidProductSvc.updateOrderState(orderState, bidProductNo);

		Integer memNo = 11001;
		List<BidProductVO> bidProductVOs = bidProductSvc.getAllByBuyNo(memNo);

		model.addAttribute("bidProductVOs", bidProductVOs);
		model.addAttribute("success", "??????" + bidProductNo + "???????????????????????????");

		return "frontend/bid/mysellingbid";
	}

	// mysellingbid ?????? ????????????
	@PostMapping("/bidProductRelist")
	public String bidProductRelist(@RequestParam("bidProductNo") Integer bidProductNo, Model model) {
		// ?????????????????????????????????
//		Integer orderState = new Integer(4);
//		bidProductSvc.updateOrderState(orderState, bidProductNo);

		// ??????????????????applylistvo ??????attribute
		BidProductVO bidProductVO = bidProductSvc.getOneBid(bidProductNo);
		BidApplyListVO bidApplyListVO = bidApplyListSvc.getOneBidApplyList(bidProductVO.getBidApplyListNo());
		model.addAttribute("bidApplyListVO", bidApplyListVO);

		// ?????????????????????gamelist
		List<GameCompanyVO> gameCompanyVOs = gameCompanySvc.getAll();
		List<GamePlatformTypeVO> gamePlatformTypeVOs = gamePlatformTypeSvc.getAll();
		List<GameTypeVO> gameTypeVOs = gameTypeSvc.getAll();
		model.addAttribute("gameCompanyVOs", gameCompanyVOs);
		model.addAttribute("gameTypeVOs", gameTypeVOs);
		model.addAttribute("gamePlatformTypeVOs", gamePlatformTypeVOs);

		return "frontend/bid/addbidapplylist";
	}

	// ?????????bidapplylist??????????????????????????????
	@PostMapping("/addBid")
	public String addBid(@RequestParam("bidApplyListNo") String bidApplyListNo, Model model) {

		// ??????bidapplylistvo??????
		BidApplyListService bidApplyListSvc = new BidApplyListService();
		BidApplyListVO bidApplyListVO = bidApplyListSvc.getOneBidApplyList(Integer.valueOf(bidApplyListNo));
		model.addAttribute("bidApplyListVO", bidApplyListVO);

		// ???????????????????????????????????????????????? ??????select
		ProductService productSvc = new ProductService();
		List<ProductVO> productList = productSvc.GetAllProducts();
		model.addAttribute("productList", productList);

		return "backend/bid/addBid";
	}

	@PostMapping("/bidProductInsert")
	public String bidProductInsert(@RequestParam("bidApplyListNo") String bidApplyListNo,
			@RequestParam("productNo") String productNo,
			@NotEmpty(message = "????????????????????????") @RequestParam("bidName") String bidName,
			@RequestParam("bidProdDescription") String bidProdDescription, @RequestParam("sellerNo") String sellerNo,
			@RequestParam("initialPrice") String initialPrice,
			@RequestParam("bidPriceIncrement") String bidPriceIncrement,
			@RequestParam("bidLaunchedTime") String bidLaunchedTime, @RequestParam("bidSoldTime") String bidSoldTime,
			@RequestPart("upfile") MultipartFile[] files, Model model) {

		// ????????????
		if (files.length == 0) {
//			errorMsgs.add("??????????????????");
		}

		// ????????????
		Integer bidProductNo = bidProductSvc.addBidProduct(Integer.valueOf(bidApplyListNo), Integer.valueOf(productNo),
				bidName, bidProdDescription, Integer.valueOf(sellerNo), Integer.valueOf(initialPrice), new Integer(0),
				Timestamp.valueOf(bidLaunchedTime), Timestamp.valueOf(bidSoldTime), Integer.valueOf(bidPriceIncrement),
				new Integer(0));
		for (MultipartFile multipartFile : files) {
			byte[] img;

			try {
				img = multipartFile.getBytes();
				bidPicSvc.addBidPic(bidProductNo, img);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		// ??????????????????????????????????????????(1)
		bidApplyListSvc.updateApplyState(Integer.valueOf(bidApplyListNo), new Integer(1));

		System.out.println("ok");

		// ???????????? VOs ??????
		List<BidProductVO> bidProductVOs = bidProductSvc.getAll();
		model.addAttribute("bidProductVOs", bidProductVOs);
		return "backend/bid/listAllBid";
	}

	// ??????????????????????????????
	@GetMapping("/listAllBidProduct")
	public String listAllBidProduct(Model model) {
		List<BidProductVO> bidProductVOs = bidProductSvc.getAll();
		model.addAttribute("bidProductVOs", bidProductVOs);
		return "backend/bid/listAllBid";
	}

	// ????????????????????????
	@PostMapping("/bidProductBackendSearch")
	public String bidProductBackendSearch(WebRequest webRequest, Model model) {
		Map<String, String[]> map = webRequest.getParameterMap();
		List<BidProductVO> bidProductVOs = bidProductSvc.getAll(map);
		model.addAttribute("bidProductVOs", bidProductVOs);
		return "backend/bid/listSearchBid";
	}

	// ????????????????????????
	@PostMapping("/bidProductEdit")
	public String bidProductEdit(@RequestParam("bidProductNo") Integer bidProductNo, Model model) {

		// ?????????????????????
		BidProductVO bidProductVO = bidProductSvc.getOneBid(bidProductNo);
		model.addAttribute("bidProductVO", bidProductVO);

		// ?????????????????????
		List<BidPicVO> bidPicVOs = bidPicSvc.getAllBidPicByBidProductNo(bidProductNo);
		model.addAttribute("bidPicVOs", bidPicVOs);

		// ????????????????????????VO
		ProductService productSvc = new ProductService();
		List<ProductVO> productVOs = productSvc.GetAllProducts();
		model.addAttribute("productVOs", productVOs);

		return "backend/bid/editBid";
	}

	// ??????????????????????????????????????????
	@PostMapping("/bidProductEditUpdate")
	public String bidProductEditUpdate(Model model) {

		return "backend/bid/editBid";
	}

	// ??????????????????????????????
	@GetMapping("/listAllBidOrder")
	public String listAllBidOrder(Model model) {
		List<BidProductVO> bidProductVOs = bidProductSvc.getAll();
		model.addAttribute("bidProductVOs", bidProductVOs);
		return "backend/bid/listAllBidOrder";
	}

	// ??????????????????????????????
	@PostMapping("/bidProductShipping")
	public String bidProductShipping(@RequestParam("bidProductNo") Integer bidProductNo, Model model) {
		// ?????????????????????????????? 2 ?????????
		Integer orderState = new Integer(2);
		bidProductSvc.updateOrderState(orderState, bidProductNo);
		model.addAttribute("success", "?????? " + bidProductNo + " ??????????????????");
		List<BidProductVO> bidProductVOs = bidProductSvc.getAll();
		model.addAttribute("bidProductVOs", bidProductVOs);
		return "backend/bid/listAllBidOrder";
	}

	// ??????????????????????????????
	@PostMapping("bidProductPay")
	public String bidProductPay(@RequestParam("bidProductNo") Integer bidProductNo, Model model) {
		// ?????????????????????????????? 6 ?????????
		Integer orderState = new Integer(6);
		bidProductSvc.updateOrderState(orderState, bidProductNo);
		BidProductVO bidProductVO = bidProductSvc.getOneBid(bidProductNo);
		MemService memService = new MemService();
		MemVO memVO = memService.getMemVObyMemNo(bidProductVO.getSellerNo());
		model.addAttribute("success", "?????? " + bidProductNo + " ???????????????????????? " + memVO.getMemName() + " ???????????? ");
		List<BidProductVO> bidProductVOs = bidProductSvc.getAll();
		model.addAttribute("bidProductVOs", bidProductVOs);
		return "backend/bid/listAllBidOrder";
	}

}
