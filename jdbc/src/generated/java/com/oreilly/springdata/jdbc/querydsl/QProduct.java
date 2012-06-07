package com.oreilly.springdata.jdbc.querydsl;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QProduct is a Querydsl query type for QProduct
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QProduct extends com.mysema.query.sql.RelationalPathBase<QProduct> {

    private static final long serialVersionUID = -1237390766;

    public static final QProduct product = new QProduct("PRODUCT");

    public final StringPath description = createString("DESCRIPTION");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath name = createString("NAME");

    public final NumberPath<java.math.BigDecimal> price = createNumber("PRICE", java.math.BigDecimal.class);

    public final com.mysema.query.sql.PrimaryKey<QProduct> sysPk10030 = createPrimaryKey(id);

    public QProduct(String variable) {
        super(QProduct.class, forVariable(variable), "PUBLIC", "PRODUCT");
    }

    public QProduct(Path<? extends QProduct> entity) {
        super(entity.getType(), entity.getMetadata(), "PUBLIC", "PRODUCT");
    }

    public QProduct(PathMetadata<?> metadata) {
        super(QProduct.class, metadata, "PUBLIC", "PRODUCT");
    }

}

