package jenkins.plugins.groovy_choice_parameter;

import hudson.model.AbstractProject;
import groovy.lang.Binding;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import groovy.lang.GroovyShell;
import hudson.model.Hudson;

public class GroovyScript {
    private String name;
    private String script;

    public GroovyScript(){}
    
    public GroovyScript(String name, String script){
        this.name = name;
        this.script = script;
    }

    public String[] execute() {
        Binding binding = new Binding();
        binding.setVariable("hudson", Hudson.getInstance());

        ArrayList<String> values = new ArrayList<String>();
        binding.setVariable("choices", values);
        
        GroovyShell shell = new GroovyShell(binding);
        shell.evaluate(script);
        values = (ArrayList)binding.getVariable("choices");
        return values.toArray(new String[values.size()]);
    }

    private static void setAllVariables(Map<String,String> variables, Binding binding){
        for(Entry<String,String> e : variables.entrySet()){
            binding.setVariable(e.getKey(), e.getValue());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

}
