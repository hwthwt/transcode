import java.io.*;
import java.lang.*;

/**
 * 该类用于转码
 */
public class MediocreExecJavac {

    //初始化参数
    //ffmpeg程序所在位置
    private static final String FFMPEG_PATH = "I:\\工作相关\\ffmpeg-20170724-03a9e6f-win64-static\\bin\\ffmpeg.exe";
    //线程数
    private static final int THREADS = 2;
    //转码前文件所在的的文件夹
    private static final String ENCODING_DIR_PATH = "I:\\工作相关\\TESTMOVIE\\encoding\\";
    //转码中文件所在的文件夹
    private static final String SUCCESS_DIR_PATH = "I:\\工作相关\\TESTMOVIE\\success\\";
    //转码成功文件所在的文件夹
    private static final String FAIL_DIR_PATH = "I:\\工作相关\\TESTMOVIE\\fail\\";
    //转码失败文件所在的文件夹
    private static final String MP4BOX_PATH = "I:\\工作相关\\TESTMOVIE\\fail\\";

    /**
     * 本方法用于提供转码，自动转移文件到转码中文件夹，转码完成后，转移到指定文件夹。
     * @param infilePath 转码的文件路径
     * @return 转码成功返回true;失败，返回false
     */
    public static boolean transcode( String infilePath){
        //根据转码源文件，生成转码中文件夹，生成转码中文件的文件名
        //获取文件名，组成转码后的文件夹路径
        String fileName = getFileName(infilePath);
        String encodingDirPath = ENCODING_DIR_PATH + fileName;
        File encodingDir = new File(encodingDirPath);
        //如果路径不存在，则进行创建
        if(!DirIO.checkDir(encodingDirPath)){
            Log.writeFile("创建转码中文件夹失败：" + encodingDirPath);
            return false;
        }
        //组成转码中文件夹的转码前文件的路径
        String encodingFilePath = encodingDirPath + "\\" + fileName + ".mp4";
        //将源文件放置于转码文件夹中
        File infile = new File(infilePath);
        File encodingFile = new File(encodingFilePath);
        if( !infile.renameTo(encodingFile) ){
            Log.writeFile("文件转移到转码中文件夹失败！");
            return false;
        }
        if(!encodingFile.isFile()){
            Log.writeFile("转码中源文件不存在：" + encodingFilePath);
            return false;
        }
        //获取视频参数
        GetVideoInfo videoInfo = new GetVideoInfo();
        videoInfo.getVideoInfo(encodingFilePath,FFMPEG_PATH);
        System.out.println(videoInfo.vb);
        //获取1080P的转码参数
        EncodeParms encodeParms = new EncodeParms();
        for(int i = 0; i <= 3 ; i++ ){
            encodeParms.setEncodeParms(i);
            //生成转码后的文件路径
            String encodingMp4FilePath = encodingDirPath + "\\" + fileName + "_" + encodeParms.name + ".mp4";
            //对比转码参数，组成转码命令1
            int bv = ( encodeParms.vb > videoInfo.vb && videoInfo.vb != 0 ) ? videoInfo.vb : encodeParms.vb ;
            int ab = ( encodeParms.ab > videoInfo.ab && videoInfo.ab != 0 ) ? videoInfo.ab : encodeParms.ab ;
            int asamplerate = ( encodeParms.asamplerate > videoInfo.asamplerate && videoInfo.asamplerate != 0 ) ? videoInfo.asamplerate : encodeParms.asamplerate ;
            String fps = (videoInfo.fps > 0  )? " -r " + String.valueOf(videoInfo.fps) + " " : "" ;
            //分辨率检测
            String solution = "";
            float encodeSolution = (float)encodeParms.width/encodeParms.height;
            float videoSolution = (float)videoInfo.width/videoInfo.height;
            if(videoInfo.width >= encodeParms.width && videoInfo.height >= encodeParms.height){
                if(encodeSolution >= videoSolution){
                    solution = (int)(videoSolution*encodeParms.height) + "x" + encodeParms.height;
                } else {
                    solution = encodeParms.width + "x" + (int)(encodeParms.width/videoSolution);
                }
            } else if(videoInfo.width >= encodeParms.width && videoInfo.height <= encodeParms.height){
                solution = encodeParms.width + "x" + (int)(encodeParms.width/videoSolution);
            } else if(videoInfo.width <= encodeParms.width && videoInfo.height >= encodeParms.height){
                solution = (int)(videoSolution*encodeParms.height) + "x" + encodeParms.height;
            } else if(videoInfo.width <= encodeParms.width && videoInfo.height <= encodeParms.height){
                if(encodeSolution >= videoSolution){
                    solution = (int)(videoSolution*encodeParms.height) + "x" + encodeParms.height;
                } else {
                    solution = encodeParms.width + "x" + (int)(encodeParms.width/videoSolution);
                }
            }
            //组成第三个转码命令
            String commandLine1 = FFMPEG_PATH + " -y -i \"" + encodingFilePath + "\" -f mp4 -vcodec libx264 -b:v " + bv + "k -s " + solution + " -strict -2 -acodec aac -ab " + ab + "k -ar " + asamplerate + fps + " -map_chapters -1 -vprofile baseline -pix_fmt yuv420p -coder 0 -movflags +faststart -threads " + THREADS + " \"" + encodingMp4FilePath + "\"";
            String commandLine2 = FFMPEG_PATH + " -y -i \"" + encodingMp4FilePath + "\" -acodec copy -vcodec copy -rc_eq 'blurCplx^(1-qComp)' -qcomp 0.6 -qmin 10 -qmax 51 -qdiff 4 -level 30 -g 30 -async 2 -partitions +parti4x4+partp8x8+partb8x8 -subq 5 -trellis 1 -refs 1 -vbsf h264_mp4toannexb -flags -global_header -map 0 -f segment -segment_time 10 -segment_list \"" + encodingDirPath + "\\" + fileName + "_" + encodeParms.name + ".m3u8\" -segment_list_flags +live -segment_format mpegts \"" + encodingDirPath + "\\" + fileName + "_" + encodeParms.name + "_%05d.ts\"";
            String commandLine3 = MP4BOX_PATH + " " + encodingMp4FilePath + " -hint";
            //if(transfer(commandLine1) && transfer(commandLine2) && transfer(commandLine3)){
            if(transfer(commandLine1) && transfer(commandLine2) ){
                continue;
            } else {
                Log.writeFile("转码失败！开始文件夹转移。");
                File failDir = new File(FAIL_DIR_PATH + fileName);
                if( !failDir.isDirectory() ){
                    if(encodingDir.renameTo(failDir)){
                        Log.writeFile("文件夹转移成功");
                    } else {
                        Log.writeFile("文件夹转移失败");
                    }
                }
                return false;
            }
        }
        Log.writeFile("转码成功！开始文件夹转移。");
        File successDir = new File(SUCCESS_DIR_PATH + fileName);
        if( !successDir.isDirectory() ){
            if(encodingDir.renameTo(successDir)){
                System.out.println("文件夹转移成功");
            } else {
                System.out.println("文件夹转移失败");
            }
        }
        return true;
    }

    /**
     * 返回指定路径的文件名，不包含后缀名
     * @param infile Sting 文件路径
     * @return String 文件名，不包含后缀名
     */
    private static String getFileName(String infile){
        File fileName = new File(infile);
        String prefix= fileName.getName().substring(0,fileName.getName().lastIndexOf("."));
        return prefix;
    }

    /**
     * 根据命令行执行转码命令
     * @param commandLine 需要执行的命令行
     * @return boolean 命令执行成功返回true;否则，返回false
     */
    private static boolean transfer(String commandLine) {
        try {
            Log.writeFile("转码命令：" + commandLine);
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(commandLine);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ( (line = br.readLine()) != null) {
                Log.writeFile(line);
            }
            int exitVal = proc.waitFor();
            Log.writeFile("Process exitValue: " + exitVal);
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
        return true;
    }

}