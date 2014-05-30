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
import com.jolbox.bonecp.BoneCPDataSource;

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
	
	@Autowired
	private Closer closer;
	
	@Override
	public Computer find(Long id) {
		String sql = "from Computer cpu where cpu.id = :id";
		Query q = em.createQuery(sql);
		Computer computer = (Computer)  q.setParameter("id",id).getSingleResult(); 
		em.close();
	    return computer	;
	}
	
	@Override
	public Long getListSize() {
		String sql = "select count(cpu) from Computer cpu";
		return  (Long) em.createQuery(sql).getSingleResult();
	}
	
	@Override
	public void getList(PageWrapper pageWrapper) {
		String sql = new StringBuilder("from Computer cpu order by ").append(pageWrapper.getFieldOrder()).append(" ")
				.append(pageWrapper.getOrder()).append(" , cpu.name asc").toString();
		Query q = em.createQuery(sql);
		q.setFirstResult(pageWrapper.getPerPage()*(pageWrapper.getPageNumber()-1));
		q.setMaxResults(pageWrapper.getPerPage());
		List<Object> list = (List<Object>) q.getResultList();
		List<Computer> listCpu = new ArrayList<Computer>();
		for (Object i : list) {
			listCpu.add((Computer) i);
		}
		pageWrapper.setComputerList(listCpu);
	}
	
	@Override
	public Long getListSizeWithName(PageWrapper pageWrapper) {	
		String sql = "select count(cpu) from Computer cpu where cpu.name like :name1 or cpu.company.name like :name2";
		Query q = em.createQuery(sql);
		String namefilter = new StringBuilder("%").append(pageWrapper.getNameFilter()).append("%").toString();
		q.setParameter("name1", namefilter);
		q.setParameter("name2", namefilter);				
		return (Long) q.getSingleResult();
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