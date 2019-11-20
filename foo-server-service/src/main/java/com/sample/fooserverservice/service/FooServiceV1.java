package com.sample.fooserverservice.service;

import com.sample.fooserverservice.common.patch.PatchHelper;
import com.sample.fooserverservice.db.OffsetLimitRequestBuilder;
import com.sample.fooserverservice.db.entity.FooEntity;
import com.sample.fooserverservice.db.repository.FooRepository;
import com.sample.fooserverservice.db.rsql.CustomRsqlVisitor;
import com.sample.fooserverservice.db.rsql.RsqlSearchOperation;
import com.sample.fooserverservice.dto.FooDTOV1;
import com.sample.fooserverservice.exception.EErrorCode;
import com.sample.fooserverservice.exception.ServiceException;
import com.sample.fooserverservice.mapper.FooDtoEntityMapper;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.json.JsonMergePatch;
import javax.json.JsonPatch;
import java.util.Optional;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 *
 * @author
 * @version 1.0
 * @see
 * @since JDK1.8
 */
@Log4j2
@Service
@Transactional
public class FooServiceV1 {

  private FooDtoEntityMapper fooDtoEntityMapper;
  private FooRepository fooRepository;
  private ApplicationEventPublisher eventPublisher;
  private final PatchHelper patchHelper;
  /**
   *
   * @param fooRepository
   */
  @Autowired
  public FooServiceV1(FooRepository fooRepository, FooDtoEntityMapper fooDtoEntityMapper, ApplicationEventPublisher eventPublisher, PatchHelper patchHelper) {
    this.fooRepository = fooRepository;
    this.fooDtoEntityMapper = fooDtoEntityMapper;
    this.eventPublisher = eventPublisher;
    this.patchHelper = patchHelper;
  }

  /**
   *
   * @param dto
   * @return
   * @throws ServiceException
   */
  public FooDTOV1 create(FooDTOV1 dto) throws ServiceException {
    try {
      FooEntity entity = fooDtoEntityMapper.toEntity(dto);

      if (entity.getId() != null && fooRepository.existsById(entity.getId())) {
        throw new ServiceException(EErrorCode.CONFLICT_ERROR.getErrorCode());
      }

      entity = fooRepository.saveAndFlush(entity);
      dto = fooDtoEntityMapper.toDTO(entity);
      //eventPublisher.publishEvent(new MessageEvent<FooDTOV1>(dto, MessageEventAction.CREATE, FooChannels.EVENT_NOTIFICATION_CHANNEL));
      return dto;
    } catch (DataIntegrityViolationException ex) {
      if ((ex.getCause() != null) && (ex.getCause() instanceof ConstraintViolationException)) {
        throw new ServiceException(EErrorCode.CONFLICT_ERROR.getErrorCode(), ex.getCause());
      }
      throw ex;
    } catch (ServiceException ex) {
      log.error("{}", ex.getMessage());
      throw ex;
    } catch (Exception ex) {
      log.error("{}", ex.getMessage());
      throw new ServiceException(EErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(), ex);
    }
  }

  /**
   *
   * @param id
   * @return
   * @throws ServiceException
   */
  public FooDTOV1 retrieveById(Long id) throws ServiceException {
    //--
    try {
      Optional<FooEntity> entity = this.fooRepository.findById(id);
      if (!entity.isPresent()) {
        throw new ServiceException(EErrorCode.NOT_FOUND_ERROR.getErrorCode());
      }
      return fooDtoEntityMapper.toDTO(entity.get());
    } catch (ServiceException ex) {
      log.error("{}", ex.getMessage());
      throw ex;
    } catch (Exception ex) {
      log.error("{}", ex.getMessage());
      throw new ServiceException(EErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(), ex);
    }
    //--
  }

  /**
   *
   * @param search
   * @param offset
   * @param limit
   * @param sort
   * @return
   * @throws ServiceException
   */
  public Page retrieve(String search, String sort, Integer offset, Integer limit) throws ServiceException {
    //--
    try {
      Page<FooEntity> page;
      Specification<FooEntity> specification;
      Pageable pageable = OffsetLimitRequestBuilder.create(offset, limit, sort);

      if (search != null) {
        Node rootNode = new RSQLParser(RsqlSearchOperation.defaultOperators()).parse(search);
        specification = rootNode.accept(new CustomRsqlVisitor<>());
        page = this.fooRepository.findAll(specification, pageable);
      } else {
        page = this.fooRepository.findAll(pageable);
      }

      return page.map(entity -> fooDtoEntityMapper.toDTO(entity));
    } catch (PropertyReferenceException ex) {
      log.error("{}", ex.getMessage());
      throw new ServiceException(EErrorCode.BAD_REQUEST_ERROR.getErrorCode(), ex);
    }
    //--
  }

