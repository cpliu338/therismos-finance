package org.therismos.finance.model;

import com.google.gson.Gson;

/**
 *
 * @author cpliu
 */
public class Transaction {
    private int id;
    private int account_id;
    private int tran_id;
    private String detail;
    private Double amount;
    /** date1 as a java.lang.String yyyy-MM-dd */
    private String date1;

    public Transaction() {
        amount=0.0;
        detail = "";
        date1 = "1970-01-01";
    }
    
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the account_id
     */
    public int getAccount_id() {
        return account_id;
    }

    /**
     * @param account_id the account_id to set
     */
    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    /**
     * @return the tran_id
     */
    public int getTran_id() {
        return tran_id;
    }

    /**
     * @param tran_id the tran_id to set
     */
    public void setTran_id(int tran_id) {
        this.tran_id = tran_id;
    }

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail the detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * @return the date1
     */
    public String getDate1() {
        return date1;
    }

    /**
     * @param date1 the date1 to set
     */
    public void setDate1(String date1) {
        this.date1 = date1;
    }

    /**
     * @return the amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
