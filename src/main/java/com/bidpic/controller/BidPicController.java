package com.bidpic.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.bidpic.model.BidPicService;
import com.bidpic.model.BidPicVO;
import com.bidproduct.model.BidProductService;
import com.bidproduct.model.BidProductVO;
import com.google.gson.Gson;

@Controller
@Validated
@RequestMapping("/bidpic")
public class BidPicController {
	
	@Autowired
	BidProductService bidProductSvc;
	
	@Autowired
	BidPicService bidPicSvc;
	
	@RequestMapping("/bidPicGetAll")
	@ResponseBody
	public String bidPicGetAll(@RequestParam("bidProductNo") Integer bidProductNo) {
		BidPicService bidPicSvc = new BidPicService();
		List<BidPicVO> list = bidPicSvc.getAllBidPicByBidProductNo(bidProductNo);
		Gson gson = new Gson();
		String json = gson.toJson(list);
		return json;
	}
	
	// 競標商品修改頁面按下刪除圖片
	@PostMapping("/bidPicDelete")
	public String bidPicDelete(@RequestParam(value = "bidProdPicNos",required = false) String[] bidProdPicNos, 
							   @RequestParam("bidProductNo") Integer bidProductNo, 
							   Model model) {
		// 取得checkbox字串編號 刪除圖片
		if(bidProdPicNos==null) {
			model.addAttribute("message","請選擇要刪除的圖片");
			
			// 取得該商品資訊
			BidProductVO bidProductVO = bidProductSvc.getOneBid(bidProductNo);
			model.addAttribute("bidProductVO", bidProductVO);
			// 取得該商品圖片
			List<BidPicVO> bidPicVOs = bidPicSvc.getAllBidPicByBidProductNo(bidProductNo);
			model.addAttribute("bidPicVOs", bidPicVOs);
			
			return "backend/bid/editBid";
		}

		if (bidProdPicNos != null) {
			for (String bidProdPicNo : bidProdPicNos) {
				bidPicSvc.deleteBidPic(Integer.valueOf(bidProdPicNo));
			}
		}
		
		// 取得該商品資訊
		BidProductVO bidProductVO = bidProductSvc.getOneBid(bidProductNo);
		model.addAttribute("bidProductVO", bidProductVO);
		// 取得該商品圖片
		List<BidPicVO> bidPicVOs = bidPicSvc.getAllBidPicByBidProductNo(bidProductNo);
		model.addAttribute("bidPicVOs", bidPicVOs);
		model.addAttribute("success","圖片刪除成功!");
		
		return "backend/bid/editBid";
	}
	
	// 競標商品修改頁面按下新增圖片
	@PostMapping("/bidPicInsertMulti")
	public String bidPicInsertMulti(@RequestParam("bidProductNo") Integer bidProductNo, 
									@RequestPart("upfile") MultipartFile[] files, 
									Model model) {

		// 判斷沒選照片直接送出
		if(files!=null&&files.length>0){ 
			// 新增圖片
			for(MultipartFile multipartFile : files) {
				byte[] img;
				
				try {
					img = multipartFile.getBytes();
					bidPicSvc.addBidPic(bidProductNo, img);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		
		// 取得該商品資訊
		BidProductVO bidProductVO = bidProductSvc.getOneBid(bidProductNo);
		model.addAttribute("bidProductVO", bidProductVO);
		// 取得該商品圖片
		List<BidPicVO> bidPicVOs = bidPicSvc.getAllBidPicByBidProductNo(bidProductNo);
		model.addAttribute("bidPicVOs", bidPicVOs);
		model.addAttribute("success","圖片新增成功!");
		return "backend/bid/editBid";
	}
}
