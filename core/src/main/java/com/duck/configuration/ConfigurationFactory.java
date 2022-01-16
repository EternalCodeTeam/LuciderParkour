package com.duck.configuration;

import com.duck.LuciderParkour;
import com.duck.configuration.common.GeneralConfiguration;
import com.duck.data.flat.FlatDataManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import lombok.Getter;
import lombok.SneakyThrows;
import net.dzikoysk.cdn.Cdn;
import net.dzikoysk.cdn.CdnFactory;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;
import panda.std.function.ThrowingFunction;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

@Getter
public class ConfigurationFactory {

    private GeneralConfiguration generalConfiguration;


    private final FlatDataManager flatDataManager = LuciderParkour.getInstance().getFlatDataManager();


    private final Cdn cdn = CdnFactory
            .createYamlLike()
            .getSettings()
            .build();



    @SneakyThrows
    public void initConfigs(Plugin plugin) throws IOException {
        flatDataManager.buildFileConfigurationModel(plugin.getDataFolder());

        this.generalConfiguration = generate(
                GeneralConfiguration.class,
                flatDataManager.getGeneralConfigurationFile(),
                cdn);


    }




    @SneakyThrows
    public <T extends Serializable> T generate(Class<T> configurationClass, File file, Cdn cdn) {
        Validate.notNull(file, "File can't be null!");


        Resource resource = Source.of(file);
        T load = cdn.load(resource, configurationClass)
                .orElseThrow(ThrowingFunction.identity());

        cdn.render(load, resource);

        return load;
    }



    public <T> T readObjectFromJackson(File file, Class<T> clazz) throws IOException {
        ObjectMapper readMapper = new ObjectMapper(new YAMLFactory());

        return readMapper.readValue(file, clazz);
    }

    public void writeObjectToJackson(File file, Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mapper.writeValue(file, object);
    }
}
