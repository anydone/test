package com.treeleaf.payment.domain;

public enum Currency {
    USD,
    MXN,
    CAD,
    BRL,
    COP;

    public static Currency valueOfNullable(Object value) {
        if(!(value instanceof Enum) || value.toString().startsWith("EMPTY")){
            return null;
        }
        return Currency.valueOf( ((Enum<?>)value).name() );
    }
}
