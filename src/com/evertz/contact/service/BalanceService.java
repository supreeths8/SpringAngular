package com.evertz.contact.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.evertz.contact.model.Balance;
import com.evertz.contact.model.BalanceRepository;
import com.evertz.contact.model.Contact;

@Service
public class BalanceService {
	
	@Autowired
	private BalanceRepository balanceRepo;
	
	@Transactional
	public List<Balance> listAll() {
		return (List<Balance>) balanceRepo.findAll();	
	}
	
	@Transactional
	public void save(Balance balance) {
		balanceRepo.save(balance);
	}
	
	public Balance get(int id) {
		Optional<Balance> result = balanceRepo.findById(id);
		return result.get();
	}
	
	public void delete(int id) {
		balanceRepo.deleteById(id);
		
	}
	
//	try {
//		float withdrawAmount = Float.parseFloat(req.getParameter("withdrawAmount"));
//		ModelAndView model = new ModelAndView();
//		model.addObject("userContact",userContact);
//		if((userContact.getBalance()-withdrawAmount)>=0) {
//			userContact.setBalance(userContact.getBalance()-withdrawAmount);
//			contactDAO.updateBalance(userContact);
//			model.setViewName("userview");
//			return model;
//		}
//		else {
//			model.setViewName("userview");
//			return model;
//		}
//		}
//		catch (Exception e) {
//			ModelAndView model = new ModelAndView();
//			model.addObject("userContact", userContact);
//			model.setViewName("userview");
//			return model;
//		}
	
	public int withdrawl(Balance balance, float toWithdraw, HttpSession userSession) {
		int flag = 0;
		if((balance.getAmount() - toWithdraw) >= 0) {
			flag = 1;
			balance.setAmount((balance.getAmount()-toWithdraw));
			userSession.setAttribute("userBalance", balance);
			save(balance);
			return flag;
		}
		else {
			return flag;
		}
	}

}
