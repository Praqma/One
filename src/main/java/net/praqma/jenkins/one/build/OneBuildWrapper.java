/*
 * The MIT License
 *
 * Copyright 2013 Praqma.
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
package net.praqma.jenkins.one.build;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.tasks.BuildWrapper;
import java.io.IOException;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * This class wraps all build steps in the Build phase.
 * 
 * @author Praqma
 */
public class OneBuildWrapper extends BuildWrapper {

    /**
     * Required data-bound constructor.
     * 
     */
    @DataBoundConstructor
    public OneBuildWrapper() { }
    
    public static final class DescriptorImpl extends Descriptor<BuildWrapper> {

        /**
         * Link text for the checkbox used on the job configuarion page.
         * @return The text to be displayed for selection.
         */
        @Override
        public String getDisplayName() {
            return "One Project - OneBuildWrapper";
        }
    }
    
    /**
     * Executed before checkout. Executed exactly one time for each build. So if you have 8 build steps in your build phase,
     * this will only be executed once.
     * 
     * @param build the build in progress
     * @param launcher
     * @param listener
     * @throws IOException
     * @throws InterruptedException 
     */
    @Override
    public void preCheckout(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
        listener.getLogger().println("In Pre-Checkout - BuildWrapper");
        super.preCheckout(build, launcher, listener);
    }

    
    /**
     * Executed after checkout, but before the actual build. Executed exactly once regardless of the number of build steps.
     * The returned {@link Environment} provides a tear down method to be run after the build phase. If the {@link Environment}
     * is null, the build is aborted.
     * 
     * @param build
     * @param launcher
     * @param listener
     * @return an environment for this build
     * @throws IOException
     * @throws InterruptedException 
     */
    @Override
    public Environment setUp(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
        listener.getLogger().println("In Build - BuildWrapper");
        return new OneEnvironment();
    }
   
    public class OneEnvironment extends Environment {

        @Override
        public boolean tearDown(AbstractBuild build, BuildListener listener) throws IOException, InterruptedException {
            listener.getLogger().println("In Build - BuildWrapper - Tearing Down");
            return super.tearDown(build, listener);
        }
        
    }
}
