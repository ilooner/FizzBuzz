/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fizzbuzz.rest;

import com.fizzbuzz.data.FizzBuzzCall;
import com.fizzbuzz.data.FizzBuzzCallReplay;
import com.fizzbuzz.data.FizzBuzzNumber;
import com.fizzbuzz.ejb.FizzBuzzCallBean;
import com.fizzbuzz.ejb.FizzBuzzComputer;
import com.fizzbuzz.ejb.TwilioVerify;
import static com.fizzbuzz.rest.NumberGatherer.URL_BASE;
import com.twilio.sdk.verbs.Hangup;
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

@WebServlet(name = "Replay", urlPatterns = {"/Replay"})
public class Replay extends HttpServlet 
{
    
    public static final String URL_BASE = "https://www.tricktry.com/FizzBuzz-war/Replay";
    
    @EJB
    private FizzBuzzComputer fizzBuzzComputer;
    
    @EJB
    private TwilioVerify twilioVerify;
    
    @EJB
    private FizzBuzzCallBean fizzBuzzCallBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        FizzBuzzCallReplay fizzBuzzCallReplay = new FizzBuzzCallReplay();
        
        if(!fizzBuzzCallReplay.setVals(request))
        {
            return;
        }
        
        if(!twilioVerify.verifyTwilio(request,
                                      response,
                                      URL_BASE + fizzBuzzCallReplay.toParams()))
        {
            return;
        }
        
        FizzBuzzNumber fizzBuzzNumber = new FizzBuzzNumber(fizzBuzzCallReplay.getFizzBuzzNumber());
                
        ////
        
        String fizzBuzzPhrase = fizzBuzzComputer.getFizzBuzzPhrase(fizzBuzzNumber);
        
        response.setContentType("text/xml;charset=UTF-8");
        
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
        
        FizzBuzzCall fizzBuzzCall = new FizzBuzzCall();
        
        fizzBuzzCall.setPhoneNumberString(fizzBuzzCallReplay.getPhoneNumber());
        fizzBuzzCall.setMinuteDelayInt(0);
        fizzBuzzCall.setFizzBuzzNumber(fizzBuzzNumber);
        fizzBuzzCall.setDateValue(Calendar.getInstance().getTime().getTime());
        
        fizzBuzzCallBean.addFizzBuzzCall(fizzBuzzCall);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
        return "Short description";
    }
}
