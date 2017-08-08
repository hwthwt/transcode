import java.io.*;
import java.lang.*;
import java.text.SimpleDateFormat;
import java.util.*;
public class Log {
    private final static String LOG_FILE_PATH ="I:\\工作相关\\TESTMOVIE";

    /**
     *  读取指定文件的内容，组成字符串并返回
     * @param fileName String 文件名
     * @return String 文件内容
     */
    public static String readFile(String fileName) {
        String dataStr = "";
        FileInputStream fis = null;
        try {
            FileReader file = new FileReader(fileName);//建立FileReader对象，并实例化为fr
            BufferedReader br=new BufferedReader(file);//建立BufferedReader对象，并实例化为br
            String Line=br.readLine();//从文件读取一行字符串
            dataStr=Line;
            br.close();//关闭BufferedReader对象
        } catch(Exception e) {

        } finally {
            try {
                if(fis!=null)
                    fis.close();
            } catch(Exception e) {}
        }
        return dataStr;
    }

    /**
     *  按指定格式，将字符串写入指定文件作为日志
     * @param str String 字符串
     */
    public static void writeFile(String str) {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(date);
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
            String dateString2 = formatter2.format(date);
            String filePath =  LOG_FILE_PATH + "\\" + dateString2 + "_log.txt";
            String line = dateString + " : " + str + "\n";
            FileIO.write(filePath,line);
            System.out.println(str);
    }

}
