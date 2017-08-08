import java.io.File;

public class DirIO {

    /**
     * 检查文件夹路径是否存在，如果不存在，则进行创建
     * @param dirPath String 文件夹路径
     * @return boolean 创建文件夹失败则返回false
     */
    public static boolean checkDir(String dirPath){
        try{
            File encodingDir = new File(dirPath);
            if( !encodingDir.isDirectory() ){
                encodingDir.mkdirs();
            }
            return true;
        } catch (Exception e) {
            Log.writeFile(e.toString());
            return false;
        }
    }
}
