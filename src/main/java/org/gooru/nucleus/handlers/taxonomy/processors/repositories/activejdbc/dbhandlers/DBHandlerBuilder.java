package org.gooru.nucleus.handlers.taxonomy.processors.repositories.activejdbc.dbhandlers;

import org.gooru.nucleus.handlers.taxonomy.processors.ProcessorContext;

/**
 * Created by ashish on 11/1/16.
 */
public class DBHandlerBuilder {
  public DBHandler buildListSubjectsHandler(ProcessorContext context) {
    return new ListSubjectsHandler(context);
  }

  public DBHandler buildListCoursesBySubjectHandler(ProcessorContext context) {
    return new ListCoursesBySubjectHandler(context);

  }

  public DBHandler buildListSubDomainsByCourseHandler(ProcessorContext context) {
    return new ListSubDomainsByCourseHandler(context);
  }
  
  public DBHandler buildListStandardsBySubDomainHandler(ProcessorContext context) {
    return new ListStandardsBySubDomainHandler(context);

  }

}
