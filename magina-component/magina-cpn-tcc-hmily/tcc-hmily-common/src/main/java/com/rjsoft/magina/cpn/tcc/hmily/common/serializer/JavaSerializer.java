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

package com.rjsoft.magina.cpn.tcc.hmily.common.serializer;



import com.rjsoft.magina.cpn.tcc.hmily.annotation.HmilySPI;
import com.rjsoft.magina.cpn.tcc.hmily.common.exception.HmilyException;

import java.io.*;

/**
 * JavaSerializer.
 *
 * @author xiaoyu
 */
@HmilySPI("jdk")
public class JavaSerializer implements ObjectSerializer {

    @Override
    public byte[] serialize(final Object obj) throws HmilyException {
        try (ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(); ObjectOutput objectOutput = new ObjectOutputStream(arrayOutputStream)) {
            objectOutput.writeObject(obj);
            objectOutput.flush();
            return arrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new HmilyException("java serialize error " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deSerialize(final byte[] param, final Class<T> clazz) throws HmilyException {
        try (ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(param); ObjectInput input = new ObjectInputStream(arrayInputStream)) {
            return (T) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new HmilyException("java deSerialize error " + e.getMessage());
        }
    }
}
