package org.therismos.finance.web;

import java.text.SimpleDateFormat;
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
@javax.inject.Named
@javax.enterprise.context.ApplicationScoped
public class TransactionBean implements java.io.Serializable {
    
    @javax.inject.Inject @RemoteServer
    private AccountService accountService;
    @javax.inject.Inject
    private MongoDao mongoDao;
    
    private java.util.List<Transaction> transactions;
    private MonthlyReportTask task;
    private Thread runner;
    private java.util.Date cutoffDate;
    //public static final long serialVersionUID = 670345243L;
    
    public TransactionBean() {
        transactions = new java.util.ArrayList<Transaction> ();
        cutoffDate = new java.util.Date();
        runner = null;
    }
    
    public boolean isTaskRunning() {
        return runner!=null && runner.isAlive();
    }
    
    public String getProgress() {
        return task==null ? "Task not running" : task.getMessage();
    }
    
    public String refresh() {
        if (task==null) return null;
        String err = task.getError();
        if (err != null) {
            FacesMessage msg = new FacesMessage(err);
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return null;
    }
   
    public String exec() {
        if (this.isTaskRunning()) return null;
        task = new MonthlyReportTask();
        task.setAccountService(accountService);
        task.setMongoDao(mongoDao);
        task.setLevel(3);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        task.setCutoffDate(fmt.format(cutoffDate));
        //serv.setUrlTransactionsPattern("http://localhost/ChurchAdmin/trans.json");
        runner = new Thread(task);
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
