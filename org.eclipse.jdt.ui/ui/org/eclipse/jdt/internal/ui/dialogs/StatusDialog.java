package org.eclipse.jdt.internal.ui.dialogs;
/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 1999, 2000
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.internal.ui.widgets.MessageLine;

/**
 * An abstract base class for dialogs with a status bar and ok/cancel buttons.
 * The status message must be passed over as StatusInfo object and can be
 * an error, warning or ok. The OK button is enabled / disabled depending
 * on the status.
 */ 
public abstract class StatusDialog extends Dialog {
	
	private Button fOkButton;
	private MessageLine fStatusLine;
	private IStatus fLastStatus;
	private String fTitle;
	private Image fImage;
	
	public StatusDialog(Shell parent) {
		super(parent);
	}
	
	/**
	 * Update the dialog's status line to reflect the given status. It is save to call
	 * this method before the dialog has been opened.
	 */
	protected void updateStatus(IStatus status) {
		fLastStatus= status;
		if (fStatusLine != null && !fStatusLine.isDisposed()) {
			updateButtonsEnableState(status);
			StatusTool.applyToStatusLine(fStatusLine, status);	
		}
	}	

	/**
	 * Update the status of the ok button to reflect the given status. Subclasses
	 * may override this method to update additional buttons.
	 */
	protected void updateButtonsEnableState(IStatus status) {
		if (fOkButton != null && !fOkButton.isDisposed())
			fOkButton.setEnabled(!status.matches(IStatus.ERROR));
	}
	
	/* (non-Javadoc)
	 * Method declared in Window.
	 */
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (fTitle != null)
			shell.setText(fTitle);
	}

	/*
	 * Non Java-Docd
	 */	
	public void create() {
		super.create();
		if (fLastStatus != null) {
			updateStatus(fLastStatus);
		}
	}

	/*
	 * Non Java-Docd
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		fOkButton= createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

	}
	
	/*
	 * Non Java-Docd
	 */				
	protected Control createButtonBar(Composite parent) {
		Composite composite= new Composite(parent, SWT.NULL);
		GridLayout layout= new GridLayout();
		layout.numColumns= 2;
		layout.marginHeight= 0;
		layout.marginWidth= convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		fStatusLine= new MessageLine(composite);
		fStatusLine.setAlignment(SWT.LEFT);
		fStatusLine.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fStatusLine.setMessage("");

		super.createButtonBar(composite);
		return composite;
	}
	
	/**
	 * Sets the title for this dialog.
	 *
	 * @param title the title
	 */
	public void setTitle(String title) {
		fTitle= title != null ? title : "";
		Shell shell= getShell();
		if (shell != null && !shell.isDisposed())
			shell.setText(fTitle);
	}

	/**
	 * Sets the image for this dialog.
	 *
	 * @param image the dialog's image
	 */
	public void setImage(Image image) {
		fImage= image;
		Shell shell= getShell();
		if (shell != null && !shell.isDisposed())
			shell.setImage(fImage);
	}	
}