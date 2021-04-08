package com.atlantis.bf.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.jhipster.config.cache.PrefixedKeyGenerator;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {
    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.atlantis.bf.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.atlantis.bf.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.atlantis.bf.domain.User.class.getName());
            createCache(cm, com.atlantis.bf.domain.Authority.class.getName());
            createCache(cm, com.atlantis.bf.domain.User.class.getName() + ".authorities");
            createCache(cm, com.atlantis.bf.domain.Client.class.getName());
            createCache(cm, com.atlantis.bf.domain.Client.class.getName() + ".ventes");
            createCache(cm, com.atlantis.bf.domain.Client.class.getName() + ".reservations");
            createCache(cm, com.atlantis.bf.domain.Products.class.getName());
            createCache(cm, com.atlantis.bf.domain.Products.class.getName() + ".reservations");
            createCache(cm, com.atlantis.bf.domain.TypeProduit.class.getName());
            createCache(cm, com.atlantis.bf.domain.TypeProduit.class.getName() + ".products");
            createCache(cm, com.atlantis.bf.domain.EntreeStock.class.getName());
            createCache(cm, com.atlantis.bf.domain.EntreeStock.class.getName() + ".products");
            createCache(cm, com.atlantis.bf.domain.Ventes.class.getName());
            createCache(cm, com.atlantis.bf.domain.Ventes.class.getName() + ".products");
            createCache(cm, com.atlantis.bf.domain.Depenses.class.getName());
            createCache(cm, com.atlantis.bf.domain.TypeDepense.class.getName());
            createCache(cm, com.atlantis.bf.domain.TypeDepense.class.getName() + ".depenses");
            createCache(cm, com.atlantis.bf.domain.Reservation.class.getName());
            createCache(cm, com.atlantis.bf.domain.Products.class.getName() + ".stocks");
            createCache(cm, com.atlantis.bf.domain.Products.class.getName() + ".ventes");
            createCache(cm, com.atlantis.bf.domain.Fournisseur.class.getName());
            createCache(cm, com.atlantis.bf.domain.Fournisseur.class.getName() + ".stocks");
            createCache(cm, com.atlantis.bf.domain.Products.class.getName() + ".ligneStocks");
            createCache(cm, com.atlantis.bf.domain.Products.class.getName() + ".ligneVentes");
            createCache(cm, com.atlantis.bf.domain.Products.class.getName() + ".ligneReservations");
            createCache(cm, com.atlantis.bf.domain.EntreeStock.class.getName() + ".ligneEntreeStocks");
            createCache(cm, com.atlantis.bf.domain.Ventes.class.getName() + ".lignesVentes");
            createCache(cm, com.atlantis.bf.domain.LignesVentes.class.getName());
            createCache(cm, com.atlantis.bf.domain.LigneEntreeStock.class.getName());
            createCache(cm, com.atlantis.bf.domain.LignesReservation.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
