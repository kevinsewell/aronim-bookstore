package com.aronim.bookstore.infrastructure.persistence;

import com.aronim.bookstore.domain.model.Role;
import com.aronim.bookstore.domain.model.RoleId;
import com.aronim.bookstore.domain.repository.RoleRepository;
import com.aronim.bookstore.infrastructure.persistence.entity.RoleEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;

    public RoleRepositoryImpl(RoleJpaRepository roleJpaRepository) {
        this.roleJpaRepository = roleJpaRepository;
    }

    @Override
    public Role save(Role role) {
        RoleEntity entity = toEntity(role);
        entity = roleJpaRepository.save(entity);
        return toDomain(entity);
    }

    @Override
    public Optional<Role> findById(RoleId id) {
        return roleJpaRepository.findById(id.getValue())
                .map(this::toDomain);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleJpaRepository.findByName(name)
                .map(this::toDomain);
    }

    @Override
    public List<Role> findAll() {
        return roleJpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Role role) {
        roleJpaRepository.deleteById(role.getId().getValue());
    }

    @Override
    public boolean existsByName(String name) {
        return roleJpaRepository.existsByName(name);
    }

    /**
     * Converts a domain Role object to a RoleEntity for persistence
     *
     * @param role The domain Role object
     * @return A RoleEntity for persistence
     */
    private RoleEntity toEntity(Role role) {
        RoleEntity entity = new RoleEntity();
        entity.setId(role.getId().getValue());
        entity.setName(role.getName());
        entity.setDescription(role.getDescription());
        return entity;
    }

    /**
     * Converts a RoleEntity to a domain Role object
     *
     * @param entity The RoleEntity from the database
     * @return A domain Role object
     */
    private Role toDomain(RoleEntity entity) {
        return Role.create(
                new RoleId(entity.getId()),
                entity.getName(),
                entity.getDescription()
        );
    }
}
