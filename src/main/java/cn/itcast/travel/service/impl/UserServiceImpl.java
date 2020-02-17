package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    private UserDao dao = new UserDaoImpl();
    /**
     * register
     * @param user
     * @return
     */
    @Override
    public boolean register(User user) {
        //search user exist or not
        User u = dao.findByUsername(user.getUsername());
        if(u!=null){
            return false;
        }
        //save user
        //set active code
        user.setCode(UuidUtil.getUuid());
        //set active status
        user.setStatus("N");
        dao.save(user);

        //3. validation email
        String content = "<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>click to active【黑马旅游网】</a>";
        MailUtils.sendMail(user.getEmail(),content,"active email");
        return true;



    }

    @Override
    public boolean active(String code) {
        //1. search user in dv
        User user = dao.findByCode(code);
        if(user !=null){
            //2. call dao function for modify active status
            dao.updateStatus(user);
            return true;
        }else{

            return false;
        }
    }

    @Override
    public User login(User user) {

        return dao.findByUsernameAndPassword( user.getUsername(),user.getPassword());

    }
}
