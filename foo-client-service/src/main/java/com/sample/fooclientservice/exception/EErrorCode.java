package com.sample.fooclientservice.exception;

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
public enum EErrorCode {

  NOT_CONTENT_ERROR("FCS-ERROR-000204"),
  NOT_MODIFIED_ERROR("FCS-ERROR-000304"),
  BAD_REQUEST_ERROR("FCS-ERROR-000400"),
  NOT_FOUND_ERROR("FCS-ERROR-000404"),
  CONFLICT_ERROR("FCS-ERROR-000409"),
  INTERNAL_SERVER_ERROR("FCS-ERROR-000500"),
  EVENT_NOTIFICATION_ERROR("FCS-ERROR-001000");

  private String errorCode;

  /**
   *
   * @param errorCode
   */
  EErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  /**
   *
   * @return
   */
  public String getErrorCode() {
    return this.errorCode;
  }

}


