package org.gooru.nucleus.handlers.taxonomy.processors.repositories.activejdbc.entities;

import java.util.Arrays;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@Table("default_course")
@IdName("default_course_id")
public class AJEntityDefaultCourse extends Model {
  
  public static final List<String> LISTCOURSES_FIELDS = Arrays.asList("default_course_id", "default_subject_id", "code", "display_code", "description", "grades", "sequence_id");

  
  public static final String LISTCOURSES_QUERY =
          "SELECT default_course_id, code, display_code, description, grades, sequence_id, has_taxonomy_representation FROM default_course WHERE default_subject_id = ? ";  
  
  public static final String FETCHBYCOURSEANDSUBJECT_QUERY =
          "SELECT default_course_id, default_subject_id FROM default_course WHERE default_course_id = ? AND default_subject_id = ? ";  

}
