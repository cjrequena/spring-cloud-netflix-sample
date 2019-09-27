package com.sample.fooserverservice.db.rsql;

import com.sample.fooserverservice.common.util.StringUtils;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cjrequena
 * @version 1.0
 * @see
 * @since JDK1.8
 */
@Log4j2
public class JpaGenericRsqlSpecification<T> implements Specification<T> {

  public static final String ENUM = "enum";
  private String property;
  private transient ComparisonOperator operator;
  private List<String> arguments;

  /**
   * @param property
   * @param operator
   * @param arguments
   */
  public JpaGenericRsqlSpecification(final String property, final ComparisonOperator operator, final List<String> arguments) {
    super();
    this.property = property;
    this.operator = operator;
    this.arguments = arguments;
  }

  /**
   * @param root
   * @param query
   * @param builder
   * @return
   */
  @Override
  public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
    final List<Object> args = castArguments(root);
    final List<Object> argsAux = new ArrayList<>();
    final Object argument = args.get(0);
    final Class<?> javaType = getJavaTypeOfClassContainingAttribute(root, property);
    final Path path = getPropertyPath(root, property);
    Predicate predicateOp = null;

    switch (RsqlSearchOperation.getSimpleOperator(operator)) {
      case EQUAL:
        predicateOp = getPredicateEqual(builder, argument, javaType, path);
        break;
      case NOT_EQUAL:
        predicateOp = getPredicateNotEqual(builder, argument, javaType, path);
        break;
      case GREATER_THAN:
        predicateOp = getPredicateGreater(builder, argument, path);
        break;
      case GREATER_THAN_OR_EQUAL:
        predicateOp = getPredicateGreaterEqual(builder, argument, path);
        break;
      case LESS_THAN:
        predicateOp = getPredicateLess(builder, argument, path);
        break;
      case LESS_THAN_OR_EQUAL:
        predicateOp = getPredicateLessEqual(builder, argument, path);
        break;
      case IN:
        predicateOp = getPredicateIn(args, argsAux, javaType, path);
        break;
      case NOT_IN:
        predicateOp = getPredicateNotIn(builder, args, argsAux, javaType, path);
        break;
      case ISNULL:
        predicateOp = getPredicateIsNull(builder, argument, path);
        break;
      case ISEMPTY:
        predicateOp = getPredicateIsEmpty(builder, argument, path);
        break;
      default:
        break;
    }

