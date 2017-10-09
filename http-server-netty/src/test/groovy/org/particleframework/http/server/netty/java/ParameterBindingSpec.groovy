/*
 * Copyright 2017 original authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package org.particleframework.http.server.netty.java

import okhttp3.Request
import org.particleframework.http.HttpStatus
import org.particleframework.http.server.netty.AbstractParticleSpec
import spock.lang.Unroll

/**
 * Created by graemerocher on 25/08/2017.
 */
class ParameterBindingSpec extends AbstractParticleSpec {


    @Unroll
    void "test bind HTTP parameters for URI #uri"() {
        given:
        def request = new Request.Builder()
                .url("$server$uri")

        def response = client.newCall(
                request.build()
        ).execute()

        def status = response.code()
        def body = null
        if (status == HttpStatus.OK.code) {
            body = response.body().string()
        }
        expect:
        body == result
        HttpStatus.valueOf(status) == httpStatus



        where:
        uri                                             | result                      | httpStatus
        '/java/parameter?max=20'                             | "Parameter Value: 20"       | HttpStatus.OK
        '/java/parameter/simple?max=20'                      | "Parameter Value: 20"       | HttpStatus.OK
        '/java/parameter/simple'                             | null                        | HttpStatus.BAD_REQUEST
        '/java/parameter/named'                              | null                        | HttpStatus.BAD_REQUEST
        '/java/parameter/named?maximum=20'                   | "Parameter Value: 20"       | HttpStatus.OK
        '/java/parameter/optional'                           | "Parameter Value: 10"       | HttpStatus.OK
        '/java/parameter/optional?max=20'                    | "Parameter Value: 20"       | HttpStatus.OK
        '/java/parameter/all'                                | "Parameter Value: 10"       | HttpStatus.OK
        '/java/parameter/all?max=20'                         | "Parameter Value: 20"       | HttpStatus.OK
        '/java/parameter/map?values.max=20&values.offset=30' | "Parameter Value: 2030"    | HttpStatus.OK
        '/java/parameter/list?values=10,20'                  | "Parameter Value: [10, 20]" | HttpStatus.OK
        '/java/parameter/list?values=10&values=20'           | "Parameter Value: [10, 20]" | HttpStatus.OK
        '/java/parameter/optionalList?values=10&values=20'   | "Parameter Value: [10, 20]" | HttpStatus.OK
    }


}