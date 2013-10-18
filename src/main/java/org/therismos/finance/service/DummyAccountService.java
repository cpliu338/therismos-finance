package org.therismos.finance.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import org.therismos.finance.model.Transaction;
import org.therismos.finance.model.TransactionWrapper;

/**
 * A dummy implementation that gets data from: /home/cpliu/Documents/Temp/DummyTransactions.json
 * @author cpliu
 */
public class DummyAccountService extends AccountServiceImpl {

    public static final String path = "/home/cpliu/Documents/Temp/DummyTransactions.json";

    @Override
    public Object download(String cutOffDate) throws IOException {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<java.util.Collection<TransactionWrapper>>(){}.getType();
        java.util.Collection<TransactionWrapper> results;
        results = gson.fromJson(new java.io.InputStreamReader(new java.io.FileInputStream(path)), collectionType);
        int size = (results==null) ? 0 : results.size();
        if (size ==0) return 0;
        java.util.ArrayList<Transaction> ar = new java.util.ArrayList<Transaction>();
        java.util.Iterator<TransactionWrapper> it = results.iterator();
        while (it.hasNext()) {
            ar.add(it.next().getTransaction());
        }
        return ar;
    }

}
