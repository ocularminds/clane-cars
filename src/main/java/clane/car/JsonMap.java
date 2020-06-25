package clane.car;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;
import javax.persistence.AttributeConverter;
import clane.car.common.JsonParser;

/**
 *
 * @author Babatope Festus
 */
public class JsonMap implements AttributeConverter<Map<String, Object>, String> {

    @Override
    public String convertToDatabaseColumn(Map<String, Object> object) {
        return JsonParser.toJson(object);
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String json) {
        return JsonParser.parse(new TypeReference<Map<String, Object>>() {
        }, json);
    }

}
