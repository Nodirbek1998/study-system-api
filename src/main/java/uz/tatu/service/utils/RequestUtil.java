package uz.tatu.service.utils;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestUtil {
    public static boolean checkValue(MultiValueMap<String, String> queryParams, String key) {
        return queryParams.containsKey(key) && !StringUtils.isEmpty(queryParams.getFirst(key));
    }

    public static boolean checkValueNumber(MultiValueMap<String, String> queryParams, String key) {

        if (queryParams.containsKey(key) && !StringUtils.isEmpty(queryParams.getFirst(key))) {
            String val = queryParams.getFirst(key);
            if (val == null) return false;

            try {
                Long value = Long.valueOf(val);
                if (value > 0L) {
                    return true;
                }
                if (value < 0L) {
                    return true;
                }
            } catch (NumberFormatException e) {
                return false;
            }

        }

        return false;
    }

    public static String getLanguage(Locale locale) {
        if (locale == null) {
            return "en";
        }
        if (locale.getLanguage() == null) {
            return "en";
        }
        if (!Arrays.asList("en", "uz", "ru").contains(locale.getLanguage().toUpperCase())) {
            return "en";
        }

        return locale.getLanguage().toUpperCase();
    }

//    public static ResponseDTO postObject(String host, ParameterizedTypeReference<ResponseDTO> responseType, RocketRequestDto obj) {
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseDTO result = new ResponseDTO();
//        String url = host + "/api/v1/users.register";
//
//        try {
//            HttpEntity<RocketRequestDto> request = new HttpEntity<>(obj);
//            ResponseEntity<ResponseDTO> response = restTemplate.exchange(url, HttpMethod.POST, request, responseType);
//            result = response.getBody();
//        } catch (RestClientException e) {
//            Logger.getLogger(RequestUtil.class.getName()).log(Level.SEVERE, null, e);
//            result.setSuccess(false);
//            result.setMessage(e.getMessage());
//        }
//        return result;
//    }

    public static String getTextLanguage(String nameUz, String nameRu, String nameEn) {
        Locale locale = LocaleContextHolder.getLocale();
        String lang = locale.getLanguage();
        String name = nameEn;
        switch (lang) {
            case "uz": {
                name = nameUz;
                break;
            }
            case "ru": {
                name = nameRu;
                break;
            }
        }
        return name;
    }

    public static List<Long> splitStringToList(String list){
        String[] converted = list.split(",");
        List<Long> convertLong = new ArrayList<>();
        for (String number : converted) {
            convertLong.add(Long.parseLong(number.trim()));
        }
        if(convertLong.isEmpty()){
            convertLong.add(0L);
        }
        return convertLong;
    }

    public static List<String> splitStringToStringList(String list){
        String[] converted = list.split(",");
        List<String> convertString = new ArrayList<>();
        for (String number : converted) {
            convertString.add(number.trim());
        }
        if(convertString.isEmpty()){
            convertString.add("");
        }
        return convertString;
    }

    public static String getDir(String[] _sort) {
        if (_sort == null || _sort.length == 1){
            return "asc";
        }
        return _sort[1];
    }

    public static boolean checkUniqueUserId(List<Long> userIds){
        return userIds.stream().distinct().count() == userIds.size();
    }

}
