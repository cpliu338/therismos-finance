package org.therismos.finance.web;

import com.google.gson.Gson;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.therismos.finance.model.Transaction;
import org.therismos.finance.service.AccountService;
import org.therismos.finance.service.MongoDao;
import org.therismos.finance.service.MonthlyReportTask;
import org.therismos.finance.service.RemoteServer;

/**
 *
 * @author cpliu
 */
//@javax.faces.bean.ViewScoped
@javax.inject.Named
@SessionScoped
public class TransactionBean implements java.io.Serializable {
    
    @javax.inject.Inject @RemoteServer
    private AccountService accountService;
    @javax.inject.Inject
    private MongoDao mongoDao;
    
    private java.util.List<Transaction> transactions;
    private MonthlyReportTask task;
    private java.util.Date cutoffDate;
    //public static final long serialVersionUID = 670345243L;
    
    public TransactionBean() {
        transactions = new java.util.ArrayList<Transaction> ();
        cutoffDate = new java.util.Date();
        task = new MonthlyReportTask();
    }
    
    public String getProgress() {
        return task.getMessage();
    }
    
    public String refresh() {
        String err = task.getError();
        if (err != null) {
            FacesMessage msg = new FacesMessage(err);
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return null;
    }
   
    public String exec() {
        task.setAccountService(accountService);
        task.setMongoDao(mongoDao);
        task.setLevel(3);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        task.setCutoffDate(fmt.format(cutoffDate));
        //serv.setUrlTransactionsPattern("http://localhost/ChurchAdmin/trans.json");
        Thread runner = new Thread(task);
        runner.start();
        //Thread.sleep(3000);
        return null;
    }

    /**
     * @param accountService the accountService to set
     */
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * @return the cutoffDate
     */
    public java.util.Date getCutoffDate() {
        return cutoffDate;
    }

    /**
     * @param cutoffDate the cutoffDate to set
     */
    public void setCutoffDate(java.util.Date cutoffDate) {
        this.cutoffDate = cutoffDate;
    }
}
