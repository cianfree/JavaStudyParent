package com.bxtpw.common.web.exception;

import com.bxtpw.common.exception.ErrorCode;
import com.bxtpw.common.exception.JsonAndCodeExceptionInterface;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * <pre>
 * 1. 不必在Controller中对异常进行处理，抛出即可，由此异常解析器统一控制。
 * 2. ajax请求（有@ResponseBody的Controller）发生错误，输出JSON。
 * 3. 页面请求（无@ResponseBody的Controller）发生错误，输出错误页面。
 * 4. 需要与AnnotationMethodHandlerAdapter使用同一个messageConverters
 * 5. Controller中不需要有专门处理异常的方法。
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015年10月9日 下午12:58:06
 * @since 0.1
 */
public class AnnotationHandlerMethodExceptionResolver extends ExceptionHandlerExceptionResolver {
    /**
     * 错误处理页面
     */
    private String defaultErrorView;

    public void setDefaultErrorView(String defaultErrorView) {
        this.defaultErrorView = defaultErrorView;
    }

    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {
        if (null != exception) {
            return handleErrorResponseBody(exception, response);
        }
        if (null == handlerMethod) { // 无处理方法
            return new ModelAndView(this.defaultErrorView);
        }
        Method method = handlerMethod.getMethod();
        if (null == method) { // 无处理方法
            return new ModelAndView(this.defaultErrorView);
        }
        ModelAndView returnValue = super.doResolveHandlerMethodException(request, response, handlerMethod, exception);
        ResponseBody responseBodyAnn = AnnotationUtils.findAnnotation(method, ResponseBody.class);
        RestController restControllerAnn = AnnotationUtils.findAnnotation(method.getDeclaringClass(), RestController.class);
        if (null != restControllerAnn || null != responseBodyAnn) { // Ajax 结果
            try {
                return handleResponseBody(returnValue, request, response);
            } catch (Exception e) {
                logger.debug(e);
                return handleErrorResponseBody(e, response);
            }
        } else { // 页面错误
            if (returnValue.getViewName() == null) {
                returnValue.setViewName(defaultErrorView);
            }
            return returnValue;
        }

    }

    /**
     * 处理MVC异常信息
     *
     * @param exception 异常对象
     * @param response  请求相应对象
     * @return 返回页面
     */
    private ModelAndView handleErrorResponseBody(Exception exception, HttpServletResponse response) {
        String errorCode = parseErrorCodeFromException(exception);
        String errorMsg = parseErrorMsgFromException(exception);
        printJson("{\"code\":\"" + errorCode + "\",\"msg\":" + errorMsg + "}", response);
        return null;
    }

    /**
     * 返回异常的错误信息，如果是实现了JsonExceptionInterface接口的就不需要加双引号和进行字符串的双引号转码， 否则进行双引号的转码，前后加上双引号再返回
     *
     * @param exception 要获取异常信息的错误对象
     * @return 返回错误信息
     */
    private String parseErrorMsgFromException(Exception exception) {
        if (null != exception) {
            if (exception instanceof JsonAndCodeExceptionInterface) {
                JsonAndCodeExceptionInterface exceptionInterface = (JsonAndCodeExceptionInterface) exception;
                if (exceptionInterface.isJson()) {
                    return exception.getMessage();
                }
            }
            return "\"" + exception.getMessage().replaceAll("\"", "\\\\\"") + "\"";
        }
        return null;
    }

    /**
     * 解析异常的错误码， 默认是500
     *
     * @param exception 异常对象
     * @return 返回错误码，默认是500，如果异常对象不是ErrorCodeExceptionInterface或JsonExceptionInterface的实例
     */
    private String parseErrorCodeFromException(Exception exception) {
        if (null != exception && exception instanceof JsonAndCodeExceptionInterface) {
            return ((JsonAndCodeExceptionInterface) exception).getErrorCode();
        }
        return ErrorCode.INTERNAL_500;
    }

    /**
     * response.setContentType("application/json; charset=utf-8") MIME类型为：Json
     *
     * @param json
     * @param response
     * @author 夏集球
     * @time 2015年7月26日 上午11:56:58
     * @since 0.1
     */
    protected void printJson(String json, HttpServletResponse response) {
        /**
         * 输出到客户端相关的cache,字符集设置
         */
        String ajaxEncoding = "UTF-8";
        response.setContentType("application/json;charset=" + ajaxEncoding);
        response.setCharacterEncoding(ajaxEncoding);
        response.setHeader("Charset", ajaxEncoding);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "No-cache");
        response.setDateHeader("Expires", 0);
        try {
            PrintWriter out = response.getWriter();
            out.write(json);
            out.flush();
            out.close();
        } catch (IOException e) {
            if (logger.isWarnEnabled()) { // 不支持的输入类型
                logger.warn("Could not write!", e);
            }
        }
    }

    /**
     * 处理Ajax业务结果
     *
     * @param returnValue
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     * @author 夏集球
     * @time 2015年10月9日 下午1:04:47
     * @version 0.1
     * @since 0.1
     */
    private ModelAndView handleResponseBody(ModelAndView returnValue, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ModelMap value = returnValue.getModelMap();
        HttpInputMessage inputMessage = new ServletServerHttpRequest(request);
        List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
        if (acceptedMediaTypes.isEmpty()) {
            acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
        }
        MediaType.sortByQualityValue(acceptedMediaTypes);
        Class<?> returnValueType = value.getClass(); // 返回类型
        // 获取可以处理媒体类型的转换器
        List<HttpMessageConverter<?>> messageConverters = getMessageConverters();
        if (messageConverters != null) {
            outputMediaTypeResponse(response, value, acceptedMediaTypes, returnValueType, messageConverters);
            return new ModelAndView();
        }
        if (logger.isWarnEnabled()) { // 不支持的输入类型
            logger.warn("Could not find HttpMessageConverter that supports return type [" + returnValueType + "] and " + acceptedMediaTypes);
        }
        return null;
    }

    /**
     * 输出媒体类型结果
     *
     * @param response
     * @param resultMap
     * @param acceptedMediaTypes
     * @param returnValueType
     * @param messageConverters
     * @throws IOException
     * @author 夏集球
     * @time 2015年10月9日 下午1:20:03
     * @version 0.1
     * @since 0.1
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void outputMediaTypeResponse(HttpServletResponse response, ModelMap resultMap, List<MediaType> acceptedMediaTypes, Class<?> returnValueType,
                                         List<HttpMessageConverter<?>> messageConverters) throws IOException {
        for (MediaType acceptedMediaType : acceptedMediaTypes) {
            for (HttpMessageConverter messageConverter : messageConverters) {
                if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
                    HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
                    if (1 == resultMap.size()) { // 如果只有一个模型，那么直接输出该模型
                        messageConverter.write(resultMap.values().iterator().next(), acceptedMediaType, outputMessage);
                    } else {
                        messageConverter.write(resultMap, acceptedMediaType, outputMessage);
                    }
                    return;
                }
            }
        }
    }
}
