/**
 * 创建对象后，根据选值不同，返回不同的转码参数
 */
public class EncodeParms {
    public String name = "";
    public String vcodec = "";
    public String acodec = "";
    public int ab = 0;
    public int vb = 0;
    public int width = 0;
    public int height = 0;
    public float fps = 0.0f;
    public String resolution = "";
    public int asamplerate = 0;

    /**
     *  根据输入值的不同，初始化对象的转码参数：0:1080P；1:720P；2:640P；3:480P
     * @param style
     */
    public void setEncodeParms(int style){
        switch (style){
            case 0 :
                name = "1080P";
                resolution = "1920x1080";
                width = 1920;
                height = 1080;
                vcodec = "libx264";
                acodec = "aac";
                vb = 1500;
                ab = 128;
                asamplerate = 44100;
                fps = 30.00f;
                break;
            case 1:
                name = "720P";
                resolution = "1280x720";
                width = 1280;
                height = 720;
                vcodec = "libx264";
                acodec = "aac";
                vb = 1000;
                ab = 96;
                asamplerate = 44100;
                fps = 30.00f;
                break;
            case 2:
                name = "640P";
                resolution = "960x640";
                width = 960;
                height = 640;
                vcodec = "libx264";
                acodec = "aac";
                vb = 500;
                ab = 80;
                asamplerate = 44100;
                fps = 30.00f;
                break;
            default:
                name = "480P";
                resolution = "640x480";
                width = 640;
                height = 480;
                vcodec = "libx264";
                acodec = "aac";
                vb = 400;
                ab = 64;
                asamplerate = 44100;
                fps = 30.00f;
                break;
        }
    }
}
