package org.therismos.finance.web;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Model;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.therismos.finance.service.MonthlyReportTask;

/**
 *
 * @author cpliu
 */
@Model
public class ReportFileDownload {
    
    public List<String> getReports() {
        File tmpdir = new File(MonthlyReportTask.tmpfolder);
        ArrayList<String> files = new ArrayList<String>();
        if (tmpdir.isDirectory()) {
            File[] results = tmpdir.listFiles(new java.io.FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.startsWith("Rep") && name.endsWith(".xlsx");
                }
            });
            for (File f : results) {
                String name = f.getName();
                int i = name.indexOf(".xlsx");
                if (i < 4) continue;
                try {
                    Date d = MonthlyReportTask.fmt.parse(name.substring(3, i));
                    // delete reports older than 10 days
                    if (d.before(new Date(System.currentTimeMillis()-10*86400000L))) {
                        f.delete();
                        continue;
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(ReportFileDownload.class.getName()).log(Level.SEVERE, null, ex);
                    continue;
                }
                files.add(name);
            }
        }
        return files;
    }
    
    private String fname;
    
    public StreamedContent getFile() {
        DefaultStreamedContent c;
        File f = new File(MonthlyReportTask.tmpfolder, fname);
        FileInputStream stream;
        try {
            stream = new FileInputStream(f);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReportFileDownload.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new DefaultStreamedContent(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", fname);
    }

    /**
     * @return the fname
     */
    public String getFname() {
        return fname;
    }

    /**
     * @param fname the fname to set
     */
    public void setFname(String fname) {
        this.fname = fname;
    }
}
