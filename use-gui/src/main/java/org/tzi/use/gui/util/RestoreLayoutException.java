/*
 * USE - UML based specification environment
 * Copyright (C) 1999-2010 Mark Richters, University of Bremen
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.tzi.use.gui.util;

/**
 * Exception indicating that the stored layout information
 * is no longer valid, i.e., could not be loaded.
 * @author Lars Hamann
 *
 */
public class RestoreLayoutException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public RestoreLayoutException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RestoreLayoutException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public RestoreLayoutException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public RestoreLayoutException(Throwable cause) {
		super(cause);
	}
}
