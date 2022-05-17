package com.bidapplylist.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.utils.HibernateUtil;

public class BidApplyListHibernateDAO implements BidApplyListDAO_interface {

//	private static final String INSERT_STMT = "INSERT INTO bidapplylist(MemNo, BidName, BidProdDescription, GameCompanyNo, GameTypeNo, GamePlatformNo, InitialPrice, BidPriceIncrement, UpcNum, BidLaunchedTime, BidSoldTime, ApplyState) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//	private static final String UPDATE_STMT = "UPDATE bidapplylist SET MemNo=?, BidName=?, BidProdDescription=?, GameCompanyNo=?, GameTypeNo=?, GamePlatformNo=?, InitialPrice=?, BidPriceIncrement=?, UpcNum=?, BidLaunchedTime=?, BidSoldTime=?, ApplyState=? WHERE BidApplyListNo=?";
//	private static final String DELETE_STMT = "DELETE FROM bidapplylist WHERE BidApplyListNo = ?";
	private static final String GET_ALL_STMT = "SELECT BidApplyListNo, MemNo, BidName, BidProdDescription, GameCompanyNo, GameTypeNo, GamePlatformNo, InitialPrice, BidPriceIncrement, UpcNum, BidLaunchedTime, BidSoldTime, ApplyState FROM bidapplylist";
	private static final String GET_ONE_STMT = "SELECT BidApplyListNo, MemNo, BidName, BidProdDescription, GameCompanyNo, GameTypeNo, GamePlatformNo, InitialPrice, BidPriceIncrement, UpcNum, BidLaunchedTime, BidSoldTime, ApplyState FROM bidapplylist WHERE BidApplyListNo = :bidApplyListNo";
	private static final String GET_ALL_STMT_MEMNO = "SELECT BidApplyListNo, MemNo, BidName, BidProdDescription, GameCompanyNo, GameTypeNo, GamePlatformNo, InitialPrice, BidPriceIncrement, UpcNum, BidLaunchedTime, BidSoldTime, ApplyState FROM bidapplylist WHERE MemNo = :memNo";
	private static final String GET_ALL_STMT_GAMEPLATFORMNO = "SELECT BidApplyListNo, MemNo, BidName, BidProdDescription, GameCompanyNo, GameTypeNo, GamePlatformNo, InitialPrice, BidPriceIncrement, UpcNum, BidLaunchedTime, BidSoldTime, ApplyState FROM bidapplylist WHERE GamePlatformNo = :gamePlatformNo";
	private static final String GET_ALL_STMT_GAMETYPENO = "SELECT BidApplyListNo, MemNo, BidName, BidProdDescription, GameCompanyNo, GameTypeNo, GamePlatformNo, InitialPrice, BidPriceIncrement, UpcNum, BidLaunchedTime, BidSoldTime, ApplyState FROM bidapplylist WHERE GameTypeNo = :gameTypeNo";
	private static final String GET_ALL_STMT_GAMECOMPANYNO = "SELECT BidApplyListNo, MemNo, BidName, BidProdDescription, GameCompanyNo, GameTypeNo, GamePlatformNo, InitialPrice, BidPriceIncrement, UpcNum, BidLaunchedTime, BidSoldTime, ApplyState FROM bidapplylist WHERE GameCompanyNo = :gameCompanyNo";
	private static final String UPDATE_ONE_STMT_APPLYSTATE = "UPDATE bidapplylist set ApplyState=? WHERE BidApplyListNo=?";
	
	@Override
	public void insert(BidApplyListVO bidApplyListVO) {
		HibernateUtil.getSessionFactory().getCurrentSession().save(bidApplyListVO);
	}

	@Override
	public void update(BidApplyListVO bidApplyListVO) {
		HibernateUtil.getSessionFactory().getCurrentSession().update(bidApplyListVO);
	}

	@Override
	public void delete(Integer bidApplyListNo) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		BidApplyListVO bidApplyListVO = session.get(BidApplyListVO.class, bidApplyListNo);
		session.delete(bidApplyListVO);
	}

	@Override
	public List<BidApplyListVO> getAll() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<BidApplyListVO> list = null;
		try {
			session.beginTransaction();
			NativeQuery<BidApplyListVO> query = session.createSQLQuery("SELECT BidApplyListNo, MemNo, BidName, BidProdDescription, GameCompanyNo, GameTypeNo, GamePlatformNo, InitialPrice, BidPriceIncrement, UpcNum, BidLaunchedTime, BidSoldTime, ApplyState FROM bidapplylist")
					.addEntity(BidApplyListVO.class);
			list = query.list();
			session.getTransaction().commit();
			
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
		}
		return list;
	}

	@Override
	public BidApplyListVO findByPrimaryKey(Integer bidApplyListNo) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		BidApplyListVO bidApplyListVO = null;
		try {
			session.beginTransaction();
			bidApplyListVO = session.get(BidApplyListVO.class, bidApplyListNo);
			session.getTransaction().commit();
			
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
		}
		return bidApplyListVO;
	}

	@Override
	public List<BidApplyListVO> findByMemNo(Integer memNo) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<BidApplyListVO> list = null;
		try {
			session.beginTransaction();
			Query query = session.createNativeQuery(GET_ALL_STMT_MEMNO).addEntity(BidApplyListVO.class).setParameter("memNo", memNo);
			list = query.getResultList();
			session.getTransaction().commit();
			
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
		}
		return list;
	}

	@Override
	public void updateApplyState(BidApplyListVO newBidApplyListVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			BidApplyListVO bidApplyListVO = session.get(BidApplyListVO.class, newBidApplyListVO.getBidApplyListNo());
			bidApplyListVO.setApplyState(newBidApplyListVO.getApplyState());
			session.update(bidApplyListVO);
			session.getTransaction().commit();
			
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
		}
	}

	@Override
	public List<BidApplyListVO> findByGamePlatformNo(Integer gamePlatformNo) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<BidApplyListVO> list = null;
		try {
			session.beginTransaction();
			NativeQuery query = session.createNativeQuery(GET_ALL_STMT_GAMEPLATFORMNO).addEntity(BidApplyListVO.class).setParameter("gamePlatformNo", gamePlatformNo);
			list = query.list();
			session.getTransaction().commit();
			
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
		}
		return list;
	}

	@Override
	public List<BidApplyListVO> findByGameTypeNo(Integer gameTypeNo) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<BidApplyListVO> list = null;
		try {
			session.beginTransaction();
			NativeQuery query = session.createNativeQuery(GET_ALL_STMT_GAMETYPENO).addEntity(BidApplyListVO.class).setParameter("gameTypeNo", gameTypeNo);
			list = query.list();
			session.getTransaction().commit();
			
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
		}
		return list;
	}

	@Override
	public List<BidApplyListVO> findByGameCompanyNo(Integer gameCompanyNo) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<BidApplyListVO> list = null;
		try {
			session.beginTransaction();
			NativeQuery query = session.createNativeQuery(GET_ALL_STMT_GAMECOMPANYNO).addEntity(BidApplyListVO.class).setParameter("gameCompanyNo", gameCompanyNo);
			list = query.list();
			session.getTransaction().commit();
			
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
		}
		return list;
	}

}
