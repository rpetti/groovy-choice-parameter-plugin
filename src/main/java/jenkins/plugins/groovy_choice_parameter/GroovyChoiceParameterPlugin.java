package jenkins.plugins.groovy_choice_parameter;

import hudson.Plugin;
import hudson.model.Descriptor.FormException;
import hudson.util.CopyOnWriteList;
import java.io.IOException;
import javax.servlet.ServletException;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

public class GroovyChoiceParameterPlugin extends Plugin {

    @Override
    public void configure(StaplerRequest req, JSONObject formData) throws IOException, ServletException, FormException {
        GroovyChoiceParameterDefinition.DESCRIPTOR.configure(req, formData);
    }

    public CopyOnWriteList<GroovyScript> getScripts(){
        return GroovyChoiceParameterDefinition.DESCRIPTOR.scripts;
    }

}
