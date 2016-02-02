package org.gooru.nucleus.handlers.taxonomy.processors.repositories;

import org.gooru.nucleus.handlers.taxonomy.processors.responses.MessageResponse;

public interface TaxonomyRepo {
  
  MessageResponse getListOfSubjects();
  
  MessageResponse getListOfCoursesBySubject();

  MessageResponse getListOfSubDomainsByCourse();

  MessageResponse getListOfStandardsBySubDomain();

}
