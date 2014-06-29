package com.fizzbuzz.ejb;

import com.fizzbuzz.data.FizzBuzzNumber;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;

@Singleton
@LocalBean
public class FizzBuzzComputer 
{
    public String getFizzBuzzPhrase(FizzBuzzNumber fizzBuzzNumber)
    {
        if(!fizzBuzzNumber.isValid())
        {
            return null;
        }
        
        int number = fizzBuzzNumber.getNumber();
        StringBuilder phrase = new StringBuilder();
        
        for(int counter = 1;
            counter <= number;
            counter++)
        {
            boolean word = false;
            
            if(counter % 3 == 0)
            {
                word = true;
                
                phrase.append("fizz");
            }
            
            if(counter % 5 == 0)
            {
                if(word)
                {
                    phrase.append(" ");
                }
                
                word = true;
                
                phrase.append("buzz");
            }
            
            if(!word)
            {
                phrase.append(counter);
            }
            
            if(counter != number)
            {
                phrase.append(", ");
            }
        }
        
        return phrase.toString();
    }
}
