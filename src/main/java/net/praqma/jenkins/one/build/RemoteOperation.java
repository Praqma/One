package net.praqma.jenkins.one.build;

import hudson.FilePath;
import hudson.remoting.VirtualChannel;
import net.praqma.util.execute.CommandLine;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author cwolfgang
 *         Date: 22-02-13
 *         Time: 11:09
 */
public class RemoteOperation implements FilePath.FileCallable<String[]> {

    @Override
    public String[]invoke( File f, VirtualChannel channel ) throws IOException, InterruptedException {
        List<String> list = CommandLine.getInstance().run( "java -version" ).stdoutList;

        String os = System.getProperty( "os.name" );

        if( list != null && !list.isEmpty() ) {
            return new String[] { os, list.get( 0 ) };
        } else {
            return new String[] { os, "Unknown" };
        }
    }
}
