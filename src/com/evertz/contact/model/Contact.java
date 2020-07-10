package com.evertz.contact.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "contact")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "contact_id")
	private Integer contact_id;
	private String name;
	private String email;
	private String address;
	private String phone;
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Contact() {

	}

	public Contact(Integer contact_id, String name, String email, String address, String phone) {
		this(name, email, address, phone);
		this.contact_id = contact_id;
	}

	@Override
	public String toString() {
		return "Contact [contact_id=" + contact_id + ", name=" + name + ", email=" + email + ", address=" + address + ", phone=" + phone
				+ ", password=" + password + "]";
	}

	public Contact(String name, String email, String address, String phone) {
		super();
		this.name = name;
		this.email = email;
		this.address = address;
		this.phone = phone;
	}

	public Contact(Integer contact_id, String name, String email, String address, String phone, String password,
			float balance) {
		super();
		this.contact_id = contact_id;
		this.name = name;
		this.email = email;
		this.address = address;
		this.phone = phone;
		this.password = password;
	}

	public Contact(Integer contact_id, String name, String email, String address, String phone, String password) {
		super();
		this.contact_id = contact_id;
		this.name = name;
		this.email = email;
		this.address = address;
		this.phone = phone;
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getId() {
		return contact_id;
	}

	public void setId(Integer contact_id) {
		this.contact_id = contact_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
