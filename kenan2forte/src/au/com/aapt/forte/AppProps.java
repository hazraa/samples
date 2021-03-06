/*
 * AppProps.java
 *
 * Created on 14 November 2007, 11:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.com.aapt.forte;


import java.util.*;
import java.io.*;
import java.net.*;
import java.text.*;

import org.apache.log4j.Logger;

/**
 * Common application configuration and runtime information used by
 * most of the classes
 *
 * @author t600387
 */
public final class AppProps {
    static AppProps theInstance=null;

    final static String rcsid = "$Source: /import/cvsroot/dev/internal/kenan2forte/src/au/com/aapt/forte/AppProps.java,v $";
    final static String rcssrc= "$Id: AppProps.java,v 1.4 2012/05/30 05:37:12 harryr Exp $";

    static Properties props=null;

    static String config=null;
    static int debuglevel=0;
    static int loglevel=4;
    static Date fdate=null;
    static Date tdate=null;
    static Date lastprocessed=null;
    static boolean daily=true;
    static boolean doInsert=false;
    
    static String importfile=null;
    static String exportfile=null;
    private String mailTo=null;
    private String mailFrom=null;
    private String mailHost=null;
    
   // static String reportfile=null;
   // static int addcustomer=0;
   // static int addaccount=0;

    private String warehouse_dburl;
    private String warehouse_dbuser;
    private String warehouse_dbpass;

    private String forte_dburl;
    private String forte_dbuser;
    private String forte_dbpwd;

    public static int UNDEF=0;
    /**
     * Current Application state
     */
    public static int forte_RERUN=1;
    /**
     * Current Application state
     */
    public static int WAREHOUSE_RERUN=2;
    /**
     * Current Application state
     */
    public static int forte_DAILY=3;
    /**
     * Current Application state
     */
    public static int WAREHOUSE_DAILY=4;

    public static int BIOS_DAILY=5;
    public static int BIOS_RERUN=6;

    private static int currentstate=UNDEF;

    private static Logger  log = Logger.getLogger("au.com.aapt.forte.AppProps");

    /** Creates a new instance of AppProps */
    public AppProps() {
        // --fromdate=[HH:MI:SS-]DD-MON-YYYY
        // Set default dates based on nowtime

        tdate = new Date();
   /*
        DateUtil du = new DateUtil();
        Long nowepoch = du.str2epoch(tdate);
        fdate = new Date( (nowepoch - 86400) * 1000);
     */
    }

    /**
     * Stringify this object
     * @return A String representation of this object
     */
    


    public Date parseDate(String datestr) throws K2FException
    {
         // --fromdate=[HH:MI:SS:]DD-MON-YYYY

        Date date=null;

        String fmt="none";
        String fmt1= "yyyyMMdd";
        String fmt2 = "HH:mm:ss:dd-MMM-yyyy";
        String fmt3= "dd-MMM-yyyy";

        //System.err.println("parseDate:" + datestr);

        ParsePosition pos = new ParsePosition(0);

        if (datestr.matches("^\\d\\d\\d\\d\\d\\d\\d\\d")==true) {
            fmt=fmt1;
        } else if (datestr.matches("^\\d\\d:\\d\\d:\\d\\d:\\d\\d-\\w\\w\\w-\\d\\d\\d\\d")==true) {
            fmt=fmt2;
        } else if (datestr.matches("\\d\\d-\\w\\w\\w-\\d\\d\\d\\d")==true) {
            fmt=fmt3;
        } else
            throw  new K2FException("Invalid data format given: " + datestr);


        //System.err.println("fmt=" + fmt);

        Calendar cal = new GregorianCalendar();

        DateFormat df = new SimpleDateFormat(fmt);
        date = df.parse(datestr, pos);

        if (date==null) throw new K2FException("Unable to parse date string: " + datestr);

        return date;
    }
    /*
     * Return a string representation of date in the form
     * DD-MON-YYYY HH24:MI:SS
     * Easy to use in oracle.
     *
     * Prolly need a sybase version too
     *
     **/
    public String getDatestr(Date date)
    {
        if (date==null) return null;

        StringBuffer sb = new StringBuffer();

        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        sb.append(cal.get(Calendar.YEAR));
        sb.append("-");
        sb.append(cal.get(Calendar.MONTH)+1);
        sb.append("-");
        sb.append(cal.get(Calendar.DAY_OF_MONTH));
        sb.append(" ");
        sb.append(cal.get(Calendar.HOUR_OF_DAY));
        sb.append(":");
        sb.append(cal.get(Calendar.MINUTE));
        sb.append(":");
        sb.append(cal.get(Calendar.SECOND));

        return sb.toString();

    }
    public void setCurrentState(int state)
    {
        this.currentstate=state;
    }
    /**
     *
     * @return
     */
    public int getCurrentState()
    {
        return currentstate;
    }

    public void setConfig(String config)
    {
        this.config=config;
    }
    public String getConfig()
    {
        return this.config;
    }

    public void setDebuglevel(int debuglevel)
    {
        this.debuglevel=debuglevel;
    }
    public int getDebuglevel()
    {
        return (this.debuglevel);
    }
    public void setLogLevel(int loglevel)
    {
        this.loglevel=loglevel;
    }
    public int getLogLevel()
    {
        return (this.loglevel);
    }

    public void setFromDate(String fdate) throws K2FException
    {
        this.fdate=parseDate(fdate);

    }
    public void setFromDate(Date date) throws K2FException
    {
        this.fdate=date;

    }
    public Date getFromDate()
    {
        return (this.fdate);
    }
    public String getFromDateString()
    {
        return this.getDatestr(this.fdate);
    }

