/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jdt.ui.tests.performance;

import java.util.ArrayList;

import org.eclipse.test.performance.PerformanceTestCase;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.ITypeNameRequestor;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchPattern;

import org.eclipse.jdt.internal.corext.util.AllTypesCache;

public class JdtPerformanceTestCase extends PerformanceTestCase {

	private static class Requestor implements ITypeNameRequestor {
		public void acceptClass(char[] packageName, char[] simpleTypeName, char[][] enclosingTypeNames, String path) {
		}
		public void acceptInterface(char[] packageName, char[] simpleTypeName, char[][] enclosingTypeNames, String path) {
		}
	}

	public JdtPerformanceTestCase() {
		super();
	}
	
	public JdtPerformanceTestCase(String name) {
		super(name);
	}
	
	protected void joinBackgroudActivities() throws CoreException {
		// Join all background activities
		boolean interrupted= true;
		while (interrupted) {
			try {
				Platform.getJobManager().join(null, null);
				interrupted= false;
			} catch (InterruptedException e) {
				interrupted= true;
			}
		}
		// Join indexing
		new SearchEngine().searchAllTypeNames(
			null,
			null,
			SearchPattern.R_EXACT_MATCH,
			IJavaSearchConstants.CLASS,
			SearchEngine.createJavaSearchScope(new IJavaElement[0]),
			new Requestor(),
			IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH,
			null);
		// Join all types cache
		AllTypesCache.getTypes(SearchEngine.createJavaSearchScope(new IJavaElement[0]), 
			IJavaSearchConstants.CLASS, new NullProgressMonitor(), new ArrayList());
	}

	protected void finishMeasurements() {
		stopMeasuring();
		commitMeasurements();
		assertPerformance();
	}
}
