package edu.zhku.hessian.auth;

import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianURLConnectionFactory;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Arvin on 2016/6/7.
 */
public class MyHessianConnectionFactory extends HessianURLConnectionFactory {

    @Override
    public HessianConnection open(URL url) throws IOException {
        HessianConnection connection = super.open(url);
        // 这里添加相关信息到请求，作为服务端权限的校验
        connection.addHeader("token", "TokenFromClient!");
        return connection;
    }
}
