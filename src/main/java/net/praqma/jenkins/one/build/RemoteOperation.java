package net.praqma.jenkins.one.build;

import hudson.FilePath;
import hudson.remoting.VirtualChannel;

import java.io.File;
import java.io.IOException;

/**
 * @author cwolfgang
 *         Date: 22-02-13
 *         Time: 11:09
 */
public class RemoteOperation implements FilePath.FileCallable<String> {

    @Override
    public String invoke( File f, VirtualChannel channel ) throws IOException, InterruptedException {
        return System.getProperty( "os.name" );
    }
}
