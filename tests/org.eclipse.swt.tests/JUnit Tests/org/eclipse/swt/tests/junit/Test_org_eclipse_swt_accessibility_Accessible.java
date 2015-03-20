/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Red Hat Inc. - Adapt to JUnit 4.
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleControlListener;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleListener;
import org.eclipse.swt.accessibility.AccessibleTextEvent;
import org.eclipse.swt.accessibility.AccessibleTextListener;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.accessibility.Accessible
 *
 * @see org.eclipse.swt.accessibility.Accessible
 */
public class Test_org_eclipse_swt_accessibility_Accessible {	

@Before
public void setUp() {
	shell = new Shell();
	accessible = shell.getAccessible();
}

@After
public void tearDown() {
	assertEquals(false, shell.isDisposed());
	shell.dispose();
	assertTrue(shell.isDisposed());
}

@Test
public void test_addAccessibleControlListenerLorg_eclipse_swt_accessibility_AccessibleControlListener() {
	AccessibleControlListener listener = new AccessibleControlListener() {
		public void getValue(AccessibleControlEvent e) {
		}
		public void getChild(AccessibleControlEvent e) {
		}
		public void getChildAtPoint(AccessibleControlEvent e) {
		}
		public void getChildCount(AccessibleControlEvent e) {
		}
		public void getChildren(AccessibleControlEvent e) {
		}
		public void getDefaultAction(AccessibleControlEvent e) {
		}
		public void getFocus(AccessibleControlEvent e) {
		}
		public void getLocation(AccessibleControlEvent e) {
		}
		public void getRole(AccessibleControlEvent e) {
		}
		public void getSelection(AccessibleControlEvent e) {
		}
		public void getState(AccessibleControlEvent e) {
		}
	};
	accessible.addAccessibleControlListener(listener);
	accessible.removeAccessibleControlListener(listener);
}

@Test
public void test_addAccessibleListenerLorg_eclipse_swt_accessibility_AccessibleListener() {
	AccessibleListener listener = new AccessibleListener() {
		public void getName(AccessibleEvent e) {
		}
		public void getDescription(AccessibleEvent e) {
		}
		public void getHelp(AccessibleEvent e) {
		}
		public void getKeyboardShortcut(AccessibleEvent e) {
		}
	};
	accessible.addAccessibleListener(listener);
	accessible.removeAccessibleListener(listener);
}

@Test
public void test_addAccessibleTextListenerLorg_eclipse_swt_accessibility_AccessibleTextListener() {
	AccessibleTextListener listener = new AccessibleTextListener() {
		public void getSelectionRange(AccessibleTextEvent e) {
		}
		public void getCaretOffset(AccessibleTextEvent e) {
		}
	};
	accessible.addAccessibleTextListener(listener);
	accessible.removeAccessibleTextListener(listener);
}

@Test
public void test_getControl() {
	assertEquals(shell, accessible.getControl());
}

/* custom */
private Shell shell;
private Accessible accessible;
}
