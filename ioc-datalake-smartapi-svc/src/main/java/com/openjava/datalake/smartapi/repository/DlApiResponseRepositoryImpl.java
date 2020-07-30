package com.openjava.datalake.smartapi.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DlApiResponseRepositoryImpl implements DlApiResponseRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
