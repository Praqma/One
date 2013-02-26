package net.praqma.jenkins.one.listeners;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.*;
import hudson.model.listeners.RunListener;
import java.io.IOException;

/**
 * 
 * A run listener that looks for builds started across an entire Jenkins instance. Multiple instantiations
 * of {@link RunListener} are allowed. Each instance is run sequentially.
 * 
 * This is extremely useful for writing a plugin that monitors usage of a given plugin for example. 
 * 
 * @author cwolfgang
 */
@Extension
public class OneRunListener extends RunListener<Run> {

    /**
     * @param run
     * @param listener 
     */
    @Override
    public void onStarted( Run run, TaskListener listener ) {
        listener.getLogger().println( "One Run Listener - OnStarted" );        
    }

    /**
     * 
     * @param run
     * @param listener 
     */
    @Override
    public void onCompleted( Run run, TaskListener listener ) {
        listener.getLogger().println( "One Run Listener - OnCompleted" );
    }
    
    /**
     * 
     * @param build
     * @param launcher
     * @param listener
     * @return
     * @throws IOException
     * @throws InterruptedException 
     */
    @Override
    public Environment setUpEnvironment( AbstractBuild build, Launcher launcher, BuildListener listener ) throws IOException, InterruptedException {
        listener.getLogger().println( "One Run Listener - OnSetupEnv" );

        return super.setUpEnvironment(build, launcher, listener);
    }
}
