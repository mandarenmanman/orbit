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

package com.ea.orbit.actors.client;

import com.ea.orbit.actors.*;
import com.ea.orbit.actors.cluster.NodeAddress;
import com.ea.orbit.actors.extensions.MessageSerializer;
import com.ea.orbit.actors.net.DefaultPipeline;
import com.ea.orbit.actors.net.Handler;
import com.ea.orbit.actors.net.Pipeline;
import com.ea.orbit.actors.runtime.BasicRuntime;
import com.ea.orbit.actors.runtime.DefaultDescriptorFactory;
import com.ea.orbit.actors.runtime.DefaultHandlers;
import com.ea.orbit.actors.runtime.Invocation;
import com.ea.orbit.actors.runtime.Messaging;
import com.ea.orbit.actors.runtime.ObjectInvoker;
import com.ea.orbit.actors.runtime.Peer;
import com.ea.orbit.actors.runtime.RemoteClient;
import com.ea.orbit.actors.runtime.SerializationHandler;
import com.ea.orbit.annotation.Wired;
import com.ea.orbit.concurrent.Task;
import com.ea.orbit.container.Startable;

import java.lang.reflect.Method;
import java.time.Clock;

/**
 * This works as a bridge to perform calls between the server and a client.
 */
public class ClientPeer extends Peer implements BasicRuntime, Startable, RemoteClient
{

    public void cleanup(final boolean b)
    {

    }

    @Override
    public Task<?> start()
    {
        getPipeline().addLast(DefaultHandlers.EXECUTION, new ClientPeerExecutor());
        getPipeline().addLast(DefaultHandlers.MESSAGING, new Messaging());
        getPipeline().addLast(DefaultHandlers.SERIALIZATION, new SerializationHandler(this, getMessageSerializer()));
        getPipeline().addLast(DefaultHandlers.NETWORK, getNetwork());
        return getPipeline().connect(null);
    }

    @Override
    public Task<?> invoke(final Addressable toReference, final Method m, final boolean oneWay, final int methodId, final Object[] params)
    {
        final Invocation invocation = new Invocation(toReference, m, oneWay, methodId, params, null);
        return getPipeline().write(invocation);
    }

    @Override
    public <T extends com.ea.orbit.actors.ActorObserver> T registerObserver(final Class<T> iClass, final T observer)
    {
        return null;
    }

    @Override
    public <T extends com.ea.orbit.actors.ActorObserver> T getRemoteObserverReference(final NodeAddress address, final Class<T> iClass, final Object id)
    {
        return null;
    }

    @Override
    public <T extends Actor> T getReference(final Class<T> iClass, final Object id)
    {
        return DefaultDescriptorFactory.get().getReference(this, iClass, id);
    }

    @Override
    public ObjectInvoker<?> getInvoker(final int interfaceId)
    {
        return null;
    }
}
