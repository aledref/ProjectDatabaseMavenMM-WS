package com.excilys.formation.webproject.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.formation.webproject.core.Computer;
import com.excilys.formation.webproject.core.LogDAOom;
import com.excilys.formation.webproject.core.PageWrapper;
import com.excilys.formation.webproject.core.QComputer;
import com.mysema.query.jpa.impl.JPAQuery;

/**
 * 
 * @author excilys
 *
 */		
@Repository
public class ComputerDAOImpl implements ComputerDAO{

	final Logger logger = LoggerFactory.getLogger(ComputerDAOImpl.class);	
	
	@PersistenceContext(unitName = "computer",type = PersistenceContextType.TRANSACTION)
	private EntityManager em;
	
	@Autowired
	private LogDAO logDAO;
	
	@Override
	public Computer find(Long id) {
		JPAQuery query = new JPAQuery(em); 
		QComputer qcpu = QComputer.computer;
		Computer cpu = query.from(qcpu)
				  .where(qcpu.id.eq(id))
				  .uniqueResult(qcpu);
		return cpu;
	}
	
	@Override
	public Long getListSize() {
		JPAQuery query = new JPAQuery(em); 
		QComputer qcpu = QComputer.computer;
		query.from(qcpu);
		
		LogDAOom logDAOom = new LogDAOom();
		logDAOom.setTitle("INFO");
		logDAOom.setBody("Listing Computer size");
		logDAO.create(logDAOom,em);
		return query.count();
	}
	
	@Override
	public void getList(PageWrapper pageWrapper) {
		JPAQuery query = new JPAQuery(em); 
		QComputer qcpu = QComputer.computer;
		query.from(qcpu);
		switch(pageWrapper.getOrderNumber()) {
		case "0" :	
			query.orderBy( qcpu.id.asc(), qcpu.id.asc());
			break;
		case "1" :
			query.orderBy( qcpu.id.desc(), qcpu.id.asc());
			break;
		case "2" :
			query.orderBy( qcpu.introduced.asc(), qcpu.id.asc());
			break;
		case "3" :
			query.orderBy( qcpu.introduced.desc(), qcpu.id.asc());
			break;
		case "4" :
			query.orderBy( qcpu.discontinued.asc(), qcpu.id.asc());
			break;
		case "5" :
			query.orderBy( qcpu.discontinued.desc(), qcpu.id.asc());
			break;
		case "6" :
			query.orderBy( qcpu.company.name.asc(), qcpu.id.asc());
			break;
		case "7" :
			query.orderBy( qcpu.company.name.desc(), qcpu.id.asc());
			break;	
		default :
			query.orderBy( qcpu.id.asc());
			break;
		}
		query.offset(pageWrapper.getPerPage()*(pageWrapper.getPageNumber()-1));
		query.limit(pageWrapper.getPerPage());
		List<Computer> list = query.list(qcpu);
		pageWrapper.setComputerList(list);
		
		LogDAOom logDAOom = new LogDAOom();
		logDAOom.setTitle("INFO");
		logDAOom.setBody("Listing Computer");
		logDAO.create(logDAOom,em);
	}
	
	@Override
	public Long getListSizeWithName(PageWrapper pageWrapper) {	
		JPAQuery query = new JPAQuery(em); 
		QComputer qcpu = QComputer.computer;
		query.from(qcpu);
		switch(pageWrapper.getOrderNumber()) {
		case "0" :	
			query.orderBy( qcpu.id.asc(), qcpu.id.asc());
			break;
		case "1" :
			query.orderBy( qcpu.id.desc(), qcpu.id.asc());
			break;
		case "2" :
			query.orderBy( qcpu.introduced.asc(), qcpu.id.asc());
			break;
		case "3" :
			query.orderBy( qcpu.introduced.desc(), qcpu.id.asc());
			break;
		case "4" :
			query.orderBy( qcpu.discontinued.asc(), qcpu.id.asc());
			break;
		case "5" :
			query.orderBy( qcpu.discontinued.desc(), qcpu.id.asc());
			break;
		case "6" :
			query.orderBy( qcpu.company.name.asc(), qcpu.id.asc());
			break;
		case "7" :
			query.orderBy( qcpu.company.name.desc(), qcpu.id.asc());
			break;	
		default :
			query.orderBy( qcpu.id.asc());
			break;
		}
		query.offset(pageWrapper.getPerPage()*(pageWrapper.getPageNumber()-1));
		query.limit(pageWrapper.getPerPage());
		
		LogDAOom logDAOom = new LogDAOom();
		logDAOom.setTitle("INFO");
		logDAOom.setBody("Listing Computer size with search filter : "+pageWrapper.getNameFilter());
		logDAO.create(logDAOom,em);
		
		return query.from(qcpu).count();
	}
	
	
	@Override
	public List<Computer> getListWithName(PageWrapper pageWrapper) {
		String sql = new StringBuilder("from Computer cpu where cpu.name like :name1 or cpu.company.name like :name2 order by ")
				.append(pageWrapper.getFieldOrder()).append(" ")
				.append(pageWrapper.getOrder()).toString();
		Query q = em.createQuery(sql);
		q.setFirstResult(pageWrapper.getPerPage()*(pageWrapper.getPageNumber()-1));
		q.setMaxResults(pageWrapper.getPerPage());
		String namefilter = new StringBuilder("%").append(pageWrapper.getNameFilter()).append("%").toString();
		q.setParameter("name1", namefilter);
		q.setParameter("name2", namefilter);
		List<Object> list = (List<Object>) q.getResultList();
		List<Computer> listCpu = new ArrayList<Computer>();
		for (Object i : list) {
			listCpu.add((Computer) i);
		}
		
		LogDAOom logDAOom = new LogDAOom();
		logDAOom.setTitle("INFO");
		logDAOom.setBody("Listing Computer with search filter : "+pageWrapper.getNameFilter());
		logDAO.create(logDAOom,em);
		
		return listCpu;
	}

	@Override
	public void create(Computer comp) {

	    em.persist(comp);
	    
	    LogDAOom logDAOom = new LogDAOom();
		logDAOom.setTitle("INFO");
		logDAOom.setBody("Creating Computer");
		logDAO.create(logDAOom,em);

	    em.close();
	}
	
	@Override
	public void save(Computer comp,Long id) {
		EntityTransaction tx = em.getTransaction();
		comp.setId(id);
	    tx.begin();
	    em.merge(comp);
	    
	    LogDAOom logDAOom = new LogDAOom();
		logDAOom.setTitle("INFO");
		logDAOom.setBody("Saving Computer");
		logDAO.create(logDAOom,em);
	    
	    tx.commit();
	    em.close();
	}
	
	@Override
	public void delete(Long id){   
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    Computer comp = find(id);
	    em.remove(em.contains(comp) ? comp : em.merge(comp));
	    
	    LogDAOom logDAOom = new LogDAOom();
		logDAOom.setTitle("INFO");
		logDAOom.setBody("Deleting Computer");
		logDAO.create(logDAOom,em);
	    
	    tx.commit();
	    em.close();
	 }	
}