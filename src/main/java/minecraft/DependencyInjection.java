package com.mageddo.jvmti.minecraft;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.mageddo.jvmti.ClassInstanceService;
import com.mageddo.jvmti.RemoteClassInstanceService;

import javax.inject.Singleton;

public class DependencyInjection extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(MinecraftScanner.class).in(Scopes.SINGLETON);
  }

  @Provides
  @Singleton
  static ClassInstanceService classInstanceService(ObjectMapper objectMapper) {
    return new RemoteClassInstanceService(objectMapper);
  }

  @Provides
  @Singleton
  static ObjectMapper objectMapper() {
    return new ObjectMapper()
      .enable(SerializationFeature.INDENT_OUTPUT)
      ;
  }
}
