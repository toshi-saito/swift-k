/*
 * Swift Parallel Scripting Language (http://swift-lang.org)
 * Code from Java CoG Kit Project (see notice below) with modifications.
 *
 * Copyright 2005-2014 University of Chicago
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//----------------------------------------------------------------------
//This code is developed as part of the Java CoG Kit project
//The terms of the license can be found at http://www.cogkit.org/license
//This message may not be removed or altered.
//----------------------------------------------------------------------

/*
 * Created on Mar 28, 2014
 */
package org.griphyn.vdl.mapping.nodes;

import k.thr.LWThread;

import org.griphyn.vdl.mapping.DSHandle;
import org.griphyn.vdl.mapping.Mapper;
import org.griphyn.vdl.mapping.Path;
import org.griphyn.vdl.mapping.RootHandle;
import org.griphyn.vdl.type.Field;

public class RootFuturePrimitiveDataNode extends AbstractFuturePrimitiveDataNode implements RootHandle {
	private int line = -1;
    private LWThread thread;
    
    public RootFuturePrimitiveDataNode(Field field) {
    	super(field);
    }

    @Override
    public RootHandle getRoot() {
        return this;
    }

    @Override
    public DSHandle getParent() {
        return null;
    }
    
    @Override
    public Path getPathFromRoot() {
        return Path.EMPTY_PATH;
    }

    @Override
    public void init(Mapper mapper) {
    }

    @Override
    public void mapperInitialized(Mapper mapper) {
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public boolean isInput() {
        return false;
    }

    @Override
    public void setInput(boolean input) {
        // doesn't matter
    }

    @Override
    public void setThread(LWThread thread) {
        this.thread = thread;
    }

    @Override
    public LWThread getThread() {
        return thread;
    }
    
    @Override
    public String getName() {
        return (String) getField().getId();
    }
    
    @Override
    protected AbstractDataNode getParentNode() {
        return null;
    }

    @Override
    public Mapper getActualMapper() {
        return null;
    }
    
    @Override
    public Mapper getMapper() {
        throw new UnsupportedOperationException("Primitive types do not have a mapper");
    }
}