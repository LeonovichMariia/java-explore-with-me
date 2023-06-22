package ru.practicum.ewmmain.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmain.exception.NotFoundException;
import ru.practicum.ewmmain.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    default User validateUser(Long userId) {
        return findById(userId).orElseThrow(() -> new NotFoundException(
                "Пользователь с id" + userId + "не найден"));
    }

    Page<User> findUsersByIdsIn(Long[] ids, Pageable pageable);
}
