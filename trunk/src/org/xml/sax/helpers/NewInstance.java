// NewInstance.java - create a new instance of a class by name.
// Written by Edwin Goei, edwingo@apache.org
// NO WARRANTY!  This class is in the Public Domain.

// $Id$

package org.xml.sax.helpers;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * Create a new instance of a class by name.
 *
 * <blockquote>
 * <em>This module, both source code and documentation, is in the
 * Public Domain, and comes with <strong>NO WARRANTY</strong>.</em>
 * </blockquote>
 *
 * <p>This class contains a static method for creating an instance of a
 * class from an explicit class name.  It tries to use the thread's context
 * ClassLoader if possible and falls back to using
 * Class.forName(String).</p>
 *
 * <p>This code is designed to compile and run on JDK version 1.1 and
 * later.  The code also runs both as part of an unbundled jar file and
 * when bundled as part of the JDK.</p>
 */
class NewInstance {
    /**
     * Creates a new instance of the specified class name
     *
     * Package private so this code is not exposed at the API level.
     */
    static Object newInstance(String className)
        throws ClassNotFoundException, IllegalAccessException,
            InstantiationException
    {
        ClassLoader classLoader;
        Method m = null;

        try {
            m = Thread.class.getMethod("getContextClassLoader", null);
        } catch (NoSuchMethodException e) {
            // Assume that we are running JDK 1.1, use the current ClassLoader
            classLoader = NewInstance.class.getClassLoader();
        }

        try {
            classLoader = (ClassLoader) m.invoke(Thread.currentThread(), null);
        } catch (IllegalAccessException e) {
            // assert(false)
            throw new ClassNotFoundException(
                "Unexpected IllegalAccessException");
        } catch (InvocationTargetException e) {
            // assert(e.getTargetException() instanceof SecurityException)
            // This case should not happen
            throw new ClassNotFoundException(
                "Unexpected InvocationTargetException");
        }

        Class driverClass;
        if (classLoader == null) {
            driverClass = Class.forName(className);
        } else {
            driverClass = classLoader.loadClass(className);
        }
        return driverClass.newInstance();
    }
}
