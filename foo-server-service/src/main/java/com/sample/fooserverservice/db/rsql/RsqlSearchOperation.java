package com.sample.fooserverservice.db.rsql;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author cjrequena
 * @version 1.0
 * @since JDK1.8
 * @see
 *
 */
public enum RsqlSearchOperation {
  EQUAL(RSQLOperators.EQUAL), //
  NOT_EQUAL(RSQLOperators.NOT_EQUAL), //
  GREATER_THAN(RSQLOperators.GREATER_THAN), //
  GREATER_THAN_OR_EQUAL(RSQLOperators.GREATER_THAN_OR_EQUAL), //
  LESS_THAN(RSQLOperators.LESS_THAN), //
  LESS_THAN_OR_EQUAL(RSQLOperators.LESS_THAN_OR_EQUAL), //
  IN(RSQLOperators.IN), //
  NOT_IN(RSQLOperators.NOT_IN), //
  ISNULL(new ComparisonOperator("=isnull=")), //
  ISEMPTY(new ComparisonOperator("=isempty=")); //

  private ComparisonOperator operator;

  RsqlSearchOperation(final ComparisonOperator operator) {
    this.operator = operator;
  }

  public static RsqlSearchOperation getSimpleOperator(final ComparisonOperator operator) {
    return Arrays.stream(values())
      .filter(operation -> operation.getOperator() == operator)
      .findAny().orElse(null);
  }

  public static Set<ComparisonOperator> defaultOperators() {
    return Arrays.stream(RsqlSearchOperation.values()).map(RsqlSearchOperation::getOperator).collect(Collectors.toSet());
  }

  public ComparisonOperator getOperator() {
    return operator;
  }
}
