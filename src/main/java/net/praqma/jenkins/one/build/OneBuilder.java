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
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.FreeStyleProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import java.io.IOException;
import net.praqma.jenkins.one.actions.OneBuildAction;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * The Builder for our project. We extend from Builder which implements BuildStep,
 * which has a perform method we use.
 * 
 * The main purpose of this example builder is to extract environment information from the executing slaves, and make 
 * this data avaiable for the view we wish to present it in.
 * 
 * During execution we re-use already added actions, and add the discovered data to the already existing build action.
 * 
 * @author Praqma
 */
public class OneBuilder extends Builder {

    public final String message;
    public final boolean remoteOperation;

    @DataBoundConstructor
    public OneBuilder( String message, boolean remoteOperation ) {
        this.message = message;
        this.remoteOperation = remoteOperation;
    }
    
    /**
     * Required method when implementing a builder. When this is invoked, it is up to you, as a plugin developer
     * to add your actions, and/or perform the operations required by your plugin in this build step.
     * 
     * Build steps as you add them to your job configuration are executed sequentially, and the return value for your
     * builder should indicate whether to execute the next build step in the list.
     * 
     * @param build the current build
     * @param launcher
     * @param listener
     * @return a boolean indicating wheather to proceed with the next buildstep
     * @throws InterruptedException
     * @throws IOException 
     */
    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        //Print to the console
        listener.getLogger().println("In Build - OneBuilder");

        String[] str = null;
        
        //Value [remoteOperation] from build step configuration 'Perform on slave'
        if( remoteOperation ) {
            //Tell jenkins to act upon the current workspace (Can be remote, or local)
            str = build.getWorkspace().act( new RemoteOperation() );
        } else {
            //Else, force this to be performed on master, regardless.
            str = new RemoteOperation().invoke( null, null );
        }

        /**
         * This is where we add our build action to the build.
         */
        OneBuildAction action = build.getAction( OneBuildAction.class );
        if( action == null ) {
            action = new OneBuildAction();
            build.addAction( action );
        }
        
        /**
         * We already have an action added, let's add an item to action
         */
        action.addItems( str[0], str[1], message );
        listener.getLogger().println( "Added items" );

        return true;
    }

    
    /**
     * Required static constructor. This is used to create 'One Project Builder' BuildStep in the list-box item on your jobs
     * configuration page. 
     */
    @Extension
    public static class Descriptor extends BuildStepDescriptor<Builder> {
        
        /**
         * This is used to determine if this build step is applicable for your chosen projec type. (FreeStyle, MultiConfiguration, Maven) 
         * Some plugin build steps might be made to be only available to MultiConfiguration projects.
         * 
         * Required to be override. In our example we require the project to be a free-style project.
         * 
         * @param The current project
         * @return a boolean indicating whether this build step can be chose given the project type
         */
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> arg0) {
            return arg0.isInstance(FreeStyleProject.class);
        }
        
        /**
         * Required method.
         * 
         * @return The text to be displayed when selecting your BuildStep, in the project
         */
        @Override
        public String getDisplayName() {
            return "One Project Builder";
        }

        /**
         * Not required. We have overriden it here for demonstational purposes, normally all constructor initialization
         * is performed with the databound constructor, and builder initialization is not needed.
         * 
         * There are instances where you might need to override this method, for example when you want to bind data in a repeatable 
         * section into a list, you can add logic here. I.E using req.bindJSONToList().
         * 
         * Invoked when the form is submitted. 
         * @param req
         * @param formData
         * @return the added builder.
         * @throws hudson.model.Descriptor.FormException 
         */
        
        @Override
        public Builder newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            OneBuilder builder = req.bindJSON(OneBuilder.class, formData);
            save();
            return builder;
        }        
        
    }
}
