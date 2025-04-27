package com.codifica.elevebot.adapter;

import com.codifica.elevebot.model.CategoriaProduto;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class CategoriaProdutoAdapter implements AttributeConverter<CategoriaProduto, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CategoriaProduto attribute) {
        return attribute == null ? null : attribute.getCode();
    }

    @Override
    public CategoriaProduto convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : CategoriaProduto.fromCode(dbData);
    }
}
