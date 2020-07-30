package com.openjava.datalake.insensitives.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DlInsensitivesRuleRepositoryImpl implements DlInsensitivesRuleRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
