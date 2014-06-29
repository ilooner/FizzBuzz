package com.fizzbuzz.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

public final class MinuteDelay
{
    private static final int LOWER_BOUND = 0;
    private static final int UPPER_BOUND = 60 * 24;
    
    private int minuteDelay;
    
    private boolean initialized;
    private boolean isValid;
    
    public MinuteDelay()
    {
    }
    
    public MinuteDelay(int minuteDelay)
    {
        setMinuteDelay(minuteDelay);
    }
    
    public MinuteDelay(String minuteDelay)
    {
        setMinuteDelay(minuteDelay);
    }
    
    public void setMinuteDelay(int minuteDelay)
    {
        initializedException();
        
        isValid = LOWER_BOUND <= minuteDelay && minuteDelay <= UPPER_BOUND;
        initialized = true;
        
        this.minuteDelay = minuteDelay;
    }
    
    public void setMinuteDelay(String minuteDelayString)
    {
        int minuteDelay;
        
        try
        {
            minuteDelay = Integer.parseInt(minuteDelayString);
        }
        catch(NumberFormatException e)
        {
            isValid = false;
            initialized = true;
            return;
        }
        
        setMinuteDelay(minuteDelay);
    }
        
    public int getMinuteDelay()
    {
        unInitializedException();
        
        return minuteDelay;
    }
    
    public boolean isValid()
    {
        unInitializedException();
        
        return isValid;
    }
    
    private boolean isInitialized()
    {
        return initialized;
    }
    
    private void initializedException()
    {
        if(isInitialized())
        {
            throw new UnsupportedOperationException("Minute delay cannot be initialized twice.");
        }
    }
    
    private void unInitializedException()
    {
        if(!isInitialized())
        {
            throw new UnsupportedOperationException("The minute delay is not initialized.");
        }
    }
    
    public void isNotValidException()
    {
        if(!isValid())
        {
            throw new IllegalArgumentException("The given minuteDelay is not valid.");
        }
    }
}
