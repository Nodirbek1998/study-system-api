package uz.tatu.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, uz.tatu.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, uz.tatu.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, uz.tatu.domain.User.class.getName());
            createCache(cm, uz.tatu.domain.Authority.class.getName());
            createCache(cm, uz.tatu.domain.User.class.getName() + ".authorities");
            createCache(cm, uz.tatu.domain.StudyUsers.class.getName());
            createCache(cm, uz.tatu.domain.StudyUsers.class.getName() + ".groups");
            createCache(cm, uz.tatu.domain.StudyUsers.class.getName() + ".testAnswers");
            createCache(cm, uz.tatu.domain.StudyUsers.class.getName() + ".taskAnswers");
            createCache(cm, uz.tatu.domain.Role.class.getName());
            createCache(cm, uz.tatu.domain.StudyLogs.class.getName());
            createCache(cm, uz.tatu.domain.RoleStaticPermission.class.getName());
            createCache(cm, uz.tatu.domain.Article.class.getName());
            createCache(cm, uz.tatu.domain.Images.class.getName());
            createCache(cm, uz.tatu.domain.Files.class.getName());
            createCache(cm, uz.tatu.domain.Subjects.class.getName());
            createCache(cm, uz.tatu.domain.Subjects.class.getName() + ".groups");
            createCache(cm, uz.tatu.domain.Units.class.getName());
            createCache(cm, uz.tatu.domain.Groups.class.getName());
            createCache(cm, uz.tatu.domain.Groups.class.getName() + ".studyUsers");
            createCache(cm, uz.tatu.domain.Groups.class.getName() + ".subjects");
            createCache(cm, uz.tatu.domain.Tests.class.getName());
            createCache(cm, uz.tatu.domain.TestQuestion.class.getName());
            createCache(cm, uz.tatu.domain.TestAnswer.class.getName());
            createCache(cm, uz.tatu.domain.TestAnswer.class.getName() + ".studyUsers");
            createCache(cm, uz.tatu.domain.Task.class.getName());
            createCache(cm, uz.tatu.domain.TaskAnswer.class.getName());
            createCache(cm, uz.tatu.domain.TaskAnswer.class.getName() + ".studyUsers");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
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
