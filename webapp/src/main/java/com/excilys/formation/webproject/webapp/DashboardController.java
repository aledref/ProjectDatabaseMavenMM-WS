package com.excilys.formation.webproject.webapp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.formation.webproject.binding.Decoder;
import com.excilys.formation.webproject.core.Computer;
import com.excilys.formation.webproject.core.PageWrapper;
import com.excilys.formation.webproject.service.MainService;


@Controller
@RequestMapping(value="/dashboard")
public class DashboardController {

	@Autowired
	private MainService mainService;
	
	@Autowired
	private Decoder decoder;

	/**
	 * 
	 * @param nameFilter
	 * @param fieldOrder
	 * @param order
	 * @param pageNumberS
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String getDashboard(@RequestParam(value="nameFilter",defaultValue="") String nameFilter, 
			@RequestParam(value="orderNumber",defaultValue="0") String orderNumber,
			@RequestParam(value="pageNumber",defaultValue="1") String pageNumberS,
			Model model) {		
		
		PageWrapper pageWrapper = null;
		
		//Search OFF
		if ( (nameFilter.isEmpty()) || (nameFilter == null) ) {

			if (nameFilter == null) nameFilter = "";

			//3-computerListSize
			Long computerListSize = mainService.getListComputerSize();

			//4-pageNumber

			Integer pageNumber = null;
			if (pageNumberS.equals("last")) {
				pageNumber = (int) Math.ceil(computerListSize / 25.0);	
				if (pageNumber == 0) pageNumber = 1;
			}else if (!pageNumberS.matches("^[0-9]*$")) {
				pageNumber = 1;
			}else pageNumber = (int) Double.parseDouble(pageNumberS);					

			//Build with all except computerList,namefilter
			pageWrapper = PageWrapper.builder().pageNumber(pageNumber).computerListSize(computerListSize).build();
			
			//Resolve Order
			pageWrapper.setOrderNumber(orderNumber);
			decoder.fromCode(pageWrapper);

			//5-Set the computerList
			mainService.getListComputer(pageWrapper);

			//Search ON
		}else {		

			//Build partial pageWrapper, countains nameFilter
			pageWrapper = PageWrapper.builder().nameFilter(nameFilter).build();
			//3-computerListSize
			Long computerListSize = mainService.getListComputerSizeWithName(pageWrapper);

			//4-pageNumber
			Integer pageNumber = null;
			if (pageNumberS == null) pageNumber = 1;
			else if ((pageNumberS.equals("last"))) {
				pageNumber = (int) Math.ceil(computerListSize / 25.0);	
				if (pageNumber == 0) pageNumber = 1;
			}else if (!pageNumberS.matches("^[0-9]*$")) {
				pageNumber = 1;
			}else pageNumber = (int) Double.parseDouble(pageNumberS);

			//Build with 5-nameFilter, countains all except except computerList
			pageWrapper = PageWrapper.builder().nameFilter(nameFilter).pageNumber(pageNumber).computerListSize(computerListSize).build();
			
			//Resolve Order
			pageWrapper.setOrderNumber(orderNumber);
			decoder.fromCode(pageWrapper);
			
			//5-Set the computerList			
			List<Computer> computerList = mainService.getListComputerWithName(pageWrapper); 

			//Build complete PageWrapper
			pageWrapper = PageWrapper.builder().nameFilter(nameFilter).pageNumber(pageNumber).computerList(computerList).computerListSize(computerListSize).build();		
		}

		//Set the PageWrapper
		model.addAttribute("pageWrapper", pageWrapper);

		return "dashboard";
	}
}
