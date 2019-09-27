package com.sample.fooserverservice.db.rsql;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author cjrequena
 * @version 1.0
 * @since JDK1.8
 * @see
 *
 */
public class JpaCustomRsqlVisitor<T> implements RSQLVisitor<Specification<T>, Void> {

    private JpaGenericRsqlSpecBuilder<T> builder;

    public JpaCustomRsqlVisitor() {
        builder = new JpaGenericRsqlSpecBuilder<>();
    }

    @Override
    public Specification<T> visit(final AndNode node, final Void param) {
        return builder.createSpecification(node);
    }

    @Override
    public Specification<T> visit(final OrNode node, final Void param) {
        return builder.createSpecification(node);
    }

    @Override
    public Specification<T> visit(final ComparisonNode node, final Void params) {
        return builder.createSpecification(node);
    }

}
