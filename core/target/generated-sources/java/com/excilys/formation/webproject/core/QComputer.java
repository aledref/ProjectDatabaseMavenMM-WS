package com.excilys.formation.webproject.core;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QComputer is a Querydsl query type for Computer
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QComputer extends EntityPathBase<Computer> {

    private static final long serialVersionUID = -593271572L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QComputer computer = new QComputer("computer");

    public final QCompany company;

    public final DateTimePath<org.joda.time.DateTime> discontinued = createDateTime("discontinued", org.joda.time.DateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<org.joda.time.DateTime> introduced = createDateTime("introduced", org.joda.time.DateTime.class);

    public final StringPath name = createString("name");

    public QComputer(String variable) {
        this(Computer.class, forVariable(variable), INITS);
    }

    public QComputer(Path<? extends Computer> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QComputer(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QComputer(PathMetadata<?> metadata, PathInits inits) {
        this(Computer.class, metadata, inits);
    }

    public QComputer(Class<? extends Computer> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
    }

}

