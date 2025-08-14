package com.cbk.user_admin_api.application.service;

import com.cbk.user_admin_api.application.query.UserPagination;
import com.cbk.user_admin_api.application.query.UserPaginationQuery;
import com.cbk.user_admin_api.domain.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryService {
    private final UserQueryRepository userQueryRepository;

    public List<UserPagination> readUsers(UserPaginationQuery query) {
        return userQueryRepository.findUsers(query.getPage(), query.getSize())
                .stream()
                .map(it -> UserPagination.of(it.getUserId(),
                                             it.getName(),
                                             it.getSsn(),
                                             it.getPhoneNumber(),
                                             it.getAddress()))
                .toList();
    }
}
