package edu.zhku.hessian.auth;

import org.springframework.remoting.caucho.HessianServiceExporter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Arvin on 2016/6/7.
 */
public class MyHessianServiceExporter extends HessianServiceExporter {

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("QueryString： " + request.getQueryString());
        System.out.println("RequestURI: " + request.getRequestURI());
        System.out.println("RequestURL: " + request.getRequestURL());

        // 这里做权限验证。你可以使用Token等等之类的机制
        // 当然你也可以在这里进行请求控制，比如访问频率等
        // 这里做简单的示例， 双方协商好

        String token = request.getHeader("token");
        System.out.println("Token: " + token);

        if (null == token || "".equals(token.trim())) {
            //response.sendError(5001000, "INVALID_TOKEN");
            throw new ServletException(new UnAuthException());
        }

        super.handleRequest(request, response);
    }
}
