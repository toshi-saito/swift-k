/*
 * Copyright 2012 University of Chicago
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

/*
 * Created on Apr 18, 2015
 */
package org.globus.swift.parsetree;

import java.util.Collections;
import java.util.List;

public class Append extends Statement {
    private LValue lhs;
    private Expression rhs;
    
    public LValue getLhs() {
        return lhs;
    }
    
    public void setLhs(LValue lhs) {
        this.lhs = lhs;
    }
    
    public Expression getRhs() {
        return rhs;
    }
    
    public void setRhs(Expression rhs) {
        this.rhs = rhs;
    }

    @Override
    public List<? extends Node> getSubNodes() {
        return Collections.singletonList(rhs);
    }
     
    @Override
    public String getNodeName() {
        return "append statement";
    }
}
