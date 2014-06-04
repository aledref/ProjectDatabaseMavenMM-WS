package com.excilys.formation.webproject.webservices;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.excilys.formation.webproject.core.Company;
import com.excilys.formation.webproject.core.Computer;
import com.excilys.formation.webproject.core.PageWrapper;

/**
 * 
 * @author excilys
 *
 */
@WebService
public interface MainServiceWS {
	/**
	 * @return the Computer in the table computer matching the id
	 */
	@WebMethod(operationName="findComputer")
	public Computer findComputer(Long id);
	/**
	 * 
	 * @return the size of the table computer
	 */
	@WebMethod
	public Long getListComputerSize();
	/**
	 * 
	 * @param pagewrapper An object countaining the info for the next query
	 */
	@WebMethod
	public void getListComputer(PageWrapper pageWrapper); 
	/**
	 * 
	 * @param pageWrapper
	 * @return the size of the List<Computer> of Computer in the table computer to be displayed
	 */
	@WebMethod
	public Long getListComputerSizeWithName(PageWrapper pageWrapper);
	/**
	 * 
	 * @param pageWrapper
	 * @return a List<Computer> of Computer in the table computer to be displayed
	 */
	@WebMethod
	public List<Computer> getListComputerWithName(PageWrapper pageWrapper);
	/**
	 * 
	 * @param comp A Computer to be put in the table computer to be displayed
	 */
	@WebMethod
	public void createComputer(Computer comp);
	/**
	 * 
	 * @param comp A Computer to be edited in the table computer
	 * @param id The id of the edited Computer
	 */
	@WebMethod
	public void saveComputer(Computer comp,Long id);
	/**
	 * 
	 * @param id The id of the Computer to be removed in the table computer
	 */
	@WebMethod
	public void deleteComputer(Long id);
	/**
	 * @return the Company in the table company matching the id
	 */
	@WebMethod
	public Company findCompanyById(String id);
	/**
	 * 
	 * @return a List<Company> of every Company in the table company
	 */
	@WebMethod
	public List<Company> getListCompany();
	/**
	 * 
	 * @param comp A Computer to be put in the table company
	 */
	@WebMethod
	public void createCompany(Company comp);
}