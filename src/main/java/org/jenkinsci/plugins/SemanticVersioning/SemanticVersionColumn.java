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

package org.jenkinsci.plugins.SemanticVersioning;

import hudson.Extension;
import hudson.ExtensionList;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.util.ListBoxModel;
import hudson.views.ListViewColumn;
import hudson.views.ListViewColumnDescriptor;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.SemanticVersioning.columnDisplay.AbstractColumnDisplayStrategy;
import org.jenkinsci.plugins.SemanticVersioning.columnDisplay.ColumnDisplayStrategy;
import org.jenkinsci.plugins.SemanticVersioning.columnDisplay.LastSuccessfulBuildStrategy;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;
import java.util.logging.Logger;

public class SemanticVersionColumn extends ListViewColumn {

    private static Logger logger = Logger.getLogger(String.valueOf(AppVersion.class));
    private ColumnDisplayStrategy displayStrategy;

    @DataBoundConstructor
    public SemanticVersionColumn(String displayStrategyName) {
        try {
            this.displayStrategy = (ColumnDisplayStrategy) Jenkins.getActiveInstance().getExtensionList(displayStrategyName).iterator().next();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static final Descriptor<ListViewColumn> descriptor = new SemanticVersionColumnDescriptor();

    public String getDisplayStrategy() {
        return this.displayStrategy.getClass().getCanonicalName();
    }

    public String getSemanticVersion(Job job) throws IOException, InterruptedException {
        ColumnDisplayStrategy strategy;
        if(job==null || job.getLastBuild()==null || job.getLastBuild().getResult()==null) {
        	return "-";
        }
        if(job.getLastBuild().getResult().ordinal == 0) {
            strategy = new LastSuccessfulBuildStrategy();
        } else {
            strategy = this.displayStrategy;
        }
        return strategy.getDisplayString(job);
    }

    @Extension(ordinal = 1.5)
    public static class SemanticVersionColumnDescriptor extends ListViewColumnDescriptor {
        @Override
        public boolean shownByDefault() {
            return false;
        }


        @Override
        public ListViewColumn newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            String strategy = formData == null ? null : formData.optString("displayStrategy");
            return new SemanticVersionColumn(StringUtils.defaultIfBlank(strategy,
                    LastSuccessfulBuildStrategy.class.getCanonicalName()));
        }

        public ListBoxModel doFillDisplayStrategyItems() {
            ListBoxModel columnDisplayStrategies = new ListBoxModel();
            ExtensionList<ColumnDisplayStrategy> extensionList = AbstractColumnDisplayStrategy.getStrategies();
            logger.info("SemanticVersionColumnDescriptor::doFillDisplayStrategyItems There are " + extensionList.size() + " strategies available.");
            for (ColumnDisplayStrategy strategy : extensionList) {
                columnDisplayStrategies.add(
                        strategy.getDescriptor().getDisplayName(),
                        strategy.getClass().getCanonicalName());
            }

            return columnDisplayStrategies;
        }

        @Override
        public String getDisplayName() {
            return Messages.SEMANTIC_VERSION_COLUMN_DISPLAY_NAME;
        }
    }
}
