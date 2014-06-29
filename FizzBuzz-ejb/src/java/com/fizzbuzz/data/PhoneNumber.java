package com.fizzbuzz.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PhoneNumber
{
    public static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("(\\+1)?\\d{3}-?\\d{3}-?\\d{4}");
    
    private String phoneNumber;
    private boolean isValid;
    
    public PhoneNumber()
    {
    }
    
    public PhoneNumber(String phoneNumber)
    {
        setPhoneNumber(phoneNumber);
    }
    
    public void setPhoneNumber(String phoneNumber)
    {
        if(phoneNumber == null)
        {
            return;
        }
        
        initializedException();
        
        phoneNumber = phoneNumber.replaceAll("\\s*", "");
        phoneNumber = phoneNumber.replaceAll("-+", "-");
        
        this.phoneNumber = phoneNumber;
        
        Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
        
        isValid = matcher.matches();
    }
    
    public String getPhoneNumber()
    {
        unInitializedException();
        
        return phoneNumber;
    }
    
    public boolean isValid()
    {
        unInitializedException();
        
        return isValid;
    }
    
    public void isNotValidException()
    {
        if(!isValid())
        {
            throw new IllegalArgumentException("The given phone number is not valid.");
        }
    }
    
    private boolean isInitialized()
    {
        return this.phoneNumber != null;
    }
    
    private void initializedException()
    {
        if(isInitialized())
        {
            throw new UnsupportedOperationException("Phone number cannot be initialized twice.");
        }
    }
    
    private void unInitializedException()
    {
        if(!isInitialized())
        {
            throw new UnsupportedOperationException("The phone number is not initialized.");
        }
    }
}
