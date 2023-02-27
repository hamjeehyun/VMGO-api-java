package vmgo.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.List;

public final class StringListConverter implements AttributeConverter {
    private final ObjectMapper mapper = new ObjectMapper();

    public Object convertToDatabaseColumn(Object var1) {
        try {
            return mapper.writeValueAsString(var1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return var1;
    }

    @Override
    public List<String> convertToEntityAttribute(Object o) {
        try {
            return mapper.readValue((JsonParser) o, List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
