package cn.arvin.example.cache.service.impl;

import cn.arvin.example.cache.domain.User;
import cn.arvin.example.cache.query.UserQuery;
import cn.arvin.example.cache.service.UserService;

import java.util.*;

/**
 * Created by Arvin on 2016/7/10.
 */
public class UserServiceMemoryImpl implements UserService {

    private Set<User> users = new HashSet<>();

    private volatile int currentId = 0;

    private Integer nextId() {
        return currentId++;
    }

    @Override
    public User save(User user) {
        user.setId(nextId());
        users.add(user);
        return user;
    }

    @Override
    public int delete(Integer userId) {
        User user = new User();
        user.setId(userId);
        boolean isDel = users.remove(user);
        return isDel ? 1 : 0;
    }

    @Override
    public int update(User user) {
        boolean isExists = users.remove(user);
        if (isExists) {
            users.add(user);
            return 1;
        }
        return 0;
    }

    @Override
    public User findById(Integer userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) return user;
        }
        return null;
    }

    @Override
    public List<User> query(UserQuery query) {
        // 获取符合条件的结果集
        List<User> accessList = getAccessList(query);
        return pageResult(Collections.synchronizedList(accessList), query.getCurPage(), query.getPageSize());
    }

    /**
     * 获取分页结果
     */
    private List<User> pageResult(List<User> list, Integer curPage, Integer pageSize) {
        if (curPage == null || pageSize == null) return list;
        int cp = curPage < 1 ? 1 : curPage;
        int ps = pageSize < 1 ? 1 : pageSize;

        int total = list.size();

        if (total < pageSize) {
            if (cp == 1) return new ArrayList<>(list);
            else return new ArrayList<>();
        }
        // 检测是否指定页码存在
        int totalPage = total % ps == 0 ? total / ps : total / ps + 1;

        if (curPage > totalPage) {
            return new ArrayList<>();
        } else {
            int begIndex = (cp - 1) * ps;
            int endIndex = begIndex + ps;
            endIndex = endIndex > total - 1 ? total - 1 : endIndex;
            return list.subList(begIndex, endIndex);
        }
    }

    private List<User> getAccessList(UserQuery query) {
        List<User> list = new ArrayList<>();
        for (User user : users) {
            boolean idAllow = query.getId() == null || user.getId().equals(query.getId());
            if (!idAllow) continue;

            boolean nameAllow = query.getName() == null || query.getName().equals(user.getName());
            if (!nameAllow) continue;

            boolean ageAllow = query.getAge() == null || query.getAge().equals(user.getAge());
            if (!ageAllow) continue;

            list.add(user);
        }
        return list;
    }
}
