package com.sample.fooserverservice.db.rsql;

import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.LogicalNode;
import cz.jirutka.rsql.parser.ast.LogicalOperator;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cjrequena
 * @version 1.0
 * @since JDK1.8
 * @see
 *
 */
public class GenericRsqlSpecBuilder<T> {

  /**
   *
   * @param node
   * @return
   */
  public Specification<T> createSpecification(final Node node) {
    if (node instanceof LogicalNode) {
      return createSpecification((LogicalNode) node);
    }
    if (node instanceof ComparisonNode) {
      return createSpecification((ComparisonNode) node);
    }
    return null;
  }

  /**
   *
   * @param logicalNode
   * @return
   */
  public Specification<T> createSpecification(final LogicalNode logicalNode) {
    final List<Specification<T>> specs = new ArrayList<>();
    Specification<T> temp;
    for (final Node node : logicalNode.getChildren()) {
      temp = createSpecification(node);
      if (temp != null) {
        specs.add(temp);
      }
    }

    Specification<T> result = specs.get(0);

    if (logicalNode.getOperator() == LogicalOperator.AND) {
      for (int i = 1; i < specs.size(); i++) {
        result = Specification.where(result).and(specs.get(i));
      }
    } else if (logicalNode.getOperator() == LogicalOperator.OR) {
      for (int i = 1; i < specs.size(); i++) {
        result = Specification.where(result).or(specs.get(i));
      }
    }

    return result;
  }

  /**
   *
   * @param comparisonNode
   * @return
   */
  public Specification<T> createSpecification(final ComparisonNode comparisonNode) {
    //return Specification.where(new JpaGenericRsqlSpecification<T>(comparisonNode.getSelector(), comparisonNode.getOperator(), comparisonNode.getArguments()));
    return Specification.where(new GenericRsqlSpecification<>(comparisonNode.getSelector(), comparisonNode.getOperator(), comparisonNode.getArguments()));
  }

}
