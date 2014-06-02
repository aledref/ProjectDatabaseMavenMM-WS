package com.excilys.formation.webproject.core;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.joda.time.DateTime;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Computer.class)
public abstract class Computer_ {

	public static volatile SingularAttribute<Computer, Long> id;
	public static volatile SingularAttribute<Computer, Company> company;
	public static volatile SingularAttribute<Computer, String> name;
	public static volatile SingularAttribute<Computer, DateTime> introduced;
	public static volatile SingularAttribute<Computer, DateTime> discontinued;

}

