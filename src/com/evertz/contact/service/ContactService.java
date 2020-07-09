package com.evertz.contact.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evertz.contact.model.Contact;
import com.evertz.contact.model.ContactRepository;

@Service
public class ContactService {

	@Autowired
	private ContactRepository contactRepo;

	@Transactional
	public List<Contact> listAll() {
		return (List<Contact>) contactRepo.findAll();
	}

	@Transactional
	public void save(Contact contact) {
		contactRepo.save(contact);

	}

	public Contact get(int id) {
		Optional<Contact> result = contactRepo.findById(id);
		return result.get();
	}

	public void delete(int id) {
		contactRepo.deleteById(id);
	}

	public Contact searchById(String value) {
		return get(Integer.parseInt(value));
	}

	public void update(int id, Contact contact) {
		Contact oldContact = get(id);
		oldContact.setId(id);
		oldContact.setName(contact.getName());
		oldContact.setAddress(contact.getAddress());
		oldContact.setEmail(contact.getPassword());
		oldContact.setPassword(contact.getPassword());
		oldContact.setPhone(contact.getPhone());
		save(oldContact);
	}
	
}
