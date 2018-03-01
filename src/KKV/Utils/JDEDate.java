package KKV.Utils;

import KKV.DBControlSqlLite.DBFieldAdapters.DBTimeString;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.Format;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class JDEDate {
    private int value = 0;
    private Calendar cal = Calendar.getInstance();   

    public Calendar getCalendar() {
        return cal;
    }        

    public static void main(String args[]) throws SQLException {
        /*COM.ibm.db2.jdbc.app.DB2Driver DB2Driver = new COM.ibm.db2.jdbc.app.DB2Driver();
        DriverManager.registerDriver(DB2Driver);
        Connection con = DriverManager.getConnection("jdbc:db2:psidbmuB", "db2admin", "AW34rgy7");

        PreparedStatement stat = con.prepareStatement("DELETE FROM PSI_BW.CY2FY");
        stat.execute();

        stat = con.prepareStatement("INSERT INTO PSI_BW.CY2FY (CPRD, CY, CM, FY, FPRD) VALUES (?,?,?,?,?)");
        JDEDate jd = new JDEDate();
        jd.setRealDate(1990, 1, 1);
        while (jd.getRealYear() < 2099) {
            stat.setInt(1, jd.getRealYear() * 12 + jd.getRealMonth());
            stat.setInt(2, jd.getRealYear());
            stat.setInt(3, jd.getRealMonth());
            stat.setInt(4, jd.getFiscalYear());
            stat.setInt(5, jd.getFiscalYear() * 12 + jd.getRealMonth());
            stat.execute();
            System.out.println("CY:" + jd.getRealYear() + " CM:" + jd.getRealMonth() + " has been added");
            jd.incMonth(1);
        }*/
      
      JDEDate jd = new JDEDate();
      
      jd.setExcelDateWithTime(42139.423611111109494231641292572021484375);
      System.out.println("Date&Time : "+jd.getDateAsDDMMYYYY_andTime("."));

/*    JDEDate jd = new JDEDate();

    //jd.setJDEDate(105355);
    jd.setJDEDateAsDDMMYYYY("01-01-2008",",");
    System.out.println(jd.getJDEDateAsYYMMDD()+" week:"+jd.getWeek());

    jd.setJDEDateAsDDMMYYYY("01-01-2009",",");
    System.out.println(jd.getJDEDateAsYYMMDD()+" week:"+jd.getWeek());

    jd.setJDEDateAsDDMMYYYY("01-01-2010",",");
    System.out.println(jd.getJDEDateAsYYMMDD()+" week:"+jd.getWeek());
*/

        /* jd.setRealDate(2004,4,2);

       SystemOut.println(jd.getJDEDateAsYYMMDD());

       jd.setJDEDateAsYYMMDD("20040402");

       SystemOut.println(jd.getJDEDateAsYYMMDD());

       jd.setRealDate(2004,12,1);
       SystemOut.println(jd.getJDEDateAsYYMMDD());
       jd.setJDEDateAsYYMMDD("20041201");
       SystemOut.println(jd.getJDEDateAsYYMMDD());
        */

    }

    public JDEDate() {
        setNowDate();
    }

    public JDEDate(int value) {
        setJDEDate(value);
    }
    
     public JDEDate(long value) {
       //setNowDate();
       setDate(value);
    }
    
    public JDEDate(JDEDate value) {
      cal.setTimeInMillis(value.getTimeInMillis());
    }

    public int getDaysInMonth() {
        JDEDate jd = new JDEDate();
        jd.setRealDate(getRealYear(), getRealMonth(), 1);
        jd.incMonth(1);
        jd.incDay(-1);
        return jd.getDay();
    }

    public String getJDEDateAsYYMMDD() {
        String mth = "" + this.getRealMonth();
        if (mth.length() == 1) mth = "0" + mth;
        String sd = "" + this.getDay();
        if (sd.length() == 1) sd = "0" + sd;
        return "" + getRealYear() + mth + sd;
    }

    ;

    public String getDateAsYYYYMMDD(String sep) {
        String mth = "" + this.getRealMonth();
        if (mth.length() == 1) mth = "0" + mth;
        String sd = "" + this.getDay();
        if (sd.length() == 1) sd = "0" + sd;
        return "" + getRealYear() + sep + mth + sep + sd;
    }
    
    public String getDateAsDDMMYYYY(String sep) {
        String mth = "" + this.getRealMonth();
        if (mth.length() == 1) mth = "0" + mth;
        String sd = "" + this.getDay();
        if (sd.length() == 1) sd = "0" + sd;
        return "" +sd+sep+mth+sep+getRealYear();
    }

    public String getDateAsYYYYMMDD_andTime(String sep, String timeSep) {
        String mth = "" + this.getRealMonth();
        if (mth.length() == 1) mth = "0" + mth;
        String sd = "" + this.getDay();
        if (sd.length() == 1) sd = "0" + sd;              
        
        return "" + getRealYear() + sep + mth + sep + sd+ " "+getTimeString(timeSep);
    }
    
    public String getDateAsDDMMYYYY_andTime(String sep) {
        String mth = "" + this.getRealMonth();
        if (mth.length() == 1) mth = "0" + mth;
        String sd = "" + this.getDay();
        if (sd.length() == 1) sd = "0" + sd;              
        
        return  "" +sd+sep+mth+sep+getRealYear()+ " "+getTimeString();
    }


    public void setJDEDateAsYYMMDD(String value) {
        if (value.trim().equals("")) {
            setJDEDate(0);
            return;
        }
        String s_year = value.substring(0, 4);
        String s_month = value.substring(4, 6);
        String s_day = value.substring(6, 8);
        try {
            this.setRealDate(new Integer(s_year).intValue(), new Integer(s_month).intValue(), new Integer(s_day).intValue());
        } catch (Exception e) { /*SystemOut.println(e);*/ }
    }

    ;

    public boolean setJDEDateAsDDMMYYYY(String value, String sep) {
        if (value.trim().equals("")) {
            setJDEDate(0);
            return false;
        }

        try {
          String s_day = value.substring(0, 2);
          String s_month = value.substring(2 + sep.length(), 4 + sep.length());
          String s_year = value.substring(4 + 2 * sep.length(), 8 + 2 * sep.length()); 
        
            this.setRealDate(new Integer(s_year).intValue(), new Integer(s_month).intValue(), new Integer(s_day).intValue());
        } catch (Exception e) {
          return false;
          /*SystemOut.println(e);*/ 
        }
        return true;
    };

    public void setJDEDateAsDDMMYYYY_HH_MM_SS(String value, String sep, String sep_time) {
        if (value.trim().equals("")) {
            setJDEDate(0);
            return;
        }
        String s_day = value.substring(0, 2);
        String s_month = value.substring(2 + sep.length(), 4 + sep.length());
        String s_year = value.substring(4 + 2 * sep.length(), 8 + 2 * sep.length());

        try {
            int year = Integer.parseInt(s_year.trim());
            if (year<100) year = 2000+year;
            this.setRealDate(year, Integer.parseInt(s_month.trim()), Integer.parseInt(s_day.trim()));
            cal.set(Calendar.HOUR_OF_DAY, 00);
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.SECOND, 00);
            int pos = value.lastIndexOf(" ");
            if (pos > 0) {
                int pos1 = value.indexOf(sep_time, pos + 1);
                //if (pos1 == -1) return;
                if (pos1==-1) pos1 = value.indexOf("-", pos + 1);
                if (pos1 == -1) return;                
                String hh = value.substring(pos, pos1);
                int hh_i = Integer.parseInt(hh.trim());
                cal.set(Calendar.HOUR_OF_DAY, hh_i);
                
                pos = pos1;
                pos1 = value.indexOf(sep_time, pos + 1);
                if (pos1==-1) pos1 = value.indexOf("-", pos + 1);
                if (pos1 == -1){
                   String mm = value.substring(pos + 1);
                   int mm_i = Integer.parseInt(mm.trim());
                   cal.set(Calendar.MINUTE, mm_i);                
                   return;
                }
                String mm = value.substring(pos + 1, pos1);
                int mm_i = Integer.parseInt(mm.trim());
                cal.set(Calendar.MINUTE, mm_i);
                
                pos = pos1;
                pos1 = value.indexOf(sep_time, pos + 1);
                if (pos1==-1) pos1 = value.indexOf("-", pos + 1);
                if (pos1 == -1) return;
                String ss = value.substring(pos + 1, pos1);
                int ss_i = 0;
                try {                                        
                    ss_i = Integer.parseInt(ss.trim());
                } catch (Exception e) {
                }
                
                
                cal.set(Calendar.SECOND, ss_i);
            }
        } catch (Exception e) { /*SystemOut.println(e);*/ }
    }

    ;

    public void setJDEDateAsYYYYMMDD(String value, String sep) {
        if (value.trim().equals("")) {
            setJDEDate(0);
            return;
        }
        String s_year = value.substring(0, 4);//  4+2*sep.length(),8+2*sep.length());
        String s_month = value.substring(4 + sep.length(), 4 + 2 + sep.length());
        String s_day = value.substring(4 + 2 + sep.length() * 2, 4 + 2 + 2 + sep.length() * 2);

        try {
            this.setRealDate(new Integer(s_year).intValue(), new Integer(s_month).intValue(), new Integer(s_day).intValue());
        } catch (Exception e) { /*SystemOut.println(e);*/ }
    }

    ;

    public void setJDEDateAsMMDDYYYY(String value, String sep) {
        if (value.trim().equals("")) {
            setJDEDate(0);
            return;
        }
        String s_month = value.substring(0, 2);
        String s_day = value.substring(2 + sep.length(), 4 + sep.length());
        String s_year = value.substring(4 + 2 * sep.length(), 8 + 2 * sep.length());

        try {
            this.setRealDate(new Integer(s_year).intValue(), new Integer(s_month).intValue(), new Integer(s_day).intValue());
        } catch (Exception e) { /*SystemOut.println(e);*/ }
    }

    ;

    public void setJDEDateAsYYMMDDWithDelimiter(String value) {
        if (value.trim().equals("")) {
            setJDEDate(0);
            return;
        }
        String s_year = value.substring(0, 4);
        String s_month = value.substring(5, 7);
        String s_day = value.substring(8, 10);
        try {
            this.setRealDate(new Integer(s_year).intValue(), new Integer(s_month).intValue(), new Integer(s_day).intValue());
        } catch (Exception e) { /*SystemOut.println(e);*/ }
    }

    ;

    public int getJDEDate() {
        return value;
    }

    public void setNowDate() {
        GregorianCalendar gc = new GregorianCalendar();
        setRealDate(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH) + 1, gc.get(Calendar.DAY_OF_MONTH));
    }

    public void setExcelDate(int excelDate) {
        cal.set(Calendar.YEAR, 1900);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.add(Calendar.DAY_OF_MONTH, excelDate - 1);
        value = (cal.get(Calendar.YEAR) - 1900) * 1000 + cal.get(Calendar.DAY_OF_YEAR);
    }
    
    public void setExcelDateWithTime(double excelDate) {
        cal.set(Calendar.YEAR, 1900);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.add(Calendar.DAY_OF_MONTH, (int)excelDate - 1);
        value = (cal.get(Calendar.YEAR) - 1900) * 1000 + cal.get(Calendar.DAY_OF_YEAR);             
        
        
        /*BigDecimal val = new BigDecimal(excelDate);
        val = val.setScale(11,BigDecimal.ROUND_HALF_UP);
        excelDate = val.doubleValue();*/ 
        
        int full_day = 24*60*60*1000; // in milisecunds
        double res = (excelDate-(int)excelDate);
        //BigDecimal minus = new BigDecimal(-excelDate.intValue());
        //double res = excelDate.add(minus).doubleValue();
        
        /*int second_part = (int)(full_day*res);
        int hours = second_part/(60*60*1000);
        int minutes = (second_part-hours*60*60*1000)/60;
        int secunds = second_part - hours*60*60 - minutes*60;*/
        //int milisecunds = second_part - hours*60*60 - minutes*60;
        
        int ms = (int)(full_day*res);
        int hours = ms/(60*60*1000);
        int minutes = (ms-hours*60*60*1000)/(60*1000);
        int secunds = (ms - hours*60*60*1000 - minutes*60*1000)/1000;
        int mms = (ms - hours*60*60*1000 - minutes*60*1000 - secunds*1000);
        if (mms==999){
          secunds++;
          if (secunds==60) {
            secunds=0;
            minutes++;
            if (minutes==60) {
              minutes = 0;
              hours++;              
            }
          }
        }        
        
        cal.set(Calendar.HOUR_OF_DAY, hours );
        cal.set(Calendar.MINUTE, minutes );
        cal.set(Calendar.SECOND, secunds );
    }
    
     public void setTime(JDEDate jd) {
        if (jd!=null){
          cal.set(Calendar.HOUR_OF_DAY, jd.get(Calendar.HOUR_OF_DAY) );
          cal.set(Calendar.MINUTE, jd.get(Calendar.MINUTE)  );
          cal.set(Calendar.SECOND, jd.get(Calendar.SECOND)  );
        }else{
          setNullTime();
        }  
    }
     
     public void setTime(DBTimeString jd) {
        if (jd!=null && jd.time!=null){
          //cal.setTimeInMillis(jd.time.getTime());
          cal.set(Calendar.HOUR_OF_DAY, jd.time.getHours() );
          cal.set(Calendar.MINUTE, jd.time.getMinutes()  );
          cal.set(Calendar.SECOND, jd.time.getSeconds() );
        }else{
          setNullTime();
        }  
    }
    

    public void setJDEDate(int value) {
        this.value = value;
        cal.set(Calendar.YEAR, getRealYear());
        cal.set(Calendar.DAY_OF_YEAR, getDayOfYear());
    }
    
    public void setPrd(int prd) {
        int year = prd/12;
        int month = prd-year*12;
        if (month==0){
          month = 12;
          year--;
        }        
        month--;
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        value = (cal.get(Calendar.YEAR) - 1900) * 1000 + cal.get(Calendar.DAY_OF_YEAR);        
    }

    public void setDate(long time) {
        cal.setTimeInMillis(time);
        value = (cal.get(Calendar.YEAR) - 1900) * 1000 + cal.get(Calendar.DAY_OF_YEAR);
    }

    public void setRealDate(int year, int month, int day) {
        try {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, day);
            value = (year - 1900) * 1000 + cal.get(Calendar.DAY_OF_YEAR);
        } catch (Exception e) {
            System.out.println("year:" + year + " month:" + month + " day:" + day);
            KKV.Utils.Tools.traceError(e);
        }
    }

    public void setNullTime() {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    public int getDays(JDEDate date) {
        //return (int)((float)cal.getTime().getTime() - (float)date.cal.getTime().getTime())/(1000*60*60*24);
        cal.set(Calendar.HOUR, 1);
        cal.set(Calendar.MINUTE, 1);
        cal.set(Calendar.SECOND, 1);

        date.cal.set(Calendar.HOUR, 1);
        date.cal.set(Calendar.MINUTE, 1);
        date.cal.set(Calendar.SECOND, 1);
        int days = Math.round((this.cal.getTime().getTime() - date.cal.getTime().getTime()) / (1000 * 60 * 60 * 24));
        return days;
    }

    public void setDaysOfYear(int CY, int days) {
        //return (int)((float)cal.getTime().getTime() - (float)date.cal.getTime().getTime())/(1000*60*60*24);
        cal.set(Calendar.YEAR, CY);
        cal.set(Calendar.DAY_OF_YEAR, days);
        value = (cal.get(Calendar.YEAR) - 1900) * 1000 + cal.get(Calendar.DAY_OF_YEAR);
    }


    public int getJDETime() {
        return cal.get(Calendar.HOUR_OF_DAY) * 10000 + cal.get(Calendar.MINUTE) * 100 + cal.get(Calendar.SECOND);
    }

    public String getJDETimeAsString() {
        String time = "" + getJDETime();
        if (time.length() == 5) {
            time = "0" + time;
        }
        return time;
    }


    public JDEDate incDay(int day) {
        cal.add(Calendar.DAY_OF_MONTH, day);
        value = (cal.get(Calendar.YEAR) - 1900) * 1000 + cal.get(Calendar.DAY_OF_YEAR);
        return this;
    }

    ;

    public void incYear(int year) {
        cal.add(Calendar.YEAR, year);
        value = (cal.get(Calendar.YEAR) - 1900) * 1000 + cal.get(Calendar.DAY_OF_YEAR);
    }

    ;

    public JDEDate incMonth(int month) {
        cal.add(Calendar.MONTH, month);
        value = (cal.get(Calendar.YEAR) - 1900) * 1000 + cal.get(Calendar.DAY_OF_YEAR);
        return this;
    }

    public String YMDS() {
        return YMDS(".");
    }

    public String YMDS(String aDelimiter) {
        if (getJDEDate() == 0) return "-";
        String mth = "" + this.getRealMonth();
        if (mth.length() == 1) mth = "0" + mth;
        String sd = "" + this.getDay();
        if (sd.length() == 1) sd = "0" + sd;
        return "" + this.getRealYear() + aDelimiter + mth + aDelimiter + sd;
    }

    public void setFiscalDate(int year, int month, int day) {
        setRealDate(year, month, 1);

        add(Calendar.MONTH, 3);

        cal.set(Calendar.DAY_OF_MONTH, day);
    }

    public BigDecimal toBigDecimal() {
        return new BigDecimal((double) value);
    }

    public int getDayOfYear() {
        return value % 1000;
    }

    public int getRealYear() {
        return 1900 + value / 1000;
    }

    public int getRealMonth() {
        return cal.get(Calendar.MONTH) + 1;
    }
    
    public int getRealPrd() {
        return getRealYear()*12+getRealMonth();
    }

    public String getRealMonth2D() {
        if (getJDEDate() == 0) return "-";
        String mth = "" + this.getRealMonth();
        if (mth.length() == 1) mth = "0" + mth;
        return mth;
    }
    
    public String getPeriodAsMMYYYY(String sep) {
        return getRealMonth2D()+sep+getRealYear();
    }

    public String getRealYear2() {
        String fy = "" + getRealYear();
        if (fy.length() == 4) fy = fy.substring(2);
        return fy;
    }

    public String getRealYear2(int incr) {
        String fy = "" + (getRealYear() + incr);
        if (fy.length() == 4) fy = fy.substring(2);
        return fy;
    }

    public java.util.Date getSQLDate() {
        //java.sql.Date dt = new java.sql.Date(cal.getTime());
        //cal.getTime().

        //return cal.getTime().;
        //cal.
        //return sqlDate;
        return cal.getTime();
    }
    
    public long getTimeInMillis() {
        return cal.getTimeInMillis();
    }
    
    public java.sql.Date getRealSQLDate() {
        return new java.sql.Date(cal.getTime().getTime());
    }
    
    public java.sql.Timestamp getTimestamp() {
        //java.sql.Date dt = new java.sql.Date(cal.getTime());
        //cal.getTime().

        //return cal.getTime().;
        //cal.
        //return sqlDate;
        return new java.sql.Timestamp(cal.getTime().getTime());
    }

    public int getDay() {
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public String getDay2D() {
        if (getJDEDate() == 0) return "-";
        String day = "" + cal.get(Calendar.DAY_OF_MONTH);
        if (day.length() == 1) day = "0" + day;
        return day;
    }

    public int getDayOfWeek() {
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public int getFiscalYear() {
        JDEDate jd = new JDEDate(value);
        jd.add(Calendar.MONTH, -3);

        return jd.getRealYear();
    }

    public String getFiscalYear2() {
        JDEDate jd = new JDEDate(value);
        jd.add(Calendar.MONTH, -3);
        String fy = "" + jd.getRealYear();
        if (fy.length() == 4) fy = fy.substring(2);
        return fy;
    }

    public String getFiscalYear2(int incr) {
        JDEDate jd = new JDEDate(value);
        jd.add(Calendar.MONTH, -3);
        String fy = "" + (jd.getRealYear() + incr);
        if (fy.length() == 4) fy = fy.substring(2);
        return fy;
    }


    // Method TJDEDate.GetJDEDateStr
    public String GetJDEDateStr() {
        return "" + value;
    }

    ;


    public int getFiscalMonth() {
        JDEDate jd = new JDEDate(value);
        jd.add(Calendar.MONTH, -3);

        return jd.getRealMonth();
    }

    public void add(int field, int inc) {
        cal.add(field, inc);
        value = (cal.get(Calendar.YEAR) - 1900) * 1000 +
                cal.get(Calendar.DAY_OF_YEAR);
    }

    public String toString() {
      return getDateAsYYYYMMDD("-")+" "+getTimeString();
        /*return "[" +
                "R=" +
                getRealYear() + "-" +
                getRealMonth() + "-" +
                getDay() + ", " +
                "F=" +
                getFiscalYear() + "-" +
                getFiscalMonth() + "-" +
                getDay() + ", " +
                "J=" + getJDEDate() +
                "]";*/
    }

    public String getMonthInSTR() {
        int m = cal.get(Calendar.MONTH);
        String res = "";
        switch (m) {
            case Calendar.JANUARY:
                res = "Jan";
                break;
            case Calendar.FEBRUARY:
                res = "Feb";
                break;
            case Calendar.MARCH:
                res = "Mar";
                break;
            case Calendar.APRIL:
                res = "Apr";
                break;
            case Calendar.MAY:
                res = "May";
                break;
            case Calendar.JUNE:
                res = "Jun";
                break;
            case Calendar.JULY:
                res = "Jul";
                break;
            case Calendar.AUGUST:
                res = "Aug";
                break;
            case Calendar.SEPTEMBER:
                res = "Sep";
                break;
            case Calendar.OCTOBER:
                res = "Oct";
                break;
            case Calendar.NOVEMBER:
                res = "Nov";
                break;
            case Calendar.DECEMBER:
                res = "Dec";
                break;
        }
        ;
        return res;
    }

    public String getFullMonthInSTR() {
        int m = cal.get(Calendar.MONTH);
        String res = "";
        switch (m) {
            case Calendar.JANUARY:
                res = "January";
                break;
            case Calendar.FEBRUARY:
                res = "February";
                break;
            case Calendar.MARCH:
                res = "March";
                break;
            case Calendar.APRIL:
                res = "April";
                break;
            case Calendar.MAY:
                res = "May";
                break;
            case Calendar.JUNE:
                res = "June";
                break;
            case Calendar.JULY:
                res = "July";
                break;
            case Calendar.AUGUST:
                res = "August";
                break;
            case Calendar.SEPTEMBER:
                res = "September";
                break;
            case Calendar.OCTOBER:
                res = "October";
                break;
            case Calendar.NOVEMBER:
                res = "November";
                break;
            case Calendar.DECEMBER:
                res = "December";
                break;
        }
        ;
        return res;
    }
    
    public String getFullMonthWithYearInSTR() {
        int m = cal.get(Calendar.MONTH);
        String res = "";
        switch (m) {
            case Calendar.JANUARY:
                res = "January";
                break;
            case Calendar.FEBRUARY:
                res = "February";
                break;
            case Calendar.MARCH:
                res = "March";
                break;
            case Calendar.APRIL:
                res = "April";
                break;
            case Calendar.MAY:
                res = "May";
                break;
            case Calendar.JUNE:
                res = "June";
                break;
            case Calendar.JULY:
                res = "July";
                break;
            case Calendar.AUGUST:
                res = "August";
                break;
            case Calendar.SEPTEMBER:
                res = "September";
                break;
            case Calendar.OCTOBER:
                res = "October";
                break;
            case Calendar.NOVEMBER:
                res = "November";
                break;
            case Calendar.DECEMBER:
                res = "December";
                break;
        }
        res += ", "+getRealYear();
        return res;
    }

    /**
     * January = 1 ...
     */
    public static String getFullMonthInSTR(int month) { //
        int m = month - 1;
        String res = "";
        switch (m) {
            case Calendar.JANUARY:
                res = "January";
                break;
            case Calendar.FEBRUARY:
                res = "February";
                break;
            case Calendar.MARCH:
                res = "March";
                break;
            case Calendar.APRIL:
                res = "April";
                break;
            case Calendar.MAY:
                res = "May";
                break;
            case Calendar.JUNE:
                res = "June";
                break;
            case Calendar.JULY:
                res = "July";
                break;
            case Calendar.AUGUST:
                res = "August";
                break;
            case Calendar.SEPTEMBER:
                res = "September";
                break;
            case Calendar.OCTOBER:
                res = "October";
                break;
            case Calendar.NOVEMBER:
                res = "November";
                break;
            case Calendar.DECEMBER:
                res = "December";
                break;
        }
        ;
        return res;
    }

    public static int getMonthForShortMonthInSTR(String st) {
        int res = -1;
        if ("JAN".equalsIgnoreCase(st)) return Calendar.JANUARY;
        if ("FEB".equalsIgnoreCase(st)) return Calendar.FEBRUARY;
        if ("MAR".equalsIgnoreCase(st)) return Calendar.MARCH;
        if ("APR".equalsIgnoreCase(st)) return Calendar.APRIL;
        if ("MAY".equalsIgnoreCase(st)) return Calendar.MAY;
        if ("JUN".equalsIgnoreCase(st)) return Calendar.JUNE;
        if ("JUL".equalsIgnoreCase(st)) return Calendar.JULY;
        if ("AUG".equalsIgnoreCase(st)) return Calendar.AUGUST;
        if ("SEP".equalsIgnoreCase(st)) return Calendar.SEPTEMBER;
        if ("OCT".equalsIgnoreCase(st)) return Calendar.OCTOBER;
        if ("NOV".equalsIgnoreCase(st)) return Calendar.NOVEMBER;
        if ("DEC".equalsIgnoreCase(st)) return Calendar.DECEMBER;
        if (res==-1) res = getMonthForShortMonthInRusSTR(st);
        if (res==-1) res = getMonthForFullMonthInSTR(st);

        return res;
    }
    
    /*** JANUARY is first :( */
    public static int getMonthForFullMonthInSTR(String st) {
        int res = -1;
        if ("January".equalsIgnoreCase(st)) return Calendar.JANUARY;
        if ("February".equalsIgnoreCase(st)) return Calendar.FEBRUARY;
        if ("March".equalsIgnoreCase(st)) return Calendar.MARCH;
        if ("April".equalsIgnoreCase(st)) return Calendar.APRIL;
        if ("May".equalsIgnoreCase(st)) return Calendar.MAY;
        if ("June".equalsIgnoreCase(st)) return Calendar.JUNE;
        if ("July".equalsIgnoreCase(st)) return Calendar.JULY;
        if ("August".equalsIgnoreCase(st)) return Calendar.AUGUST;
        if ("September".equalsIgnoreCase(st)) return Calendar.SEPTEMBER;
        if ("October".equalsIgnoreCase(st)) return Calendar.OCTOBER;
        if ("November".equalsIgnoreCase(st)) return Calendar.NOVEMBER;
        if ("December".equalsIgnoreCase(st)) return Calendar.DECEMBER;
        if (res==-1) res = getMonthForShortMonthInRusSTR(st);
        
        return res;
    }        

    public static int getMonthForShortMonthInRusSTR(String st) {
        int res = -1;
        if ("Января".equalsIgnoreCase(st)) return Calendar.JANUARY;
        if ("Февраля".equalsIgnoreCase(st)) return Calendar.FEBRUARY;
        if ("Марта".equalsIgnoreCase(st)) return Calendar.MARCH;
        if ("Апреля".equalsIgnoreCase(st)) return Calendar.APRIL;
        if ("Мая".equalsIgnoreCase(st)) return Calendar.MAY;
        if ("�?юня".equalsIgnoreCase(st)) return Calendar.JUNE;
        if ("�?юля".equalsIgnoreCase(st)) return Calendar.JULY;
        if ("Августа".equalsIgnoreCase(st)) return Calendar.AUGUST;
        if ("Сентября".equalsIgnoreCase(st)) return Calendar.SEPTEMBER;
        if ("Октября".equalsIgnoreCase(st)) return Calendar.OCTOBER;
        if ("Ноября".equalsIgnoreCase(st)) return Calendar.NOVEMBER;
        if ("Декабря".equalsIgnoreCase(st)) return Calendar.DECEMBER;

        return res;
    }

    // if Saturday or Sunday return true
    public boolean isSatOrSunday() {
        int m = cal.get(Calendar.DAY_OF_WEEK);
        if (m == Calendar.SUNDAY || m == Calendar.SATURDAY) return true;
        return false;
    }

    public String getDayOfWeekINSTR() {
        int m = cal.get(Calendar.DAY_OF_WEEK);
        String res = ""; //  SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
        switch (m) {
            case Calendar.SUNDAY:
                res = "Sun";
                break;
            case Calendar.MONDAY:
                res = "Mon";
                break;
            case Calendar.TUESDAY:
                res = "Tue";
                break;
            case Calendar.WEDNESDAY:
                res = "Wed";
                break;
            case Calendar.THURSDAY:
                res = "Thu";
                break;
            case Calendar.FRIDAY:
                res = "Fri";
                break;
            case Calendar.SATURDAY:
                res = "Sat";
                break;
        }
        ;
        return res;
    }

    public String getShortFormat(String sep) {
        if (getJDEDate() == 0) return "-";
        String mth = "" + this.getRealMonth();
        if (mth.length() == 1) mth = "0" + mth;
        String sd = "" + this.getDay();
        if (sd.length() == 1) sd = "0" + sd;
        String year = "" + this.getRealYear();
        if (year.length() == 4) year = year.substring(2);
        return "" + year + sep + mth + sep + sd;
    }
    
    public String getTimeString() {
        return  Tools.padl(""+this.cal.get(Calendar.HOUR_OF_DAY),2,'0')+":"+
                Tools.padl(""+this.cal.get(Calendar.MINUTE),2,'0')+":"+
                Tools.padl(""+this.cal.get(Calendar.SECOND),2,'0');
    }  
    
    public String getTimeString(String timeSep) {
        return  Tools.padl(""+this.cal.get(Calendar.HOUR_OF_DAY),2,'0')+timeSep+
                Tools.padl(""+this.cal.get(Calendar.MINUTE),2,'0')+timeSep+
                Tools.padl(""+this.cal.get(Calendar.SECOND),2,'0');
    }  

    public String getDDMMYYYY(String sep) {
        if (getJDEDate() == 0) return "-";
        String mth = "" + this.getRealMonth();
        if (mth.length() == 1) mth = "0" + mth;
        String sd = "" + this.getDay();
        if (sd.length() == 1) sd = "0" + sd;
        String year = "" + this.getRealYear();
        //if (year.length()==4) year = year.substring(2);
        return "" + sd + sep + mth + sep + year;
    }

    public String getMMDDYYYY(String sep) {
        if (getJDEDate() == 0) return "-";
        String mth = "" + this.getRealMonth();
        if (mth.length() == 1) mth = "0" + mth;
        String sd = "" + this.getDay();
        if (sd.length() == 1) sd = "0" + sd;
        String year = "" + this.getRealYear();
        //if (year.length()==4) year = year.substring(2);
        return "" + mth + sep + sd + sep + year;
    }

    public String getRealMonthYear(String sep) {
        if (getJDEDate() == 0) return "-";
        String mth = "" + this.getRealMonth();
        if (mth.length() == 1) mth = "0" + mth;
        String year = "" + this.getRealYear();
        return "" + mth + sep + year;
    }

    public int get(int field) {
        return cal.get(field);
    }

    int CalendarWeekYear;

    public int getCalendarWeekYear() {
        return CalendarWeekYear;
    }

    ;

    public int getWeek() {
        /*JDEDate jd = new JDEDate();
        jd.setRealDate(cal.get(Calendar.YEAR), cal.get(Calendar.JANUARY), 1);
        boolean firstWeekIsFirstWeek = false;
        if ( jd.getDayOfWeek()==Calendar.FRIDAY ||
             jd.getDayOfWeek()==Calendar.SUNDAY ||
             jd.getDayOfWeek()==Calendar.SATURDAY )
        {
          firstWeekIsFirstWeek = true;
        }
        //cal.setFirstDayOfWeek(Calendar.);

        int week = 0;
        int wo = cal.get(Calendar.WEEK_OF_YEAR);
        if (cal.get(Calendar.MONTH)==Calendar.DECEMBER && wo==1){
          Calendar g = new GregorianCalendar();
          g.setTimeInMillis(cal.getTimeInMillis());
          g.add(Calendar.DAY_OF_YEAR, -7);
          week = g.get(Calendar.WEEK_OF_YEAR);
        }
        week = week+wo;
        if (firstWeekIsFirstWeek){
          week--;
        }*/
        JDEDate jd = new JDEDate();
        jd.setRealDate(cal.get(Calendar.YEAR), 1, 1);
        int day = jd.getDayOfWeek();
        day--;
        if (day == 0) day = 7;

        CalendarWeekYear = getRealYear();

        int week = (getDayOfYear() + day) / 7 + 1;
        boolean firstWeekIsFirstWeek = false;
        int dow = jd.getDayOfWeek();
        if (jd.getDayOfWeek() == Calendar.FRIDAY ||
                jd.getDayOfWeek() == Calendar.SUNDAY ||
                jd.getDayOfWeek() == Calendar.SATURDAY) {
            firstWeekIsFirstWeek = true;
        }
        day = getDayOfWeek();
        day--;
        if (day == 0) day = 7;
        if ((getDay() + (7 - day)) >= 31 && getRealMonth() == 12) {
            JDEDate jd_last = new JDEDate();
            jd_last.setRealDate(getRealYear(), 12, 31);
            if (jd_last.getDayOfWeek() == Calendar.MONDAY ||
                    jd_last.getDayOfWeek() == Calendar.TUESDAY ||
                    jd_last.getDayOfWeek() == Calendar.WEDNESDAY) {
                CalendarWeekYear++;
                week = 1;
            }
        }//else
        if (firstWeekIsFirstWeek) week = week - 1;
        if (week == 0) {
            CalendarWeekYear--;
            JDEDate jd1 = new JDEDate();
            jd1.setRealDate(cal.get(Calendar.YEAR), 12, 31);
            if (jd1.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                jd1.incDay(7);
                return jd1.getWeek() - 1;
            }
            return jd1.getWeek();
        }
        return week;
    }


}