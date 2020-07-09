package com.evertz.contact.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "balance")
public class Balance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer contact_id;
	private float amount=0;

	public Balance() {
		super();
	}

	public Balance(Integer contact_id, float amount) {
		super();
		this.contact_id = contact_id;
		this.amount = amount;
	}

	public Integer getContact_id() {
		return contact_id;
	}

	public void setContact_id(Integer contact_id) {
		this.contact_id = contact_id;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

}
