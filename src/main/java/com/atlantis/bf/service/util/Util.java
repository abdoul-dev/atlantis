/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.bf.service.util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * @author LANKOANDE
 */
public class Util {

    private static final Logger log = LoggerFactory.getLogger(Util.class);

    public static boolean isBiggerOrEqualThan(BigDecimal nombre1, BigDecimal nombre2) {
        if (Objects.equals(nombre1, nombre2)) return true;
        if (nombre1 == null) return false;
        if (nombre2 == null) return true;
        return nombre1.compareTo(nombre2) >= 0;
    }

    public static boolean isBiggerThan(BigDecimal nombre1, BigDecimal nombre2) {
        if (Objects.equals(nombre1, nombre2)) return false;
        if (nombre1 == null) return false;
        if (nombre2 == null) return true;
        return nombre1.compareTo(nombre2) > 0;
    }

    public static LocalDate getMax(LocalDate... dates) {
        if (dates == null || dates.length <= 0) {
            return null;
        }
        LocalDate max = dates[0];
        for (int i = 1; i < dates.length; i++) {
            LocalDate date = dates[i];
            if (date.isAfter(max)) {
                max = date;
            }
        }
        return max;
    }

    public static LocalDate min(LocalDate... dates) {
        if (dates == null || dates.length <= 0) {
            return null;
        }
        LocalDate max = dates[0];
        for (int i = 1; i < dates.length; i++) {
            LocalDate date = dates[i];
            if (date != null && max != null && date.isBefore(max)) {
                max = date;
            }
        }
        return max;
    }

    /**
     * Calcul et retourne une copie du nombre minimal entre deux BigDecimal
     *
     * @param decimal1
     * @param decimal2
     * @return Le plus petit entre decimal1 et decimal2
     */
    public static BigDecimal min(BigDecimal decimal1, BigDecimal decimal2) {
        BigDecimal d1 = decimal1, d2 = decimal2;
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return d1.compareTo(d2) <= 0 ? d1 : d2;
    }

    /**
     * Calcul et retourne une copie du nombre maximal entre deux BigDecimal
     *
     * @param decimal1
     * @param decimal2
     * @return Le plus grand entre decimal1 et decimal2
     */
    public static BigDecimal max(BigDecimal decimal1, BigDecimal decimal2) {
        if (decimal1 == null && decimal2 == null) return null;
        if (decimal1 == null) return decimal2;
        if (decimal2 == null) return decimal1;
        return decimal1.compareTo(decimal2) >= 0 ? decimal1 : decimal2;
    }

    public static boolean isNullOrFalse(Boolean bool) {
        return bool == null || !bool;
    }

    public static boolean isNullOrTrue(Boolean b) {
        return b == null || b;
    }

    public static boolean isTrue(Boolean bool) {
        return (bool != null) && bool;
    }

    public static boolean estPositif(BigDecimal bigDecimal) {
        return bigDecimal != null && bigDecimal.compareTo(BigDecimal.ZERO) >= 0;
    }

    public static boolean estNullOuPositif(BigDecimal bigDecimal) {
        return bigDecimal == null || bigDecimal.compareTo(BigDecimal.ZERO) >= 0;
    }

    public static Boolean estPositifStrict(BigDecimal bigDecimal) {
        return bigDecimal != null && bigDecimal.compareTo(BigDecimal.ZERO) > 0;
    }

    public static Boolean estPositifStrict(Integer number) {
        return number != null && number > 0;
    }

    public static Boolean estNegatifStrict(BigDecimal bigDecimal) {
        return bigDecimal != null && bigDecimal.compareTo(BigDecimal.ZERO) < 0;
    }

    public static boolean estSuperieurOuEgal(BigDecimal nombre1, BigDecimal nombre2) {
        if (Objects.equals(nombre1, nombre2)) return true;
        if (nombre1 == null) return false;
        if (nombre2 == null) return true;
        return nombre1.compareTo(nombre2) >= 0;
    }

    public static boolean estSuperieurStrict(BigDecimal nombre1, BigDecimal nombre2) {
        if (Objects.equals(nombre1, nombre2)) return false;
        if (nombre1 == null) return false;
        if (nombre2 == null) return true;
        return nombre1.compareTo(nombre2) > 0;
    }


