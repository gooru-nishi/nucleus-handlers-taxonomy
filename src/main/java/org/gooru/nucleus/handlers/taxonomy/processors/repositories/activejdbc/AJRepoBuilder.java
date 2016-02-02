package org.gooru.nucleus.handlers.taxonomy.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.taxonomy.processors.ProcessorContext;
import org.gooru.nucleus.handlers.taxonomy.processors.repositories.TaxonomyRepo;

/**
 * Created by ashish on 29/12/15.
 */
public class AJRepoBuilder {

  public TaxonomyRepo buildTaxonomyRepo(ProcessorContext context) {
    return new AJTaxonomyRepo(context);
  }

}
