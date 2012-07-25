package com.oreilly.springdata.querydsl.core;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QEmailAddress is a Querydsl query type for EmailAddress
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QEmailAddress extends BeanPath<EmailAddress> {

    private static final long serialVersionUID = 1501930332;

    public static final QEmailAddress emailAddress = new QEmailAddress("emailAddress");

    public final StringPath value = createString("value");

    public QEmailAddress(String variable) {
        super(EmailAddress.class, forVariable(variable));
    }

    public QEmailAddress(Path<? extends EmailAddress> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QEmailAddress(PathMetadata<?> metadata) {
        super(EmailAddress.class, metadata);
    }

}

