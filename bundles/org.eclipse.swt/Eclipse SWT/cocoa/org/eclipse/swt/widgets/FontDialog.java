/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;

/**
 * Instances of this class allow the user to select a font
 * from all available fonts in the system.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class FontDialog extends Dialog {
	NSFontPanel panel;
	FontData fontData;
	RGB rgb;
	boolean selected;
	int fontID, fontSize;

/**
 * Constructs a new instance of this class given only its parent.
 *
 * @param parent a shell which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public FontDialog (Shell parent) {
	this (parent, SWT.APPLICATION_MODAL);
}

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a shell which will be the parent of the new instance
 * @param style the style of dialog to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public FontDialog (Shell parent, int style) {
	super (parent, checkStyle (parent, style));
	checkSubclass ();
}

void changeFont(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	selected = true;
}

/**
 * Returns a FontData object describing the font that was
 * selected in the dialog, or null if none is available.
 * 
 * @return the FontData for the selected font, or null
 * @deprecated use #getFontList ()
 */
public FontData getFontData () {
	return fontData;
}

/**
 * Returns a FontData set describing the font that was
 * selected in the dialog, or null if none is available.
 * 
 * @return the FontData for the selected font, or null
 * @since 2.1.1
 */
public FontData [] getFontList () {
	if (fontData == null) return null;
	FontData [] result = new FontData [1];
	result [0] = fontData;
	return result;
}

/**
 * Returns an RGB describing the color that was selected
 * in the dialog, or null if none is available.
 *
 * @return the RGB value for the selected color, or null
 *
 * @see PaletteData#getRGBs
 * 
 * @since 2.1
 */
public RGB getRGB () {
	return rgb;
}

/**
 * Makes the dialog visible and brings it to the front
 * of the display.
 *
 * @return a FontData object describing the font that was selected,
 *         or null if the dialog was cancelled or an error occurred
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
 * </ul>
 */
public FontData open () {
	Display display = parent != null ? parent.display : Display.getCurrent ();	
	panel = NSFontPanel.sharedFontPanel();

	// Install a callback so editing keys work.
	int /*long*/ panelClass = 0;
	int /*long*/ swtPanelClass = 0;
	Callback performKeyEquivalentCallback = null;
	String className = "SWTFontDialogPanel";
	performKeyEquivalentCallback = new Callback(this, "performKeyEquivalent", 3);
	int /*long*/ proc = performKeyEquivalentCallback.getAddress();
	if (proc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	swtPanelClass = OS.objc_allocateClassPair(OS.object_getClass(panel.id), className, 0);
	OS.class_addMethod(swtPanelClass, OS.sel_performKeyEquivalent_, proc, "@:@");
	OS.objc_registerClassPair(swtPanelClass);
	panelClass = OS.object_setClass(panel.id, swtPanelClass);
	
	panel.setTitle(NSString.stringWith(title != null ? title : ""));
	boolean create = fontData != null;
	Font font = create ? new Font(display, fontData) : display.getSystemFont();
	panel.setPanelFont(font.handle, false);
	SWTPanelDelegate delegate = (SWTPanelDelegate)new SWTPanelDelegate().alloc().init();
	int /*long*/ jniRef = OS.NewGlobalRef(this);
	if (jniRef == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.object_setInstanceVariable(delegate.id, Display.SWT_OBJECT, jniRef);
	panel.setDelegate(delegate);
	fontData = null;
	selected = false;
	panel.orderFront(null);
	display.setModalDialog(this);
	NSApplication.sharedApplication().runModalForWindow(panel);
	display.setModalDialog(null);
	OS.object_setClass(panel.id, panelClass);
	OS.objc_disposeClassPair(swtPanelClass);
	if (performKeyEquivalentCallback != null) performKeyEquivalentCallback.dispose();
	if (selected) {
		NSFont nsFont = panel.panelConvertFont(font.handle);
		if (nsFont != null) {
			fontData = Font.cocoa_new(display, nsFont).getFontData()[0];
		}
	}
	panel.setDelegate(null);
	delegate.release();
	OS.DeleteGlobalRef(jniRef);
	if (create) font.dispose();
	return fontData;
}

int /*long*/ performKeyEquivalent(int /*long*/ id, int /*long*/ sel, int /*long*/ arg) {
	NSEvent nsEvent = new NSEvent(arg);
	int stateMask = 0;
	int /*long*/ selector = 0;
	int /*long*/ modifierFlags = nsEvent.modifierFlags();
	if ((modifierFlags & OS.NSAlternateKeyMask) != 0) stateMask |= SWT.ALT;
	if ((modifierFlags & OS.NSShiftKeyMask) != 0) stateMask |= SWT.SHIFT;
	if ((modifierFlags & OS.NSControlKeyMask) != 0) stateMask |= SWT.CONTROL;
	if ((modifierFlags & OS.NSCommandKeyMask) != 0) stateMask |= SWT.COMMAND;
	if (stateMask == SWT.COMMAND) {
		short keyCode = nsEvent.keyCode ();
		switch (keyCode) {
			case 7: /* X */
				selector = OS.sel_cut_;
				break;
			case 8: /* C */
				selector = OS.sel_copy_;
				break;
			case 9: /* V */
				selector = OS.sel_paste_;
				break;
			case 0: /* A */
				selector = OS.sel_selectAll_;
				break;
		}
		
		if (selector != 0) {
			NSApplication.sharedApplication().sendAction(selector, null, panel);
			return 1;
		}
	}

	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	return OS.objc_msgSendSuper(super_struct, sel, arg);
}

void setColor_forAttribute(int /*long*/ id, int /*long*/ sel, int /*long*/ colorArg, int /*long*/ attribute) {
	if (attribute != 0 && NSString.stringWith("NSColor").isEqualToString(new NSString(attribute))) { //$NON-NLS-1$
		if (colorArg != 0) {
			NSColor color = new NSColor(colorArg);
			color = color.colorUsingColorSpaceName(OS.NSCalibratedRGBColorSpace);
			rgb = new RGB((int)(color.redComponent() * 255), (int)(color.greenComponent() * 255), (int)(color.blueComponent() * 255));
		} else {
			rgb = null;
		}
	}
}

/**
 * Sets a FontData object describing the font to be
 * selected by default in the dialog, or null to let
 * the platform choose one.
 * 
 * @param fontData the FontData to use initially, or null
 * @deprecated use #setFontList (FontData [])
 */
public void setFontData (FontData fontData) {
	this.fontData = fontData;
}

/**
 * Sets the set of FontData objects describing the font to
 * be selected by default in the dialog, or null to let
 * the platform choose one.
 * 
 * @param fontData the set of FontData objects to use initially, or null
 *        to let the platform select a default when open() is called
 *
 * @see Font#getFontData
 * 
 * @since 2.1.1
 */
public void setFontList (FontData [] fontData) {
	if (fontData != null && fontData.length > 0) {
		this.fontData = fontData [0];
	} else {
		this.fontData = null;
	}
}

/**
 * Sets the RGB describing the color to be selected by default
 * in the dialog, or null to let the platform choose one.
 *
 * @param rgb the RGB value to use initially, or null to let
 *        the platform select a default when open() is called
 *
 * @see PaletteData#getRGBs
 * 
 * @since 2.1
 */
public void setRGB (RGB rgb) {
	this.rgb = rgb;
}

void windowWillClose(int /*long*/ id, int /*long*/ sel, int /*long*/ sender) {
	NSApplication.sharedApplication().stop(null);
}

}
