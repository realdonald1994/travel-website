package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private RouteService service =  new RouteServiceImpl();
    FavoriteService favoriteService = new FavoriteServiceImpl();
    /**
     * page query
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. parameter
        String currentPage_str = request.getParameter("currentPage");
        String pageSize_str = request.getParameter("pageSize");
        String cid_str = request.getParameter("cid");
        String rname = request.getParameter("rname");
        rname = new String(rname.getBytes("iso-8859-1"),"utf-8");

        //2. process parameter
        int cid =0;
        if(cid_str != null && cid_str.length() > 0 && !"null".equals(cid_str)){
            cid=Integer.parseInt(cid_str);
        }

        int currentPage = 0;
        if(currentPage_str != null && currentPage_str.length() > 0){
            currentPage=Integer.parseInt(currentPage_str);
        }else{
            currentPage =1;
        }

        int pageSize = 0;
        if(pageSize_str != null && pageSize_str.length() > 0){
            pageSize=Integer.parseInt(pageSize_str);
        }else{
            pageSize = 5;
        }


        //3. call service
        PageBean<Route> pb = service.pageQuery(cid, currentPage, pageSize,rname);

        //4. pagebean->json->return
        writeValue(pb,response);
    }

    /**
     * rid->route detail
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. rid
        //2. service->route
        //json
        String rid = request.getParameter("rid");
        Route route = service.findOne(rid);
        writeValue(route,response);
    }

    /**
     *  favorite:user->route
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        User user = (User)request.getSession().getAttribute("user");
        int uid;
        if(user ==null){
            // not login
            uid =0;
        }else{
            // login
            uid =user.getUid();
        }

        boolean flag = favoriteService.isFavorite(rid, uid);
        //json
        writeValue(flag,response);

    }

    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        User user = (User)request.getSession().getAttribute("user");
        int uid;
        if(user ==null){
            // not login
            return;
        }else{
            // login
            uid =user.getUid();
        }

        // add favorite
        favoriteService.add(rid,uid);
    }

    }
