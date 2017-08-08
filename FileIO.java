import java.io.FileWriter;
import java.io.IOException;

public class FileIO {
    /**
     *  将字符串写入文件中
     * @param filePath 文件路径
     * @param str 字符串内容
     */
    public static void write(String filePath, String str){
        FileWriter fw = null;
        try {
            fw = new FileWriter(filePath, true);
            fw.write(str);
            fw.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            if (fw != null)
                try {
                    fw.close();
                } catch (IOException e) {
                    throw new RuntimeException("关闭失败！");
                }
        }
    }
}
