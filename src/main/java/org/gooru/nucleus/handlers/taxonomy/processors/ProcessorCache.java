package org.gooru.nucleus.handlers.taxonomy.processors;

import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessorCache {
  private static final ProcessorCache INSTANCE = new ProcessorCache();
  private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorCache.class);

  private final CacheHolder listSubjects;
  private final CacheHolder coursesListBySubject;
  private final CacheHolder domainsListByCourses;
  private final CacheHolder standardsByDomain;

  private ProcessorCache() {
    this.listSubjects = new CacheHolder();
    this.coursesListBySubject = new CacheHolder();
    this.domainsListByCourses = new CacheHolder();
    this.standardsByDomain = new CacheHolder();
    }

  public static ProcessorCache getInstance() {
    return INSTANCE;
  }

  public JsonObject getListSubjects() {
    return listSubjects.getCachedValue();
  }

  public void setListSubjects(JsonObject listSubjects) {
    if (listSubjects != null) {
      LOGGER.debug("Trying to initialize list of subjects");
      this.listSubjects.initialize(listSubjects);
    }
  }

  public JsonObject getCoursesListBySubject() {
    return this.coursesListBySubject.getCachedValue();
  }

  public void setCoursesListBySubject(JsonObject coursesListBySubject) {
    if (coursesListBySubject != null) {
      LOGGER.debug("Trying to initialize courses list by subject");
      this.coursesListBySubject.initialize(coursesListBySubject);
    }
  }

  public JsonObject getDomainsListByCourses() {
    return this.domainsListByCourses.getCachedValue();
  }

  public void setDomainsListByCourses(JsonObject domainsListByCourses) {
    if (this.domainsListByCourses != null) {
      LOGGER.debug("Trying to initialize domains list by courses");
      this.domainsListByCourses.initialize(domainsListByCourses);
    }
  }

  public JsonObject getStandardsByDomain() {
    return this.standardsByDomain.getCachedValue();
  }

  public void setStandardsByDomain(JsonObject standardsByDomain) {
    if (standardsByDomain != null) {
      LOGGER.debug("Trying to initialize standards by domain");
      this.standardsByDomain.initialize(standardsByDomain);
    }
  }



  private static class CacheHolder {
    private final Object lock = new Object();
    JsonObject cache;
    private volatile boolean initialized = false;

    public CacheHolder() {
    }

    public void initialize(JsonObject cacheItem) {
      if (cacheItem != null) {
        if (!initialized) {
          synchronized (lock) {
            if (!initialized) {
              LOGGER.debug("Initialization successful");
              this.cache = cacheItem.copy();
              this.initialized = true;
            }
          }
        }
      }
    }

    public void reset() {
      this.initialized = false;
    }

    public JsonObject getCachedValue() {
      if (this.cache == null) {
        return null;
      }
      return this.cache.copy();
    }

  }
}
