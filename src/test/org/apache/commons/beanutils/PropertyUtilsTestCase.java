/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//beanutils/src/test/org/apache/commons/beanutils/PropertyUtilsTestCase.java,v 1.13 2002/01/21 00:44:39 craigmcc Exp $
 * $Revision: 1.13 $
 * $Date: 2002/01/21 00:44:39 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */


package org.apache.commons.beanutils;


import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.beanutils.priv.PrivateBeanFactory;
import org.apache.commons.beanutils.priv.PrivateDirect;
import org.apache.commons.beanutils.priv.PrivateIndirect;


/**
 * <p>Test Case for the PropertyUtils class.  The majority of these tests use
 * instances of the TestBean class, so be sure to update the tests if you
 * change the characteristics of that class.</p>
 *
 * <p>So far, this test case has tests for the following methods of the
 * <code>PropertyUtils</code> class:</p>
 * <ul>
 * <li>getIndexedProperty(Object,String)</li>
 * <li>getIndexedProperty(Object,String,int)</li>
 * <li>getMappedProperty(Object,String)</li>
 * <li>getMappedProperty(Object,String,String</li>
 * <li>getNestedProperty(Object,String)</li>
 * <li>getPropertyDescriptor(Object,String)</li>
 * <li>getPropertyDescriptors(Object)</li>
 * <li>getSimpleProperty(Object,String)</li>
 * <li>setIndexedProperty(Object,String,Object)</li>
 * <li>setIndexedProperty(Object,String,String,Object)</li>
 * <li>setMappedProperty(Object,String,Object)</li>
 * <li>setMappedProperty(Object,String,String,Object)</li>
 * <li>setNestedProperty(Object,String,Object)</li>
 * <li>setSimpleProperty(Object,String,Object)</li>
 * </ul>
 *
 * @author Craig R. McClanahan
 * @author Jan Sorensen
 * @version $Revision: 1.13 $ $Date: 2002/01/21 00:44:39 $
 */

public class PropertyUtilsTestCase extends TestCase {


    // ---------------------------------------------------- Instance Variables


    /**
     * The fully qualified class name of our private bean class.
     */
    private static final String PRIVATE_BEAN_CLASS =
        "org.apache.commons.beanutils.priv.PrivateBean";


    /**
     * The fully qualified class name of our private directly
     * implemented interface.
     */
    private static final String PRIVATE_DIRECT_CLASS =
        "org.apache.commons.beanutils.priv.PrivateDirect";


    /**
     * The fully qualified class name of our private indirectly
     * implemented interface.
     */
    private static final String PRIVATE_INDIRECT_CLASS =
        "org.apache.commons.beanutils.priv.PrivateIndirect";


    /**
     * The fully qualified class name of our test bean class.
     */
    private static final String TEST_BEAN_CLASS =
        "org.apache.commons.beanutils.TestBean";


    /**
     * The basic test bean for each test.
     */
    protected TestBean bean = null;


    /**
     * The "package private subclass" test bean for each test.
     */
    protected TestBeanPackageSubclass beanPackageSubclass = null;


    /**
     * The test bean for private access tests.
     */
    protected PrivateDirect beanPrivate = null;


    /**
     * The test bean for private access tests of subclasses.
     */
    protected PrivateDirect beanPrivateSubclass = null;


    /**
     * The "public subclass" test bean for each test.
     */
    protected TestBeanPublicSubclass beanPublicSubclass = null;


