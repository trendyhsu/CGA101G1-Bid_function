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

	// 首頁全部競標商品
	@GetMapping("/listAllBid")
	public String listAllBid() {
		return "frontend/bid/listallbid";
	}

	// 首頁單一商品
	@GetMapping("/listOneBid")
	public String listOneBid(@RequestParam("bidProductNo") String bidProductNo) {

		return "frontend/bid/listonebid";
	}

	// 獲取全部商品list API
	@RequestMapping("/bidProduct")
	@ResponseBody
	public List<BidProductVO> bidProduct() {
		BidProductService bidProductSvc = new BidProductService();
		List<BidProductVO> list = bidProductSvc.getAll();
		return list;
	}

	// 獲取全部Game API
	@RequestMapping("/bidProductGetGame")
	@ResponseBody
	public String bidProductGetGame() {
		// 取得所有遊戲類別
		GameTypeService gameTypeSvc = new GameTypeService();
		List<GameTypeVO> gameTypeVOs = gameTypeSvc.getAll();
		JSONArray jsonObject1 = new JSONArray(gameTypeVOs);

		// 取得所有遊戲公司
		GameCompanyService gameCompanySvc = new GameCompanyService();
		List<GameCompanyVO> gameCompanyVOs = gameCompanySvc.getAll();
		JSONArray jsonObject2 = new JSONArray(gameCompanyVOs);

		// 取得所有遊戲平台
		GamePlatformTypeService gamePlatformTypeSvc = new GamePlatformTypeService();
		List<GamePlatformTypeVO> gamePlatformTypeVOs = gamePlatformTypeSvc.getAll();
		JSONArray jsonObject3 = new JSONArray(gamePlatformTypeVOs);

		// 創建JSONObject物件 裝入上述物件
		JSONObject gameAll = new JSONObject();
		gameAll.put("gameTypeVOs", jsonObject1);
		gameAll.put("gameCompanyVOs", jsonObject2);
		gameAll.put("gamePlatformTypeVOs", jsonObject3);
		return gameAll.toString();
	}

	// 首頁搜尋功能 API
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

	// 首頁遊戲搜尋功能 API
	@RequestMapping("/bidProductGameSearch")
	@ResponseBody
	public List<BidProductVO> bidProductGameSearch(@RequestParam("keyword") String keyword) {
		BidProductService bidProductSvc = new BidProductService();
		BidApplyListService bidApplyListSvc = new BidApplyListService();

		// 用來裝整理過後的BidProductVO
		List<BidProductVO> list = new ArrayList<BidProductVO>();

		// 過濾是遊戲分類還是遊戲公司還是遊戲平台
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
			// 查無資料?
		}
		return list;
	}

	// 單一商品獲取全部資訊
	@RequestMapping("/bidProductGetOne")
	@ResponseBody
	public String bidProductGetOne(@RequestParam("bidProductNo") Integer bidProductNo) {

		// 取得該競標商品VO
		BidProductService bidProductSvc = new BidProductService();
		BidProductVO bidProductVO = bidProductSvc.getOneBid(bidProductNo);
		JSONObject jsonObject1 = new JSONObject(bidProductVO);

		// 取得該商品最高出價
		BidRecordService bidRecordSvc = new BidRecordService();
		BidRecordVO bidRecordVO = bidRecordSvc.getHighestByBidProductNo(bidProductNo);

		// 取得一手商品VO
		ProductService productSvc = new ProductService();
		ProductVO productVO = productSvc.GetOne(bidProductVO.getProductNo());

		// 取得遊戲平台VO
		GamePlatformTypeVO gamePlatformTypeVO = bidProductVO.getBidApplyListVO().getGamePlatformTypeVO();
		JSONObject jsonObject4 = new JSONObject(gamePlatformTypeVO);

		// 取得遊戲類別VO
		GameTypeVO gameTypeVO = bidProductVO.getBidApplyListVO().getGameTypeVO();
		JSONObject jsonObject5 = new JSONObject(gameTypeVO);

		// 取得遊戲公司VO
		GameCompanyVO gameCompanyVO = bidProductVO.getBidApplyListVO().getGameCompanyVO();
		JSONObject jsonObject6 = new JSONObject(gameCompanyVO);

		// 創建JSONObject物件 裝入上述物件
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

	// 會員中心點下出價商品追蹤 mybid
	@GetMapping("/myBid")
	public String myBid(Model model) {

		Integer memNo = 11002;

		List<BidRecordVO> bidRecordVOs = bidRecordSvc.getByMemNo(memNo);

		Set<Integer> bidProductNoSet = new TreeSet<Integer>();

		// 使用 set 去除重複
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

		// 放置過濾完的 bidProductVO
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
		// 將過濾好的VO放置於model內
		model.addAttribute("bidProductVOs", bidProductVOs);
		model.addAttribute("bidRecordVOByProductNos", bidRecordVOByProductNos);
		return "frontend/bid/mybid";
	}

	// 會員中心點下得標商品查詢 mybidorder
	@GetMapping("/myBidOrder")
	public String myBidOrder(Model model) {

		Integer memNo = 11003;

		List<BidProductVO> bidProductVOs = bidProductSvc.getAllByBuyNo(memNo);

		model.addAttribute("bidProductVOs", bidProductVOs);

		return "frontend/bid/mybidorder";
	}

	// mybidorder按下checkout結帳
	@PostMapping("/bidCheckout")
	public String bidCheckout(@RequestParam("bidProductNo") Integer bidProductNo, Model model) {
		// 取得該商品資訊
		BidProductVO bidProductVO = bidProductSvc.getOneBid(bidProductNo);

		// 取得該商品所有的商品圖
		List<BidPicVO> bidPicVOs = bidPicSvc.getAllBidPicByBidProductNo(bidProductNo);

		model.addAttribute("bidProductVO", bidProductVO);
		model.addAttribute("bidPicVOs", bidPicVOs);

		// 取得會員資訊
		Integer memNo = 11003;
		MemService memSvc = new MemService();
		MemVO memVO = memSvc.getMemVObyMemNo(memNo);
		model.addAttribute("memVO", memVO);

		return "frontend/bid/bidcheckout";

	}

	// checkout頁面按下結帳
	@PostMapping("/bidProductCheckout")
	public String bidProductCheckout(@RequestParam("bidProductNo") Integer bidProductNo,
			@RequestParam("receiverName") String receiverName,
			@RequestParam("receiverAddressCity") String receiverAddressCity,
			@RequestParam("receiverAddressDist") String receiverAddressDist,
			@RequestParam("receiverAddressDetail") String receiverAddressDetail,
			@RequestParam("receiverPhone") String receiverPhone, @RequestParam("creditcardNum") String creditcardNum,
			Model model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ******************/

		/*************************** 2.開始新增資料 ***************************************/
		// 組合地址
		receiverAddressDetail = receiverAddressCity + receiverAddressDist + receiverAddressDetail;
		// 定義orderState為1 訂單處理中
		Integer orderState = new Integer(1);
		// 開始修改競標訂單狀態
		bidProductSvc.updateReceiverAndOrderState(orderState, receiverName, receiverAddressDetail, receiverPhone,
				bidProductNo);
		/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
		Integer memNo = 11002;
		System.out.println(receiverAddressDetail);
		List<BidProductVO> bidProductVOs = bidProductSvc.getAllByBuyNo(memNo);

		model.addAttribute("bidProductVOs", bidProductVOs);
		model.addAttribute("success", "結帳成功，請靜待商品出貨！");

		return "frontend/bid/mybidorder";
	}

	// mybidorder按下領收
	@PostMapping("/bidProductReceive")
	public String bidProductReceive(@RequestParam("bidProductNo") Integer bidProductNo, Model model) {

		// 開始修改競標訂單狀態 5 已領收
		Integer orderState = new Integer(5);
		bidProductSvc.updateOrderState(orderState, bidProductNo);

		Integer memNo = 11002;
		List<BidProductVO> bidProductVOs = bidProductSvc.getAllByBuyNo(memNo);

		model.addAttribute("bidProductVOs", bidProductVOs);
		model.addAttribute("success", "編號 " + bidProductNo + " 商品已領收！");

		return "frontend/bid/mybidorder";
	}

	// 會員中心點下我的上架中商品 mysellingbid
	@GetMapping("/mySellingBid")
	public String mySellingBid(Model model) {

		Integer memNo = 11001;

		List<BidProductVO> bidProductVOs = bidProductSvc.getAllBySellerNo(memNo);

		model.addAttribute("bidProductVOs", bidProductVOs);

		return "frontend/bid/mysellingbid";
	}

	// mysellingbid 按下 取回
	@PostMapping("/bidProductGetBack")
	public String bidProductGetBack(@RequestParam("bidProductNo") Integer bidProductNo, Model model) {

		// 開始修改競標訂單狀態 3 取回處理中
		Integer orderState = new Integer(3);
		bidProductSvc.updateOrderState(orderState, bidProductNo);

		Integer memNo = 11001;
		List<BidProductVO> bidProductVOs = bidProductSvc.getAllByBuyNo(memNo);

		model.addAttribute("bidProductVOs", bidProductVOs);
		model.addAttribute("success", "編號" + bidProductNo + "商品取回已提出申請");

		return "frontend/bid/mysellingbid";
	}

	// mysellingbid 按下 重新上架
	@PostMapping("/bidProductRelist")
	public String bidProductRelist(@RequestParam("bidProductNo") Integer bidProductNo, Model model) {
		// 修改狀態為重新申請上架
//		Integer orderState = new Integer(4);
//		bidProductSvc.updateOrderState(orderState, bidProductNo);

		// 取得該商品的applylistvo 放入attribute
		BidProductVO bidProductVO = bidProductSvc.getOneBid(bidProductNo);
		BidApplyListVO bidApplyListVO = bidApplyListSvc.getOneBidApplyList(bidProductVO.getBidApplyListNo());
		model.addAttribute("bidApplyListVO", bidApplyListVO);

		// 取得選單要用的gamelist
		List<GameCompanyVO> gameCompanyVOs = gameCompanySvc.getAll();
		List<GamePlatformTypeVO> gamePlatformTypeVOs = gamePlatformTypeSvc.getAll();
		List<GameTypeVO> gameTypeVOs = gameTypeSvc.getAll();
		model.addAttribute("gameCompanyVOs", gameCompanyVOs);
		model.addAttribute("gameTypeVOs", gameTypeVOs);
		model.addAttribute("gamePlatformTypeVOs", gamePlatformTypeVOs);

		return "frontend/bid/addbidapplylist";
	}

	// 後台當bidapplylist按下上架時訪問的頁面
	@PostMapping("/addBid")
	public String addBid(@RequestParam("bidApplyListNo") String bidApplyListNo, Model model) {

		// 放入bidapplylistvo資訊
		BidApplyListService bidApplyListSvc = new BidApplyListService();
		BidApplyListVO bidApplyListVO = bidApplyListSvc.getOneBidApplyList(Integer.valueOf(bidApplyListNo));
		model.addAttribute("bidApplyListVO", bidApplyListVO);

		// 放入所有商品用來比較是否有該遊戲 自動select
		ProductService productSvc = new ProductService();
		List<ProductVO> productList = productSvc.GetAllProducts();
		model.addAttribute("productList", productList);

		return "backend/bid/addBid";
	}

	@PostMapping("/bidProductInsert")
	public String bidProductInsert(@RequestParam("bidApplyListNo") String bidApplyListNo,
			@RequestParam("productNo") String productNo,
			@NotEmpty(message = "遊戲名稱請勿空白") @RequestParam("bidName") String bidName,
			@RequestParam("bidProdDescription") String bidProdDescription, @RequestParam("sellerNo") String sellerNo,
			@RequestParam("initialPrice") String initialPrice,
			@RequestParam("bidPriceIncrement") String bidPriceIncrement,
			@RequestParam("bidLaunchedTime") String bidLaunchedTime, @RequestParam("bidSoldTime") String bidSoldTime,
			@RequestPart("upfile") MultipartFile[] files, Model model) {

		// 上傳圖片
		if (files.length == 0) {
//			errorMsgs.add("請上傳圖片！");
		}

		// 上傳資料
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
		// 將競標申請單的狀態改成已處理(1)
		bidApplyListSvc.updateApplyState(Integer.valueOf(bidApplyListNo), new Integer(1));

		System.out.println("ok");

		// 裝入所有 VOs 回傳
		List<BidProductVO> bidProductVOs = bidProductSvc.getAll();
		model.addAttribute("bidProductVOs", bidProductVOs);
		return "backend/bid/listAllBid";
	}

	// 後台按下查看競標商品
	@GetMapping("/listAllBidProduct")
	public String listAllBidProduct(Model model) {
		List<BidProductVO> bidProductVOs = bidProductSvc.getAll();
		model.addAttribute("bidProductVOs", bidProductVOs);
		return "backend/bid/listAllBid";
	}

	// 競標商品按下搜尋
	@PostMapping("/bidProductBackendSearch")
	public String bidProductBackendSearch(WebRequest webRequest, Model model) {
		Map<String, String[]> map = webRequest.getParameterMap();
		List<BidProductVO> bidProductVOs = bidProductSvc.getAll(map);
		model.addAttribute("bidProductVOs", bidProductVOs);
		return "backend/bid/listSearchBid";
	}

	// 競標商品按下修改
	@PostMapping("/bidProductEdit")
	public String bidProductEdit(@RequestParam("bidProductNo") Integer bidProductNo, Model model) {

		// 取得該商品資訊
		BidProductVO bidProductVO = bidProductSvc.getOneBid(bidProductNo);
		model.addAttribute("bidProductVO", bidProductVO);

		// 取得該商品圖片
		List<BidPicVO> bidPicVOs = bidPicSvc.getAllBidPicByBidProductNo(bidProductNo);
		model.addAttribute("bidPicVOs", bidPicVOs);

		// 取得所有一手商品VO
		ProductService productSvc = new ProductService();
		List<ProductVO> productVOs = productSvc.GetAllProducts();
		model.addAttribute("productVOs", productVOs);

		return "backend/bid/editBid";
	}

	// 競標商品修改頁面按下修改資料
	@PostMapping("/bidProductEditUpdate")
	public String bidProductEditUpdate(Model model) {

		return "backend/bid/editBid";
	}

	// 後台按下競標訂單管理
	@GetMapping("/listAllBidOrder")
	public String listAllBidOrder(Model model) {
		List<BidProductVO> bidProductVOs = bidProductSvc.getAll();
		model.addAttribute("bidProductVOs", bidProductVOs);
		return "backend/bid/listAllBidOrder";
	}

	// 競標訂單管理按下出貨
	@PostMapping("/bidProductShipping")
	public String bidProductShipping(@RequestParam("bidProductNo") Integer bidProductNo, Model model) {
		// 開始修改競標訂單狀態 2 已出貨
		Integer orderState = new Integer(2);
		bidProductSvc.updateOrderState(orderState, bidProductNo);
		model.addAttribute("success", "編號 " + bidProductNo + " 商品已出貨！");
		List<BidProductVO> bidProductVOs = bidProductSvc.getAll();
		model.addAttribute("bidProductVOs", bidProductVOs);
		return "backend/bid/listAllBidOrder";
	}

	// 競標訂單管理按下撥款
	@PostMapping("bidProductPay")
	public String bidProductPay(@RequestParam("bidProductNo") Integer bidProductNo, Model model) {
		// 開始修改競標訂單狀態 6 已撥款
		Integer orderState = new Integer(6);
		bidProductSvc.updateOrderState(orderState, bidProductNo);
		BidProductVO bidProductVO = bidProductSvc.getOneBid(bidProductNo);
		MemService memService = new MemService();
		MemVO memVO = memService.getMemVObyMemNo(bidProductVO.getSellerNo());
		model.addAttribute("success", "編號 " + bidProductNo + " 商品已撥款至賣方 " + memVO.getMemName() + " 帳戶中！ ");
		List<BidProductVO> bidProductVOs = bidProductSvc.getAll();
		model.addAttribute("bidProductVOs", bidProductVOs);
		return "backend/bid/listAllBidOrder";
	}

}
