package com.oreilly.springdata.querydsl.core;


import static com.mysema.query.types.PathMetadataFactory.*;
import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAbstractEntity is a Querydsl query type for AbstractEntity
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QAbstractEntity extends EntityPathBase<AbstractEntity> {

    private static final long serialVersionUID = 341919817;

    public static final QAbstractEntity abstractEntity = new QAbstractEntity("abstractEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QAbstractEntity(String variable) {
        super(AbstractEntity.class, forVariable(variable));
    }

    public QAbstractEntity(Path<? extends AbstractEntity> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QAbstractEntity(PathMetadata<?> metadata) {
        super(AbstractEntity.class, metadata);
    }

}

