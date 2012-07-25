package com.oreilly.springdata.querydsl.order;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = -1386117917;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QOrder order = new QOrder("order");

    public final com.oreilly.springdata.querydsl.core.QAbstractEntity _super = new com.oreilly.springdata.querydsl.core.QAbstractEntity(this);

    public final com.oreilly.springdata.querydsl.core.QAddress billingAddress;

    public final com.oreilly.springdata.querydsl.core.QCustomer customer;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final SetPath<LineItem, QLineItem> lineItems = this.<LineItem, QLineItem>createSet("lineItems", LineItem.class, QLineItem.class);

    public final com.oreilly.springdata.querydsl.core.QAddress shippingAddress;

    public QOrder(String variable) {
        this(Order.class, forVariable(variable), INITS);
    }

    public QOrder(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QOrder(PathMetadata<?> metadata, PathInits inits) {
        this(Order.class, metadata, inits);
    }

    public QOrder(Class<? extends Order> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.billingAddress = inits.isInitialized("billingAddress") ? new com.oreilly.springdata.querydsl.core.QAddress(forProperty("billingAddress")) : null;
        this.customer = inits.isInitialized("customer") ? new com.oreilly.springdata.querydsl.core.QCustomer(forProperty("customer"), inits.get("customer")) : null;
        this.shippingAddress = inits.isInitialized("shippingAddress") ? new com.oreilly.springdata.querydsl.core.QAddress(forProperty("shippingAddress")) : null;
    }

}

