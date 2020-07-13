package com.evertz.contact.controller;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.evertz.contact.model.Admin;
import com.evertz.contact.model.Balance;
import com.evertz.contact.model.Contact;
import com.evertz.contact.service.BalanceService;
import com.evertz.contact.service.ContactService;

@CrossOrigin("*")
@RestController
public class MainController {

	@Autowired
	private ContactService service;

	@Autowired
	private BalanceService balanceService;

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

////////////////////////////////////////////////////////////////////////////
	@GetMapping("/index")
	public ResponseEntity<List<Contact>> index() {
		List<Contact> list = service.listAll();
		return ResponseEntity.ok().body(list);
	}

	@PostMapping("/save")
	public ResponseEntity<?> save(@RequestBody Contact contact) {
		service.save(contact);
		balanceService.save(new Balance());
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<Contact> get(@PathVariable("id") int id) {
		Contact contact = service.get(id);
		return ResponseEntity.ok().body(contact);
	}

	@RequestMapping(value = "/edit/{id}")
	public ResponseEntity<?> edit(@PathVariable("id") int id, @RequestBody Contact contact) {
		service.update(id, contact);
		return ResponseEntity.ok().body("UPDATED");
	}

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> deleteContact(@PathVariable("id") int id) {
		balanceService.delete(id);
		service.delete(id);
		return ResponseEntity.ok().body("DELETED");
	}

	@RequestMapping(value = "/user/{id}")
	public ResponseEntity<Contact> getUserData(@PathVariable("id") int id) {
		Contact contact = service.get(id);
		return ResponseEntity.ok().body(contact);
	}

	@RequestMapping(value = "/user/{id}/balance")
	public ResponseEntity<Balance> getUserBalance(@PathVariable("id") int id) {
		Balance bal = balanceService.get(id);
		return ResponseEntity.ok().body(bal);
	}

	@RequestMapping(value = "/user/withdraw", method = RequestMethod.POST)
	public ResponseEntity<?> withdrawl(@RequestBody Balance balance, @RequestBody float toWithdraw) {
		if (balanceService.withdrawl(balance, toWithdraw) == 1) {
			return ResponseEntity.ok().body("BALANCE UPDATED");
		}
		return ResponseEntity.ok().body("NO");
	}

	@RequestMapping(value = "/user/deposit", method = RequestMethod.POST)
	public ResponseEntity<?> deposit(@RequestBody Balance balance) {
		float toDeposit = 69;
		balanceService.deposit(balance, toDeposit);
		return ResponseEntity.ok().body("BALANCE UPDATED");
	}
	

///////////////////////////////////////////////////////////////////////////

	@RequestMapping(value = "/logout")
	public ModelAndView logout(HttpSession session) {
		ModelAndView model = new ModelAndView();
		session.invalidate();
		model.setViewName("welcome");
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
		if ((req.getParameter("byId") == null) || (req.getParameter("keyword") == null)) {
			returnIndex(model);
			model.addObject("message", "Check search parameters");
			return model;
		}
		List<Contact> searchContactResults = new ArrayList<Contact>();
		List<Balance> searchBalanceResults = new ArrayList<Balance>();
		if (req.getParameter("byId").contentEquals("id")) {
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
