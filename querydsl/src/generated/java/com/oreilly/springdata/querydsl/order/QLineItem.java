package com.oreilly.springdata.querydsl.order;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QLineItem is a Querydsl query type for LineItem
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QLineItem extends EntityPathBase<LineItem> {

    private static final long serialVersionUID = -2069159054;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QLineItem lineItem = new QLineItem("lineItem");

    public final com.oreilly.springdata.querydsl.core.QAbstractEntity _super = new com.oreilly.springdata.querydsl.core.QAbstractEntity(this);

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final com.oreilly.springdata.querydsl.core.QProduct product;

    public QLineItem(String variable) {
        this(LineItem.class, forVariable(variable), INITS);
    }

    public QLineItem(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QLineItem(PathMetadata<?> metadata, PathInits inits) {
        this(LineItem.class, metadata, inits);
    }

    public QLineItem(Class<? extends LineItem> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new com.oreilly.springdata.querydsl.core.QProduct(forProperty("product")) : null;
    }

}

