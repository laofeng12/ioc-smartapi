package com.openjava.datalake.rescata.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DlRescataStructurePermissionRepositoryImpl implements DlRescataStructurePermissionRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
