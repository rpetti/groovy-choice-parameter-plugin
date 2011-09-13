package jenkins.plugins.groovy_choice_parameter;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.model.StringParameterValue;

public class GroovyChoiceParameterValue extends StringParameterValue {

    @DataBoundConstructor
    public GroovyChoiceParameterValue(String name, String value) {
        super(name, value);
    }
}
