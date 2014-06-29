package com.fizzbuzz.ejb;

import com.fizzbuzz.data.FizzBuzzCallReplay;
import com.fizzbuzz.data.FizzBuzzNumber;
import com.fizzbuzz.data.PhoneNumber;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.CallFactory;
import com.twilio.sdk.resource.instance.Call;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

@Stateless
@LocalBean
public class ReplayBean
{
    public void replay(FizzBuzzCallReplay callReplay)
    {
        PhoneNumber phoneNumber = new PhoneNumber(callReplay.getPhoneNumber());
        FizzBuzzNumber fizzBuzzNumber = new FizzBuzzNumber(callReplay.getFizzBuzzNumber());
        
        if(!phoneNumber.isValid() ||
           !fizzBuzzNumber.isValid())
        {
            return;
        }
        
        TwilioRestClient client = new TwilioRestClient(TwilioVerify.TWILIO_ACCOUNT_SID, TwilioVerify.TWILIO_AUTH_TOKEN);
        
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("https://www.tricktry.com/FizzBuzz-war/Replay");
        urlBuilder.append(callReplay.toParams());
        
        System.out.println(urlBuilder.toString());
        
        // Build a filter for the CallList
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Url", urlBuilder.toString()));
        params.add(new BasicNameValuePair("To", phoneNumber.getPhoneNumber()));
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
}
