package com.excilys.formation.webproject.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.formation.webproject.core.Computer;
import com.excilys.formation.webproject.core.PageWrapper;
import com.excilys.formation.webproject.core.QComputer;
import com.jolbox.bonecp.BoneCPDataSource;
import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQuery;

/**
 * attributenumber : associates an Integer to any field of Computer
 *  	-Long id : 0;
 *  	-String	name : 1;
 *  	-Timestamp introduced : 2;
 *  	-Timestamp discontinued : 3;
 *  	-Company company : 4;
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
	
	@Autowired
	private BoneCPDataSource dataSource;
	
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
		
		switch(pageWrapper.get)
		
		
		query.orderBy( qcpu.id.asc(), qcpu.id.asc());
		
		
		
		
		
		
		
		query.offset(pageWrapper.getPerPage()*(pageWrapper.getPageNumber()-1));
		query.limit(pageWrapper.getPerPage());
		List<Computer> list = query.list(qcpu);
		pageWrapper.setComputerList(list);
		
		String namefilter = new StringBuilder("%").append(pageWrapper.getNameFilter()).append("%").toString();
		CriteriaBuilder builder = emf.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery( Long.class );
		Root<Computer> root = criteria.from( Computer.class );
		//criteria.where(builder.like(Computer_.name,namefilter));
		//criteria.where(builder.like(root.<Company>get("company").<String>get("name"),namefilter));
		criteria.select(builder.count(criteria.from(Computer.class)));
		return em.createQuery(criteria).getSingleResult();
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