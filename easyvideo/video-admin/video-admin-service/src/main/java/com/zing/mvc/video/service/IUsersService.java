package com.zing.mvc.video.service;

import com.zing.mvc.video.pojo.User;
import com.zing.mvc.video.utils.PagedResult;

public interface IUsersService {

    PagedResult queryUsers(User user, Integer page, Integer pageSize);

}
