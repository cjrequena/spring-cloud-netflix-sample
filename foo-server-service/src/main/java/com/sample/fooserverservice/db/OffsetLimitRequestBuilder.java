package com.sample.fooserverservice.db;

import com.sample.fooserverservice.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OffsetLimitRequestBuilder implements Pageable, Serializable {
  private static final long serialVersionUID = -3493946120950974073L;
  private long offset;
  private int limit;
  private Sort sort;

  /**
   * Builder
   * @param offset
   * @param limit
   * @param sortingCriteria
   * @return
   */
  public static OffsetLimitRequestBuilder create(Integer offset, Integer limit, String sortingCriteria) {
    sortingCriteria = StringUtils.toCamelCase.apply(sortingCriteria);
    Sort sort;
    if (StringUtils.isNotEmpty(sortingCriteria)) {
      Set<String> sortingFields = new LinkedHashSet<>(Arrays.asList(StringUtils.split(sortingCriteria, ",")));
      List<Sort.Order> sortingOrders = sortingFields.stream().map(OffsetLimitRequestBuilder::getOrder).collect(Collectors.toList());
      sort = sortingOrders.isEmpty() ? Sort.unsorted() : Sort.by(sortingOrders);
    } else {
      sort = Sort.unsorted();
    }

    if (limit == null || limit == 0) {
      limit = Integer.MAX_VALUE;
    }

    return new OffsetLimitRequestBuilder(ObjectUtils.defaultIfNull(offset, 0), limit, sort);
  }

  /**
   *
   * @param value
   * @return
   */
  private static Sort.Order getOrder(String value) {
    if (StringUtils.startsWith(value, "-")) {
      return new Sort.Order(Sort.Direction.DESC, StringUtils.substringAfter(value, "-"));
    } else if (StringUtils.startsWith(value, "+")) {
      return new Sort.Order(Sort.Direction.ASC, StringUtils.substringAfter(value, "+"));
    } else {
      // Sometimes '+' from query param can be replaced as ' '
      return new Sort.Order(Sort.Direction.ASC, StringUtils.trim(value));
    }
  }

  @Override
  public int getPageNumber() {
    return 0;
  }

  @Override
  public int getPageSize() {
    return limit;
  }

  @Override
  public long getOffset() {
    return offset;
  }

  @Override
  public Sort getSort() {
    return sort;
  }

  @Override
  public Pageable next() {
    return null;
  }

  @Override
  public Pageable previousOrFirst() {
    return null;
  }

  @Override
  public Pageable first() {
    return null;
  }

  @Override
  public boolean hasPrevious() {
    return false;
  }

}
