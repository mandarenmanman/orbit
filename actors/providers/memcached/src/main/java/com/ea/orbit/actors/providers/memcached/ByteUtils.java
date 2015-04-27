/*
 Copyright (C) 2015 Electronic Arts Inc.  All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1.  Redistributions of source code must retain the above copyright
     notice, this list of conditions and the following disclaimer.
 2.  Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in the
     documentation and/or other materials provided with the distribution.
 3.  Neither the name of Electronic Arts, Inc. ("EA") nor the names of
     its contributors may be used to endorse or promote products derived
     from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY ELECTRONIC ARTS AND ITS CONTRIBUTORS "AS IS" AND ANY
 EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL ELECTRONIC ARTS OR ITS CONTRIBUTORS BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ea.orbit.actors.providers.memcached;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Utility class for doing things with bytes and bytearrays.
 *
 * @author Johno Crawford (johno@sulake.com)
 */
public class ByteUtils
{

    /**
     * Extracts an object which has been serialized into a byte array
     *
     * @param byteArray byte array containing the objecct
     * @return the serialized object or null if error occurs
     */
    public static Object getSerializedObject(byte[] byteArray) throws IOException, ClassNotFoundException
    {
        ByteArrayInputStream str = new ByteArrayInputStream(byteArray);
        Object result = null;

        ObjectInputStream ois = new ObjectInputStream(str);
        result = ois.readObject();
        return result;
    }

    /**
     * Serializes object into a byte array
     *
     * @param object the serializable object to serialize
     * @return byte array containing the serialized object
     */
    public static byte[] serializeObject(Object object)
    {
        ByteArrayOutputStream str = new ByteArrayOutputStream();
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(str);
            oos.writeObject(object);
        }
        catch (IOException impossible)
        {
            throw new RuntimeException("Failed to serialize object " + object + " to byte array", impossible);
        }
        return str.toByteArray();
    }
}
