package org.gooru.nucleus.handlers.taxonomy.processors;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.gooru.nucleus.handlers.taxonomy.constants.MessageConstants;
import org.gooru.nucleus.handlers.taxonomy.processors.repositories.RepoBuilder;
import org.gooru.nucleus.handlers.taxonomy.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.taxonomy.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.taxonomy.processors.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;

class MessageProcessor implements Processor {

  private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);
  private final Message<Object> message;
  private String userId;
  private JsonObject prefs;
  private JsonObject request;

  public MessageProcessor(Message<Object> message) {
    this.message = message;
  }

  @Override
  public MessageResponse process() {
    MessageResponse result;
    try {
      // Validate the message itself
      ExecutionResult<MessageResponse> validateResult = validateAndInitialize();
      if (validateResult.isCompleted()) {
        return validateResult.result();
      }

      final String msgOp = message.headers().get(MessageConstants.MSG_HEADER_OP);
      switch (msgOp) {
        case MessageConstants.MSG_OP_TAXONOMY_SUBJECTS_GET : 
          result = processListOfSubjects();
          break;
        case MessageConstants.MSG_OP_TAXONOMY_COURSES_GET : 
          result = processListOfCoursesBySubject();
          break;
        case MessageConstants.MSG_OP_TAXONOMY_DOMAINS_GET : 
          result = processListOfSubDomainByCourse();
          break;
        case MessageConstants.MSG_OP_TAXONOMY_STANDARDS_GET : 
          result = processListOfStandardsBySubDomain();
          break;
       default:
          LOGGER.error("Invalid operation type passed in, not able to handle");
          return MessageResponseFactory.createInvalidRequestResponse("Invalid operation");
      }
      return result;
    } catch (Throwable e) {
      LOGGER.error("Unhandled exception in processing", e);
      return MessageResponseFactory.createInternalErrorResponse();
    }
  }

  private MessageResponse processListOfSubjects() {
    ProcessorContext context = createContext();
    return new RepoBuilder().buildTaxonomyRepo(context).getListOfSubjects();
  }

  private MessageResponse processListOfCoursesBySubject() {
    ProcessorContext context = createContext();
    if (context.subjectId() == null || context.subjectId().isEmpty()) {
      LOGGER.error("invalid Subject Id. ");
      return MessageResponseFactory.createInvalidRequestResponse("Invalid subject id");
    } 
    return new RepoBuilder().buildTaxonomyRepo(context).getListOfCoursesBySubject();
  }

  private MessageResponse processListOfSubDomainByCourse() {
    ProcessorContext context = createContext();
    if (context.subjectId() == null || context.subjectId().isEmpty()) {
      LOGGER.error("invalid Subject Id. ");
      return MessageResponseFactory.createInvalidRequestResponse("Invalid subject id");
    } 
    if (context.courseId() == null || context.courseId().isEmpty()) {
      LOGGER.error("invalid course Id. ");
      return MessageResponseFactory.createInvalidRequestResponse("Invalid course id");
    } 
   return new RepoBuilder().buildTaxonomyRepo(context).getListOfSubDomainsByCourse();
  }
 
  private MessageResponse processListOfStandardsBySubDomain() {
    ProcessorContext context = createContext();
    if (context.subjectId() == null || context.subjectId().isEmpty()) {
      LOGGER.error("invalid Subject Id. ");
      return MessageResponseFactory.createInvalidRequestResponse("Invalid subject id");
    } 
    if (context.courseId() == null || context.courseId().isEmpty()) {
      LOGGER.error("invalid course Id. ");
      return MessageResponseFactory.createInvalidRequestResponse("Invalid course id");
    } 
    if (context.domainId() == null || context.domainId().isEmpty()) {
      LOGGER.error("invalid sub domain Id. ");
      return MessageResponseFactory.createInvalidRequestResponse("Invalid sub-domain id");
    } 
    return new RepoBuilder().buildTaxonomyRepo(context).getListOfStandardsBySubDomain();
  }
 
  private ProcessorContext createContext() {
    String subjectId = message.headers().get(MessageConstants.SUBJECT_ID);
    LOGGER.debug("Subject Id : {}", subjectId);
    String courseId = message.headers().get(MessageConstants.COURSE_ID);
    LOGGER.debug("Course Id : {}", courseId);
    String domainId = message.headers().get(MessageConstants.DOMAIN_ID);
    return new ProcessorContext(userId, prefs, request, subjectId, courseId, domainId);
  }

  private ExecutionResult<MessageResponse> validateAndInitialize() {
    if (message == null || !(message.body() instanceof JsonObject)) {
      LOGGER.error("Invalid message received, either null or body of message is not JsonObject ");
      return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }

    userId = ((JsonObject) message.body()).getString(MessageConstants.MSG_USER_ID);
    if (!validateUser(userId)) {
      LOGGER.error("Invalid user id passed. Not authorized.");
      return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }

    prefs = ((JsonObject) message.body()).getJsonObject(MessageConstants.MSG_KEY_PREFS);
    request = ((JsonObject) message.body()).getJsonObject(MessageConstants.MSG_HTTP_BODY);

    if (prefs == null || prefs.isEmpty()) {
      LOGGER.error("Invalid preferences obtained, probably not authorized properly");
      return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }

    if (request == null) {
      LOGGER.error("Invalid JSON payload on Message Bus");
      return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }

    // All is well, continue processing
    return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
  }

  private boolean validateUser(String userId) {
    return !(userId == null || userId.isEmpty()) && (userId.equalsIgnoreCase(MessageConstants.MSG_USER_ANONYMOUS) || validateUuid(userId));
  }

  private boolean validateUuid(String uuidString) {
    try {
      UUID.fromString(uuidString);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    } catch (Exception e) {
      return false;
    }
  }
  
  
  
 }
