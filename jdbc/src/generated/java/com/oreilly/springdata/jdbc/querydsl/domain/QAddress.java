package com.oreilly.springdata.jdbc.querydsl.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAddress is a Querydsl query type for QAddress
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAddress extends com.mysema.query.sql.RelationalPathBase<QAddress> {

    private static final long serialVersionUID = -447360295;

    public static final QAddress address = new QAddress("ADDRESS");

    public final StringPath city = createString("CITY");

    public final StringPath country = createString("COUNTRY");

    public final NumberPath<Integer> customerId = createNumber("CUSTOMER_ID", Integer.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath street = createString("STREET");

    public final com.mysema.query.sql.PrimaryKey<QAddress> sysPk10051 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QCustomer> addressCustomerRef = createForeignKey(customerId, "ID");

    public QAddress(String variable) {
        super(QAddress.class, forVariable(variable), "PUBLIC", "ADDRESS");
    }

    public QAddress(Path<? extends QAddress> entity) {
        super(entity.getType(), entity.getMetadata(), "PUBLIC", "ADDRESS");
    }

    public QAddress(PathMetadata<?> metadata) {
        super(QAddress.class, metadata, "PUBLIC", "ADDRESS");
    }

}

