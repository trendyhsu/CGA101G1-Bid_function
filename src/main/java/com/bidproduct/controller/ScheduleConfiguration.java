//package com.bidproduct.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import com.bidproduct.model.BidProductService;
//import com.bidproduct.model.BidProductVO;
//
//@Configuration
//@EnableScheduling
//public class ScheduleConfiguration {
//	
//	@Autowired
//	BidProductService bidProductSvc;
//	
//	List<BidProductVO> bidProductVOAll = bidProductSvc.getAll();
//		
//	@Scheduled(fixedRate = 10000)
//	public void changeBidState() {
//		int i = 0;
//		System.out.println(i++);
//	}
//}
