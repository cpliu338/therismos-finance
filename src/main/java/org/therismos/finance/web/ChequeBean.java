package org.therismos.finance.web;

import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.therismos.finance.service.MongoDao;

/**
 *
 * @author cpliu
 */
@javax.inject.Named
@javax.enterprise.context.RequestScoped
public class ChequeBean {
    private String status = "Ready";
    @javax.inject.Inject
    private MongoDao mongoDao;
    private String month;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
    
    @javax.annotation.PostConstruct
    public void init() {
        DBCollection cheques = mongoDao.getCheques();
        if (cheques == null)
            status = "Null";
    }

    public String getStatus() {
        return status;
    }
    private String strUrl = "http://%s/ChurchAdmin/index.php/cheques/signed";
    
    public void download() {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(String.format(strUrl, "therismos.dyndns.org"));
            HttpResponse resp = client.execute(get);
            if (resp.getStatusLine().getStatusCode()>200) 
                throw new IOException("HTTP "+resp.getStatusLine().getStatusCode());
            if (resp.getEntity().getContentType().getValue().indexOf("javascript") < 0 &&
                    resp.getEntity().getContentType().getValue().indexOf("json") < 0) 
                throw new IOException("Mime: "+resp.getEntity().getContentType().getValue());
            String json = new java.util.Scanner(resp.getEntity().getContent(),"UTF-8").useDelimiter("\\A").next();
            com.mongodb.DBObject o = (com.mongodb.DBObject) JSON.parse(json);
            WriteResult result = mongoDao.saveCheques(month, o);
            status = result.getError()==null ? "OK" : result.getError();
        } catch (IOException ex) {
            Logger.getLogger(ChequeBean.class.getName()).log(Level.SEVERE, null, ex);
            status = ex.getMessage();
        } catch (RuntimeException ex) {
            Logger.getLogger(ChequeBean.class.getName()).log(Level.SEVERE, null, ex);
            status = ex.getClass().getName();
        }        
    }
}
