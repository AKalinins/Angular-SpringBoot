package angularspring.ecommerce.config;

import angularspring.ecommerce.entity.Product;
import angularspring.ecommerce.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public DataRestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

        HttpMethod[] unsupportedMethods = {HttpMethod.PUT, HttpMethod.PUT, HttpMethod.DELETE};

        //Disable PUT, POST and DELETE methods for Product
        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure(((metdata, httpMethods) -> httpMethods.disable(unsupportedMethods)))
                .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(unsupportedMethods)));

        //Disable PUT, POST and DELETE methods for ProductCategory
        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure(((metdata, httpMethods) -> httpMethods.disable(unsupportedMethods)))
                .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(unsupportedMethods)));

        exposeIds(config);
    }

    private void exposeIds(RepositoryRestConfiguration configuration) {

        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        List<Class> classes = new ArrayList<>();

        for (EntityType<?> entity : entities) {
            classes.add(entity.getJavaType());
        }

        Class[] domainTypes = classes.toArray(new Class[0]);
        configuration.exposeIdsFor(domainTypes);
    }
}
