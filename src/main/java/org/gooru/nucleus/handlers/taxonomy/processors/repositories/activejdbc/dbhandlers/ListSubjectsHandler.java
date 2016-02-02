package org.gooru.nucleus.handlers.taxonomy.processors.repositories.activejdbc.dbhandlers;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;


import org.gooru.nucleus.handlers.taxonomy.processors.ProcessorContext;
import org.gooru.nucleus.handlers.taxonomy.processors.repositories.activejdbc.entities.AJEntityDefaultSubject;
import org.gooru.nucleus.handlers.taxonomy.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.gooru.nucleus.handlers.taxonomy.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.taxonomy.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.taxonomy.processors.responses.MessageResponseFactory;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ashish on 11/1/16.
 */
class ListSubjectsHandler implements DBHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ListSubjectsHandler.class);
  private final ProcessorContext context;

  public ListSubjectsHandler(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public ExecutionResult<MessageResponse> checkSanity() {
    // There should be an assessment id present
     if (context.userId() == null || context.userId().isEmpty()) {
      LOGGER.warn("Invalid user");
      return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse("Not allowed"), ExecutionResult.ExecutionStatus.FAILED);
    }
    return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
  }

  @Override
  public ExecutionResult<MessageResponse> validateRequest() {
     return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
  }

  @Override
  public ExecutionResult<MessageResponse> executeRequest() {
  
    JsonObject response = null;
    LazyList<AJEntityDefaultSubject> subjects = AJEntityDefaultSubject.findBySQL(AJEntityDefaultSubject.LISTSUBJECTS_QUERY);
    if (subjects.size() > 0) {
      LOGGER.debug("found subjects");
      response = new JsonObject().put("subjects",new JsonArray(new JsonFormatterBuilder().buildSimpleJsonFormatter(false, AJEntityDefaultSubject.LISTSUBJECTS_FIELDS).toJson(subjects)));
    } 
    if ( response == null ) {
      return new ExecutionResult<>(MessageResponseFactory.createInternalErrorResponse(), ExecutionResult.ExecutionStatus.FAILED); 
    }
 
    return new ExecutionResult<>(MessageResponseFactory.createSuccessResponse(response), ExecutionResult.ExecutionStatus.SUCCESSFUL);
  }

  @Override
  public boolean handlerReadOnly() {
    return true;
  }

}
