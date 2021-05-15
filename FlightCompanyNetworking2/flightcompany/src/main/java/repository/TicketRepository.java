package repository;

import domain.Ticket;
import domain.validators.ValidationException;


public interface TicketRepository extends Repository<Integer, Ticket> {

    /**
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     *          or null - if there is no entity with the given id
     * @throws IllegalArgumentException
     *                  if id is null.
     */
    Ticket findOne(Integer id);

    /**
     *
     * @return all entities
     */
    Iterable<Ticket> findAll();

    /**
     *
     * @param entity
     *         entity must be not null
     * @throws ValidationException
     *            if the entity is not valid
     * @throws IllegalArgumentException
     *             if the given entity is null.     *
     */
    void save(Ticket entity);


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
    void update(Ticket entity);

}

