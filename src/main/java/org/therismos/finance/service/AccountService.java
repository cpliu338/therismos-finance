package org.therismos.finance.service;

import java.io.IOException;


/**
 * Gets a bunch of transactions/entries (e.g. from a server), keeps totals for each summary account
 * @author cpliu
 */
public interface AccountService {


    /**
     * Download the entire list of transactions/entries, in the same financial year and up to cutOffDate.
     * @param cutOffDate The cutoff date as a String, assumed yyyy-MM-dd
     * @return java.util.List of transactions/entries if successful.  String if there is some problem
     * @throws IOException 
     */
    public Object download(String cutOffDate) throws IOException;

    /**
     * Clear the totals, create totals if not already done
     */
    public void clearTotals();

    /**
     * Update (accumulate) the totals with an amount, under code but summarised to level
     * @param level if level of summary code. E.g. for level=2, code 115 will be reckoned to code 110
     * @param code
     * @param amount the amount of this transaction/entry
     */
    public void reckon(int level, String code, Double amount);

    /**
     * Get the totals
     * @return the totals
     */
    public java.util.Map<String, Double> getTotals();
}
