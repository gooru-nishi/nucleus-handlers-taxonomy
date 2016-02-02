package org.gooru.nucleus.handlers.taxonomy.processors.repositories;

import org.gooru.nucleus.handlers.taxonomy.processors.ProcessorContext;
import org.gooru.nucleus.handlers.taxonomy.processors.repositories.activejdbc.AJRepoBuilder;

/**
 * Created by ashish on 29/12/15.
 */
public class RepoBuilder {
  public TaxonomyRepo buildTaxonomyRepo(ProcessorContext context) {
    return new AJRepoBuilder().buildTaxonomyRepo(context);
  }
 
}
