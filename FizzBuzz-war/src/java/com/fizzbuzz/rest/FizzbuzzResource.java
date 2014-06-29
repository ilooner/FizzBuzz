/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fizzbuzz.rest;

import com.fizzbuzz.data.CallRequest;
import com.fizzbuzz.data.FizzBuzzCallReplay;
import com.fizzbuzz.data.FizzBuzzCalls;
import com.fizzbuzz.ejb.CallRequestBean;
import com.fizzbuzz.ejb.FizzBuzzCallBean;
import com.fizzbuzz.ejb.ReplayBean;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("fizzbuzz")
@Stateless
public class FizzbuzzResource 
{
    @Context
    private UriInfo context;

    @EJB
    private CallRequestBean callRequestBean;
    
    @EJB
    private FizzBuzzCallBean fizzBuzzCallBean;
    
    @EJB
    private ReplayBean replayBean;
    
    public FizzbuzzResource()
    {
    }

    @GET
    @Path("calls")
    @Produces("application/json")
    public FizzBuzzCalls getCalls()
    {
        return fizzBuzzCallBean.getCalls();
    }
    
    @PUT
    @Consumes("application/json")
    public void requestCall(CallRequest callRequest)
    {
        callRequestBean.processCallRequest(callRequest);
    }
    
    @PUT
    @Path("replay")
    @Consumes("application/json")
    public void replay(FizzBuzzCallReplay callReplay)
    {
        replayBean.replay(callReplay);
    }
}
