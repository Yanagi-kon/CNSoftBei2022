package com.l024.webmanager.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

//token工具类
public class TokenUtil {

    //从token中获取用户编号
    public static String getTokenUserNumber(){
        String token = getRequest().getHeader("X-Token");// 从 http 请求头中取出 token
        if(!StringUtil.isEmpty(token)){
            DecodedJWT decode = JWT.decode(token);
            if(decode!=null&&decode.getAudience()!=null){
                return decode.getAudience().get(0);
            }
        }
        return "";
    }

    private static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : requestAttributes.getRequest();
    }

}
