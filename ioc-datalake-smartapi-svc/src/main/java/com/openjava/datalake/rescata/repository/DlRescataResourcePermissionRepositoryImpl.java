package com.openjava.datalake.rescata.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DlRescataResourcePermissionRepositoryImpl implements DlRescataResourcePermissionRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
