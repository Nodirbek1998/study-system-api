package uz.tatu.service.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CardLocaleDateDeserializer extends StdDeserializer<LocalDate> {
    public CardLocaleDateDeserializer() {
        this(null);
    }

    public CardLocaleDateDeserializer(Class<?> vc) {
        super(vc);
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String dateStr = jsonParser.getValueAsString();
        if (dateStr == null || dateStr.isEmpty()) return null;

        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (Exception e) {
            return null;
        }

    }
}
