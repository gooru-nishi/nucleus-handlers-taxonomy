package org.gooru.nucleus.handlers.taxonomy.processors;

import io.vertx.core.json.JsonObject;

public interface Processor {
  public JsonObject process();
}
