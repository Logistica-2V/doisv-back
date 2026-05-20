package com.logistica.doisv.core.util.generation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DataUtil {

    public static LocalDate hoje(){
        return LocalDate.now(ZoneId.of("America/Sao_Paulo"));
    }

    public static LocalDateTime dataHoraAgora(){
        return LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }

}
