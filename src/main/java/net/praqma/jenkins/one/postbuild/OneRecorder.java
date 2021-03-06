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
package net.praqma.jenkins.one.postbuild;

import hudson.AbortException;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import net.praqma.jenkins.one.actions.OneBuildAction;
import net.praqma.jenkins.one.actions.OneProjectAction;
import org.kohsuke.stapler.DataBoundConstructor;


/**
 *
 * This is a {@link Recorder}, which is a {@link Publisher}.
 * 
 * Publisher are {@link hudson.tasks.BuildStep}s performed in the post build phase. They are designed for reporting.
 * The build phase usually involves building or compiling, and maybe some rudimentary smoke test of your software.
 * Then in the post build phase you're more likely to perform static analysis of your source code, check for coverage and maybe compiler warnings.
 * 
 * Our example demonstrates in a very simple way how the post build step can be used to set the overall result of a given build.
 * 
 * Our example recorder requires the user to enter a string, that must be contained as a part build steps in order to be considered stable.
 * 
 * @author Praqma
 */
public class OneRecorder extends Recorder {
    
    public final String requiredText;


    /**
     * Required constructor. Our Recorder, which extends Publisher, which implements Describable
     * must have specified a DataBoundConstuctor. The DataBoundConstructor is invoked whenever the job cofiguration 
     * is saved, and is used to bind form input variables to constructor parameters.
     * 
     */
    @DataBoundConstructor
    public OneRecorder(final String requiredText) {
        this.requiredText = requiredText;
    } 
    
    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }
    
    /**
     * Performs the required operations for this build step. The method should generally return true. If some critical error arises such as
     * not being able to open a required file, it is much better to abort the pipeline by throwing an {@link AbortException}.
     *
     * This very simple reference implementation contains code that checks if the action contains items with the text, you specify when configuring
     * the {@link OneRecorder} project. 
     * 
     * @param build
     * @param launcher
     * @param listener
     * @return a boolean value indicating proper execution, if true, the next item in build step is picked up for execution
     * @throws InterruptedException
     * @throws IOException 
     */
    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
                
        listener.getLogger().println("In PostBuild - OneRecorder");
        
        OneBuildAction oba = build.getAction(OneBuildAction.class);
        boolean stable = false;
        for(OneBuildAction.Items item : oba.items) {
            if(item.message.contains(requiredText)) {
                stable = true;
            }
        }
        if(stable) {
            listener.getLogger().println(String.format("The message \"%s\" was present in the result setting build to stable", requiredText));
            build.setResult(Result.SUCCESS);
        } else {
            listener.getLogger().println(String.format("The message \"%s\" was not present in the result setting build to stable", requiredText));
            build.setResult(Result.UNSTABLE);
        }
        
        return true;
    }

    /**
     * This step can be useful if you wish to perform some clean-up taske before the build actually begins.
     * @param build
     * @param listener
     * @return a boolean value indicating success or failure
     */
    @Override
    public boolean prebuild( AbstractBuild<?, ?> build, BuildListener listener ) {
        listener.getLogger().println("In PreBuild - OneRecorder");
        return true;
    }
    
    /**
     * This method returns a list actions you wish to have displayed on the front page
     * @param project
     * @return a list of project actions to be displayed.
     */
    @Override
    public Collection<? extends Action> getProjectActions( AbstractProject<?, ?> project ) {
        return Collections.singletonList( new OneProjectAction( project ) );
    }
    
    /**
     * Required class with a concrete implementation of the descriptor.
     */
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        /**
         * Same as with the OneBuilder
         * @param arg0 The class name of the selected project type.
         * @return a boolean value indicating whether this BuildStep can be used with the selected Project Type.
         */
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> arg0) {
            return true;
        }

        /**
         * Required method. Needs to be overridden.
         * 
         * @return the name to be display in the describable list
         */
        @Override
        public String getDisplayName() {
            return "One Project Recorder";
        }
        
    }
    
}
