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
 * Created on Sep 3, 2015
 */
package org.griphyn.vdl.compiler.intermediate;

import java.util.List;

import org.antlr.stringtemplate.StringTemplate;

public class IArrayExpression extends IAbstractExpression {
    private String fieldName;
    private List<IExpression> items;
    
    public IArrayExpression() {
        super();
    }


    public void addItem(IExpression item) {
        items = lazyAdd(items, item);
    }


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }


    @Override
    protected void setTemplateAttributes(OutputContext oc, StringTemplate st) {
        super.setTemplateAttributes(oc, st);
        setAll(oc, st, items, "elements");
        st.setAttribute("field", fieldName);
    }

    @Override
    protected String getTemplateName() {
        return "array";
    }
}
