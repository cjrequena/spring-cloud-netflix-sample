package com.sample.fooserverservice.db.repository;

import com.sample.fooserverservice.db.entity.FooEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 * @author cjrequena
 * @version 1.0
 * @since JDK1.8
 * @see
 *
 */
@Repository
public interface FooRepository extends JpaRepository<FooEntity, Long>, JpaSpecificationExecutor<FooEntity> {
}
