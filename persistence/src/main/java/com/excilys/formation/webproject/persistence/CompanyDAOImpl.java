package com.excilys.formation.webproject.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.formation.webproject.core.Company;
import com.jolbox.bonecp.BoneCPDataSource;

/**
 * 
 * @author excilys
 *
 */
@Repository
public class CompanyDAOImpl implements CompanyDAO{

	@PersistenceContext(unitName = "computer")
	private EntityManager em;
	
	@Autowired
	private BoneCPDataSource datasource;
	
	@Override
	public Company findById(Long id) {
		if (id ==0) return null;
		String sql = "from Company cpy where cpy.id = :id";
		Query q = em.createQuery(sql);
		Company company = (Company)  q.setParameter("id",id).getSingleResult(); 
	    return company	;
	}
	
	@Override
	public Company findByName(String name) {
		if (name == "") return Company.builder().build();
		String sql = "from Company cpy where cpy.name = :name";
		Query q = em.createQuery(sql);
		Company company = (Company)  q.setParameter("name",name).getSingleResult(); 
	    return company	;
	}
	
	@Override
	public List<Company> getList() {
		String sql = "from Company";
		Query q = em.createQuery(sql);
		List<Object> list = (List<Object>) q.getResultList();
		List<Company> listCpy = new ArrayList<Company>();
		for (Object i : list) {
			listCpy.add((Company) i);
		}
		return listCpy;
	}
	
	@Override
	public void create(Company comp){
	}
}