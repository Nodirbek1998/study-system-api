package uz.tatu.service.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CardLocaleTimeDeserializer extends StdDeserializer<LocalTime> {
    public CardLocaleTimeDeserializer() {
        this(null);
    }

    public CardLocaleTimeDeserializer(Class<?> vc) {
        super(vc);
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public LocalTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String dateStr = jsonParser.getValueAsString();
        if (dateStr == null || dateStr.isEmpty()) return null;

        try {
            return LocalTime.parse(dateStr, formatter);
        } catch (Exception e) {
            return null;
        }

    }
}