    /**
     * Retourne la valeur du bigdécimal ou ZERO si celui est null
     *
     * @param montantInitial
     * @return
     */
    public static BigDecimal getZeroIfNull(BigDecimal montantInitial) {
        return montantInitial == null ? BigDecimal.ZERO : montantInitial;
    }

    /**
     * Retourne vrai si une variable est null ou équivaut à zero
     *
     * @param montant :
     * @return
     */
    public static boolean isZeroOrNull(BigDecimal montant) {
        return montant == null || BigDecimal.ZERO.compareTo(montant) == 0;
    }

    /**
     * Retourne la valeur du bigdécimal ou ZERO si celui est null
     *
     * @param nombre
     * @return
     */
    public static Integer getZeroIfNull(Integer nombre) {
        return nombre == null ? 0 : nombre;
    }

    /**
     * Retourne la valeur de l'entier ou 1 si celui est null
     *
     * @param nombre
     * @return
     */
    public static Integer getOneIfNull(Integer nombre) {
        return nombre == null ? 1 : nombre;
    }


    public static String format(ZonedDateTime date, String format) {
        if (Objects.nonNull(date)) {
            return DateTimeFormatter.ofPattern(format).format(date);
        } else {
            return "";
        }
    }

    public static String format(Instant date, String format) {
        if (Objects.nonNull(date)) {
            return DateTimeFormatter.ofPattern(format).format(date.atZone(ZoneId.systemDefault()));
        } else {
            return "";
        }
    }

    public static String formatToddMMYYYY(LocalDate date) {
        if (Objects.nonNull(date))
            return DateTimeFormatter.ofPattern("dd-MM-yyyy").format(date);
        return "";
    }

    public static String formatForFile(ZonedDateTime date) {
        Locale l = Locale.CANADA_FRENCH;  // Or Locale.US, Locale.KOREA, etc.
        if (Objects.nonNull(date))
            return DateTimeFormatter.ofPattern("dd-MM-yyyy-HH.mm.ss").withZone(ZoneOffset.UTC).withLocale(l).format(date);
        return "";
    }

    public static String formatToText(LocalDate date) {
        Locale l = Locale.CANADA_FRENCH;  // Or Locale.US, Locale.KOREA, etc.
        DateTimeFormatter f = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
            .withLocale(l);
        String output = date.format(f);
        return output;
    }

    public static Float getZeroIfNull(Float pourcentage) {
        return pourcentage == null ? 0f : pourcentage;
    }

    public static LocalDate getNowIfNull(LocalDate date) {
        return date == null ? ZonedDateTime.now().toLocalDate() : date;
    }

    public static ZonedDateTime getNowIfNull(ZonedDateTime date) {
        return date == null ? ZonedDateTime.now() : date;
    }

    public static boolean estNullOuZero(BigDecimal montant) {
        return montant == null || BigDecimal.ZERO.compareTo(montant) == 0;
    }

    public static boolean estNullOuZero(Integer montant) {
        return montant == null || montant == 0;
    }

    public static boolean estNullOuNegatif(Integer montant) {
        return montant == null || montant <= 0;
    }

    public static boolean estNullOuNegatif(BigDecimal montant) {
        return montant == null || BigDecimal.ZERO.compareTo(montant) >= 0;
    }


    private static String formatPrice(double amount) {
        DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("");
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator(' ');
        df.setDecimalFormatSymbols(dfs);
        return df.format(amount);
    }

