package org.gooru.nucleus.handlers.taxonomy.processors.repositories.activejdbc.entities;

import java.util.Arrays;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

/**
 * Created by ashish on 29/12/15.
 */
@Table("default_subject")
@IdName("default_subject_id")
public class AJEntityDefaultSubject extends Model {
  
  public static final List<String> LISTSUBJECTS_FIELDS = Arrays.asList("default_subject_id", "code", "display_code", "sequence_id", "subject_classification" );

  
  public static final String LISTSUBJECTS_QUERY =
          "SELECT default_subject_id, code, display_code,description, sequence_id, subject_classification FROM default_subject";
  
}