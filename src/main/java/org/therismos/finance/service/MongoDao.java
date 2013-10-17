package org.therismos.finance.service;

import com.mongodb.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.therismos.finance.model.Account;

/**
 * A dao to retrieve data from mongolab, which keeps "accounts" as a collection
 * @author cpliu
 */
//@javax.ejb.Startup
@javax.inject.Singleton
public class MongoDao implements java.io.Serializable {
    
    MongoClient mongoClient;
    DB db;
    DBCollection collection;
    boolean auth;
    ArrayList<Account> results;
    
    public MongoDao() {
        results = new ArrayList<Account>();
    }
    
    @javax.annotation.PostConstruct
    public void init() {
        results.clear();
        try {
//            mongoClient = new MongoClient();
            mongoClient = new MongoClient("ds043358.mongolab.com", 43358);
            db = mongoClient.getDB("therismos");
            auth = db.authenticate("therismos","26629066".toCharArray());
//        if (!auth) return;
            collection = db.getCollection("accounts");
            collection.setObjectClass(Account.class);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MongoDao.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        Iterator<DBObject> it = collection.find().sort(new BasicDBObject("id",1)).iterator();
        while (it.hasNext()) {
            results.add((Account)it.next());
        }
    }
    
    public Account getAccountByCode(String code) {
        for (Account a : results) {
            if (a.getCode().equals(code)) return a;
        }
        return null;
    }
    
    public Account getAccountById(int id) {
        for (Account a : results) {
            if (id == a.getId()) return a;
        }
        return null;
    }
    
    public java.util.List<Account> getAccounts() {
        return results;
    }
    
    public List<DBObject> getAccountsBelow(String summaryCode) throws UnknownHostException {
//        mongoClient = new MongoClient("ds043358.mongolab.com", 43358);
//        db = mongoClient.getDB("therismos");
//        auth = db.authenticate("therismos","26629066".toCharArray());
//        if (!auth) return Collections.EMPTY_LIST;
        DBObject match = new BasicDBObject("code", new BasicDBObject("$regex", "^"+summaryCode) );
        DBCursor cursor = collection.find(match);
        return cursor.toArray();
    }

    public Account convertToModel(DBObject raw) {
        Object o = raw.get("id");
        int id=-1;
        if (o instanceof Integer) id=(Integer)o;
        else if (o instanceof Double) id=((Double)o).intValue();
        Account a = new Account(id, (String)raw.get("name"));
        o = raw.get("code");
        if (o instanceof String)
            a.setCode((String)o);
        o = raw.get("name_chi");
        if (o instanceof String)
            a.setName_chi((String)o);
        o = raw.get("code");
        if (o instanceof String)
            a.setCode((String)o);
        o = raw.get("detail");
        if (o instanceof String)
            a.setDetail((String)o);
        return a;
    }
    
    /**
     * Save an account, never changes its id, in addition to _id
     * @param a Account to save
     * 
     * @throws RuntimeException 
     */
    public void saveAccount(Account a) {
        Account o = (Account)collection.findOne(new BasicDBObject("id",a.getId()));
        if (o==null) throw new java.lang.RuntimeException("Account not found, use saveNewAccount instead");
        o.setCode(a.getCode());
        o.setName(a.getName());
        o.setDetail(a.getDetail());
        o.setName_chi(a.getName_chi());
        collection.save(o);
    }
    
    public void createAccount() throws java.io.IOException {
        DBCollection accs = db.getCollection("accounts");
        java.io.BufferedReader rdr = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream("/home/cpliu/ChurchAdmin/accounts.tmp")));
        String [] tokens;
        String line;
        while((line = rdr.readLine()) != null) {
            tokens = line.split(",");
            int l = tokens.length;
            if (l<4) continue;
            BasicDBObject doc = new BasicDBObject("id", Integer.parseInt(tokens[0]))
                .append("name", tokens[1])
                .append("name_chi", tokens[2])
                .append("code", tokens[3]);
            if (l>4)
                doc.append("detail", tokens[4]);
            accs.insert(doc);
        }
    }
    
    public static void main(String[] args) {
        MongoDao dao = new MongoDao();
        dao.init();
//        Account a = new Account();
//        a.setId(10002);
//        a.setCode("10A1");
//        a.setDetail(new java.util.Date().toString());
//        a.setName("來往");
        for (Account a : dao.getAccounts()) {
            System.out.println(a.getId() + ":" + a.getName_chi());
        }
    }
    
}
