package org.therismos.finance.web;

import com.google.gson.Gson;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
//import javax.enterprise.context.RequestScoped;
import org.therismos.finance.model.Account;
import org.therismos.finance.service.MongoDao;

/**
 *
 * @author cpliu
 */
//@Named(value = "accountBean")
@javax.faces.bean.ManagedBean
@javax.faces.bean.SessionScoped
public class AccountBean {
    @javax.ejb.EJB
    private MongoDao dao;
    
    private int key;
    private Account selectedAccount;
    
    @PostConstruct
    public void init() {
        Logger.getLogger(AccountBean.class.getName()).entering("AccountBean", "<init>");
//        this.setId(10);
    }
    
    public java.util.List<Account> getAccounts() {
        return dao.getAccounts();
    }
    
    public String getAccountsAsJson() {
        Gson gson = new Gson();
        return gson.toJson(dao.getAccounts());
    }
    
    public String edit() {
        return "editAccount";
    }
    
    public Account getSelectedAccount() {
        return selectedAccount;
    }
    
    private String stripquotes(String s) {
        String s2 = s;
        if (s2.startsWith("\""))
            s2 = s2.substring(1);
        if (s2.endsWith("\""))
            s2 = s2.substring(0, s2.length()-1);
        return s2;
    }
    
    public String save() {
        selectedAccount.setName(stripquotes(selectedAccount.getName()));
        selectedAccount.setName_chi(stripquotes(selectedAccount.getName_chi()));
        selectedAccount.setCode(stripquotes(selectedAccount.getCode()));
        dao.saveAccount(selectedAccount);
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Saved"));
        return "accounts?faces-redirect=true";
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(MongoDao dao) {
        this.dao = dao;
    }

    /**
     * @param selectedAccount the selectedAccount to set
     */
    public void setSelectedAccount(Account selectedAccount) {
        this.selectedAccount = selectedAccount;
    }
}
