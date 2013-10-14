package org.therismos.finance.model;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author cpliu
 */
public class TransactionWrapper {
    public static final String key = "Transaction";
    private Transaction Transaction;
    
    public TransactionWrapper(Transaction t) {
        Transaction = t;
    }
    
    public TransactionWrapper() {
        Transaction = null;
    }
    
    public void setTransaction (Transaction t) {
        Transaction = t;
    }
    
    public Transaction getTransaction () {
        return Transaction;
    }
    
    @Override
    public String toString() {
        return new Gson().toJson(this);
//        Transaction t = Transaction.get(key);
//        return t==null ? "Null" : "Wrapper of " + t.toString();
    }

}
