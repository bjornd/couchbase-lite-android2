//
// JavaTest.java
//
// Copyright (c) 2017 Couchbase, Inc All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
package com.couchbase.lite;

import com.couchbase.litecore.LiteCoreException;
import com.couchbase.litecore.fleece.AllocSlice;
import com.couchbase.litecore.fleece.Encoder;
import com.couchbase.litecore.fleece.FLValue;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JavaTest extends BaseTest {

    // https://github.com/couchbase/couchbase-lite-android/issues/1453
    @Test
    public void testFLEncode() throws LiteCoreException {
        testRoundTrip(42L);
        testRoundTrip(Long.MIN_VALUE);
        testRoundTrip("Fleece");
        Map<String, Object> map = new HashMap<>();
        map.put("foo", "bar");
        testRoundTrip(map);
        testRoundTrip(Arrays.asList((Object) "foo", "bar"));
        testRoundTrip(true);
        testRoundTrip(3.14F);
        testRoundTrip(Math.PI);
    }

    private void testRoundTrip(Object item) throws LiteCoreException {
        Encoder encoder = new Encoder();
        assertNotNull(encoder);
        try {
            assertTrue(encoder.writeObject(item));
            AllocSlice slice = encoder.finish();
            assertNotNull(slice);
            FLValue flValue = FLValue.fromData(slice);
            assertNotNull(flValue);
            Object obj = FLValue.toObject(flValue);
            assertEquals(item, obj);
        } finally {
            encoder.release();
        }
    }
}
