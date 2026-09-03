package dev.chess.converter;

import dev.chess.valueobject.Fen;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class FenConverter implements AttributeConverter<Fen, String> {

    @Override
    public String convertToDatabaseColumn(Fen attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Fen convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Fen(dbData);
    }
}
