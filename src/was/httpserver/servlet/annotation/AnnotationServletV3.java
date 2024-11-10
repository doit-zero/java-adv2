package was.httpserver.servlet.annotation;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.HttpServlet;
import was.httpserver.PageNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationServletV3 implements HttpServlet {

    private final List<Object> controllers;
    private Map<String,Object> methodMap = new HashMap<>();

    public AnnotationServletV3(List<Object> controllers) {
        this.controllers = controllers;
        initializeMethodMap(controllers);
    }

    private void initializeMethodMap(List<Object> controllers) {
        for (Object controller : controllers) {
            Class<?> aClass = controller.getClass();
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                if(method.isAnnotationPresent(Mapping.class)){
                    Mapping annotation = method.getAnnotation(Mapping.class);
                    String path = annotation.value();
                    if(methodMap.containsKey(path)){
                        Object getController = methodMap.get(path);
                        throw new IllegalArgumentException(path + "에는 이미 " + getController.getClass().getName()  +  "은 이미 등록 되어 있습니다.");
                    }
                    methodMap.put(path,controller);
                }
            }
        }

    }

    @Override
    public void service(HttpRequest request, HttpResponse response)  {
        String path = request.getPath();

        for (Object controller : controllers) {
            Class<?> aClass = controller.getClass();
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                if(method.isAnnotationPresent(Mapping.class)){
                    Mapping annotation = method.getAnnotation(Mapping.class);
                    String value = annotation.value();
                    if(value.equals(path)){
                        invoke(request, response, controller, method);
                        return;
                    }
                }
            }
        }
        throw new PageNotFoundException("request = " + path);
    }

    private void invoke(HttpRequest request, HttpResponse response, Object controller, Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];

        for (int i =0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] == HttpRequest.class){
                args[i] = request;
            } else if (parameterTypes[i]  == HttpResponse.class){
                args[i] = response;
            } else {
                // 그 외는 예를 던지도록 만든다.
                throw new IllegalArgumentException("Unsupported parameterType: " + parameterTypes[i]);
            }
        }

        try {
            // 해당 객체와 ars를 method.invoke로 실행시킨다.
            method.invoke(controller,args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
