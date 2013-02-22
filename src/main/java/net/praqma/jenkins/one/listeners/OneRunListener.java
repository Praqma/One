package net.praqma.jenkins.one.listeners;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.*;
import hudson.model.listeners.RunListener;

import java.io.IOException;

/**
 * @author cwolfgang
 *         Date: 22-02-13
 *         Time: 10:15
 */
@Extension
public class OneRunListener extends RunListener<Run> {

    @Override
    public void onStarted( Run run, TaskListener listener ) {
        listener.getLogger().println( "One Run Listener - OnStarted" );
    }

    @Override
    public void onCompleted( Run run, TaskListener listener ) {
        listener.getLogger().println( "One Run Listener - OnCompleted" );
    }

    @Override
    public Environment setUpEnvironment( AbstractBuild build, Launcher launcher, BuildListener listener ) throws IOException, InterruptedException {
        listener.getLogger().println( "One Run Listener - OnSetupEnv" );

        return super.setUpEnvironment(build, launcher, listener);
    }
}