    /**
     * The set of property names we expect to have returned when calling
     * <code>getPropertyDescriptors()</code>.  You should update this list
     * when new properties are added to TestBean.
     */
    protected final static String[] properties = {
        "booleanProperty",
        "booleanSecond",
        "doubleProperty",
        "floatProperty",
        "intArray",
        "intIndexed",
        "intProperty",
        "listIndexed",
        "longProperty",
        "nested",
        "nullProperty",
        "readOnlyProperty",
        "shortProperty",
        "stringArray",
        "stringIndexed",
        "stringProperty",
        "writeOnlyProperty",
    };


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public PropertyUtilsTestCase(String name) {

        super(name);

    }


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        bean = new TestBean();
        beanPackageSubclass = new TestBeanPackageSubclass();
        beanPrivate = PrivateBeanFactory.create();
        beanPrivateSubclass = PrivateBeanFactory.createSubclass();
        beanPublicSubclass = new TestBeanPublicSubclass();

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(PropertyUtilsTestCase.class));

    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {

        bean = null;
        beanPackageSubclass = null;
        beanPrivate = null;
        beanPrivateSubclass = null;
        beanPublicSubclass = null;

    }



    // ------------------------------------------------ Individual Test Methods


    /**
     * Corner cases on getPropertyDescriptor invalid arguments.
     */
    public void testGetDescriptorArguments() {

        try {
            PropertyUtils.getPropertyDescriptor(null, "stringProperty");
            fail("Should throw IllegalArgumentException 1");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 1");
        }

        try {
            PropertyUtils.getPropertyDescriptor(bean, null);
            fail("Should throw IllegalArgumentException 2");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 2");
        }

    }


    /**
     * Positive getPropertyDescriptor on property <code>booleanProperty</code>.
     */
    public void testGetDescriptorBoolean() {

        testGetDescriptorBase("booleanProperty", "getBooleanProperty",
                              "setBooleanProperty");

    }


    /**
     * Positive getPropertyDescriptor on property <code>doubleProperty</code>.
     */
    public void testGetDescriptorDouble() {

        testGetDescriptorBase("doubleProperty", "getDoubleProperty",
                              "setDoubleProperty");

    }


    /**
     * Positive getPropertyDescriptor on property <code>floatProperty</code>.
     */
    public void testGetDescriptorFloat() {

        testGetDescriptorBase("floatProperty", "getFloatProperty",
                              "setFloatProperty");

    }


    /**
     * Positive getPropertyDescriptor on property <code>intProperty</code>.
     */
    public void testGetDescriptorInt() {

        testGetDescriptorBase("intProperty", "getIntProperty",
                              "setIntProperty");

    }


    /**
     * Positive getPropertyDescriptor on property <code>longProperty</code>.
     */
    public void testGetDescriptorLong() {

        testGetDescriptorBase("longProperty", "getLongProperty",
                              "setLongProperty");

    }


    /**
     * Positive getPropertyDescriptor on property
     * <code>readOnlyProperty</code>.
     */
    public void testGetDescriptorReadOnly() {

        testGetDescriptorBase("readOnlyProperty", "getReadOnlyProperty",
                              null);

    }


    /**
     * Positive getPropertyDescriptor on property <code>booleanSecond</code>
     * that uses an "is" method as the getter.
     */
    public void testGetDescriptorSecond() {

        testGetDescriptorBase("booleanSecond", "isBooleanSecond",
                              "setBooleanSecond");

    }


    /**
     * Positive getPropertyDescriptor on property <code>shortProperty</code>.
     */
    public void testGetDescriptorShort() {

        testGetDescriptorBase("shortProperty", "getShortProperty",
                              "setShortProperty");

    }


    /**
     * Positive getPropertyDescriptor on property <code>stringProperty</code>.
     */
    public void testGetDescriptorString() {

        testGetDescriptorBase("stringProperty", "getStringProperty",
                              "setStringProperty");

    }


    /**
     * Negative getPropertyDescriptor on property <code>unknown</code>.
     */
    public void testGetDescriptorUnknown() {

        testGetDescriptorBase("unknown", null, null);

    }


    /**
     * Positive getPropertyDescriptor on property
     * <code>writeOnlyProperty</code>.
     */
    public void testGetDescriptorWriteOnly() {

        testGetDescriptorBase("writeOnlyProperty", null,
                              "setWriteOnlyProperty");

    }


    /**
     * Positive test for getPropertyDescriptors().  Each property name
     * listed in <code>properties</code> should be returned exactly once.
     */
    public void testGetDescriptors() {

        PropertyDescriptor pd[] =
            PropertyUtils.getPropertyDescriptors(bean);
        assertNotNull("Got descriptors", pd);
        int count[] = new int[properties.length];
        for (int i = 0; i < pd.length; i++) {
            String name = pd[i].getName();
            for (int j = 0; j < properties.length; j++) {
                if (name.equals(properties[j]))
                    count[j]++;
            }
        }
        for (int j = 0; j < properties.length; j++) {
            if (count[j] < 0)
                fail("Missing property " + properties[j]);
            else if (count[j] > 1)
                fail("Duplicate property " + properties[j]);
        }

    }


    /**
     * Corner cases on getPropertyDescriptors invalid arguments.
     */
    public void testGetDescriptorsArguments() {

        try {
            PropertyUtils.getPropertyDescriptors(null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException");
        }

    }


    /**
     * Corner cases on getIndexedProperty invalid arguments.
     */
    public void testGetIndexedArguments() {

        // Use explicit index argument

        try {
            PropertyUtils.getIndexedProperty(null, "intArray", 0);
            fail("Should throw IllegalArgumentException 1");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 1");
        }

        try {
            PropertyUtils.getIndexedProperty(bean, null, 0);
            fail("Should throw IllegalArgumentException 2");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 2");
        }

        // Use index expression

        try {
            PropertyUtils.getIndexedProperty(null,
                                            "intArray[0]");
            fail("Should throw IllegalArgumentException 3");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 3");
        }

        try {
            PropertyUtils.getIndexedProperty(bean, "[0]");
            fail("Should throw NoSuchMethodException 4");
        } catch (NoSuchMethodException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of NoSuchMethodException 4");
        }

        try {
            PropertyUtils.getIndexedProperty(bean, "intArray");
            fail("Should throw IllegalArgumentException 5");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 5");
        }

        // Use explicit index argument

        try {
            PropertyUtils.getIndexedProperty(null, "intIndexed", 0);
            fail("Should throw IllegalArgumentException 1");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 1");
        }

        try {
            PropertyUtils.getIndexedProperty(bean, null, 0);
            fail("Should throw IllegalArgumentException 2");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 2");
        }

        // Use index expression

        try {
            PropertyUtils.getIndexedProperty(null,
                                            "intIndexed[0]");
            fail("Should throw IllegalArgumentException 3");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 3");
        }

        try {
            PropertyUtils.getIndexedProperty(bean, "[0]");
            fail("Should throw NoSuchMethodException 4");
        } catch (NoSuchMethodException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of NoSuchMethodException 4");
        }

        try {
            PropertyUtils.getIndexedProperty(bean, "intIndexed");
            fail("Should throw IllegalArgumentException 5");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 5");
        }

    }


    /**
     * Positive and negative tests on getIndexedProperty valid arguments.
     */
    public void testGetIndexedValues() {

        Object value = null;

        // Use explicit key argument

        for (int i = 0; i < 5; i++) {

            try {
                value =
                    PropertyUtils.getIndexedProperty(bean, "intArray", i);
                assertNotNull("intArray returned value " + i, value);
                assertTrue("intArray returned Integer " + i,
                           value instanceof Integer);
                assertEquals("intArray returned correct " + i, i * 10,
                             ((Integer) value).intValue());
            } catch (Throwable t) {
                fail("intArray " + i + " threw " + t);
            }

            try {
                value =
                    PropertyUtils.getIndexedProperty(bean, "intIndexed", i);
                assertNotNull("intIndexed returned value " + i, value);
                assertTrue("intIndexed returned Integer " + i,
                           value instanceof Integer);
                assertEquals("intIndexed returned correct " + i, i * 10,
                             ((Integer) value).intValue());
            } catch (Throwable t) {
                fail("intIndexed " + i + " threw " + t);
            }

            try {
                value =
                    PropertyUtils.getIndexedProperty(bean, "listIndexed", i);
                assertNotNull("listIndexed returned value " + i, value);
                assertTrue("list returned String " + i,
                           value instanceof String);
                assertEquals("listIndexed returned correct " + i,
                             "String " + i, (String) value);
            } catch (Throwable t) {
                fail("listIndexed " + i + " threw " + t);
            }

            try {
                value =
                    PropertyUtils.getIndexedProperty(bean, "stringArray", i);
                assertNotNull("stringArray returned value " + i, value);
                assertTrue("stringArray returned String " + i,
                           value instanceof String);
                assertEquals("stringArray returned correct " + i,
                             "String " + i, (String) value);
            } catch (Throwable t) {
                fail("stringArray " + i + " threw " + t);
            }

            try {
                value =
                    PropertyUtils.getIndexedProperty(bean, "stringIndexed", i);
                assertNotNull("stringIndexed returned value " + i, value);
                assertTrue("stringIndexed returned String " + i,
                           value instanceof String);
                assertEquals("stringIndexed returned correct " + i,
                             "String " + i, (String) value);
            } catch (Throwable t) {
                fail("stringIndexed " + i + " threw " + t);
            }

        }

        // Use key expression

        for (int i = 0; i < 5; i++) {

            try {
                value =
                    PropertyUtils.getIndexedProperty(bean,
                                                     "intArray[" + i + "]");
                assertNotNull("intArray returned value " + i, value);
                assertTrue("intArray returned Integer " + i,
                           value instanceof Integer);
                assertEquals("intArray returned correct " + i, i * 10,
                             ((Integer) value).intValue());
            } catch (Throwable t) {
                fail("intArray " + i + " threw " + t);
            }

            try {
                value =
                    PropertyUtils.getIndexedProperty(bean,
                                                     "intIndexed[" + i + "]");
                assertNotNull("intIndexed returned value " + i, value);
                assertTrue("intIndexed returned Integer " + i,
                           value instanceof Integer);
                assertEquals("intIndexed returned correct " + i, i * 10,
                             ((Integer) value).intValue());
            } catch (Throwable t) {
                fail("intIndexed " + i + " threw " + t);
            }

            try {
                value =
                    PropertyUtils.getIndexedProperty(bean,
                                                     "listIndexed[" + i + "]");
                assertNotNull("listIndexed returned value " + i, value);
                assertTrue("listIndexed returned String " + i,
                           value instanceof String);
                assertEquals("listIndexed returned correct " + i,
                             "String " + i, (String) value);
            } catch (Throwable t) {
                fail("listIndexed " + i + " threw " + t);
            }

            try {
                value =
                    PropertyUtils.getIndexedProperty(bean,
                                                     "stringArray[" + i + "]");
                assertNotNull("stringArray returned value " + i, value);
                assertTrue("stringArray returned String " + i,
                           value instanceof String);
                assertEquals("stringArray returned correct " + i,
                             "String " + i, (String) value);
            } catch (Throwable t) {
                fail("stringArray " + i + " threw " + t);
            }

            try {
                value =
                    PropertyUtils.getIndexedProperty(bean,
                                                     "stringIndexed[" + i + "]");
                assertNotNull("stringIndexed returned value " + i, value);
                assertTrue("stringIndexed returned String " + i,
                           value instanceof String);
                assertEquals("stringIndexed returned correct " + i,
                             "String " + i, (String) value);
            } catch (Throwable t) {
                fail("stringIndexed " + i + " threw " + t);
            }

        }

        // Index out of bounds tests

        try {
            value =
                PropertyUtils.getIndexedProperty(bean,
                                                 "intArray", -1);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException t) {
            ; // Expected results
        } catch (Throwable t) {
            fail("Threw " + t + " instead of ArrayIndexOutOfBoundsException");
        }

        try {
            value =
                PropertyUtils.getIndexedProperty(bean,
                                                 "intArray", 5);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException t) {
            ; // Expected results
        } catch (Throwable t) {
            fail("Threw " + t + " instead of ArrayIndexOutOfBoundsException");
        }

        try {
            value =
                PropertyUtils.getIndexedProperty(bean,
                                                 "intIndexed", -1);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException t) {
            ; // Expected results
        } catch (Throwable t) {
            fail("Threw " + t + " instead of ArrayIndexOutOfBoundsException");
        }

        try {
            value =
                PropertyUtils.getIndexedProperty(bean,
                                                 "intIndexed", 5);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException t) {
            ; // Expected results
        } catch (Throwable t) {
            fail("Threw " + t + " instead of ArrayIndexOutOfBoundsException");
        }

        try {
            value =
                PropertyUtils.getIndexedProperty(bean,
                                                 "listIndexed", -1);
            fail("Should have thrown IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException t) {
            ; // Expected results
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IndexOutOfBoundsException");
        }

        try {
            value =
                PropertyUtils.getIndexedProperty(bean,
                                                 "listIndexed", 5);
            fail("Should have thrown IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException t) {
            ; // Expected results
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IndexOutOfBoundsException");
        }

        try {
            value =
                PropertyUtils.getIndexedProperty(bean,
                                                 "stringArray", -1);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException t) {
            ; // Expected results
        } catch (Throwable t) {
            fail("Threw " + t + " instead of ArrayIndexOutOfBoundsException");
        }

        try {
            value =
                PropertyUtils.getIndexedProperty(bean,
                                                 "stringArray", 5);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException t) {
            ; // Expected results
        } catch (Throwable t) {
            fail("Threw " + t + " instead of ArrayIndexOutOfBoundsException");
        }

        try {
            value =
                PropertyUtils.getIndexedProperty(bean,
                                                 "stringIndexed", -1);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException t) {
            ; // Expected results
        } catch (Throwable t) {
            fail("Threw " + t + " instead of ArrayIndexOutOfBoundsException");
        }

        try {
            value =
                PropertyUtils.getIndexedProperty(bean,
                                                 "stringIndexed", 5);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException t) {
            ; // Expected results
        } catch (Throwable t) {
            fail("Threw " + t + " instead of ArrayIndexOutOfBoundsException");
        }

    }


    /**
     * Corner cases on getMappedProperty invalid arguments.
     */
    public void testGetMappedArguments() {

        // Use explicit key argument

        try {
            PropertyUtils.getMappedProperty(null, "mappedProperty",
                                            "First Key");
            fail("Should throw IllegalArgumentException 1");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 1");
        }

        try {
            PropertyUtils.getMappedProperty(bean, null, "First Key");
            fail("Should throw IllegalArgumentException 2");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 2");
        }

        try {
            PropertyUtils.getMappedProperty(bean, "mappedProperty", null);
            fail("Should throw IllegalArgumentException 3");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 3");
        }

        // Use key expression

        try {
            PropertyUtils.getMappedProperty(null,
                                            "mappedProperty(First Key)");
            fail("Should throw IllegalArgumentException 4");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 4");
        }

        try {
            PropertyUtils.getMappedProperty(bean, "(Second Key)");
            fail("Should throw IllegalArgumentException 5");
        } catch (NoSuchMethodException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of NoSuchMethodException 5");
        }

        try {
            PropertyUtils.getMappedProperty(bean, "mappedProperty");
            fail("Should throw IllegalArgumentException 6");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 6");
        }

    }


    /**
     * Positive and negative tests on getMappedProperty valid arguments.
     */
    public void testGetMappedValues() {

        Object value = null;

        // Use explicit key argument

        try {
            value = PropertyUtils.getMappedProperty(bean, "mappedProperty",
                                                    "First Key");
            assertEquals("Can find first value", "First Value", value);
        } catch (Throwable t) {
            fail("Finding first value threw " + t);
        }

        try {
            value = PropertyUtils.getMappedProperty(bean, "mappedProperty",
                                                    "Second Key");
            assertEquals("Can find second value", "Second Value", value);
        } catch (Throwable t) {
            fail("Finding second value threw " + t);
        }

        try {
            value = PropertyUtils.getMappedProperty(bean, "mappedProperty",
                                                    "Third Key");
            assertNull("Can not find third value", value);
        } catch (Throwable t) {
            fail("Finding third value threw " + t);
        }

        // Use key expression

        try {
            value =
                PropertyUtils.getMappedProperty(bean,
                                                "mappedProperty(First Key)");
            assertEquals("Can find first value", "First Value", value);
        } catch (Throwable t) {
            fail("Finding first value threw " + t);
        }

        try {
            value =
                PropertyUtils.getMappedProperty(bean,
                                                "mappedProperty(Second Key)");
            assertEquals("Can find second value", "Second Value", value);
        } catch (Throwable t) {
            fail("Finding second value threw " + t);
        }

        try {
            value =
                PropertyUtils.getMappedProperty(bean,
                                                "mappedProperty(Third Key)");
            assertNull("Can not find third value", value);
        } catch (Throwable t) {
            fail("Finding third value threw " + t);
        }

    }


    /**
     * Corner cases on getNestedProperty invalid arguments.
     */
    public void testGetNestedArguments() {

        try {
            PropertyUtils.getNestedProperty(null, "stringProperty");
            fail("Should throw IllegalArgumentException 1");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 1");
        }

        try {
            PropertyUtils.getNestedProperty(bean, null);
            fail("Should throw IllegalArgumentException 2");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 2");
        }

    }


    /**
     * Test getNestedProperty on a boolean property.
     */
    public void testGetNestedBoolean() {

        try {
            Object value =
                PropertyUtils.getNestedProperty
                (bean, "nested.booleanProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof Boolean));
            assertTrue("Got correct value",
                   ((Boolean) value).booleanValue() ==
                   bean.getNested().getBooleanProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test getNestedProperty on a double property.
     */
    public void testGetNestedDouble() {

        try {
            Object value =
                PropertyUtils.getNestedProperty
                (bean, "nested.doubleProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof Double));
            assertEquals("Got correct value",
                         ((Double) value).doubleValue(),
                         bean.getNested().getDoubleProperty(),
                         0.005);
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test getNestedProperty on a float property.
     */
    public void testGetNestedFloat() {

        try {
            Object value =
                PropertyUtils.getNestedProperty
                (bean, "nested.floatProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof Float));
            assertEquals("Got correct value",
                         ((Float) value).floatValue(),
                         bean.getNested().getFloatProperty(),
                         (float) 0.005);
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test getNestedProperty on an int property.
     */
    public void testGetNestedInt() {

        try {
            Object value =
                PropertyUtils.getNestedProperty
                (bean, "nested.intProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof Integer));
            assertEquals("Got correct value",
                         ((Integer) value).intValue(),
                         bean.getNested().getIntProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test getNestedProperty on a long property.
     */
    public void testGetNestedLong() {

        try {
            Object value =
                PropertyUtils.getNestedProperty
                (bean, "nested.longProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof Long));
            assertEquals("Got correct value",
                         ((Long) value).longValue(),
                         bean.getNested().getLongProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test getNestedProperty on a read-only String property.
     */
    public void testGetNestedReadOnly() {

        try {
            Object value =
                PropertyUtils.getNestedProperty
                (bean, "nested.readOnlyProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof String));
            assertEquals("Got correct value",
                         (String) value,
                         bean.getReadOnlyProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test getNestedProperty on a short property.
     */
    public void testGetNestedShort() {

        try {
            Object value =
                PropertyUtils.getNestedProperty
                (bean, "nested.shortProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof Short));
            assertEquals("Got correct value",
                         ((Short) value).shortValue(),
                         bean.getNested().getShortProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test getNestedProperty on a String property.
     */
    public void testGetNestedString() {

        try {
            Object value =
                PropertyUtils.getNestedProperty
                (bean, "nested.stringProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof String));
            assertEquals("Got correct value",
                         ((String) value),
                         bean.getNested().getStringProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Negative test getNestedProperty on an unknown property.
     */
    public void testGetNestedUnknown() {

        try {
            Object value =
                PropertyUtils.getNestedProperty
                (bean, "nested.unknown");
            fail("Should have thrown NoSuchMethodException");
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            ; // Correct result for this test
        }

    }


    /**
     * Test getNestedProperty on a write-only String property.
     */
    public void testGetNestedWriteOnly() {

        try {
            Object value =
                PropertyUtils.getNestedProperty
                (bean, "writeOnlyProperty");
            fail("Should have thrown NoSuchMethodException");
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            ; // Correct result for this test
        }

    }


    /**
     * Test getting accessible property reader methods for a specified
     * list of properties of our standard test bean.
     */
    public void testGetReadMethodBasic() {

        testGetReadMethod(bean, properties, TEST_BEAN_CLASS);

    }


    /**
     * Test getting accessible property reader methods for a specified
     * list of properties of a package private subclass of our standard
     * test bean.
     */
    public void testGetReadMethodPackageSubclass() {

        testGetReadMethod(beanPackageSubclass, properties, TEST_BEAN_CLASS);

    }


    /**
     * Test getting accessible property reader methods for a specified
     * list of properties that are declared either directly or via
     * implemented interfaces.
     */
    public void testGetReadMethodPublicInterface() {

        // Properties "bar" and "baz" are visible via implemented interfaces
        // (one direct and one indirect)
        testGetReadMethod(beanPrivate,
                          new String[] { "bar" },
                          PRIVATE_DIRECT_CLASS);
        testGetReadMethod(beanPrivate,
                          new String[] { "baz" },
                          PRIVATE_INDIRECT_CLASS);

        // Properties "bar" and "baz" are visible via implemented interfaces
        // (one direct and one indirect).  The interface is implemented in
        // a superclass
        testGetReadMethod(beanPrivateSubclass,
                          new String[] { "bar" },
                          PRIVATE_DIRECT_CLASS);
        testGetReadMethod(beanPrivateSubclass,
                          new String[] { "baz" },
                          PRIVATE_INDIRECT_CLASS);

        // Property "foo" is not accessible because the underlying
        // class has package scope
        PropertyDescriptor pd[] =
            PropertyUtils.getPropertyDescriptors(beanPrivate);
        int n = -1;
        for (int i = 0; i < pd.length; i++) {
            if ("foo".equals(pd[i].getName())) {
                n = i;
                break;
            }
        }
        assertTrue("Found foo descriptor", n >= 0);
        Method reader = pd[n].getReadMethod();
        assertNotNull("Found foo read method", reader);
        Object value = null;
        try {
            value = reader.invoke(beanPrivate, new Class[0]);
            fail("Foo reader did throw IllegalAccessException");
        } catch (IllegalAccessException e) {
            ; // Expected result for this test
        } catch (Throwable t) {
            fail("Invoke foo reader: " + t);
        }

    }


    /**
     * Test getting accessible property reader methods for a specified
     * list of properties of a public subclass of our standard test bean.
     */
    public void testGetReadMethodPublicSubclass() {

        testGetReadMethod(beanPublicSubclass, properties, TEST_BEAN_CLASS);

    }


    /**
     * Corner cases on getSimpleProperty invalid arguments.
     */
    public void testGetSimpleArguments() {

        try {
            PropertyUtils.getSimpleProperty(null, "stringProperty");
            fail("Should throw IllegalArgumentException 1");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 1");
        }

        try {
            PropertyUtils.getSimpleProperty(bean, null);
            fail("Should throw IllegalArgumentException 2");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 2");
        }

    }


    /**
     * Test getSimpleProperty on a boolean property.
     */
    public void testGetSimpleBoolean() {

        try {
            Object value =
                PropertyUtils.getSimpleProperty(bean,
                                                "booleanProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof Boolean));
            assertTrue("Got correct value",
                   ((Boolean) value).booleanValue() ==
                   bean.getBooleanProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test getSimpleProperty on a double property.
     */
    public void testGetSimpleDouble() {

        try {
            Object value =
                PropertyUtils.getSimpleProperty(bean,
                                                "doubleProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof Double));
            assertEquals("Got correct value",
                         ((Double) value).doubleValue(),
                         bean.getDoubleProperty(),
                         (double) 0.005);
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test getSimpleProperty on a float property.
     */
    public void testGetSimpleFloat() {

        try {
            Object value =
                PropertyUtils.getSimpleProperty(bean,
                                                "floatProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof Float));
            assertEquals("Got correct value",
                         ((Float) value).floatValue(),
                         bean.getFloatProperty(),
                         (float) 0.005);
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Negative test getSimpleProperty on an indexed property.
     */
    public void testGetSimpleIndexed() {

        Object value = null;
        try {
            value = PropertyUtils.getSimpleProperty(bean,
                                                    "intIndexed[0]");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            ; // Correct result for this test
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test getSimpleProperty on an int property.
     */
    public void testGetSimpleInt() {

        try {
            Object value =
                PropertyUtils.getSimpleProperty(bean,
                                                "intProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof Integer));
            assertEquals("Got correct value",
                         ((Integer) value).intValue(),
                         bean.getIntProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test getSimpleProperty on a long property.
     */
    public void testGetSimpleLong() {

        try {
            Object value =
                PropertyUtils.getSimpleProperty(bean,
                                                "longProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof Long));
            assertEquals("Got correct value",
                         ((Long) value).longValue(),
                         bean.getLongProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Negative test getSimpleProperty on a nested property.
     */
    public void testGetSimpleNested() {

        Object value = null;
        try {
            value = PropertyUtils.getSimpleProperty(bean,
                                                    "nested.stringProperty");
            fail("Should have thrown IllegaArgumentException");
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            ; // Correct result for this test
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test getSimpleProperty on a read-only String property.
     */
    public void testGetSimpleReadOnly() {

        try {
            Object value =
                PropertyUtils.getSimpleProperty(bean,
                                                "readOnlyProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof String));
            assertEquals("Got correct value",
                         (String) value,
                         bean.getReadOnlyProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test getSimpleProperty on a short property.
     */
    public void testGetSimpleShort() {

        try {
            Object value =
                PropertyUtils.getSimpleProperty(bean,
                                                "shortProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof Short));
            assertEquals("Got correct value",
                         ((Short) value).shortValue(),
                         bean.getShortProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test getSimpleProperty on a String property.
     */
    public void testGetSimpleString() {

        try {
            Object value =
                PropertyUtils.getSimpleProperty(bean,
                                                "stringProperty");
            assertNotNull("Got a value", value);
            assertTrue("Got correct type", (value instanceof String));
            assertEquals("Got correct value",
                         (String) value,
                         bean.getStringProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Negative test getSimpleProperty on an unknown property.
     */
    public void testGetSimpleUnknown() {

        try {
            Object value =
                PropertyUtils.getSimpleProperty(bean,
                                                "unknown");
            fail("Should have thrown NoSuchMethodException");
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            ; // Correct result for this test
        }

    }


    /**
     * Test getSimpleProperty on a write-only String property.
     */
    public void testGetSimpleWriteOnly() {

        try {
            Object value =
                PropertyUtils.getSimpleProperty(bean,
                                                "writeOnlyProperty");
            fail("Should have thrown NoSuchMethodException");
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            ; // Correct result for this test
        }

    }


    /**
     * Test getting accessible property writer methods for a specified
     * list of properties of our standard test bean.
     */
    public void testGetWriteMethodBasic() {

        testGetWriteMethod(bean, properties, TEST_BEAN_CLASS);

    }


    /**
     * Test getting accessible property writer methods for a specified
     * list of properties of a package private subclass of our standard
     * test bean.
     */
    public void testGetWriteMethodPackageSubclass() {

        testGetWriteMethod(beanPackageSubclass, properties, TEST_BEAN_CLASS);

    }


    /**
     * Test getting accessible property writer methods for a specified
     * list of properties of a public subclass of our standard test bean.
     */
    public void testGetWriteMethodPublicSubclass() {

        testGetWriteMethod(beanPublicSubclass, properties, TEST_BEAN_CLASS);

    }


    /**
     * Test the mappedPropertyType of MappedPropertyDescriptor.
     */
    public void testMappedPropertyType() throws Exception {

        MappedPropertyDescriptor desc;

        // Check a String property
        desc = (MappedPropertyDescriptor)
            PropertyUtils.getPropertyDescriptor(bean,
                                                "mappedProperty");
        assertEquals(String.class, desc.getMappedPropertyType());

        // Check an int property
        desc = (MappedPropertyDescriptor)
            PropertyUtils.getPropertyDescriptor(bean,
                                                "mappedIntProperty");
        assertEquals(Integer.TYPE, desc.getMappedPropertyType());

    }


    /**
     * Corner cases on setIndexedProperty invalid arguments.
     */
    public void testSetIndexedArguments() {

        // Use explicit index argument

        try {
            PropertyUtils.setIndexedProperty(null, "intArray", 0,
                                             new Integer(1));
            fail("Should throw IllegalArgumentException 1");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 1");
        }

        try {
            PropertyUtils.setIndexedProperty(bean, null, 0,
                                             new Integer(1));
            fail("Should throw IllegalArgumentException 2");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 2");
        }

        // Use index expression

        try {
            PropertyUtils.setIndexedProperty(null,
                                            "intArray[0]",
                                             new Integer(1));
            fail("Should throw IllegalArgumentException 3");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 3");
        }

        try {
            PropertyUtils.setIndexedProperty(bean, "[0]",
                                             new Integer(1));
            fail("Should throw NoSuchMethodException 4");
        } catch (NoSuchMethodException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of NoSuchMethodException 4");
        }

        try {
            PropertyUtils.setIndexedProperty(bean, "intArray",
                                             new Integer(1));
            fail("Should throw IllegalArgumentException 5");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 5");
        }

        // Use explicit index argument

        try {
            PropertyUtils.setIndexedProperty(null, "intIndexed", 0,
                                             new Integer(1));
            fail("Should throw IllegalArgumentException 1");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 1");
        }

        try {
            PropertyUtils.setIndexedProperty(bean, null, 0,
                                             new Integer(1));
            fail("Should throw IllegalArgumentException 2");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 2");
        }

        // Use index expression

        try {
            PropertyUtils.setIndexedProperty(null,
                                            "intIndexed[0]",
                                             new Integer(1));
            fail("Should throw IllegalArgumentException 3");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 3");
        }

        try {
            PropertyUtils.setIndexedProperty(bean, "[0]",
                                             new Integer(1));
            fail("Should throw NoSuchMethodException 4");
        } catch (NoSuchMethodException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of NoSuchMethodException 4");
        }

        try {
            PropertyUtils.setIndexedProperty(bean, "intIndexed",
                                             new Integer(1));
            fail("Should throw IllegalArgumentException 5");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 5");
        }

    }


    /**
     * Positive and negative tests on setIndexedProperty valid arguments.
     */
    public void testSetIndexedValues() {

        Object value = null;

        // Use explicit index argument

        try {
            PropertyUtils.setIndexedProperty(bean,
                                             "intArray", 0,
                                             new Integer(1));
            value =
                PropertyUtils.getIndexedProperty(bean,
                                                 "intArray", 0);
            assertNotNull("Returned new value 0", value);
            assertTrue("Returned Integer new value 0",
                       value instanceof Integer);
            assertEquals("Returned correct new value 0", 1,
                         ((Integer) value).intValue());
        } catch (Throwable t) {
            fail("Threw " + t);
        }

        try {
            PropertyUtils.setIndexedProperty(bean,
                                             "intIndexed", 1,
                                             new Integer(11));
            value =
                PropertyUtils.getIndexedProperty(bean,
                                                 "intIndexed", 1);
            assertNotNull("Returned new value 1", value);
            assertTrue("Returned Integer new value 1",
                       value instanceof Integer);
            assertEquals("Returned correct new value 1", 11,
                         ((Integer) value).intValue());
        } catch (Throwable t) {
            fail("Threw " + t);
        }

        try {
            PropertyUtils.setIndexedProperty(bean,
                                             "listIndexed", 2,
                                             "New Value 2");
            value =
                PropertyUtils.getIndexedProperty(bean,
                                                 "listIndexed", 2);
            assertNotNull("Returned new value 2", value);
            assertTrue("Returned String new value 2",
                       value instanceof String);
            assertEquals("Returned correct new value 2", "New Value 2",
                         (String) value);
        } catch (Throwable t) {
            fail("Threw " + t);
        }

        try {
            PropertyUtils.setIndexedProperty(bean,
                                             "stringArray", 2,
                                             "New Value 2");
            value =
                PropertyUtils.getIndexedProperty(bean,
                                                 "stringArray", 2);
            assertNotNull("Returned new value 2", value);
            assertTrue("Returned String new value 2",
                       value instanceof String);
            assertEquals("Returned correct new value 2", "New Value 2",
                         (String) value);
        } catch (Throwable t) {
            fail("Threw " + t);
        }

        try {
            PropertyUtils.setIndexedProperty(bean,
                                             "stringArray", 3,
                                             "New Value 3");
            value =
                PropertyUtils.getIndexedProperty(bean,
                                                 "stringArray", 3);
            assertNotNull("Returned new value 3", value);
            assertTrue("Returned String new value 3",
                       value instanceof String);
            assertEquals("Returned correct new value 3", "New Value 3",
                         (String) value);
        } catch (Throwable t) {
            fail("Threw " + t);
        }

        // Use index expression

        try {
            PropertyUtils.setIndexedProperty(bean,
                                             "intArray[4]",
                                             new Integer(1));
            value =
                PropertyUtils.getIndexedProperty(bean,
                                                 "intArray[4]");
            assertNotNull("Returned new value 4", value);
            assertTrue("Returned Integer new value 4",
                       value instanceof Integer);
            assertEquals("Returned correct new value 4", 1,
                         ((Integer) value).intValue());
        } catch (Throwable t) {
            fail("Threw " + t);
        }

        try {
            PropertyUtils.setIndexedProperty(bean,
                                             "intIndexed[3]",
                                             new Integer(11));
            value =
                PropertyUtils.getIndexedProperty(bean,
                                                 "intIndexed[3]");
            assertNotNull("Returned new value 5", value);
            assertTrue("Returned Integer new value 5",
                       value instanceof Integer);
            assertEquals("Returned correct new value 5", 11,
                         ((Integer) value).intValue());
        } catch (Throwable t) {
            fail("Threw " + t);
        }

        try {
            PropertyUtils.setIndexedProperty(bean,
                                             "listIndexed[1]",
                                             "New Value 2");
            value =
                PropertyUtils.getIndexedProperty(bean,
                                                 "listIndexed[1]");
            assertNotNull("Returned new value 6", value);
            assertTrue("Returned String new value 6",
                       value instanceof String);
            assertEquals("Returned correct new value 6", "New Value 2",
                         (String) value);
        } catch (Throwable t) {
            fail("Threw " + t);
        }

        try {
            PropertyUtils.setIndexedProperty(bean,
                                             "stringArray[1]",
                                             "New Value 2");
            value =
                PropertyUtils.getIndexedProperty(bean,
                                                 "stringArray[2]");
            assertNotNull("Returned new value 6", value);
            assertTrue("Returned String new value 6",
                       value instanceof String);
            assertEquals("Returned correct new value 6", "New Value 2",
                         (String) value);
        } catch (Throwable t) {
            fail("Threw " + t);
        }

        try {
            PropertyUtils.setIndexedProperty(bean,
                                             "stringArray[0]",
                                             "New Value 3");
            value =
                PropertyUtils.getIndexedProperty(bean,
                                                 "stringArray[0]");
            assertNotNull("Returned new value 7", value);
            assertTrue("Returned String new value 7",
                       value instanceof String);
            assertEquals("Returned correct new value 7", "New Value 3",
                         (String) value);
        } catch (Throwable t) {
            fail("Threw " + t);
        }

        // Index out of bounds tests

        try {
            PropertyUtils.setIndexedProperty(bean,
                                             "intArray", -1,
                                             new Integer(0));
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException t) {
            ; // Expected results
        } catch (Throwable t) {
            fail("Threw " + t + " instead of ArrayIndexOutOfBoundsException");
        }

        try {
            PropertyUtils.setIndexedProperty(bean,
                                             "intArray", 5,
                                             new Integer(0));
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException t) {
            ; // Expected results
        } catch (Throwable t) {
            fail("Threw " + t + " instead of ArrayIndexOutOfBoundsException");
        }

        try {
            PropertyUtils.setIndexedProperty(bean,
                                             "intIndexed", -1,
                                             new Integer(0));
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException t) {
            ; // Expected results
        } catch (Throwable t) {
            fail("Threw " + t + " instead of ArrayIndexOutOfBoundsException");
        }

        try {
            PropertyUtils.setIndexedProperty(bean,
                                             "intIndexed", 5,
                                             new Integer(0));
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException t) {
            ; // Expected results
        } catch (Throwable t) {
            fail("Threw " + t + " instead of ArrayIndexOutOfBoundsException");
        }

        try {
            PropertyUtils.setIndexedProperty(bean,
                                             "listIndexed", 5,
                                             "New String");
            fail("Should have thrown IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException t) {
            ; // Expected results
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IndexOutOfBoundsException");
        }

        try {
            PropertyUtils.setIndexedProperty(bean,
                                             "listIndexed", -1,
                                             "New String");
            fail("Should have thrown IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException t) {
            ; // Expected results
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IndexOutOfBoundsException");
        }

        try {
            PropertyUtils.setIndexedProperty(bean,
                                             "stringArray", -1,
                                             "New String");
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException t) {
            ; // Expected results
        } catch (Throwable t) {
            fail("Threw " + t + " instead of ArrayIndexOutOfBoundsException");
        }

        try {
            PropertyUtils.setIndexedProperty(bean,
                                             "stringArray", 5,
                                             "New String");
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException t) {
            ; // Expected results
        } catch (Throwable t) {
            fail("Threw " + t + " instead of ArrayIndexOutOfBoundsException");
        }

        try {
            PropertyUtils.setIndexedProperty(bean,
                                             "stringIndexed", -1,
                                             "New String");
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException t) {
            ; // Expected results
        } catch (Throwable t) {
            fail("Threw " + t + " instead of ArrayIndexOutOfBoundsException");
        }

        try {
            PropertyUtils.setIndexedProperty(bean,
                                             "stringIndexed", 5,
                                             "New String");
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException t) {
            ; // Expected results
        } catch (Throwable t) {
            fail("Threw " + t + " instead of ArrayIndexOutOfBoundsException");
        }

    }


    /**
     * Corner cases on getMappedProperty invalid arguments.
     */
    public void testSetMappedArguments() {

        // Use explicit key argument

        try {
            PropertyUtils.setMappedProperty(null, "mappedProperty",
                                            "First Key", "First Value");
            fail("Should throw IllegalArgumentException 1");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 1");
        }

        try {
            PropertyUtils.setMappedProperty(bean, null, "First Key",
                                            "First Value");
            fail("Should throw IllegalArgumentException 2");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 2");
        }

        try {
            PropertyUtils.setMappedProperty(bean, "mappedProperty", null,
                                            "First Value");
            fail("Should throw IllegalArgumentException 3");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 3");
        }

        // Use key expression

        try {
            PropertyUtils.setMappedProperty(null,
                                            "mappedProperty(First Key)",
                                            "First Value");
            fail("Should throw IllegalArgumentException 4");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 4");
        }

        try {
            PropertyUtils.setMappedProperty(bean, "(Second Key)",
                                            "Second Value");
            fail("Should throw IllegalArgumentException 5");
        } catch (NoSuchMethodException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of NoSuchMethodException 5");
        }

        try {
            PropertyUtils.setMappedProperty(bean, "mappedProperty",
                                            "Third Value");
            fail("Should throw IllegalArgumentException 6");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 6");
        }

    }


    /**
     * Positive and negative tests on setMappedProperty valid arguments.
     */
    public void testSetMappedValues() {

        Object value = null;

        // Use explicit key argument

        try {
            value = PropertyUtils.getMappedProperty(bean, "mappedProperty",
                                                    "Fourth Key");
            assertNull("Can not find fourth value", value);
        } catch (Throwable t) {
            fail("Finding fourth value threw " + t);
        }

        try {
            PropertyUtils.setMappedProperty(bean, "mappedProperty",
                                            "Fourth Key", "Fourth Value");
        } catch (Throwable t) {
            fail("Setting fourth value threw " + t);
        }

        try {
            value = PropertyUtils.getMappedProperty(bean, "mappedProperty",
                                                    "Fourth Key");
            assertEquals("Can find fourth value", "Fourth Value", value);
        } catch (Throwable t) {
            fail("Finding fourth value threw " + t);
        }

        // Use key expression

        try {
            value =
                PropertyUtils.getMappedProperty(bean,
                                                "mappedProperty(Fifth Key)");
            assertNull("Can not find fifth value", value);
        } catch (Throwable t) {
            fail("Finding fifth value threw " + t);
        }

        try {
            PropertyUtils.setMappedProperty(bean,
                                            "mappedProperty(Fifth Key)",
                                            "Fifth Value");
        } catch (Throwable t) {
            fail("Setting fifth value threw " + t);
        }

        try {
            value =
                PropertyUtils.getMappedProperty(bean,
                                                "mappedProperty(Fifth Key)");
            assertEquals("Can find fifth value", "Fifth Value", value);
        } catch (Throwable t) {
            fail("Finding fifth value threw " + t);
        }

    }


    /**
     * Corner cases on setNestedProperty invalid arguments.
     */
    public void testSetNestedArguments() {

        try {
            PropertyUtils.setNestedProperty(null, "stringProperty", "");
            fail("Should throw IllegalArgumentException 1");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 1");
        }

        try {
            PropertyUtils.setNestedProperty(bean, null, "");
            fail("Should throw IllegalArgumentException 2");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 2");
        }

    }


    /**
     * Test setNextedProperty on a boolean property.
     */
    public void testSetNestedBoolean() {

        try {
            boolean oldValue = bean.getNested().getBooleanProperty();
            boolean newValue = !oldValue;
            PropertyUtils.setNestedProperty(bean,
                                            "nested.booleanProperty",
                                            new Boolean(newValue));
            assertTrue("Matched new value",
                   newValue ==
                   bean.getNested().getBooleanProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test setNestedProperty on a double property.
     */
    public void testSetNestedDouble() {

        try {
            double oldValue = bean.getNested().getDoubleProperty();
            double newValue = oldValue + 1.0;
            PropertyUtils.setNestedProperty(bean,
                                            "nested.doubleProperty",
                                            new Double(newValue));
            assertEquals("Matched new value",
                         newValue,
                         bean.getNested().getDoubleProperty(),
                         0.005);
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test setNestedProperty on a float property.
     */
    public void testSetNestedFloat() {

        try {
            float oldValue = bean.getNested().getFloatProperty();
            float newValue = oldValue + (float) 1.0;
            PropertyUtils.setNestedProperty(bean,
                                            "nested.floatProperty",
                                            new Float(newValue));
            assertEquals("Matched new value",
                         newValue,
                         bean.getNested().getFloatProperty(),
                         (float) 0.005);
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test setNestedProperty on a int property.
     */
    public void testSetNestedInt() {

        try {
            int oldValue = bean.getNested().getIntProperty();
            int newValue = oldValue + 1;
            PropertyUtils.setNestedProperty(bean,
                                            "nested.intProperty",
                                            new Integer(newValue));
            assertEquals("Matched new value",
                         newValue,
                         bean.getNested().getIntProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test setNestedProperty on a long property.
     */
    public void testSetNestedLong() {

        try {
            long oldValue = bean.getNested().getLongProperty();
            long newValue = oldValue + 1;
            PropertyUtils.setNestedProperty(bean,
                                            "nested.longProperty",
                                            new Long(newValue));
            assertEquals("Matched new value",
                         newValue,
                         bean.getNested().getLongProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test setNestedProperty on a read-only String property.
     */
    public void testSetNestedReadOnly() {

        try {
            String oldValue = bean.getNested().getWriteOnlyPropertyValue();
            String newValue = oldValue + " Extra Value";
            PropertyUtils.setNestedProperty(bean,
                                            "nested.readOnlyProperty",
                                            newValue);
            fail("Should have thrown NoSuchMethodException");
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            ; // Correct result for this test
        }

    }


    /**
     * Test setNestedProperty on a short property.
     */
    public void testSetNestedShort() {

        try {
            short oldValue = bean.getNested().getShortProperty();
            short newValue = oldValue; newValue++;
            PropertyUtils.setNestedProperty(bean,
                                            "nested.shortProperty",
                                            new Short(newValue));
            assertEquals("Matched new value",
                         newValue,
                         bean.getNested().getShortProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test setNestedProperty on a String property.
     */
    public void testSetNestedString() {

        try {
            String oldValue = bean.getNested().getStringProperty();
            String newValue = oldValue + " Extra Value";
            PropertyUtils.setNestedProperty(bean,
                                            "nested.stringProperty",
                                            newValue);
            assertEquals("Matched new value",
                         newValue,
                         bean.getNested().getStringProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test setNestedProperty on an unknown property name.
     */
    public void testSetNestedUnknown() {

        try {
            String newValue = "New String Value";
            PropertyUtils.setNestedProperty(bean,
                                            "nested.unknown",
                                            newValue);
            fail("Should have thrown NoSuchMethodException");
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            ; // Correct result for this test
        }

    }


    /**
     * Test setNestedProperty on a write-only String property.
     */
    public void testSetNestedWriteOnly() {

        try {
            String oldValue = bean.getNested().getWriteOnlyPropertyValue();
            String newValue = oldValue + " Extra Value";
            PropertyUtils.setNestedProperty(bean,
                                            "nested.writeOnlyProperty",
                                            newValue);
            assertEquals("Matched new value",
                         newValue,
                         bean.getNested().getWriteOnlyPropertyValue());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Corner cases on setSimpleProperty invalid arguments.
     */
    public void testSetSimpleArguments() {

        try {
            PropertyUtils.setSimpleProperty(null, "stringProperty", "");
            fail("Should throw IllegalArgumentException 1");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 1");
        }

        try {
            PropertyUtils.setSimpleProperty(bean, null, "");
            fail("Should throw IllegalArgumentException 2");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        } catch (Throwable t) {
            fail("Threw " + t + " instead of IllegalArgumentException 2");
        }

    }


    /**
     * Test setSimpleProperty on a boolean property.
     */
    public void testSetSimpleBoolean() {

        try {
            boolean oldValue = bean.getBooleanProperty();
            boolean newValue = !oldValue;
            PropertyUtils.setSimpleProperty(bean,
                                            "booleanProperty",
                                            new Boolean(newValue));
            assertTrue("Matched new value",
                   newValue ==
                   bean.getBooleanProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test setSimpleProperty on a double property.
     */
    public void testSetSimpleDouble() {

        try {
            double oldValue = bean.getDoubleProperty();
            double newValue = oldValue + 1.0;
            PropertyUtils.setSimpleProperty(bean,
                                            "doubleProperty",
                                            new Double(newValue));
            assertEquals("Matched new value",
                         newValue,
                         bean.getDoubleProperty(),
                         0.005);
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test setSimpleProperty on a float property.
     */
    public void testSetSimpleFloat() {

        try {
            float oldValue = bean.getFloatProperty();
            float newValue = oldValue + (float) 1.0;
            PropertyUtils.setSimpleProperty(bean,
                                            "floatProperty",
                                            new Float(newValue));
            assertEquals("Matched new value",
                         newValue,
                         bean.getFloatProperty(),
                         (float) 0.005);
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Negative test setSimpleProperty on an indexed property.
     */
    public void testSetSimpleIndexed() {

        try {
            PropertyUtils.setSimpleProperty(bean,
                                            "stringIndexed[0]",
                                            "New String Value");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            ; // Correct result for this test
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test setSimpleProperty on a int property.
     */
    public void testSetSimpleInt() {

        try {
            int oldValue = bean.getIntProperty();
            int newValue = oldValue + 1;
            PropertyUtils.setSimpleProperty(bean,
                                            "intProperty",
                                            new Integer(newValue));
            assertEquals("Matched new value",
                         newValue,
                         bean.getIntProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test setSimpleProperty on a long property.
     */
    public void testSetSimpleLong() {

        try {
            long oldValue = bean.getLongProperty();
            long newValue = oldValue + 1;
            PropertyUtils.setSimpleProperty(bean,
                                            "longProperty",
                                            new Long(newValue));
            assertEquals("Matched new value",
                         newValue,
                         bean.getLongProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Negative test setSimpleProperty on a nested property.
     */
    public void testSetSimpleNested() {

        try {
            PropertyUtils.setSimpleProperty(bean,
                                            "nested.stringProperty",
                                            "New String Value");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            ; // Correct result for this test
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test setSimpleProperty on a read-only String property.
     */
    public void testSetSimpleReadOnly() {

        try {
            String oldValue = bean.getWriteOnlyPropertyValue();
            String newValue = oldValue + " Extra Value";
            PropertyUtils.setSimpleProperty(bean,
                                            "readOnlyProperty",
                                            newValue);
            fail("Should have thrown NoSuchMethodException");
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            ; // Correct result for this test
        }

    }


    /**
     * Test setSimpleProperty on a short property.
     */
    public void testSetSimpleShort() {

        try {
            short oldValue = bean.getShortProperty();
            short newValue = oldValue; newValue++;
            PropertyUtils.setSimpleProperty(bean,
                                            "shortProperty",
                                            new Short(newValue));
            assertEquals("Matched new value",
                         newValue,
                         bean.getShortProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test setSimpleProperty on a String property.
     */
    public void testSetSimpleString() {

        try {
            String oldValue = bean.getStringProperty();
            String newValue = oldValue + " Extra Value";
            PropertyUtils.setSimpleProperty(bean,
                                            "stringProperty",
                                            newValue);
            assertEquals("Matched new value",
                         newValue,
                         bean.getStringProperty());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Test setSimpleProperty on an unknown property name.
     */
    public void testSetSimpleUnknown() {

        try {
            String newValue = "New String Value";
            PropertyUtils.setSimpleProperty(bean,
                                            "unknown",
                                            newValue);
            fail("Should have thrown NoSuchMethodException");
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            ; // Correct result for this test
        }

    }


    /**
     * Test setSimpleProperty on a write-only String property.
     */
    public void testSetSimpleWriteOnly() {

        try {
            String oldValue = bean.getWriteOnlyPropertyValue();
            String newValue = oldValue + " Extra Value";
            PropertyUtils.setSimpleProperty(bean,
                                            "writeOnlyProperty",
                                            newValue);
            assertEquals("Matched new value",
                         newValue,
                         bean.getWriteOnlyPropertyValue());
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Base for testGetDescriptorXxxxx() series of tests.
     *
     * @param name Name of the property to be retrieved
     * @param read Expected name of the read method (or null)
     * @param write Expected name of the write method (or null)
     */
    protected void testGetDescriptorBase(String name, String read,
                                         String write) {

        try {
            PropertyDescriptor pd =
                PropertyUtils.getPropertyDescriptor(bean, name);
            if ((read != null) || (write != null)) {
                assertNotNull("Got descriptor", pd);
            } else {
                assertNull("Got descriptor", pd);
                return;
            }
            Method rm = pd.getReadMethod();
            if (read != null) {
                assertNotNull("Got read method", rm);
                assertEquals("Got correct read method",
                             rm.getName(), read);
            } else {
                assertNull("Got read method", rm);
            }
            Method wm = pd.getWriteMethod();
            if (write != null) {
                assertNotNull("Got write method", wm);
                assertEquals("Got correct write method",
                             wm.getName(), write);
            } else {
                assertNull("Got write method", wm);
            }
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException");
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException");
        }

    }


    /**
     * Base for testGetReadMethod() series of tests.
     *
     * @param bean Bean for which to retrieve read methods.
     * @param properties Property names to search for
     * @param className Class name where this method should be defined
     */
    protected void testGetReadMethod(Object bean, String properties[],
                                     String className) {

        PropertyDescriptor pd[] =
            PropertyUtils.getPropertyDescriptors(bean);
        for (int i = 0; i < properties.length; i++) {

            // Identify the property descriptor for this property
            if (properties[i].equals("intIndexed"))
                continue;
            if (properties[i].equals("stringIndexed"))
                continue;
            if (properties[i].equals("writeOnlyProperty"))
                continue;
            int n = -1;
            for (int j = 0; j < pd.length; j++) {
                if (properties[i].equals(pd[j].getName())) {
                    n = j;
                    break;
                }
            }
            assertTrue("PropertyDescriptor for " + properties[i],
                   n >= 0);

            // Locate an accessible property reader method for it
            Method reader = PropertyUtils.getReadMethod(pd[n]);
            assertNotNull("Reader for " + properties[i],
                          reader);
            Class clazz = reader.getDeclaringClass();
            assertNotNull("Declaring class for " + properties[i],
                          clazz);
            assertEquals("Correct declaring class for " + properties[i],
                         clazz.getName(),
                         className);

            // Actually call the reader method we received
            try {
                Object value =
                    reader.invoke(bean, new Class[0]);
            } catch (Throwable t) {
                fail("Call for " + properties[i] + ": " + t);
            }

        }

    }


    /**
     * Base for testGetWriteMethod() series of tests.
     *
     * @param bean Bean for which to retrieve write methods.
     * @param properties Property names to search for
     * @param className Class name where this method should be defined
     */
    protected void testGetWriteMethod(Object bean, String properties[],
                                      String className) {


        PropertyDescriptor pd[] =
            PropertyUtils.getPropertyDescriptors(bean);
        for (int i = 0; i < properties.length; i++) {

            // Identify the property descriptor for this property
            if (properties[i].equals("intIndexed"))
                continue;
            if (properties[i].equals("listIndexed"))
                continue;
            if (properties[i].equals("nested"))
                continue; // This property is read only
            if (properties[i].equals("readOnlyProperty"))
                continue;
            if (properties[i].equals("stringIndexed"))
                continue;
            int n = -1;
            for (int j = 0; j < pd.length; j++) {
                if (properties[i].equals(pd[j].getName())) {
                    n = j;
                    break;
                }
            }
            assertTrue("PropertyDescriptor for " + properties[i],
                   n >= 0);

            // Locate an accessible property reader method for it
            Method writer = PropertyUtils.getWriteMethod(pd[n]);
            assertNotNull("Writer for " + properties[i],
                          writer);
            Class clazz = writer.getDeclaringClass();
            assertNotNull("Declaring class for " + properties[i],
                          clazz);
            assertEquals("Correct declaring class for " + properties[i],
                         clazz.getName(),
                         className);

        }

    }


}
