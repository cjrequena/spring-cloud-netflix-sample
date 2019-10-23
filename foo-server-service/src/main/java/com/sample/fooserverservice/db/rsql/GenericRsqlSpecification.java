package com.sample.fooserverservice.db.rsql;

import com.sample.fooserverservice.common.util.StringUtils;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import lombok.AllArgsConstructor;
import org.hibernate.query.criteria.internal.path.PluralAttributePath;
import org.hibernate.query.criteria.internal.path.SingularAttributePath;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author cjrequena
 *
 */
@AllArgsConstructor
public class GenericRsqlSpecification<T> implements Specification<T> {
  public static final String ENUM = "enum";
  private String property;
  private ComparisonOperator operator;
  private List<String> arguments;

  /**
   *
   * @param root
   * @param query
   * @param builder
   * @return
   */
  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    final Path path = parseProperty(root);
    final List<Object> arguments = castArguments(path);
    final List<Object> argumentsAux = new ArrayList<>();

    Object argument = arguments.get(0);
    Predicate predicateOp = null;
    switch (RsqlSearchOperation.getSimpleOperator(operator)) {
      case EQUAL:
        predicateOp = getPredicateEqual(builder, argument, path);
        break;
      case NOT_EQUAL:
        predicateOp = getPredicateNotEqual(builder, argument, path);
        break;
      case GREATER_THAN:
        predicateOp = getPredicateGreaterThan(builder, argument, path);
        break;
      case GREATER_THAN_OR_EQUAL:
        predicateOp = getPredicateGreaterThanOrEqual(builder, argument, path);
        break;
      case LESS_THAN:
        predicateOp = getPredicateLessThan(builder, argument, path);
        break;
      case LESS_THAN_OR_EQUAL:
        predicateOp = getPredicateLessThanOrEqual(builder, argument, path);
        break;
      case IN:
        predicateOp = getPredicateIn(builder, arguments, argumentsAux, path);
        break;
      case NOT_IN:
        predicateOp = getPredicateNotIn(builder, arguments, argumentsAux, path);
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

  /**
   *
   * @param root
   * @return
   */
  private Path<String> parseProperty(Root<T> root) {
    property = StringUtils.toCamelCase.apply(property);
    Path<String> path;
    if (property.contains(".")) {
      // Nested properties
      String[] pathSteps = property.split("\\.");
      String step = pathSteps[0];
      path = root.get(step);
      From lastFrom = root;

      for (int i = 1; i <= pathSteps.length - 1; i++) {
        if (path instanceof PluralAttributePath) {
          PluralAttribute attr = ((PluralAttributePath) path).getAttribute();
          Join join = getJoin(attr, lastFrom);
          path = join.get(pathSteps[i]);
          lastFrom = join;
        } else if (path instanceof SingularAttributePath) {
          SingularAttribute attr = ((SingularAttributePath) path).getAttribute();
          if (attr.getPersistentAttributeType() != Attribute.PersistentAttributeType.BASIC) {
            Join join = lastFrom.join(attr, JoinType.LEFT);
            path = join.get(pathSteps[i]);
            lastFrom = join;
          } else {
            path = path.get(pathSteps[i]);
          }
        } else {
          path = path.get(pathSteps[i]);
        }
      }
    } else {
      path = root.get(property);
    }
    return path;
  }

  /**
   *
   * @param attr
   * @param from
   * @return
   */
  private Join getJoin(PluralAttribute attr, From from) {
    switch (attr.getCollectionType()) {
      case COLLECTION:
        return from.join((CollectionAttribute) attr);
      case SET:
        return from.join((SetAttribute) attr);
      case LIST:
        return from.join((ListAttribute) attr);
      case MAP:
        return from.join((MapAttribute) attr);
      default:
        return null;
    }
  }

  /**
   *
   * @param path
   * @return
   */
  private List<Object> castArguments(Path<?> path) {
    Class<?> javaType = path.getJavaType();
    return arguments.stream().map(argument -> {

      if (javaType.equals(Integer.class) || javaType.equals(int.class)) {
        return Integer.parseInt(argument);
      } else if (javaType.equals(Long.class) || javaType.equals(long.class)) {
        return Long.parseLong(argument);
      } else if (javaType.equals(LocalDateTime.class)) {
        return LocalDateTime.parse(argument);
      } else if (javaType.equals(LocalDate.class)) {
        return LocalDate.parse(argument);
      } else if (javaType.equals(Boolean.class) || javaType.equals(boolean.class)) {
        return Boolean.parseBoolean(argument);
      } else if (javaType.equals(Double.class) || javaType.equals(double.class)) {
        return Double.parseDouble(argument);
      } else {
        return argument;
      }

    }).collect(Collectors.toList());
  }

  /**
   *
   * @param builder
   * @param argument
   * @param path
   * @return
   */
  private Predicate getPredicateEqual(CriteriaBuilder builder, Object argument, Path<String> path) {
    Class<? extends String> javaType = path.getJavaType();
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

  /**
   *
   * @param builder
   * @param argument
   * @param path
   * @return
   */
  private Predicate getPredicateNotEqual(CriteriaBuilder builder, Object argument, Path<String> path) {
    Class<? extends String> javaType = path.getJavaType();
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

  /**
   *
   * @param builder
   * @param argument
   * @param path
   * @return
   */
  private Predicate getPredicateGreaterThan(CriteriaBuilder builder, Object argument, Path path) {
    if (argument instanceof LocalDate) {
      return builder.greaterThan(path, (LocalDate) argument);
    } else if (argument instanceof LocalDateTime) {
      return builder.greaterThan(path, (LocalDateTime) argument);
    } else {
      return builder.greaterThan(path, argument.toString());
    }
  }

  /**
   *
   * @param builder
   * @param argument
   * @param path
   * @return
   */
  private Predicate getPredicateGreaterThanOrEqual(CriteriaBuilder builder, Object argument, Path path) {
    if (argument instanceof LocalDate) {
      return builder.greaterThanOrEqualTo(path, (LocalDate) argument);
    } else if (argument instanceof LocalDateTime) {
      return builder.greaterThanOrEqualTo(path, (LocalDateTime) argument);
    } else {
      return builder.greaterThanOrEqualTo(path, argument.toString());
    }
  }

  /**
   *
   * @param builder
   * @param argument
   * @param path
   * @return
   */
  private Predicate getPredicateLessThan(CriteriaBuilder builder, Object argument, Path path) {
    if (argument instanceof LocalDate) {
      return builder.lessThan(path, (LocalDate) argument);
    } else if (argument instanceof LocalDateTime) {
      return builder.lessThan(path, (LocalDateTime) argument);
    } else {
      return builder.lessThan(path, argument.toString());
    }
  }

  /**
   *
   * @param builder
   * @param argument
   * @param path
   * @return
   */
  private Predicate getPredicateLessThanOrEqual(CriteriaBuilder builder, Object argument, Path path) {
    if (argument instanceof LocalDate) {
      return builder.lessThanOrEqualTo(path, (LocalDate) argument);
    } else if (argument instanceof LocalDateTime) {
      return builder.lessThanOrEqualTo(path, (LocalDateTime) argument);
    } else {
      return builder.lessThanOrEqualTo(path, argument.toString());
    }
  }

  /**
   *
   * @param builder
   * @param arguments
   * @param argumentsAux
   * @param path
   * @return
   */
  private Predicate getPredicateIn(CriteriaBuilder builder, List<Object> arguments, List<Object> argumentsAux, Path<String> path) {
    Class<? extends String> type = path.getJavaType();
    if (isEnum(type)) {
      for (final Object argument : arguments) {
        argumentsAux.add(Enum.valueOf(Class.class.cast(type), (String) argument));
      }
      return path.in(argumentsAux);
    } else {
      return path.in(arguments);
    }
  }

  /**
   *
   * @param builder
   * @param arguments
   * @param argumentsAux
   * @param path
   * @return
   */
  private Predicate getPredicateNotIn(CriteriaBuilder builder, List<Object> arguments, List<Object> argumentsAux, Path<String> path) {
    Class<? extends String> type = path.getJavaType();
    if (isEnum(type)) {
      for (final Object argItem : arguments) {
        argumentsAux.add(Enum.valueOf(Class.class.cast(type), (String) argItem));
      }
      return path.in(argumentsAux);
    } else {
      return path.in(arguments);
    }
  }

  /**
   *
   * @param builder
   * @param argument
   * @param path
   * @return
   */
  private Predicate getPredicateIsNull(CriteriaBuilder builder, Object argument, Path path) {
    if (argument.toString().toLowerCase().equals("true") || argument.toString().equals("1")) {
      return builder.isNull(path);
    } else if (argument.toString().toLowerCase().equals("false") || argument.toString().equals("0")) {
      return builder.isNotNull(path);
    }
    throw new IllegalArgumentException("valid arguments: { true, false, 1, 0 }");

  }

  /**
   *
   * @param builder
   * @param argument
   * @param path
   * @return
   */
  private Predicate getPredicateIsEmpty(CriteriaBuilder builder, Object argument, Path path) {
    if (argument.toString().toLowerCase().equals("true") || argument.toString().equals("1")) {
      return builder.equal(path, "");
    } else if (argument.toString().toLowerCase().equals("false") || argument.toString().equals("0")) {
      return builder.notEqual(path, "");
    }
    throw new IllegalArgumentException("valid arguments: { true, false, 1, 0 }");
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