    return predicateOp;
  }

  private Predicate getPredicateNotIn(CriteriaBuilder builder, List<Object> args, List<Object> argsAux, Class<?> javaType, Path path) {
    if (isEnum(javaType)) {
      for (final Object argItem : args) {
        argsAux.add(Enum.valueOf(Class.class.cast(javaType), (String) argItem));
      }
      return builder.not(path.in(argsAux));
    } else {
      return builder.not(path.in(args));
    }
  }

  private Predicate getPredicateIn(List<Object> args, List<Object> argsAux, Class<?> javaType, Path path) {
    if (isEnum(javaType)) {
      for (final Object argItem : args) {
        argsAux.add(Enum.valueOf(Class.class.cast(javaType), (String) argItem));
      }
      return path.in(argsAux);
    } else {
      return path.in(args);
    }
  }

  private Predicate getPredicateLessEqual(CriteriaBuilder builder, Object argument, Path path) {
    if (argument instanceof LocalDate) {
      return builder.lessThanOrEqualTo(path, (LocalDate) argument);
    } else if (argument instanceof LocalDateTime) {
      return builder.lessThanOrEqualTo(path, (LocalDateTime) argument);
    } else {
      return builder.lessThanOrEqualTo(path, argument.toString());
    }
  }

  private Predicate getPredicateLess(CriteriaBuilder builder, Object argument, Path path) {
    if (argument instanceof LocalDate) {
      return builder.lessThan(path, (LocalDate) argument);
    } else if (argument instanceof LocalDateTime) {
      return builder.lessThan(path, (LocalDateTime) argument);
    } else {
      return builder.lessThan(path, argument.toString());
    }
  }

  private Predicate getPredicateGreaterEqual(CriteriaBuilder builder, Object argument, Path path) {
    if (argument instanceof LocalDate) {
      return builder.greaterThanOrEqualTo(path, (LocalDate) argument);
    } else if (argument instanceof LocalDateTime) {
      return builder.greaterThanOrEqualTo(path, (LocalDateTime) argument);
    } else {
      return builder.greaterThanOrEqualTo(path, argument.toString());
    }
  }

  private Predicate getPredicateGreater(CriteriaBuilder builder, Object argument, Path path) {
    if (argument instanceof LocalDate) {
      return builder.greaterThan(path, (LocalDate) argument);
    } else if (argument instanceof LocalDateTime) {
      return builder.greaterThan(path, (LocalDateTime) argument);
    } else {
      return builder.greaterThan(path, argument.toString());
    }
  }

  private Predicate getPredicateNotEqual(CriteriaBuilder builder, Object argument, Class<?> javaType, Path path) {
    if (isEnum(javaType)) {
      return builder.notEqual(path, Enum.valueOf(Class.class.cast(javaType), (String) argument));
    } else if (argument instanceof String) {
      return builder.notLike(path, argument.toString().replace('*', '%'));
    } else if (argument == null) {
      return builder.isNotNull(path);
    } else {
      return builder.notEqual(path, argument);
    }
  }

  private Predicate getPredicateEqual(CriteriaBuilder builder, Object argument, Class<?> javaType, Path path) {
    if (isEnum(javaType)) {
      return builder.equal(path, Enum.valueOf(Class.class.cast(javaType), (String) argument));
    } else if (argument instanceof String) {
      return builder.like(path, argument.toString().replace('*', '%'));
    } else if (argument == null) {
      return builder.isNull(path);
    } else {
      return builder.equal(path, argument);
    }
  }

  private Predicate getPredicateIsNull(CriteriaBuilder builder, Object argument, Path path) {
    if (argument.toString().toLowerCase().equals("true") || argument.toString().equals("1")){
      return builder.isNull(path);
    } else if (argument.toString().toLowerCase().equals("false") || argument.toString().equals("0")){
      return builder.isNotNull(path);
    }
    throw new IllegalArgumentException("valid arguments: { true, false, 1, 0 }");

  }

  private Predicate getPredicateIsEmpty(CriteriaBuilder builder, Object argument, Path path) {
    if (argument.toString().toLowerCase().equals("true") || argument.toString().equals("1")){
      return builder.equal(path, "");
    } else if (argument.toString().toLowerCase().equals("false") || argument.toString().equals("0")){
      return builder.notEqual(path, "");
    }
    throw new IllegalArgumentException("valid arguments: { true, false, 1, 0 }");
  }

  /**
   * @param root
   * @return
   */
  protected List<Object> castArguments(final Root<T> root) {
    final List<Object> args = new ArrayList<>();
    property = StringUtils.toCamelCase.apply(property);

    final Class<? extends Object> type = getJavaTypeOfClassContainingAttribute(root, property);
    for (final String argument : arguments) {
      if (type.equals(Integer.class) || type.equals(int.class)) {
        args.add(Integer.parseInt(argument));
      } else if (type.equals(Long.class) || type.equals(long.class)) {
        args.add(Long.parseLong(argument));
      } else if (type.equals(LocalDateTime.class)) {
        args.add(LocalDateTime.parse(argument));
      } else if (type.equals(LocalDate.class)) {
        args.add(LocalDate.parse(argument));
      } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
        args.add(Boolean.parseBoolean(argument));
      } else if (type.equals(Double.class) || type.equals(double.class)) {
        args.add(Double.parseDouble(argument));
      } else {
        args.add(argument);
      }
    }

    return args;
  }

  /**
   * @param root
   * @param property
   * @return
   */
  protected Class<? extends Object> getJavaTypeOfClassContainingAttribute(Root root, String property) {
    String[] propertyArr = property.split("\\.");
    int propertyLength = propertyArr.length;
    switch (propertyLength) {
      case 1:
        return root.get(propertyArr[0]).getJavaType();
      case 2:
        return root.get(propertyArr[0]).get(propertyArr[1]).getJavaType();
      case 3:
        return root.get(propertyArr[0]).get(propertyArr[1]).get(propertyArr[2]).getJavaType();
      default:
        return root.get(property).getJavaType();
    }
  }

  /**
   * @param root
   * @param property
   * @return
   */
  protected Path getPropertyPath(Root root, String property) {
    String[] propertyArr = property.split("\\.");
    int propertyLength = propertyArr.length;
    switch (propertyLength) {
      case 1:
        return root.get(propertyArr[0]);
      case 2:
        return root.get(propertyArr[0]).get(propertyArr[1]);
      case 3:
        return root.get(propertyArr[0]).get(propertyArr[1]).get(propertyArr[2]);
      default:
        return root.get(property);
    }
  }

  /**
   * @param javaType
   * @return
   */
  private boolean isEnum(Class<? extends Object> javaType) {
    String parentJavaClass = "";
    if (javaType.getSuperclass() != null) {
      parentJavaClass = javaType.getSuperclass().getSimpleName().toLowerCase();
    }
    return parentJavaClass.equals(ENUM);
  }
}
