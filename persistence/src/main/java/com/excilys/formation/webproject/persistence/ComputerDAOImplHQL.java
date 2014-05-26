package com.excilys.formation.webproject.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class ComputerDAOImplHQL implements ComputerDAO{

	final Logger logger = LoggerFactory.getLogger(ComputerDAOImplHQL.class);
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

    public void setDataSource(BoneCPDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	@Autowired
	private BoneCPDataSource dataSource;
	
	@Autowired
	private Closer closer;
	
	@Autowired
	private ComputerRowMapper cpuRowMapper;
	
	@Override
	public Computer find(Long id) {

		String sql = "SELECT cpu.id,cpu.name,cpu.introduced,cpu.discontinued,cpu.company_id,cpy.name FROM computer AS cpu "
					+"LEFT OUTER JOIN company AS cpy ON cpu.company_id = cpy.id WHERE cpu.id = ?";
		Computer computer = (Computer)em.createQuery(sql).setParameter("id", id).getSingleResult(); 
	    return computer	;
	}
	
	@Override
	public Integer getListSize() {
		String sql = "SELECT COUNT(*) as computerlistsize FROM computer";
		return jdbcTemplate.queryForObject(sql,Integer.class);
	}
	
	@Override
	public void getList(PageWrapper pageWrapper) {
		String sql = new StringBuilder("SELECT DISTINCT cpu.id,cpu.name,cpu.introduced,cpu.discontinued,cpu.company_id,cpy.name "
				+ "FROM computer AS cpu LEFT OUTER JOIN company AS cpy ON cpu.company_id = cpy.id ORDER BY ")
				.append(pageWrapper.getFieldOrder()).append(" ")
				.append(pageWrapper.getOrder()).append(", cpu.name ASC LIMIT ?,?").toString();
		Object[] obj = new Object[]{pageWrapper.getPerPage()*(pageWrapper.getPageNumber()-1),pageWrapper.getPerPage()};
		List<Computer> list = jdbcTemplate.query(sql,obj,cpuRowMapper);
		pageWrapper.setComputerList(list);
	}
	
	@Override
	public Integer getListSizeWithName(PageWrapper pageWrapper) {	
		String sql = "SELECT COUNT(*) AS computerListSize FROM computer AS cpu LEFT OUTER JOIN company AS cpy "
				+ "ON cpu.company_id = cpy.id WHERE cpu.name LIKE ? OR cpy.name LIKE ?";
				String namefilter = new StringBuilder("%").append(pageWrapper.getNameFilter()).append("%").toString();
		return jdbcTemplate.queryForObject(sql,new Object[]{namefilter,namefilter},Integer.class);
	}
	
	
	@Override
	public List<Computer> getListWithName(PageWrapper pageWrapper) {	
		String namefilter = new StringBuilder("%").append(pageWrapper.getNameFilter()).append("%").toString();
		String sql = new StringBuilder("SELECT cpu.id,cpu.name,cpu.introduced,cpu.discontinued,cpu.company_id,cpy.name FROM computer AS cpu "
				+ "LEFT OUTER JOIN company AS cpy ON cpu.company_id = cpy.id WHERE cpu.name LIKE ? OR cpy.name LIKE ? ")
				.append(pageWrapper.getFieldOrder()).append(" ")
				.append(pageWrapper.getOrder()).append(", cpu.name ASC LIMIT ?,?").toString();
		Object[] obj = new Object[]{namefilter,namefilter,pageWrapper.getPerPage()*(pageWrapper.getPageNumber()-1),pageWrapper.getPerPage()};
		return jdbcTemplate.query(sql,obj,cpuRowMapper);
	}

	@Override
	public void create(Computer comp) {
		EntityTransaction tx = em.getTransaction();
		Query query = null;
	    tx.begin();
	    String idCompany = String.valueOf(comp.getCompany().getId());
	    if (idCompany == null) {
	    	query = em.createQuery("INSERT into computer(name,introduced,discontinued) VALUES (?,?,?)");
	    	query.setParameter("name", comp.getName());
	    	query.setParameter("introduced", comp.getIntroduced());
	    	query.setParameter("discontinued", comp.getDiscontinued());
	    } else {
	    	query = em.createQuery("INSERT into computer(name,introduced,discontinued,company_id) VALUES (?,?,?,?)");
	    	query.setParameter("name", comp.getName());
	    	query.setParameter("introduced", comp.getIntroduced());
	    	query.setParameter("discontinued", comp.getDiscontinued());
	    	query.setParameter("company_id", idCompany);	
	    }
	    query.executeUpdate();
	    tx.commit();
	}
	
	@Override
	public void save(Computer comp,Long id) {
		EntityTransaction tx = em.getTransaction();
		Query query = null;
	    tx.begin();
	    String idCompany = String.valueOf(comp.getCompany().getId());
	    if (idCompany == null) {
	    	query = em.createQuery("UPDATE computer SET name=?, introduced=?, discontinued=? WHERE id = ?");
	    	query.setParameter("name", comp.getName());
	    	query.setParameter("introduced", comp.getIntroduced());
	    	query.setParameter("discontinued", comp.getDiscontinued());
	    	query.setParameter("id", id);
	    } else {
	    	query = em.createQuery("UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE id = ?");
	    	query.setParameter("name", comp.getName());
	    	query.setParameter("introduced", comp.getIntroduced());
	    	query.setParameter("discontinued", comp.getDiscontinued());
	    	query.setParameter("company_id", idCompany);
	    	query.setParameter("id", id);		
	    }
	    query.executeUpdate();
	    tx.commit();
	}
	
	@Override
	public void delete(Long id) {    
	    EntityTransaction tx = em.getTransaction();
	    tx.begin();
	    em.createQuery("DELETE FROM computer WHERE id = ?").setParameter("id", id).executeUpdate();
	    tx.commit();
	 }	
}