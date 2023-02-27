package vmgo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "onf")
public class ExceptionMessageProperty {
    private Map<String,String> bizMessages = new HashMap<>();

    public ExceptionMessageProperty() {}

    public void setBizMessages(Map<String,String> bizMessages) {
        this.bizMessages = bizMessages;
    }

    public Map<String,String> getBizMessages() {
        return this.bizMessages;
    }

}
