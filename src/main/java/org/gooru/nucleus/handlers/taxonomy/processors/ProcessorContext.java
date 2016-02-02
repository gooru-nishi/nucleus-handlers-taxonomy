package org.gooru.nucleus.handlers.taxonomy.processors;

import io.vertx.core.json.JsonObject;

/**
 * Created by ashish on 7/1/16.
 */
public class ProcessorContext {

  final private String userId;
  final private JsonObject prefs;
  final private JsonObject request;
  final private String subjectId;
  final private String courseId;
  final private String domainId;

  public ProcessorContext(String userId, JsonObject prefs, JsonObject request, String subjectId, String courseId, String domainId) {
    if (prefs == null || userId == null || prefs.isEmpty()) {
      throw new IllegalStateException("Processor Context creation failed because of invalid values");
    }
    this.userId = userId;
    this.prefs = prefs.copy();
    this.request = request != null ? request.copy() : null;
    this.subjectId = subjectId;
    this.courseId = courseId;
    this.domainId = domainId;
  }

  public String userId() {
    return this.userId;
  }

  public JsonObject prefs() {
    return this.prefs.copy();
  }

  public JsonObject request() {
    return this.request;
  }

  public String subjectId() {
    return this.subjectId;
  }

  public String courseId() {
    return this.courseId;
  }
  
  public String domainId() {
    return this.domainId;
  }

}
