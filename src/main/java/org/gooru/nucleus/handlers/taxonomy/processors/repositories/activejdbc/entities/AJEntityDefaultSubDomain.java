package org.gooru.nucleus.handlers.taxonomy.processors.repositories.activejdbc.entities;

import java.util.Arrays;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@Table("default_subdomain")
@IdName("default_subdomain_id")
public class AJEntityDefaultSubDomain extends Model {
  
  public static final List<String> LISTSUBDOMAINS_FIELDS = Arrays.asList("default_subdomain_id", "default_course_id", "default_domain_id", "code", "display_code", "domain_display_code", "description", "sequence_id", "has_taxonomy_representation");
  
  public static final String LISTSUBDOMAINS_QUERY =
          "SELECT default_subdomain_id, default_course_id, default_domain_id, code, display_code, domain_display_code, description, sequence_id, has_taxonomy_representation FROM default_subdomain WHERE default_course_id = ?";  

  public static final String FETCHBYSUBDOMAINANDCOURSE_QUERY =
          "SELECT default_subdomain_id, default_course_id FROM default_subdomain WHERE default_subdomain_id = ? AND default_course_id = ?";  
}
