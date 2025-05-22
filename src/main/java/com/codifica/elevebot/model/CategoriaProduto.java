package com.codifica.elevebot.model;

import com.codifica.elevebot.exception.IllegalArgumentException;

public enum CategoriaProduto {
    GASTO_FIXO(1),
    MANUTENCAO(2),
    INSUMO(3),
    PRODUTO(4);

    private final int code;

    CategoriaProduto(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static CategoriaProduto fromCode(int code) {
        for (CategoriaProduto valor : values()) {
            if (valor.getCode() == code) {
                return valor;
            }
        }
        throw new IllegalArgumentException("Código inválido para CategoriaProduto: " + code);
    }
}