public class Main {

    public static void main(String[] args) {
        //初始化参数
        //转码文件存放目录
        final String DIRNAME = "I:\\工作相关\\TESTMOVIE\\before\\";
        //ffmpeg程序存放路径
        final String FFMPEG_PATH = "I:\\\\工作相关\\\\ffmpeg-20170724-03a9e6f-win64-static\\\\bin\\\\ffmpeg.exe";
        //首先获取一条需要进行转码的视频路径
        String beforeTranscodeFile = "";
        String tempFileName = "";
        do{
            tempFileName = BeforeTranscodeDir.getSingleFileName(DIRNAME);
            if(tempFileName != ""){
                beforeTranscodeFile = DIRNAME + tempFileName;
                //进行转码操作
                MediocreExecJavac.transcode(beforeTranscodeFile);
            } else {
                Log.writeFile("等待5秒后重试");
                try{
                    Thread.currentThread().sleep(5000);
                }catch(InterruptedException ie){
                    ie.printStackTrace();
                }
            }
        } while(true);
    }

}
