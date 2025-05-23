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
package org.apache.lucene.backward_codecs.lucene101;

import org.apache.lucene.backward_codecs.lucene90.blocktree.Lucene90BlockTreeTermsWriter;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.tests.index.BasePostingsFormatTestCase;
import org.apache.lucene.tests.util.TestUtil;

public class TestLucene101PostingsFormatV0 extends BasePostingsFormatTestCase {

  @Override
  protected Codec getCodec() {
    return TestUtil.alwaysPostingsFormat(
        new Lucene101RWPostingsFormat(
            Lucene90BlockTreeTermsWriter.DEFAULT_MIN_BLOCK_SIZE,
            Lucene90BlockTreeTermsWriter.DEFAULT_MAX_BLOCK_SIZE,
            Lucene101PostingsFormat.VERSION_START));
  }
}
