// NewInstance.java - create a new instance of a class by name.
// Written by Edwin Goei, edwingo@apache.org
// NO WARRANTY!  This class is in the Public Domain.

// $Id$

package org.xml.sax.helpers;

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
 * <p>This code is designed to run on JDK version 1.1 and later including
 * JVMs that perform early linking like the Microsoft JVM in IE 5.  Note
 * however that it must be compiled on a JDK version 1.2 or later system
 * since it calls Thread#getContextClassLoader().  The code also runs both
 * as part of an unbundled jar file and when bundled as part of the
 * JDK.</p>
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
        try {
            // Construct the name of the concrete class to instantiate
            Class clazz = Class.forName(NewInstance.class.getName()
                                        + "$ClassLoaderFinderConcrete");
            ClassLoaderFinder clf = (ClassLoaderFinder) clazz.newInstance();
            classLoader = clf.getContextClassLoader();
        } catch (LinkageError le) {
            // Assume that we are running JDK 1.1, use the current ClassLoader
            classLoader = NewInstance.class.getClassLoader();
        } catch (ClassNotFoundException x) {
            // This case should not normally happen.  MS IE can throw this
            // instead of a LinkageError the second time Class.forName() is
            // called so assume that we are running JDK 1.1 and use the
            // current ClassLoader
            classLoader = NewInstance.class.getClassLoader();
        }

        Class driverClass;
        if (classLoader == null) {
            driverClass = Class.forName(className);
        } else {
            driverClass = classLoader.loadClass(className);
        }
        return driverClass.newInstance();
    }

    /*
     * The following nested classes allow getContextClassLoader() to be
     * called only on JDK 1.2 and yet run in older JDK 1.1 JVMs
     */

    private static abstract class ClassLoaderFinder {
        abstract ClassLoader getContextClassLoader();
    }

    static class ClassLoaderFinderConcrete extends ClassLoaderFinder {
        ClassLoader getContextClassLoader() {
            return Thread.currentThread().getContextClassLoader();
        }
    }
}
