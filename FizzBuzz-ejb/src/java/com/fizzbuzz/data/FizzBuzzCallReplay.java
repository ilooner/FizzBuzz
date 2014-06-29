package com.fizzbuzz.data;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class FizzBuzzCallReplay 
{
    public static final String PARAM_PHONE_NUMBER = "a";
    public static final String PARAM_FIZZ_BUZZ_NUMBER = "b";
    
    @XmlElement(name="phoneNumber")
    private String phoneNumber;
    
    @XmlElement(name="fizzBuzzNumber")
    private String fizzBuzzNumber;
    
    public FizzBuzzCallReplay()
    {
    }
    
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }
    
    public String getPhoneNumber()
    {
        return phoneNumber;
    }
    
    public void setFizzBuzzNumber(String fizzBuzzNumber)
    {
        this.fizzBuzzNumber = fizzBuzzNumber;
    }
    
    public String getFizzBuzzNumber()
    {
        return fizzBuzzNumber;
    }
    
    public boolean setVals(HttpServletRequest request)
    {
        String phoneNumberString = request.getParameter(PARAM_PHONE_NUMBER);
        
        if(phoneNumberString == null)
        {
            return false;
        }
        
        PhoneNumber phoneNumber = new PhoneNumber(phoneNumberString);
        this.phoneNumber = phoneNumber.getPhoneNumber();
        
        if(!phoneNumber.isValid())
        {
            return false;
        }
        
        String fizzBuzzNumberString = request.getParameter(PARAM_FIZZ_BUZZ_NUMBER);
        
        if(fizzBuzzNumberString == null)
        {
            return false;
        }
        
        FizzBuzzNumber fizzBuzzNumber = new FizzBuzzNumber(fizzBuzzNumberString);
        
        if(!fizzBuzzNumber.isValid())
        {
            return false;
        }
        
        this.fizzBuzzNumber = new Integer(fizzBuzzNumber.getNumber()).toString();
        
        return true;
    }
    
    public String toParams()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        sb.append(PARAM_PHONE_NUMBER);
        sb.append("=");
        sb.append(phoneNumber);
        
        sb.append("&");
        sb.append(PARAM_FIZZ_BUZZ_NUMBER);
        sb.append("=");
        sb.append(fizzBuzzNumber);
        
        return sb.toString();
    }
}
