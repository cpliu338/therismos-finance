package org.therismos.finance.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.therismos.finance.model.Transaction;
import org.therismos.finance.model.TransactionWrapper;

/**
 *
 * @author cpliu
 */
//@javax.ejb.Stateless
@RemoteServer
public class AccountServiceImpl implements AccountService, java.io.Serializable {

    private String urlTransactionsPattern = "http://therismos.dyndns.org/ChurchAdmin/index.php/transactions/download/%s?to=%s";
    public static final long serialVersionUID = 9085672645L;
    
    @Override
    public Object download(String cutOffDate) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(String.format(urlTransactionsPattern, cutOffDate.substring(0,4),cutOffDate));
        HttpResponse resp = client.execute(get);
        if (resp.getStatusLine().getStatusCode()>200)
            return resp.getStatusLine();
        if (resp.getEntity().getContentType().getValue().indexOf("javascript") < 0 &&
                resp.getEntity().getContentType().getValue().indexOf("json") < 0) 
            return resp.getEntity().getContentType().getValue();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<java.util.Collection<TransactionWrapper>>(){}.getType();
        java.util.Collection<TransactionWrapper> results;
        results = gson.fromJson(new java.io.InputStreamReader(resp.getEntity().getContent(), "UTF-8"), collectionType);
        int size = (results==null) ? 0 : results.size();
        if (size ==0) return 0;
        java.util.ArrayList<Transaction> ar = new java.util.ArrayList<Transaction>();
        java.util.Iterator<TransactionWrapper> it = results.iterator();
        while (it.hasNext()) {
            ar.add(it.next().getTransaction());
        }
        return ar;
    }

    /**
     * @return the urlTransactionsPattern
     */
    public String getUrlTransactionsPattern() {
        return urlTransactionsPattern;
    }

    /**
     * @param urlTransactionsPattern the urlTransactionsPattern to set
     */
    public void setUrlTransactionsPattern(String urlTransactionsPattern) {
        this.urlTransactionsPattern = urlTransactionsPattern;
    }
    
    private java.util.Map<String, Double> totals;

    @Override
    public void clearTotals() {
        if (totals == null)
            totals = new java.util.HashMap<String, Double>();
        else
            totals.clear();
    }
    
    @Override
    public void reckon(int level, String code2, Double amount) {
        if (level < 1) throw new java.lang.IllegalArgumentException("Level must be at least 1");
        String code = code2;
        if (code2.length()>level)
            code = code2.substring(0, level).concat("0");
        if (totals.containsKey(code)) 
            totals.put(code, totals.get(code)+amount);
        else
            totals.put(code, amount);
    }

    /**
     * @return the totals
     */
    @Override
    public java.util.Map<String, Double> getTotals() {
        return totals;
    }
}
