package com.gbmoraes.issues.adapters.outbound.id;

import com.fasterxml.uuid.Generators;
import com.gbmoraes.issues.application.port.Id;
import org.springframework.stereotype.Component;

@Component
public class UUIDv7 implements Id {

  @Override
  public String generate() {
    return Generators.timeBasedEpochGenerator().generate().toString();
  }
}
