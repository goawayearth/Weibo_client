package util;
/*
用于网络访问
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FlickrFetcher {

    public static final String TAG = "FlickrFetcher";

    /**
     *
     * @param urlSpec
     * @return
     * @throws Exception
     */
    public byte[] getUrlBytes(String urlSpec) throws Exception{

        URL url = new URL(urlSpec);
        // 与URL的网址建立一个连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        /*
        下面是禁止重定向，然后自己来处理重定向来获得更大的控制权
         */
        connection.setInstanceFollowRedirects(false);
        // 获取响应头中 location 字段的值
        String redirect = connection.getHeaderField("Location");
        if (redirect != null){
            connection = (HttpURLConnection)new URL(redirect).openConnection();
        }

        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // in是http连接时候的输入流
            InputStream in = connection.getInputStream();

            // 连接服务器没成功，抛出异常
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage() + " :with "+urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            /*
            in.read函数是读取服务器返回的字符串，并放在了buffer中，返回的是读取了byte的数量
             */
            while((bytesRead = in.read(buffer)) > 0){
                // 将从输入流中读取的数据写入的内存的ByteArrayOutputStream中
                out.write(buffer,0,bytesRead);
            }
            // 关闭输出流
            out.close();
            // 将缓存中的数控以字节数组的形式返回
            return out.toByteArray();
        }
        finally {
            // 最后都是关闭连接
            connection.disconnect();
        }
    }

    /**
     * 返回从URL读取的字节数组
     * @param urlSpec
     * @return
     * @throws Exception
     */
    public String getUrlString(String urlSpec)throws Exception{
        return new String(getUrlBytes(urlSpec));
    }
}
