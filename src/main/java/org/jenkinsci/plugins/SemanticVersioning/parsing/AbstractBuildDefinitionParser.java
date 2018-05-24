/*
 * The MIT License
 *
 * Copyright (c) 2014, Steve Wagner
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.jenkinsci.plugins.SemanticVersioning.parsing;

import hudson.ExtensionList;
import jenkins.model.Jenkins;
import org.apache.tools.ant.ExtensionPoint;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * BuildDefinitionParser abstraction layer for better backward compatibility
 * @author timii
 *
 */

public abstract class AbstractBuildDefinitionParser extends ExtensionPoint implements BuildDefinitionParser, Serializable {
	static final Logger logger = Logger.getLogger(String.valueOf(AbstractBuildDefinitionParser.class));
	public static ExtensionList<BuildDefinitionParser> getParsers() {
			return Jenkins.getActiveInstance().getExtensionList(BuildDefinitionParser.class);
	}
}
