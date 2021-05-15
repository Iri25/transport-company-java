package persistence;

import model.User;
import model.validators.ValidationException;

public interface UserRepository extends Repository<Integer, User> {

    /**
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     *          or null - if there is no entity with the given id
     * @throws IllegalArgumentException
     *                  if id is null.
     */
    User findOne(Integer id);

    /**
     *
     * @param username the username of the entity to be returned
     *                 username must not be null
     * @param password the entity with the specified password
     *                 password must not be null
     * @return the entity with the specified username and password
     *         or null - if there is no entity with the given username and password
     */
    User findUsernamePassword(String username, String password);

    /**
     *
     * @return all entities
     */
    Iterable<User> findAll();

    /**
     *
     * @param entity
     *         entity must be not null
     * @throws ValidationException
     *            if the entity is not valid
     * @throws IllegalArgumentException
     *             if the given entity is null.     *
     */
    void save(User entity);


    /**
     *  removes the entity with the specified id
     * @param id
     *      id must be not null
     * @throws IllegalArgumentException
     *                   if the given id is null.
     */
    void delete(Integer id);

    /**
     *
     * @param entity
     *          entity must not be null
     * @throws IllegalArgumentException
     *             if the given entity is null.
     * @throws ValidationException
     *             if the entity is not valid.
     */
    void update(User entity);

}

