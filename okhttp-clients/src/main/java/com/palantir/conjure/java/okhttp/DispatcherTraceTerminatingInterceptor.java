/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
 *
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

package com.palantir.conjure.java.okhttp;

import com.palantir.tracing.AsyncTracer;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;

public final class DispatcherTraceTerminatingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        AsyncTracer tracerTag = chain.request().tag(AsyncTracer.class);
        if (tracerTag == null) {
            return chain.proceed(chain.request());
        }

        return tracerTag.withTrace(() -> chain.proceed(chain.request()));
    }
}
