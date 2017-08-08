import java.io.File;
import java.util.Arrays;
/**
 * 这个类用来获取需要进行转码的数据，一次获取一条。再次获取需要重新加载。
 */
public class BeforeTranscodeDir {

    /**
     *  通过给出的文件夹路径，找出一条限定格式的视频文件路径，并返回。
     * @param dirname String 文件夹路径
     * @return String 文件路径，找不到时，返回""
     */
    public static String getSingleFileName(String dirname){
        File beforeDir = new File(dirname);
        if (beforeDir.isDirectory()) {
            Log.writeFile("目录 " + dirname);
            String s[] = beforeDir.list();
            for (int i=0; i < s.length; i++) {
                File f = new File(dirname + "/" + s[i]);
                if (!f.isDirectory()) {
                    //还需要判断文件后缀名是否是视频格式。
                    if(checkSuffixInArray(getFileSuffix(s[i]))){
                        Log.writeFile("找到视频文件:" + s[i] );
                        return s[i];
                    }
                }
            }
        } else {
            Log.writeFile(dirname + " 不是一个目录");
            return "";
        }
        Log.writeFile("没有找到视频文件！");
        return  "";
    }

    /**
     *  通过文件路径获取小写的文件后缀名
     * @param filename String 文件路径
     * @return String 文件后缀名
     */
    private static String getFileSuffix(String filename){
        File fileName = new File(filename);
        String suffix = fileName.getName().substring(fileName.getName().lastIndexOf(".")+1);
        return suffix.toLowerCase();
    }

    /**
     * 检测后缀名是否在数组中。
     * @param suffix String 后缀名
     * @return 在的话，返回true；否则，返回false;
     */
    private static boolean checkSuffixInArray(String suffix){
        String[] suffixArray = {"mp4","avi","rmvb","rm","mkv","3gp","mpeg"};
        return Arrays.asList(suffixArray).contains(suffix);
    }
}
