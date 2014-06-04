package com.excilys.formation.webproject.webservices;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.formation.webproject.core.Company;
import com.excilys.formation.webproject.core.Computer;
import com.excilys.formation.webproject.core.PageWrapper;
import com.excilys.formation.webproject.persistence.CompanyDAO;
import com.excilys.formation.webproject.persistence.ComputerDAO;


/**
 * 
 * @author excilys
 *
 */
@WebService(endpointInterface = "com.excilys.formation.webproject.webservices.MainServiceWS")
@Transactional(readOnly=true)
public class MainServiceWSImpl implements MainServiceWS{

	@Autowired
	private ComputerDAO cpuDAO;
	
	@Autowired
	private CompanyDAO cpyDAO;
	
	@WebMethod(operationName="findComputer")
	@Transactional 
	@Override
	public Computer findComputer(Long id) {
		return cpuDAO.find(id);
	}
	
	@WebMethod
	@Transactional
	@Override
	public Long getListComputerSize() {
		return cpuDAO.getListSize();	
	}
	
	@WebMethod
	@Transactional
	@Override
	public void getListComputer(PageWrapper pageWrapper) {
		cpuDAO.getList(pageWrapper);	
	}
	
	@WebMethod
	@Transactional
	@Override
	public Long getListComputerSizeWithName(PageWrapper pageWrapper) {
		return cpuDAO.getListSizeWithName(pageWrapper);
	}
	
	@WebMethod
	@Transactional
	@Override
	public List<Computer> getListComputerWithName(PageWrapper pageWrapper) {
		return cpuDAO.getListWithName(pageWrapper);
	}
	
	@WebMethod
	@Transactional(readOnly=false)
	@Override
	public void createComputer(Computer comp) {
			cpuDAO.create(comp);
	}	
	
	@WebMethod
	@Transactional(readOnly=false)
	@Override
	public void saveComputer(Computer comp, Long id){
			cpuDAO.save(comp,id);
	}
	
	@WebMethod
	@Transactional(readOnly=false)
	@Override
	public void deleteComputer(Long id) {
			cpuDAO.delete(id);
	}
	
	@WebMethod
	@Transactional 
	@Override
	public Company findCompanyById(String id) {
		Company comp = new Company();
		Long idL = Long.decode(id);
		comp = cpyDAO.findById(idL);
		return comp; 
	}
	
	@WebMethod
	@Transactional
	@Override
	public List<Company> getListCompany() {
		return (ArrayList<Company>) cpyDAO.getList();
	}
	
	@WebMethod
	@Transactional(readOnly=false)
	@Override
	public void createCompany(Company comp) {
			cpyDAO.create(comp);
	}		
}