package com.fizzbuzz.data;

public final class FizzBuzzNumber
{
    public static final int MAX_DIGITS = 3;
    private static final int LOWER_BOUND = 1;
    private static final int UPPER_BOUND = 999;
    
    private int number;
    private boolean isValid;
    
    public FizzBuzzNumber(String numberString)
    {
        setNumber(numberString);
    }
    
    public FizzBuzzNumber(int number)
    {
        setNumber(number);
    }
    
    private void setNumber(String numberString)
    {
        if(numberString == null)
        {
            return;
        }
        
        int number;
        
        try
        {
            number = Integer.parseInt(numberString);
        }
        catch(NumberFormatException e)
        {
            isValid = false;
            return;
        }
        
        setNumber(number);
    }
    
    private void setNumber(int number)
    {
        isValid = LOWER_BOUND <= number && number <= UPPER_BOUND;
        this.number = number;
    }
    
    public int getNumber()
    {
        return number;
    }
    
    public boolean isValid()
    {
        return isValid;
    }
    
    public void isNotValidException()
    {
        if(!isValid())
        {
            throw new IllegalArgumentException("The given fizzbuzznumber is not valid.");
        }
    }
}
