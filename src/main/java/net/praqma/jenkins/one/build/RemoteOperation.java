package net.praqma.jenkins.one.build;

import hudson.FilePath;
import hudson.remoting.VirtualChannel;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import net.praqma.util.execute.CommandLine;

/**
 * Class representing a remote file operation. Whenever Jenkins acts() upon it's workspace 
 * the method invoke() is called on the machine executing the job (Either the remote or the master).
 * 
 * @author cwolfgang
 *         Date: 22-02-13
 *         Time: 11:09
 * 
 */
public class RemoteOperation implements FilePath.FileCallable<String[]> {

    /**
     * This class implements {@link FilePath.FileCallable}. The invoke method is executed either on the master or the remote.
     * 
     * If the File parameter f is null, then this is a local operation (Master). If not, this will get executed on the assigned slave.
     * 
     * The returned value MUST be {@link Serializable} if the task is ever going be executed remotely.
     * 
     * @param f the remote workspace (or null if not remote)
     * @param channel the remote channel (or null if not remote)
     * @return a String[] containing information about the current OS and the installed java version
     * @throws IOException
     * @throws InterruptedException 
     */
    @Override
    public String[]invoke( File f, VirtualChannel channel ) throws IOException, InterruptedException {
        
        //Read command line output into a list of strings.
        List<String> list = CommandLine.getInstance().run( "java -version" ).stdoutList;

        //Get the operating system name
        String os = System.getProperty( "os.name" );

        if( list != null && !list.isEmpty() ) {
            return new String[] { os, list.get( 0 ) };
        } else {
            return new String[] { os, "Unknown" };
        }
    }
}
