import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.regex.*;

/**
 * 本类用来获取视频参数
 */
public class GetVideoInfo {
    public String vcodec = "";
    public String acodec = "";
    public int ab = 0;
    public int duration = 0;
    public int vb = 0;
    public float fps = 0.0f;
    public String resolution = "";
    public int width = 0;
    public int height = 0;
    public int asamplerate = 0;
    public float starttime = 0;
    public int b = 0;
    /**
     * 通过ffmpeg获取视频文件的编码参数等数据，并初始化对象的视频参数
     * @param video_path String 文件路径
     * @param ffmpeg_path String ffmpeg程序所在位置
     */
    public void getVideoInfo(String video_path, String ffmpeg_path) {
        List<String> commands = new java.util.ArrayList<String>();
        commands.add(ffmpeg_path);
        commands.add("-i");
        commands.add(video_path);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            System.out.println(commands);
            builder.command(commands);
            final Process p = builder.start();
            //从输入流中读取视频信息
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line = "";
            while ((line = br.readLine()) != null) {
                //针对每一行的数据进行分别匹配，防止数据错位
                checkParmars(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  将时间格式"00:00:10.68"，转换成秒
     * @param timelen 时间格式字符串
     * @return int 秒
     */
    private int getTimelen(String timelen) {
        int min = 0;
        String strs[] = timelen.split(":");
        if (strs[0].compareTo("0") > 0) {
            min += Integer.valueOf(strs[0]) * 60 * 60;//秒
        }
        if (strs[1].compareTo("0") > 0) {
            min += Integer.valueOf(strs[1]) * 60;
        }
        if (strs[2].compareTo("0") > 0) {
            min += Math.round(Float.valueOf(strs[2]));
        }
        return min;
    }

    /**
     * 匹配字符串中的参数，如果有响应的值，则取出来设置为对象的视频参数
     * @param line String 字符串
     */
    private void checkParmars(String line){
        //从视频信息中解析时长
        String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
        Pattern patternDuration = Pattern.compile(regexDuration);
        Matcher m = patternDuration.matcher(line);
        if (m.find()) {
            int time = getTimelen(m.group(1));
            Log.writeFile("视频时长：" + time + ", 开始时间：" + m.group(2) + ",比特率：" + m.group(3) + "kb/s");
            this.duration = time;
            this.starttime = Float.parseFloat(m.group(2));
            this.b = Integer.parseInt(m.group(3));
        }
        //从视频信息中解析视频流数据
        String regexVideo ="Video: (.*?), (.*?), (.*?)[,\\s]";
        Pattern patternVideo = Pattern.compile(regexVideo);
        Matcher v = patternVideo.matcher(line);
        if (v.find()) {
            //再次匹配line中是否有码率，帧率
            String bv = checkSinglePatter(", (\\d*) kb/s",line);
            String reselt = "编码格式：" + v.group(1) + ", 视频格式：" + v.group(2) + ",分辨率：" + v.group(3) ;
            this.vcodec = v.group(1);
            this.resolution = v.group(3);
            //拆分分辨率为宽和高
            String temp[] = resolution.split("x");
            this.width = Integer.parseInt(temp[0]);
            this.height = Integer.parseInt(temp[1]);

            if(bv != "0"){
                reselt += ",视频码率:" + bv +"kb/s";
                this.vb = Integer.parseInt(bv);
            }
            String f = checkSinglePatter("kb/s, (.*?) fps",line);
            if( f != "0" ){
                reselt += ",帧率:" + f +" fps";
                this.fps = Float.parseFloat(f);
            }
            Log.writeFile(reselt);
        }
        //从视频信息中解析音频流数据
        String regexAudio ="Audio: (.*?), (\\d*) Hz,(.*?), (\\d*) kb/s";
        Pattern patternAudio = Pattern.compile(regexAudio);
        Matcher a = patternAudio.matcher(line);
        if (a.find()) {
            String av = checkSinglePatter(", (\\d*) kb/s",line);
            String reselt = "音频编码：" + a.group(1) + ", 音频采样频率：" + a.group(2)  ;
            this.acodec = a.group(1);
            this.asamplerate = Integer.parseInt(a.group(2));
            if(av != "0"){
                reselt += ",音频码率:" + av +"kb/s";
                this.ab = Integer.parseInt(av);
            }
            Log.writeFile(reselt);
        }
    }

    /**
     *  从字符串中使用正则表达式匹配出单一变量的值，并返回
     * @param patter String 正则表达式
     * @param line String 字符串
     * @return String 匹配出的变量值，没有的话，为“0”
     */
    private String checkSinglePatter(String patter, String line){
        Pattern patternbv = Pattern.compile(patter);
        Matcher bv = patternbv.matcher(line);
        if(bv.find()) {
            return bv.group(1).toString();
        } else {
            return "0";
        }
    }
}