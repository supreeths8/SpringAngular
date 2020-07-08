package com.evertz.contact.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.evertz.contact.model.Admin;
import com.evertz.contact.model.Balance;
import com.evertz.contact.model.Contact;
import com.evertz.contact.service.BalanceService;
import com.evertz.contact.service.ContactService;

@Controller
public class MainController {

	@Autowired
	private ContactService service;

	@Autowired
	private BalanceService balanceService;

	@Autowired
	private Admin admin;

	String msg = "Wrong Credentials";

	public ModelAndView returnIndex(ModelAndView model) {
		List<Contact> listContact = service.listAll();
		List<Balance> listBalance = balanceService.listAll();
		model.addObject("listContact", listContact);
		model.addObject("listBalance", listBalance);
		model.setViewName("index");
		return model;
	}

	@RequestMapping(value = "/")
	public ModelAndView adminContact(ModelAndView model) {
		model.setViewName("welcome");
		return model;
	}

	@RequestMapping(value = "/adminlogin")
	public ModelAndView adminLogin() {
		ModelAndView model = new ModelAndView();
		model.setViewName("adminlogin");
		return model;
	}

	@RequestMapping(value = "/userlogin")
	public ModelAndView userLogin() {
		ModelAndView model = new ModelAndView();
		model.setViewName("userlogin");
		return model;
	}

	@RequestMapping(value = "/index")
	public ModelAndView login(HttpServletRequest req, HttpSession session) {
		ModelAndView model = new ModelAndView();
		if(session.getAttribute("admin") != null) {
			model = returnIndex(model);
			return model;
		}
		admin.setId(req.getParameter("id"));
		admin.setPassword(req.getParameter("password"));
		session.setAttribute("admin", admin);
		if (admin.getId() == null) {
			model.addObject("message", "Session Error");
			model.setViewName("adminlogin");
		} else if (admin.getId().equals("admin") && admin.getPassword().equals("admin")) {
			model = returnIndex(model);
		} else {
			model.addObject("message", msg);
			model.setViewName("adminlogin");
		}
		return model;

	}

	@RequestMapping(value = "/logout")
	public ModelAndView logout(HttpSession session) {
		ModelAndView model = new ModelAndView();
		session.invalidate();
		model.setViewName("welcome");
		return model;
	}

	@RequestMapping(value = "/new")
	public ModelAndView newContact(HttpSession session) {
		Contact newContact = new Contact();
		ModelAndView model = new ModelAndView();

		if (session.getAttribute("admin") != null) {
			model.addObject(newContact);
			model.setViewName("contact_form");
		} else {
			msg = "Session error";
			model.setViewName("adminlogin");
			model.addObject("message", msg);
		}
		return model;
	}

