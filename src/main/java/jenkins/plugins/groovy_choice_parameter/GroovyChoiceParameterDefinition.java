package jenkins.plugins.groovy_choice_parameter;

import hudson.Extension;
import hudson.model.ParameterValue;
import hudson.model.ParameterDefinition;
import hudson.util.CopyOnWriteList;
import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class GroovyChoiceParameterDefinition extends ParameterDefinition {

    public static final String PARAMETER_TYPE_SINGLE_SELECT = "PT_SINGLE_SELECT";
    public static final String PARAMETER_TYPE_MULTI_SELECT = "PT_MULTI_SELECT";

    @Extension
    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

    public static class DescriptorImpl extends ParameterDescriptor {

        public final CopyOnWriteList<GroovyScript> scripts = new CopyOnWriteList<GroovyScript>();

        public DescriptorImpl() {
            super(GroovyChoiceParameterDefinition.class);
            load();
        }
        
        @Override
        public String getDisplayName() {
            return Messages.GroovyChoiceParameterDefinition_DisplayName();
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
            scripts.replaceBy(req.bindParametersToList(GroovyScript.class, "groovyScript."));
            save();
            return true;
        }

        @Override
        public ParameterDefinition newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            return req.bindJSON(GroovyChoiceParameterDefinition.class, formData);
        }

        public GroovyScript[] getScripts() {
            Iterator<GroovyScript> gs = scripts.iterator();
            int count=0;
            while(gs.hasNext()){
                gs.next();
                count++;
            }
            return scripts.toArray(new GroovyScript[count]);
        }

    }

    @Override
    public ParameterDescriptor getDescriptor() {
        return DESCRIPTOR;
    }

    private String type;
    private String defaultValue;
    private String scriptName;

    @DataBoundConstructor
    public GroovyChoiceParameterDefinition(
            String name,
            String description,
            String scriptName,
            String type,
            String defaultValue) {
        super(name, description);
        this.type = type;
        this.defaultValue = defaultValue;
        this.scriptName = scriptName;

    }
    
    @Override
    public ParameterValue createValue(StaplerRequest request) {
        String value[] = request.getParameterValues(getName());
        if (value == null) {
            return getDefaultParameterValue();
        }
        return null;
    }

    @Override
    public ParameterValue createValue(StaplerRequest request, JSONObject jO) {
        Object value = jO.get("value");
        String strValue = "";
        if (value instanceof String) {
            strValue = (String) value;
        } else if (value instanceof JSONArray) {
            JSONArray jsonValues = (JSONArray) value;
            for (int i = 0; i < jsonValues.size(); i++) {
                strValue += jsonValues.getString(i);
                if (i < jsonValues.size() - 1) {
                    strValue += ",";
                }
            }
        }

        GroovyChoiceParameterValue groovyChoiceParameterValue = new GroovyChoiceParameterValue(jO.getString("name"), strValue);
        return groovyChoiceParameterValue;
    }

    @Override
    public ParameterValue getDefaultParameterValue() {
        String defaultValue = getDefaultValue();
        if (!StringUtils.isBlank(defaultValue)) {
            return new GroovyChoiceParameterValue(getName(), defaultValue);
        } else {
            String values[] = computeValues();
            if(values.length >= 1){
                return new GroovyChoiceParameterValue(getName(), values[0]);
            }
        }

        return super.getDefaultParameterValue();
    }

    public GroovyScript getScript() {
        GroovyScript[] scripts = DESCRIPTOR.getScripts();
        if(scriptName == null){
            return null;
        }
        for(GroovyScript gs: scripts){
            if(gs.getName().equals(scriptName))
                return gs;
        }
        return null;
    }

    private String[] computeValues() {
        return getScript().execute();
    }

    private String computeValue() {
        return StringUtils.join(computeValues(),',');
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return computeValue();
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }
}
