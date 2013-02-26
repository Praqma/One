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
package net.praqma.jenkins.one.actions;


import hudson.model.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class implementing actionable and ProminentProjectAction.
 * 
 * Prominent project actions means that the plugin will have a footprint on the jobs main page. 
 * 
 * The prominent part indicates that it will provide a link with the name specified in the {@link OneProjectAction#getDisplayName()} method.
 * 
 * 
 * 
 * @author Praqma
 */
public class OneProjectAction extends Actionable implements ProminentProjectAction {

    public final AbstractProject<?,?> project;
    
    public OneProjectAction(AbstractProject<?,?> project) {
        this.project = project;
    }

    @Override
    public synchronized List<Action> getActions() {
        return super.getActions();
    }

    /**
     * This method is used to create the text for the link to 'Project Action' link on the Job's
     * front page.
     * 
     * @return the text to be displayed on the project action link page 
     */
    @Override
    public String getDisplayName() {
        return "One project action";
    }

    @Override
    public String getSearchUrl() {
        return "oneprojectaction";
    }

    /**
     * If this method returns null, no icon will be used and the link will not be visible
     */ 
    @Override
    public String getIconFileName() {
        return "/plugin/one-plugin/images/64x64/one-icon.png";
    }

    /**
     * 
     * @return the url name used for this action. This is very useful if you want to use
     * your plugin in a restful way. For example jenkins/job/jobname/action
     */
    @Override
    public String getUrlName() {
        return "oneprojectaction";
    }

    /**
     * 
     * @return the last build action associated with this project. 
     */
    public OneBuildAction getLastAction() {
        for( AbstractBuild<?, ?> b = project.getLastCompletedBuild() ; b != null ; b = b.getPreviousBuild() ) {
            OneBuildAction action = b.getAction( OneBuildAction.class );
            if( action != null ) {
                return action;
            }
        }

        return null;
    }

    /**
     * This method can be used to show the last N results 
     * 
     * @param number
     * @return a List containing a list of Action items picked up during build 
     */
    public List<List<OneBuildAction.Items>> getItems( int number ) {
        List<List<OneBuildAction.Items>> list = new ArrayList<List<OneBuildAction.Items>>( number );

        for( AbstractBuild<?, ?> b = project.getLastCompletedBuild() ; b != null ; b = b.getPreviousBuild() ) {
            OneBuildAction action = b.getAction( OneBuildAction.class );
            if( action != null ) {
                list.add( action.getItems() );

                if( list.size() == number ) {
                    return list;
                }
            }
        }

        return list;
    }

}
