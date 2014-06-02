package com.excilys.formation.webproject.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.formation.webproject.core.Computer;
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
	
	@Autowired
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("computer");
	
	@PersistenceContext(unitName = "computer")
	private EntityManager em;
	
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
		return query.count();
	}
	
	@Override
	public void getList(PageWrapper pageWrapper) {
		JPAQuery query = new JPAQuery(em); 
		QComputer qcpu = QComputer.computer;
		query.from(qcpu).orderBy(qcpu.name.asc(), qcpu.id.asc());
		query.offset(pageWrapper.getPerPage()*(pageWrapper.getPageNumber()-1));
		query.limit(pageWrapper.getPerPage());
		List<Computer> list = query.list(qcpu);
		pageWrapper.setComputerList(list);
	}
	
	@Override
	public Long getListSizeWithName(PageWrapper pageWrapper) {	
		JPAQuery query = new JPAQuery(em); 
		QComputer qcpu = QComputer.computer;
		query.from(qcpu);
		switch(pageWrapper.getFieldOrder()+pageWrapper.getOrder()) {
		case "cpu.idASC" :	
			query.orderBy( qcpu.id.asc(), qcpu.id.asc());
			break;
		case "cpu.idDESC" :
			query.orderBy( qcpu.id.desc(), qcpu.id.asc());
			break;
		case "cpu.introducedASC" :
			query.orderBy( qcpu.introduced.asc(), qcpu.id.asc());
			break;
		case "cpu.introducedDESC" :
			query.orderBy( qcpu.introduced.desc(), qcpu.id.asc());
			break;
		case "cpu.discontinuedASC" :
			query.orderBy( qcpu.discontinued.asc(), qcpu.id.asc());
			break;
		case "cpu.discontinuedDESC" :
			query.orderBy( qcpu.discontinued.desc(), qcpu.id.asc());
			break;
		case "cpu.company_idDESC" :
			query.orderBy( qcpu.company.id.asc(), qcpu.id.asc());
			break;
		case "cpu.company_idASC" :
			query.orderBy( qcpu.company.id.desc(), qcpu.id.asc());
			break;	
		default :
			query.orderBy( qcpu.id.asc());
			break;
		}
		query.offset(pageWrapper.getPerPage()*(pageWrapper.getPageNumber()-1));
		query.limit(pageWrapper.getPerPage());
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
		return listCpu;
	}

	@Override
	public void create(Computer comp) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    em.persist(comp);
	    tx.commit();
	    em.close();
	}
	
	@Override
	public void save(Computer comp,Long id) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		comp.setId(id);
	    tx.begin();
	    em.merge(comp);
	    tx.commit();
	    em.close();
	}
	
	@Override
	public void delete(Long id){   
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    Computer comp = find(id);
	    em.remove(em.contains(comp) ? comp : em.merge(comp));
	    tx.commit();
	    em.close();
	 }	
}