    // return fromdate  less 24hrs
    public Date getFromDate_24()
    {
          DateUtil du = new DateUtil();
          Long epoch = du.str2epoch(getFromDate());
          return ( new java.util.Date( (epoch.longValue() - 86400) * 1000));
    }

    public void setToDate(String tdate) throws K2FException
    {
        this.tdate=parseDate(tdate);
    }
    public void setToDate(Date date) throws K2FException
    {
        this.tdate=date;
    }
    public Date getToDate()
    {
        return (this.tdate);
    }
    public void setLastProcessed(Date date)
    {
        this.lastprocessed=date;
    }
    public Date getLastProcessed()
    {
        return (this.lastprocessed);
    }
    public String getToDateString()
    {
        return this.getDatestr(this.tdate);
    }

    public void setDaily(boolean daily)
    {
        this.daily=daily;
    }
    public boolean getDaily()
    {
        return (this.daily);
    }
    /*
    public String getLog4jConfig()
    {
        return this.log4j_config;
    }
    public void setLog4jConfig(String log4j)
    {
        this.log4j_config=log4j;
    }
*/
    public String getWarehouseDburl()
    {
        return this.warehouse_dburl;
    }
    public void setWarehouseDburl(String url)
    {
        this.warehouse_dburl=url;
    }
    public String getWarehouseDbuser()
    {
        return this.warehouse_dbuser;
    }
    public void setWarehouseDbuser(String user)
    {
        this.warehouse_dbuser=user;
    }
    public String getWarehouseDbpwd()
    {
        return this.warehouse_dbpass;
    }
    public void setWarehouseDbpwd(String pwd)
    {
        this.warehouse_dbpass=pwd;
    }
    public String getForteDburl()
    {
        return this.forte_dburl;
    }
    public void setForteDburl(String url)
    {
        this.forte_dburl=url;
    }
    public String getForteDbuser()
    {
        return this.forte_dbuser;
    }
    public void setForteDbuser(String user)
    {
        this.forte_dbuser=user;
    }
    public String getForteDbpwd()
    {
        return this.forte_dbpwd;
    }
    public void setForteDbpwd(String pwd)
    {
        this.forte_dbpwd=pwd;
    }

    public String getExport()
    {
        return exportfile;
    }
    public void setExport(String filename)
    {
        this.exportfile=filename;
    }

    public String getImport()
    {
        return importfile;
    }
    public void setImport(String filename)
    {
        this.importfile=filename;
    }

    public  boolean isDoInsert() {
        return doInsert;
    }

    public  void setDoInsert(boolean doInsert) {
        AppProps.doInsert = doInsert;
    }

    public  String getMailTo() {
		return mailTo;
	}

	public  void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public  String getMailFrom() {
		return mailFrom;
	}

	public  void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public  String getMailHost() {
		return mailHost;
	}

	public  void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	public static AppProps getInstance() {
        if (theInstance==null) {
            theInstance = new AppProps();
            //theInstance.loadProps(sResource);
        }

        return  theInstance;
    }

    public static AppProps getInstance(String sResource) throws IOException, K2FException {
        //AppProps theInstance = new AppProps();
        if (theInstance==null) {
            theInstance = new AppProps();
            theInstance.loadConfig(sResource);
        }
        return theInstance;
    }

    private String getPropertiesFromClassPath(String propertyFile) throws K2FException {
        String propertiesFile = null;
        props = new Properties();

        URL url = this.getClass().getClassLoader().getResource(propertyFile);

        if (null != url) {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(propertyFile);
            try {
                props.load(is);
                propertiesFile = url.toString();
            } catch (IOException e) {
                throw new K2FException("Unable to load properties file from classpath: " + propertyFile.toString());
            } finally {
                safeCloseStream(is);
            }
        }
        return propertiesFile;
    }

    private void safeCloseStream(InputStream is) {
        if (null != is) {
            try {
                is.close();
            } catch (Exception e) {
            }
        }
    }

    public void loadConfig() throws IOException, K2FException {
        this.setConfig(getPropertiesFromClassPath("application.properties"));
        log.info("properties=" + this.config);
        setConfig();
    }

    public void loadConfig(String sResource) throws IOException, K2FException {
//        String resource=null;
        URL url=null;

//        try {
        log.debug("loadProps()");

         props = new Properties();
         if (getDebuglevel()>3)
             System.err.println("config=" + sResource);

        InputStream in = new FileInputStream(new File(this.getConfig()));

        props.load(in);

        setConfig();

    }

        public void setConfig() throws IOException, K2FException
        {

      //  setDebuglevel(Integer.parseInt(props.getProperty("debuglevel","0")));

        this.setDebuglevel(new Integer(props.getProperty("debug","0")).intValue());

        this.setForteDburl(props.getProperty("forte_dburl","error"));
        this.setForteDbuser(props.getProperty("forte_dbuser","error"));
        this.setForteDbpwd(props.getProperty("forte_dbpwd","error"));

        this.setWarehouseDburl(props.getProperty("warehouse_dburl","error"));
        this.setWarehouseDbuser(props.getProperty("warehouse_dbuser","error"));
        this.setWarehouseDbpwd(props.getProperty("warehouse_dbpwd","error"));

        this.setMailTo(props.getProperty("mailTo",null));
        this.setMailFrom(props.getProperty("mailFrom",null));
        this.setMailHost(props.getProperty("mailHost",null));

    }

}
