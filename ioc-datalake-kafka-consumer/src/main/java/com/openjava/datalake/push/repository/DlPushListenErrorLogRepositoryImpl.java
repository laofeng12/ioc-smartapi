package com.openjava.datalake.push.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DlPushListenErrorLogRepositoryImpl implements DlPushListenErrorLogRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
