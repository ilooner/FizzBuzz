package com.fizzbuzz.data;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="fizzBuzzCallsRoot")
@XmlAccessorType(XmlAccessType.NONE)
public class FizzBuzzCalls
{
    @XmlElement(name="fizzBuzzCalls")
    private List<FizzBuzzCall> fizzBuzzCalls;
    
    public FizzBuzzCalls()
    {
    }
    
    public void setFizzBuzzCalls(List<FizzBuzzCall> fizzBuzzCalls)
    {
        if(fizzBuzzCalls == null)
        {
            throw new NullPointerException("The given fizzBuzzCalls cannot be null.");
        }
        
        this.fizzBuzzCalls = fizzBuzzCalls;
    }
    
    public List<FizzBuzzCall> getFizzBuzzCalls()
    {
        return fizzBuzzCalls;
    }
}
