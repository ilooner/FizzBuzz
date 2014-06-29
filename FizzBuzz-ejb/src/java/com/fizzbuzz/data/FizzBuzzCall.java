package com.fizzbuzz.data;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Entity
@Table(name="fizz_buzz_call",
       catalog="fizzbuzz")
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries({@NamedQuery(name="getCallsByDate",
                           query="select fizzBuzzCall from FizzBuzzCall fizzBuzzCall order by fizzBuzzCall.dateValue desc")})
public class FizzBuzzCall implements Serializable
{
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd G 'at' HH:mm:ss z");
    
    public static String PARAM_PHONE_NUMBER = "a";
    public static String PARAM_MINUTE_DELAY = "b";
    public static String PARAM_FIZZ_BUZZ_NUMBER = "c";
    public static String PARAM_DATE = "d";
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="fizz_buzz_call_id",
            nullable=false)
    private Long id;
    
    @Column(name="phone_number", 
            length=20,
            nullable=false)
    @XmlElement(name="phoneNumberString")
    private String phoneNumberString;
    
    @Column(name="minute_delay",
            nullable=false)
    @XmlElement(name="minuteDelayInt")
    private Integer minuteDelayInt;
    
    @Column(name="fizz_buzz_number",
            nullable=false)
    @XmlElement(name="fizzBuzzNumberInt")
    private Integer fizzBuzzNumberInt;
    
    @Column(name="date_value",
            nullable=false)
    private Long dateValue;
    
    @XmlElement(name="dateString")
    @Transient
    private String dateString;
    
    public FizzBuzzCall()
    {
    }
    
    public FizzBuzzCall(PhoneNumber phoneNumber,
                        MinuteDelay minuteDelay,
                        FizzBuzzNumber fizzBuzzNumber,
                        Long date)
    {
        setPhoneNumber(phoneNumber);
        setMinuteDelay(minuteDelay);
        setFizzBuzzNumber(fizzBuzzNumber);
        setDateValue(date);
    }
    
    public boolean setVals(HttpServletRequest request)
    {
        String temp = request.getParameter(PARAM_PHONE_NUMBER);
        
        if(temp != null)
        {
            PhoneNumber phoneNumber = new PhoneNumber(temp);
            
            if(!phoneNumber.isValid())
            {
                return false;
            }
            
            setPhoneNumber(phoneNumber);
        }
        
        temp = request.getParameter(PARAM_MINUTE_DELAY);
        
        if(temp != null)
        {
            MinuteDelay minuteDelay = new MinuteDelay(temp);
            
            if(!minuteDelay.isValid())
            {
                return false;
            }
            
            setMinuteDelay(minuteDelay);
        }
        
        temp = request.getParameter(PARAM_FIZZ_BUZZ_NUMBER);
        
        if(temp != null)
        {
            FizzBuzzNumber fizzBuzzNumber = new FizzBuzzNumber(temp);
            
            if(!fizzBuzzNumber.isValid())
            {
                return false;
            }
            
            setFizzBuzzNumber(fizzBuzzNumber);
        }
        
        temp = request.getParameter(PARAM_DATE);
        
        if(temp != null)
        {
            long time;
            
            try
            {
                time = Long.parseLong(temp);
            }
            catch(NumberFormatException e)
            {
                return false;
            }
            
            setDateValue(time);
        }
        
        return true;
    }
    
    public boolean isPhoneNumberSet()
    {
        return phoneNumberString != null;
    }
    
    public void setPhoneNumber(PhoneNumber phoneNumber)
    {
        if(isPhoneNumberSet())
        {
            throw new UnsupportedOperationException("The phone number is already set.");
        }
        
        phoneNumber.isNotValidException();
        
        this.phoneNumberString = phoneNumber.getPhoneNumber();
    }
    
    public void setPhoneNumberString(String phoneNumberString)
    {
        setPhoneNumber(new PhoneNumber(phoneNumberString));
    }
    
    public String getPhoneNumberString()
    {
        return phoneNumberString;
    }
    
    public boolean isMinuteDelaySet()
    {
        return minuteDelayInt != null;
    }
    
    public void setMinuteDelay(MinuteDelay minuteDelay)
    {
        if(isMinuteDelaySet())
        {
            throw new UnsupportedOperationException("The minute delay is already set.");
        }
        
        minuteDelay.isNotValidException();
        
        this.minuteDelayInt = minuteDelay.getMinuteDelay();
    }
    
    public void setMinuteDelayInt(int minuteDelay)
    {
        setMinuteDelay(new MinuteDelay(minuteDelay));
    }
    
    public int getMinuteDelayInt()
    {
        return minuteDelayInt;
    }
    
    public boolean isFizzBuzzNumberSet()
    {
        return fizzBuzzNumberInt != null;
    }
    
    public void setFizzBuzzNumber(FizzBuzzNumber fizzBuzzNumber)
    {
        if(isFizzBuzzNumberSet())
        {
            throw new UnsupportedOperationException("The given fizz buzz number is already set.");
        }
        
        fizzBuzzNumber.isNotValidException();
        
        this.fizzBuzzNumberInt = fizzBuzzNumber.getNumber();
    }
    
    public void setFizzBuzzNumberInt(int fizzBuzzNumber)
    {
        setFizzBuzzNumber(new FizzBuzzNumber(fizzBuzzNumber));
    }
    
    public int getFizzBuzzNumberInt()
    {
        return fizzBuzzNumberInt;
    }
    
    public boolean isDateSet()
    {
        return dateValue != null;
    }
    
    public void setDateValue(Long date)
    {
        if(isDateSet())
        {
            throw new UnsupportedOperationException("The date is already set.");
        }
        
        if(date == null)
        {
            throw new NullPointerException("The given date cannot be null.");
        }
        
        this.dateValue = date;
    }
    
    public Long getDateValue()
    {
        return dateValue;
    }
    
    public void updateDateString()
    {
        if(!isDateSet())
        {
            return;
        }
        
        java.util.Date formatDate = new java.util.Date(this.dateValue);
        this.dateString = DATE_FORMATTER.format(formatDate);
    }
    
    public void setDateString(String dateString)
    {
        this.dateString = dateString;
    }
    
    public String getDateString()
    {
        return dateString;
    }
    
    public boolean isInitialized()
    {
        return phoneNumberString != null &&
               minuteDelayInt != null &&
               fizzBuzzNumberInt != null &&
               dateValue != null;
    }
    
    public boolean oneInitialized()
    {
        return phoneNumberString != null ||
               minuteDelayInt != null ||
               fizzBuzzNumberInt != null ||
               dateValue != null;
    }
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String toParams()
    {
        StringBuilder paramBuilder = new StringBuilder();
        
        if(oneInitialized())
        {
            paramBuilder.append("?");
        }
        
        boolean previous = false;
               
        if(phoneNumberString != null)
        {
            if(previous)
            {
                paramBuilder.append("&");
            }
            else
            {
                previous = true;
            }
            
            paramBuilder.append(FizzBuzzCall.PARAM_PHONE_NUMBER);
            paramBuilder.append("=");
            paramBuilder.append(phoneNumberString);
        }
        
        if(minuteDelayInt != null)
        {
            if(previous)
            {
                paramBuilder.append("&");
            }
            else
            {
                previous = true;
            }
            
            paramBuilder.append(FizzBuzzCall.PARAM_MINUTE_DELAY);
            paramBuilder.append("=");
            paramBuilder.append(minuteDelayInt);
        }
        
        if(fizzBuzzNumberInt != null)
        {
            if(previous)
            {
                paramBuilder.append("&");
            }
            else
            {
                previous = true;
            }
            
            paramBuilder.append(FizzBuzzCall.PARAM_FIZZ_BUZZ_NUMBER);
            paramBuilder.append("=");
            paramBuilder.append(fizzBuzzNumberInt);
        }

        if(dateValue != null)
        {
            if(previous)
            {
                paramBuilder.append("&");
            }
            else
            {
                previous = true;
            }
            
            paramBuilder.append(FizzBuzzCall.PARAM_DATE);
            paramBuilder.append("=");
            paramBuilder.append(dateValue);
        }
                
        return paramBuilder.toString();
    }
}
