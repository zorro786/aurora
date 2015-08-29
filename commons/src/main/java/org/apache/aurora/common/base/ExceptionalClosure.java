/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.aurora.common.base;

/**
 * An interface that captures a unit of work against an item.
 *
 * @param <T> The closure type.
 * @param <E> The exception type thrown by the closure.
 *
 * @author John Sirois
 */
public interface ExceptionalClosure<T, E extends Exception> {

  /**
   * Performs a unit of work on item, possibly throwing {@code E} in the process.
   *
   * <p>TODO(John Sirois): consider supporting @Nullable
   *
   * @param item the item to perform work against
   * @throws E if there was a problem performing the work
   */
  void execute(T item) throws E;
}