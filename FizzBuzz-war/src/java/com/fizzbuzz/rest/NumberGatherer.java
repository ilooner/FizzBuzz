/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fizzbuzz.rest;

import com.fizzbuzz.data.FizzBuzzCall;
import com.fizzbuzz.data.FizzBuzzNumber;
import com.fizzbuzz.ejb.TwilioVerify;
import com.twilio.sdk.verbs.Gather;
import com.twilio.sdk.verbs.Redirect;
import com.twilio.sdk.verbs.Say;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringEscapeUtils;

@WebServlet(name = "NumberGatherer", urlPatterns = {"/NumberGatherer"})
public class NumberGatherer extends HttpServlet
{
    public static final String URL_BASE = "https://www.tricktry.com/FizzBuzz-war/NumberGatherer";
    
    @EJB
    private TwilioVerify twilioVerify;
    
    protected void processRequest(HttpServletRequest request,
                                  HttpServletResponse response) throws
                                  ServletException, IOException 
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
        
        response.setContentType("text/xml;charset=UTF-8");
        
        
        ////
        
        TwiMLResponse twimlResponse = new TwiMLResponse();
        
        try 
        {
            Gather gather = new Gather();
            gather.setAction(StringEscapeUtils.escapeXml(ProcessNumber.URL_BASE + fizzBuzzCall.toParams()));
            gather.setNumDigits(FizzBuzzNumber.MAX_DIGITS);

            Say sayEnter = new Say("Please enter a fizz buzz number between 1 and 999. Press pound when you are done. Thank you.");
            gather.append(sayEnter);
            
            Say sorry = new Say("Sorry, I didn't get your response.");
            Redirect redirect = new Redirect(StringEscapeUtils.escapeXml(URL_BASE + fizzBuzzCall.toParams()));
            redirect.setMethod("POST");
            
            twimlResponse.append(gather);
            twimlResponse.append(sorry);
            twimlResponse.append(redirect);
        } 
        catch(TwiMLException ex)
        {
            Logger.getLogger(NumberGatherer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        try(PrintWriter out = response.getWriter()) 
        {
            System.out.println(twimlResponse.toEscapedXML());
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + twimlResponse.toEscapedXML());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo()
    {
        return "Gets the number from the user";
    }
}
