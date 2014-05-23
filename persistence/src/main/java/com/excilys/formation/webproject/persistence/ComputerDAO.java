package com.excilys.formation.webproject.persistence;

import java.sql.SQLException;
import java.util.List;

import com.excilys.formation.webproject.core.Computer;
import com.excilys.formation.webproject.core.PageWrapper;


/**
 * 
 * @author excilys
 *
 */
public interface ComputerDAO{

	/**
	 * @return The Computer in the table computer matching the id
	 */
	public Computer find(Long id);
	/**
	 * 
	 * @return
	 */
	public Integer getListSize();	
	/**
	 * 
	 * @param pagewrapper
	 * @return
	 */
	public void getList(PageWrapper pagewrapper);
	/**
	 * 
	 * @param pageWrapper
	 * @return
	 */
	public Integer getListSizeWithName(PageWrapper pageWrapper);
	/**
	 * 
	 * @param pageWrapper
	 * @return
	 */
	public List<Computer> getListWithName(PageWrapper pageWrapper);
	/**
	 * 
	 * @param cn
	 * @param comp
	 * @throws SQLException
	 */
	public void create(Computer comp);
	/**
	 * 
	 * @param cn
	 * @param comp
	 * @param id
	 * @throws SQLException
	 */
	public void save(Computer comp,Long id);
	/**
	 * 
	 * @param cn
	 * @param id
	 * @throws SQLException
	 */
	public void delete(Long id);
}