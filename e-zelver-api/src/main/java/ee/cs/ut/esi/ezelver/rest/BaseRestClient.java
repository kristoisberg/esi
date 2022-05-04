package ee.cs.ut.esi.ezelver.rest;

import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Map;
import java.util.Set;

public abstract class BaseRestClient {
    private final RestTemplate restTemplate;
    private final Validator validator;
    private final String serviceUrl;

    public BaseRestClient(RestTemplate restTemplate, Validator validator, String serviceUrl) {
        this.restTemplate = restTemplate;
        this.validator = validator;
        this.serviceUrl = serviceUrl;
    }

    protected <TResult> TResult get(String endpoint, Class<TResult> resultType, Object... params) {
        return restTemplate.getForObject(getEndpointUrl(endpoint), resultType, params);
    }

    protected <TResult> TResult get(String endpoint, Class<TResult> resultType, Map<String, ?> params) {
        return restTemplate.getForObject(getEndpointUrl(endpoint), resultType, params);
    }

    protected <TEntity> void post(String endpoint, TEntity entity, Object... params) {
        validate(entity);
        restTemplate.postForLocation(getEndpointUrl(endpoint), entity, params);
    }

    protected <TEntity> void post(String endpoint, TEntity entity, Map<String, ?> params) {
        validate(entity);
        restTemplate.postForLocation(getEndpointUrl(endpoint), entity, params);
    }

    protected <TEntity, TResult> TResult post(String endpoint,
                                              TEntity entity,
                                              Class<TResult> resultType,
                                              Object... params) {
        validate(entity);
        return restTemplate.postForObject(getEndpointUrl(endpoint), entity, resultType, params);
    }

    protected <TEntity, TResult> TResult post(String endpoint,
                                              TEntity entity,
                                              Class<TResult> resultType,
                                              Map<String, ?> params) {
        validate(entity);
        return restTemplate.postForObject(getEndpointUrl(endpoint), entity, resultType, params);
    }

    protected <TEntity> void put(String endpoint, TEntity entity, Object... params) {
        validate(entity);
        restTemplate.put(getEndpointUrl(endpoint), entity, params);
    }

    protected <TEntity> void put(String endpoint, TEntity entity, Map<String, ?> params) {
        validate(entity);
        restTemplate.put(getEndpointUrl(endpoint), entity, params);
    }

    protected void delete(String endpoint, Object... params) {
        restTemplate.delete(getEndpointUrl(endpoint), params);
    }

    protected void delete(String endpoint, Map<String, ?> params) {
        restTemplate.delete(getEndpointUrl(endpoint), params);
    }

    private <TEntity> void validate(TEntity entity) {
        Set<ConstraintViolation<TEntity>> violations = validator.validate(entity);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private String getEndpointUrl(String endpoint) {
        return serviceUrl + "/" + endpoint;
    }
}
