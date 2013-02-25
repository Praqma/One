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

import hudson.model.Action;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * A class representing an action performed in a build step. These actions are added, and builds can contain 
 * multiple actions of the same type, these build can contain buisness logic, data etc. 
 * 
 * This data can the be extracted for use in the various views that Jenkins offers.
 * 
 * In our example we will re-use the same action through the entire build pipeline.
 * 
 * @author Praqma
 */
public class OneBuildAction implements Action {

    /**
     * Small data class used to store data we collect from our slaves.
     */
    public static class Items {
        public String os;
        public String string;
        public String message;

        public Items( String os, String string, String message ) {
            this.os = os;
            this.string = string;
            this.message = message;
        }

        @Override
        public String toString() {
            return "OS: " + os + ", String: " + string + ", Message: " + message;
        }
    }

    public List<Items> items = new ArrayList<Items>();

    public OneBuildAction() {
    }

    public void addItems( String os, String string, String message ) {
        items.add( new Items( os, string, message ) );
    }

    public List<Items> getItems() {
        return items;
    }

    /**
     * 
     * @return the path to the icon file to be used by jenkins. If null, no link will be generated
     */
    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "One build action";
    }

    @Override
    public String getUrlName() {
        return "onebuildaction";
    }
    

}
