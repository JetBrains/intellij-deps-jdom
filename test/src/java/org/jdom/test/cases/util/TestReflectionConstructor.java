package org.jdom.test.cases.util;

import static org.jdom.test.util.UnitTestUtil.checkException;
import static org.jdom.test.util.UnitTestUtil.failNoException;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import org.jdom.Namespace;
import org.jdom.internal.ReflectionConstructor;

@SuppressWarnings("javadoc")
public class TestReflectionConstructor {

	@Test
	public void testConstruct() {
		List<?> al = ReflectionConstructor.construct("java.util.ArrayList", List.class);
		assertTrue(al.isEmpty());
	}

	@Test
	public void testConstructNoSuchClass() {
		try {
			ReflectionConstructor.construct("java.util.Junk", List.class);
			failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			checkException(IllegalArgumentException.class, e);
		}
	}

	@Test
	public void testConstructDefaultConstructor() {
		try {
			ReflectionConstructor.construct("java.util.Integer", Integer.class);
			failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			checkException(IllegalArgumentException.class, e);
		}
	}

	@Test
	public void testConstructNoaccessConstructor() {
		try {
			ReflectionConstructor.construct("org.jdom.Namespace", Namespace.class);
			failNoException(IllegalArgumentException.class);
		} catch (Exception e) {
			checkException(IllegalArgumentException.class, e);
		}
	}
	
	@Test
	public void testConstructClassCastConstructor() {
		try {
			ReflectionConstructor.construct("java.lang.String", Namespace.class);
			failNoException(ClassCastException.class);
		} catch (Exception e) {
			checkException(ClassCastException.class, e);
		}
	}
}
