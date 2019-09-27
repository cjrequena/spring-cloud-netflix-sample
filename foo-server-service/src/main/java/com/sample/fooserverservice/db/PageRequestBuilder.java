package com.sample.fooserverservice.db;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

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
public final class PageRequestBuilder {

  /**
   * Private constructor to hide the implicit public one
   */
  private PageRequestBuilder() {
  }

  /**
   *
   * @param page
   * @param size
   * @param sortingCriteria
   * @return
   */
  public static PageRequest getPageRequest(Integer page, Integer size, String sortingCriteria) {

    Set<String> sortingFields = new LinkedHashSet<>(Arrays.asList(StringUtils.split(StringUtils.defaultIfEmpty(sortingCriteria, ""), ",")));

    List<Order> sortingOrders = sortingFields.stream().map(PageRequestBuilder::getOrder).collect(Collectors.toList());

    Sort sort = sortingOrders.isEmpty() ? null : Sort.by(sortingOrders);

    if (sort != null) {
      return PageRequest.of(ObjectUtils.defaultIfNull(page, 0), ObjectUtils.defaultIfNull(size, 20), sort);
    } else {
      return PageRequest.of(ObjectUtils.defaultIfNull(page, 0), ObjectUtils.defaultIfNull(size, 20));
    }

  }

  /**
   *
   * @param value
   * @return
   */
  private static Order getOrder(String value) {

    if (StringUtils.startsWith(value, "-")) {
      return new Order(Direction.DESC, StringUtils.substringAfter(value, "-"));
    } else if (StringUtils.startsWith(value, "+")) {
      return new Order(Direction.ASC, StringUtils.substringAfter(value, "+"));
    } else {
      // Sometimes '+' from query param can be replaced as ' '
      return new Order(Direction.ASC, StringUtils.trim(value));
    }

  }

}
