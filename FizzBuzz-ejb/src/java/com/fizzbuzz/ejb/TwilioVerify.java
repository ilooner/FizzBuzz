
package com.fizzbuzz.ejb;

import com.fizzbuzz.data.FizzBuzzCall;
import com.fizzbuzz.data.FizzBuzzCallReplay;
import com.twilio.sdk.TwilioUtils;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
@LocalBean
public class TwilioVerify
{
    public static final String TWILIO_NUMBER = "+14429996869";
    public static final String TWILIO_AUTH_TOKEN = "872f10c2172d6395198d28cc0c9c3065";
    public static final String TWILIO_ACCOUNT_SID = "ACabb3cfb6fc5258944cf1c74e3501b8e9";
    
    private static final TwilioUtils twilioUtils = new TwilioUtils(TWILIO_AUTH_TOKEN);
    
    public boolean verifyTwilio(HttpServletRequest request,
                                HttpServletResponse response,
                                String urlBase)
    {
        String expectedSignature = request.getHeader("x-twilio-signature");
        
        Enumeration<String> keyEnumeration = request.getParameterNames();
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> parameterMapSimplified = new HashMap<String, String>();
        
        while(keyEnumeration.hasMoreElements())
        {
            String parameter = keyEnumeration.nextElement();
            
            if(parameter.equals(FizzBuzzCall.PARAM_DATE) ||
               parameter.equals(FizzBuzzCall.PARAM_FIZZ_BUZZ_NUMBER) ||
               parameter.equals(FizzBuzzCall.PARAM_MINUTE_DELAY) ||
               parameter.equals(FizzBuzzCall.PARAM_PHONE_NUMBER) ||
               parameter.equals(FizzBuzzCallReplay.PARAM_FIZZ_BUZZ_NUMBER) ||
               parameter.equals(FizzBuzzCallReplay.PARAM_PHONE_NUMBER))
            {
                continue;
            }
            
            
            String[] parameterArray = parameterMap.get(parameter);
            
            StringBuilder stringBuilder = new StringBuilder();
            
            for(String parameterArrayValue: parameterArray)
            {
                stringBuilder.append(parameterArrayValue);
            }
            
            parameterMapSimplified.put(parameter, stringBuilder.toString());
        }
        
        return twilioUtils.validateRequest(expectedSignature,
                                           urlBase,
                                           parameterMapSimplified);
    }
}
