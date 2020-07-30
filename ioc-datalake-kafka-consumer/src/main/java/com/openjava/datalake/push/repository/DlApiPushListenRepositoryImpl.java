package com.openjava.datalake.push.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DlApiPushListenRepositoryImpl implements DlApiPushListenRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
