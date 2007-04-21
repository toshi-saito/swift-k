package org.griphyn.vdl.mapping;

import java.util.HashMap;
import java.util.Map;

import org.griphyn.vdl.mapping.Mapper;

/** AbstractMapper provides an implementation of the Mapper interface to be
    used as a base class for writing other mappers. It provides handling
    for mapper properties in a simple fashion that should be suitable for
    most cases.
*/

public abstract class AbstractMapper implements Mapper {

	protected Map params;

	public synchronized void setParam(String name, Object value) {
		if (params == null) {
			params = new HashMap();
		}
		params.put(name, value);
	}

	public synchronized Object getParam(String name) {
		if (params != null) {
			return params.get(name);
		}
		else {
			return null;
		}
	}

	public void setParams(Map params) {
		this.params = params;
	}


}
