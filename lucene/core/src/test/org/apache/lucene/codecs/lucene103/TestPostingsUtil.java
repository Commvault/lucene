/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.lucene.codecs.lucene103;

import java.io.IOException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.tests.util.LuceneTestCase;

public class TestPostingsUtil extends LuceneTestCase {

  // checks for bug described in https://github.com/apache/lucene/issues/13373
  public void testIntegerOverflow() throws IOException {
    // Size that writes the first value as a regular vint
    int randomSize1 = random().nextInt(1, 3);
    // Size that writes the first value as a group vint
    int randomSize2 = random().nextInt(4, ForUtil.BLOCK_SIZE);
    doTestIntegerOverflow(randomSize1);
    doTestIntegerOverflow(randomSize2);
  }

  private void doTestIntegerOverflow(int size) throws IOException {
    final int[] docDeltaBuffer = new int[size];
    final int[] freqBuffer = new int[size];

    final int delta = 1 << 30;
    docDeltaBuffer[0] = delta;
    try (Directory dir = newDirectory()) {
      try (IndexOutput out = dir.createOutput("test", IOContext.DEFAULT)) {
        // In old implementation, this would cause integer overflow exception.
        PostingsUtil.writeVIntBlock(out, docDeltaBuffer, freqBuffer, size, true);
      }
      int[] restoredDocs = new int[size];
      int[] restoredFreqs = new int[size];
      try (IndexInput in = dir.openInput("test", IOContext.DEFAULT)) {
        PostingsUtil.readVIntBlock(in, restoredDocs, restoredFreqs, size, true, true);
      }
      assertEquals(delta, restoredDocs[0]);
    }
  }
}