  /**
   *
   * @param id
   * @param dto
   * @throws ServiceException
   */
  public FooDTOV1 update(Long id, FooDTOV1 dto) throws ServiceException {
    //--
    try {
      if (!fooRepository.existsById(id)) {
        throw new ServiceException(EErrorCode.NOT_FOUND_ERROR.getErrorCode());
      }
      FooEntity entity = fooDtoEntityMapper.toEntity(dto);
      entity.setId(id);
      // Check NOT MODIFIED error
      FooEntity oldEntity = fooDtoEntityMapper.toEntity(retrieveById(id));
      if (!entity.equals(oldEntity)) {
        // Save on database
        this.fooRepository.save(entity);
      } else {
        throw new ServiceException(EErrorCode.NOT_MODIFIED_ERROR.getErrorCode(), HttpStatus.NOT_MODIFIED);
      }
      dto = fooDtoEntityMapper.toDTO(entity);
      //eventPublisher.publishEvent(new MessageEvent<FooDTOV1>(dto, MessageEventAction.UPDATE, FooChannels.EVENT_NOTIFICATION_CHANNEL));
      return dto;
    } catch (ServiceException ex) {
      log.error("{}", ex.getMessage());
      throw ex;
    } catch (Exception ex) {
      log.error("{}", ex.getMessage());
      throw new ServiceException(EErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(), ex);
    }
  }

  public FooDTOV1 patch(Long id, JsonPatch patchDocument) throws ServiceException {
    // --
    try {
      FooDTOV1 originalDTO = retrieveById(id);
      if (originalDTO == null) {
        throw new ServiceException(EErrorCode.NOT_FOUND_ERROR.getErrorCode());
      }
      FooEntity entity = fooDtoEntityMapper.toEntity(originalDTO);
      FooDTOV1 dtoPatched = patchHelper.patch(patchDocument, originalDTO, FooDTOV1.class);
      fooDtoEntityMapper.toEntity(dtoPatched, entity);
      this.fooRepository.save(entity);
      return dtoPatched;
    } catch (ServiceException ex) {
      log.error("{}", ex.getMessage());
      throw ex;
    } catch (Exception ex) {
      log.error("{}", ex.getMessage());
      throw new ServiceException(EErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(), ex);
    }
  }

  public FooDTOV1 patch(Long id, JsonMergePatch mergePatchDocument) throws ServiceException {
    try {
      FooDTOV1 fooDTO = retrieveById(id);
      if (fooDTO == null) {
        throw new ServiceException(EErrorCode.NOT_FOUND_ERROR.getErrorCode());
      }
      FooEntity entity = fooDtoEntityMapper.toEntity(fooDTO);
      FooDTOV1 dtoMergePatched = patchHelper.mergePatch(mergePatchDocument, fooDTO, FooDTOV1.class);
      fooDtoEntityMapper.toEntity(dtoMergePatched, entity);
      this.fooRepository.save(entity);
      return dtoMergePatched;
    } catch (ServiceException ex) {
      log.error("{}", ex.getMessage());
      throw ex;
    } catch (Exception ex) {
      log.error("{}", ex.getMessage());
      throw new ServiceException(EErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(), ex);
    }
  }

  /**
   *
   * @param id
   * @throws ServiceException
   */
  public void delete(Long id) throws ServiceException {
    //--
    try {
      if (!fooRepository.existsById(id)) {
        throw new ServiceException(EErrorCode.NOT_FOUND_ERROR.getErrorCode());
      }
      FooEntity entity = this.fooRepository.findById(id).get();
      FooDTOV1 dto = fooDtoEntityMapper.toDTO(entity);
      this.fooRepository.deleteById(id);
      //eventPublisher.publishEvent(new MessageEvent<FooDTOV1>(dto, MessageEventAction.DELETE, FooChannels.EVENT_NOTIFICATION_CHANNEL));
    } catch (ServiceException ex) {
      log.error("{}", ex.getMessage());
      throw ex;
    } catch (Exception ex) {
      log.error("{}", ex.getMessage());
      throw new ServiceException(EErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(), ex);
    }
  }
}
