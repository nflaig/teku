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

package tech.pegasys.teku.spec.logic.versions.merge.block;

import tech.pegasys.teku.spec.datastructures.execution.ExecutionPayload;

public interface OptimisticExecutionPayloadExecutor {

  OptimisticExecutionPayloadExecutor NOOP = payload -> true;

  /**
   * At least begins execution of the specified payload, which may complete asynchronously. Note
   * that a {@code true} value does NOT indicate the payload is valid only that it is not
   * immediately found to be invalid and can be optimistically accepted.
   *
   * @param executionPayload the payload to execute
   * @return true if the payload should be optimistically accepted or false to * immediately
   *     invalidate the payload
   */
  boolean optimisticallyExecute(final ExecutionPayload executionPayload);
}