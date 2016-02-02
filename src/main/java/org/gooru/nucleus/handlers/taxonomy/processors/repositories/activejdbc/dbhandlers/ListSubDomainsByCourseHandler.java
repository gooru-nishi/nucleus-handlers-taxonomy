package org.gooru.nucleus.handlers.taxonomy.processors.repositories.activejdbc.dbhandlers;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.gooru.nucleus.handlers.taxonomy.processors.ProcessorContext;
import org.gooru.nucleus.handlers.taxonomy.processors.repositories.activejdbc.entities.AJEntityDefaultCourse;
import org.gooru.nucleus.handlers.taxonomy.processors.repositories.activejdbc.entities.AJEntityDefaultSubDomain;
import org.gooru.nucleus.handlers.taxonomy.processors.repositories.activejdbc.entities.AJEntityDefaultSubject;
import org.gooru.nucleus.handlers.taxonomy.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.gooru.nucleus.handlers.taxonomy.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.taxonomy.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.taxonomy.processors.responses.MessageResponseFactory;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ListSubDomainsByCourseHandler implements DBHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ListSubDomainsByCourseHandler.class);
  private final ProcessorContext context;

  public ListSubDomainsByCourseHandler(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public ExecutionResult<MessageResponse> checkSanity() {
    if (context.subjectId() == null) {
      LOGGER.error("checkSanity() failed. Subject Id is null!");
      return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(), ExecutionResult.ExecutionStatus.FAILED);
    } else if (context.subjectId().isEmpty()) {
      LOGGER.error("checkSanity() failed. subjectId is empty!");
      return new ExecutionResult<>(MessageResponseFactory.createNotFoundResponse("The queried Subject Id is not found in the DB."), ExecutionResult.ExecutionStatus.FAILED);
    }
    if (context.courseId() == null) {
      LOGGER.error("checkSanity() failed. Course Id is null!");
      return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(), ExecutionResult.ExecutionStatus.FAILED);
    } else if (context.courseId().isEmpty()) {
      LOGGER.error("checkSanity() failed. Course Id is empty!");
      return new ExecutionResult<>(MessageResponseFactory.createNotFoundResponse("The queried Course Id is not found in the DB."), ExecutionResult.ExecutionStatus.FAILED);
    }
    // we need some valid user -- anonymous will also do
    if (context.userId() == null || context.userId().isEmpty()) {
      return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse("Invalid user context"), ExecutionResult.ExecutionStatus.FAILED);
    }

    LOGGER.debug("checkSanity() passed");
    return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
  }

  @Override
  public ExecutionResult<MessageResponse> validateRequest() {
    
    if (!AJEntityDefaultSubject.exists(Long.parseLong(context.subjectId()))) {
      LOGGER.debug("validateRequest() failed");
      return new ExecutionResult<>(MessageResponseFactory.createNotFoundResponse("The queried Subject Id is not found in the DB."), ExecutionResult.ExecutionStatus.FAILED);
    }
    
    if (!AJEntityDefaultCourse.exists(Long.parseLong(context.courseId()))) {
      LOGGER.debug("validateRequest() failed");
      return new ExecutionResult<>(MessageResponseFactory.createNotFoundResponse("The queried Course Id is not found in the DB."), ExecutionResult.ExecutionStatus.FAILED);
    }
    
    LazyList<AJEntityDefaultCourse> course = AJEntityDefaultCourse.findBySQL(AJEntityDefaultCourse.FETCHBYCOURSEANDSUBJECT_QUERY, Long.parseLong(context.courseId()), Long.parseLong(context.subjectId()));
    if (course.size() <= 0) {
      LOGGER.debug("found association");
      return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse("Course and Subject association not found"), ExecutionResult.ExecutionStatus.FAILED);
    } 
    LOGGER.debug("validateRequest() passed");
    return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
  }

  @Override
  public ExecutionResult<MessageResponse> executeRequest() {
    JsonObject response = null;
    LazyList<AJEntityDefaultSubDomain> subdomains = AJEntityDefaultSubDomain.findBySQL(AJEntityDefaultSubDomain.LISTSUBDOMAINS_QUERY, Long.parseLong(context.courseId()));
    if (subdomains.size() > 0) {
      LOGGER.debug("found courses");
      response = new JsonObject().put("subdomains",new JsonArray(new JsonFormatterBuilder().buildSimpleJsonFormatter(false, AJEntityDefaultSubDomain.LISTSUBDOMAINS_FIELDS).toJson(subdomains)));
    } 
    if ( response == null ) {
      return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse("No subdomains found for this course and subject."), ExecutionResult.ExecutionStatus.FAILED); 
    }
    return new ExecutionResult<>(MessageResponseFactory.createSuccessResponse(response), ExecutionResult.ExecutionStatus.SUCCESSFUL);
  }

  @Override
  public boolean handlerReadOnly() {
    return true;
  }

}
