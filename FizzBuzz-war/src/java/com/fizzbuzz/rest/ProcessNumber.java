package com.fizzbuzz.rest;

import com.fizzbuzz.data.FizzBuzzCall;
import com.fizzbuzz.ejb.FizzBuzzComputer;
import com.fizzbuzz.data.FizzBuzzNumber;
import com.fizzbuzz.data.PhoneNumber;
import com.fizzbuzz.ejb.FizzBuzzCallBean;
import com.fizzbuzz.ejb.TwilioVerify;
import com.twilio.sdk.verbs.Hangup;
import com.twilio.sdk.verbs.Redirect;
import com.twilio.sdk.verbs.Say;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringEscapeUtils;

@WebServlet(name = "ProcessNumber", urlPatterns = {"/ProcessNumber"})
public class ProcessNumber extends HttpServlet
{
    public static final String URL_BASE = "https://www.tricktry.com/FizzBuzz-war/ProcessNumber";
    
    @EJB
    private TwilioVerify twilioVerify;
    
    @EJB
    private FizzBuzzComputer fizzBuzzComputer;
    
    @EJB
    private FizzBuzzCallBean fizzBuzzCallBean;
    
    protected void processRequest(HttpServletRequest request,
                                  HttpServletResponse response)
            throws ServletException, IOException
    {
        FizzBuzzCall fizzBuzzCall = new FizzBuzzCall();
        
        if(!fizzBuzzCall.setVals(request))
        {
            return;
        }
        
        if(!twilioVerify.verifyTwilio(request,
                                      response,
                                      URL_BASE + fizzBuzzCall.toParams()))
        {
            return;
        }
        
        PhoneNumber phoneNumber = new PhoneNumber(request.getParameter("From"));
        
        if(!phoneNumber.isValid())
        {
            return;
        }
        
        FizzBuzzNumber fizzBuzzNumber = new FizzBuzzNumber(request.getParameter("Digits"));
                
        ////
        
        String fizzBuzzPhrase = fizzBuzzComputer.getFizzBuzzPhrase(fizzBuzzNumber);
        
        response.setContentType("text/xml;charset=UTF-8");
        
        if(fizzBuzzPhrase == null)
        {
            TwiMLResponse twimlResponse = new TwiMLResponse();
            
            try
            {
                Say sorry = new Say("Sorry, you did not enter a correct FizzBuzz number.");
                Redirect redirect = new Redirect(StringEscapeUtils.escapeXml(NumberGatherer.URL_BASE + fizzBuzzCall.toParams()));
                redirect.setMethod("POST");
                
                twimlResponse.append(sorry);
                twimlResponse.append(redirect);
            } 
            catch(TwiMLException ex)
            {
                Logger.getLogger(ProcessNumber.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try(PrintWriter out = response.getWriter())
            {
                out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + twimlResponse.toXML());
            }
            
            return;
        }
        
        TwiMLResponse twimlResponse = new TwiMLResponse();

        try 
        {
            Say say = new Say("Your fizz buzz phrase is the following. " + fizzBuzzPhrase + "Thank you. Bye.");
            Hangup hangup = new Hangup();

            twimlResponse.append(say);
            twimlResponse.append(hangup);
        } 
        catch(TwiMLException ex) 
        {
            Logger.getLogger(ProcessNumber.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        try(PrintWriter out = response.getWriter())
        {
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + twimlResponse.toEscapedXML());
        }
        
        ////
        
        if(!fizzBuzzCall.isPhoneNumberSet())
        {
            fizzBuzzCall.setPhoneNumber(phoneNumber);
        }
        
        if(!fizzBuzzCall.isMinuteDelaySet())
        {
            fizzBuzzCall.setMinuteDelayInt(0);
        }
        
        if(!fizzBuzzCall.isFizzBuzzNumberSet())
        {
            fizzBuzzCall.setFizzBuzzNumber(fizzBuzzNumber);
        }
        
        if(!fizzBuzzCall.isDateSet())
        {
            fizzBuzzCall.setDateValue(Calendar.getInstance().getTime().getTime());
        }
        
        fizzBuzzCallBean.addFizzBuzzCall(fizzBuzzCall);
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException 
    {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo()
    {
        return "Process Number";
    }
}
