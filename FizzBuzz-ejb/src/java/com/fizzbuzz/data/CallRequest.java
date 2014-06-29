package com.fizzbuzz.data;

import javax.annotation.Resource;
import javax.ejb.TimerService;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="callRequest")
@XmlAccessorType(XmlAccessType.NONE)
public class CallRequest
{
    @XmlElement(name="PhoneNumber")
    private String phoneNumber;
    @XmlElement(name="MinuteDelay")
    private String minuteDelay;
    
    public CallRequest()
    {
    }
    
    public void setPhoneNumber(String phoneNumber)
    {
        if(phoneNumber == null)
        {
            throw new NullPointerException("The given phoneNumber cannot be null.");
        }
        
        this.phoneNumber = phoneNumber;
    }
    
    public String getPhoneNumber()
    {
        return this.phoneNumber;
    }
    
    public void setMinuteDelay(String minuteDelay)
    {
        if(minuteDelay == null)
        {
            throw new NullPointerException("The given minuteDelay cannot be null.");
        }
        
        this.minuteDelay = minuteDelay;
    }
    
    public String getMinuteDelay()
    {
        return this.minuteDelay;
    }
}
