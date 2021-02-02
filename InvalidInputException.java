package p1;

/**
 ***********************************************************************
 * $Id: InvalidInputException.java 329 2015-01-06 07:43:12Z mkwayisi $
 * ---------------------------------------------------------------------
 * Authored by Michael Kwayisi. Copyright (c) 2014. See license below.
 * Comments are appreciated - mic at kwayisi dot org | www.kwayisi.org
 * ---------------------------------------------------------------------
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are stringently met:
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions, and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions, and the following
 *     disclaimer in the documentation and/or other materials provided
 *     with the distribution.
 *  3. The end-user documentation included with the redistribution,
 *     if any, must include the following acknowledgment:
 *       "This product includes software written by Michael Kwayisi."
 *     Alternately, this acknowledgment may appear in the software
 *     itself, if and wherever such 3rd-party acknowledgments appear.
 *  4. Neither the name of the software nor the name of its author
 *     and/or contributors may be used to endorse or promote products
 *     derived from this software without specific prior permission.
 ***********************************************************************
 */

public class InvalidInputException extends Exception {
//======================================================================
// [FUNC] Primary class constructor with no parameters.
public InvalidInputException() {
	super();
}

//======================================================================
// [FUNC] Constructor accepting exception message as parameter.
public InvalidInputException(String msg) {
	super(msg);
}
}
