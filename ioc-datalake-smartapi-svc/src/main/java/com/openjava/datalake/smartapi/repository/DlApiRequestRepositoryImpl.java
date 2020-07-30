package com.openjava.datalake.smartapi.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DlApiRequestRepositoryImpl implements DlApiRequestRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
