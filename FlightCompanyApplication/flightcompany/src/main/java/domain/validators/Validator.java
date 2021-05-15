package domain.validators;

/**
 *
 * @param <T>
 */
public interface Validator<T> {

    /**
     * validate the entity
     * @param entity
     * @throws ValidationException
     */
    void validate(T entity) throws ValidationException;
}