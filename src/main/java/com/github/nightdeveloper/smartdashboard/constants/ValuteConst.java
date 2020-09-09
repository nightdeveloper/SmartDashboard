package com.github.nightdeveloper.smartdashboard.constants;

public enum ValuteConst {

    USD("USD"), EUR("EUR"), UNDEFINED("UNDEFINED");

    private final String code;

    ValuteConst(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ValuteConst from(String str) {
        if (USD.getCode().equals(str)) {
            return USD;
        } else if (EUR.getCode().equals(str)) {
            return EUR;
        }

        return UNDEFINED;
    }

    @Override
    public String toString() {
        return "Valute{" +
                "code='" + code + '\'' +
                '}';
    }
}
