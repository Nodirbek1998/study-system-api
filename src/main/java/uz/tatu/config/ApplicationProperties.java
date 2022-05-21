package uz.tatu.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * Properties specific to Studysystem.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application")
@ConstructorBinding
public class ApplicationProperties {

    private final Settings settings;

    public ApplicationProperties(Settings settings) {
        this.settings = settings;
    }

    public ApplicationProperties.Settings getApplicationSettingDto() {
        return this.settings;
    }

    @Data
    @NoArgsConstructor
    public static class Settings {
        private String filepath;
        private String chathost;
        private String weatherapikey;
        private String serverhost;
        private String serverredirecthost;
        private String exchangeurl;
        private String internalJurnalForCard;
        private Long receiverJurnalId;

        private String mailFrom;
    }

}
