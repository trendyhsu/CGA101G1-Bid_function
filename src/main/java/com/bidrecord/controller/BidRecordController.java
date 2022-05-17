package com.bidrecord.controller;

import java.io.Writer;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.bidrecord.model.BidRecordService;
import com.bidrecord.model.BidRecordVO;
import com.google.gson.Gson;
import com.member.model.MemVO;

@Controller
@Validated
@RequestMapping("/bidRecord")
public class BidRecordController {

	@Autowired
	private ServletContext context;

	@Autowired
	BidRecordService bidRecordSvc;

	@RequestMapping("/getAllJson")
	@ResponseBody
	public String getAllJson() {
		List<BidRecordVO> bidRecordVOs = bidRecordSvc.getAll();
		Gson gson = new Gson();
		String json = gson.toJson(bidRecordVOs);
		return json;
	}

	// 競標時按下出價先確定有沒有session 順便在session中存入導回的商品網址
	@PostMapping("/bidRecordGetSession")
	@ResponseBody
	public String bidRecordGetSession(@RequestParam("bidProductNo") Integer bidProductNo, HttpSession session) {

		System.out.println(bidProductNo);
		session.setAttribute("initlocation", context.getContextPath() + "/bid/listOneBid?bidProductNo=" + bidProductNo);

//		MemVO memVO = (MemVO) session.getAttribute("memVO");
		String memNo = null;
//		try {
//			memNo = memVO.getMemNo();
//		} catch (NullPointerException e) {
//			memNo = 0;
//		}
		memNo = String.valueOf(11003);
		return memNo;
	}

	// 後台競標商品按下出價紀錄查詢
	@PostMapping("/bidRecordGetOneByBidProductNo")
	public String bidRecordGetOneByBidProductNo(@RequestParam Integer bidProductNo, Model model) {
		List<BidRecordVO> bidRecordVOs = bidRecordSvc.getAllBidRecordByBidProductNo(bidProductNo);
		model.addAttribute("bidRecordVOs", bidRecordVOs);
		return "backend/bid/listAllBidRecordByBidProductNo";
	}
}
