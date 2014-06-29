package com.fizzbuzz.ejb;

import com.fizzbuzz.data.FizzBuzzCall;
import com.fizzbuzz.data.FizzBuzzCalls;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

@Stateless
@LocalBean
@TransactionManagement(value=TransactionManagementType.BEAN)
public class FizzBuzzCallBean 
{
    @Resource
    private UserTransaction ut;
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    public void addFizzBuzzCall(FizzBuzzCall fizzBuzzCall)
    {
        EntityManager em = null;
        
        try
        {
            ut.begin();
            
            em = emf.createEntityManager();
            em.persist(fizzBuzzCall);
            
            ut.commit();
        }
        catch(Exception e)
        {
            Logger.getLogger(FizzBuzzCallBean.class.getName()).log(Level.SEVERE, null, e);
            
            try
            {
                ut.rollback();
            }
            catch(Exception e1)
            {
                Logger.getLogger(FizzBuzzCallBean.class.getName()).log(Level.SEVERE, null, e1);
            }
        }
        finally
        {
            if(em != null)
            {
                em.close();
            }
        }
    }
    
    public FizzBuzzCalls getCalls()
    {
        EntityManager em = null;
        List<FizzBuzzCall> fizzBuzzCallList = null;
        
        try
        {
            ut.begin();
            
            em = emf.createEntityManager();
            Query query = em.createNamedQuery("getCallsByDate");
            
            fizzBuzzCallList = query.getResultList();
            
            ut.commit();
        }
        catch(Exception e)
        {
            Logger.getLogger(FizzBuzzCallBean.class.getName()).log(Level.SEVERE, null, e);
            
            try
            {
                ut.rollback();
            }
            catch(Exception e1)
            {
                Logger.getLogger(FizzBuzzCallBean.class.getName()).log(Level.SEVERE, null, e1);
            }
        }
        finally
        {
            if(em != null)
            {
                em.close();
            }
        }
        
        for(FizzBuzzCall fizzBuzzCall: fizzBuzzCallList)
        {
            fizzBuzzCall.updateDateString();
            System.out.println(fizzBuzzCall.getDateString());
        }
        
        FizzBuzzCalls fizzBuzzCalls = new FizzBuzzCalls();
        fizzBuzzCalls.setFizzBuzzCalls(fizzBuzzCallList);
        
        return fizzBuzzCalls;
    }
}
