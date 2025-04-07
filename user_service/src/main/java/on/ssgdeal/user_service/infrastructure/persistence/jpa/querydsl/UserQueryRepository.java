package on.ssgdeal.user_service.infrastructure.persistence.jpa.querydsl;

import on.ssgdeal.user_service.application.dto.SearchUserDto;
import on.ssgdeal.user_service.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface UserQueryRepository {

    Page<User> searchUser(SearchUserDto requestDto);
}
