package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {

    private CategoryDao dao =  new CategoryDaoImpl();
    /**
     *
     * @return
     */
    @Override
    public List<Category> findAll() {

        //1. search from redis
        Jedis jedis = JedisUtil.getJedis();
        Set<String> categorys = jedis.zrange("category", 0, -1);
        List<Category> list = null;
        //2 . list is null or not
        if(categorys==null || categorys.size()==0){
            //null -> first search ->mysql->redis
            list = dao.findAll();
            for(int i=0;i<list.size();i++){
                jedis.zadd("category",list.get(i).getCid(),list.get(i).getCname());
            }
        }else{
            //not null-> return from redis
            list = new ArrayList<Category>();
            for(String name:categorys){
                Category category = new Category();
                category.setCname(name);
                list.add(category);
            }
        }

        return list;
    }
}
