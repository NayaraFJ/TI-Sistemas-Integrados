package br.pucminas.sige.sla.application;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Set;

/** Feriados federais fixos; feriados locais, religiosos e recessos são cadastrados pela agência. */
public final class BrazilianHolidays {
  private BrazilianHolidays() {}
  private static final Set<MonthDay> FIXED=Set.of(MonthDay.of(1,1),MonthDay.of(4,21),MonthDay.of(5,1),MonthDay.of(9,7),MonthDay.of(10,12),MonthDay.of(11,2),MonthDay.of(11,15),MonthDay.of(12,25));
  public static boolean isHoliday(LocalDate date){return FIXED.contains(MonthDay.from(date))||(date.getYear()>=2024&&date.getMonthValue()==11&&date.getDayOfMonth()==20);}
}
