package vmgo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Configuration
@EnableConfigurationProperties(ExceptionMessageProperty.class)
@Component
@Scope("singleton")
public class ExceptionMessageConfig {

    @Autowired
    private ExceptionMessageProperty exMessageProperty;

    public ExceptionMessageConfig() {
    }

    /**
     * 메시지 저장소에서 메시지를 로드한다.
     */
    public Map<String, String> loadMessages() {
        return exMessageProperty.getBizMessages();
    }

}
