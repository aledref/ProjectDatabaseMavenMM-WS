package com.excilys.formation.webproject.service;

import java.util.ArrayList;
import java.util.List;

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
@Service
@Transactional(readOnly=true)
public class MainServiceImpl implements MainService{

	@Autowired
	private ComputerDAO cpuDAO;
	
	@Autowired
	private CompanyDAO cpyDAO;
	
	@Transactional 
	@Override
	public Computer findComputer(Long id) {
		return cpuDAO.find(id);
	}
	
	@Transactional
	@Override
	public Long getListComputerSize() {
		return cpuDAO.getListSize();	
	}
	
	@Transactional
	@Override
	public void getListComputer(PageWrapper pageWrapper) {
		cpuDAO.getList(pageWrapper);	
	}
	
	@Transactional
	@Override
	public Long getListComputerSizeWithName(PageWrapper pageWrapper) {
		return cpuDAO.getListSizeWithName(pageWrapper);
	}
	
	@Transactional
	@Override
	public List<Computer> getListComputerWithName(PageWrapper pageWrapper) {
		return cpuDAO.getListWithName(pageWrapper);
	}
	
	@Transactional(readOnly=false)
	@Override
	public void createComputer(Computer comp) {
			cpuDAO.create(comp);
	}	
	
	@Transactional(readOnly=false)
	@Override
	public void saveComputer(Computer comp, Long id){
			cpuDAO.save(comp,id);
	}
	
	@Transactional(readOnly=false)
	@Override
	public void deleteComputer(Long id) {
			cpuDAO.delete(id);
	}
	
	@Transactional 
	@Override
	public Company findCompanyById(String id) {
		Company comp = new Company();
		Long idL = Long.decode(id);
		comp = cpyDAO.findById(idL);
		return comp; 
	}
	
	@Transactional
	@Override
	public List<Company> getListCompany() {
		return (ArrayList<Company>) cpyDAO.getList();
	}
	
	@Transactional(readOnly=false)
	@Override
	public void createCompany(Company comp) {
			cpyDAO.create(comp);
	}		
}