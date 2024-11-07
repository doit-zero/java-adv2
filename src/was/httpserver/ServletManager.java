package was.httpserver;

import was.httpserver.servlet.InternalErrorServlet;
import was.httpserver.servlet.NotFoundServlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * servlet을 보관하고 request와 response를 받아 서블릿을 실행시킨다.
 * */
public class ServletManager {

    // String에는 request.getPath() 로 경로 값과 그 요청을 처리할 수 있는 HttpServlet을 servletMap에 담아둠
    private final Map<String,HttpServlet> servletMap = new HashMap<>();
    private HttpServlet defaultServlet;
    private HttpServlet notFoundErrorServlet = new NotFoundServlet();
    private HttpServlet internalErrorServlet = new InternalErrorServlet();

    public ServletManager() {
    }

    public void add(String path,HttpServlet servlet){
        servletMap.put(path,servlet);
    }

    public void setDefaultServlet(HttpServlet defaultServlet){
        this.defaultServlet = defaultServlet;
    }

    public void setNotFoundErrorServlet(HttpServlet notFoundErrorServlet) {
        this.notFoundErrorServlet = notFoundErrorServlet;
    }

    public void setInternalErrorServlet(HttpServlet internalErrorServlet) {
        this.internalErrorServlet = internalErrorServlet;
    }

    public void execute(HttpRequest request,HttpResponse response) throws IOException {
        try{
            HttpServlet servlet = servletMap.getOrDefault(request.getPath(), defaultServlet);
            if(servlet == null){
                throw new PageNotFoundException("요청하신 " + request.getPath() +"는 찾을 수 없습니다.");
            }
            servlet.service(request,response);
        } catch (PageNotFoundException e){
            e.printStackTrace();
            notFoundErrorServlet.service(request,response);
        }catch (IOException e) {
            e.printStackTrace();
            internalErrorServlet.service(request,response);
        }
    }
}
