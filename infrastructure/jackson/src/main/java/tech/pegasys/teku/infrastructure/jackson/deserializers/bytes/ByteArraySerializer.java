/*
 * Copyright 2021 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package tech.pegasys.teku.infrastructure.jackson.deserializers.bytes;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class ByteArraySerializer extends JsonSerializer<byte[]> {
  @Override
  public void serialize(
      final byte[] value, final JsonGenerator gen, final SerializerProvider serializers)
      throws IOException {
    gen.writeStartArray();
    for (int i = 0; i < value.length; i++) {
      final int s = value[i] & 0xff;
      gen.writeString(String.format("%d", s));
    }
    gen.writeEndArray();
  }
}