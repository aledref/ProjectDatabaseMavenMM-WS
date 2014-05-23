package com.excilys.formation.webproject.persistence;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
	
	@Autowired
	private SessionFactory sessionF;
	
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
		
		Computer computer = jdbcTemplate.queryForObject(sql, new Object[]{String.valueOf(id)},cpuRowMapper);
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
		String sql = null;	
		Object[] obj = null;
		Long idCompany = comp.getCompany().getId();
		if (idCompany == null) {
			sql = new String("INSERT into computer(name,introduced,discontinued) VALUES (?,?,?)");
			obj = new Object[]{comp.getName(),comp.getIntroduced(),comp.getDiscontinued()};
		} else {
			sql = new String("INSERT into computer(name,introduced,discontinued,company_id) VALUES (?,?,?,?)");
			obj = new Object[]{comp.getName(),comp.getIntroduced(),comp.getDiscontinued(),idCompany};	
		}
		jdbcTemplate.update(sql,obj);
	}
	
	@Override
	public void save(Computer comp,Long id) {
		String sql = null;	
		Object[] obj = null;
		String idCompany = String.valueOf(comp.getCompany().getId());
		if (idCompany == null) {
			sql = new String("UPDATE computer SET name=?, introduced=?, discontinued=? WHERE id = ?");
			obj = new Object[]{String.valueOf(comp.getName()),String.valueOf(comp.getIntroduced()),String.valueOf(comp.getDiscontinued()),id};
		} else {
			sql = new String("UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE id = ?");
			obj = new Object[]{comp.getName(),comp.getIntroduced(),comp.getDiscontinued(),idCompany,id};	
		}
		jdbcTemplate.update(sql,obj);
	}
	/*
	@Override
	public void delete(Long id) {
		String sql = "DELETE FROM computer WHERE id = ?";
		Object[] obj = new Object[]{id};
		jdbcTemplate.update(sql,obj);
	}*/
	
	@Override
	public void delete(Long id) {
	//Declaration d'un objet Transaction
		Session session =null;
	    Transaction tx=null;
	    
	    try{
	    	//obtention de session hibernate 
	    	session = sessionF.getCurrentSession();
	    			
	    	//debut transaction
	    	tx = session.beginTransaction();	
	    			
	    	//update
	    	session.createQuery("DELETE FROM computer WHERE id = ?").setParameter("id", id).executeUpdate();
 
	    	//validation de la transaction
	    	tx.commit();    
	    	
	        //fermeture session
	    	sessionF.close();
	    }catch(Exception e){
	        System.out.println("erreur de la transaction"+e.getMessage());
	        tx.rollback();
	    }
	    System.out.println("Im done !!!");
	 }
	
}