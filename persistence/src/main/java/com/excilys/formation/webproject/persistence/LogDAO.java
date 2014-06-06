package com.excilys.formation.webproject.persistence;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.excilys.formation.webproject.core.LogDAOom;
import com.excilys.formation.webproject.core.PageWrapper;
import com.excilys.formation.webproject.core.QLogDAOom;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository
public class LogDAO {	
	
	public LogDAOom find(Long id, EntityManager em) {
		JPAQuery query = new JPAQuery(em); 
		QLogDAOom qlog = QLogDAOom.logDAOom;
		LogDAOom logDAOom = query.from(qlog)
				  .where(qlog.id.eq(id))
				  .uniqueResult(qlog);
		return logDAOom;
	}
	
	public Long getListSize(EntityManager em) {
		JPAQuery query = new JPAQuery(em); 
		QLogDAOom qlog = QLogDAOom.logDAOom;
		query.from(qlog);
		return query.count();
	}
	
	public List<LogDAOom> getList(PageWrapper pageWrapper, EntityManager em) {
		JPAQuery query = new JPAQuery(em); 
		QLogDAOom qlog = QLogDAOom.logDAOom;
		query.from(qlog);
		query.orderBy(qlog.id.asc());
		List<LogDAOom> list = query.list(qlog);
		return list;
	}
	
	public void create(LogDAOom logDAOom, EntityManager em) {
	    em.persist(logDAOom);
	}
	
	public void save(LogDAOom logDAOom,Long id, EntityManager em) {
		logDAOom.setId(id);
	    em.merge(logDAOom);
	}
	
	public void delete(Long id, EntityManager em){   
		LogDAOom logDAOom = find(id,em);
	    em.remove(em.contains(logDAOom) ? logDAOom : em.merge(logDAOom));
	 }	
}
