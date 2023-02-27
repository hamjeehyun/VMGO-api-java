package vmgo.util;

import java.text.MessageFormat;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vmgo.config.ExceptionMessageConfig;
import vmgo.domain.common.OnfException;

@Component
public class ExceptionUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionUtil.class);

    @Autowired
    private ExceptionMessageConfig loader;
    private static ExceptionMessageConfig sLoader;

    @PostConstruct
    public void registerInstance() {
        this.sLoader  = loader;
    }

    public ExceptionUtil() {
    }

    public static OnfException createOnfBizException(String errorCode){
        return new OnfException(errorCode, generateMessage(errorCode));
    }
    
    public static <T> OnfException createOnfBizException(String errorCode, T param) {
    	return createOnfBizException(errorCode, new String[] { String.valueOf(param) });
    }

    public static OnfException createOnfBizException(String errorCode, Object[] params) {
        return new OnfException(errorCode, generateMessage(errorCode, params));
    }

    public static String generateMessage(String errorCode) {
        return generateMessage(errorCode, (Object[])null);
    }

    public static String generateMessage(String errorCode, Object[] params) {
        String errorMessage = getErrorMessage(errorCode);
        if (errorMessage == null) {
            return null;
        } else {
            if (params != null) {
                errorMessage = applyFormat(errorMessage, params);
            }
            return errorMessage;
        }
    }

    private static String applyFormat(String errorMessage, Object[] params) {
        MessageFormat mf = new MessageFormat(errorMessage);
        return mf.format(params);
    }
    
    private static String getErrorMessage(String errorCode) {
        String exMessage = (String)sLoader.loadMessages().get(errorCode);
        return exMessage != null ? exMessage : null;
    }
}
