package org.gooru.nucleus.handlers.taxonomy.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.taxonomy.processors.ProcessorContext;
import org.gooru.nucleus.handlers.taxonomy.processors.repositories.activejdbc.dbhandlers.DBHandlerBuilder;
import org.gooru.nucleus.handlers.taxonomy.processors.repositories.activejdbc.transactions.TransactionExecutor;
import org.gooru.nucleus.handlers.taxonomy.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.taxonomy.processors.repositories.TaxonomyRepo;



public class AJTaxonomyRepo implements TaxonomyRepo {

 // private static final String DB_DEFAULT = "taxonomy_lookup";
  private final ProcessorContext context;
  
  public AJTaxonomyRepo(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public MessageResponse getListOfSubjects() {
    return new TransactionExecutor().executeTransaction(new DBHandlerBuilder().buildListSubjectsHandler(context));
  }

  @Override
  public MessageResponse getListOfCoursesBySubject() {
    return new TransactionExecutor().executeTransaction(new DBHandlerBuilder().buildListCoursesBySubjectHandler(context));
  }
  
  @Override
  public MessageResponse getListOfSubDomainsByCourse() {
    return new TransactionExecutor().executeTransaction(new DBHandlerBuilder().buildListSubDomainsByCourseHandler(context));
  }
  
  @Override
  public MessageResponse getListOfStandardsBySubDomain() {
    return new TransactionExecutor().executeTransaction(new DBHandlerBuilder().buildListStandardsBySubDomainHandler(context));
  }
}