    public static String formatPrice(BigDecimal amount) {
        return Util.formatPrice(Util.getZeroIfNull(amount).doubleValue());
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    public static String trim(String str) {
        return str != null ? str.trim() : "";
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    public static BigDecimal toBigDecimal(String montant) {
        if (isNullOrEmpty(montant) || "-".equals(montant)) {
            montant = "0";
        }
        montant = montant.replace(',', '.').replaceAll("[^0-9.\\-]", "");
        return new BigDecimal(Double.valueOf(montant));
    }


    /**
     * Renvoie la date en fin de journée
     *
     * @param date
     * @return
     */
    public static ZonedDateTime finJournee(ZonedDateTime date) {
        if (date != null) {
            return date.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        }
        return null;
    }

    /**
     * @param date
     * @return
     * @see #finJournee(ZonedDateTime)
     */
    public static ZonedDateTime finJournee(LocalDate date) {
        if (date != null) {
            return finJournee(date.atStartOfDay(ZoneId.systemDefault()));
        }
        return null;
    }

    /**
     * Renvoie la date en début de journée.
     *
     * @param date
     * @return
     */
    public static ZonedDateTime debutJournee(ZonedDateTime date) {
        if (date != null) {
            return date.withHour(0).withMinute(0).withSecond(0).withNano(0);
        }
        return null;
    }

    /**
     * @param date
     * @return
     * @see #debutJournee(ZonedDateTime)
     */
    public static ZonedDateTime debutJournee(LocalDate date) {
        if (date != null) {
            return debutJournee(date.atStartOfDay(ZoneId.systemDefault()));
        }
        return null;
    }

    public static LocalDate replaceByIfNull(LocalDate d, LocalDate d2) {
        return d == null ? d2 : d;
    }

    public static ZonedDateTime replaceByIfNull(ZonedDateTime d, ZonedDateTime d2) {
        return d == null ? d2 : d;
    }

    public static String toHexadecimal(String text) {
        try {
            byte[] myBytes = text.getBytes(StandardCharsets.UTF_8);
            return DatatypeConverter.printHexBinary(myBytes);
        } catch (Exception ex) {
            return "Can't convert text to hexadecimal";
        }
    }

    /**
     * Le téléphone peut-être mal formaté,
     * Il faut donc le reformatter au bon format.
     * Ajouter le préfixe 226 s'il n'est pas déjà présent,
     * supprimer les caractères incorrects( , ; ou /)
     *
     * @param tel
     * @return
     */
    public static String formatPhoneNumber(String tel) {
        tel = StringUtils.deleteWhitespace(tel);
        tel = tel.replace(" ", "").replace("-", "");
        final int indexSlash = tel.indexOf('/');
        final int indexDot = tel.indexOf(';');
        final int indexCom = tel.indexOf(',');
        int index = Util.minPos(indexCom, indexDot, indexSlash);
        tel = index != -1 ? tel.substring(0, index) : tel;

        if (tel.startsWith("+226")) {
            tel = tel.replaceFirst("\\+226", "226");
        } else if (tel.startsWith("00226")) {
            tel = tel.replaceFirst("00226", "226");
        } else if (tel.startsWith("0226")) {
            tel = tel.replaceFirst("0226", "226");
        } else if (tel.length() == 8) {
            tel = "226" + tel;
        }
        return tel;
    }

    /**
     * Remplace tous les caractères accentués par des caractères normaux d'un message
     * donnée et le retourner
     *
     * @param msg Le message contenant éventuellement des caractères accentués
     * @return Le message sans caractères accentués
     */
    public static String toAscii(String msg) {
        try {
            return Normalizer
                .normalize(msg, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        } catch (Exception ex) {
            log.error("Impossible de remplacer les caractere accentue dans la chaine : " + msg);
            return msg;
        }
    }


    /**
     * La fonction retourne le plus petit nombre positif qui est dans ce tableau.
     * S'il ne trouve pas, il retourne -1
     *
     * @param nombres
     * @return
     */
    public static int minPos(int... nombres) {
        int min = nombres[0];
        for (int i = 1; i < nombres.length; i++) {
            if (nombres[i] >= 0 && min < 0) {
                min = nombres[i];
            }
            if (nombres[i] >= 0 && nombres[i] <= min) {
                min = nombres[0];
            }
        }
        return min;
    }

    public static List<String> buildTelephone(String cts) {
        String[] numeroNonFormattes = cts.split(";");
        List<String> numeroFormattes = new ArrayList<>();
        for (int i = 0; i < numeroNonFormattes.length; i++) {
            numeroFormattes.add(formatPhoneNumber(numeroNonFormattes[i]));
        }
        return numeroFormattes;
    }
}