	public void saveFunction(Contact saveContact, Balance saveBalance) {
		saveBalance.setAmount(0);
		service.save(saveContact);
		balanceService.save(saveBalance);
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView saveContact(@ModelAttribute Contact contact, Balance balance) {
		ModelAndView model = new ModelAndView();
		saveFunction(contact, balance);
		model = returnIndex(model);
		return model;
	}

	@RequestMapping(value = "/edit")
	public ModelAndView editContact(@RequestParam int id, HttpSession session) {
		ModelAndView model = new ModelAndView();
		Contact editContact = service.get(id);
		System.out.println(editContact);
		if (session.getAttribute("admin") != null) {
			model.addObject(editContact);
			model.setViewName("update_form");
		} else {
			msg = "Session error";
			model.setViewName("adminlogin");
			model.addObject("message", msg);
		}

		return model;
	}

	@RequestMapping(value = "/delete")
	public ModelAndView deleteContact(@RequestParam int id, HttpSession session) {
		ModelAndView model = new ModelAndView();
		if (session.getAttribute("admin") != null) {
			service.delete(id);
			balanceService.delete(id);
			model = returnIndex(model);
		} else {
			msg = "Session error";
			model.setViewName("adminlogin");
			model.addObject("message", msg);
		}

		return model;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView updateContact(@ModelAttribute("contact") Contact contact) {
		ModelAndView model = new ModelAndView();
		System.out.println(contact);
		service.save(contact);
		model = returnIndex(model);
		return model;
	}

	@RequestMapping(value = "/userview")
	public ModelAndView userLogin(HttpServletRequest req, HttpSession userSession) {
		ModelAndView model = new ModelAndView();
		if (userSession.getAttribute("user") != null) {
			Contact user = (Contact) userSession.getAttribute("user");
			Balance balance = (Balance) userSession.getAttribute("userBalance");
			model.addObject("userContact", user);
			model.addObject("userBalance)", balance);
			model.setViewName("userview");
		} else {
			String Id = req.getParameter("id");
			String password = req.getParameter("password");
			if ((Id == null) || (password == null)) {
				model.addObject("message", "Chutiya nahi bana sakta");
				model.setViewName("userlogin");
				return model;
			}
			int id = Integer.parseInt(Id);
			Contact userContact = service.get(id);
			Balance userBalance = balanceService.get(id);
			if ((id == userContact.getId()) && (password.contentEquals(userContact.getPassword()))) {
				userSession.setAttribute("user", userContact);
				userSession.setAttribute("userBalance", userBalance);
				model.addObject("userContact", userContact);
				model.addObject("userBalance)", userBalance);
				model.setViewName("userview");
			} else {
				model.addObject("message", "Wrong Credentials");
				model.setViewName("userlogin");
			}
		}
		return model;
	}

	@RequestMapping(value = "/userview/deposit")
	public ModelAndView depositAmount(HttpServletRequest req, HttpSession userSession) {
		ModelAndView model = new ModelAndView();
		Float toDeposit = Float.parseFloat(req.getParameter("depositAmount"));
		if (userSession.getAttribute("userBalance") != null) {
			Balance balance = (Balance) userSession.getAttribute("userBalance");
			balance.setAmount((balance.getAmount() + toDeposit));
			balanceService.save(balance);
			userSession.setAttribute("userBalance", balance);
			model.addObject("userContact", userSession.getAttribute("user"));
			model.addObject("userBalance", balance);
			model.setViewName("userview");
		} else {
			model.addObject("message", "Session Error");
			model.setViewName("userlogin");
		}
		return model;
	}

	@RequestMapping(value = "/userlogout")
	public ModelAndView userLogout(HttpSession userSession) {
		ModelAndView model = new ModelAndView();
		userSession.invalidate();
		model.setViewName("welcome");
		return model;
	}

	@RequestMapping(value = "/userview/withdraw")
	public ModelAndView withdrawAmount(HttpServletRequest req, HttpSession userSession) {
		ModelAndView model = new ModelAndView();
		Float toWithdraw = Float.parseFloat(req.getParameter("withdrawAmount"));
		if (userSession.getAttribute("userBalance") != null) {
			Balance balance = (Balance) userSession.getAttribute("userBalance");
			if (balanceService.withdrawl(balance, toWithdraw, userSession) == 1) {
				model.addObject("userContact", userSession.getAttribute("user"));
				model.addObject("userBalance", userSession.getAttribute("userBalance"));
				model.setViewName("userview");
			} else {
				model.addObject("userContact", userSession.getAttribute("user"));
				model.addObject("userBalance", userSession.getAttribute("userBalance"));
				model.addObject("message", "Not enough balance");
				model.setViewName("userview");

			}

		} else {
			model.addObject("message", "Session Error");
			model.setViewName("userlogin");
		}
		return model;

	}
	
	@RequestMapping(value = "/search")
	public ModelAndView searchIndex(HttpServletRequest req) {
		ModelAndView model = new ModelAndView();
		if((req.getParameter("byId") == null) || (req.getParameter("keyword") == null)) {
			returnIndex(model);
			model.addObject("message", "Check search parameters");
			return model;
		}
		List<Contact> searchContactResults = new ArrayList<Contact> ();
		List<Balance> searchBalanceResults = new ArrayList<Balance> ();
		if(req.getParameter("byId").contentEquals("id")) {
			Balance balance = balanceService.get(Integer.parseInt(req.getParameter("keyword")));
			searchContactResults.add(service.searchById(req.getParameter("keyword")));
			searchBalanceResults.add(balance);
			model.addObject("listContact", searchContactResults);
			model.addObject("listBalance", searchBalanceResults);
			model.setViewName("index");
		}
		return model;
	}
}
