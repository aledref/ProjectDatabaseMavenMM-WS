package com.excilys.formation.webproject.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.formation.webproject.core.Company;
import com.excilys.formation.webproject.core.Computer;

@Component
public class ComputerRowMapper implements RowMapper<Computer>{
	
	final Logger logger = LoggerFactory.getLogger(ComputerDAOImpl.class);
	
	@Override
	public Computer mapRow(ResultSet set, int row) throws SQLException {
           Computer computer = new Computer();
           computer.setId(set.getLong("id"));
           String name = set.getString(2);
           computer.setName(name);  
           try {
           DateTime introducedTotal = new DateTime(set.getTimestamp("introduced"));
           DateTimeFormatter Ifmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		   String strIDateOnly = Ifmt.print(introducedTotal);
           computer.setIntroduced(new DateTime(strIDateOnly));
           } catch (SQLException e) {
        	   logger.info("Timestamp introduced Null on " + name);
           }
           try {
           DateTime discontinuedTotal = new DateTime(set.getTimestamp("discontinued"));
		   DateTimeFormatter dFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		   String strDDateOnly = dFmt.print(discontinuedTotal);
		   computer.setDiscontinued(new DateTime(strDDateOnly));
		   } catch (SQLException e) {
			   logger.info("Timestamp discontinued Null on " + name);
		   }
           computer.setCompany(new Company.CpyBuilder().id(new Long(set.getLong("company_id"))).name(set.getString(6)).build());
           return computer;
        }
}
