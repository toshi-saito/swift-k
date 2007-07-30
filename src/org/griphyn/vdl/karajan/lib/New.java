/*
 * Created on Dec 26, 2006
 */
package org.griphyn.vdl.karajan.lib;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.globus.cog.karajan.arguments.Arg;
import org.globus.cog.karajan.stack.VariableStack;
import org.globus.cog.karajan.util.TypeUtil;
import org.globus.cog.karajan.workflow.ExecutionException;
import org.griphyn.vdl.mapping.AbstractDataNode;
import org.griphyn.vdl.mapping.DSHandle;
import org.griphyn.vdl.mapping.Path;
import org.griphyn.vdl.mapping.RootArrayDataNode;
import org.griphyn.vdl.mapping.RootDataNode;
import org.griphyn.vdl.mapping.file.ConcurrentMapper;

public class New extends VDLFunction {
	public static final Arg OA_TYPE = new Arg.Optional("type", null);
	public static final Arg OA_MAPPING = new Arg.Optional("mapping", null);
	public static final Arg OA_VALUE = new Arg.Optional("value", null);
	public static final Arg OA_DBGNAME = new Arg.Optional("dbgname", null);
	
	static {
		setArguments(New.class, new Arg[] { OA_TYPE, OA_MAPPING, OA_VALUE, OA_ISARRAY,
				OA_DBGNAME, });
	}

	public Object function(VariableStack stack) throws ExecutionException {
		String type = TypeUtil.toString(OA_TYPE.getValue(stack));
		Object value = OA_VALUE.getValue(stack);
		Map mapping = (Map) OA_MAPPING.getValue(stack);
		boolean isArray = TypeUtil.toBoolean(OA_ISARRAY.getValue(stack));
		String dbgname = TypeUtil.toString(OA_DBGNAME.getValue(stack));
		if (dbgname != null) {
			if (mapping == null) {
				mapping = new HashMap();
			}
			mapping.put("dbgname", dbgname);
		}
		if (type == null && value == null) {
			throw new ExecutionException("You must specify either a type or a value");
		}
		if (mapping != null) {
			String mapper = (String) mapping.get("descriptor");
			if ("concurrent_mapper".equals(mapper)) {
				String threadPrefix = getThreadPrefix(stack);
				ConcurrentMapper.PARAM_THREAD_PREFIX.setValue(mapping, threadPrefix);
			}
		}
		try {
			DSHandle handle;
			if (isArray) {
				// dealing with array variable
				handle = new RootArrayDataNode(type);
				if (value != null) {
					if (value instanceof RootArrayDataNode) {
						handle = (RootArrayDataNode) value;
					}
					else {
						if (!(value instanceof List)) {
							throw new ExecutionException(
									"An array variable can only be initialized with a list of values");
						}
						int index = 0;
						Iterator i = ((List) value).iterator();
						while (i.hasNext()) {
							// TODO check type consistency of elements with
							// the type of the array
							Object n = i.next();
							Path p = Path.EMPTY_PATH.addLast(String.valueOf(index), true);
							if (n instanceof DSHandle) {
								handle.getField(p).set((DSHandle) n);
							}
							else {
								throw new RuntimeException("An array variable can only be initialized by a list of DSHandle values");
							}
							index++;
						}
					}
					closeShallow(stack, handle);
				}

				if (mapping != null) {
					handle.init(mapping);
				}
			}
			else if (value instanceof DSHandle) {
				handle = (DSHandle) value;
			}
			else if (type != null) {
				handle = new RootDataNode(type);
				if (mapping != null) {
					handle.init(mapping);
				}
				if (value != null) {
					handle.setValue(internalValue(type, value));
					closeShallow(stack, handle);
				}
			}
			else {
				handle = new RootDataNode(inferType(value));
				handle.setValue(internalValue(handle.getType(), value));
				closeShallow(stack, handle);
			}
			return handle;
		}
		catch (Exception e) {
			throw new ExecutionException(e);
		}
	}

}
