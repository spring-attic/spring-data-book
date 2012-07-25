package com.oreilly.springdata.querydsl.core;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAddress is a Querydsl query type for Address
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QAddress extends EntityPathBase<Address> {

    private static final long serialVersionUID = -1204895024;

    public static final QAddress address = new QAddress("address");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final StringPath city = createString("city");

    public final StringPath country = createString("country");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath street = createString("street");

    public QAddress(String variable) {
        super(Address.class, forVariable(variable));
    }

    public QAddress(Path<? extends Address> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QAddress(PathMetadata<?> metadata) {
        super(Address.class, metadata);
    }

}

