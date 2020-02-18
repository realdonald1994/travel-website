package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {
    /**
     * cid->total count
     */
    public int findTotalCount(int cid,String rname);
    /**
     * cid,currentPage pageSize ->list
     */
    public List<Route> findByPage(int cid, int currentPage, int pageSize,String rname);

    public Route findOne(int rid);
}
