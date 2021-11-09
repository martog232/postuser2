package com.example.postuser.security;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailValidator implements Predicate<String> {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
        Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", Pattern.CASE_INSENSITIVE);
    @Override
    public boolean test(String s) {
        Matcher matcher=VALID_EMAIL_ADDRESS_REGEX.matcher(s);
        return matcher.matches();
    }
}
