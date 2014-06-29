package com.fizzbuzz.ejb;

import com.fizzbuzz.data.PhoneNumber;
import com.fizzbuzz.data.MinuteDelay;
import com.fizzbuzz.data.CallRequest;
import com.fizzbuzz.data.FizzBuzzCall;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.CallFactory;
import com.twilio.sdk.resource.instance.Call;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

@Stateless
@LocalBean
public class CallRequestBean 
{
    @Resource
    TimerService timerService;
    
    public void processCallRequest(CallRequest callRequest)
    {
        PhoneNumber phoneNumber = new PhoneNumber(callRequest.getPhoneNumber());
        MinuteDelay minuteDelay = new MinuteDelay(callRequest.getMinuteDelay());
        
        if(!phoneNumber.isValid())
        {
            return;
        }
        
        if(!minuteDelay.isValid())
        {
            return;
        }
        
        FizzBuzzCall fizzBuzzCall = new FizzBuzzCall();
        fizzBuzzCall.setPhoneNumber(phoneNumber);
        fizzBuzzCall.setMinuteDelay(minuteDelay);
        
        if(minuteDelay.getMinuteDelay() == 0)
        {
            makeCall(fizzBuzzCall);
        }
        else
        {
            timerService.createTimer(1000 * 60 * minuteDelay.getMinuteDelay(), fizzBuzzCall);
        }
    }
    
    public void makeCall(FizzBuzzCall fizzBuzzCall)
    {
        TwilioRestClient client = new TwilioRestClient(TwilioVerify.TWILIO_ACCOUNT_SID, TwilioVerify.TWILIO_AUTH_TOKEN);
 
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("https://www.tricktry.com/FizzBuzz-war/NumberGatherer");
        urlBuilder.append(fizzBuzzCall.toParams());
        
        System.out.println(urlBuilder.toString());
        
        // Build a filter for the CallList
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Url", urlBuilder.toString()));
        params.add(new BasicNameValuePair("To", fizzBuzzCall.getPhoneNumberString()));
        params.add(new BasicNameValuePair("From", TwilioVerify.TWILIO_NUMBER));
        
        CallFactory callFactory = client.getAccount().getCallFactory();
        
        try 
        {
            Call call = callFactory.create(params);
        }
        catch(TwilioRestException ex)
        {
            Logger.getLogger(CallRequestBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Timeout
    public void makeCall(Timer timer)
    {
        makeCall((FizzBuzzCall) timer.getInfo());
    }
}
