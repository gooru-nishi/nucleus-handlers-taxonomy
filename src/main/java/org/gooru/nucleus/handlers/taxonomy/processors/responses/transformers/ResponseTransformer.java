package org.gooru.nucleus.handlers.taxonomy.processors.responses.transformers;


import io.vertx.core.json.JsonObject;

public interface ResponseTransformer {

  JsonObject transform();

}
