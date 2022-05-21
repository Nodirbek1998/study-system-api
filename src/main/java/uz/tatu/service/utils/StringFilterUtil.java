package uz.tatu.service.utils;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class StringFilterUtil {

    public static String strLikeTwoSide(String filterField){
        if (StringUtils.isEmpty(filterField)){
            return "";
        }

        return "%" + filterField + "%";
    }

    public static String strLikeTwoSideLow(String filterField){
        if (StringUtils.isEmpty(filterField)){
            return "";
        }

        return "%" + filterField.toLowerCase() + "%";
    }

    public static String strLikeBegin(String filterField){
        if (StringUtils.isEmpty(filterField)){
            return "";
        }

        return "%" + filterField;
    }

    public static String strLikeEnd(String filterField){
        if (StringUtils.isEmpty(filterField)){
            return "";
        }

        return filterField + "%";
    }

    public static String md5(String tetx) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(tetx.getBytes());
        byte byteData[] = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString().toUpperCase();
    }

    public static String formatDouble(String dvalue){
        BigDecimal value = BigDecimal.valueOf(Double.valueOf(dvalue));
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        symbols.setDecimalSeparator('.');
        DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
        return formatter.format(value);
    }

}
